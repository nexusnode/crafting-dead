package net.rocketpowered.connector.client.gui.guild;

import java.util.function.Consumer;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextFieldView;
import com.craftingdead.immerse.client.gui.view.TextView;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class SendInviteView extends ParentView {

  private static final Component TITLE = new TranslatableComponent("view.guild.send_invite");

  public SendInviteView(Consumer<String> resultConsumer) {
    super(new Properties<>());

    var dialogView = new ParentView(new Properties<>().id("dialog").backgroundBlur(50));
    this.addChild(dialogView);

    dialogView.addChild(new TextView(new Properties<>().id("title"))
        .setText(TITLE)
        .setCentered(true));

    var usernameFieldView = new TextFieldView(new Properties<>().id("username-field"));
    dialogView.addChild(usernameFieldView);

    var controlsView = new ParentView(new Properties<>().id("controls"));
    dialogView.addChild(controlsView);

    var sendButtonView = Theme.createGreenButton(new TextComponent("Send"),
        () -> resultConsumer.accept(usernameFieldView.getValue()), "send-button");
    usernameFieldView.setResponder(value -> sendButtonView.setEnabled(!value.isEmpty()));
    controlsView.addChild(sendButtonView);

    controlsView.addChild(Theme.createRedButton(new TextComponent("Cancel"),
        () -> resultConsumer.accept(null), "cancel-button"));
  }
}
