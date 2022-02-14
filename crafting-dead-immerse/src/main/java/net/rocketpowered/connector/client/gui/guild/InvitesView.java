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

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.View;
import com.google.common.collect.Sets;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.rocketpowered.connector.RocketConnector;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.payload.GuildInvitePayload;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.payload.GuildMemberPayload;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.payload.GuildPayload;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.payload.UserPayload;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.payload.UserPresencePayload;
import net.rocketpowered.connector.internal.shaded.org.bson.types.ObjectId;
import net.rocketpowered.connector.internal.shaded.reactor.core.Disposable;
import net.rocketpowered.connector.internal.shaded.reactor.core.publisher.Flux;
import net.rocketpowered.connector.internal.shaded.reactor.core.publisher.Mono;
import net.rocketpowered.connector.internal.shaded.reactor.core.scheduler.Schedulers;

public class InvitesView extends ParentView {

  public static final Component TITLE = new TranslatableComponent("view.guild.invites");

  private final ParentView dialogView;

  private final ParentView invitesListView;

  private final ParentView controlsView;

  private final View acceptButton;
  private final View declineButton;

  private final Map<ObjectId, InviteView> inviteViews = new HashMap<>();

  @Nullable
  private GuildPayload guild;

  @Nullable
  private InviteView selectedInviteView;

  private Disposable listener;

  private Set<GuildInvitePayload> lastInvites = Collections.emptySet();

  public InvitesView() {
    super(new Properties<>());

    this.dialogView =
        new ParentView(new Properties<>().styleClasses("dialog").backgroundBlur(50));
    this.dialogView.addChild(new TextView(new Properties<>().id("title"))
        .setText(TITLE)
        .setCentered(true));

    this.invitesListView = new ParentView(new Properties<>().id("list"));

    this.dialogView.addChild(this.invitesListView);

    this.controlsView = new ParentView(new Properties<>().id("controls"));
    this.controlsView.addChild(
        this.acceptButton = Theme.createBlueButton(
            new TextComponent("Accept"),
            () -> RocketConnector.getInstance().getGameClientApi()
                .next()
                .flatMap(api -> api.acceptGuildInvite(
                    this.selectedInviteView.invite.getGuild().getId()))
                .subscribe()));
    this.controlsView.addChild(
        this.declineButton = Theme.createRedButton(
            new TextComponent("Decline"),
            () -> RocketConnector.getInstance().getGameClientApi()
                .next()
                .flatMap(api -> api.declineGuildInvite(
                    this.selectedInviteView.invite.getGuild().getId()))
                .subscribe()));

    this.dialogView.addChild(this.controlsView);

    this.addChild(this.dialogView);

    this.updateSelected();
  }

  protected void updateSelected() {
    this.selectedInviteView = this.invitesListView.getChildViews().stream()
        .filter(InviteView.class::isInstance)
        .map(InviteView.class::cast)
        .filter(View::isFocused)
        .findAny()
        .orElse(null);

    this.acceptButton.setEnabled(this.selectedInviteView != null);
    this.declineButton.setEnabled(this.selectedInviteView != null);
  }

  @Override
  protected void added() {
    super.added();
    this.listener = RocketConnector.getInstance().getGameClientApi()
        .flatMap(api -> api.getSocialProfile()
            .publishOn(Schedulers.fromExecutor(this.minecraft))
            .doOnNext(profile -> {
              Sets.difference(this.lastInvites, profile.getGuildInvites())
                  .forEach(invite -> {
                    var view = this.inviteViews.remove(invite.getGuild().getId());
                    if (view != null && view.isAdded()) {
                      this.invitesListView.removeChild(view);
                      this.updateSelected();
                    }
                  });

              profile.getGuildInvites().forEach(invite -> {
                var view =
                    this.inviteViews.computeIfAbsent(invite.getGuild().getId(),
                        __ -> new InviteView(invite));
                if (!view.isAdded()) {
                  this.invitesListView.addChild(view);
                  this.updateSelected();
                }
              });

              this.invitesListView.layout();
              this.lastInvites = profile.getGuildInvites();
            }))
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

  private static class InviteView extends ParentView {

    private final GuildInvitePayload invite;

    private final TextView totalMembersView;
    private final TextView onlineMemebrsView;
    private final TextView offlineMemebrsView;

    private Disposable memberListener;

    public InviteView(GuildInvitePayload invite) {
      super(new Properties<>().styleClasses("item").unscaleBorder(false));

      this.invite = invite;

      this.totalMembersView = new TextView(new Properties<>());
      this.onlineMemebrsView = new TextView(new Properties<>());
      this.onlineMemebrsView.getColorProperty().defineState(Theme.ONLINE);
      this.offlineMemebrsView = new TextView(new Properties<>());
      this.offlineMemebrsView.getColorProperty().defineState(Color.GRAY);

      this.addChild(new TextView(new Properties<>())
          .setText(new TextComponent(this.invite.getGuild().getName())
              .withStyle(ChatFormatting.BOLD)));

      var playerCountsView = new ParentView(new Properties<>().id("player-counts"));

      this.addChild(playerCountsView);
      playerCountsView.addChild(this.totalMembersView);
      playerCountsView.addChild(new TextView(new Properties<>())
          .setText(new TextComponent(" | ")));
      playerCountsView.addChild(this.onlineMemebrsView);
      playerCountsView.addChild(new TextView(new Properties<>())
          .setText(new TextComponent(" | ")));
      playerCountsView.addChild(this.offlineMemebrsView);
      this.invite.getSender().ifPresent(sender -> this.addChild(new TextView(new Properties<>())
          .setText(new TextComponent("Invited by " + sender.getMinecraftProfile().getName())
              .withStyle(ChatFormatting.ITALIC))));
    }

    @Override
    protected boolean isFocusable() {
      return true;
    }

    @Override
    protected void added() {
      super.added();
      AtomicInteger counter = new AtomicInteger();
      this.memberListener = RocketConnector.getInstance().getGameClientApi()
          .flatMap(api -> Mono
              .fromRunnable(() -> this.minecraft.executeBlocking(() -> {
                this.onlineMemebrsView.setText("0 Online");
                this.offlineMemebrsView.setText("0 Offline");
              }))
              .thenMany(api.getGuildMembers(this.invite.getGuild().getId()))
              .doOnNext(__ -> counter.incrementAndGet())
              .doOnComplete(() -> {
                int count = counter.getAndSet(0);
                if (count == 1) {
                  this.totalMembersView.setText("1 Member");
                } else {
                  this.totalMembersView.setText(count + " Members");
                }
              })
              .map(GuildMemberPayload::getUser)
              .map(UserPayload::getId)
              .flatMap(userId -> api.getUserPresence(userId).next())
              .groupBy(UserPresencePayload::isOnline)
              .publishOn(Schedulers.fromExecutor(this.minecraft))
              .delayUntil(group -> group.count()
                  .doOnNext(count -> {
                    if (group.key()) {
                      this.onlineMemebrsView.setText(count + " Online");
                    } else {
                      this.offlineMemebrsView.setText(new TextComponent(count + " Offline"));
                    }
                  }))
              // Update member counts every minute.
              .repeatWhen(flux -> Flux.interval(Duration.ofMinutes(1L))))
          .subscribeOn(Schedulers.boundedElastic())
          .subscribe();
    }

    @Override
    protected void removed() {
      super.removed();
      this.memberListener.dispose();
    }
  }
}
