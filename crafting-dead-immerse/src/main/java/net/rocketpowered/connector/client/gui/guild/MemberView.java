package net.rocketpowered.connector.client.gui.guild;

import com.craftingdead.immerse.client.gui.screen.Theme;
import com.mojang.authlib.GameProfile;
import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.FontStyle;
import net.minecraft.client.Minecraft;
import net.rocketpowered.common.Guild;
import net.rocketpowered.common.GuildMember;
import net.rocketpowered.common.GuildMemberLeaveEvent;
import net.rocketpowered.common.GuildRank;
import net.rocketpowered.common.UserPresence;
import net.rocketpowered.sdk.Rocket;
import net.rocketpowered.sdk.interf.GameClientInterface;
import reactor.core.Disposable;
import reactor.core.scheduler.Schedulers;
import sm0keysa1m0n.bliss.Color;
import sm0keysa1m0n.bliss.StyledText;
import sm0keysa1m0n.bliss.minecraft.view.AvatarView;
import sm0keysa1m0n.bliss.view.ParentView;
import sm0keysa1m0n.bliss.view.TextView;

public class MemberView extends ParentView {

  private Guild guild;

  private GuildMember member;

  private Disposable removeListener;
  private Disposable presenceListener;

  private final AvatarView avatarView;
  private final TextView nameView;
  private final TextView rankView;

  public MemberView(Guild guild, GuildMember member) {
    super(new Properties().styleClasses("item").unscaleBorder(false).focusable(true));

    this.guild = guild;

    this.avatarView = new AvatarView(new Properties(),
        new GameProfile(member.user().minecraftProfile().id(), null));
    this.addChild(this.avatarView);

    var textView = new ParentView(new Properties().id("text"));
    this.addChild(textView);

    this.nameView = new TextView(new Properties())
        .setWrap(false)
        .setText(member.user().minecraftProfile().name());
    textView.addChild(this.nameView);

    this.rankView = new TextView(new Properties()).setWrap(false);
    textView.addChild(this.rankView);

    this.updateMember(member);
  }

  @Override
  public void styleRefreshed(FontMgr fontManager) {
    this.avatarView.getStyle().defineBorderColorState(Theme.OFFLINE);
    this.nameView.getStyle().color.defineState(Theme.OFFLINE);
  }

  public GuildMember getMember() {
    return this.member;
  }

  public void updateGuild(Guild guild) {
    this.guild = guild;
    this.updateMember(this.member);
  }

  public void updateMember(GuildMember member) {
    this.member = member;

    var rank = member.rank();
    var color = Color.WHITE;
    var owner = member.user().equals(this.guild.owner());
    if (owner) {
      color = Color.GOLD;
    } else if (rank == GuildRank.DIGNITARY) {
      color = Color.DARK_PURPLE;
    } else if (rank == GuildRank.ENVOY) {
      color = Color.AQUA;
    }

    this.rankView.setText(new StyledText(
        owner ? "Owner" : rank.getDisplayName().orElse(""),
        FontStyle.ITALIC, null));
    this.rankView.getStyle().color.defineState(color);

    this.getStyle().borderLeftColor.defineState(color);
  }

  private void updatePresence(UserPresence presence) {
    var color = presence.online() ? Theme.ONLINE : Theme.OFFLINE;
    this.avatarView.getStyle().defineBorderColorState(color);
    this.nameView.getStyle().color.defineState(color);
  }

  @Override
  public void added() {
    super.added();
    this.removeListener = Rocket.gameClientInterfaceFeed()
        .flatMap(GameClientInterface::getGuildEventFeed)
        .ofType(GuildMemberLeaveEvent.class)
        .filter(event -> event.user().equals(this.member.user()))
        .next()
        .subscribeOn(Schedulers.boundedElastic())
        .publishOn(Schedulers.fromExecutor(Minecraft.getInstance()))
        .subscribe(__ -> {
          if (this.hasParent()) {
            var parent = this.getParent();
            parent.removeChild(this);
            parent.layout();
          }
        });

    this.presenceListener = Rocket.gameClientInterfaceFeed()
        .flatMap(api -> api.getUserPresenceFeed(this.member.user().id()))
        .subscribeOn(Schedulers.boundedElastic())
        .publishOn(Schedulers.fromExecutor(Minecraft.getInstance()))
        .subscribe(this::updatePresence);
  }

  @Override
  public void removed() {
    super.removed();
    this.removeListener.dispose();
    this.presenceListener.dispose();
  }
}
