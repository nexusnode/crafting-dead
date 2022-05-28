package com.craftingdead.immerse.client.gui.screen;

import java.io.IOException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.fake.FakeLevel;
import com.craftingdead.immerse.client.fake.FakePlayer;
import com.craftingdead.immerse.client.util.FitType;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.github.humbleui.skija.FilterTileMode;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.FontStyle;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.ImageFilter;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PaintMode;
import io.github.humbleui.skija.Typeface;
import io.github.humbleui.skija.shaper.Shaper;
import io.github.humbleui.types.IRect;
import io.github.humbleui.types.Rect;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class TestScreen extends Screen {

  private Image image;

  private static final ResourceLocation SMOKE_TEXTURE =
      new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/smoke.png");


  private static long fogStartTime = Util.getMillis();

  private final LivingEntity livingEntity;


  public TestScreen() {
    super(TextComponent.EMPTY);

    this.livingEntity = new FakePlayer(Minecraft.getInstance().getUser().getGameProfile());

    try (var inputStream = Minecraft.getInstance().getResourceManager()
        .getResource(new ResourceLocation("textures/gui/title/background/panorama_1.png"))
        .getInputStream()) {
      this.image = Image.makeFromEncoded(inputStream.readAllBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }

    var image = new ResourceLocation("textures/gui/title/background/panorama");

    for (int i = 0; i < 6; ++i) {
      var location = new ResourceLocation(image.getNamespace(), image.getPath() + "_" + i + ".png");
      this.imageLocations[i] = location;

    }

  }

  @Override
  public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    this.time += partialTick;

    // RenderSystem.clear(16640, false);
    // this.minecraft.getMainRenderTarget().bindWrite(true);
    // FogRenderer.setupNoFog();
    // RenderSystem.enableTexture();
    // RenderSystem.enableCull();
    //
    // RenderSystem.viewport(0, 0, this.minecraft.getWindow().getWidth(),
    // this.minecraft.getWindow().getHeight());
    // RenderSystem.clear(256, false);
    // Lighting.setupFor3DItems();



    this.renderDirtBackground(0);

    var fogSize = FitType.COVER.getSize(1920, 1080, this.width, this.height);
    float fogWidth = fogSize.x;
    float fogHeight = fogSize.y;

    final float pct =
        Mth.clamp((Util.getMillis() - fogStartTime) / (1000.0F * 100.0F * 2.0F), 0.0F, 1.0F);
    if (pct == 1.0F) {
      fogStartTime = Util.getMillis();
    }

//    poseStack.pushPose();
//    {
//      poseStack.scale(4F, 4F, 4F);
//      RenderSystem.enableBlend();
//
//      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.3F);
//
//      RenderSystem.setShaderTexture(0, SMOKE_TEXTURE);
//
//      final float smokeX = pct * this.width;
//
//      RenderUtil.blit(poseStack, smokeX, 0, fogWidth, fogHeight);
//      RenderUtil.blit(poseStack, smokeX - fogWidth, 0, fogWidth,
//          fogHeight);
//
//      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//
//      RenderSystem.disableBlend();
//    }
//    poseStack.popPose();


//    var i =0;
//    var j =0;
//    InventoryScreen.renderEntityInInventory(i + 51, j + 75, 30, (float) (i + 51) - mouseX,
//        (float) (j + 75 - 50) - mouseY, this.livingEntity);

//    this.font.draw(poseStack, "Hello World!", 55, 50, 0xFFFFFFFF);

    // this.minecraft.getMainRenderTarget().unbindWrite();

    var skia = CraftingDeadImmerse.getInstance().getClientDist().getSkia();

    skia.begin();


    var canvas = skia.canvas();
    try (var paint = new Paint().setColor(0xFFE5E5E5).setMode(PaintMode.FILL)) {
      // skia.canvas().drawRect(Rect.makeWH(this.minecraft.getWindow().getWidth(),
      // this.minecraft.getWindow().getHeight()), paint);

      canvas.drawCircle(200, 200, 50, paint);
    }


    var image = skia.surface.makeImageSnapshot(IRect.makeXYWH(0, 0, 200, 200));

    try(var paint =  new Paint().setImageFilter(ImageFilter.makeBlur(5, 5, FilterTileMode.CLAMP)) ) {
      canvas.drawImageRect(image, Rect.makeXYWH(0, 0, 200, 200),
       paint   );
    }


    try (Typeface face = FontMgr.getDefault().matchFamilyStyle("Calibri", FontStyle.BOLD);
        Font font = new Font(face, 20);
        Paint fill = new Paint().setColor(0xFFFFFFFF);) {
      var blob = Shaper.make().shape("Hello World!", font);
      if (blob != null) {
        var bounds = blob.getBounds();
        canvas.drawTextBlob(blob, 0, 0, fill);
      }
    }

    skia.end();


  }

  private final ResourceLocation[] imageLocations = new ResourceLocation[6];

  private float time;



  @Override
  public void init() {}

  public void render(float partialTick) {


  }

  public void render(Minecraft p_108850_, float p_108851_, float p_108852_, float p_108853_) {
    Tesselator tesselator = Tesselator.getInstance();
    BufferBuilder bufferbuilder = tesselator.getBuilder();
    Matrix4f matrix4f = Matrix4f.perspective(85.0D,
        (float) p_108850_.getWindow().getWidth() / (float) p_108850_.getWindow().getHeight(), 0.05F,
        10.0F);
    RenderSystem.backupProjectionMatrix();
    RenderSystem.setProjectionMatrix(matrix4f);
    PoseStack posestack = RenderSystem.getModelViewStack();
    posestack.pushPose();
    posestack.setIdentity();
    posestack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
    RenderSystem.applyModelViewMatrix();
    RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glEnable(GL11.GL_BLEND);
    // RenderSystem.disableCull();
    // RenderSystem.depthMask(false);
    // RenderSystem.defaultBlendFunc();
    int i = 2;

    for (int j = 0; j < 4; ++j) {
      // posestack.pushPose();
      // float f = ((float) (j % 2) / 2.0F - 0.5F) / 256.0F;
      // float f1 = ((float) (j / 2) / 2.0F - 0.5F) / 256.0F;
      // float f2 = 0.0F;
      // posestack.translate((double) f, (double) f1, 0.0D);
      // posestack.mulPose(Vector3f.XP.rotationDegrees(p_108851_));
      // posestack.mulPose(Vector3f.YP.rotationDegrees(p_108852_));
      // RenderSystem.applyModelViewMatrix();

      for (int k = 0; k < 6; ++k) {
        RenderSystem.setShaderTexture(0, this.imageLocations[k]);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        // RenderSystem.defaultBlendFunc();
        RenderSystem.blendEquation(GL20.GL_FUNC_ADD);


        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int l = Math.round(255.0F * p_108853_) / (j + 1);
        if (k == 0) {
          bufferbuilder.vertex(-1.0D, -1.0D, 1.0D).uv(0.0F, 0.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(-1.0D, 1.0D, 1.0D).uv(0.0F, 1.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(1.0D, 1.0D, 1.0D).uv(1.0F, 1.0F).color(255, 255, 255, l).endVertex();
          bufferbuilder.vertex(1.0D, -1.0D, 1.0D).uv(1.0F, 0.0F).color(255, 255, 255, l)
              .endVertex();
        }

        if (k == 1) {
          bufferbuilder.vertex(1.0D, -1.0D, 1.0D).uv(0.0F, 0.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(1.0D, 1.0D, 1.0D).uv(0.0F, 1.0F).color(255, 255, 255, l).endVertex();
          bufferbuilder.vertex(1.0D, 1.0D, -1.0D).uv(1.0F, 1.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(1.0D, -1.0D, -1.0D).uv(1.0F, 0.0F).color(255, 255, 255, l)
              .endVertex();
        }

        if (k == 2) {
          bufferbuilder.vertex(1.0D, -1.0D, -1.0D).uv(0.0F, 0.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(1.0D, 1.0D, -1.0D).uv(0.0F, 1.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(-1.0D, 1.0D, -1.0D).uv(1.0F, 1.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(-1.0D, -1.0D, -1.0D).uv(1.0F, 0.0F).color(255, 255, 255, l)
              .endVertex();
        }

        if (k == 3) {
          bufferbuilder.vertex(-1.0D, -1.0D, -1.0D).uv(0.0F, 0.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(-1.0D, 1.0D, -1.0D).uv(0.0F, 1.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(-1.0D, 1.0D, 1.0D).uv(1.0F, 1.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(-1.0D, -1.0D, 1.0D).uv(1.0F, 0.0F).color(255, 255, 255, l)
              .endVertex();
        }

        if (k == 4) {
          bufferbuilder.vertex(-1.0D, -1.0D, -1.0D).uv(0.0F, 0.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(-1.0D, -1.0D, 1.0D).uv(0.0F, 1.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(1.0D, -1.0D, 1.0D).uv(1.0F, 1.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(1.0D, -1.0D, -1.0D).uv(1.0F, 0.0F).color(255, 255, 255, l)
              .endVertex();
        }

        if (k == 5) {
          bufferbuilder.vertex(-1.0D, 1.0D, 1.0D).uv(0.0F, 0.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(-1.0D, 1.0D, -1.0D).uv(0.0F, 1.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(1.0D, 1.0D, -1.0D).uv(1.0F, 1.0F).color(255, 255, 255, l)
              .endVertex();
          bufferbuilder.vertex(1.0D, 1.0D, 1.0D).uv(1.0F, 0.0F).color(255, 255, 255, l).endVertex();
        }

        tesselator.end();
      }

      // posestack.popPose();
      // RenderSystem.applyModelViewMatrix();
      // RenderSystem.colorMask(true, true, true, false);
    }

    // RenderSystem.colorMask(true, true, true, true);
    RenderSystem.restoreProjectionMatrix();
    posestack.popPose();
    RenderSystem.applyModelViewMatrix();
    // RenderSystem.depthMask(true);
    // RenderSystem.enableCull();
    // RenderSystem.enableDepthTest();
  }
}
