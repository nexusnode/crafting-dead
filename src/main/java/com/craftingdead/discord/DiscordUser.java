package com.craftingdead.discord;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DiscordUser extends Structure {
    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList(
            "userId",
            "username",
            "discriminator",
            "avatar"
    ));

    public DiscordUser(String encoding) {
        super();
        setStringEncoding(encoding);
    }

    public DiscordUser() {
        this("UTF-8");
    }

    public String userId;
    public String username;
    public String discriminator;
    public String avatar;

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (!(o instanceof DiscordUser))
            return false;
        DiscordUser that = (DiscordUser) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(username, that.username)
                && Objects.equals(discriminator, that.discriminator)
                && Objects.equals(avatar, that.avatar);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(userId, username, discriminator, avatar);
    }

    @Override
    protected List<String> getFieldOrder()
    {
        return FIELD_ORDER;
    }
}
