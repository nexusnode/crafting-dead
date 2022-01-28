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
import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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

public class ManageMembersView extends ParentView<ManageMembersView, YogaLayout, YogaLayout> {

  public static final ITextComponent TITLE =
      new TranslationTextComponent("view.guild.manage_members");

  private final ParentView<?, YogaLayout, YogaLayout> membersView;

  private final ParentView<?, YogaLayout, YogaLayout> membersListView;

  private final ParentView<?, YogaLayout, YogaLayout> controlsView;

  private final View<?, YogaLayout> promoteButton;
  private final View<?, YogaLayout> demoteButton;
  private final View<?, YogaLayout> kickButton;

  private final Map<ObjectId, MemberView> memberViews = new HashMap<>();

  @Nullable
  private GuildPayload guild;

  @Nullable
  private GuildMemberPayload member;

  @Nullable
  private MemberView selectedMemberView;

  private Disposable listener;

  public ManageMembersView(YogaLayout layout, Consumer<View<?, YogaLayout>> viewConsumer) {
    super(layout, new YogaLayoutParent().setFlexDirection(FlexDirection.ROW)
        .setJustifyContent(Justify.CENTER).setAlignItems(Align.CENTER));

    this.membersView =
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

    this.membersListView =
        new ParentView<>(
            new YogaLayout()
                .setFlex(1)
                .setBottomPadding(5)
                .setWidthPercent(100)
                .setOverflow(Overflow.SCROLL),
            new YogaLayoutParent()
                .setFlexDirection(FlexDirection.ROW)
                .setFlexWrap(FlexWrap.WRAP)
                .setJustifyContent(Justify.CENTER))
                    .configure(view -> view.getBackgroundColorProperty()
                        .setBaseValue(new Color(0x40121212)));

    this.membersView.addChild(this.membersListView);

    this.controlsView = new ParentView<>(
        new YogaLayout().setWidthPercent(100).setMaxWidth(250).setHeight(45),
        new YogaLayoutParent()
            .setJustifyContent(Justify.CENTER)
            .setAlignItems(Align.CENTER)
            .setFlexDirection(FlexDirection.ROW))
                .addChild(
                    this.promoteButton = Theme.createBlueButton(
                        new StringTextComponent("Promote"),
                        () -> RocketConnector.getInstance().getGameClientApi()
                            .next()
                            .flatMap(api -> api.setGuildMemberRank(
                                this.selectedMemberView.member.getUser().getId(),
                                this.selectedMemberView.member.getRank().promote()))
                            .subscribe())
                        .configure(view -> view.getBackgroundColorProperty()
                            .defineState(Theme.BLUE_DISABLED, States.DISABLED))
                        .configure(view -> view.getLayout().setMargin(3)))
                .addChild(
                    this.demoteButton = Theme.createRedButton(
                        new StringTextComponent("Demote"),
                        () -> RocketConnector.getInstance().getGameClientApi()
                            .next()
                            .flatMap(api -> api.setGuildMemberRank(
                                this.selectedMemberView.member.getUser().getId(),
                                this.selectedMemberView.member.getRank().demote()))
                            .subscribe())
                        .configure(view -> view.getBackgroundColorProperty()
                            .defineState(Theme.RED_DISABLED, States.DISABLED))
                        .configure(view -> view.getLayout().setMargin(3)))
                .addChild(this.kickButton = Theme.createRedButton(
                    new StringTextComponent("Kick"),
                    () -> RocketConnector.getInstance().getGameClientApi()
                        .next()
                        .flatMap(api -> api.kickGuildMember(
                            this.selectedMemberView.member.getUser().getId()))
                        .subscribe())
                    .configure(view -> view.getBackgroundColorProperty()
                        .defineState(Theme.RED_DISABLED, States.DISABLED))
                    .configure(view -> view.getLayout().setMargin(3)))
                .addChild(Theme.createBlueButton(new StringTextComponent("Invite"),
                    () -> viewConsumer
                        .accept(new SendInviteView(new YogaLayout().setFlex(1), result -> {
                          if (!Strings.isNullOrEmpty(result)) {
                            RocketConnector.getInstance().getGameClientApi()
                                .flatMap(api -> api.getUserId(result).flatMap(api::sendGuildInvite))
                                .subscribe();
                          }
                          viewConsumer.accept(this);
                        })))
                    .configure(view -> view.getLayout().setMargin(3)));

    this.membersView.addChild(this.controlsView);

    this.addChild(this.membersView);

    this.updateSelected();
  }

  protected void updateSelected() {
    this.selectedMemberView = this.membersListView.getChildViews().stream()
        .filter(MemberView.class::isInstance)
        .map(MemberView.class::cast)
        .filter(View::isSelected)
        .findAny()
        .orElse(null);

    if (this.selectedMemberView == null) {
      this.promoteButton.setEnabled(false);
      this.demoteButton.setEnabled(false);
      this.kickButton.setEnabled(false);
      return;
    }

    GuildMemberPayload selectedMember = this.selectedMemberView.member;
    GuildRank selectedMemberRank = selectedMember.getRank();
    boolean selectedMemberOwner = selectedMember.isOwner(this.guild);

    GuildRank selfRank = this.member.getRank();
    long selfPermissions = this.member.getPermissions(this.guild);
    boolean selfOwner = this.member.isOwner(this.guild);
    boolean manageRanks = GuildPermission.MANAGE_RANKS.hasPermission(selfPermissions);

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
      // this.minecraft.setScreen(new MainMenuScreen());
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
      if (this.controlsView.getParent() != this.membersView) {
        this.membersView.addChild(this.controlsView);
        this.layout();
      }
    } else if (this.controlsView.getParent() == this.membersView) {
      this.membersView.removeChild(this.controlsView);
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

  private class MemberView extends ParentView<MemberView, YogaLayout, YogaLayout> {

    private GuildMemberPayload member;

    private Disposable removeListener;
    private Disposable presenceListener;

    private final AvatarView<YogaLayout> avatarView;
    private final TextView<YogaLayout> nameView;

    public MemberView(GuildMemberPayload member) {
      super(
          new YogaLayout()
              .setWidth(110)
              .setHeight(25)
              .setLeftBorderWidth(1)
              .setPadding(3)
              .setMargin(5),
          new YogaLayoutParent()
              .setFlexDirection(FlexDirection.ROW)
              .setAlignItems(Align.CENTER));

      this.updateMember(member);

      this.getBackgroundColorProperty().setBaseValue(new Color(0xA0333333));

      this.getOutlineColorProperty()
          .defineState(Color.WHITE, States.SELECTED)
          .defineState(Color.GRAY, States.HOVERED);
      this.getOutlineWidthProperty()
          .defineState(1.0F, States.SELECTED)
          .defineState(1.0F, States.HOVERED);
      this.setUnscaleBorder(false);

      this.avatarView = new AvatarView<>(new YogaLayout()
          .setWidth(20)
          .setBorderWidth(1.0F)
          .setRightMargin(3)
          .setAspectRatio(1),
          new GameProfile(member.getUser().getMinecraftProfile().getId(), null));
      this.avatarView.getBorderColorProperty().setBaseValue(Theme.OFFLINE);
      this.addChild(this.avatarView);

      this.nameView = new TextView<>(new YogaLayout().setFlexShrink(1))
          .setWrap(false)
          .setText(member.getUser().getMinecraftProfile().getName());
      this.nameView.getColorProperty().setBaseValue(Theme.OFFLINE);
      this.addChild(this.nameView);
    }

    private void updateMember(GuildMemberPayload member) {
      this.member = member;

      GuildRank rank = member.getRank();

      Color color = Color.GRAY;

      if (member.getUser().equals(ManageMembersView.this.guild.getOwner())) {
        color = Color.GOLD;
      } else if (rank == GuildRank.DIGNITARY) {
        color = Color.DARK_PURPLE;
      } else if (rank == GuildRank.ENVOY) {
        color = Color.AQUA;
      } else if (member.equals(ManageMembersView.this.member)) {
        color = Theme.ONLINE;
      }

      this.getLeftBorderColorProperty().setBaseValue(color);
    }

    private void updatePresence(UserPresencePayload presence) {
      Color color = presence.isOnline() ? Theme.ONLINE : Theme.OFFLINE;
      this.avatarView.getBorderColorProperty().setBaseValue(color);
      this.nameView.getColorProperty().setBaseValue(color);
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
      boolean result = super.mouseClicked(mouseX, mouseY, button);
      this.setSelected(this.isHovered());
      return result;
    }
  }
}
