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

package com.craftingdead.immerse.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;

public final class DependencyLoader {

  private static final String MOD_JAR_URL_PROTOCOL = "modjar";
  private static final String DEPENDENCIES_DIR = "dependencies";

  private static final Logger logger = LogManager.getLogger();

  public static void loadDependencies() {
    if (FMLLoader.isProduction()) {
      logger.info("Loading dependencies");

      ModFile modFile = ModList.get().getModFileById(CraftingDeadImmerse.ID).getFile();
      Path resolvedDir = modFile.findResource(DEPENDENCIES_DIR);
      try (Stream<Path> paths = Files.walk(resolvedDir)) {
        paths
            .filter(p -> p.toString().endsWith(".jar"))
            .map(DependencyLoader::toModJarUrl)
            .forEach(DependencyLoader::addUrlToClassLoader);
      } catch (IOException e) {
        throw new UncheckedIOException("Failed to search for dependency jars", e);
      }

      logger.info("Finished loading dependencies");
    }
  }

  private static URL toModJarUrl(Path modJarPath) {
    try {
      return new URL(MOD_JAR_URL_PROTOCOL, CraftingDeadImmerse.ID, modJarPath.toString());
    } catch (MalformedURLException e) {
      throw new IllegalStateException("Failed to parse mod jar URL", e);
    }
  }

  private static void addUrlToClassLoader(URL url) {
    try {
      URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
      Method addURLMethod =
          URLClassLoader.class.getDeclaredMethod("addURL", new Class[] {URL.class});
      addURLMethod.setAccessible(true);
      addURLMethod.invoke(classLoader, new Object[] {url});
    } catch (Throwable t) {
      throw new IllegalStateException("Failed to add URL to system class loader", t);
    }
  }
}
