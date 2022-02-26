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

import java.util.Set;
import javax.annotation.Nullable;
import com.craftingdead.immerse.client.gui.view.PanoramaView;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.google.common.base.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.rocketpowered.api.Rocket;
import net.rocketpowered.api.gateway.GameClientGateway;
import net.rocketpowered.common.GuildPermission;
import net.rocketpowered.common.payload.GuildInvitePayload;
import net.rocketpowered.common.payload.GuildMemberPayload;
import net.rocketpowered.common.payload.GuildPayload;
import net.rocketpowered.common.payload.SocialProfilePayload;
import net.rocketpowered.connector.client.gui.RocketToast;
import reactor.core.Disposable;
import reactor.core.scheduler.Schedulers;

public class GuildView extends ParentView {

  private final ParentView contentView = new ParentView(new Properties<>().id("content"));

  private final ParentView sideBarView;

  private final YourGuildView yourGuildView;
  private final InvitesView invitesView;

  private final TextView invitesButtonView;
  private final View createGuildButtonView;
  private final View manageMembersButtonView;
  private final View yourGuildButtonView;

  @Nullable
  private GuildPayload guild;

  @Nullable
  private GuildMemberPayload selfMember;

  private Disposable profileListener;
  @Nullable
  private Disposable guildMemberListener;

  public GuildView() {
    super(new Properties<>());

    this.yourGuildView = new YourGuildView(this::setContentView);
    var manageMembersView = new ManageMembersView(this::setContentView);
    this.invitesView = new InvitesView();

    this.addChild(new PanoramaView(new Properties<>(), TitleScreen.CUBE_MAP));

    this.addChild(
        this.sideBarView = new ParentView(new Properties<>().id("side-bar").backgroundBlur(50.0F)));

    this.manageMembersButtonView = new TextView(new Properties<>().focusable(true))
        .setText(ManageMembersView.TITLE)
        .setCentered(true);
    this.manageMembersButtonView.addListener(ActionEvent.class,
        event -> this.setContentView(manageMembersView));

    this.yourGuildButtonView = new TextView(new Properties<>().focusable(true))
        .setText(YourGuildView.TITLE)
        .setCentered(true);
    this.yourGuildButtonView.addListener(ActionEvent.class,
        event -> this.setContentView(this.yourGuildView));

    this.invitesButtonView = new TextView(new Properties<>().focusable(true))
        .setText(InvitesView.TITLE)
        .setCentered(true);
    this.invitesButtonView.addListener(ActionEvent.class,
        event -> this.setContentView(this.invitesView));
    this.sideBarView.addChild(this.invitesButtonView);

    this.createGuildButtonView = new TextView(new Properties<>().focusable(true))
        .setText(CreateGuildDialogView.TITLE)
        .setCentered(true);
    this.createGuildButtonView.addListener(ActionEvent.class,
        event -> this.setContentView(new CreateGuildDialogView((name, tag) -> {
          Rocket.getGameClientGateway()
              .ifPresentOrElse(connection -> connection.createGuild(name, tag)
                  .doOnSubscribe(__ -> RocketToast.info(this.minecraft, "Creating guild: " + name))
                  .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Guild created"))
                  .doOnError(error -> RocketToast.error(this.minecraft, error.getMessage()))
                  .publishOn(Schedulers.fromExecutor(this.minecraft))
                  .subscribe(), () -> RocketToast.info(this.minecraft, "Not connected to Rocket"));
        }, () -> this.setContentView(this.invitesView))));
    this.sideBarView.addChild(this.createGuildButtonView);

    this.addChild(this.contentView);

    this.setContentView(this.invitesView);
  }

  private void setContentView(View view) {
    this.contentView.replace(view);
  }

  private Component makeInvitesText(Set<GuildInvitePayload> invites) {
    return invites.isEmpty()
        ? InvitesView.TITLE
        : InvitesView.TITLE.copy().append(
            new TextComponent(" (" + invites.size() + ")").withStyle(ChatFormatting.LIGHT_PURPLE));
  }

  private void handleProfile(SocialProfilePayload profile, GameClientGateway gateway) {
    this.invitesButtonView.setText(this.makeInvitesText(profile.getGuildInvites()));

    var guild = profile.getGuild().orElse(null);
    var lastGuild = this.guild;
    this.guild = guild;

    if (guild != null
        && lastGuild != null
        && !this.guild.getOwner().equals(lastGuild.getOwner())
        && this.selfMember != null) {
      // Refresh ownership status
      this.handleGuildMember(this.selfMember);
    }

    if (Objects.equal(lastGuild, this.guild)) {
      return;
    }

    if (guild == null) {
      if (this.guildMemberListener != null) {
        this.guildMemberListener.dispose();
        this.guildMemberListener = null;
      }
      this.setContentView(this.invitesView);
      this.sideBarView.removeChild(this.manageMembersButtonView);
      this.sideBarView.removeChild(this.yourGuildButtonView);
      this.sideBarView.addChild(this.createGuildButtonView);
    } else {
      this.guildMemberListener = gateway.getGuildMember()
          .publishOn(Schedulers.fromExecutor(this.minecraft))
          .doOnNext(this::handleGuildMember)
          .subscribe();
      this.setContentView(this.yourGuildView);
      this.sideBarView.removeChild(this.createGuildButtonView);
      this.sideBarView.addChild(this.yourGuildButtonView);
    }
    this.layout();
  }

  private void handleGuildMember(GuildMemberPayload member) {
    this.selfMember = member;
    var permissions = member.getPermissions(this.guild);
    if (GuildPermission.KICK.hasPermission(permissions)
        || GuildPermission.MANAGE_RANKS.hasPermission(permissions)
        || GuildPermission.INVITE.hasPermission(permissions)) {
      if (!this.manageMembersButtonView.hasParent()) {
        this.sideBarView.addChild(this.manageMembersButtonView);
        this.layout();
      }
    } else if (this.manageMembersButtonView.hasParent()) {
      this.sideBarView.removeChild(this.manageMembersButtonView);
      this.setContentView(this.yourGuildView);
      this.layout();
    }
  }

  @Override
  protected void added() {
    super.added();
    this.profileListener = Rocket.getGameClientGatewayStream()
        .flatMap(api -> api.getSocialProfile()
            .publishOn(Schedulers.fromExecutor(this.minecraft))
            .doOnNext(profile -> this.handleProfile(profile, api)))
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
  }

  @Override
  protected void removed() {
    super.removed();
    this.selfMember = null;
    this.guild = null;
    this.profileListener.dispose();
    if (this.guildMemberListener != null) {
      this.guildMemberListener.dispose();
    }
  }
}
