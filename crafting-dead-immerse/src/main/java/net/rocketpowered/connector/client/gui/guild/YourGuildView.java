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

  @Nullable
  private GuildPayload guild;

  private Disposable listener;

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
  }

  private void updateGuild(GameClientGateway gateway, GuildPayload guild) {
    if (guild == null) {
      return;
    }

    if (!guild.equals(this.guild)) {
      this.membersView.clearChildren();
      gateway.getGuildMembers(guild.getId())
          .publishOn(Schedulers.fromExecutor(this.minecraft))
          .doOnNext(member -> this.updateMember(gateway, member))
          .doOnTerminate(this::layout)
          .subscribeOn(Schedulers.boundedElastic())
          .subscribe();
    } else {
      this.memberViews.values().forEach(view -> view.updateGuild(guild));
    }

    this.informationView.clearChildren();

    this.informationView.addChild(new TextView(new Properties<>())
        .setText(new TextComponent("")
            .append(new TextComponent("Name: ")
                .withStyle(ChatFormatting.GRAY))
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

    var controlsView = new ParentView(new Properties<>().id("controls"));
    this.informationView.addChild(controlsView);

    if (guild.getOwner().getMinecraftProfile().getId().equals(
        this.minecraft.getUser().getGameProfile().getId())) {
      controlsView.addChild(Theme.createRedButton(new TextComponent("Transfer"),
          () -> this.contentConsumer.accept(new UsernameDialogView(
              new TranslatableComponent("view.guild.your_guild.transfer.message"),
              result -> {
                gateway.getUserId(result)
                    .flatMap(gateway::transferGuildOwnership)
                    .doOnSubscribe(__ -> RocketToast.info(this.minecraft,
                        "Transferring guild: " + guild.getName()))
                    .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Guild transferred"))
                    .doOnError(error -> RocketToast.error(this.minecraft, error.getMessage()))
                    .subscribe();
                this.contentConsumer.accept(this);
              },
              () -> this.contentConsumer.accept(this)))));
      controlsView.addChild(Theme.createRedButton(new TextComponent("Delete"),
          () -> this.contentConsumer.accept(new ConfirmDialogView(
              new TranslatableComponent("view.guild.your_guild.delete.message",
                  this.guild.getName()),
              () -> {
                gateway.deleteGuild()
                    .doOnSubscribe(
                        __ -> RocketToast.info(this.minecraft,
                            "Deleting guild: " + guild.getName()))
                    .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Guild deleted"))
                    .doOnError(error -> RocketToast.error(this.minecraft, error.getMessage()))
                    .subscribe();
                this.contentConsumer.accept(this);
              },
              () -> this.contentConsumer.accept(this)))));
    } else {
      controlsView.addChild(Theme.createRedButton(new TextComponent("Leave"),
          () -> this.contentConsumer.accept(new ConfirmDialogView(
              new TranslatableComponent("view.guild.your_guild.leave.message",
                  this.guild.getName()),
              () -> {
                gateway.leaveGuild()
                    .doOnSubscribe(
                        __ -> RocketToast.info(this.minecraft,
                            "Leaving guild: " + guild.getName()))
                    .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Left guild"))
                    .doOnError(error -> RocketToast.error(this.minecraft, error.getMessage()))
                    .subscribe();
                this.contentConsumer.accept(this);
              },
              () -> this.contentConsumer.accept(this)))));
    }

    this.informationView.layout();

    this.guild = guild;
  }

  private void updateMember(GameClientGateway gateway, GuildMemberPayload member) {
    var view = this.memberViews.computeIfAbsent(member.getUser().getId(),
        __ -> new MemberView(this.guild, member));
    if (view.hasParent()) {
      view.updateMember(member);
    } else {
      view.addListener(RemovedEvent.class,
          __ -> this.memberViews.remove(member.getUser().getId(), view));
      this.membersView.addChild(view);
    }
  }

  @Override
  protected void added() {
    super.added();
    this.listener = Rocket.getGameClientGatewayStream()
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
    this.guild = null;
  }
}
