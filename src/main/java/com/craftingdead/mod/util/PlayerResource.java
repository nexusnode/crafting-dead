package com.craftingdead.mod.util;

import java.util.UUID;

public enum PlayerResource {

    AVATAR_URL("https://crafatar.com/avatars/%s.png");

    private String url;

    PlayerResource(String url) {
        this.url = url;
    }

    public String getUrl(UUID playerUUID) {
        return String.format(url, new Object[]{playerUUID});
    }

}