package com.craftingdead.mod.client;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.mod.CommonConfig;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.IModDist;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.paint.IPaint;
import com.craftingdead.mod.capability.player.DefaultPlayer;
import com.craftingdead.mod.capability.player.IPlayer;
import com.craftingdead.mod.capability.player.SelfPlayer;
import com.craftingdead.mod.client.crosshair.CrosshairManager;
import com.craftingdead.mod.client.gui.IngameGui;
import com.craftingdead.mod.client.gui.screen.inventory.ModInventoryScreen;
import com.craftingdead.mod.client.model.GunModel;
import com.craftingdead.mod.client.model.PerspectiveAwareModel;
import com.craftingdead.mod.client.renderer.entity.AdvancedZombieRenderer;
import com.craftingdead.mod.client.renderer.entity.CorpseRenderer;
import com.craftingdead.mod.client.renderer.entity.GrenadeRenderer;
import com.craftingdead.mod.client.renderer.entity.SupplyDropRenderer;
import com.craftingdead.mod.client.renderer.entity.layer.ClothingLayer;
import com.craftingdead.mod.client.renderer.entity.layer.EquipmentLayer;
import com.craftingdead.mod.client.tutorial.IModTutorialStep;
import com.craftingdead.mod.client.tutorial.ModTutorialSteps;
import com.craftingdead.mod.entity.ModEntityTypes;
import com.craftingdead.mod.inventory.InventorySlotType;
import com.craftingdead.mod.inventory.container.ModContainerTypes;
import com.craftingdead.mod.item.AttachmentItem;
import com.craftingdead.mod.item.ClothingItem;
import com.craftingdead.mod.item.GunItem;
import com.craftingdead.mod.item.ModItems;
import com.craftingdead.mod.item.PaintItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.ScreenManager;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientDist implements IModDist {

  public static final KeyBinding RELOAD =
      new KeyBinding("key.reload", GLFW.GLFW_KEY_R, "key.categories.gameplay");
  public static final KeyBinding TOGGLE_FIRE_MODE =
      new KeyBinding("key.toggle_fire_mode", GLFW.GLFW_KEY_V, "key.categories.gameplay");
  public static final KeyBinding OPEN_MOD_INVENTORY =
      new KeyBinding("key.craftingdead.inventory", GLFW.GLFW_KEY_Z, "key.categories.inventory");

  private static final Minecraft minecraft = Minecraft.getInstance();

  /**
   * Random.
   */
  private static final Random random = new Random();

  private final CrosshairManager crosshairManager = new CrosshairManager();

  /**
   * Current camera velocity.
   */
  private Vec2f rotationVelocity = Vec2f.ZERO;

  /**
   * Camera velocity of last tick.
   */
  private Vec2f prevRotationVelocity = this.rotationVelocity;

  private long rollStartTime = 0;

  private float roll;

  private IngameGui ingameGui;

  private TutorialSteps lastTutorialStep;

  public ClientDist() {
    FMLJavaModLoadingContext.get().getModEventBus().register(this);
    MinecraftForge.EVENT_BUS.register(this);

    ((IReloadableResourceManager) minecraft.getResourceManager())
        .addReloadListener(this.crosshairManager);

    this.ingameGui = new IngameGui(minecraft, this, CrosshairManager.DEFAULT_CROSSHAIR);
  }

  public void joltCamera(float accuracy) {
    float amount = ((1.0F - accuracy) * 100) / 2.5F;
    float randomAmount = random.nextBoolean() ? amount : -amount;
    this.rotationVelocity =
        new Vec2f(this.rotationVelocity.x + randomAmount, this.rotationVelocity.y - amount);
    this.rollStartTime = Util.milliTime();
    this.roll = randomAmount;
  }

  public void setTutorialStep(ModTutorialSteps step) {
    CommonConfig.clientConfig.tutorialStep.set(step);
    Tutorial tutorial = minecraft.getTutorial();
    tutorial.setStep(TutorialSteps.NONE);
    tutorial.tutorialStep = step.create(this);
  }

  public CrosshairManager getCrosshairManager() {
    return this.crosshairManager;
  }

  public LazyOptional<SelfPlayer> getPlayer() {
    return minecraft.player != null
        ? minecraft.player
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
        .registerEntityRenderingHandler(ModEntityTypes.supplyDrop, SupplyDropRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.grenade, GrenadeRenderer::new);

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
    minecraft.getRenderManager().getSkinMap().forEach((skin, renderer) -> {
      renderer.addLayer(function.apply(renderer));
    });
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleClientTick(TickEvent.ClientTickEvent event) {
    switch (event.phase) {
      case START:
        if (minecraft.loadingGui == null
            && (minecraft.currentScreen == null || minecraft.currentScreen.passEvents)) {
          while (TOGGLE_FIRE_MODE.isPressed()) {
            ItemStack heldStack = minecraft.player.getHeldItemMainhand();
            heldStack
                .getCapability(ModCapabilities.GUN)
                .ifPresent(gun -> gun.toggleFireMode(minecraft.player, true));
          }
          while (OPEN_MOD_INVENTORY.isPressed()) {
            minecraft.player
                .getCapability(ModCapabilities.LIVING)
                .filter(living -> living instanceof IPlayer)
                .<IPlayer<?>>cast()
                .ifPresent(IPlayer::openPlayerContainer);
            if (minecraft.getTutorial().tutorialStep instanceof IModTutorialStep) {
              ((IModTutorialStep) minecraft.getTutorial().tutorialStep).openModInventory();
            }
          }

          TutorialSteps currentTutorialStep = minecraft.gameSettings.tutorialStep;
          if (this.lastTutorialStep != currentTutorialStep) {
            if (currentTutorialStep == TutorialSteps.NONE) {
              this.setTutorialStep(CommonConfig.clientConfig.tutorialStep.get());
            }
            this.lastTutorialStep = currentTutorialStep;
          }
        }
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleRawMouse(InputEvent.RawMouseEvent event) {
    if (minecraft.getConnection() != null && minecraft.currentScreen == null) {
      if (minecraft.gameSettings.keyBindAttack.matchesMouseKey(event.getButton())) {
        boolean triggerPressed = event.getAction() == GLFW.GLFW_PRESS;
        ItemStack heldStack = minecraft.player.getHeldItemMainhand();
        heldStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> {
          event.setCanceled(true);
          gun.setTriggerPressed(minecraft.player, heldStack, triggerPressed, true);
        });
      }
    }
  }

  @SubscribeEvent
  public void handleAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof ClientPlayerEntity) {
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "living"),
              new SerializableProvider<>(new SelfPlayer((ClientPlayerEntity) event.getObject()),
                  () -> ModCapabilities.LIVING));
    } else if (event.getObject() instanceof AbstractClientPlayerEntity) {
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "living"),
              new SerializableProvider<>(
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
                  .orElse(false) || player.isAiming());

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
    float pct = MathHelper.clamp((Util.milliTime() - this.rollStartTime) / 1000.0F * 5, 0.0F, 1.0F);
    float roll = (float) Math.sin(Math.toRadians(180 * pct)) * this.roll / 20;
    if (pct == 1.0F) {
      this.roll = 0;
    }
    event.setRoll(roll);
  }

  @SubscribeEvent
  public void handeFOVUpdate(FOVUpdateEvent event) {
    ItemStack heldStack = minecraft.player.getHeldItemMainhand();
    if (this.getPlayer().map(IPlayer::isAiming).orElse(false)) {
      heldStack.getCapability(ModCapabilities.GUN).ifPresent(gunController -> {
        event
            .setNewfov(event.getFov()
                * gunController.getAttachmentMultiplier(AttachmentItem.MultiplierType.FOV));
      });
    }
  }

  @SubscribeEvent
  public void handleRenderTick(TickEvent.RenderTickEvent event) {
    switch (event.phase) {
      case START:
        if (minecraft.player != null) {
          float smoothYaw =
              MathHelper.lerp(0.25F, this.prevRotationVelocity.x, this.rotationVelocity.x);
          float smoothPitch =
              MathHelper.lerp(0.25F, this.prevRotationVelocity.y, this.rotationVelocity.y);
          this.rotationVelocity = Vec2f.ZERO;
          this.prevRotationVelocity = new Vec2f(smoothYaw, smoothPitch);
          minecraft.player.rotateTowards(smoothYaw, smoothPitch);
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

  // ================================================================================
  // ASM Hooks
  // ================================================================================

  public static void renderArmsWithExtraSkins(PlayerRenderer renderer, MatrixStack matrix,
      IRenderTypeBuffer buffer, int p_229144_3_, AbstractClientPlayerEntity playerEntity,
      ModelRenderer firstLayerModel, ModelRenderer secondLayerModel) {
    playerEntity.getCapability(ModCapabilities.LIVING).ifPresent(living -> {
      String skinType = playerEntity.getSkinType();
      ItemStack clothingStack =
          living.getInventory().getStackInSlot(InventorySlotType.CLOTHING.getIndex());
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
