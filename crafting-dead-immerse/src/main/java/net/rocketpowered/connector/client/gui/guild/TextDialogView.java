/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

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
