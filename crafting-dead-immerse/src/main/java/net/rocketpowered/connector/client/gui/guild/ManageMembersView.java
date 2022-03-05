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
  private final View inviteButton;

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
                    this.selectedMemberView.getMember().user().id(),
                    this.selectedMemberView.getMember().rank().promote())
                .subscribe()),
            "promote-button"));
    this.controlsView.addChild(
        this.demoteButton = Theme.createRedButton(
            new TextComponent("Demote"),
            () -> Rocket.getGameClientGateway().ifPresent(gateway -> gateway
                .setGuildMemberRank(
                    this.selectedMemberView.getMember().user().id(),
                    this.selectedMemberView.getMember().rank().demote())
                .subscribe())));
    this.controlsView.addChild(this.kickButton = Theme.createRedButton(
        new TextComponent("Kick"),
        () -> Rocket.getGameClientGateway().ifPresent(gateway -> gateway
            .kickGuildMember(this.selectedMemberView.getMember().user().id())
            .subscribe())));
    this.controlsView
        .addChild(this.inviteButton = Theme.createBlueButton(new TextComponent("Invite"),
            () -> viewConsumer.accept(new TextDialogView(
                new TranslatableComponent("view.guild.manage_members.send_invite.message"),
                new TranslatableComponent("view.guild.text_dialog.username"),
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
    var selectedMemberRank = selectedMember.rank();
    var selectedMemberLower = this.guild.isOwner(this.member)
        || selectedMemberRank.ordinal() < this.member.rank().ordinal();

    var permissions = this.guild.getPermissions(this.member);
    var manageRanks = !this.guild.isOwner(selectedMember)
        && GuildPermission.MANAGE_RANKS.hasPermission(permissions);

    this.promoteButton.setEnabled(!selectedMemberRank.isHighest()
        && selectedMemberLower
        && manageRanks);
    this.demoteButton.setEnabled(!selectedMemberRank.isLowest()
        && selectedMemberLower
        && manageRanks);
    this.kickButton.setEnabled(!selectedMember.equals(this.member)
        && selectedMemberLower
        && GuildPermission.KICK.hasPermission(permissions));
  }

  private void updateGuild(GameClientGateway gateway, GuildPayload guild) {
    if (guild == null) {
      return;
    }

    if (!guild.equals(this.guild)) {
      this.membersListView.clearChildren();
      gateway.getGuildMembers(guild.id())
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
    if (member.user().equals(gateway.user())) {
      this.updateSelfMember(member);
    }
    var view = this.memberViews.computeIfAbsent(member.user().id(),
        __ -> new MemberView(this.guild, member));
    if (view.hasParent()) {
      view.updateMember(member);
    } else {
      view.addListener(RemovedEvent.class,
          __ -> this.memberViews.remove(member.user().id(), view));
      this.membersListView.addChild(view);
    }
  }

  private void updateSelfMember(GuildMemberPayload member) {
    this.member = member;
    var permissions = this.guild.getPermissions(member);

    this.inviteButton.setEnabled(GuildPermission.INVITE.hasPermission(permissions));

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
    this.listener = Rocket.getGameClientGatewayFeed()
        .flatMap(gateway -> Mono.when(
            gateway.getSocialProfileFeed()
                .publishOn(Schedulers.fromExecutor(this.minecraft))
                .doOnNext(profile -> this.updateGuild(gateway, profile.guild())),
            gateway.getGuildEventFeed()
                .ofType(GuildMemberUpdateEvent.class)
                .filter(event -> this.guild != null
                    && event.getGuildId().equals(this.guild.id()))
                .publishOn(Schedulers.fromExecutor(this.minecraft))
                .doOnNext(event -> {
                  this.updateMember(gateway, event.guildMember());
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
