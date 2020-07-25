package com.craftingdead.core.client.renderer.item;


import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.magazine.IMagazine;
import com.craftingdead.core.capability.paint.IPaint;
import com.craftingdead.core.capability.scope.IScope;
import com.craftingdead.core.client.renderer.item.gun.GunAnimation;
import com.craftingdead.core.client.renderer.item.model.ModelMuzzleFlash;
import com.craftingdead.core.item.AttachmentItem;
import com.craftingdead.core.item.ModItems;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public abstract class RenderGun implements IItemRenderer {

  private static final Random random = new Random();

  protected Minecraft mc = Minecraft.getInstance();

  private final Queue<Pair<GunAnimation, Runnable>> animations = new LinkedList<>();

  private long animationStartTime = 0L;

  protected Model gunModel = this.getGunModel();
  protected Model muzzleFlashModel = new ModelMuzzleFlash();

  protected float muzzleFlashX = 0.4F;
  protected float muzzleFlashY = 0.2F;
  protected float muzzleFlashZ = -1.8F;
  protected float muzzleScale = 2F;

  protected boolean flash;

  public void flash() {
    this.flash = true;
  }

  public void addAnimation(GunAnimation animation, Runnable callback) {
    this.animations.add(Pair.of(animation, callback));
  }

  public void removeCurrentAnimation() {
    this.animations.poll();
    this.animationStartTime = 0L;
  }

  public void clearAnimations() {
    this.animations.clear();
  }

  @Override
  public void tick(ItemStack itemStack, LivingEntity livingEntity) {
    Pair<GunAnimation, Runnable> animation = this.animations.peek();
    if (animation != null) {
      if (this.animationStartTime == 0L) {
        this.animationStartTime = Util.milliTime();
      }
      float progress =
          MathHelper.clamp(
              (Util.milliTime() - this.animationStartTime) / animation.getLeft().getLength(),
              0.0F, 1.0F);
      animation.getLeft().onUpdate(this.mc, livingEntity, itemStack, progress);
      if (progress >= 1.0F) {
        if (animation.getRight() != null) {
          animation.getRight().run();
        }
        this.removeCurrentAnimation();
      }
    }
  }

  @Override
  public boolean handleRenderType(ItemStack item,
      ItemCameraTransforms.TransformType transformType) {
    switch (transformType) {
      case THIRD_PERSON_LEFT_HAND:
      case THIRD_PERSON_RIGHT_HAND:
      case FIRST_PERSON_LEFT_HAND:
      case FIRST_PERSON_RIGHT_HAND:
      case HEAD:
        return true;
      default:
        return false;
    }
  }

  /**
   * Set up transformations to simulate the render state in Minecraft 1.6.4.
   * 
   * @param matrixStack - {@link MatrixStack} to transform
   * @param thirdPerson - if we are in third person or not
   * @param leftHanded - if the player is left handed
   */
  private final void applyLegacyTransforms(MatrixStack matrixStack,
      ItemCameraTransforms.TransformType transformType,
      boolean leftHanded) {

    final boolean thirdPerson =
        transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND
            || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;

    // TODO Left hand support
    if (leftHanded && !thirdPerson) {
      matrixStack.translate(1.12F, 0, 0);
    }

    matrixStack.scale(0.5F, 0.5F, 0.5F);
    matrixStack.translate(-0.5, -0.5F, -0.5F);

    if (thirdPerson) {
      matrixStack.translate(-1F, 0, 0);
      matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(50));
      matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(70));
      matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-35));
    }

    if (transformType == ItemCameraTransforms.TransformType.HEAD) {
      matrixStack.translate(-1F, 2.5F, 3.75F);


      matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(270));
      matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(25F));


      matrixStack.scale(2F, 2F, 2F);
      matrixStack.translate(-2, -2F, -2F);
    }

    matrixStack.translate(0.3135F, 0.4765F, 0.8625F);

    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(0F));
    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(95));
    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(335.0F));
    matrixStack.translate(-0.9375F, -0.0625F, 0.0F);
  }


  private final void renderFirstPersonArms(AbstractClientPlayerEntity playerEntity,
      ItemStack itemStack, IGun gun, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      int packedLight, GunAnimation animation, float partialTicks) {
    final PlayerRenderer playerRenderer =
        (PlayerRenderer) this.mc.getRenderManager().getRenderer(playerEntity);

    // Right Hand
    matrixStack.push();
    {
      if (animation != null) {
        animation.doRenderHand(itemStack, partialTicks, true, matrixStack);
      }

      this.mc.getTextureManager().bindTexture(playerEntity.getLocationSkin());
      matrixStack.translate(1F, 1F, 0F);
      matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(125.0F));
      matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));
      matrixStack.translate(0.19F, -1.4F, 0.5F);
      this.renderHandLocation(playerEntity, gun, true, matrixStack);
      playerRenderer.renderRightArm(matrixStack, renderTypeBuffer, packedLight,
          playerEntity);

    }
    matrixStack.pop();

    // Left Hand
    matrixStack.push();
    {
      if (animation != null) {
        animation.doRenderHand(itemStack, partialTicks, false, matrixStack);
      }

      this.mc.getTextureManager().bindTexture(playerEntity.getLocationSkin());
      matrixStack.translate(1.5F, 0.0F, 0.0F);
      matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(120.0F));
      matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(120.0F));
      matrixStack.translate(0.3F, -1.5F, -0.12F);
      this.renderHandLocation(playerEntity, gun, false, matrixStack);
      matrixStack.scale(1.0F, 1.2F, 1.0F);
      playerRenderer.renderLeftArm(matrixStack, renderTypeBuffer, packedLight,
          playerEntity);
    }
    matrixStack.pop();
  }

  private final void renderFirstPerson(AbstractClientPlayerEntity playerEntity,
      ItemStack itemStack, IGun gun, IScope scope, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer,
      int packedLight, int packedOverlay, ResourceLocation texture, GunAnimation animation,
      float partialTicks) {
    matrixStack.push();
    {
      if (scope != null & scope.isAiming(playerEntity, itemStack)) {
        this.renderGunFirstPersonAiming(playerEntity, gun, matrixStack);
      } else {
        if (!playerEntity.isSprinting()) {
          this.renderFirstPersonArms(playerEntity, itemStack, gun, matrixStack, renderTypeBuffer,
              packedLight, animation, partialTicks);
        }

        if (this.flash) {
          this.flash = false;
          matrixStack.push();
          {
            matrixStack.multiply(new Vector3f(-0.08F, 1.0F, 0.0F).getDegreesQuaternion(-85));
            matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(30));
            matrixStack.scale(muzzleScale, muzzleScale, muzzleScale);
            if (gun.getAttachments().contains(ModItems.SUPPRESSOR.get())) {
              muzzleFlashZ -= 0.4;
            }
            matrixStack.translate(muzzleFlashX, muzzleFlashY, muzzleFlashZ);
            IVertexBuilder flashVertexBuilder = renderTypeBuffer
                .getBuffer(this.muzzleFlashModel.getLayer(new ResourceLocation(CraftingDead.ID,
                    "textures/models/guns/flashes/flash"
                        + (random.nextInt(3) + 1) + ".png")));
            this.muzzleFlashModel.render(matrixStack, flashVertexBuilder, packedLight,
                packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
          }
          matrixStack.pop();
        }

        this.renderGunFirstPerson(playerEntity, gun, matrixStack);
      }

      if (playerEntity.isSprinting()) {
        this.renderWhileSprinting(matrixStack);
      }

      if (animation != null) {
        animation.doRender(itemStack, partialTicks, matrixStack);
      }

      IVertexBuilder vertexBuilder =
          renderTypeBuffer.getBuffer(this.gunModel.getLayer(texture));

      float r = 1.0F;
      float g = 1.0F;
      float b = 1.0F;
      IPaint paint = gun.getPaintStack().getCapability(ModCapabilities.PAINT).orElse(null);
      if (paint != null && paint.getColour().isPresent()) {
        int colour = paint.getColour().get();
        r = (float) (colour >> 16 & 255) / 255.0F;
        g = (float) (colour >> 8 & 255) / 255.0F;
        b = (float) (colour & 255) / 255.0F;
      }

      this.gunModel.render(matrixStack, vertexBuilder, packedLight, packedOverlay, r, g, b,
          1.0F);

      matrixStack.push();
      {
        if (animation != null) {
          animation.doRenderAmmo(itemStack, partialTicks, matrixStack);
        }
        this.renderMainGunAmmo(playerEntity, itemStack.getItem().getRegistryName(), gun,
            matrixStack,
            renderTypeBuffer, packedLight, packedOverlay);
      }
      matrixStack.pop();

      this.renderMainGunAttachments(playerEntity, gun, matrixStack, renderTypeBuffer, packedLight,
          packedOverlay);
    }
    matrixStack.pop();
  }

  private final void renderThirdPerson(LivingEntity entity, ItemStack itemStack, IGun gun,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer,
      int packedLight, int packedOverlay, ResourceLocation texture,
      GunAnimation animation,
      float partialTicks) {
    matrixStack.push();
    {
      this.renderGunThirdPerson(entity, gun, matrixStack);

      if (animation != null) {
        animation.doRender(itemStack, partialTicks, matrixStack);
      }

      final IVertexBuilder vertexBuilder =
          renderTypeBuffer.getBuffer(this.gunModel.getLayer(texture));
      float r = 1.0F;
      float g = 1.0F;
      float b = 1.0F;
      final IPaint paint = gun.getPaintStack().getCapability(ModCapabilities.PAINT).orElse(null);
      if (paint != null && paint.getColour().isPresent()) {
        int colour = paint.getColour().get();
        r = (float) (colour >> 16 & 255) / 255.0F;
        g = (float) (colour >> 8 & 255) / 255.0F;
        b = (float) (colour & 255) / 255.0F;
      }

      this.gunModel.render(matrixStack, vertexBuilder, packedLight, packedOverlay, r, g, b,
          1.0F);

      this.renderMainGunAmmo(entity, itemStack.getItem().getRegistryName(), gun, matrixStack,
          renderTypeBuffer, packedLight, packedOverlay);
      this.renderMainGunAttachments(entity, gun, matrixStack,
          renderTypeBuffer, packedLight, packedOverlay);
    }
    matrixStack.pop();
  }

  @Override
  public void renderItem(ItemStack itemStack, ItemCameraTransforms.TransformType type,
      LivingEntity entity,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {

    final IGun gun = itemStack.getCapability(ModCapabilities.GUN)
        .orElseThrow(() -> new InvalidParameterException("Gun expected"));

    final float partialTicks = this.mc.getRenderPartialTicks();

    final Pair<GunAnimation, Runnable> animationPair = this.animations.peek();
    final GunAnimation animation = animationPair == null ? null : animationPair.getLeft();

    final ResourceLocation texture = this.getTexture(itemStack.getItem().getRegistryName(), gun);

    final IScope scope = itemStack.getCapability(ModCapabilities.SCOPE).orElse(null);
    final boolean scopeOverlayActive = scope != null && scope.isAiming(entity, itemStack)
        && scope.getOverlayTexture(entity, itemStack).isPresent();
    if (scopeOverlayActive) {
      return;
    }

    final boolean leftHanded = type == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND
        || type == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;

    matrixStack.push();
    {
      this.applyLegacyTransforms(matrixStack, type, leftHanded);
      switch (type) {
        case HEAD:
          this.renderOnBack(entity, itemStack.getItem().getRegistryName(), gun,
              matrixStack, renderTypeBuffer, packedLight, packedOverlay);
          break;
        case FIRST_PERSON_LEFT_HAND:
        case FIRST_PERSON_RIGHT_HAND:
          if (entity instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity playerEntity = (AbstractClientPlayerEntity) entity;
            this.renderFirstPerson(playerEntity, itemStack, gun, scope, matrixStack,
                renderTypeBuffer,
                packedLight, packedOverlay, texture, animation, partialTicks);
          }
          break;
        case THIRD_PERSON_LEFT_HAND:
        case THIRD_PERSON_RIGHT_HAND:
          this.renderThirdPerson(entity, itemStack, gun, matrixStack, renderTypeBuffer, packedLight,
              packedOverlay, texture, animation, partialTicks);
          break;
        default:
          break;
      }
    }
    matrixStack.pop();
  }

  public void renderOnBack(LivingEntity entity, ResourceLocation registryName,
      IGun gun,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {
    matrixStack.push();
    {
      ResourceLocation guntexture = this.getTexture(registryName, gun);

      IVertexBuilder vertexBuilder =
          renderTypeBuffer.getBuffer(this.gunModel.getLayer(guntexture));

      float r = 1.0F;
      float g = 1.0F;
      float b = 1.0F;
      IPaint paint = gun.getPaintStack().getCapability(ModCapabilities.PAINT).orElse(null);
      if (paint != null && paint.getColour().isPresent()) {
        int colour = paint.getColour().get();
        r = (float) (colour >> 16 & 255) / 255.0F;
        g = (float) (colour >> 8 & 255) / 255.0F;
        b = (float) (colour & 255) / 255.0F;
      }

      this.renderGunOnPlayerBack(entity, gun, matrixStack);
      this.gunModel.render(matrixStack, vertexBuilder, packedLight, packedOverlay, r, g, b, 1.0F);
      this.renderMainGunAmmo(entity, registryName, gun, matrixStack, renderTypeBuffer, packedLight,
          packedOverlay);
      this.renderMainGunAttachments(entity, gun, matrixStack, renderTypeBuffer, packedLight,
          packedOverlay);
    }
    matrixStack.pop();
  }

  private void renderMainGunAttachments(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {

    if (gun.hasIronSight()) {
      matrixStack.push();
      {
        this.renderIronSights(livingEntity, gun, matrixStack, renderTypeBuffer, packedLight,
            packedOverlay);
      }
      matrixStack.pop();
    }


    matrixStack.push();
    {
      float scale = 0.1F;
      matrixStack.scale(scale, scale, scale);

      for (AttachmentItem attachmentItem : gun.getAttachments()) {
        if (attachmentItem.getModel() != null) {
          Pair<Model, ResourceLocation> model = attachmentItem.getModel();
          matrixStack.push();
          {
            this.renderGunAttachment(livingEntity, attachmentItem, matrixStack);
            float scale2 = 10F;
            matrixStack.scale(scale2, scale2, scale2);
            IVertexBuilder vertexBuilder =
                renderTypeBuffer
                    .getBuffer(model.getLeft().getLayer(model.getRight()));
            model.getLeft().render(matrixStack, vertexBuilder, packedLight, packedOverlay,
                1.0F, 1.0F, 1.0F, 1.0F);
          }
          matrixStack.pop();
        }
      }
    }
    matrixStack.pop();
  }

  private void renderMainGunAmmo(LivingEntity livingEntity, ResourceLocation registryName, IGun gun,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    IMagazine magazine =
        gun.getMagazineStack().getCapability(ModCapabilities.MAGAZINE).orElse(null);
    if (magazine != null && magazine.getModel() != null) {
      matrixStack.push();
      {
        Pair<Model, ResourceLocation> model = magazine.getModel();
        ResourceLocation ammoTexture =
            model.getRight() == null ? this.getTexture(registryName, gun) : model.getRight();
        float scale = 0.825F;
        matrixStack.scale(scale, scale, scale);

        this.renderGunAmmo(livingEntity, gun.getMagazineStack(), matrixStack);
        IVertexBuilder vertexBuilder =
            renderTypeBuffer.getBuffer(model.getLeft().getLayer(ammoTexture));
        model.getLeft().render(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F, 1.0F,
            1.0F, 1.0F);
      }
      matrixStack.pop();
    }
  }

  private ResourceLocation getTexture(ResourceLocation registryName, IGun gun) {
    ResourceLocation skin = gun.getPaintStack().getCapability(ModCapabilities.PAINT)
        .map(IPaint::getSkin).orElse(Optional.empty()).orElse(null);
    if (skin != null) {
      return new ResourceLocation(skin.getNamespace(), "textures/models/guns/"
          + registryName.getPath() + "_" + skin.getPath() + ".png");
    }
    return new ResourceLocation(registryName.getNamespace(),
        "textures/models/guns/" + registryName.getPath() + ".png");
  }

  public float getTracerXZ() {
    return 0.07F;
  }

  public float getTracerY() {
    return 0.03F;
  }

  /**
   * Renders a gun in third person: EntityPlayer, Gun ItemStack
   */
  protected abstract void renderGunThirdPerson(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack);

  /**
   * Renders a gun in first person: EntityPlayer, Gun ItemStack
   */
  protected abstract void renderGunFirstPerson(PlayerEntity entityplayer, IGun gun,
      MatrixStack matrixStack);

  /**
   * Renders a gun in first person when the player is aiming: EntityPlayer, Gun ItemStack
   */
  protected abstract void renderGunFirstPersonAiming(PlayerEntity entityplayer, IGun gun,
      MatrixStack matrixStack);

  /**
   * Renders the iron sights on the given gun
   */
  protected abstract void renderIronSights(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay);

  /**
   * Renders a gun on the players back
   */
  protected abstract void renderGunOnPlayerBack(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack);

  /**
   * Renders a gun's ammo if it has any: EntityPlayer, Ammo ItemStack
   */
  protected abstract void renderGunAmmo(LivingEntity livingEntity, ItemStack magazineStack,
      MatrixStack matrixStack);

  /**
   * Renders an attachment found on the gun: Entity, Gun Stack, Attachment
   */
  protected abstract void renderGunAttachment(LivingEntity livingEntity, AttachmentItem attachment,
      MatrixStack matrixStack);

  /**
   * Model for the gun
   */
  protected abstract Model getGunModel();

  public void renderHandLocation(PlayerEntity playerEntity, IGun gun, boolean rightHanded,
      MatrixStack matrixStack) {
    if (rightHanded) {
      matrixStack.translate(0.0F, 0.0F, 0.0F);
    } else {
      matrixStack.translate(0.0F, 0.0F, 0.0F);
    }
  }

  public void renderWhileSprinting(MatrixStack matrixStack) {
    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-70));
    matrixStack.translate(0.7F, 0.0F, 0.2F);
  }
}
