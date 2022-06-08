package net.rocketpowered.connector.client.gui.guild;

import com.craftingdead.immerse.client.gui.screen.Theme;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import sm0keysa1m0n.bliss.view.ParentView;
import sm0keysa1m0n.bliss.view.TextView;

public class ConfirmDialogView extends ParentView {

  public ConfirmDialogView(Component message, Runnable yesListener, Runnable noListener) {
    super(new Properties().styleClasses("dialog", "blur"));

    this.addChild(new TextView(new Properties()).setText(message));

    var controlsView = new ParentView(new Properties().id("controls"));
    this.addChild(controlsView);

    controlsView.addChild(Theme.createGreenButton(CommonComponents.GUI_YES, yesListener));
    controlsView.addChild(Theme.createRedButton(CommonComponents.GUI_NO, noListener));
  }
}
