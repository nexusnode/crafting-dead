package net.rocketpowered.connector.client.gui.guild;

import java.util.function.BiConsumer;
import com.craftingdead.immerse.client.gui.screen.Theme;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.rocketpowered.common.GuildConstants;
import sm0keysa1m0n.bliss.view.ParentView;
import sm0keysa1m0n.bliss.view.TextFieldView;
import sm0keysa1m0n.bliss.view.TextView;

public class CreateGuildDialogView extends ParentView {

  public static final Component TITLE = new TranslatableComponent("view.guild.create_guild");

  public CreateGuildDialogView(BiConsumer<String, String> resultConsumer, Runnable cancelListener) {
    super(new Properties().styleClasses("dialog", "blur"));

    this.addChild(new TextView(new Properties()).setText(TITLE));

    var nameFieldView = new TextFieldView(new Properties());
    nameFieldView.setPlaceholder("Name");
    nameFieldView.setMaxLength(GuildConstants.GUILD_NAME_MAX_LENGTH);
    this.addChild(nameFieldView);

    var tagFieldView = new TextFieldView(new Properties());
    tagFieldView.setPlaceholder("Tag");
    tagFieldView.setMaxLength(GuildConstants.GUILD_TAG_MAX_LENGTH);
    this.addChild(tagFieldView);

    var controlsView = new ParentView(new Properties().id("controls"));
    this.addChild(controlsView);

    var createButtonView = Theme.createGreenButton(new TextComponent("Create"),
        () -> resultConsumer.accept(nameFieldView.getText(), tagFieldView.getText()));
    createButtonView.setEnabled(false);
    nameFieldView.setResponder(
        value -> createButtonView.setEnabled(value.length() >= GuildConstants.GUILD_NAME_MIN_LENGTH
            && tagFieldView.getText().length() >= GuildConstants.GUILD_TAG_MIN_LENGTH));
    tagFieldView.setResponder(
        value -> createButtonView.setEnabled(value.length() >= GuildConstants.GUILD_TAG_MIN_LENGTH
            && nameFieldView.getText().length() >= GuildConstants.GUILD_NAME_MIN_LENGTH));
    controlsView.addChild(createButtonView);

    controlsView.addChild(Theme.createRedButton(CommonComponents.GUI_CANCEL, cancelListener));
  }
}
