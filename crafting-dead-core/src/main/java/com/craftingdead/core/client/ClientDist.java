package com.craftingdead.core.client;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.IModDist;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.capability.living.player.DefaultPlayer;
import com.craftingdead.core.capability.living.player.SelfPlayer;
import com.craftingdead.core.capability.paint.IPaint;
import com.craftingdead.core.client.audio.EffectsManager;
import com.craftingdead.core.client.crosshair.CrosshairManager;
import com.craftingdead.core.client.gui.IngameGui;
import com.craftingdead.core.client.gui.screen.inventory.GenericContainerScreen;
import com.craftingdead.core.client.gui.screen.inventory.ModInventoryScreen;
import com.craftingdead.core.client.model.GunModel;
import com.craftingdead.core.client.model.PerspectiveAwareModel;
import com.craftingdead.core.client.particle.GrenadeSmokeParticle;
import com.craftingdead.core.client.particle.RGBFlashParticle;
import com.craftingdead.core.client.renderer.entity.AdvancedZombieRenderer;
import com.craftingdead.core.client.renderer.entity.CorpseRenderer;
import com.craftingdead.core.client.renderer.entity.GiantZombieRenderer;
import com.craftingdead.core.client.renderer.entity.SupplyDropRenderer;
import com.craftingdead.core.client.renderer.entity.grenade.C4ExplosiveRenderer;
import com.craftingdead.core.client.renderer.entity.grenade.CylinderGrenadeRenderer;
import com.craftingdead.core.client.renderer.entity.grenade.FragGrenadeRenderer;
import com.craftingdead.core.client.renderer.entity.grenade.SlimGrenadeRenderer;
import com.craftingdead.core.client.renderer.entity.layer.ClothingLayer;
import com.craftingdead.core.client.renderer.entity.layer.EquipmentLayer;
import com.craftingdead.core.client.tutorial.IModTutorialStep;
import com.craftingdead.core.client.tutorial.ModTutorialSteps;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.entity.ModEntityTypes;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.inventory.container.ModContainerTypes;
import com.craftingdead.core.item.ClothingItem;
import com.craftingdead.core.item.GunItem;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.item.PaintItem;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.main.OpenModInventoryMessage;
import com.craftingdead.core.particle.ModParticleTypes;
import com.craftingdead.core.potion.ModEffects;
import com.craftingdead.core.util.ArbitraryTooltips;
import com.craftingdead.core.util.ArbitraryTooltips.TooltipFunction;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.BipedModel.ArmPose;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientDist implements IModDist {

  public static final KeyBinding RELOAD =
      new KeyBinding("key.reload", GLFW.GLFW_KEY_R, "key.categories.gameplay");
  public static final KeyBinding TOGGLE_FIRE_MODE =
      new KeyBinding("key.toggle_fire_mode", GLFW.GLFW_KEY_V, "key.categories.gameplay");
  public static final KeyBinding OPEN_MOD_INVENTORY =
      new KeyBinding("key.craftingdead.inventory", GLFW.GLFW_KEY_Z, "key.categories.inventory");

  private final Minecraft minecraft = Minecraft.getInstance();

  /**
   * Random.
   */
  private static final Random random = new Random();

  public static final ClientConfig clientConfig;
  public static final ForgeConfigSpec clientConfigSpec;

  static {
    final Pair<ClientConfig, ForgeConfigSpec> clientConfigPair =
        new ForgeConfigSpec.Builder().configure(ClientConfig::new);
    clientConfigSpec = clientConfigPair.getRight();
    clientConfig = clientConfigPair.getLeft();
  }

  private final CrosshairManager crosshairManager = new CrosshairManager();

  private final EffectsManager effectsManager = new EffectsManager();

  /**
   * Current camera velocity.
   */
  private Vec2f rotationVelocity = Vec2f.ZERO;

  /**
   * Camera velocity of last tick.
   */
  private Vec2f prevRotationVelocity = this.rotationVelocity;

  private long rollDurationMs = 250L;

  private long joltStartTime = 0;

  private float roll;

  private float pitch;

  private float fovMultiplier = 1.0F;

  private IngameGui ingameGui;

  private TutorialSteps lastTutorialStep;

  private boolean effectsManagerLoaded = false;

  public ClientDist() {
    FMLJavaModLoadingContext.get().getModEventBus().register(this);
    MinecraftForge.EVENT_BUS.register(this);

    ((IReloadableResourceManager) this.minecraft.getResourceManager())
        .addReloadListener(this.crosshairManager);

    this.ingameGui = new IngameGui(this.minecraft, this, CrosshairManager.DEFAULT_CROSSHAIR);

    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientConfigSpec);
  }

  public void joltCamera(float amountPercent, boolean permenantJolt) {
    float randomAmount = amountPercent * (random.nextFloat() + 1.0F);
    float randomNegativeAmount = randomAmount * (random.nextBoolean() ? 1.0F : -1.0F);
    if (permenantJolt) {
      this.rotationVelocity = new Vec2f(this.rotationVelocity.x + randomNegativeAmount * 20.0F,
          this.rotationVelocity.y - randomAmount * 20.0F);
    }
    this.joltStartTime = Util.milliTime();
    this.roll = randomNegativeAmount;
    this.pitch = randomAmount / 3.0F;
    this.fovMultiplier = 1.0F + (-randomAmount / 20.0F);
    this.rollDurationMs = (long) (250L * (amountPercent / 2));
  }

  public void setTutorialStep(ModTutorialSteps step) {
    clientConfig.tutorialStep.set(step);
    Tutorial tutorial = this.minecraft.getTutorial();
    tutorial.setStep(TutorialSteps.NONE);
    tutorial.tutorialStep = step.create(this);
  }

  /**
   * Checks whether the entity is inside the FOV of the game client. The size of the game window is
   * also considered. Blocks in front of player's view are not considered.
   *
   * @param target - The entity to test from the player's view
   * @return <code>true</code> if the entity can be directly seen. <code>false</code> otherwise.
   */
  public boolean isInsideGameFOV(Entity target, boolean considerF5) {
    ActiveRenderInfo activerenderinfo = this.minecraft.gameRenderer.getActiveRenderInfo();
    Vec3d projectedViewVec3d = considerF5 ? activerenderinfo.getProjectedView()
        : target.getPositionVec().add(0, target.getEyeHeight(), 0);
    double viewerX = projectedViewVec3d.getX();
    double viewerY = projectedViewVec3d.getY();
    double viewerZ = projectedViewVec3d.getZ();

    if (!target.isInRangeToRender3d(viewerX, viewerY, viewerZ)) {
      return false;
    }

    AxisAlignedBB renderBoundingBox = target.getRenderBoundingBox();

    // Validation from Vanilla.
    // Generates a render bounding box if it is needed.
    if (renderBoundingBox.hasNaN() || renderBoundingBox.getAverageEdgeLength() == 0.0D) {
      renderBoundingBox = new AxisAlignedBB(target.getX() - 2.0D, target.getY() - 2.0D,
          target.getZ() - 2.0D, target.getX() + 2.0D, target.getY() + 2.0D, target.getZ() + 2.0D);
    }

    return RenderUtil.createClippingHelper(1F, considerF5).isVisible(renderBoundingBox);
  }

  public CrosshairManager getCrosshairManager() {
    return this.crosshairManager;
  }

  public LazyOptional<SelfPlayer> getPlayer() {
    return this.minecraft.player != null
        ? this.minecraft.player
            .getCapability(ModCapabilities.LIVING)
            .filter(living -> living instanceof SelfPlayer)
            .cast()
        : LazyOptional.empty();
  }

  public IngameGui getIngameGui() {
    return this.ingameGui;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  @SubscribeEvent
  public void handleClientSetup(FMLClientSetupEvent event) {
    ScreenManager.registerFactory(ModContainerTypes.PLAYER.get(), ModInventoryScreen::new);
    ScreenManager.registerFactory(ModContainerTypes.VEST.get(), GenericContainerScreen::new);
    ScreenManager
        .registerFactory(ModContainerTypes.SMALL_BACKPACK.get(), GenericContainerScreen::new);
    ScreenManager
        .registerFactory(ModContainerTypes.MEDIUM_BACKPACK.get(), GenericContainerScreen::new);
    ScreenManager
        .registerFactory(ModContainerTypes.LARGE_BACKPACK.get(), GenericContainerScreen::new);
    ScreenManager.registerFactory(ModContainerTypes.GUN_BAG.get(), GenericContainerScreen::new);
    ScreenManager.registerFactory(ModContainerTypes.QUIVER.get(), GenericContainerScreen::new);

    ModelLoaderRegistry
        .registerLoader(new ResourceLocation(CraftingDead.ID, "gun"), GunModel.Loader.INSTANCE);
    ModelLoaderRegistry
        .registerLoader(new ResourceLocation(CraftingDead.ID, "perspective_aware"),
            PerspectiveAwareModel.Loader.INSTANCE);

    ClientRegistry.registerKeyBinding(TOGGLE_FIRE_MODE);
    ClientRegistry.registerKeyBinding(RELOAD);
    ClientRegistry.registerKeyBinding(OPEN_MOD_INVENTORY);

    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.corpse, CorpseRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.corpse, CorpseRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.advancedZombie, AdvancedZombieRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.fastZombie, AdvancedZombieRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.tankZombie, AdvancedZombieRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.weakZombie, AdvancedZombieRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.policeZombie, AdvancedZombieRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.doctorZombie, AdvancedZombieRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.giantZombie, GiantZombieRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.supplyDrop, SupplyDropRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.c4Explosive, C4ExplosiveRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.fireGrenade, CylinderGrenadeRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.fragGrenade, FragGrenadeRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.gasGrenade, CylinderGrenadeRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.pipeGrenade, CylinderGrenadeRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.decoyGrenade, SlimGrenadeRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.smokeGrenade, CylinderGrenadeRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(ModEntityTypes.flashGrenade, SlimGrenadeRenderer::new);

    StartupMessageManager.addModMessage("Loading model layers");

    this.registerPlayerLayer(ClothingLayer::new);
    this
        .registerPlayerLayer(
            renderer -> new EquipmentLayer.Builder<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>()
                .withRenderer(renderer)
                .withSlot(InventorySlotType.MELEE)
                .withCrouchingOrientation(true)
                .build());
    this
        .registerPlayerLayer(
            renderer -> new EquipmentLayer.Builder<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>()
                .withRenderer(renderer)
                .withSlot(InventorySlotType.VEST)
                .withCrouchingOrientation(true)
                .build());
    this
        .registerPlayerLayer(
            renderer -> new EquipmentLayer.Builder<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>()
                .withRenderer(renderer)
                .withSlot(InventorySlotType.HAT)
                .withHeadOrientation(true)
                // Inverts X and Y rotation. This is from Mojang, based on HeadLayer.class.
                // TODO Find a reason to not remove this line. Also, if you remove it, you will
                // need to change the json file of every helmet since the scale affects positions.
                .withArbitraryTransformation(matrix -> matrix.scale(-1F, -1F, 1F))
                .build());
    this
        .registerPlayerLayer(
            renderer -> new EquipmentLayer.Builder<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>()
                .withRenderer(renderer)
                .withSlot(InventorySlotType.GUN)
                .withCrouchingOrientation(true)
                .build());
    this
        .registerPlayerLayer(
            renderer -> new EquipmentLayer.Builder<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>()
                .withRenderer(renderer)
                .withSlot(InventorySlotType.BACKPACK)
                .withCrouchingOrientation(true)
                .build());
  }

  /**
   * Registers a layer into {@link PlayerRenderer}. Can be used normally during
   * {@link FMLClientSetupEvent}.
   *
   * @param function - {@link Function} with a {@link PlayerRenderer} as input and a
   *        {@link LayerRenderer} as output.
   */
  public void registerPlayerLayer(
      Function<PlayerRenderer, LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> function) {
    // A little dirty way, blame Mojang
    this.minecraft.getRenderManager().getSkinMap().forEach((skin, renderer) -> {
      renderer.addLayer(function.apply(renderer));
    });
  }

  @SubscribeEvent
  public void handleParticleFactoryRegisterEvent(ParticleFactoryRegisterEvent event) {
    ParticleManager particleManager = this.minecraft.particles;

    particleManager
        .registerFactory(ModParticleTypes.RGB_FLASH.get(),
            sprite -> new RGBFlashParticle.Factory(sprite));

    particleManager
        .registerFactory(ModParticleTypes.GRENADE_SMOKE.get(),
            sprite -> new GrenadeSmokeParticle.Factory(sprite));
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleSoundLoad(SoundLoadEvent event) {
    this.effectsManager.reload(event.getManager());
  }

  @SubscribeEvent
  public void handlePlaySound(PlaySoundEvent event) {
    this.effectsManager.setLevels(event.getSound(), 5.0F, 1.0F);
  }

  @SubscribeEvent
  public void handleTooltipEvent(ItemTooltipEvent event) {
    Collection<TooltipFunction> functions =
        ArbitraryTooltips.getFunctions(event.getItemStack().getItem());
    int lineIndex = 1;

    // Applies the arbitrary tooltip
    for (TooltipFunction function : functions) {
      World world = event.getEntity() != null ? event.getEntity().world : null;
      ITextComponent tooltip =
          function.createTooltip(event.getItemStack(), world, event.getFlags());
      if (tooltip != null) {
        event.getToolTip().add(lineIndex++, tooltip);
      }
    }
  }

  @SubscribeEvent
  public void handleClientTick(TickEvent.ClientTickEvent event) {
    switch (event.phase) {
      case START:
        // TODO SoundLoadEvent is not called upon initial startup - see
        // https://github.com/MinecraftForge/MinecraftForge/pull/6777
        if (!this.effectsManagerLoaded) {
          this.effectsManager.reload(this.minecraft.getSoundHandler().sndManager);
          this.effectsManagerLoaded = true;
        }
        if (this.minecraft.loadingGui == null
            && (this.minecraft.currentScreen == null || this.minecraft.currentScreen.passEvents)) {
          while (TOGGLE_FIRE_MODE.isPressed()) {
            ItemStack heldStack = this.minecraft.player.getHeldItemMainhand();
            heldStack
                .getCapability(ModCapabilities.GUN)
                .ifPresent(gun -> gun.toggleFireMode(this.minecraft.player, true));
          }
          while (OPEN_MOD_INVENTORY.isPressed()) {
            NetworkChannel.MAIN.getSimpleChannel().sendToServer(new OpenModInventoryMessage());
            if (this.minecraft.getTutorial().tutorialStep instanceof IModTutorialStep) {
              ((IModTutorialStep) this.minecraft.getTutorial().tutorialStep).openModInventory();
            }
          }

          TutorialSteps currentTutorialStep = this.minecraft.gameSettings.tutorialStep;
          if (this.lastTutorialStep != currentTutorialStep) {
            if (currentTutorialStep == TutorialSteps.NONE) {
              this.setTutorialStep(clientConfig.tutorialStep.get());
            }
            this.lastTutorialStep = currentTutorialStep;
          }
          if (this.minecraft.player.isPotionActive(ModEffects.ADRENALINE.get())) {
            this.effectsManager.setAllLevels(1.0F, 0.015F);
          }
        }
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleRawMouse(InputEvent.RawMouseEvent event) {
    if (this.minecraft.getConnection() != null && this.minecraft.currentScreen == null) {
      if (this.minecraft.gameSettings.keyBindAttack.matchesMouseKey(event.getButton())) {
        boolean triggerPressed = event.getAction() == GLFW.GLFW_PRESS;
        ItemStack heldStack = this.minecraft.player.getHeldItemMainhand();
        heldStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> {
          event.setCanceled(true);
          gun.setTriggerPressed(this.minecraft.player, heldStack, triggerPressed, true);
        });
      }
    }
  }

  @SubscribeEvent
  public void handleAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof ClientPlayerEntity) {
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "living"),
              new SerializableCapabilityProvider<>(
                  new SelfPlayer((ClientPlayerEntity) event.getObject()),
                  () -> ModCapabilities.LIVING));
    } else if (event.getObject() instanceof AbstractClientPlayerEntity) {
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "living"),
              new SerializableCapabilityProvider<>(
                  new DefaultPlayer<>((AbstractClientPlayerEntity) event.getObject()),
                  () -> ModCapabilities.LIVING));
    }
  }

  @SubscribeEvent
  public void handleRenderLiving(RenderLivingEvent.Pre<?, BipedModel<?>> event) {
    ItemStack heldStack = event.getEntity().getHeldItemMainhand();
    if (event.getRenderer().getEntityModel() instanceof BipedModel
        && heldStack.getItem() instanceof GunItem) {
      BipedModel<?> model = event.getRenderer().getEntityModel();
      switch (event.getEntity().getPrimaryHand()) {
        case LEFT:
          model.leftArmPose = ArmPose.BOW_AND_ARROW;
          break;
        case RIGHT:
          model.rightArmPose = ArmPose.BOW_AND_ARROW;
          break;
        default:
          break;
      }
    }
  }

  @SubscribeEvent
  public void handleRenderGameOverlayPost(RenderGameOverlayEvent.Post event) {
    switch (event.getType()) {

      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleRenderGameOverlayPre(RenderGameOverlayEvent.Pre event) {
    switch (event.getType()) {
      case ALL:
        this.ingameGui
            .renderGameOverlay(event.getPartialTicks(), event.getWindow().getScaledWidth(),
                event.getWindow().getScaledHeight());
        break;
      case CROSSHAIRS:
        this.getPlayer().ifPresent(player -> {
          ClientPlayerEntity playerEntity = player.getEntity();
          ItemStack heldStack = playerEntity.getHeldItemMainhand();

          event
              .setCanceled(heldStack
                  .getCapability(ModCapabilities.ACTION)
                  .map(action -> action.isActive(playerEntity))
                  .orElse(false)
                  || heldStack
                      .getCapability(ModCapabilities.SCOPE)
                      .map(scope -> scope.isAiming(playerEntity, heldStack))
                      .orElse(false));

          if (!event.isCanceled()) {
            heldStack.getCapability(ModCapabilities.GUN).ifPresent(gunController -> {
              event.setCanceled(true);
              if (gunController.hasCrosshair()) {
                this.ingameGui
                    .renderCrosshairs(gunController.getAccuracy(playerEntity, heldStack),
                        event.getPartialTicks(), event.getWindow().getScaledWidth(),
                        event.getWindow().getScaledHeight());
              }
            });
          }
        });
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleCameraSetup(EntityViewRenderEvent.CameraSetup event) {
    float pct = MathHelper
        .clamp((float) (Util.milliTime() - this.joltStartTime) / this.rollDurationMs, 0.0F, 1.0F);
    float roll = (float) Math.sin(Math.toRadians(180 * pct)) * this.roll;
    float pitch = (float) Math.sin(Math.toRadians(360 * pct)) * this.pitch;
    if (pct == 1.0F) {
      this.roll = 0;
      this.pitch = 0;
    }

    event.setRoll(event.getRoll() + roll);
    event.setPitch(event.getPitch() + pitch);
  }

  @SubscribeEvent
  public void handeFOVUpdate(FOVUpdateEvent event) {
    event.setNewfov(event.getFov() * this.fovMultiplier);
    this.fovMultiplier = 1.0F;
    ItemStack heldStack = this.minecraft.player.getHeldItemMainhand();
    heldStack.getCapability(ModCapabilities.SCOPE).ifPresent(scope -> {
      if (scope.isAiming(this.minecraft.player, heldStack)) {
        event.setNewfov(event.getFov() * scope.getFovModifier(this.minecraft.player, heldStack));
      }
    });
  }

  @SubscribeEvent
  public void handleRenderTick(TickEvent.RenderTickEvent event) {
    switch (event.phase) {
      case START:
        if (this.minecraft.player != null) {
          float smoothYaw =
              MathHelper.lerp(0.15F, this.prevRotationVelocity.x, this.rotationVelocity.x);
          float smoothPitch =
              MathHelper.lerp(0.15F, this.prevRotationVelocity.y, this.rotationVelocity.y);
          this.rotationVelocity = Vec2f.ZERO;
          this.prevRotationVelocity = new Vec2f(smoothYaw, smoothPitch);
          this.minecraft.player.rotateTowards(smoothYaw, smoothPitch);
        }
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleItemColor(ColorHandlerEvent.Item event) {
    // Color for stacks with GUN_CONTROLLER capability
    IItemColor gunColor = (stack,
        tintIndex) -> stack
            .getCapability(ModCapabilities.GUN)
            .map(gunController -> gunController
                .getPaintStack()
                .getCapability(ModCapabilities.PAINT)
                .map(IPaint::getColour)
                .orElse(Optional.empty()))
            .orElse(Optional.empty())
            .orElse(0xFFFFFF) | 0xFF << 24;


    // Registers the color for every matching CD item
    ModItems.ITEMS
        .getEntries()
        .stream()
        .map(RegistryObject::get)
        .filter(item -> item instanceof GunItem)
        .forEach(item -> event.getItemColors().register(gunColor, item));

    // Color for stacks with PAINT_COLOR capability
    IItemColor paintStackColor = (stack, tintIndex) -> stack
        .getCapability(ModCapabilities.PAINT)
        .map(IPaint::getColour)
        .orElse(Optional.empty())
        .orElse(Integer.MAX_VALUE);

    // Registers the color for every matching CD item
    ModItems.ITEMS
        .getEntries()
        .stream()
        .map(RegistryObject::get)
        .filter(item -> item instanceof PaintItem)
        .forEach(item -> event.getItemColors().register(paintStackColor, () -> item));
  }

  @SubscribeEvent
  public void handleTextureStitch(TextureStitchEvent.Pre event) {
    // Automatically adds every painted gun skins
    ModItems.ITEMS
        .getEntries()
        .stream()
        .map(RegistryObject::get)
        .filter(item -> item instanceof GunItem)
        .map(item -> (GunItem) item)
        .forEach(gun -> {
          gun.getAcceptedPaints().stream().filter(PaintItem::hasSkin).forEach(paint -> {
            event
                .addSprite(
                    // Example: "craftingdead:models/guns/m4a1_diamond_paint"
                    new ResourceLocation(paint.getRegistryName().getNamespace(),
                        "models/guns/" + gun.getRegistryName().getPath() + "_"
                            + paint.getRegistryName().getPath()));
          });
        });
  }

  @SubscribeEvent
  public void handleGuiOpen(GuiOpenEvent event) {
    if (this.minecraft.currentScreen instanceof ModInventoryScreen && event.getGui() == null
        && ((ModInventoryScreen) this.minecraft.currentScreen).isTransitioning()) {
      event.setCanceled(true);
    }
  }

  // ================================================================================
  // ASM Hooks
  // ================================================================================

  public static void renderArmsWithExtraSkins(PlayerRenderer renderer, MatrixStack matrix,
      IRenderTypeBuffer buffer, int p_229144_3_, AbstractClientPlayerEntity playerEntity,
      ModelRenderer firstLayerModel, ModelRenderer secondLayerModel) {
    playerEntity.getCapability(ModCapabilities.LIVING).ifPresent(living -> {
      String skinType = playerEntity.getSkinType();
      ItemStack clothingStack = living.getStackInSlot(InventorySlotType.CLOTHING.getIndex());
      if (clothingStack.getItem() instanceof ClothingItem) {
        ClothingItem clothingItem = (ClothingItem) clothingStack.getItem();
        ResourceLocation clothingSkin = clothingItem.getClothingSkin(skinType);

        PlayerModel<AbstractClientPlayerEntity> playermodel = renderer.getEntityModel();
        playermodel.swingProgress = 0.0F;
        playermodel.isSneaking = false;
        playermodel.swimAnimation = 0.0F;
        playermodel.setAngles(playerEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

        firstLayerModel.showModel = true;
        secondLayerModel.showModel = true;

        firstLayerModel.rotateAngleX = 0.0F;
        firstLayerModel
            .render(matrix, buffer.getBuffer(RenderType.getEntityTranslucent(clothingSkin)),
                p_229144_3_, OverlayTexture.DEFAULT_UV);
        secondLayerModel.rotateAngleX = 0.0F;
        secondLayerModel
            .render(matrix, buffer.getBuffer(RenderType.getEntityTranslucent(clothingSkin)),
                p_229144_3_, OverlayTexture.DEFAULT_UV);
      }
    });
  }
}
