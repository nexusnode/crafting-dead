package net.rocketpowered.connector.client.gui.guild;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.immerse.client.gui.GuiUtil;
import com.craftingdead.immerse.client.gui.screen.Theme;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.rocketpowered.common.Guild;
import net.rocketpowered.common.GuildMember;
import net.rocketpowered.common.GuildMemberUpdateEvent;
import net.rocketpowered.common.GuildPermission;
import net.rocketpowered.common.RocketException;
import net.rocketpowered.connector.client.gui.RocketToast;
import net.rocketpowered.sdk.Rocket;
import net.rocketpowered.sdk.interf.GameClientInterface;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import sm0keysa1m0n.bliss.StyledText;
import sm0keysa1m0n.bliss.minecraft.AdapterUtil;
import sm0keysa1m0n.bliss.view.ParentView;
import sm0keysa1m0n.bliss.view.TextView;
import sm0keysa1m0n.bliss.view.View;
import sm0keysa1m0n.bliss.view.event.RemovedEvent;

public class YourGuildView extends ParentView {

  public static final StyledText TITLE = GuiUtil.translatable("view.guild.your_guild");

  private final Minecraft minecraft = Minecraft.getInstance();

  private final ParentView informationView;

  private final ParentView membersView;

  private final Map<ObjectId, MemberView> memberViews = new HashMap<>();

  private final Consumer<View> contentConsumer;

  private final View transferButton;
  private final View deleteButton;
  private final View renameButton;
  private final View leaveButton;

  @Nullable
  private Guild guild;

  private Disposable listener;

  @Nullable
  private GuildMember selfMember;

  public YourGuildView(Consumer<View> contentConsumer) {
    super(new Properties().styleClasses("page", "blur"));

    this.contentConsumer = contentConsumer;

    this.addChild(new TextView(new Properties().id("title")).setText(TITLE));

    var guildView = new ParentView(new Properties().id("guild"));
    this.addChild(guildView);

    guildView.addChild(
        this.informationView = new ParentView(new Properties().id("information")));

    guildView.addChild(
        this.membersView = new ParentView(new Properties().id("members")));

    this.transferButton = Theme.createRedButton(new TextComponent("Transfer"),
        () -> this.contentConsumer.accept(new TextDialogView(
            new TranslatableComponent("view.guild.your_guild.transfer.message"),
            I18n.get("view.guild.text_dialog.username"),
            result -> {
              Rocket.gameClientInterface().ifPresent(gateway -> gateway.getUserId(result)
                  .flatMap(gateway::transferGuildOwnership)
                  .doOnSubscribe(__ -> RocketToast.info(this.minecraft,
                      "Transferring guild: " + this.guild.name()))
                  .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Guild transferred"))
                  .doOnError(RocketException.class,
                      error -> RocketToast.error(this.minecraft, error.getMessage()))
                  .subscribe());
              this.contentConsumer.accept(this);
            },
            () -> this.contentConsumer.accept(this))));

    this.deleteButton = Theme.createRedButton(new TextComponent("Delete"),
        () -> this.contentConsumer.accept(new ConfirmDialogView(
            new TranslatableComponent("view.guild.your_guild.delete.message",
                this.guild.name()),
            () -> {
              Rocket.gameClientInterface().ifPresent(gateway -> gateway.deleteGuild()
                  .doOnSubscribe(
                      __ -> RocketToast.info(this.minecraft,
                          "Deleting guild: " + guild.name()))
                  .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Guild deleted"))
                  .doOnError(RocketException.class,
                      error -> RocketToast.error(this.minecraft, error.getMessage()))
                  .subscribe());
              this.contentConsumer.accept(this);
            },
            () -> this.contentConsumer.accept(this))));

    this.renameButton = Theme.createBlueButton(new TextComponent("Rename"),
        () -> this.contentConsumer.accept(new TextDialogView(
            new TranslatableComponent("view.guild.your_guild.rename.message"),
            I18n.get("view.guild.text_dialog.name"),
            result -> {
              Rocket.gameClientInterface().ifPresent(gateway -> gateway.renameGuild(result)
                  .doOnSubscribe(__ -> RocketToast.info(this.minecraft,
                      "Renaming guild: " + this.guild.name()))
                  .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Guild renamed"))
                  .doOnError(RocketException.class,
                      error -> RocketToast.error(this.minecraft, error.getMessage()))
                  .subscribe());
              this.contentConsumer.accept(this);
            },
            () -> this.contentConsumer.accept(this))));

    this.leaveButton = Theme.createRedButton(new TextComponent("Leave"),
        () -> this.contentConsumer.accept(new ConfirmDialogView(
            new TranslatableComponent("view.guild.your_guild.leave.message",
                this.guild.name()),
            () -> {
              Rocket.gameClientInterface().ifPresent(gateway -> gateway.leaveGuild()
                  .doOnSubscribe(
                      __ -> RocketToast.info(this.minecraft,
                          "Leaving guild: " + this.guild.name()))
                  .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Left guild"))
                  .doOnError(RocketException.class,
                      error -> RocketToast.error(this.minecraft, error.getMessage()))
                  .subscribe());
              this.contentConsumer.accept(this);
            },
            () -> this.contentConsumer.accept(this))));
  }

  private void updateGuild(GameClientInterface gateway, Guild guild) {
    if (guild == null) {
      return;
    }

    if (!guild.equals(this.guild)) {
      this.membersView.clearChildren();
      gateway.getGuildMembers(guild.id())
          .publishOn(Schedulers.fromExecutor(this.minecraft))
          .doOnNext(member -> this.updateMember(gateway, member))
          .doOnTerminate(this::layout)
          .subscribeOn(Schedulers.boundedElastic())
          .subscribe();
    } else {
      this.memberViews.values().forEach(view -> view.updateGuild(guild));
    }

    this.guild = guild;

    if (this.selfMember != null) {
      this.updateInformation(gateway);
    }
  }

  private void updateInformation(GameClientInterface gateway) {
    this.informationView.clearChildren();

    this.informationView.addChild(new TextView(new Properties())
        .setText(AdapterUtil.createStyledText(
            TextComponent.EMPTY.copy()
                .append(new TextComponent("Name: ")
                    .withStyle(ChatFormatting.GRAY))
                .append(this.guild.name()))));
    this.informationView.addChild(new TextView(new Properties())
        .setText(AdapterUtil.createStyledText(
            TextComponent.EMPTY.copy()
                .append(new TextComponent("Tag: ")
                    .withStyle(ChatFormatting.GRAY))
                .append(this.guild.tag()))));
    this.informationView.addChild(new TextView(new Properties())
        .setText(AdapterUtil.createStyledText(
            TextComponent.EMPTY.copy()
                .append(new TextComponent("Owner: ")
                    .withStyle(ChatFormatting.GRAY))
                .append(this.guild.owner().minecraftProfile().name()))));

    var controlsView = new ParentView(new Properties().id("controls"));
    this.informationView.addChild(controlsView);

    var permissions = this.guild.getPermissions(this.selfMember);

    if (GuildPermission.RENAME.contains(permissions)) {
      controlsView.forceAddChild(this.renameButton);
    }

    if (this.guild.isOwner(gateway.user())) {
      controlsView.forceAddChild(this.transferButton);
      controlsView.forceAddChild(this.deleteButton);
    } else {
      controlsView.forceAddChild(this.leaveButton);
    }

    this.informationView.layout();
  }

  private void updateMember(GameClientInterface gateway, GuildMember member) {
    if (member.user().equals(gateway.user())) {
      this.selfMember = member;
      this.updateInformation(gateway);
    }

    var view = this.memberViews.computeIfAbsent(member.user().id(),
        __ -> new MemberView(this.guild, member));
    if (view.hasParent()) {
      view.updateMember(member);
    } else {
      view.eventBus().subscribe(RemovedEvent.class,
          __ -> this.memberViews.remove(member.user().id(), view));
      this.membersView.addChild(view);
    }
  }

  @Override
  public void added() {
    super.added();
    this.listener = Rocket.gameClientInterfaceFeed()
        .flatMap(api -> Mono.when(
            api.getSocialProfileFeed()
                .publishOn(Schedulers.fromExecutor(this.minecraft))
                .doOnNext(profile -> this.updateGuild(api, profile.guild())),
            api.getGuildEventFeed()
                .ofType(GuildMemberUpdateEvent.class)
                .filter(event -> this.guild != null
                    && event.guildId().equals(this.guild.id()))
                .publishOn(Schedulers.fromExecutor(this.minecraft))
                .doOnNext(event -> this.updateMember(api, event.guildMember()))))
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
  }

  @Override
  public void removed() {
    super.removed();
    this.listener.dispose();
  }
}
