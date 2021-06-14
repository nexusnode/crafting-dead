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

package com.craftingdead.core.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.IDataSerializer;

public class SynchedData {

  private static final Logger LOGGER = LogManager.getLogger();

  private static final Map<Class<?>, Integer> ID_POOL = Maps.newHashMap();

  private final DataEntry<?>[] entries = new DataEntry<?>[255];
  private final Runnable dirtyListener;
  private boolean empty = true;
  private boolean dirty;

  public SynchedData() {
    this.dirtyListener = () -> {
    };
  }

  public SynchedData(Runnable dirtyListener) {
    this.dirtyListener = dirtyListener;
  }

  public static <T> DataParameter<T> defineId(Class<?> clazz, IDataSerializer<T> serializer) {
    if (LOGGER.isDebugEnabled()) {
      try {
        Class<?> ownerClass =
            Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
        if (!ownerClass.equals(clazz)) {
          LOGGER.warn("defineId called for: {} from {}", clazz, ownerClass, new RuntimeException());
        }
      } catch (ClassNotFoundException classnotfoundexception) {
      }
    }

    int id;
    if (ID_POOL.containsKey(clazz)) {
      id = ID_POOL.get(clazz) + 1;
    } else {
      int i = 0;
      Class<?> ownerClass = clazz;

      while (ownerClass != Entity.class) {
        ownerClass = ownerClass.getSuperclass();
        if (ID_POOL.containsKey(ownerClass)) {
          i = ID_POOL.get(ownerClass) + 1;
          break;
        }
      }

      id = i;
    }

    if (id > 254) {
      throw new IllegalArgumentException(
          "Data value id is too big with " + id + "! (Max is " + 254 + ")");
    } else {
      ID_POOL.put(clazz, id);
      return serializer.createAccessor(id);
    }
  }

  public <T> void register(DataParameter<T> parameter, T value) {
    int id = parameter.getId();
    if (id > 254) {
      throw new IllegalArgumentException(
          "Data parameter id is too big with " + id + "! (Max is 254)");
    } else if (this.entries[id] != null) {
      throw new IllegalArgumentException("Duplicate id value for " + id + "!");
    } else if (DataSerializers.getSerializedId(parameter.getSerializer()) < 0) {
      throw new IllegalArgumentException(
          "Unregistered serializer " + parameter.getSerializer() + " for " + id + "!");
    } else {
      this.createEntry(parameter, value);
    }
  }

  private <T> void createEntry(DataParameter<T> parameter, T value) {
    DataEntry<T> entry = new DataEntry<>(parameter, value);
    this.entries[parameter.getId()] = entry;
    this.empty = false;
  }

  @SuppressWarnings("unchecked")
  private <T> DataEntry<T> getEntry(DataParameter<T> parameter) {
    DataEntry<T> entry;
    try {
      entry = (DataEntry<T>) this.entries[parameter.getId()];
    } catch (Throwable throwable) {
      CrashReport crashReport =
          CrashReport.forThrowable(throwable, "Getting data entry");
      CrashReportCategory category = crashReport.addCategory("Getting data entry");
      category.setDetail("Data parameter ID", parameter);
      throw new ReportedException(crashReport);
    }

    return entry;
  }

  public <T> T get(DataParameter<T> parameter) {
    return this.getEntry(parameter).getValue();
  }

  public <T> void set(DataParameter<T> parameter, T value) {
    DataEntry<T> entry = this.getEntry(parameter);
    if (ObjectUtils.notEqual(value, entry.getValue())) {
      entry.setValue(value);
      entry.setDirty(true);
      this.markDirty();
    }
  }

  public <T> T compute(DataParameter<T> parameter, Function<T, T> remappingFunction) {
    DataEntry<T> entry = this.getEntry(parameter);
    T newValue = remappingFunction.apply(entry.getValue());
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

  public static void pack(List<DataEntry<?>> entries, PacketBuffer buf) {
    if (entries != null) {
      for (DataEntry<?> entry : entries) {
        writeEntry(buf, entry);
      }
    }
    buf.writeByte(255);
  }

  private static <T> void writeEntry(PacketBuffer out, DataEntry<T> entry) {
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
  public List<DataEntry<?>> packDirty() {
    List<DataEntry<?>> list = null;
    if (this.dirty) {
      for (DataEntry<?> entry : this.entries) {
        if (entry.isDirty()) {
          entry.setDirty(false);
          if (list == null) {
            list = Lists.newArrayList();
          }
          list.add(entry.copy());
        }
      }
    }
    this.dirty = false;
    return list;
  }

  @Nullable
  public List<DataEntry<?>> getAll() {
    List<DataEntry<?>> list = null;
    for (DataEntry<?> entry : this.entries) {
      if (list == null) {
        list = new ArrayList<>();
      }
      list.add(entry.copy());
    }
    return list;
  }

  @Nullable
  public static List<DataEntry<?>> unpack(PacketBuffer buf) {
    List<DataEntry<?>> entries = null;

    int id;
    while ((id = buf.readUnsignedByte()) != 255) {
      if (entries == null) {
        entries = Lists.newArrayList();
      }

      int j = buf.readVarInt();
      IDataSerializer<?> serializer = DataSerializers.getSerializer(j);
      if (serializer == null) {
        throw new DecoderException("Unknown serializer type " + j);
      }

      entries.add(readEntry(buf, id, serializer));
    }

    return entries;
  }

  private static <T> SynchedData.DataEntry<T> readEntry(PacketBuffer buf,
      int id, IDataSerializer<T> serializer) {
    return new SynchedData.DataEntry<>(serializer.createAccessor(id),
        serializer.read(buf));
  }

  public void assignValues(@Nullable List<DataEntry<?>> entries) {
    if (entries == null) {
      return;
    }

    for (DataEntry<?> entry : entries) {
      DataEntry<?> currentEntry = this.entries[entry.getKey().getId()];
      if (currentEntry != null) {
        this.assignValue(currentEntry, entry);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private <T> void assignValue(DataEntry<T> destination, DataEntry<?> source) {
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

  public void cleanDirty() {
    this.dirty = false;

    for (DataEntry<?> entry : this.entries) {
      entry.setDirty(false);
    }
  }

  public static class DataEntry<T> {

    private final DataParameter<T> parameter;
    private T value;
    private boolean dirty;

    private DataEntry(DataParameter<T> parameter, T value) {
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

    public void setDirty(boolean dirty) {
      this.dirty = dirty;
    }

    public SynchedData.DataEntry<T> copy() {
      return new SynchedData.DataEntry<>(this.parameter,
          this.parameter.getSerializer().copy(this.value));
    }
  }
}
