/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.immerse.client.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.arikia.dev.drpc.OSUtil;

public class NativesUtil {

  public static void loadNativeLibrary(String name) {
    String mappedName = System.mapLibraryName(name);
    OSUtil osUtil = new OSUtil();
    File homeDir;
    String finalPath;
    String tempPath;
    String dir;

    if (osUtil.isMac()) {
      homeDir = new File(System.getProperty("user.home") + File.separator + "Library"
          + File.separator + "Application Support" + File.separator);
      dir = "darwin";
      tempPath = homeDir + File.separator + name + File.separator + mappedName;
    } else if (osUtil.isWindows()) {
      homeDir = new File(System.getenv("TEMP"));
      boolean is64bit = System.getProperty("sun.arch.data.model").equals("64");
      dir = (is64bit ? "win-x64" : "win-x86");
      tempPath = homeDir + File.separator + name + File.separator + mappedName;
    } else {
      homeDir = new File(System.getProperty("user.home"), "." + name);
      dir = "linux";
      tempPath = homeDir + File.separator + mappedName;
    }

    finalPath = "/" + dir + "/" + mappedName;

    File f = new File(tempPath);

    try (InputStream in = NativesUtil.class.getResourceAsStream(finalPath);
        OutputStream out = openOutputStream(f)) {
      copyFile(in, out);
      f.deleteOnExit();
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.load(f.getAbsolutePath());
  }

  private static FileOutputStream openOutputStream(final File file) throws IOException {
    if (file.exists()) {
      if (file.isDirectory()) {
        throw new IOException("File '" + file + "' exists but is a directory");
      }
      if (!file.canWrite()) {
        throw new IOException("File '" + file + "' cannot be written to");
      }
    } else {
      final File parent = file.getParentFile();
      if (parent != null) {
        if (!parent.mkdirs() && !parent.isDirectory()) {
          throw new IOException("Directory '" + parent + "' could not be created");
        }
      }
    }
    return new FileOutputStream(file);
  }

  private static void copyFile(final InputStream input, final OutputStream output)
      throws IOException {
    byte[] buffer = new byte[1024 * 4];
    int n;
    while (-1 != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
    }
  }
}
