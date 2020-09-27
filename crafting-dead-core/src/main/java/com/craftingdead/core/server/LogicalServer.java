package com.craftingdead.core.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.capability.hydration.DefaultHydration;
import com.craftingdead.core.capability.hydration.PresetHydration;
import com.craftingdead.core.capability.living.DefaultLiving;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.living.Player;
import com.craftingdead.core.game.GameManager;
import com.craftingdead.core.game.GameType;
import com.craftingdead.core.game.GameTypes;
import com.craftingdead.core.game.IGameServer;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.login.SetupGameMessage;
import com.craftingdead.core.network.message.main.SelectTeamMessage;
import com.craftingdead.core.potion.ModEffects;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import net.minecraft.network.play.server.SHeldItemChangePacket;
import net.minecraft.network.play.server.SJoinGamePacket;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.network.play.server.SPlayerAbilitiesPacket;
import net.minecraft.network.play.server.SPlayerListItemPacket;
import net.minecraft.network.play.server.SRespawnPacket;
import net.minecraft.network.play.server.SServerDifficultyPacket;
import net.minecraft.network.play.server.SSetExperiencePacket;
import net.minecraft.network.play.server.SSpawnPositionPacket;
import net.minecraft.network.play.server.STagsListPacket;
import net.minecraft.network.play.server.SUpdateRecipesPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.DemoplayerEntityteractionManager;
import net.minecraft.server.management.playerEntityteractionManager;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.registries.ForgeRegistry;

public class LogicalServer extends WorldSavedData {

  private final MinecraftServer minecraftServer;
  private IGameServer<?, ?> gameServer;
  private Set<ServerPlayerEntity> waitingForTeam = new ReferenceOpenHashSet<>();

  public LogicalServer(MinecraftServer minecraftServer) {
    super(CraftingDead.ID);
    this.minecraftServer = minecraftServer;
  }

  public void init() {
    this.minecraftServer.getWorld(DimensionType.OVERWORLD).getSavedData().getOrCreate(() -> this,
        CraftingDead.ID);
  }

  public boolean initializeConnectionToPlayer(NetworkManager networkManager,
      ServerPlayerEntity playerEntity) {
    if (!this.gameServer.getDefaultTeam().isPresent()) {
      NetworkChannel.PLAY.getSimpleChannel().sendTo(new SelectTeamMessage(), networkManager,
          NetworkDirection.PLAY_TO_CLIENT);
      this.waitingForTeam.add(playerEntity);
      return true;
    }
    this.gameServer.setTeam(this.gameServer.get, team);
    return false;
  }

  public void loadGame(GameType gameType, CompoundNBT nbt, boolean newGame) {
    this.currentGame = gameType.createGameServer(this);
    this.currentGame.deserializeNBT(nbt);
    this.minecraftServer.getPlayerList().getPlayers().forEach(playerEntity -> {
      this.currentGame.getTeam(playerEntity);
      this.minecraftServer.getPlayerList().recreatePlayerEntity(playerEntity, this.currentGame.get,
          newGame);
    });
  }


  public ServerPlayerEntity loadPlayerEntity(ServerPlayerEntity oldPlayerEntity,
      boolean keepEverything) {
    this.minecraftServer.getPlayerList().removePlayer(oldPlayerEntity);
    oldPlayerEntity.getServerWorld().removePlayer(oldPlayerEntity, true);
    playerEntityteractionManager playerEntityteractionManager;
    if (this.minecraftServer.isDemo()) {
      playerEntityteractionManager =
          new DemoplayerEntityteractionManager(
              this.minecraftServer.getWorld(oldPlayerEntity.dimension));
    } else {
      playerEntityteractionManager =
          new playerEntityteractionManager(this.minecraftServer.getWorld(oldPlayerEntity.dimension));
    }

    ServerPlayerEntity serverplayerentity =
        new ServerPlayerEntity(this.minecraftServer, this.minecraftServer.getWorld(oldPlayerEntity.dimension),
            oldPlayerEntity.getGameProfile(), playerEntityteractionManager);
    serverplayerentity.connection = oldPlayerEntity.connection;
    serverplayerentity.copyFrom(oldPlayerEntity, keepEverything);
    oldPlayerEntity.remove(false); // Forge: clone event had a chance to see old data, now discard
                                   // it
    serverplayerentity.dimension = dimension;
    serverplayerentity.setEntityId(oldPlayerEntity.getEntityId());
    serverplayerentity.setPrimaryHand(oldPlayerEntity.getPrimaryHand());

    for (String s : oldPlayerEntity.getTags()) {
      serverplayerentity.addTag(s);
    }

    ServerWorld serverworld = this.minecraftServer.getWorld(oldPlayerEntity.dimension);
    this.setPlayerGameTypeBasedOnOther(serverplayerentity, oldPlayerEntity, serverworld);
    if (blockpos != null) {
      Optional<Vec3d> optional = PlayerEntity.checkBedValidRespawnPosition(
          this.minecraftServer.getWorld(oldPlayerEntity.dimension), blockpos, flag);
      if (optional.isPresent()) {
        Vec3d vec3d = optional.get();
        serverplayerentity.setLocationAndAngles(vec3d.x, vec3d.y, vec3d.z, 0.0F, 0.0F);
        serverplayerentity.setSpawnPoint(blockpos, flag, false, dimension);
      } else {
        serverplayerentity.connection.sendPacket(new SChangeGameStatePacket(0, 0.0F));
      }
    }

    while (!serverworld.hasNoCollisions(serverplayerentity)
        && serverplayerentity.getPosY() < 256.0D) {
      serverplayerentity.setPosition(serverplayerentity.getPosX(),
          serverplayerentity.getPosY() + 1.0D, serverplayerentity.getPosZ());
    }

    WorldInfo worldinfo = serverplayerentity.world.getWorldInfo();
    net.minecraftforge.fml.network.NetworkHooks
        .sendDimensionDataPacket(serverplayerentity.connection.netManager, serverplayerentity);
    serverplayerentity.connection.sendPacket(
        new SRespawnPacket(serverplayerentity.dimension, WorldInfo.byHashing(worldinfo.getSeed()),
            worldinfo.getGenerator(), serverplayerentity.interactionManager.getGameType()));
    BlockPos blockpos1 = serverworld.getSpawnPoint();
    serverplayerentity.connection.setPlayerLocation(serverplayerentity.getPosX(),
        serverplayerentity.getPosY(), serverplayerentity.getPosZ(), serverplayerentity.rotationYaw,
        serverplayerentity.rotationPitch);
    serverplayerentity.connection.sendPacket(new SSpawnPositionPacket(blockpos1));
    serverplayerentity.connection.sendPacket(
        new SServerDifficultyPacket(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
    serverplayerentity.connection.sendPacket(new SSetExperiencePacket(serverplayerentity.experience,
        serverplayerentity.experienceTotal, serverplayerentity.experienceLevel));
    this.minecraftServer.getPlayerList().sendWorldInfo(serverplayerentity, serverworld);
    this.minecraftServer.getPlayerList().updatePermissionLevel(serverplayerentity);
    serverworld.addRespawnedPlayer(serverplayerentity);
    this.minecraftServer.getPlayerList().addPlayer(serverplayerentity);
    this.minecraftServer.getPlayerList().
    this.minecraftServer.getPlayerList().uuidToPlayerMap.put(serverplayerentity.getUniqueID(), serverplayerentity);
    serverplayerentity.addSelfToInternalCraftingInventory();
    serverplayerentity.setHealth(serverplayerentity.getHealth());
    net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerRespawnEvent(serverplayerentity,
        keepEverything);
    return serverplayerentity;
  }

  public List<Pair<String, SetupGameMessage>> generateSetupGameMessage(boolean isLocal) {
    return Collections.singletonList(Pair.of(SetupGameMessage.class.getName(),
        new SetupGameMessage(this.currentGame.getGameType())));
  }

  public MinecraftServer getMinecraftServer() {
    return this.minecraftServer;
  }

  @Override
  public void read(CompoundNBT nbt) {
    if (nbt.contains("gameType", Constants.NBT.TAG_STRING)) {
      GameType gameType = GameRegistry.findRegistry(GameType.class)
          .getValue(new ResourceLocation(nbt.getString("gameType")));
      if (gameType != null) {
        IGameServer<?, ?> gameServer = gameType.createGameServer(this);
        this.loadGame(gameServer, gameServer);
      }
    }
    String gameTypeId = nbt.getString("gameType");
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    return null;
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleServerTick(TickEvent.ServerTickEvent event) {
    switch (event.phase) {
      case START:
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleLivingUpdate(LivingUpdateEvent event) {
    if (!(event.getEntityLiving() instanceof PlayerEntity)) {
      event.getEntityLiving().getCapability(ModCapabilities.LIVING)
          .ifPresent(ILiving::tick);
    }
  }

  @SubscribeEvent
  public void handlePlayerTick(TickEvent.PlayerTickEvent event) {
    switch (event.phase) {
      case END:
        event.player.getCapability(ModCapabilities.LIVING).ifPresent(ILiving::tick);
        break;
      default:
        break;
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingSetTarget(LivingSetAttackTargetEvent event) {
    if (event.getTarget() != null && event.getEntityLiving() instanceof MobEntity) {
      MobEntity mobEntity = (MobEntity) event.getEntityLiving();
      if (mobEntity.isPotionActive(ModEffects.FLASH_BLINDNESS.get())) {
        mobEntity.setAttackTarget(null);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingDeath(LivingDeathEvent event) {
    if (event
        .getEntity()
        .getCapability(ModCapabilities.LIVING)
        .map(living -> living.onDeath(event.getSource()))
        .orElse(false)
        || (event.getSource().getTrueSource() != null && event
            .getSource()
            .getTrueSource()
            .getCapability(ModCapabilities.LIVING)
            .map(living -> living.onKill(event.getEntity()))
            .orElse(false))) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingDrops(LivingDropsEvent event) {
    event
        .getEntity()
        .getCapability(ModCapabilities.LIVING)
        .ifPresent(
            living -> event.setCanceled(living.onDeathDrops(event.getSource(), event.getDrops())));
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingAttack(LivingAttackEvent event) {
    event
        .getEntity()
        .getCapability(ModCapabilities.LIVING)
        .ifPresent(
            living -> event.setCanceled(living.onAttacked(event.getSource(), event.getAmount())));
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingDamage(LivingDamageEvent event) {
    event
        .getEntity()
        .getCapability(ModCapabilities.LIVING)
        .ifPresent(
            living -> event.setAmount(living.onDamaged(event.getSource(), event.getAmount())));
  }

  @SubscribeEvent
  public void handlePlayerClone(PlayerEvent.Clone event) {
    Player.get(event.getPlayer()).copyFrom(Player.get(event.getOriginal()), event.isWasDeath());
  }

  @SubscribeEvent
  public void handleAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
    if (!(event.getObject() instanceof PlayerEntity) && event.getObject() instanceof LivingEntity) {
      event.addCapability(new ResourceLocation(CraftingDead.ID, "living"),
          new SerializableCapabilityProvider<>(
              new DefaultLiving<>((LivingEntity) event.getObject()),
              () -> ModCapabilities.LIVING));
    } else if (event.getObject() instanceof ServerPlayerEntity) {
      event
          .addCapability(this.currentGame.getGameType().getRegistryName(),
              new SerializableCapabilityProvider<>(
                  this.currentGame.createPlayer((ServerPlayerEntity) event.getObject()),
                  () -> ModCapabilities.LIVING));
    }
  }

  @SubscribeEvent
  public void handleAttachCapabilitiesItemStack(AttachCapabilitiesEvent<ItemStack> event) {
    final Item item = event.getObject().getItem();
    int hydration = -1;
    if (item == Items.APPLE || item == Items.RABBIT_STEW) {
      hydration = 2;
    } else if (item == Items.CARROT || item == Items.BEETROOT || item == Items.HONEY_BOTTLE) {
      hydration = 1;
    } else if (item == Items.CHORUS_FRUIT || item == Items.SWEET_BERRIES) {
      hydration = 3;
    } else if (item == Items.ENCHANTED_GOLDEN_APPLE || item == Items.GOLDEN_APPLE
        || item == Items.MUSHROOM_STEW || item == Items.SUSPICIOUS_STEW
        || item == Items.BEETROOT_SOUP || item == Items.MELON_SLICE) {
      hydration = 5;
    } else if (item == Items.GOLDEN_CARROT) {
      hydration = 6;
    }
    if (hydration != -1) {
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "hydration"),
              new SimpleCapabilityProvider<>(new PresetHydration(hydration),
                  () -> ModCapabilities.HYDRATION));
    } else if (item == Items.POTION) {
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "hydration"),
              new SimpleCapabilityProvider<>(new DefaultHydration(),
                  () -> ModCapabilities.HYDRATION));
    }
  }

  @SubscribeEvent
  public void handleUseItem(LivingEntityUseItemEvent.Finish event) {
    event
        .getItem()
        .getCapability(ModCapabilities.HYDRATION)
        .map(hydration -> hydration.getHydration(event.getItem()))
        .ifPresent(hydration -> event
            .getEntityLiving()
            .addPotionEffect(new EffectInstance(ModEffects.HYDRATE.get(), 1, hydration)));
  }
}
