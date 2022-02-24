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

import javax.annotation.Nullable;
import com.craftingdead.immerse.client.gui.view.PanoramaView;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.google.common.base.Objects;
import net.minecraft.client.gui.screens.TitleScreen;
import net.rocketpowered.connector.RocketConnector;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.GuildPermission;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.payload.GuildMemberPayload;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.payload.GuildPayload;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.payload.SocialProfilePayload;
import net.rocketpowered.connector.internal.shaded.reactor.core.Disposable;
import net.rocketpowered.connector.internal.shaded.reactor.core.publisher.Mono;
import net.rocketpowered.connector.internal.shaded.reactor.core.scheduler.Schedulers;

public class GuildView extends ParentView {

  private final ParentView contentView = new ParentView(new Properties<>().id("content"));

  private final ParentView sideBarView;

  private final YourGuildView yourGuildView;
  private final InvitesView invitesView;

  private final View manageMembersButtonView;
  private final View yourGuildButtonView;

  @Nullable
  private GuildPayload guild;

  private Disposable listener;

  public GuildView() {
    super(new Properties<>());

    this.yourGuildView = new YourGuildView();
    var manageMembersView = new ManageMembersView(this::setContentView);
    this.invitesView = new InvitesView();

    this.addChild(new PanoramaView(new Properties<>(), TitleScreen.CUBE_MAP));

    this.addChild(
        this.sideBarView = new ParentView(new Properties<>().id("side-bar").backgroundBlur(50.0F)));

    this.manageMembersButtonView = new TextView(new Properties<>())
        .setText(ManageMembersView.TITLE)
        .setCentered(true);
    this.manageMembersButtonView.addListener(ActionEvent.class,
        event -> this.setContentView(manageMembersView));

    this.yourGuildButtonView = new TextView(new Properties<>())
        .setText(YourGuildView.TITLE)
        .setCentered(true);
    this.yourGuildButtonView.addListener(ActionEvent.class,
        event -> this.setContentView(yourGuildView));

    var invitesButtonView = new TextView(new Properties<>())
        .setText(InvitesView.TITLE)
        .setCentered(true);
    invitesButtonView.addListener(ActionEvent.class,
        event -> this.setContentView(this.invitesView));
    this.sideBarView.addChild(invitesButtonView);

    this.addChild(this.contentView);

    this.setContentView(this.invitesView);
  }

  private void setContentView(View view) {
    this.contentView.replace(view);
  }

  private void handleProfile(SocialProfilePayload profile) {
    var guild = profile.getGuild().orElse(null);
    if (Objects.equal(guild, this.guild)) {
      return;
    }

    this.guild = guild;
    if (guild == null) {
      this.sideBarView.removeChild(this.yourGuildButtonView);
      this.setContentView(this.invitesView);
    } else {
      this.sideBarView.addChild(this.yourGuildButtonView);
      this.setContentView(this.yourGuildView);
    }
    this.layout();
  }

  private void handleGuildMember(GuildMemberPayload member) {
    if (this.guild == null) {
      return;
    }
    var permissions = member.getPermissions(this.guild);
    if (GuildPermission.KICK.hasPermission(permissions)
        || GuildPermission.MANAGE_RANKS.hasPermission(permissions)
        || GuildPermission.INVITE.hasPermission(permissions)) {
      if (!this.manageMembersButtonView.isAdded()) {
        this.sideBarView.addChild(this.manageMembersButtonView);
        this.layout();
      }
    } else if (this.manageMembersButtonView.isAdded()) {
      this.sideBarView.removeChild(this.manageMembersButtonView);
      this.layout();
    }
  }

  @Override
  protected void added() {
    super.added();
    this.listener = RocketConnector.getInstance()
        .getGameClientApi()
        .flatMap(api -> Mono.when(
            api.getSocialProfile()
                .publishOn(Schedulers.fromExecutor(this.minecraft))
                .doOnNext(this::handleProfile),
            api.getGuildMember()
                .publishOn(Schedulers.fromExecutor(this.minecraft))
                .doOnNext(this::handleGuildMember)))
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
  }

  @Override
  protected void removed() {
    super.removed();
    this.listener.dispose();
  }
}
