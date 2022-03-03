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

package com.craftingdead.immerse.client.gui.view.style;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.view.style.parser.StylesheetParser;
import com.craftingdead.immerse.client.gui.view.style.tree.StyleList;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class StylesheetManager implements ResourceManagerReloadListener {

  private static final ResourceLocation USER_AGENT =
      new ResourceLocation(CraftingDeadImmerse.ID, "css/user-agent.css");

  private static final Logger logger = LogUtils.getLogger();

  private static StylesheetManager instance;

  private final LoadingCache<ResourceLocation, StyleList> styleCache;

  private final Map<String, Theme> themes = new HashMap<>();

  private StylesheetManager() {
    this.styleCache = CacheBuilder.newBuilder()
        .maximumSize(100)
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .build(new CacheLoader<>() {
          @Override
          public StyleList load(@Nonnull ResourceLocation stylesheet) throws IOException {
            return StylesheetParser.loadStylesheet(stylesheet);
          }

          @Override
          public ListenableFuture<StyleList> reload(ResourceLocation key, StyleList oldValue)
              throws Exception {
            logger.info("Stylesheet has been forced to reload {}", key);
            return super.reload(key, oldValue);
          }
        });
  }

  @Override
  public void onResourceManagerReload(ResourceManager resourceManager) {
    for (var key : this.styleCache.asMap().keySet()) {
      this.styleCache.refresh(key);
    }
  }

  public void forceReload(Iterable<ResourceLocation> styleSheets) {
    this.forceReload(styleSheets, null);
  }

  public StyleList forceReload(Iterable<ResourceLocation> styleSheets, @Nullable String themeId) {
    styleSheets.forEach(this.styleCache::refresh);
    return this.refreshStylesheets(styleSheets, themeId);
  }

  public StyleList refreshStylesheets(Iterable<ResourceLocation> styleSheets) {
    return this.refreshStylesheets(styleSheets, null);
  }

  public StyleList refreshStylesheets(Iterable<ResourceLocation> styleSheets,
      @Nullable String themeId) {
    StyleList userAgentList = null;
    try {
      userAgentList = this.getStyleList(USER_AGENT);
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    var list = themeId != null
        ? new StyleList(this.getTheme(themeId))
        : userAgentList != null ? new StyleList(userAgentList) : new StyleList();
    list.merge(this.loadStylesheets(styleSheets));
    return list;
  }

  public StyleList loadStylesheets(Iterable<ResourceLocation> styleSheets) {
    var list = new StyleList();
    for (var styleSheet : styleSheets) {
      try {
        list.merge(this.getStyleList(styleSheet));
      } catch (ExecutionException e) {
        logger.error("Error loading stylesheet {} {}", styleSheet, e);
      }
    }
    return list;
  }

  private StyleList getStyleList(ResourceLocation styleSheet) throws ExecutionException {
    return this.styleCache.get(styleSheet);
  }

  public void addTheme(String themeId, ResourceLocation styleSheet) {
    if (StringUtils.isEmpty(themeId)) {
      throw new IllegalArgumentException("Invalid theme " + themeId);
    }
    try {
      if (!this.themes.containsKey(themeId)) {
        this.createTheme(themeId);
      }

      var theme = this.themes.get(themeId);

      if (theme.styleSheets().contains(styleSheet)) {
        return;
      }

      theme.styleSheets().add(styleSheet);
      theme.styleList().merge(this.getStyleList(styleSheet));
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
  }

  public void removeTheme(String themeId, ResourceLocation styleSheet) {
    if (StringUtils.isEmpty(themeId)) {
      throw new IllegalArgumentException("Invalid theme " + themeId);
    }

    var theme = this.themes.get(themeId);
    if (theme == null) {
      return;
    }

    if (!theme.styleSheets().contains(styleSheet)) {
      return;
    }

    theme.styleSheets().remove(styleSheet);
    theme.styleList(this.loadStylesheets(theme.styleSheets()));
  }

  private StyleList getTheme(String themeId) {
    if (!this.themes.containsKey(themeId)) {
      this.createTheme(themeId);
    }
    return this.themes.get(themeId).styleList();
  }

  private void createTheme(String themeId) {
    try {
      var theme = new Theme();
      theme.styleSheets().add(USER_AGENT);
      theme.styleList().merge(this.getStyleList(USER_AGENT));
      this.themes.put(themeId, theme);
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
  }

  public static StylesheetManager getInstance() {
    if (instance == null) {
      instance = new StylesheetManager();
    }
    return instance;
  }

  private static class Theme {

    private final List<ResourceLocation> styleSheets = new ArrayList<>();
    private StyleList styleList = new StyleList();

    public StyleList styleList() {
      return this.styleList;
    }

    public void styleList(StyleList styleList) {
      this.styleList = styleList;
    }

    public List<ResourceLocation> styleSheets() {
      return this.styleSheets;
    }
  }
}
