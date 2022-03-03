/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.CRC32;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.Platform;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;

/**
 * Copied from {@link org.lwjgl.system.SharedLibraryLoader}.
 * 
 * @author Sm0keySa1m0n
 */
public class LwjglNativeUtil {

  private static final Logger logger = LogUtils.getLogger();

  private static Path nativesDir;

  private static void initNativesDir() {
    if (nativesDir != null) {
      return;
    }

    try {
      nativesDir = Files.createTempDirectory("natives");
    } catch (IOException e) {
      throw new ReportedException(CrashReport.forThrowable(e, "Creating natives directory"));
    }

    var newLibPath = nativesDir.toString();
    logger.info("Adding library path: {}", newLibPath);

    // Prepend the path in which the libraries were extracted to org.lwjgl.librarypath
    String libPath = Configuration.LIBRARY_PATH.get();
    if (libPath != null && !libPath.isEmpty()) {
      newLibPath += File.pathSeparator + libPath;
    }

    System.setProperty(Configuration.LIBRARY_PATH.getProperty(), newLibPath);
    Configuration.LIBRARY_PATH.set(newLibPath);
  }

  public static void load(String name) {
    var libName = Platform.mapLibraryNameBundled(name);
    try {
      var platform = Platform.get();
      var mapLibraryNameMethod = Platform.class.getDeclaredMethod("mapLibraryName", String.class);
      mapLibraryNameMethod.setAccessible(true);
      libName = (String) mapLibraryNameMethod.invoke(platform, libName);
    } catch (ReflectiveOperationException e) {
      throw new ReportedException(
          CrashReport.forThrowable(e, "Accessing mapLibraryName via reflection"));
    }

    initNativesDir();

    var resourcePath = "/lwjgl-natives/" + libName;
    var resource = LwjglNativeUtil.class.getResource(resourcePath);
    if (resource == null) {
      var message = "Resource not found: " + resourcePath;
      throw new ReportedException(new CrashReport(message, new IllegalStateException(message)));
    }

    try {
      extract(nativesDir.resolve(libName), resource);
    } catch (IOException e) {
      throw new ReportedException(CrashReport.forThrowable(e, "Extracting: " + libName));
    }
  }

  /**
   * Extracts a native library resource if it does not already exist or the CRC does not match.
   *
   * @param resource the resource to extract
   * @param file the extracted file
   *
   * @return a {@link FileChannel} that has locked the resource
   *
   * @throws IOException if an IO error occurs
   */
  private static void extract(Path file, URL resource) throws IOException {
    if (Files.exists(file)) {
      try (
          InputStream source = resource.openStream();
          InputStream target = Files.newInputStream(file)) {
        if (crc(source) == crc(target)) {
          logger.debug("Found at: {}", file);
          return;
        }
      }
    }

    // If file doesn't exist or the CRC doesn't match, extract it to the temp dir.
    logger.debug("Extracting: {}", resource.getPath());

    Files.createDirectories(file.getParent());
    try (InputStream source = resource.openStream()) {
      Files.copy(source, file, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  /**
   * Returns a CRC of the remaining bytes in a stream.
   *
   * @param input the stream
   *
   * @return the CRC
   */
  private static long crc(InputStream input) throws IOException {
    CRC32 crc = new CRC32();

    byte[] buffer = new byte[8 * 1024];
    for (int n; (n = input.read(buffer)) != -1;) {
      crc.update(buffer, 0, n);
    }

    return crc.getValue();
  }
}
