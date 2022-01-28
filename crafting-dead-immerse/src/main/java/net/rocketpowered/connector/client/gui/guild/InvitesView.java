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
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.States;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexWrap;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Justify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.PositionType;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.google.common.collect.Sets;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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

public class InvitesView extends ParentView<InvitesView, YogaLayout, YogaLayout> {

  public static final ITextComponent TITLE = new TranslationTextComponent("view.guild.invites");

  private final ParentView<?, YogaLayout, YogaLayout> invitesView;

  private final ParentView<?, YogaLayout, YogaLayout> invitesListView;

  private final ParentView<?, YogaLayout, YogaLayout> controlsView;

  private final View<?, YogaLayout> acceptButton;
  private final View<?, YogaLayout> declineButton;

  private final Map<ObjectId, InviteView> inviteViews = new HashMap<>();

  @Nullable
  private GuildPayload guild;

  @Nullable
  private InviteView selectedInviteView;

  private Disposable listener;

  private Set<GuildInvitePayload> lastInvites = Collections.emptySet();

  public InvitesView(YogaLayout layout) {
    super(layout, new YogaLayoutParent().setFlexDirection(FlexDirection.ROW)
        .setJustifyContent(Justify.CENTER).setAlignItems(Align.CENTER));

    this.invitesView =
        new ParentView<>(
            new YogaLayout()
                .setPositionType(PositionType.ABSOLUTE)
                .setWidthPercent(90)
                .setHeightPercent(90),
            new YogaLayoutParent().setAlignItems(Align.CENTER))
                .configure(
                    view -> view.getBackgroundColorProperty().setBaseValue(new Color(0xA0363636)))
                .configure(view -> view.getBorderRadiusProperty().setBaseValue(7.5F))
                .setBackgroundBlur(50)
                .addChild(new TextView<>(new YogaLayout().setPadding(7))
                    .setText(TITLE)
                    .setCentered(true));

    this.invitesListView =
        new ParentView<>(
            new YogaLayout()
                .setFlex(1)
                .setWidthPercent(100)
                .setBottomPadding(5)
                .setFlexShrink(1)
                .setOverflow(Overflow.SCROLL),
            new YogaLayoutParent()
                .setFlexDirection(FlexDirection.ROW)
                .setFlexWrap(FlexWrap.WRAP)
                .setJustifyContent(Justify.CENTER))
                    .configure(view -> view.getBackgroundColorProperty()
                        .setBaseValue(new Color(0x40121212)));

    this.invitesView.addChild(this.invitesListView);

    this.controlsView = new ParentView<>(
        new YogaLayout().setFlexShrink(1).setWidth(200).setHeight(45),
        new YogaLayoutParent()
            .setJustifyContent(Justify.CENTER)
            .setAlignItems(Align.CENTER)
            .setFlexDirection(FlexDirection.ROW))
                .addChild(
                    this.acceptButton = Theme.createBlueButton(
                        new StringTextComponent("Accept"),
                        () -> RocketConnector.getInstance().getGameClientApi()
                            .next()
                            .flatMap(api -> api.acceptGuildInvite(
                                this.selectedInviteView.invite.getGuild().getId()))
                            .subscribe())
                        .configure(view -> view.getBackgroundColorProperty()
                            .defineState(Theme.BLUE_DISABLED, States.DISABLED))
                        .configure(view -> view.getLayout().setMargin(3)))
                .addChild(
                    this.declineButton = Theme.createRedButton(
                        new StringTextComponent("Decline"),
                        () -> RocketConnector.getInstance().getGameClientApi()
                            .next()
                            .flatMap(api -> api.declineGuildInvite(
                                this.selectedInviteView.invite.getGuild().getId()))
                            .subscribe())
                        .configure(view -> view.getBackgroundColorProperty()
                            .defineState(Theme.RED_DISABLED, States.DISABLED))
                        .configure(view -> view.getLayout().setMargin(3)));

    this.invitesView.addChild(this.controlsView);

    this.addChild(this.invitesView);

    this.updateSelected();
  }

  protected void updateSelected() {
    this.selectedInviteView = this.invitesListView.getChildViews().stream()
        .filter(InviteView.class::isInstance)
        .map(InviteView.class::cast)
        .filter(View::isSelected)
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
                    InviteView view = this.inviteViews.remove(invite.getGuild().getId());
                    if (view != null && view.isAdded()) {
                      this.invitesListView.removeChild(view);
                    }
                  });

              profile.getGuildInvites().forEach(invite -> {
                InviteView view =
                    this.inviteViews.computeIfAbsent(invite.getGuild().getId(),
                        __ -> new InviteView(invite));
                if (!view.isAdded()) {
                  this.invitesListView.addChild(view);
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

  private static class InviteView extends ParentView<InviteView, YogaLayout, YogaLayout> {

    private final GuildInvitePayload invite;

    private final TextView<YogaLayout> totalMembersView;
    private final TextView<YogaLayout> onlineMemebrsView;
    private final TextView<YogaLayout> offlineMemebrsView;

    private Disposable memberListener;

    public InviteView(GuildInvitePayload invite) {
      super(
          new YogaLayout()
              .setWidthPercent(80)
              .setHeight(40)
              .setPadding(3)
              .setMargin(5),
          new YogaLayoutParent()
              .setFlexDirection(FlexDirection.COLUMN));

      this.invite = invite;

      this.getBackgroundColorProperty().setBaseValue(new Color(0xA0333333));
      this.getOutlineColorProperty()
          .defineState(Color.WHITE, States.SELECTED)
          .defineState(Color.GRAY, States.HOVERED);
      this.getOutlineWidthProperty()
          .defineState(1.0F, States.SELECTED)
          .defineState(1.0F, States.HOVERED);
      this.setUnscaleBorder(false);

      this.totalMembersView = new TextView<>(new YogaLayout());
      this.onlineMemebrsView = new TextView<>(new YogaLayout());
      this.onlineMemebrsView.getColorProperty().setBaseValue(Theme.ONLINE);
      this.offlineMemebrsView = new TextView<>(new YogaLayout());
      this.offlineMemebrsView.getColorProperty().setBaseValue(Color.GRAY);

      this.addChild(new TextView<>(new YogaLayout())
          .setText(new StringTextComponent(this.invite.getGuild().getName())
              .withStyle(TextFormatting.BOLD)));
      this.addChild(new ParentView<>(new YogaLayout().setFlex(1),
          new YogaLayoutParent().setFlexDirection(FlexDirection.ROW))
              .addChild(this.totalMembersView)
              .addChild(new TextView<>(new YogaLayout())
                  .setText(new StringTextComponent(" | ")))
              .addChild(this.onlineMemebrsView)
              .addChild(new TextView<>(new YogaLayout())
                  .setText(new StringTextComponent(" | ")))
              .addChild(this.offlineMemebrsView));
      this.invite.getSender().ifPresent(sender -> this.addChild(new TextView<>(new YogaLayout())
          .setText(new StringTextComponent("Invited by " + sender.getMinecraftProfile().getName())
              .withStyle(TextFormatting.ITALIC))));
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
                      this.offlineMemebrsView.setText(new StringTextComponent(count + " Offline"));
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
      boolean result = super.mouseClicked(mouseX, mouseY, button);
      this.setSelected(this.isHovered());
      return result;
    }
  }
}
