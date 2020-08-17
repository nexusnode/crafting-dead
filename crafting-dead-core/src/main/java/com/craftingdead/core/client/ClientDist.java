package com.craftingdead.core.client;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.IModDist;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.capability.gun.IGun;
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
import com.craftingdead.core.client.renderer.CameraManager;
import com.craftingdead.core.client.renderer.entity.AdvancedZombieRenderer;
import com.craftingdead.core.client.renderer.entity.GiantZombieRenderer;
import com.craftingdead.core.client.renderer.entity.SupplyDropRenderer;
import com.craftingdead.core.client.renderer.entity.grenade.C4ExplosiveRenderer;
import com.craftingdead.core.client.renderer.entity.grenade.CylinderGrenadeRenderer;
import com.craftingdead.core.client.renderer.entity.grenade.FragGrenadeRenderer;
import com.craftingdead.core.client.renderer.entity.grenade.SlimGrenadeRenderer;
import com.craftingdead.core.client.renderer.entity.layer.ClothingLayer;
import com.craftingdead.core.client.renderer.entity.layer.EquipmentLayer;
import com.craftingdead.core.client.renderer.item.ItemRendererManager;
import com.craftingdead.core.client.tutorial.IModTutorialStep;
import com.craftingdead.core.client.tutorial.ModTutorialSteps;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.entity.ModEntityTypes;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.inventory.container.ModContainerTypes;
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
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.noties.tumbleweed.TweenManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.BipedModel.ArmPose;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.client.model.ModelLoader;
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
  public static final KeyBinding REMOVE_MAGAZINE =
      new KeyBinding("key.remove_magazine", GLFW.GLFW_KEY_J, "key.categories.gameplay");
  public static final KeyBinding TOGGLE_FIRE_MODE =
      new KeyBinding("key.toggle_fire_mode", GLFW.GLFW_KEY_V, "key.categories.gameplay");
  public static final KeyBinding OPEN_MOD_INVENTORY =
      new KeyBinding("key.craftingdead.inventory", GLFW.GLFW_KEY_Z, "key.categories.inventory");

  private static final ResourceLocation ADRENALINE_SHADER =
      new ResourceLocation(CraftingDead.ID, "shaders/post/adrenaline.json");

  public static final ClientConfig clientConfig;
  public static final ForgeConfigSpec clientConfigSpec;

  static {
    final Pair<ClientConfig, ForgeConfigSpec> clientConfigPair =
        new ForgeConfigSpec.Builder().configure(ClientConfig::new);
    clientConfigSpec = clientConfigPair.getRight();
    clientConfig = clientConfigPair.getLeft();
  }

  private final Minecraft minecraft;

  private final CrosshairManager crosshairManager;

  private final EffectsManager effectsManager;

  private final IngameGui ingameGui;

  private final ItemRendererManager itemRendererManager;

  private final CameraManager cameraManager;

  private TutorialSteps lastTutorialStep;

  private boolean effectsManagerLoaded = false;

  private long adrenalineShaderStartTime = 0L;

  private boolean freezeMovementInput = false;

  private TweenManager tweenManager = TweenManager.create();

  private float lastTime = 0F;

  private boolean wasAdrenalineActive;

  private float lastFov;

  private float fov;

  public ClientDist() {
    FMLJavaModLoadingContext.get().getModEventBus().register(this);
    MinecraftForge.EVENT_BUS.register(this);
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientConfigSpec);

    this.minecraft = Minecraft.getInstance();
    this.crosshairManager = new CrosshairManager();
    // Minecraft is null on date gen launch
    if (this.minecraft != null) {
      ((IReloadableResourceManager) this.minecraft.getResourceManager())
          .addReloadListener(this.crosshairManager);
    }
    this.effectsManager = new EffectsManager();
    this.ingameGui = new IngameGui(this.minecraft, this, CrosshairManager.DEFAULT_CROSSHAIR);
    this.itemRendererManager = new ItemRendererManager();
    this.cameraManager = new CameraManager();
  }

  public void setTutorialStep(ModTutorialSteps step) {
    clientConfig.tutorialStep.set(step);
    Tutorial tutorial = this.minecraft.getTutorial();
    tutorial.setStep(TutorialSteps.NONE);
    tutorial.tutorialStep = step.create(this);
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

  public void setFreezeMovementInput(boolean freezeMovementInput) {
    this.freezeMovementInput = freezeMovementInput;
  }

  public TweenManager getTweenManager() {
    return this.tweenManager;
  }

  public CameraManager getCameraManager() {
    return this.cameraManager;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  @SubscribeEvent
  public void handleModelRegistry(ModelRegistryEvent event) {
    this.itemRendererManager.gatherItemRenderers();
    this.itemRendererManager.getModelDependencies().forEach(ModelLoader::addSpecialModel);
  }

  @SubscribeEvent
  public void handleModelBake(ModelBakeEvent event) {
    this.itemRendererManager.refreshCachedModels();
  }

  @SubscribeEvent
  public void handleClientSetup(FMLClientSetupEvent event) {
    StartupMessageManager.addModMessage("Registering container screen factories");

    ScreenManager.registerFactory(ModContainerTypes.PLAYER.get(), ModInventoryScreen::new);
    ScreenManager.registerFactory(ModContainerTypes.VEST.get(), GenericContainerScreen::new);

    StartupMessageManager.addModMessage("Registering model loaders");

    ModelLoaderRegistry
        .registerLoader(new ResourceLocation(CraftingDead.ID, "gun"), GunModel.Loader.INSTANCE);
    ModelLoaderRegistry
        .registerLoader(new ResourceLocation(CraftingDead.ID, "perspective_aware"),
            PerspectiveAwareModel.Loader.INSTANCE);

    StartupMessageManager.addModMessage("Registering key bindings");

    ClientRegistry.registerKeyBinding(TOGGLE_FIRE_MODE);
    ClientRegistry.registerKeyBinding(RELOAD);
    ClientRegistry.registerKeyBinding(REMOVE_MAGAZINE);
    ClientRegistry.registerKeyBinding(OPEN_MOD_INVENTORY);

    StartupMessageManager.addModMessage("Registering entity renderers");

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
    final ParticleManager particleManager = this.minecraft.particles;
    particleManager.registerFactory(ModParticleTypes.RGB_FLASH.get(),
        RGBFlashParticle.Factory::new);
    particleManager.registerFactory(ModParticleTypes.GRENADE_SMOKE.get(),
        GrenadeSmokeParticle.Factory::new);
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleSoundLoad(SoundLoadEvent event) {
    this.effectsManager.reload(event.getManager());
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
        this.lastTime = (float) Math.ceil(this.lastTime);
        if (this.minecraft.isGamePaused()) {
          this.getPlayer().ifPresent(player -> {
            ItemStack heldStack = player.getEntity().getHeldItemMainhand();
            heldStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> {
              gun.setTriggerPressed(player, heldStack, false, true);
              if (gun.isPerformingRightMouseAction()) {
                gun.toggleRightMouseAction(player, true);
              }
            });
          });
        }
        // TODO SoundLoadEvent is not called upon initial startup - see
        // https://github.com/MinecraftForge/MinecraftForge/pull/6777
        if (!this.effectsManagerLoaded) {
          this.effectsManager.reload(this.minecraft.getSoundHandler().sndManager);
          this.effectsManagerLoaded = true;
        }
        if (this.minecraft.loadingGui == null
            && (this.minecraft.currentScreen == null || this.minecraft.currentScreen.passEvents)) {
          final ItemStack heldStack = this.minecraft.player.getHeldItemMainhand();
          this.getPlayer().ifPresent(player -> {
            while (TOGGLE_FIRE_MODE.isPressed()) {
              heldStack
                  .getCapability(ModCapabilities.GUN)
                  .ifPresent(gun -> gun.toggleFireMode(player, true));
            }
            while (RELOAD.isPressed()) {
              heldStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> gun.reload(player));
            }
            while (REMOVE_MAGAZINE.isPressed()) {
              heldStack.getCapability(ModCapabilities.GUN)
                  .ifPresent(gun -> gun.removeMagazine(player));
            }
          });
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
            this.wasAdrenalineActive = true;
            this.effectsManager.setHighpassLevels(1.0F, 0.015F);
            this.effectsManager.setDirectHighpassForAll();
          } else if (this.wasAdrenalineActive) {
            this.wasAdrenalineActive = false;
            this.effectsManager.removeFilterForAll();
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
        this.getPlayer()
            .ifPresent(player -> heldStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> {
              event.setCanceled(true);
              gun.setTriggerPressed(player, heldStack, triggerPressed, true);
            }));
      } else if (this.minecraft.gameSettings.keyBindUseItem.matchesMouseKey(event.getButton())) {
        ItemStack heldStack = this.minecraft.player.getHeldItemMainhand();
        this.getPlayer()
            .ifPresent(player -> heldStack.getCapability(ModCapabilities.GUN)
                .filter(gun -> gun
                    .getRightMouseActionTriggerType() == IGun.RightMouseActionTriggerType.HOLD)
                .ifPresent(gun -> {
                  if ((event.getAction() == GLFW.GLFW_PRESS && !gun.isPerformingRightMouseAction())
                      || (event.getAction() == GLFW.GLFW_RELEASE
                          && gun.isPerformingRightMouseAction())) {
                    gun.toggleRightMouseAction(player, true);
                  }
                  event.setCanceled(true);
                }));
      }
    }
  }

  @SubscribeEvent
  public void handleInputUpdate(InputUpdateEvent event) {
    if (this.freezeMovementInput) {
      final MovementInput input = event.getMovementInput();
      input.forwardKeyDown = input.backKeyDown =
          input.leftKeyDown = input.rightKeyDown = input.jump = input.sneaking = false;
      input.moveForward = 0.0F;
      input.moveStrafe = 0.0F;
      this.freezeMovementInput = false;
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
  public void handleRenderGameOverlayPre(RenderGameOverlayEvent.Pre event) {
    switch (event.getType()) {
      case ALL:
        this.getPlayer().ifPresent(player -> this.ingameGui
            .renderGameOverlay(player, event.getPartialTicks(), event.getWindow().getScaledWidth(),
                event.getWindow().getScaledHeight()));
        break;
      case CROSSHAIRS:
        this.getPlayer().ifPresent(player -> {
          ClientPlayerEntity playerEntity = player.getEntity();
          ItemStack heldStack = playerEntity.getHeldItemMainhand();

          event
              .setCanceled(player.getActionProgress().isPresent()
                  || heldStack.getCapability(ModCapabilities.SCOPE)
                      .map(scope -> scope.isAiming(playerEntity, heldStack)).orElse(false));

          if (!event.isCanceled()) {
            heldStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> {
              event.setCanceled(true);
              if (gun.hasCrosshair()) {
                this.ingameGui
                    .renderCrosshairs(gun.getAccuracy(player, heldStack),
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
    final Vector3f rotations = this.cameraManager.getCameraRotation();
    event.setPitch(event.getPitch() + rotations.getX());
    event.setYaw(event.getYaw() + rotations.getY());
    event.setRoll(event.getRoll() + rotations.getZ());
  }

  @SubscribeEvent
  public void handeFOVUpdate(FOVUpdateEvent event) {
    event.setNewfov(event.getNewfov() + this.cameraManager.getFov());
  }

  @SubscribeEvent
  public void handeFOVUpdate(EntityViewRenderEvent.FOVModifier event) {

    ItemStack heldStack = this.minecraft.player.getHeldItemMainhand();
    float newFov = heldStack.getCapability(ModCapabilities.SCOPE)
        .filter(scope -> scope.isAiming(this.minecraft.player, heldStack))
        .map(scope -> 1 / scope.getZoomMultiplier(this.minecraft.player, heldStack)).orElse(1.0F);

    this.lastFov = this.fov;
    this.fov = MathHelper.lerp(0.25F, this.fov, newFov);

    event.setFOV(event.getFOV()
        * MathHelper.lerp(this.minecraft.getRenderPartialTicks(), this.lastFov, this.fov));
  }

  @SubscribeEvent
  public void handleRenderTick(TickEvent.RenderTickEvent event) {
    switch (event.phase) {
      case START:
        float currentTime = (float) Math.floor(this.lastTime) + event.renderTickTime;
        float deltaTime = (currentTime - this.lastTime) * 50;
        this.lastTime = currentTime;
        this.tweenManager.update(deltaTime);

        if (this.minecraft.player != null) {
          final Vec2f rotationVelocity = this.cameraManager.getLookRotationVelocity();
          this.minecraft.player.rotateTowards(rotationVelocity.y, rotationVelocity.x);
        }
        break;
      case END:
        if (this.minecraft.player != null) {
          this.updateAdrenalineShader(event.renderTickTime);
        }
        break;
      default:
        break;
    }
  }

  private void updateAdrenalineShader(float partialTicks) {
    final GameRenderer gameRenderer = this.minecraft.gameRenderer;
    final boolean shaderLoaded = gameRenderer.getShaderGroup() != null
        && gameRenderer.getShaderGroup().getShaderGroupName()
            .equals(ADRENALINE_SHADER.toString());
    if (this.minecraft.player.isPotionActive(ModEffects.ADRENALINE.get())) {
      final long currentTime = Util.milliTime();
      if (this.adrenalineShaderStartTime == 0L) {
        this.adrenalineShaderStartTime = currentTime;
      }
      float progress = MathHelper
          .clamp(((currentTime - this.adrenalineShaderStartTime)
              - partialTicks) / 5000.0F, 0.0F, 1.0F);
      if (!shaderLoaded) {
        if (gameRenderer.getShaderGroup() != null) {
          gameRenderer.stopUseShader();
        }
        gameRenderer.loadShader(ADRENALINE_SHADER);
      }
      ShaderGroup shaderGroup = gameRenderer.getShaderGroup();
      RenderUtil.updateUniform("Saturation", progress * 0.25F, shaderGroup);
    } else if (shaderLoaded) {
      this.adrenalineShaderStartTime = 0L;
      gameRenderer.stopUseShader();
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
    if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) {
      this.itemRendererManager.getTexturesToStitch().forEach(event::addSprite);
    }
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

  public static boolean renderLivingPre(LivingRenderer<?, ?> renderer, final LivingEntity entity,
      final MatrixStack matrixStack, final IVertexBuilder builder, final int light,
      final int overlay, final float limbSwing, final float limbSwingAmount, final float ageInTicks,
      final float netHeadYaw, final float headPitch) {
    return false;
  }

  public static void renderLivingPost(LivingRenderer<?, ?> renderer, final LivingEntity entity,
      final MatrixStack matrixStack, final IVertexBuilder builder, final int light,
      final int overlay, final float limbSwing, final float limbSwingAmount, final float ageInTicks,
      final float netHeadYaw, final float headPitch) {}

  public static void renderArmWithClothing(PlayerRenderer renderer, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer,
      int packedLight, AbstractClientPlayerEntity playerEntity, ModelRenderer armRenderer,
      ModelRenderer armwearRenderer) {
    playerEntity.getCapability(ModCapabilities.LIVING).ifPresent(living -> {
      String skinType = playerEntity.getSkinType();
      ItemStack clothingStack =
          living.getItemHandler().getStackInSlot(InventorySlotType.CLOTHING.getIndex());
      clothingStack.getCapability(ModCapabilities.CLOTHING).ifPresent(clothing -> {
        ResourceLocation clothingSkin = clothing.getTexture(skinType);

        PlayerModel<AbstractClientPlayerEntity> playerModel = renderer.getEntityModel();
        playerModel.swingProgress = 0.0F;
        playerModel.isSneak = false;
        playerModel.swimAnimation = 0.0F;
        playerModel.setRotationAngles(playerEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

        armRenderer.showModel = true;
        armwearRenderer.showModel = true;

        armRenderer.rotateAngleX = 0.0F;
        armRenderer.render(matrixStack,
            renderTypeBuffer.getBuffer(RenderType.getEntityTranslucent(clothingSkin)), packedLight,
            OverlayTexture.NO_OVERLAY);
        armwearRenderer.rotateAngleX = 0.0F;
        armwearRenderer.render(matrixStack,
            renderTypeBuffer.getBuffer(RenderType.getEntityTranslucent(clothingSkin)), packedLight,
            OverlayTexture.NO_OVERLAY);
      });
    });
  }
}
