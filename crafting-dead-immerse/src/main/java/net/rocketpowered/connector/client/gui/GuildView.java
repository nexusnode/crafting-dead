package net.rocketpowered.connector.client.gui;

import java.util.UUID;
import com.craftingdead.immerse.client.gui.screen.menu.MainMenuView;
import com.craftingdead.immerse.client.gui.view.AvatarView;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexWrap;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Justify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.PositionType;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.TextComponent;

public class GuildView extends ParentView<GuildView, ViewScreen, YogaLayout> {

  public GuildView(ViewScreen layout) {
    super(layout, new YogaLayoutParent().setFlexDirection(FlexDirection.ROW)
        .setJustifyContent(Justify.CENTER).setAlignItems(Align.CENTER));


    this.addChild(MainMenuView.createBackgroundView());

    ParentView<?, YogaLayout, YogaLayout> membersView =
        new ParentView<>(
            new YogaLayout()
                .setOverflow(Overflow.HIDDEN)
                .setPositionType(PositionType.ABSOLUTE)
                .setWidthPercent(45)
                .setHeightPercent(75)
                .setRightPercent(15),
            new YogaLayoutParent())
                .configure(
                    view -> view.getBackgroundColorProperty().setBaseValue(new Color(0xA0363636)))
                .setBackgroundBlur(50)
                .addChild(new TextView<>(new YogaLayout().setTopPadding(5),
                    new TextComponent("Guild Members")).setCentered(true));

    ParentView<?, YogaLayout, YogaLayout> membersListView =
        new ParentView<>(
            new YogaLayout()
                .setPadding(5)
                .setFlexShrink(1)
                .setOverflow(Overflow.SCROLL),
            new YogaLayoutParent()
                .setFlexDirection(FlexDirection.ROW)
                .setFlexWrap(FlexWrap.WRAP)
                .setJustifyContent(Justify.SPACE_AROUND)
                .setAlignContent(Align.SPACE_AROUND));

    membersView.addChild(membersListView);

    this.addChild(membersView);

    membersListView.addChild(this.createMemberView());
    membersListView.addChild(this.createMemberView());
    membersListView.addChild(this.createMemberView());
    membersListView.addChild(this.createMemberView());
    membersListView.addChild(this.createMemberView());
    membersListView.addChild(this.createMemberView());

  }

  private View<?, YogaLayout> createMemberView() {
    return new ParentView<>(
        new YogaLayout().setWidth(100).setHeight(25).setBorderWidth(1).setPadding(3),
        new YogaLayoutParent().setFlexDirection(FlexDirection.ROW).setAlignItems(Align.CENTER))
            .configure(
                view -> view.getBackgroundColorProperty().setBaseValue(new Color(0xA0333333)))
            .addChild(new AvatarView<>(new YogaLayout()
                .setWidth(20)
                .setBorderWidth(2F)
                .setRightMargin(3)
                .setAspectRatio(1),
                new GameProfile(UUID.fromString("dbb22877-8006-4516-8b64-1fec55ccd30d"), null))
                    .configure(
                        view -> view.getBorderColorProperty().setBaseValue(new Color(0xFF90ba3c))))
            .configure(view -> view.getBorderColorProperty().setBaseValue(Color.GRAY))
            .addChild(new TextView<>(
                new YogaLayout(),
                new TextComponent("Sm0keySa1m0n")));
  }
}
