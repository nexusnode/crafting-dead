package net.rocketpowered.connector.client.gui.guild;

import java.util.function.Consumer;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextFieldView;
import com.craftingdead.immerse.client.gui.view.TextView;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class UsernameDialogView extends ParentView {

  public UsernameDialogView(Component message, Consumer<String> resultConsumer,
      Runnable cancelListener) {
    super(new Properties<>().styleClasses("dialog").backgroundBlur(50));

    this.addChild(new TextView(new Properties<>())
        .setText(message)
        .setCentered(true));

    var usernameFieldView = new TextFieldView(new Properties<>());
    usernameFieldView.setPlaceholder(new TextComponent("Username"));
    var sendButtonView = Theme.createGreenButton(CommonComponents.GUI_PROCEED,
        () -> resultConsumer.accept(usernameFieldView.getValue()));
    sendButtonView.setEnabled(false);
    usernameFieldView.setResponder(value -> sendButtonView.setEnabled(!value.isBlank()));
    this.addChild(usernameFieldView);

    var controlsView = new ParentView(new Properties<>().id("controls"));
    this.addChild(controlsView);

    controlsView.addChild(sendButtonView);
    controlsView.addChild(
        Theme.createRedButton(CommonComponents.GUI_CANCEL, cancelListener));
  }
}
