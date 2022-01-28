package net.rocketpowered.connector.client.gui.guild;

import java.util.function.Consumer;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextFieldView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Justify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.PositionType;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SendInviteView extends ParentView<SendInviteView, YogaLayout, YogaLayout> {

  private static final ITextComponent TITLE =
      new TranslationTextComponent("view.guild.send_invite");

  public SendInviteView(YogaLayout layout, Consumer<String> resultConsumer) {
    super(layout, new YogaLayoutParent().setFlexDirection(FlexDirection.ROW)
        .setJustifyContent(Justify.CENTER).setAlignItems(Align.CENTER));

    TextFieldView<YogaLayout> textFieldView =
        new TextFieldView<>(new YogaLayout().setWidthPercent(80).setHeight(20).setPadding(4));

    textFieldView.getBackgroundColorProperty().setBaseValue(new Color(0x40121212));

    View<?, YogaLayout> sendButtonView = Theme.createGreenButton(new StringTextComponent("Send"),
        () -> resultConsumer.accept(textFieldView.getValue()))
        .configure(view -> view.getLayout().setWidth(75).setHeight(20).setMargin(7));

    textFieldView.setResponder(value -> sendButtonView.setEnabled(!value.isEmpty()));

    this.addChild(new ParentView<>(
        new YogaLayout()
            .setPositionType(PositionType.ABSOLUTE)
            .setWidth(250)
            .setHeight(90)
            .setPadding(5),
        new YogaLayoutParent().setAlignItems(Align.CENTER))
            .configure(
                view -> view.getBackgroundColorProperty().setBaseValue(new Color(0xA0363636)))
            .configure(view -> view.getBorderRadiusProperty().setBaseValue(7.5F))
            .setBackgroundBlur(50)
            .addChild(new TextView<>(new YogaLayout().setPadding(7))
                .setText(TITLE)
                .setCentered(true))
            .addChild(textFieldView)
            .addChild(new ParentView<>(new YogaLayout().setFlex(1),
                new YogaLayoutParent().setFlexDirection(FlexDirection.ROW)
                    .setJustifyContent(Justify.CENTER))
                        .addChild(sendButtonView)
                        .addChild(Theme.createRedButton(new StringTextComponent("Cancel"),
                            () -> resultConsumer.accept(null))
                            .configure(view -> view.getLayout().setWidth(75).setHeight(20)
                                .setMargin(7)))));
  }
}
