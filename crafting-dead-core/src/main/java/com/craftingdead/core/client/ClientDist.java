/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.ModDist;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.client.crosshair.CrosshairManager;
import com.craftingdead.core.client.gui.IngameGui;
import com.craftingdead.core.client.gui.screen.inventory.EquipmentScreen;
import com.craftingdead.core.client.gui.screen.inventory.GenericContainerScreen;
import com.craftingdead.core.client.particle.GrenadeSmokeParticle;
import com.craftingdead.core.client.particle.RGBFlashParticle;
import com.craftingdead.core.client.renderer.CameraManager;
import com.craftingdead.core.client.renderer.entity.grenade.C4ExplosiveRenderer;
import com.craftingdead.core.client.renderer.entity.grenade.CylinderGrenadeRenderer;
import com.craftingdead.core.client.renderer.entity.grenade.FragGrenadeRenderer;
import com.craftingdead.core.client.renderer.entity.grenade.SlimGrenadeRenderer;
import com.craftingdead.core.client.renderer.entity.layers.ClothingLayer;
import com.craftingdead.core.client.renderer.entity.layers.EquipmentLayer;
import com.craftingdead.core.client.renderer.entity.layers.ParachuteLayer;
import com.craftingdead.core.client.renderer.item.GunRenderer;
import com.craftingdead.core.client.renderer.item.ItemRendererManager;
import com.craftingdead.core.client.sounds.EffectsManager;
import com.craftingdead.core.client.tutorial.ModTutorialStepInstance;
import com.craftingdead.core.client.tutorial.ModTutorialSteps;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.core.event.RenderArmClothingEvent;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.OpenModInventoryMessage;
import com.craftingdead.core.particle.ModParticleTypes;
import com.craftingdead.core.util.MutableVector2f;
import com.craftingdead.core.world.effect.ModMobEffects;
import com.craftingdead.core.world.entity.ModEntityTypes;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.entity.grenade.FlashGrenadeEntity;
import com.craftingdead.core.world.gun.Gun;
import com.craftingdead.core.world.inventory.ModEquipmentSlotType;
import com.craftingdead.core.world.inventory.ModMenuTypes;
import com.craftingdead.core.world.item.ArbitraryTooltips;
import com.craftingdead.core.world.item.ArbitraryTooltips.TooltipFunction;
import com.craftingdead.core.world.item.GunItem;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.core.world.item.RegisterGunColour;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.GameRenderer;
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
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientDist implements ModDist {

  public static final KeyBinding RELOAD =
      new KeyBinding("key.reload", GLFW.GLFW_KEY_R, "key.categories.gameplay");
  public static final KeyBinding REMOVE_MAGAZINE =
      new KeyBinding("key.remove_magazine", GLFW.GLFW_KEY_J, "key.categories.gameplay");
  public static final KeyBinding TOGGLE_FIRE_MODE =
      new KeyBinding("key.toggle_fire_mode", GLFW.GLFW_KEY_V, "key.categories.gameplay");
  public static final KeyBinding OPEN_EQUIPMENT_MENU =
      new KeyBinding("key.equipment_menu", GLFW.GLFW_KEY_Z, "key.categories.inventory");
  public static final KeyBinding TOGGLE_COMBAT_MODE =
      new KeyBinding("key.toggle_combat_mode", KeyConflictContext.UNIVERSAL, KeyModifier.ALT,
          InputMappings.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_C), "key.categories.inventory");

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

  private static final Vector3f CAMERA_ROTATIONS = new Vector3f();
  private static final MutableVector2f FOV = new MutableVector2f();

  private static final int DOUBLE_CLICK_DURATION = 500;

  private final Minecraft minecraft;

  private final CrosshairManager crosshairManager;

  private final IngameGui ingameGui;

  private final ItemRendererManager itemRendererManager;

  private final CameraManager cameraManager;

  private EffectsManager effectsManager;

  private TutorialSteps lastTutorialStep;

  private long adrenalineShaderStartTime = 0L;

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
    modBus.addListener(this::handleConfigReloading);

    MinecraftForge.EVENT_BUS.register(this);
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientConfigSpec);

    this.minecraft = Minecraft.getInstance();
    this.crosshairManager = new CrosshairManager();
    // Minecraft is null on date gen launch
    if (this.minecraft != null) {
      ((IReloadableResourceManager) this.minecraft.getResourceManager())
          .registerReloadListener(this.crosshairManager);
    }
    this.ingameGui =
        new IngameGui(this.minecraft, this, new ResourceLocation(clientConfig.crosshair.get()));
    this.itemRendererManager = new ItemRendererManager();
    this.cameraManager = new CameraManager();
  }

  public void setTutorialStep(ModTutorialSteps step) {
    clientConfig.tutorialStep.set(step);
    Tutorial tutorial = this.minecraft.getTutorial();
    tutorial.setStep(TutorialSteps.NONE);
    tutorial.instance = step.create(this);
  }

  public CrosshairManager getCrosshairManager() {
    return this.crosshairManager;
  }

  public IngameGui getIngameGui() {
    return this.ingameGui;
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
  public Optional<PlayerExtension<ClientPlayerEntity>> getPlayer() {
    return Optional.ofNullable(this.minecraft.player)
        .flatMap(p -> p.getCapability(Capabilities.LIVING).resolve())
        .filter(living -> living instanceof PlayerExtension)
        .map(living -> (PlayerExtension<ClientPlayerEntity>) living);
  }

  @SuppressWarnings("unchecked")
  public PlayerExtension<ClientPlayerEntity> getExpectedPlayer() {
    return Capabilities.getOrThrow(Capabilities.LIVING, this.minecraft.player,
        PlayerExtension.class);
  }

  public boolean isRightMouseDown() {
    return this.minecraft.options.keyUse.isDown();
  }

  public boolean isLocalPlayer(Entity entity) {
    return entity == this.minecraft.player;
  }

  public void handleHit(Vector3d hitPos, boolean dead) {
    clientConfig.hitMarkerMode.get().createHitMarker(hitPos, dead)
        .ifPresent(this.ingameGui::displayHitMarker);
    if (dead && ClientDist.clientConfig.killSoundEnabled.get()) {
      // Plays a sound that follows the player
      SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS
          .getValue(new ResourceLocation(ClientDist.clientConfig.killSound.get()));
      if (soundEvent != null) {
        Minecraft.getInstance().getSoundManager().play(
            SimpleSound.forUI(soundEvent, 5.0F, 1.5F));
      }
    }
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

  private void handleConfigReloading(ModConfig.Reloading event) {
    if (event.getConfig().getSpec() == clientConfigSpec) {
      this.ingameGui.setCrosshairLocation(new ResourceLocation(clientConfig.crosshair.get()));
    }
  }

  private void handleModelRegistry(ModelRegistryEvent event) {
    StartupMessageManager.addModMessage("Gathering item renderers");
    this.itemRendererManager.gatherItemRenderers();
    StartupMessageManager.addModMessage("Registering special models");
    this.itemRendererManager.getModelDependencies().forEach(ModelLoader::addSpecialModel);
  }

  private void handleModelBake(ModelBakeEvent event) {
    this.itemRendererManager.refreshCachedModels();
  }

  private void handleClientSetup(FMLClientSetupEvent event) {
    ItemModelsProperties.registerGeneric(new ResourceLocation("wearing"),
        (itemStack, world, entity) -> entity.getCapability(Capabilities.LIVING)
            .filter(living -> living.getItemHandler()
                .getStackInSlot(ModEquipmentSlotType.HAT.getIndex()) == itemStack)
            .map(__ -> 1.0F)
            .orElse(0.0F));

    StartupMessageManager.addModMessage("Registering tooltips");

    ArbitraryTooltips.registerTooltip(ModItems.SCUBA_MASK,
        (stack, world,
            tooltipFlags) -> new TranslationTextComponent("item_lore.clothing_item.water_breathing")
                .withStyle(TextFormatting.GRAY));

    ArbitraryTooltips.registerTooltip(ModItems.SCUBA_CLOTHING,
        (stack, world,
            tooltipFlags) -> new TranslationTextComponent("item_lore.clothing_item.water_speed")
                .withStyle(TextFormatting.GRAY));

    ScreenManager.register(ModMenuTypes.EQUIPMENT.get(), EquipmentScreen::new);
    ScreenManager.register(ModMenuTypes.VEST.get(), GenericContainerScreen::new);

    ClientRegistry.registerKeyBinding(TOGGLE_FIRE_MODE);
    ClientRegistry.registerKeyBinding(RELOAD);
    ClientRegistry.registerKeyBinding(REMOVE_MAGAZINE);
    ClientRegistry.registerKeyBinding(OPEN_EQUIPMENT_MENU);
    ClientRegistry.registerKeyBinding(TOGGLE_COMBAT_MODE);

    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.C4_EXPLOSIVE.get(),
        C4ExplosiveRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.FIRE_GRENADE.get(),
        CylinderGrenadeRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.FRAG_GRENADE.get(),
        FragGrenadeRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.DECOY_GRENADE.get(),
        SlimGrenadeRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SMOKE_GRENADE.get(),
        CylinderGrenadeRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.FLASH_GRENADE.get(),
        SlimGrenadeRenderer::new);

    this.registerPlayerLayer(ParachuteLayer::new);
    this.registerPlayerLayer(ClothingLayer::new);
    this.registerPlayerLayer(
        renderer -> new EquipmentLayer.Builder<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>()
            .withRenderer(renderer)
            .withSlot(ModEquipmentSlotType.MELEE)
            .withCrouchingOrientation(true)
            .build());
    this.registerPlayerLayer(
        renderer -> new EquipmentLayer.Builder<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>()
            .withRenderer(renderer)
            .withSlot(ModEquipmentSlotType.VEST)
            .withCrouchingOrientation(true)
            .build());
    this.registerPlayerLayer(
        renderer -> new EquipmentLayer.Builder<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>()
            .withRenderer(renderer)
            .withSlot(ModEquipmentSlotType.HAT)
            .withHeadOrientation(true)
            // Inverts X and Y rotation. This is from Mojang, based on HeadLayer.class.
            // TODO Find a reason to not remove this line. Also, if you remove it, you will
            // need to change the json file of every helmet since the scale affects positions.
            .withArbitraryTransformation(matrix -> matrix.scale(-1F, -1F, 1F))
            .build());
    this.registerPlayerLayer(
        renderer -> new EquipmentLayer.Builder<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>()
            .withRenderer(renderer)
            .withSlot(ModEquipmentSlotType.GUN)
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
    this.minecraft.getEntityRenderDispatcher().getSkinMap().forEach((skin, renderer) -> {
      renderer.addLayer(function.apply(renderer));
    });
  }

  private void handleParticleFactoryRegisterEvent(ParticleFactoryRegisterEvent event) {
    ParticleManager particleEngine = this.minecraft.particleEngine;
    particleEngine.register(ModParticleTypes.GRENADE_SMOKE.get(),
        GrenadeSmokeParticle.Factory::new);
    particleEngine.register(ModParticleTypes.RGB_FLASH.get(), RGBFlashParticle.Factory::new);
  }

  private void handleItemColor(ColorHandlerEvent.Item event) {
    IItemColor gunColour = (itemStack, tintIndex) -> GunRenderer.getColour(itemStack);
    ForgeRegistries.ITEMS.getValues().stream()
        .filter(item -> item.getClass().isAnnotationPresent(RegisterGunColour.class))
        .forEach(item -> event.getItemColors().register(gunColour, item));
  }

  private void handleTextureStitch(TextureStitchEvent.Pre event) {
    if (event.getMap().location().equals(PlayerContainer.BLOCK_ATLAS)) {
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
      World world = event.getEntity() != null ? event.getEntity().level : null;
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
        PlayerExtension<ClientPlayerEntity> player = this.getPlayer().orElse(null);
        if (player != null) {
          ItemStack heldStack = player.getEntity().getMainHandItem();
          Gun gun = heldStack.getCapability(Capabilities.GUN).orElse(null);

          boolean worldFocused = !this.minecraft.isPaused() && this.minecraft.overlay == null
              && (this.minecraft.screen == null);

          this.cameraManager.tick();

          if (!worldFocused || player.getEntity().isSpectator()) {
            // Stop gun actions if world not focused.
            if (gun != null) {
              if (gun.isTriggerPressed()) {
                gun.setTriggerPressed(player, false, true);
              }
              if (gun.isPerformingSecondaryAction()) {
                gun.setPerformingSecondaryAction(player, false, true);
              }
            }
            return;
          }

          while (TOGGLE_COMBAT_MODE.consumeClick()) {
            player.setCombatModeEnabled(!player.isCombatModeEnabled());
          }

          // Update gun input
          if (gun != null) {
            while (TOGGLE_FIRE_MODE.consumeClick()) {
              gun.toggleFireMode(player, true);
            }
            while (RELOAD.consumeClick()) {
              gun.getAmmoProvider().reload(player);
            }
            while (REMOVE_MAGAZINE.consumeClick()) {
              gun.getAmmoProvider().unload(player);
            }
          }

          // Update crouching
          if (this.minecraft.player.isShiftKeyDown() != this.wasSneaking) {
            if (this.minecraft.player.isShiftKeyDown()) {
              final long currentTime = Util.getMillis();
              if (currentTime - this.lastSneakPressTime <= DOUBLE_CLICK_DURATION) {
                player.setCrouching(true, true);
              }
              this.lastSneakPressTime = Util.getMillis();
            } else {
              player.setCrouching(false, true);
            }
            this.wasSneaking = this.minecraft.player.isShiftKeyDown();
          }

          // Update tutorial
          while (OPEN_EQUIPMENT_MENU.consumeClick()) {
            NetworkChannel.PLAY.getSimpleChannel().sendToServer(new OpenModInventoryMessage());
            if (this.minecraft.getTutorial().instance instanceof ModTutorialStepInstance) {
              ((ModTutorialStepInstance) this.minecraft.getTutorial().instance).openEquipmentMenu();
            }
          }
          TutorialSteps currentTutorialStep = this.minecraft.options.tutorialStep;
          if (this.lastTutorialStep != currentTutorialStep) {
            if (currentTutorialStep == TutorialSteps.NONE) {
              this.setTutorialStep(clientConfig.tutorialStep.get());
            }
            this.lastTutorialStep = currentTutorialStep;
          }

          // Update adrenaline effects
          if (this.minecraft.player.hasEffect(ModMobEffects.ADRENALINE.get())) {
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
    PlayerExtension<ClientPlayerEntity> player = this.getPlayer().orElse(null);
    if (player != null && this.minecraft.overlay == null
        && this.minecraft.screen == null && !player.getEntity().isSpectator()) {
      ItemStack heldStack = player.getEntity().getMainHandItem();
      Gun gun = heldStack.getCapability(Capabilities.GUN).orElse(null);
      if (this.minecraft.options.keyAttack.matchesMouse(event.getButton())) {
        boolean triggerPressed = event.getAction() == GLFW.GLFW_PRESS;
        if (gun != null) {
          event.setCanceled(true);
          gun.setTriggerPressed(player, triggerPressed, true);
        }
      } else if (this.minecraft.options.keyUse.matchesMouse(event.getButton())) {
        if (gun != null) {
          switch (gun.getSecondaryActionTrigger()) {
            case HOLD:
              gun.setPerformingSecondaryAction(player, event.getAction() == GLFW.GLFW_PRESS, true);
              break;
            case TOGGLE:
              if (event.getAction() == GLFW.GLFW_PRESS) {
                gun.setPerformingSecondaryAction(player, !gun.isPerformingSecondaryAction(), true);
              }
              break;
            default:
              break;
          }
          event.setCanceled(true);
        }
      }
    }
  }

  @SubscribeEvent
  public void handleRenderLiving(RenderLivingEvent.Pre<?, BipedModel<?>> event) {
    ItemStack heldStack = event.getEntity().getMainHandItem();
    // TODO Unpleasant way of setting pose for gun. Introduce nicer system (with better poses).
    if (event.getRenderer().getModel() instanceof BipedModel
        && heldStack.getItem() instanceof GunItem) {
      BipedModel<?> model = event.getRenderer().getModel();
      switch (event.getEntity().getMainArm()) {
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
    PlayerExtension<AbstractClientPlayerEntity> player =
        this.minecraft.getCameraEntity() instanceof AbstractClientPlayerEntity
            ? this.minecraft.getCameraEntity().getCapability(Capabilities.LIVING)
                .<PlayerExtension<AbstractClientPlayerEntity>>cast()
                .orElse(null)
            : null;
    if (player == null) {
      return;
    }
    ItemStack heldStack = player.getEntity().getMainHandItem();
    Gun gun = heldStack.getCapability(Capabilities.GUN).orElse(null);
    switch (event.getType()) {
      case HEALTH:
      case HOTBAR:
      case EXPERIENCE:
      case HEALTHMOUNT:
      case FOOD:
      case AIR:
      case ARMOR:
        event.setCanceled(player.isCombatModeEnabled());
        break;
      case ALL:
        this.ingameGui.renderOverlay(player, heldStack, gun, event.getMatrixStack(),
            event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(),
            event.getPartialTicks());
        break;
      case CROSSHAIRS:
        boolean isAiming = heldStack.getCapability(Capabilities.SCOPE)
            .map(scope -> scope.isAiming(player.getEntity()))
            .orElse(false);
        if (player.isMonitoringAction() || isAiming) {
          event.setCanceled(true);
          break;
        }

        if (gun != null) {
          event.setCanceled(true);
          if (gun.getClient().hasCrosshair()) {
            this.ingameGui.renderCrosshairs(gun.getAccuracy(player),
                event.getPartialTicks(), event.getWindow().getGuiScaledWidth(),
                event.getWindow().getGuiScaledHeight());
          }
        }
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleCameraSetup(EntityViewRenderEvent.CameraSetup event) {
    this.cameraManager.getCameraRotations((float) event.getRenderPartialTicks(), CAMERA_ROTATIONS);
    event.setPitch(event.getPitch() + CAMERA_ROTATIONS.x());
    event.setYaw(event.getYaw() + CAMERA_ROTATIONS.y());
    event.setRoll(event.getRoll() + CAMERA_ROTATIONS.z());
  }

  @SubscribeEvent
  public void handeFOVUpdate(FOVUpdateEvent event) {
    event.setNewfov(event.getNewfov()
        + this.cameraManager.getFov(Minecraft.getInstance().getFrameTime()));
  }

  @SubscribeEvent
  public void handeFOVUpdate(EntityViewRenderEvent.FOVModifier event) {
    if (this.minecraft.getCameraEntity() instanceof LivingEntity) {
      LivingEntity livingEntity = (LivingEntity) this.minecraft.getCameraEntity();
      ItemStack heldStack = livingEntity.getMainHandItem();
      float newFov = heldStack.getCapability(Capabilities.SCOPE)
          .filter(scope -> scope.isAiming(livingEntity))
          .map(scope -> 1 / scope.getZoomMultiplier(livingEntity)).orElse(1.0F);

      this.lastFov = this.fov;
      this.fov = MathHelper.lerp(0.25F, this.fov, newFov);

      event.setFOV(event.getFOV()
          * MathHelper.lerp(this.minecraft.getFrameTime(), this.lastFov, this.fov));
    }
  }

  @SubscribeEvent
  public void handleRenderTick(TickEvent.RenderTickEvent event) {
    switch (event.phase) {
      case START:
        if (this.minecraft.player != null) {
          this.cameraManager.getLookRotationDelta(FOV);
          this.minecraft.player.turn(FOV.getY(), FOV.getX());
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
    final boolean shaderLoaded = gameRenderer.currentEffect() != null
        && gameRenderer.currentEffect().getName()
            .equals(ADRENALINE_SHADER.toString());
    if (this.minecraft.player.hasEffect(ModMobEffects.ADRENALINE.get())) {
      final long currentTime = Util.getMillis();
      if (this.adrenalineShaderStartTime == 0L) {
        this.adrenalineShaderStartTime = currentTime;
      }
      float progress = MathHelper.clamp(
          ((currentTime - this.adrenalineShaderStartTime) - partialTicks) / 5000.0F, 0.0F, 1.0F);
      if (!shaderLoaded) {
        if (gameRenderer.currentEffect() != null) {
          gameRenderer.shutdownEffect();
        }
        gameRenderer.loadEffect(ADRENALINE_SHADER);
      }
      ShaderGroup shaderGroup = gameRenderer.currentEffect();
      RenderUtil.updateUniform("Saturation", progress * 0.25F, shaderGroup);
    } else if (shaderLoaded) {
      this.adrenalineShaderStartTime = 0L;
      gameRenderer.shutdownEffect();
    }
  }

  @SubscribeEvent
  public void handleGuiOpen(GuiOpenEvent event) {
    // Prevents current GUI being closed before new one opens.
    if (this.minecraft.screen instanceof EquipmentScreen && event.getGui() == null
        && ((EquipmentScreen) this.minecraft.screen).isTransitioning()) {
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
        RenderUtil.isInsideFrustum(flashGrenadeEntity, false));
    if (duration > 0) {
      EffectInstance flashEffect =
          new EffectInstance(ModMobEffects.FLASH_BLINDNESS.get(), duration);
      ModMobEffects.applyOrOverrideIfLonger(this.minecraft.player, flashEffect);
    }
  }

  // ================================================================================
  // Hooks
  // ================================================================================

  /**
   * @see com.craftingdead.core.mixin.PlayerRendererMixin
   */
  public static void renderArmWithClothing(PlayerRenderer renderer, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer,
      int packedLight, AbstractClientPlayerEntity playerEntity, ModelRenderer armRenderer,
      ModelRenderer armwearRenderer) {

    ResourceLocation clothingTexture = playerEntity.getCapability(Capabilities.LIVING)
        .map(LivingExtension::getItemHandler)
        .map(itemHandler -> itemHandler.getStackInSlot(ModEquipmentSlotType.CLOTHING.getIndex()))
        .flatMap(clothingStack -> clothingStack.getCapability(Capabilities.CLOTHING).resolve())
        .map(clothing -> clothing.getTexture(playerEntity.getModelName()))
        .orElse(null);

    RenderArmClothingEvent event = new RenderArmClothingEvent(playerEntity, clothingTexture);
    MinecraftForge.EVENT_BUS.post(event);
    clothingTexture = event.getClothingTexture();

    if (clothingTexture != null) {
      PlayerModel<AbstractClientPlayerEntity> playerModel = renderer.getModel();
      playerModel.attackTime = 0.0F;
      playerModel.crouching = false;
      playerModel.swimAmount = 0.0F;
      playerModel.setupAnim(playerEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

      armRenderer.visible = true;
      armwearRenderer.visible = true;

      armRenderer.xRot = 0.0F;
      armRenderer.render(matrixStack,
          renderTypeBuffer.getBuffer(RenderType.entityTranslucent(clothingTexture)), packedLight,
          OverlayTexture.NO_OVERLAY);
      armwearRenderer.xRot = 0.0F;
      armwearRenderer.render(matrixStack,
          renderTypeBuffer.getBuffer(RenderType.entityTranslucent(clothingTexture)), packedLight,
          OverlayTexture.NO_OVERLAY);
    }
  }
}
