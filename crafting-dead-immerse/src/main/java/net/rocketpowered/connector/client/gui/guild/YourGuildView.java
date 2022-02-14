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

package net.rocketpowered.connector.client.gui.guild;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.view.AvatarView;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.rocketpowered.connector.RocketConnector;
import net.rocketpowered.connector.api.GameClientApi;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.GuildRank;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.payload.GuildMemberLeaveEvent;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.payload.GuildMemberPayload;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.payload.GuildMemberUpdateEvent;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.payload.GuildPayload;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.payload.UserPresencePayload;
import net.rocketpowered.connector.internal.shaded.org.bson.types.ObjectId;
import net.rocketpowered.connector.internal.shaded.reactor.core.Disposable;
import net.rocketpowered.connector.internal.shaded.reactor.core.publisher.Mono;
import net.rocketpowered.connector.internal.shaded.reactor.core.scheduler.Schedulers;

public class YourGuildView extends ParentView {

  public static final Component TITLE = new TranslatableComponent("view.guild.your_guild");

  private final ParentView informationView;

  private final ParentView membersView;

  private final Map<ObjectId, MemberView> memberViews = new HashMap<>();

  @Nullable
  private GuildPayload guild;

  private Disposable listener;

  public YourGuildView() {
    super(new Properties<>());

    var dialogView = new ParentView(new Properties<>().styleClasses("dialog").backgroundBlur(50));
    dialogView.addChild(new TextView(new Properties<>().id("title"))
        .setText(TITLE)
        .setCentered(true));

    var guildView = new ParentView(new Properties<>().id("guild"));
    dialogView.addChild(guildView);

    guildView.addChild(
        this.informationView = new ParentView(new Properties<>().id("information")));

    guildView.addChild(
        this.membersView = new ParentView(new Properties<>().id("members")));

    this.addChild(dialogView);
  }

  private void updateGuild(GameClientApi api, GuildPayload guild) {
    if (guild == null) {
      // this.minecraft.setScreen(new MainMenuScreen());
      return;
    }

    if (!guild.equals(this.guild)) {
      this.membersView.clearChildren();
      api.getGuildMembers(guild.getId())
          .publishOn(Schedulers.fromExecutor(this.minecraft))
          .doOnNext(member -> this.updateMember(api, member))
          .doOnTerminate(this::layout)
          .subscribeOn(Schedulers.boundedElastic())
          .subscribe();
    }

    this.informationView.clearChildren();

    this.informationView.addChild(new TextView(new Properties<>())
        .setText(new TextComponent("")
            .append(new TextComponent("Name: ")
                .withStyle(ChatFormatting.GOLD))
            .append(guild.getName()))
        .setShadow(false));
    this.informationView.addChild(new TextView(new Properties<>())
        .setText(new TextComponent("")
            .append(new TextComponent("Tag: ")
                .withStyle(ChatFormatting.GRAY))
            .append(guild.getTag()))
        .setShadow(false));
    this.informationView.addChild(new TextView(new Properties<>())
        .setText(new TextComponent("")
            .append(new TextComponent("Owner: ")
                .withStyle(ChatFormatting.GRAY))
            .append(guild.getOwner().getMinecraftProfile().getName()))
        .setShadow(false));

    this.informationView.layout();

    this.guild = guild;
  }

  private void updateMember(GameClientApi api, GuildMemberPayload member) {
    MemberView view =
        this.memberViews.computeIfAbsent(member.getUser().getId(), __ -> new MemberView(member));
    if (view.isAdded()) {
      view.updateMember(member);
    } else {
      this.membersView.addChild(view);
    }
  }

  @Override
  protected void added() {
    super.added();
    this.listener = RocketConnector.getInstance().getGameClientApi()
        .flatMap(api -> Mono.when(
            api.getSocialProfile()
                .publishOn(Schedulers.fromExecutor(this.minecraft))
                .doOnNext(profile -> this.updateGuild(api, profile.getGuild().orElse(null))),
            api.getGuildEvents()
                .ofType(GuildMemberUpdateEvent.class)
                .filter(event -> this.guild != null
                    && event.getGuildId().equals(this.guild.getId()))
                .publishOn(Schedulers.fromExecutor(this.minecraft))
                .doOnNext(event -> this.updateMember(api, event.getGuildMember()))))
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
  }

  @Override
  protected void removed() {
    super.removed();
    this.listener.dispose();
  }

  private class MemberView extends ParentView {

    private GuildMemberPayload member;

    private Disposable removeListener;
    private Disposable presenceListener;

    private final AvatarView avatarView;
    private final TextView nameView;

    public MemberView(GuildMemberPayload member) {
      super(new Properties<>().unscaleBorder(false));

      this.updateMember(member);

      this.avatarView = new AvatarView(new Properties<>(),
          new GameProfile(member.getUser().getMinecraftProfile().getId(), null));
      this.avatarView.defineBorderColorState(Theme.OFFLINE);
      this.addChild(this.avatarView);

      this.nameView = new TextView(new Properties<>().id("name"))
          .setWrap(false)
          .setText(member.getUser().getMinecraftProfile().getName());
      this.nameView.defineBorderColorState(Theme.OFFLINE);

      this.nameView.getColorProperty().defineState(Theme.OFFLINE);
      this.addChild(this.nameView);
    }

    @Override
    protected boolean isFocusable() {
      return true;
    }

    private void updateMember(GuildMemberPayload member) {
      this.member = member;

      GuildRank rank = member.getRank();

      Color color = Color.GRAY;

      if (member.getUser().equals(YourGuildView.this.guild.getOwner())) {
        color = Color.GOLD;
      } else if (rank == GuildRank.DIGNITARY) {
        color = Color.DARK_PURPLE;
      } else if (rank == GuildRank.ENVOY) {
        color = Color.AQUA;
      }

      this.getLeftBorderColorProperty().defineState(color);
    }

    private void updatePresence(UserPresencePayload presence) {
      var color = presence.isOnline() ? Theme.ONLINE : Theme.OFFLINE;
      this.avatarView.defineBorderColorState(color);
      this.nameView.getColorProperty().defineState(color);
    }

    @Override
    protected void added() {
      super.added();
      this.removeListener = RocketConnector.getInstance().getGameClientApi()
          .flatMap(GameClientApi::getGuildEvents)
          .ofType(GuildMemberLeaveEvent.class)
          .filter(event -> event.getUser().equals(this.member.getUser()))
          .next()
          .subscribeOn(Schedulers.boundedElastic())
          .publishOn(Schedulers.fromExecutor(this.minecraft))
          .subscribe(__ -> {
            YourGuildView.this.memberViews.remove(this.member.getUser().getId(), this);
            if (this.isAdded()) {
              this.getParent().removeChild(this);
            }
          });

      this.presenceListener = RocketConnector.getInstance().getGameClientApi()
          .flatMap(api -> api.getUserPresence(this.member.getUser().getId()))
          .subscribeOn(Schedulers.boundedElastic())
          .publishOn(Schedulers.fromExecutor(this.minecraft))
          .subscribe(this::updatePresence);
    }

    @Override
    protected void removed() {
      super.removed();
      this.removeListener.dispose();
      this.presenceListener.dispose();
    }
  }
}
