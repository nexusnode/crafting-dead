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

import java.util.concurrent.atomic.AtomicReference;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.PanoramaView;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.States;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.Transition;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Justify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.PositionType;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.google.common.base.Objects;
import net.minecraft.util.ResourceLocation;
import net.rocketpowered.connector.RocketConnector;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.GuildPermission;
import net.rocketpowered.connector.internal.shaded.net.rocketpowered.api.payload.GuildPayload;
import net.rocketpowered.connector.internal.shaded.reactor.core.Disposable;
import net.rocketpowered.connector.internal.shaded.reactor.core.publisher.Mono;
import net.rocketpowered.connector.internal.shaded.reactor.core.scheduler.Schedulers;

public class GuildView extends ParentView<GuildView, Layout, YogaLayout> {

  private final ParentView<?, YogaLayout, YogaLayout> contentContainer =
      new ParentView<>(new YogaLayout().setFlex(1), new YogaLayoutParent());

  private final ParentView<?, YogaLayout, YogaLayout> buttonListView;

  private final View<?, YogaLayout> manageMembersButtonView;

  private Disposable listener;

  public GuildView(Layout layout) {
    super(layout, new YogaLayoutParent().setFlexDirection(FlexDirection.ROW));

    YourGuildView yourGuildView = new YourGuildView(new YogaLayout().setFlex(1));
    ManageMembersView manageMembersView =
        new ManageMembersView(new YogaLayout().setFlex(1), this::setContentView);
    InvitesView invitesView = new InvitesView(new YogaLayout().setFlex(1));

    this.addChild(new PanoramaView<>(new YogaLayout().setPositionType(PositionType.ABSOLUTE)
        .setWidthPercent(100)
        .setHeightPercent(100), new ResourceLocation("textures/gui/title/background/panorama")));

    this.buttonListView = new ParentView<>(new YogaLayout().setWidth(100),
        new YogaLayoutParent().setAlignItems(Align.CENTER).setJustifyContent(Justify.CENTER));
    this.buttonListView.getBackgroundColorProperty().setBaseValue(new Color(0xA0363636));
    this.buttonListView.setBackgroundBlur(50.0F);

    this.manageMembersButtonView = new TextView<>(new YogaLayout().setPadding(5))
        .setText(ManageMembersView.TITLE)
        .setCentered(true)
        .addListener(ActionEvent.class,
            (view, event) -> this.setContentView(manageMembersView))
        .configure(view -> view.getColorProperty()
            .setTransition(Transition.linear(250L))
            .defineState(Color.YELLOW, States.HOVERED));

    this.addChild(this.buttonListView
        .addChild(new TextView<>(new YogaLayout().setPadding(5))
            .setText(YourGuildView.TITLE)
            .setCentered(true)
            .addListener(ActionEvent.class, (view, event) -> this.setContentView(yourGuildView))
            .configure(view -> view.getColorProperty()
                .setTransition(Transition.linear(250L))
                .defineState(Color.YELLOW, States.HOVERED)))
        .addChild(new TextView<>(new YogaLayout().setPadding(5))
            .setText(InvitesView.TITLE)
            .setCentered(true)
            .addListener(ActionEvent.class, (view, event) -> this.setContentView(invitesView))
            .configure(view -> view.getColorProperty()
                .setTransition(Transition.linear(250L))
                .defineState(Color.YELLOW, States.HOVERED))));

    this.addChild(this.contentContainer);


    this.setContentView(yourGuildView);
  }


  private void setContentView(View<?, YogaLayout> view) {
    // Already added
    if (view.getParent() == this.contentContainer) {
      return;
    }
    this.contentContainer
        .queueAllForRemoval()
        .addChild(view)
        .layout();
  }

  @Override
  protected void added() {
    super.added();
    AtomicReference<GuildPayload> currentGuild = new AtomicReference<>();
    this.listener = RocketConnector.getInstance()
        .getGameClientApi()
        .flatMap(api -> Mono.when(
            api.getSocialProfile()
                .flatMap(profile -> {
                  GuildPayload guild = profile.getGuild().orElse(null);
                  if (!Objects.equal(guild, currentGuild.get())) {
                    currentGuild.set(guild);
                    return Mono.justOrEmpty(guild);
                  }
                  return Mono.empty();
                }),
            api.getGuildMember()
                .publishOn(Schedulers.fromExecutor(this.minecraft))
                .doOnNext(member -> {
                  GuildPayload guild = currentGuild.get();
                  if (guild == null) {
                    return;
                  }
                  long permissions = member.getPermissions(guild);
                  if (GuildPermission.KICK.hasPermission(permissions)
                      || GuildPermission.MANAGE_RANKS.hasPermission(permissions)
                      || GuildPermission.INVITE.hasPermission(permissions)) {
                    if (!this.manageMembersButtonView.isAdded()) {
                      this.buttonListView.addChild(this.manageMembersButtonView);
                      this.layout();
                    }
                  } else if (this.manageMembersButtonView.isAdded()) {
                    this.buttonListView.removeChild(this.manageMembersButtonView);
                    this.layout();
                  }
                })))
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
  }

  @Override
  protected void removed() {
    super.removed();
    this.listener.dispose();
  }
}
