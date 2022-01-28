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
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexWrap;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Justify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.PositionType;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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

public class YourGuildView extends ParentView<ManageMembersView, YogaLayout, YogaLayout> {

  public static final ITextComponent TITLE = new TranslationTextComponent("view.guild.your_guild");

  private final ParentView<?, YogaLayout, YogaLayout> fieldsView;

  private final ParentView<?, YogaLayout, YogaLayout> membersListView;

  private final Map<ObjectId, MemberView> memberViews = new HashMap<>();

  @Nullable
  private GuildPayload guild;

  private Disposable listener;

  public YourGuildView(YogaLayout layout) {
    super(layout, new YogaLayoutParent().setFlexDirection(FlexDirection.ROW)
        .setJustifyContent(Justify.CENTER).setAlignItems(Align.CENTER));

    ParentView<?, YogaLayout, YogaLayout> windowView =
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
                .addChild(new TextView<>(new YogaLayout().setPadding(7)).setText(TITLE)
                    .setCentered(true));

    this.fieldsView = new ParentView<>(
        new YogaLayout()
            .setHeightPercent(100)
            .setWidthPercent(33)
            .setRightBorderWidth(1),
        new YogaLayoutParent())
            .configure(view -> view.getBackgroundColorProperty()
                .setBaseValue(new Color(0x40121212)));
    this.fieldsView.getRightBorderColorProperty().setBaseValue(new Color(125, 125, 125, 125));

    this.membersListView =
        new ParentView<>(
            new YogaLayout()
                .setFlex(1)
                .setHeightPercent(100)
                .setBottomPadding(5)
                .setOverflow(Overflow.SCROLL),
            new YogaLayoutParent()
                .setFlexDirection(FlexDirection.ROW)
                .setFlexWrap(FlexWrap.WRAP)
                .setJustifyContent(Justify.CENTER));
    
    this.membersListView.getBackgroundColorProperty().setBaseValue(new Color(0x25121212));

    windowView.addChild(new ParentView<>(new YogaLayout().setWidthPercent(100).setFlex(1),
        new YogaLayoutParent().setFlexDirection(FlexDirection.ROW))
            .addChild(this.fieldsView)
            .addChild(this.membersListView));

    this.addChild(windowView);
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

    this.fieldsView.clearChildren();

    this.fieldsView.addChild(new TextView<>(new YogaLayout().setPadding(5))
        .setText(new StringTextComponent("")
            .append(new StringTextComponent("Name: ")
                .withStyle(TextFormatting.GOLD))
            .append(guild.getName()))
        .setShadow(false));
    this.fieldsView.addChild(new TextView<>(new YogaLayout().setPadding(5))
        .setText(new StringTextComponent("")
            .append(new StringTextComponent("Tag: ")
                .withStyle(TextFormatting.GRAY))
            .append(guild.getTag()))
        .setShadow(false));
    this.fieldsView.addChild(new TextView<>(new YogaLayout().setPadding(5))
        .setText(new StringTextComponent("")
            .append(new StringTextComponent("Owner: ")
                .withStyle(TextFormatting.GRAY))
            .append(guild.getOwner().getMinecraftProfile().getName()))
        .setShadow(false));


    this.fieldsView.layout();


    this.guild = guild;
  }

  private void updateMember(GameClientApi api, GuildMemberPayload member) {
    MemberView view =
        this.memberViews.computeIfAbsent(member.getUser().getId(), __ -> new MemberView(member));
    if (view.isAdded()) {
      view.updateMember(member);
    } else {
      this.membersListView.addChild(view);
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

      if (member.getUser().equals(YourGuildView.this.guild.getOwner())) {
        color = Color.GOLD;
      } else if (rank == GuildRank.DIGNITARY) {
        color = Color.DARK_PURPLE;
      } else if (rank == GuildRank.ENVOY) {
        color = Color.AQUA;
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
      boolean result = super.mouseClicked(mouseX, mouseY, button);
      this.setSelected(this.isHovered());
      return result;
    }
  }
}
