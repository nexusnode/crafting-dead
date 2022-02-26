package net.rocketpowered.connector.client.gui.guild;

import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ConfirmDialogView extends ParentView {

  public ConfirmDialogView(Component message, Runnable yesListener, Runnable noListener) {
    super(new Properties<>().styleClasses("dialog").backgroundBlur(50));

    this.addChild(new TextView(new Properties<>())
        .setText(message)
        .setCentered(true));

    var controlsView = new ParentView(new Properties<>().id("controls"));
    this.addChild(controlsView);

    controlsView.addChild(Theme.createGreenButton(CommonComponents.GUI_YES, yesListener));
    controlsView.addChild(Theme.createRedButton(CommonComponents.GUI_NO, noListener));
  }
}
