/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.network.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.IDataSerializer;

public class GenericDataManager {
  private final Map<Integer, GenericDataManager.DataEntry<?>> entries = Maps.newHashMap();
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private boolean empty = true;
  private boolean dirty;
  private int nextId;

  public <T> DataParameter<T> register(IDataSerializer<T> serializer, T value) {
    int i = this.nextId++;
    if (i > 254) {
      throw new IllegalArgumentException(
          "Data value id is too big with " + i + "! (Max is " + 254 + ")");
    } else if (this.entries.containsKey(i)) {
      throw new IllegalArgumentException("Duplicate id value for " + i + "!");
    } else if (DataSerializers.getSerializerId(serializer) < 0) {
      throw new IllegalArgumentException(
          "Unregistered serializer " + serializer + " for " + i + "!");
    } else {
      DataParameter<T> parameter = new DataParameter<>(i, serializer);
      this.setEntry(parameter, value);
      return parameter;
    }
  }

  private <T> void setEntry(DataParameter<T> key, T value) {
    GenericDataManager.DataEntry<T> dataentry = new GenericDataManager.DataEntry<>(key, value);
    this.lock.writeLock().lock();
    this.entries.put(key.getId(), dataentry);
    this.empty = false;
    this.lock.writeLock().unlock();
  }

  @SuppressWarnings("unchecked")
  private <T> GenericDataManager.DataEntry<T> getEntry(DataParameter<T> key) {
    this.lock.readLock().lock();

    GenericDataManager.DataEntry<T> dataentry;
    try {
      dataentry = (GenericDataManager.DataEntry<T>) this.entries.get(key.getId());
    } catch (Throwable throwable) {
      CrashReport crashreport =
          CrashReport.makeCrashReport(throwable, "Getting synched entity data");
      CrashReportCategory crashreportcategory = crashreport.makeCategory("Synched entity data");
      crashreportcategory.addDetail("Data ID", key);
      throw new ReportedException(crashreport);
    } finally {
      this.lock.readLock().unlock();
    }

    return dataentry;
  }

  public <T> T get(DataParameter<T> key) {
    return this.getEntry(key).getValue();
  }

  public <T> void set(DataParameter<T> key, T value) {
    GenericDataManager.DataEntry<T> dataentry = this.getEntry(key);
    if (ObjectUtils.notEqual(value, dataentry.getValue())) {
      dataentry.setValue(value);
      dataentry.setDirty(true);
      this.dirty = true;
    }

  }

  public boolean isDirty() {
    return this.dirty;
  }

  public static void writeEntries(List<GenericDataManager.DataEntry<?>> entries, PacketBuffer buf)
      throws IOException {
    if (entries != null) {
      for (GenericDataManager.DataEntry<?> entry : entries) {
        writeEntry(buf, entry);
      }
    }
    buf.writeByte(255);
  }

  @Nullable
  public List<GenericDataManager.DataEntry<?>> getDirty() {
    List<GenericDataManager.DataEntry<?>> list = null;
    if (this.dirty) {
      this.lock.readLock().lock();

      for (GenericDataManager.DataEntry<?> dataentry : this.entries.values()) {
        if (dataentry.isDirty()) {
          dataentry.setDirty(false);
          if (list == null) {
            list = Lists.newArrayList();
          }

          list.add(dataentry.copy());
        }
      }

      this.lock.readLock().unlock();
    }

    this.dirty = false;
    return list;
  }

  @Nullable
  public List<GenericDataManager.DataEntry<?>> getAll() {
    List<GenericDataManager.DataEntry<?>> list = null;
    this.lock.readLock().lock();

    for (GenericDataManager.DataEntry<?> dataentry : this.entries.values()) {
      if (list == null) {
        list = Lists.newArrayList();
      }

      list.add(dataentry.copy());
    }

    this.lock.readLock().unlock();
    return list;
  }

  private static <T> void writeEntry(PacketBuffer buf, GenericDataManager.DataEntry<T> entry)
      throws IOException {
    DataParameter<T> dataparameter = entry.getKey();
    int i = DataSerializers.getSerializerId(dataparameter.getSerializer());
    if (i < 0) {
      throw new EncoderException("Unknown serializer type " + dataparameter.getSerializer());
    } else {
      buf.writeByte(dataparameter.getId());
      buf.writeVarInt(i);
      dataparameter.getSerializer().write(buf, entry.getValue());
    }
  }

  @Nullable
  public static List<GenericDataManager.DataEntry<?>> readEntries(PacketBuffer buf)
      throws IOException {
    List<GenericDataManager.DataEntry<?>> list = null;

    int id;
    while ((id = buf.readUnsignedByte()) != 255) {
      if (list == null) {
        list = Lists.newArrayList();
      }

      int j = buf.readVarInt();
      IDataSerializer<?> idataserializer = DataSerializers.getSerializer(j);
      if (idataserializer == null) {
        throw new DecoderException("Unknown serializer type " + j);
      }

      list.add(makeDataEntry(buf, id, idataserializer));
    }

    return list;
  }

  private static <T> GenericDataManager.DataEntry<T> makeDataEntry(PacketBuffer bufferIn, int idIn,
      IDataSerializer<T> serializerIn) {
    return new GenericDataManager.DataEntry<>(serializerIn.createKey(idIn),
        serializerIn.read(bufferIn));
  }

  public void setEntryValues(List<GenericDataManager.DataEntry<?>> entriesIn) {
    this.lock.writeLock().lock();

    for (GenericDataManager.DataEntry<?> dataentry : entriesIn) {
      GenericDataManager.DataEntry<?> dataentry1 = this.entries.get(dataentry.getKey().getId());
      if (dataentry1 != null) {
        this.setEntryValue(dataentry1, dataentry);
      }
    }

    this.lock.writeLock().unlock();
    this.dirty = true;
  }

  @SuppressWarnings("unchecked")
  private <T> void setEntryValue(GenericDataManager.DataEntry<T> target,
      GenericDataManager.DataEntry<?> source) {
    if (!Objects.equals(source.key.getSerializer(), target.key.getSerializer())) {
      throw new IllegalStateException(String.format(
          "Invalid entity data item type for field %d: old=%s(%s), new=%s(%s)",
          target.key.getId(), target.value, target.value.getClass(), source.value,
          source.value.getClass()));
    } else {
      target.setValue((T) source.getValue());
    }
  }

  public boolean isEmpty() {
    return this.empty;
  }

  public void setClean() {
    this.dirty = false;
    this.lock.readLock().lock();

    for (GenericDataManager.DataEntry<?> dataentry : this.entries.values()) {
      dataentry.setDirty(false);
    }

    this.lock.readLock().unlock();
  }

  public static class DataEntry<T> {
    private final DataParameter<T> key;
    private T value;
    private boolean dirty;

    public DataEntry(DataParameter<T> keyIn, T valueIn) {
      this.key = keyIn;
      this.value = valueIn;
      this.dirty = true;
    }

    public DataParameter<T> getKey() {
      return this.key;
    }

    public void setValue(T valueIn) {
      this.value = valueIn;
    }

    public T getValue() {
      return this.value;
    }

    public boolean isDirty() {
      return this.dirty;
    }

    public void setDirty(boolean dirtyIn) {
      this.dirty = dirtyIn;
    }

    public GenericDataManager.DataEntry<T> copy() {
      return new GenericDataManager.DataEntry<>(this.key,
          this.key.getSerializer().copyValue(this.value));
    }
  }
}
