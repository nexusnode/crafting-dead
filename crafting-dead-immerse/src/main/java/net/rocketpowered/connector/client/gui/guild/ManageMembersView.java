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
import java.util.function.Consumer;
import javax.annotation.Nullable;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.view.AvatarView;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.View;
import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.rocketpowered.connector.RocketConnector;
import net.rocketpowered.connector.api.GameClientApi;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.GuildPermission;
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

public class ManageMembersView extends ParentView {

  public static final Component TITLE =
      new TranslatableComponent("view.guild.manage_members");

  private final ParentView dialogView;

  private final ParentView membersListView;

  private final ParentView controlsView;

  private final View promoteButton;
  private final View demoteButton;
  private final View kickButton;

  private final Map<ObjectId, MemberView> memberViews = new HashMap<>();

  @Nullable
  private GuildPayload guild;

  @Nullable
  private GuildMemberPayload member;

  @Nullable
  private MemberView selectedMemberView;

  private Disposable listener;

  public ManageMembersView(Consumer<View> viewConsumer) {
    super(new Properties<>());

    this.dialogView = new ParentView(new Properties<>().styleClasses("dialog").backgroundBlur(50));

    this.dialogView.addChild(new TextView(new Properties<>().id("title"))
        .setText(TITLE)
        .setCentered(true));

    this.dialogView.addChild(
        this.membersListView = new ParentView(new Properties<>().id("list")));

    this.controlsView = new ParentView(new Properties<>().id("controls"));

    this.controlsView.addChild(
        this.promoteButton = Theme.createBlueButton(
            new TextComponent("Promote"),
            () -> RocketConnector.getInstance().getGameClientApi()
                .next()
                .flatMap(api -> api.setGuildMemberRank(
                    this.selectedMemberView.member.getUser().getId(),
                    this.selectedMemberView.member.getRank().promote()))
                .subscribe(),
            "promote-button"));
    this.controlsView.addChild(
        this.demoteButton = Theme.createRedButton(
            new TextComponent("Demote"),
            () -> RocketConnector.getInstance().getGameClientApi()
                .next()
                .flatMap(api -> api.setGuildMemberRank(
                    this.selectedMemberView.member.getUser().getId(),
                    this.selectedMemberView.member.getRank().demote()))
                .subscribe()));
    this.controlsView.addChild(this.kickButton = Theme.createRedButton(
        new TextComponent("Kick"),
        () -> RocketConnector.getInstance().getGameClientApi()
            .next()
            .flatMap(api -> api.kickGuildMember(
                this.selectedMemberView.member.getUser().getId()))
            .subscribe()));
    this.controlsView.addChild(Theme.createBlueButton(new TextComponent("Invite"),
        () -> viewConsumer.accept(new SendInviteView(result -> {
          if (!Strings.isNullOrEmpty(result)) {
            RocketConnector.getInstance().getGameClientApi()
                .flatMap(api -> api.getUserId(result).flatMap(api::sendGuildInvite))
                .subscribe();
          }
          viewConsumer.accept(this);
        }))));

    this.dialogView.addChild(this.controlsView);

    this.addChild(this.dialogView);

    this.updateSelected();
  }

  protected void updateSelected() {
    this.selectedMemberView = this.membersListView.getChildViews().stream()
        .filter(MemberView.class::isInstance)
        .map(MemberView.class::cast)
        .filter(View::isFocused)
        .findAny()
        .orElse(null);

    if (this.selectedMemberView == null) {
      this.promoteButton.setEnabled(false);
      this.demoteButton.setEnabled(false);
      this.kickButton.setEnabled(false);
      return;
    }

    var selectedMember = this.selectedMemberView.member;
    var selectedMemberRank = selectedMember.getRank();
    var selectedMemberOwner = selectedMember.isOwner(this.guild);

    var selfRank = this.member.getRank();
    var selfPermissions = this.member.getPermissions(this.guild);
    var selfOwner = this.member.isOwner(this.guild);
    var manageRanks = GuildPermission.MANAGE_RANKS.hasPermission(selfPermissions);

    this.promoteButton.setEnabled(!selectedMemberRank.isHighest()
        && (selfOwner || selectedMemberRank.ordinal() < selfRank.ordinal())
        && !selectedMemberOwner
        && manageRanks);
    this.demoteButton.setEnabled(!selectedMemberRank.isLowest()
        && (selfOwner || selectedMemberRank.ordinal() < selfRank.ordinal())
        && !selectedMemberOwner
        && manageRanks);
    this.kickButton.setEnabled(!selectedMember.equals(this.member)
        && GuildPermission.KICK.hasPermission(selfPermissions)
        && !selectedMemberOwner);
  }

  private void updateGuild(GameClientApi api, GuildPayload guild) {
    if (guild == null) {
      return;
    }

    if (!guild.equals(this.guild)) {
      this.membersListView.clearChildren();
      api.getGuildMembers(guild.getId())
          .publishOn(Schedulers.fromExecutor(this.minecraft))
          .doOnNext(member -> this.updateMember(api, member))
          .doOnTerminate(this::layout)
          .subscribeOn(Schedulers.boundedElastic())
          .subscribe();
    }

    this.guild = guild;
  }

  private void updateMember(GameClientApi api, GuildMemberPayload member) {
    if (member.getUser().equals(api.getUser())) {
      this.updateSelfMember(member);
    }
    MemberView view =
        this.memberViews.computeIfAbsent(member.getUser().getId(), __ -> new MemberView(member));
    if (view.isAdded()) {
      view.updateMember(member);
    } else {
      this.membersListView.addChild(view);
    }
  }

  private void updateSelfMember(GuildMemberPayload member) {
    this.member = member;
    long permissions =
        this.guild.getPermissions(member.getUser().getId(), member.getRank());
    if (GuildPermission.KICK.hasPermission(permissions)
        || GuildPermission.MANAGE_RANKS.hasPermission(permissions)
        || GuildPermission.INVITE.hasPermission(permissions)) {
      if (this.controlsView.getParent() != this.dialogView) {
        this.dialogView.addChild(this.controlsView);
        this.layout();
      }
    } else if (this.controlsView.getParent() == this.dialogView) {
      this.dialogView.removeChild(this.controlsView);
      this.layout();
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
                .doOnNext(event -> {
                  this.updateMember(api, event.getGuildMember());
                  this.membersListView.layout();
                })))
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
  }

  @Override
  protected void removed() {
    super.removed();
    this.listener.dispose();
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    boolean result = super.mouseClicked(mouseX, mouseY, button);
    this.updateSelected();
    return result;
  }

  private class MemberView extends ParentView {

    private GuildMemberPayload member;

    private Disposable removeListener;
    private Disposable presenceListener;

    private final AvatarView avatarView;
    private final TextView nameView;

    public MemberView(GuildMemberPayload member) {
      super(new Properties<>().styleClasses("item").unscaleBorder(false));

      this.updateMember(member);

      this.avatarView = new AvatarView(new Properties<>(),
          new GameProfile(member.getUser().getMinecraftProfile().getId(), null));
      this.avatarView.defineBorderColorState(Theme.OFFLINE);
      this.addChild(this.avatarView);

      this.nameView = new TextView(new Properties<>().id("name"))
          .setWrap(false)
          .setText(member.getUser().getMinecraftProfile().getName());
      this.nameView.getColorProperty().defineState(Theme.OFFLINE);
      this.addChild(this.nameView);
    }

    @Override
    protected boolean isFocusable() {
      return true;
    }

    private void updateMember(GuildMemberPayload member) {
      this.member = member;

      var rank = member.getRank();

      var color = Color.GRAY;

      if (member.getUser().equals(ManageMembersView.this.guild.getOwner())) {
        color = Color.GOLD;
      } else if (rank == GuildRank.DIGNITARY) {
        color = Color.DARK_PURPLE;
      } else if (rank == GuildRank.ENVOY) {
        color = Color.AQUA;
      } else if (member.equals(ManageMembersView.this.member)) {
        color = Theme.ONLINE;
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
            ManageMembersView.this.memberViews.remove(this.member.getUser().getId(), this);
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
