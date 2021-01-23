/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.core.client;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.IModDist;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.core.capability.living.Player;
import com.craftingdead.core.capability.paint.IPaint;
import com.craftingdead.core.client.audio.EffectsManager;
import com.craftingdead.core.client.crosshair.CrosshairManager;
import com.craftingdead.core.client.gui.IngameGui;
import com.craftingdead.core.client.gui.screen.inventory.GenericContainerScreen;
import com.craftingdead.core.client.gui.screen.inventory.PlayerScreen;
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
import com.craftingdead.core.entity.grenade.FlashGrenadeEntity;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.inventory.container.ModContainerTypes;
import com.craftingdead.core.item.GunItem;
import com.craftingdead.core.item.ModItemModelsProperties;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.item.PaintItem;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.OpenModInventoryMessage;
import com.craftingdead.core.particle.ModParticleTypes;
import com.craftingdead.core.potion.ModEffects;
import com.craftingdead.core.util.ArbitraryTooltips;
import com.craftingdead.core.util.ArbitraryTooltips.TooltipFunction;
import com.craftingdead.core.util.Text;
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
import net.minecraft.potion.EffectInstance;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class ClientDist implements IModDist {

  public static final KeyBinding RELOAD =
      new KeyBinding("key.reload", GLFW.GLFW_KEY_R, "key.categories.gameplay");
  public static final KeyBinding REMOVE_MAGAZINE =
      new KeyBinding("key.remove_magazine", GLFW.GLFW_KEY_J, "key.categories.gameplay");
  public static final KeyBinding TOGGLE_FIRE_MODE =
      new KeyBinding("key.toggle_fire_mode", GLFW.GLFW_KEY_V, "key.categories.gameplay");
  public static final KeyBinding OPEN_MOD_INVENTORY =
      new KeyBinding("key.craftingdead.inventory", GLFW.GLFW_KEY_Z, "key.categories.inventory");

  public static final ClientConfig clientConfig;
  public static final ForgeConfigSpec clientConfigSpec;

  static {
    final Pair<ClientConfig, ForgeConfigSpec> clientConfigPair =
        new ForgeConfigSpec.Builder().configure(ClientConfig::new);
    clientConfigSpec = clientConfigPair.getRight();
    clientConfig = clientConfigPair.getLeft();
  }

  private static final ResourceLocation ADRENALINE_SHADER =
      new ResourceLocation(CraftingDead.ID, "shaders/post/adrenaline.json");

  private static final int DOUBLE_CLICK_DURATION = 500;

  private final Minecraft minecraft;

  private final CrosshairManager crosshairManager;

  private final IngameGui ingameGui;

  private final ItemRendererManager itemRendererManager;

  private final CameraManager cameraManager;

  private EffectsManager effectsManager;

  private TutorialSteps lastTutorialStep;

  private long adrenalineShaderStartTime = 0L;

  private TweenManager tweenManager = TweenManager.create();

  private float lastTime = 0F;

  private boolean wasAdrenalineActive;

  private float lastFov;

  private float fov;

  private boolean wasSneaking;
  private long lastSneakPressTime;

  public ClientDist() {
    final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    modBus.addListener(this::handleModelRegistry);
    modBus.addListener(this::handleModelBake);
    modBus.addListener(this::handleClientSetup);
    modBus.addListener(this::handleParticleFactoryRegisterEvent);
    modBus.addListener(this::handleItemColor);
    modBus.addListener(this::handleTextureStitch);
    modBus.addListener(this::handleSoundLoad);

    MinecraftForge.EVENT_BUS.register(this);
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientConfigSpec);

    this.minecraft = Minecraft.getInstance();
    this.crosshairManager = new CrosshairManager();
    // Minecraft is null on date gen launch
    if (this.minecraft != null) {
      ((IReloadableResourceManager) this.minecraft.getResourceManager())
          .addReloadListener(this.crosshairManager);
    }
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

  public IngameGui getIngameGui() {
    return this.ingameGui;
  }

  public TweenManager getTweenManager() {
    return this.tweenManager;
  }

  public CameraManager getCameraManager() {
    return this.cameraManager;
  }

  public ItemRendererManager getItemRendererManager() {
    return this.itemRendererManager;
  }

  /**
   * Get the {@link Minecraft} instance. If accessing {@link Minecraft} from a common class
   * (contains both client and server code) don't access fields directly from {@link Minecraft} as
   * it will cause class loading problems. To safely access {@link ClientPlayerEntity} in a
   * multi-sided environment, use {@link #getPlayer()}.
   * 
   * @return {@link Minecraft}
   */
  public Minecraft getMinecraft() {
    return this.minecraft;
  }

  @SuppressWarnings("unchecked")
  public Optional<IPlayer<ClientPlayerEntity>> getPlayer() {
    return Optional.ofNullable(this.minecraft.player)
        .flatMap(p -> p.getCapability(ModCapabilities.LIVING).resolve())
        .filter(living -> living instanceof IPlayer)
        .map(living -> (IPlayer<ClientPlayerEntity>) living);
  }

  @SuppressWarnings("unchecked")
  public IPlayer<ClientPlayerEntity> getExpectedPlayer() {
    return ModCapabilities.getExpected(ModCapabilities.LIVING, this.minecraft.player,
        IPlayer.class);
  }

  public boolean isRightMouseDown() {
    return this.minecraft.gameSettings.keyBindUseItem.isKeyDown();
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  /**
   * This has to be handled on the mod bus and forge bus.
   */
  @SubscribeEvent
  public void handleSoundLoad(SoundLoadEvent event) {
    this.effectsManager = new EffectsManager(event.getManager());
  }

  private void handleModelRegistry(ModelRegistryEvent event) {
    StartupMessageManager.addModMessage("Registering model loaders");
    StartupMessageManager.addModMessage("Gathering item renderers");
    this.itemRendererManager.gatherItemRenderers();
    StartupMessageManager.addModMessage("Registering special models");
    this.itemRendererManager.getModelDependencies().forEach(ModelLoader::addSpecialModel);
  }

  private void handleModelBake(ModelBakeEvent event) {
    this.itemRendererManager.refreshCachedModels();
  }

  private void handleClientSetup(FMLClientSetupEvent event) {
    ModItemModelsProperties.register();

    StartupMessageManager.addModMessage("Registering tooltips");

    ArbitraryTooltips.registerTooltip(ModItems.SCUBA_MASK,
        (stack, world, tooltipFlags) -> Text
            .translate("item_lore.clothing_item.water_breathing")
            .mergeStyle(TextFormatting.GRAY));

    ArbitraryTooltips.registerTooltip(ModItems.SCUBA_CLOTHING,
        (stack, world, tooltipFlags) -> Text
            .translate("item_lore.clothing_item.water_speed")
            .mergeStyle(TextFormatting.GRAY));

    StartupMessageManager.addModMessage("Registering container screen factories");

    ScreenManager.registerFactory(ModContainerTypes.PLAYER.get(), PlayerScreen::new);
    ScreenManager.registerFactory(ModContainerTypes.VEST.get(), GenericContainerScreen::new);

    StartupMessageManager.addModMessage("Registering key bindings");

    ClientRegistry.registerKeyBinding(TOGGLE_FIRE_MODE);
    ClientRegistry.registerKeyBinding(RELOAD);
    ClientRegistry.registerKeyBinding(REMOVE_MAGAZINE);
    ClientRegistry.registerKeyBinding(OPEN_MOD_INVENTORY);

    StartupMessageManager.addModMessage("Registering entity renderers");

    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.advancedZombie,
        AdvancedZombieRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.fastZombie,
        AdvancedZombieRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.tankZombie,
        AdvancedZombieRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.weakZombie,
        AdvancedZombieRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.policeZombie,
        AdvancedZombieRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.doctorZombie,
        AdvancedZombieRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.giantZombie,
        GiantZombieRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.supplyDrop,
        SupplyDropRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.c4Explosive,
        C4ExplosiveRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.fireGrenade,
        CylinderGrenadeRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.fragGrenade,
        FragGrenadeRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.pipeGrenade,
        CylinderGrenadeRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.decoyGrenade,
        SlimGrenadeRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.smokeGrenade,
        CylinderGrenadeRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.flashGrenade,
        SlimGrenadeRenderer::new);

    StartupMessageManager.addModMessage("Loading model layers");

    this.registerPlayerLayer(ClothingLayer::new);
    this.registerPlayerLayer(
        renderer -> new EquipmentLayer.Builder<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>()
            .withRenderer(renderer)
            .withSlot(InventorySlotType.MELEE)
            .withCrouchingOrientation(true)
            .build());
    this.registerPlayerLayer(
        renderer -> new EquipmentLayer.Builder<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>()
            .withRenderer(renderer)
            .withSlot(InventorySlotType.VEST)
            .withCrouchingOrientation(true)
            .build());
    this.registerPlayerLayer(
        renderer -> new EquipmentLayer.Builder<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>()
            .withRenderer(renderer)
            .withSlot(InventorySlotType.HAT)
            .withHeadOrientation(true)
            // Inverts X and Y rotation. This is from Mojang, based on HeadLayer.class.
            // TODO Find a reason to not remove this line. Also, if you remove it, you will
            // need to change the json file of every helmet since the scale affects positions.
            .withArbitraryTransformation(matrix -> matrix.scale(-1F, -1F, 1F))
            .build());
    this.registerPlayerLayer(
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
  private void registerPlayerLayer(
      Function<PlayerRenderer, LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> function) {
    // A little dirty way, blame Mojang
    this.minecraft.getRenderManager().getSkinMap().forEach((skin, renderer) -> {
      renderer.addLayer(function.apply(renderer));
    });
  }

  private void handleParticleFactoryRegisterEvent(ParticleFactoryRegisterEvent event) {
    final ParticleManager particleManager = this.minecraft.particles;
    particleManager.registerFactory(ModParticleTypes.RGB_FLASH.get(),
        RGBFlashParticle.Factory::new);
    particleManager.registerFactory(ModParticleTypes.GRENADE_SMOKE.get(),
        GrenadeSmokeParticle.Factory::new);
  }

  private void handleItemColor(ColorHandlerEvent.Item event) {
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

  private void handleTextureStitch(TextureStitchEvent.Pre event) {
    if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) {
      this.itemRendererManager.getTexturesToStitch().forEach(event::addSprite);
    }
  }

  // ================================================================================
  // Client Forge Events
  // ================================================================================

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
        IPlayer<ClientPlayerEntity> player = this.getPlayer().orElse(null);
        if (player != null) {
          ItemStack heldStack = player.getEntity().getHeldItemMainhand();
          IGun gun = heldStack.getCapability(ModCapabilities.GUN).orElse(null);

          // Stop performing action if game is paused
          if (this.minecraft.isGamePaused() && gun != null) {
            gun.setTriggerPressed(player, heldStack, false, true);
            if (gun.isPerformingRightMouseAction()) {
              gun.toggleRightMouseAction(player, true);
            }
          }

          if (this.minecraft.loadingGui == null && (this.minecraft.currentScreen == null
              || this.minecraft.currentScreen.passEvents)) {
            // Update gun input
            if (gun != null) {
              while (TOGGLE_FIRE_MODE.isPressed()) {
                gun.toggleFireMode(player, true);
              }
              while (RELOAD.isPressed()) {
                gun.reload(player);
              }
              while (REMOVE_MAGAZINE.isPressed()) {
                gun.removeMagazine(player);
              }
            }

            // Update crouching
            if (this.minecraft.player.isSneaking() != this.wasSneaking) {
              if (this.minecraft.player.isSneaking()) {
                final long currentTime = Util.milliTime();
                if (currentTime - this.lastSneakPressTime <= DOUBLE_CLICK_DURATION) {
                  player.setCrouching(true, true);
                }
                this.lastSneakPressTime = Util.milliTime();
              } else {
                player.setCrouching(false, true);
              }
              this.wasSneaking = this.minecraft.player.isSneaking();
            }

            // Update tutorial
            while (OPEN_MOD_INVENTORY.isPressed()) {
              NetworkChannel.PLAY.getSimpleChannel().sendToServer(new OpenModInventoryMessage());
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

            // Update adrenaline effects
            if (this.minecraft.player.isPotionActive(ModEffects.ADRENALINE.get())) {
              this.wasAdrenalineActive = true;
              this.effectsManager.setHighpassLevels(1.0F, 0.015F);
              this.effectsManager.setDirectHighpassForAll();
            } else if (this.wasAdrenalineActive) {
              this.wasAdrenalineActive = false;
              this.effectsManager.removeFilterForAll();
            }
          }
        }
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleRawMouse(InputEvent.RawMouseEvent event) {
    IPlayer<ClientPlayerEntity> player = this.getPlayer().orElse(null);
    if (player != null && this.minecraft.loadingGui == null
        && this.minecraft.currentScreen == null) {
      ItemStack heldStack = player.getEntity().getHeldItemMainhand();
      IGun gun = heldStack.getCapability(ModCapabilities.GUN).orElse(null);
      if (this.minecraft.gameSettings.keyBindAttack.matchesMouseKey(event.getButton())) {
        boolean triggerPressed = event.getAction() == GLFW.GLFW_PRESS;
        if (gun != null) {
          event.setCanceled(true);
          gun.setTriggerPressed(player, heldStack, triggerPressed, true);
        }
      } else if (this.minecraft.gameSettings.keyBindUseItem.matchesMouseKey(event.getButton())) {
        if (gun != null
            && gun.getRightMouseActionTriggerType() == IGun.RightMouseActionTriggerType.HOLD) {
          if ((event.getAction() == GLFW.GLFW_PRESS && !gun.isPerformingRightMouseAction())
              || (event.getAction() == GLFW.GLFW_RELEASE && gun.isPerformingRightMouseAction())) {
            gun.toggleRightMouseAction(IPlayer.getExpected(this.minecraft.player), true);
          }
          event.setCanceled(true);
        }
      }
    }
  }

  @SubscribeEvent
  public void handleAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof AbstractClientPlayerEntity) {
      event.addCapability(ILiving.ID,
          new SerializableCapabilityProvider<>(
              new Player<>((AbstractClientPlayerEntity) event.getObject()),
              () -> ModCapabilities.LIVING));
    }
  }

  @SubscribeEvent
  public void handleRenderLiving(RenderLivingEvent.Pre<?, BipedModel<?>> event) {
    ItemStack heldStack = event.getEntity().getHeldItemMainhand();
    // TODO Unpleasant way of setting pose for gun. Introduce nicer system (with better poses).
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
    final IPlayer<ClientPlayerEntity> player = this.getPlayer().orElse(null);
    if (player == null) {
      return;
    }
    ItemStack heldStack = player.getEntity().getHeldItemMainhand();
    IGun gun = heldStack.getCapability(ModCapabilities.GUN).orElse(null);
    switch (event.getType()) {
      case ALL:
        if (player != null) {
          this.ingameGui.renderOverlay(player, heldStack, gun, event.getMatrixStack(),
              event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight(),
              event.getPartialTicks());
        }
        break;
      case CROSSHAIRS:
        if (player != null) {
          boolean isAiming = heldStack.getCapability(ModCapabilities.SCOPE)
              .map(scope -> scope.isAiming(this.minecraft.player, heldStack))
              .orElse(false);
          if (player.isPerformingAction() || isAiming) {
            event.setCanceled(true);
            break;
          }

          if (gun != null) {
            event.setCanceled(true);
            if (gun.hasCrosshair()) {
              this.ingameGui.renderCrosshairs(gun.getAccuracy(player, heldStack),
                  event.getPartialTicks(), event.getWindow().getScaledWidth(),
                  event.getWindow().getScaledHeight());
            }
          }
        }
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleCameraSetup(EntityViewRenderEvent.CameraSetup event) {
    final Vector3f rotations = this.cameraManager.getCameraRotations();
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
    if (this.minecraft.player != null) {
      ItemStack heldStack = this.minecraft.player.getHeldItemMainhand();
      float newFov = heldStack.getCapability(ModCapabilities.SCOPE)
          .filter(scope -> scope.isAiming(this.minecraft.player, heldStack))
          .map(scope -> 1 / scope.getZoomMultiplier(this.minecraft.player, heldStack)).orElse(1.0F);

      this.lastFov = this.fov;
      this.fov = MathHelper.lerp(0.25F, this.fov, newFov);

      event.setFOV(event.getFOV()
          * MathHelper.lerp(this.minecraft.getRenderPartialTicks(), this.lastFov, this.fov));
    }
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
          final Vector2f rotationVelocity = this.cameraManager.getLookRotationDelta();
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
      float progress = MathHelper.clamp(
          ((currentTime - this.adrenalineShaderStartTime) - partialTicks) / 5000.0F, 0.0F, 1.0F);
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
  public void handleGuiOpen(GuiOpenEvent event) {
    // Prevents current GUI being closed before new one opens.
    if (this.minecraft.currentScreen instanceof PlayerScreen && event.getGui() == null
        && ((PlayerScreen) this.minecraft.currentScreen).isTransitioning()) {
      event.setCanceled(true);
    }
  }

  // ================================================================================
  // Client-only helper methods
  // ================================================================================

  public void checkApplyFlashEffects(FlashGrenadeEntity flashGrenadeEntity) {
    // Applies the flash effect at client side for a better delay compensation
    // and better FOV calculation
    int duration = flashGrenadeEntity.calculateDuration(this.minecraft.player,
        RenderUtil.isInsideGameFOV(flashGrenadeEntity, false));
    if (duration > 0) {
      EffectInstance flashEffect = new EffectInstance(ModEffects.FLASH_BLINDNESS.get(), duration);
      ModEffects.applyOrOverrideIfLonger(this.minecraft.player, flashEffect);
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
