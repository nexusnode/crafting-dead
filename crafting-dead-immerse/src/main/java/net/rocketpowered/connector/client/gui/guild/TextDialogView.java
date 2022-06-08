package net.rocketpowered.connector.client.gui.guild;

import java.util.function.Consumer;
import com.craftingdead.immerse.client.gui.screen.Theme;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import sm0keysa1m0n.bliss.view.ParentView;
import sm0keysa1m0n.bliss.view.TextFieldView;
import sm0keysa1m0n.bliss.view.TextView;

public class TextDialogView extends ParentView {

  public TextDialogView(Component message, String placeholder, Consumer<String> resultConsumer,
      Runnable cancelListener) {
    super(new Properties().styleClasses("dialog", "blur"));

    this.addChild(new TextView(new Properties()).setText(message));

    var usernameFieldView = new TextFieldView(new Properties());
    usernameFieldView.setPlaceholder(placeholder);
    var sendButtonView = Theme.createGreenButton(CommonComponents.GUI_PROCEED,
        () -> resultConsumer.accept(usernameFieldView.getText()));
    sendButtonView.setEnabled(false);
    usernameFieldView.setResponder(value -> sendButtonView.setEnabled(!value.isBlank()));
    this.addChild(usernameFieldView);

    var controlsView = new ParentView(new Properties().id("controls"));
    this.addChild(controlsView);

    controlsView.addChild(sendButtonView);
    controlsView.addChild(
        Theme.createRedButton(CommonComponents.GUI_CANCEL, cancelListener));
  }
}
