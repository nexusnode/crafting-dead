package net.rocketpowered.connector.client.gui.guild;

import java.util.Set;
import org.jetbrains.annotations.Nullable;
import com.google.common.base.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.TextComponent;
import net.rocketpowered.common.Guild;
import net.rocketpowered.common.GuildInvite;
import net.rocketpowered.common.GuildMember;
import net.rocketpowered.common.GuildPermission;
import net.rocketpowered.common.SocialProfile;
import net.rocketpowered.connector.client.gui.RocketToast;
import net.rocketpowered.sdk.Rocket;
import net.rocketpowered.sdk.interf.GameClientInterface;
import reactor.core.Disposable;
import reactor.core.scheduler.Schedulers;
import sm0keysa1m0n.bliss.StyledText;
import sm0keysa1m0n.bliss.minecraft.AdapterUtil;
import sm0keysa1m0n.bliss.minecraft.view.PanoramaView;
import sm0keysa1m0n.bliss.view.ParentView;
import sm0keysa1m0n.bliss.view.TextView;
import sm0keysa1m0n.bliss.view.View;
import sm0keysa1m0n.bliss.view.event.ActionEvent;

public class GuildView extends ParentView {

  private final Minecraft minecraft = Minecraft.getInstance();

  private final ParentView contentView = new ParentView(new Properties().id("content"));

  private final ParentView sideBarView;

  private final YourGuildView yourGuildView;
  private final InvitesView invitesView;

  private final TextView invitesButtonView;
  private final View createGuildButtonView;
  private final View manageMembersButtonView;
  private final View yourGuildButtonView;

  @Nullable
  private Guild guild;

  @Nullable
  private GuildMember selfMember;

  private Disposable profileListener;
  @Nullable
  private Disposable guildMemberListener;

  public GuildView() {
    super(new Properties());

    this.yourGuildView = new YourGuildView(this::setContentView);
    var manageMembersView = new ManageMembersView(this::setContentView);
    this.invitesView = new InvitesView();

    this.addChild(new PanoramaView(new Properties(), TitleScreen.CUBE_MAP));

    this.addChild(
        this.sideBarView = new ParentView(new Properties().id("side-bar").styleClasses("blur")));

    this.manageMembersButtonView = new TextView(new Properties().focusable(true))
        .setText(ManageMembersView.TITLE);
    this.manageMembersButtonView.eventBus().subscribe(ActionEvent.class,
        event -> this.setContentView(manageMembersView));

    this.yourGuildButtonView = new TextView(new Properties().focusable(true))
        .setText(YourGuildView.TITLE);
    this.yourGuildButtonView.eventBus().subscribe(ActionEvent.class,
        event -> this.setContentView(this.yourGuildView));

    this.invitesButtonView = new TextView(new Properties().focusable(true))
        .setText(InvitesView.TITLE);
    this.invitesButtonView.eventBus().subscribe(ActionEvent.class,
        event -> this.setContentView(this.invitesView));
    this.sideBarView.addChild(this.invitesButtonView);

    this.createGuildButtonView = new TextView(new Properties().focusable(true))
        .setText(CreateGuildDialogView.TITLE);
    this.createGuildButtonView.eventBus().subscribe(ActionEvent.class,
        event -> this.setContentView(new CreateGuildDialogView((name, tag) -> {
          Rocket.gameClientInterface()
              .ifPresentOrElse(connection -> connection.createGuild(name, tag)
                  .doOnSubscribe(__ -> RocketToast.info(this.minecraft, "Creating guild: " + name))
                  .doOnSuccess(__ -> RocketToast.info(this.minecraft, "Guild created"))
                  .doOnError(error -> RocketToast.error(this.minecraft, error.getMessage()))
                  .publishOn(Schedulers.fromExecutor(this.minecraft))
                  .subscribe(), () -> RocketToast.info(this.minecraft, "Not connected to Rocket"));
        }, () -> this.setContentView(this.invitesView))));
    this.sideBarView.addChild(this.createGuildButtonView);

    this.addChild(this.contentView);

    this.setContentView(this.invitesView);
  }

  private void setContentView(View view) {
    this.contentView.replace(view);
  }

  private StyledText makeInvitesText(Set<GuildInvite> invites) {
    return invites.isEmpty()
        ? InvitesView.TITLE
        : AdapterUtil.createStyledText(new TextComponent(InvitesView.TITLE.text()).append(
            new TextComponent(" (" + invites.size() + ")").withStyle(ChatFormatting.LIGHT_PURPLE)));
  }

  private void handleProfile(SocialProfile profile, GameClientInterface gateway) {
    this.invitesButtonView.setText(this.makeInvitesText(profile.guildInvites()));

    var lastGuild = this.guild;
    this.guild = profile.guild();

    if (this.guild != null
        && lastGuild != null
        && !this.guild.owner().equals(lastGuild.owner())
        && this.selfMember != null) {
      // Refresh ownership status
      this.handleGuildMember(this.selfMember);
    }

    if (Objects.equal(lastGuild, this.guild)) {
      return;
    }

    if (guild == null) {
      if (this.guildMemberListener != null) {
        this.guildMemberListener.dispose();
        this.guildMemberListener = null;
      }
      this.setContentView(this.invitesView);
      this.sideBarView.removeChild(this.manageMembersButtonView);
      this.sideBarView.removeChild(this.yourGuildButtonView);
      this.sideBarView.addChild(this.createGuildButtonView);
    } else {
      this.guildMemberListener = gateway.getGuildMemberFeed()
          .publishOn(Schedulers.fromExecutor(this.minecraft))
          .doOnNext(this::handleGuildMember)
          .subscribe();
      this.setContentView(this.yourGuildView);
      this.sideBarView.removeChild(this.createGuildButtonView);
      this.sideBarView.addChild(this.yourGuildButtonView);
    }
    this.layout();
  }

  private void handleGuildMember(GuildMember member) {
    this.selfMember = member;
    var permissions = this.guild.getPermissions(member);
    if (GuildPermission.KICK.contains(permissions)
        || GuildPermission.MANAGE_RANKS.contains(permissions)
        || GuildPermission.INVITE.contains(permissions)) {
      if (!this.manageMembersButtonView.hasParent()) {
        this.sideBarView.addChild(this.manageMembersButtonView);
        this.layout();
      }
    } else if (this.manageMembersButtonView.hasParent()) {
      this.sideBarView.removeChild(this.manageMembersButtonView);
      this.setContentView(this.yourGuildView);
      this.layout();
    }
  }

  @Override
  public void added() {
    super.added();
    this.profileListener = Rocket.gameClientInterfaceFeed()
        .flatMap(api -> api.getSocialProfileFeed()
            .publishOn(Schedulers.fromExecutor(this.minecraft))
            .doOnNext(profile -> this.handleProfile(profile, api)))
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();
  }

  @Override
  public void removed() {
    super.removed();
    this.selfMember = null;
    this.guild = null;
    this.profileListener.dispose();
    if (this.guildMemberListener != null) {
      this.guildMemberListener.dispose();
    }
  }
}
