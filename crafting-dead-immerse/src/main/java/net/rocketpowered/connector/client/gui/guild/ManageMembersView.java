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
import org.bson.types.ObjectId;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.event.RemovedEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.rocketpowered.api.Rocket;
import net.rocketpowered.api.gateway.GameClientGateway;
import net.rocketpowered.common.GuildPermission;
import net.rocketpowered.common.payload.GuildMemberPayload;
import net.rocketpowered.common.payload.GuildMemberUpdateEvent;
import net.rocketpowered.common.payload.GuildPayload;
import net.rocketpowered.connector.client.gui.RocketToast;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class ManageMembersView extends ParentView {

  public static final Component TITLE =
      new TranslatableComponent("view.guild.manage_members");

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
    super(new Properties<>().styleClasses("page").backgroundBlur(50));

    this.addChild(new TextView(new Properties<>().id("title"))
        .setText(TITLE)
        .setCentered(true));

    this.addChild(
        this.membersListView = new ParentView(new Properties<>().id("list")));

    this.controlsView = new ParentView(new Properties<>().id("controls"));

    this.controlsView.addChild(
        this.promoteButton = Theme.createBlueButton(
            new TextComponent("Promote"),
            () -> Rocket.getGameClientGateway().ifPresent(gateway -> gateway
                .setGuildMemberRank(
                    this.selectedMemberView.getMember().getUser().getId(),
                    this.selectedMemberView.getMember().getRank().promote())
                .subscribe()),
            "promote-button"));
    this.controlsView.addChild(
        this.demoteButton = Theme.createRedButton(
            new TextComponent("Demote"),
            () -> Rocket.getGameClientGateway().ifPresent(gateway -> gateway
                .setGuildMemberRank(
                    this.selectedMemberView.getMember().getUser().getId(),
                    this.selectedMemberView.getMember().getRank().demote())
                .subscribe())));
    this.controlsView.addChild(this.kickButton = Theme.createRedButton(
        new TextComponent("Kick"),
        () -> Rocket.getGameClientGateway().ifPresent(gateway -> gateway
            .kickGuildMember(this.selectedMemberView.getMember().getUser().getId())
            .subscribe())));
    this.controlsView.addChild(Theme.createBlueButton(new TextComponent("Invite"),
        () -> viewConsumer.accept(new UsernameDialogView(
            new TranslatableComponent("view.guild.manage_members.send_invite.message"),
            result -> {
              if (result.equalsIgnoreCase(this.minecraft.getUser().getName())) {
                RocketToast.error(this.minecraft, "Cannot invite yourself");
                viewConsumer.accept(this);
                return;
              }
              Rocket.getGameClientGateway()
                  .ifPresent(gateway -> gateway.getUserId(result)
                      .flatMap(gateway::sendGuildInvite)
                      .doOnSubscribe(__ -> RocketToast.info(this.minecraft,
                          "Sending invite to: " + result))
                      .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Invite sent"))
                      .doOnError(error -> RocketToast.error(this.minecraft, error.getMessage()))
                      .subscribe());
              viewConsumer.accept(this);
            }, () -> viewConsumer.accept(this)))));

    this.addChild(this.controlsView);

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

    var selectedMember = this.selectedMemberView.getMember();
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

  private void updateGuild(GameClientGateway gateway, GuildPayload guild) {
    if (guild == null) {
      return;
    }

    if (!guild.equals(this.guild)) {
      this.membersListView.clearChildren();
      gateway.getGuildMembers(guild.getId())
          .publishOn(Schedulers.fromExecutor(this.minecraft))
          .doOnNext(member -> this.updateMember(gateway, member))
          .doOnTerminate(this::layout)
          .subscribeOn(Schedulers.boundedElastic())
          .subscribe();
    } else {
      this.memberViews.values().forEach(view -> view.updateGuild(guild));
    }

    this.guild = guild;
  }

  private void updateMember(GameClientGateway gateway, GuildMemberPayload member) {
    if (member.getUser().equals(gateway.getUser())) {
      this.updateSelfMember(member);
    }
    var view = this.memberViews.computeIfAbsent(member.getUser().getId(),
        __ -> new MemberView(this.guild, member));
    if (view.hasParent()) {
      view.updateMember(member);
    } else {
      view.addListener(RemovedEvent.class,
          __ -> this.memberViews.remove(member.getUser().getId(), view));
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
      if (this.controlsView.getParent() != this) {
        this.addChild(this.controlsView);
        this.layout();
      }
    } else if (this.controlsView.getParent() == this) {
      this.removeChild(this.controlsView);
      this.layout();
    }
  }

  @Override
  protected void added() {
    super.added();
    this.listener = Rocket.getGameClientGatewayStream()
        .flatMap(gateway -> Mono.when(
            gateway.getSocialProfile()
                .publishOn(Schedulers.fromExecutor(this.minecraft))
                .doOnNext(profile -> this.updateGuild(gateway, profile.getGuild().orElse(null))),
            gateway.getGuildEvents()
                .ofType(GuildMemberUpdateEvent.class)
                .filter(event -> this.guild != null
                    && event.getGuildId().equals(this.guild.getId()))
                .publishOn(Schedulers.fromExecutor(this.minecraft))
                .doOnNext(event -> {
                  this.updateMember(gateway, event.getGuildMember());
                  this.membersListView.layout();
                })))
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
  }

  @Override
  protected void removed() {
    super.removed();
    this.listener.dispose();
    this.guild = null;
    this.member = null;
    this.selectedMemberView = null;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    boolean result = super.mouseClicked(mouseX, mouseY, button);
    this.updateSelected();
    return result;
  }
}
