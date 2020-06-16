package com.craftingdead.core.util;

import java.util.UUID;

public enum PlayerResource {

  AVATAR_URL("https://crafatar.com/avatars/%s.png");

  private String url;

  PlayerResource(String url) {
    this.url = url;
  }

  public String getUrl(UUID playerId) {
    return String.format(url, new Object[]{playerId});
  }
}
