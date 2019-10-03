package com.craftingdead.mod;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.craftingdead.mod.item.BackpackItem;
import com.craftingdead.mod.test.BackpackContainer;
import com.craftingdead.mod.test.ClientProxy;
import com.craftingdead.mod.test.IProxy;
import com.craftingdead.mod.test.ServerProxy;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.mod.block.ModBlocks;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.player.ServerPlayer;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.entity.ModEntityTypes;
import com.craftingdead.mod.item.ModItems;
import com.craftingdead.mod.masterserver.net.protocol.handshake.HandshakeProtocol;
import com.craftingdead.mod.masterserver.net.protocol.handshake.HandshakeSession;
import com.craftingdead.mod.net.NetworkChannel;
import com.craftingdead.mod.server.ServerDist;
import com.craftingdead.mod.tileentity.ModTileEntityTypes;
import com.craftingdead.network.TcpClient;
import com.craftingdead.network.pipeline.NetworkManager;
import com.craftingdead.network.util.TransportType;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.EventLoopGroup;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.JarVersionLookupHandler;

@Mod(CraftingDead.ID)
public class CraftingDead {

  /**
   * Mod ID.
   */
  public static final String ID = "craftingdead";

  public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
  /**
   * Mod version.
   */
  public static final String VERSION;

  /**
   * Mod display name.
   */
  public static final String DISPLAY_NAME;

  static {
    VERSION =
        JarVersionLookupHandler.getImplementationVersion(CraftingDead.class).orElse("[version]");
    assert VERSION != null;
    DISPLAY_NAME =
        JarVersionLookupHandler.getImplementationTitle(CraftingDead.class).orElse("[display_name]");
    assert DISPLAY_NAME != null;
  }

  /**
   * Logger.
   */
  private static final Logger logger = LogManager.getLogger();

  /**
   * Singleton.
   */
  @Getter
  private static CraftingDead instance;

  /**
   * Mod distribution.
   */
  @Getter
  private final IModDist modDist;

  /**
   * {@link ScheduledExecutorService} for polling the connection to the Master Server.
   */
  private final ScheduledExecutorService poller =
      Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder()
          .setNameFormat("Master Server Connection Poller #%d").setDaemon(true).build());

  /**
   * {@link EventLoopGroup} for {@link #tcpClient}.
   */
  private EventLoopGroup group;

  /**
   * {@link TcpClient} used to connect to the Master Server.
   */
  private TcpClient tcpClient;

  /**
   * The {@link NetworkManager} for the Master Server connection.
   */
  private Optional<NetworkManager> networkManager = Optional.empty();

  @Setter
  private boolean retryConnect = true;

  public CraftingDead() {
    instance = this;

    this.modDist = DistExecutor.runForDist(() -> ClientDist::new, () -> ServerDist::new);

    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.register(this);

    // These need to stay in this order as certain Items require access to EntityTypes etc.
    ModEntityTypes.initialize();
    modEventBus.addGenericListener(EntityType.class, ModEntityTypes::register);

    ModBlocks.initialize();
    modEventBus.addGenericListener(Block.class, ModBlocks::register);

    ModTileEntityTypes.initialize();
    modEventBus.addGenericListener(TileEntityType.class, ModTileEntityTypes::register);

    ModItems.initialize();
    modEventBus.addGenericListener(Item.class, ModItems::register);

    MinecraftForge.EVENT_BUS.register(this);

    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CommonConfig.clientConfigSpec);
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.commonConfigSpec);
  }

  private void pollConnection() {
    if (!this.isConnected() && this.retryConnect) {
      logger.info("Attempting to connect to master server");
      try {
        final String host = CommonConfig.commonConfig.masterServerHost.get();
        final int port = CommonConfig.commonConfig.masterServerPort.get();
        this.networkManager = Optional.of(this.tcpClient.connect(
            InetSocketAddress.createUnresolved(host, port),
            (networkManager) -> new HandshakeSession(networkManager, this.modDist::handleConnect),
            HandshakeProtocol.INSTANCE));
      } catch (Throwable t) {
        logger.warn("Master server connection failed -> {}", t.getMessage());
      }
    }
  }



  public void tickConnection() {
    this.tcpClient.tick();
  }

  public boolean isConnected() {
    return this.networkManager.map((networkManager) -> networkManager.isChannelOpen())
        .orElse(false);
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  @SubscribeEvent
  public void handleCommonSetup(FMLCommonSetupEvent event) {
    logger.info("Starting {}, version {}", DISPLAY_NAME, VERSION);
    NetworkChannel.loadChannels();
    logger.info("Registering capabilities");
    ModCapabilities.registerCapabilities();
    proxy.init();
  }


  @SubscribeEvent
  public void handleLoadComplete(FMLLoadCompleteEvent event) {
    TransportType transport =
        this.modDist.isUsingNativeTransport() ? TransportType.BEST : TransportType.NIO;
    this.group = transport.getEventLoopGroupFactory().apply(0, null);
    this.tcpClient = new TcpClient(this.group, transport.getSocketChannel());
    this.poller.scheduleAtFixedRate(this::pollConnection, 0L, 30L, TimeUnit.SECONDS);
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handlePlayerTick(TickEvent.PlayerTickEvent event) {
    switch (event.phase) {
      case END:
        event.player.getCapability(ModCapabilities.PLAYER).ifPresent((player) -> player.tick());
        ItemStack itemStack = event.player.getHeldItemMainhand();
        itemStack.getCapability(ModCapabilities.TRIGGERABLE)
            .ifPresent((triggerable) -> triggerable.tick(itemStack, event.player));
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleLivingDeath(LivingDeathEvent event) {
    if (!event.isCanceled() && event.getEntity() instanceof PlayerEntity) {
      event.getEntity().getCapability(ModCapabilities.PLAYER)
          .ifPresent((player) -> event.setCanceled(player.onDeath(event.getSource())));
    }
    if (!event.isCanceled() && event.getSource().getTrueSource() instanceof PlayerEntity) {
      event.getSource().getTrueSource().getCapability(ModCapabilities.PLAYER)
          .ifPresent((player) -> event.setCanceled(player.onKill(event.getEntity())));
    }
  }


  @SubscribeEvent
  public void handlePlayerClone(PlayerEvent.Clone event) {
    event.getEntityPlayer().getCapability(ModCapabilities.PLAYER).<ServerPlayer>cast()
        .ifPresent((player) -> {
          event.getOriginal().getCapability(ModCapabilities.PLAYER).<ServerPlayer>cast()
              .ifPresent((that) -> {
                player.copyFrom(that, event.isWasDeath());
              });
        });
  }

  @SubscribeEvent
  public void handleAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof ServerPlayerEntity) {
      ServerPlayer player = new ServerPlayer((ServerPlayerEntity) event.getObject());
      event.addCapability(new ResourceLocation(CraftingDead.ID, "player"),
          new SerializableProvider<>(player, ModCapabilities.PLAYER));
    }
  }
}
