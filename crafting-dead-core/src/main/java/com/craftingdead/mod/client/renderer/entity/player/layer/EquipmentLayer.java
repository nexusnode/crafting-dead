package com.craftingdead.mod.client.renderer.entity.player.layer;

import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.commons.lang3.Validate;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.player.IPlayer;
import com.craftingdead.mod.client.util.RenderUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Layer that renders {@link IEquipableModel}s attached to a player's body.
 */
@SuppressWarnings("deprecation")
public class EquipmentLayer
    extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

  /**
   * A getter {@link Function} that gets the {@link ItemStack} that will be rendered by this layer.
   */
  private final Function<IPlayer<? extends PlayerEntity>, ItemStack> itemStackGetter;

  /**
   * Whether this model should be rotated when the player is crouching.
   */
  private final boolean useCrouchingOrientation;

  /**
   * Whether this model should be rotated accordingly to the player's head.
   */
  private final boolean useHeadOrientation;

  /**
   * Optional arbitrary transformation right before rendering the {@link ItemStack}.
   */
  private final Consumer<MatrixStack> transformation;

  private EquipmentLayer(Builder builder) {
    super(builder.entityRenderer);
    this.itemStackGetter = builder.itemStackGetter;
    this.useCrouchingOrientation = builder.useCrouchingOrientation;
    this.transformation = builder.tranformation;
    this.useHeadOrientation = builder.useHeadOrientation;
  }

  @Override
  public void render(MatrixStack matrix, IRenderTypeBuffer buffers,
      int somethingThatSeemsToBeLightLevel, AbstractClientPlayerEntity playerEntity,
      float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_,
      float p_225628_10_) {

    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    playerEntity.getCapability(ModCapabilities.PLAYER).ifPresent(player -> {

      ItemStack itemStack = this.itemStackGetter.apply(player);

      if (!itemStack.isEmpty()) {
        IBakedModel itemModel =
            itemRenderer.getItemModelWithOverrides(itemStack, playerEntity.world, playerEntity);


        matrix.push();

        // Applies crouching rotation is needed
        if (this.useCrouchingOrientation && playerEntity.isCrouching()) {
          RenderUtil.applyPlayerCrouchRotation(matrix);
        }

        // Applies the head rotation if needed
        if (this.useHeadOrientation) {
          this.getEntityModel().func_205072_a().rotate(matrix);
        }

        // Applies the arbitrary transformation if needed
        if (this.transformation != null) {
          this.transformation.accept(matrix);
        }

        // Renders the item. Also note the TransformType.
        itemRenderer
            .renderItem(itemStack, TransformType.HEAD, false, matrix, buffers,
                somethingThatSeemsToBeLightLevel, OverlayTexture.DEFAULT_UV, itemModel);

        matrix.pop();
      }
    });
  }

  public static class Builder {
    private IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> entityRenderer;
    private Function<IPlayer<? extends PlayerEntity>, ItemStack> itemStackGetter;
    private Consumer<MatrixStack> tranformation;
    private boolean useCrouchingOrientation;
    private boolean useHeadOrientation;

    public Builder withRenderer(
        IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> entityRenderer) {
      this.entityRenderer = entityRenderer;
      return this;
    }

    public Builder withItemStackGetter(
        Function<IPlayer<? extends PlayerEntity>, ItemStack> itemStackGetter) {
      this.itemStackGetter = itemStackGetter;
      return this;
    }

    public Builder withArbitraryTransformation(Consumer<MatrixStack> transformation) {
      this.tranformation = transformation;
      return this;
    }

    public Builder withCrouchingOrientation(boolean useCrouchingOrientation) {
      this.useCrouchingOrientation = useCrouchingOrientation;
      return this;
    }

    public Builder withHeadOrientation(boolean useHeadOrientation) {
      this.useHeadOrientation = useHeadOrientation;
      return this;
    }

    public EquipmentLayer build() {
      Validate.notNull(this.entityRenderer, "The renderer must not be null");
      Validate.notNull(this.itemStackGetter, "The ItemStack getter must not be null");

      return new EquipmentLayer(this);
    }
  }
}
