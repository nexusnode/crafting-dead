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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import com.google.common.collect.Lists;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.IDataSerializer;

public class NetworkDataManager {

  private final Map<Integer, NetworkDataManager.DataEntry<?>> entries = new Int2ObjectArrayMap<>();
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final Runnable dirtyListener;

  private boolean empty = true;
  private boolean dirty;

  public NetworkDataManager() {
    this.dirtyListener = () -> {
    };
  }

  public NetworkDataManager(Runnable dirtyListener) {
    this.dirtyListener = dirtyListener;
  }

  public <T> void register(DataParameter<T> parameter, T value) {
    int id = parameter.getId();
    if (id > 254) {
      throw new IllegalArgumentException(
          "Data parameter id is too big with " + id + "! (Max is 254)");
    } else if (this.entries.containsKey(id)) {
      throw new IllegalArgumentException("Duplicate id value for " + id + "!");
    } else if (DataSerializers.getSerializedId(parameter.getSerializer()) < 0) {
      throw new IllegalArgumentException(
          "Unregistered serializer " + parameter.getSerializer() + " for " + id + "!");
    } else {
      this.setEntry(parameter, value);
    }
  }

  private <T> void setEntry(DataParameter<T> parameter, T value) {
    NetworkDataManager.DataEntry<T> entry = new NetworkDataManager.DataEntry<>(parameter, value);
    this.lock.writeLock().lock();
    this.entries.put(parameter.getId(), entry);
    this.empty = false;
    this.lock.writeLock().unlock();
  }

  @SuppressWarnings("unchecked")
  private <T> NetworkDataManager.DataEntry<T> getEntry(DataParameter<T> parameter) {
    this.lock.readLock().lock();

    NetworkDataManager.DataEntry<T> entry;
    try {
      entry = (NetworkDataManager.DataEntry<T>) this.entries.get(parameter.getId());
    } catch (Throwable throwable) {
      CrashReport crashReport =
          CrashReport.forThrowable(throwable, "Getting data entry");
      CrashReportCategory category = crashReport.addCategory("Getting data entry");
      category.setDetail("Data parameter ID", parameter);
      throw new ReportedException(crashReport);
    } finally {
      this.lock.readLock().unlock();
    }

    return entry;
  }

  public <T> T get(DataParameter<T> parameter) {
    return this.getEntry(parameter).getValue();
  }

  public <T> void set(DataParameter<T> parameter, T value) {
    NetworkDataManager.DataEntry<T> entry = this.getEntry(parameter);
    if (ObjectUtils.notEqual(value, entry.getValue())) {
      entry.setValue(value);
      entry.setDirty(true);
      this.markDirty();
    }
  }

  public <T> T getUpdate(DataParameter<T> parameter, Function<T, T> updater) {
    NetworkDataManager.DataEntry<T> entry = this.getEntry(parameter);
    T newValue = updater.apply(entry.getValue());
    if (ObjectUtils.notEqual(newValue, entry.getValue())) {
      entry.setValue(newValue);
      entry.setDirty(true);
      this.markDirty();
    }
    return newValue;
  }

  public boolean isDirty() {
    return this.dirty;
  }

  private void markDirty() {
    if (!this.dirty) {
      this.dirtyListener.run();
    }
    this.dirty = true;
  }

  public static void writeEntries(List<NetworkDataManager.DataEntry<?>> entries, PacketBuffer buf) {
    if (entries != null) {
      for (NetworkDataManager.DataEntry<?> entry : entries) {
        writeEntry(buf, entry);
      }
    }
    buf.writeByte(255);
  }

  private static <T> void writeEntry(PacketBuffer out, NetworkDataManager.DataEntry<T> entry) {
    DataParameter<T> parameter = entry.getKey();
    int i = DataSerializers.getSerializedId(parameter.getSerializer());
    if (i < 0) {
      throw new EncoderException("Unknown serializer type " + parameter.getSerializer());
    } else {
      out.writeByte(parameter.getId());
      out.writeVarInt(i);
      parameter.getSerializer().write(out, entry.getValue());
    }
  }

  @Nullable
  public List<NetworkDataManager.DataEntry<?>> getDirty() {
    List<NetworkDataManager.DataEntry<?>> list = null;
    if (this.dirty) {
      this.lock.readLock().lock();
      for (NetworkDataManager.DataEntry<?> enttry : this.entries.values()) {
        if (enttry.isDirty()) {
          enttry.setDirty(false);
          if (list == null) {
            list = Lists.newArrayList();
          }
          list.add(enttry.copy());
        }
      }
      this.lock.readLock().unlock();
    }
    this.dirty = false;
    return list;
  }

  @Nullable
  public List<NetworkDataManager.DataEntry<?>> getAll() {
    List<NetworkDataManager.DataEntry<?>> list = null;
    this.lock.readLock().lock();
    for (NetworkDataManager.DataEntry<?> entry : this.entries.values()) {
      if (list == null) {
        list = Lists.newArrayList();
      }
      list.add(entry.copy());
    }
    this.lock.readLock().unlock();
    return list;
  }

  @Nullable
  public static List<NetworkDataManager.DataEntry<?>> readEntries(PacketBuffer in) {
    List<NetworkDataManager.DataEntry<?>> entries = null;

    int id;
    while ((id = in.readUnsignedByte()) != 255) {
      if (entries == null) {
        entries = Lists.newArrayList();
      }

      int j = in.readVarInt();
      IDataSerializer<?> serializer = DataSerializers.getSerializer(j);
      if (serializer == null) {
        throw new DecoderException("Unknown serializer type " + j);
      }

      entries.add(readEntry(in, id, serializer));
    }

    return entries;
  }

  private static <T> NetworkDataManager.DataEntry<T> readEntry(PacketBuffer buf,
      int id, IDataSerializer<T> serializer) {
    return new NetworkDataManager.DataEntry<>(serializer.createAccessor(id),
        serializer.read(buf));
  }

  public void setEntryValues(@Nullable List<NetworkDataManager.DataEntry<?>> entries) {
    if (entries == null) {
      return;
    }

    this.lock.writeLock().lock();

    for (NetworkDataManager.DataEntry<?> entry : entries) {
      NetworkDataManager.DataEntry<?> currentEntry = this.entries.get(entry.getKey().getId());
      if (currentEntry != null) {
        this.transferData(currentEntry, entry);
      }
    }

    this.lock.writeLock().unlock();
  }

  @SuppressWarnings("unchecked")
  private <T> void transferData(NetworkDataManager.DataEntry<T> destination,
      NetworkDataManager.DataEntry<?> source) {
    if (!Objects.equals(source.parameter.getSerializer(), destination.parameter.getSerializer())) {
      throw new IllegalStateException(String.format(
          "Data entry mismatch for %d: old=%s(%s), new=%s(%s)",
          destination.parameter.getId(), destination.value, destination.value.getClass(),
          source.value,
          source.value.getClass()));
    } else {
      destination.setValue((T) source.getValue());
    }
  }

  public boolean isEmpty() {
    return this.empty;
  }

  public void setClean() {
    this.dirty = false;
    this.lock.readLock().lock();

    for (NetworkDataManager.DataEntry<?> entry : this.entries.values()) {
      entry.setDirty(false);
    }

    this.lock.readLock().unlock();
  }

  public static class DataEntry<T> {

    private final DataParameter<T> parameter;
    private T value;
    private boolean dirty;

    public DataEntry(DataParameter<T> parameter, T value) {
      this.parameter = parameter;
      this.value = value;
      this.dirty = true;
    }

    public DataParameter<T> getKey() {
      return this.parameter;
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

    public NetworkDataManager.DataEntry<T> copy() {
      return new NetworkDataManager.DataEntry<>(this.parameter,
          this.parameter.getSerializer().copy(this.value));
    }
  }
}
