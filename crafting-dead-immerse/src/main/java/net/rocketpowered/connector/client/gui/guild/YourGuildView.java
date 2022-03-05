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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.rocketpowered.api.Rocket;
import net.rocketpowered.api.gateway.GameClientGateway;
import net.rocketpowered.common.GuildPermission;
import net.rocketpowered.common.RocketException;
import net.rocketpowered.common.payload.GuildMemberPayload;
import net.rocketpowered.common.payload.GuildMemberUpdateEvent;
import net.rocketpowered.common.payload.GuildPayload;
import net.rocketpowered.connector.client.gui.RocketToast;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class YourGuildView extends ParentView {

  public static final Component TITLE = new TranslatableComponent("view.guild.your_guild");

  private final ParentView informationView;

  private final ParentView membersView;

  private final Map<ObjectId, MemberView> memberViews = new HashMap<>();

  private final Consumer<View> contentConsumer;

  private final View transferButton;
  private final View deleteButton;
  private final View renameButton;
  private final View leaveButton;

  @Nullable
  private GuildPayload guild;

  private Disposable listener;

  @Nullable
  private GuildMemberPayload selfMember;

  public YourGuildView(Consumer<View> contentConsumer) {
    super(new Properties<>().styleClasses("page").backgroundBlur(50));

    this.contentConsumer = contentConsumer;

    this.addChild(new TextView(new Properties<>().id("title"))
        .setText(TITLE)
        .setCentered(true));

    var guildView = new ParentView(new Properties<>().id("guild"));
    this.addChild(guildView);

    guildView.addChild(
        this.informationView = new ParentView(new Properties<>().id("information")));

    guildView.addChild(
        this.membersView = new ParentView(new Properties<>().id("members")));

    this.transferButton = Theme.createRedButton(new TextComponent("Transfer"),
        () -> this.contentConsumer.accept(new TextDialogView(
            new TranslatableComponent("view.guild.your_guild.transfer.message"),
            new TranslatableComponent("view.guild.text_dialog.username"),
            result -> {
              Rocket.getGameClientGateway().ifPresent(gateway -> gateway.getUserId(result)
                  .flatMap(gateway::transferGuildOwnership)
                  .doOnSubscribe(__ -> RocketToast.info(this.minecraft,
                      "Transferring guild: " + this.guild.name()))
                  .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Guild transferred"))
                  .doOnError(RocketException.class,
                      error -> RocketToast.error(this.minecraft, error.getMessage()))
                  .subscribe());
              this.contentConsumer.accept(this);
            },
            () -> this.contentConsumer.accept(this))));

    this.deleteButton = Theme.createRedButton(new TextComponent("Delete"),
        () -> this.contentConsumer.accept(new ConfirmDialogView(
            new TranslatableComponent("view.guild.your_guild.delete.message",
                this.guild.name()),
            () -> {
              Rocket.getGameClientGateway().ifPresent(gateway -> gateway.deleteGuild()
                  .doOnSubscribe(
                      __ -> RocketToast.info(this.minecraft,
                          "Deleting guild: " + guild.name()))
                  .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Guild deleted"))
                  .doOnError(RocketException.class,
                      error -> RocketToast.error(this.minecraft, error.getMessage()))
                  .subscribe());
              this.contentConsumer.accept(this);
            },
            () -> this.contentConsumer.accept(this))));

    this.renameButton = Theme.createBlueButton(new TextComponent("Rename"),
        () -> this.contentConsumer.accept(new TextDialogView(
            new TranslatableComponent("view.guild.your_guild.rename.message"),
            new TranslatableComponent("view.guild.text_dialog.name"),
            result -> {
              Rocket.getGameClientGateway().ifPresent(gateway -> gateway.renameGuild(result)
                  .doOnSubscribe(__ -> RocketToast.info(this.minecraft,
                      "Renaming guild: " + this.guild.name()))
                  .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Guild renamed"))
                  .doOnError(RocketException.class,
                      error -> RocketToast.error(this.minecraft, error.getMessage()))
                  .subscribe());
              this.contentConsumer.accept(this);
            },
            () -> this.contentConsumer.accept(this))));

    this.leaveButton = Theme.createRedButton(new TextComponent("Leave"),
        () -> this.contentConsumer.accept(new ConfirmDialogView(
            new TranslatableComponent("view.guild.your_guild.leave.message",
                this.guild.name()),
            () -> {
              Rocket.getGameClientGateway().ifPresent(gateway -> gateway.leaveGuild()
                  .doOnSubscribe(
                      __ -> RocketToast.info(this.minecraft,
                          "Leaving guild: " + this.guild.name()))
                  .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Left guild"))
                  .doOnError(RocketException.class,
                      error -> RocketToast.error(this.minecraft, error.getMessage()))
                  .subscribe());
              this.contentConsumer.accept(this);
            },
            () -> this.contentConsumer.accept(this))));
  }

  private void updateGuild(GameClientGateway gateway, GuildPayload guild) {
    if (guild == null) {
      return;
    }

    if (!guild.equals(this.guild)) {
      this.membersView.clearChildren();
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

    if (this.selfMember != null) {
      this.updateInformation(gateway);
    }
  }

  private void updateInformation(GameClientGateway gateway) {
    this.informationView.clearChildren();

    this.informationView.addChild(new TextView(new Properties<>())
        .setText(new TextComponent("")
            .append(new TextComponent("Name: ")
                .withStyle(ChatFormatting.GRAY))
            .append(this.guild.name()))
        .setShadow(false));
    this.informationView.addChild(new TextView(new Properties<>())
        .setText(new TextComponent("")
            .append(new TextComponent("Tag: ")
                .withStyle(ChatFormatting.GRAY))
            .append(this.guild.tag()))
        .setShadow(false));
    this.informationView.addChild(new TextView(new Properties<>())
        .setText(new TextComponent("")
            .append(new TextComponent("Owner: ")
                .withStyle(ChatFormatting.GRAY))
            .append(this.guild.owner().minecraftProfile().name()))
        .setShadow(false));

    var controlsView = new ParentView(new Properties<>().id("controls"));
    this.informationView.addChild(controlsView);

    var permissions = this.guild.getPermissions(this.selfMember);

    if (GuildPermission.RENAME.hasPermission(permissions)) {
      controlsView.forceAddChild(this.renameButton);
    }

    if (this.guild.isOwner(gateway.user())) {
      controlsView.forceAddChild(this.transferButton);
      controlsView.forceAddChild(this.deleteButton);
    } else {
      controlsView.forceAddChild(this.leaveButton);
    }

    this.informationView.layout();
  }

  private void updateMember(GameClientGateway gateway, GuildMemberPayload member) {
    if (member.user().equals(gateway.user())) {
      this.selfMember = member;
      this.updateInformation(gateway);
    }

    var view = this.memberViews.computeIfAbsent(member.user().id(),
        __ -> new MemberView(this.guild, member));
    if (view.hasParent()) {
      view.updateMember(member);
    } else {
      view.addListener(RemovedEvent.class, __ -> this.memberViews.remove(member.user().id(), view));
      this.membersView.addChild(view);
    }
  }

  @Override
  protected void added() {
    super.added();
    this.listener = Rocket.getGameClientGatewayFeed()
        .flatMap(api -> Mono.when(
            api.getSocialProfileFeed()
                .publishOn(Schedulers.fromExecutor(this.minecraft))
                .doOnNext(profile -> this.updateGuild(api, profile.guild())),
            api.getGuildEventFeed()
                .ofType(GuildMemberUpdateEvent.class)
                .filter(event -> this.guild != null
                    && event.guildId().equals(this.guild.id()))
                .publishOn(Schedulers.fromExecutor(this.minecraft))
                .doOnNext(event -> this.updateMember(api, event.guildMember()))))
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
  }

  @Override
  protected void removed() {
    super.removed();
    this.listener.dispose();
  }
}
