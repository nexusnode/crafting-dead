package com.craftingdead.mod.client.renderer.loading;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.asm.FMLSanityChecker;

public abstract class AbstractLoadingScreen {

	private static final IResourcePack mcPack = Minecraft.getMinecraft().defaultResourcePack;
	private static final IResourcePack fmlPack = createResourcePack(FMLSanityChecker.fmlLocation);
	private IResourcePack miscPack;

	private final ResourceLocation fontLoc = new ResourceLocation("textures/font/ascii.png");
	private Texture fontTexture;

	protected SplashFontRenderer fontRenderer;

	protected Minecraft mc;

	protected int width, height;

	private List<Texture> textures = new ArrayList<Texture>();

	public AbstractLoadingScreen() {
		File miscPackFile = new File(Minecraft.getMinecraft().gameDir, "resources");
		miscPack = createResourcePack(miscPackFile);

		fontTexture = new Texture(fontLoc, null);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		fontRenderer = new SplashFontRenderer();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	protected void unload() {
		for (Texture texture : this.textures) {
			texture.delete();
		}
	}

	protected abstract void drawScreen(int mouseX, int mouseY);

	public final void handleInput() {
		if (Keyboard.isCreated()) {
			while (Keyboard.next()) {
				this.handleKeyboardInput();
			}
		}
	}

	private final void handleKeyboardInput() {
		char eventChar = Keyboard.getEventCharacter();
		if (Keyboard.getEventKey() == 0 && eventChar >= ' ' || Keyboard.getEventKeyState()) {
			this.keyTyped(eventChar, Keyboard.getEventKey());
		}

	}

	protected void keyTyped(char typedChar, int keyCode) {
		;
	}

	protected void drawImage(double x, double y, double width, double height, Texture texture, float alpha) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
		texture.bind();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + height, 0.0D).tex(0.0D, 1.0D).endVertex();
		bufferbuilder.pos(x + width, y + height, 0.0D).tex(1.0D, 1.0D).endVertex();
		bufferbuilder.pos(x + width, y, 0.0D).tex(1.0D, 0.0D).endVertex();
		bufferbuilder.pos(x, y, 0.0D).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	private static IResourcePack createResourcePack(File file) {
		if (file.isDirectory()) {
			return new FolderResourcePack(file);
		} else {
			return new FileResourcePack(file);
		}
	}

	protected class Texture {
		private final ResourceLocation location;

		private int glTextureId = -1;

		public Texture(ResourceLocation location, @Nullable ResourceLocation fallback) {
			this(location, fallback, true);
		}

		public Texture(ResourceLocation location, @Nullable ResourceLocation fallback, boolean allowRP) {
			InputStream s = null;
			try {
				this.location = location;
				this.glTextureId = GL11.glGenTextures();

				s = open(location, fallback, allowRP);

				BufferedImage bufferedImage = ImageIO.read(s);

				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.glTextureId);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, bufferedImage.getWidth(),
						bufferedImage.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
						this.convertImage(bufferedImage));

				GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -2);

				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

				textures.add(this);
			} catch (IOException e) {
				FMLLog.log.error("Error reading texture from file: {}", location, e);
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(s);
			}
		}

		public ResourceLocation getLocation() {
			return location;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public void bind() {
			if (this.glTextureId != -1)
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.glTextureId);
		}

		public void delete() {
			if (this.glTextureId != -1)
				GL11.glDeleteTextures(this.glTextureId);
		}

		private ByteBuffer convertImage(BufferedImage image) {
			int[] pixels = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

			ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

			for (int y = 0; y < image.getHeight(); y++) {
				for (int x = 0; x < image.getWidth(); x++) {
					int pixel = pixels[y * image.getWidth() + x];
					buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
					buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
					buffer.put((byte) (pixel & 0xFF)); // Blue component
					buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component. Only for RGBA
				}
			}

			buffer.flip();

			return buffer;
		}

	}

	protected class SplashFontRenderer extends FontRenderer {
		public SplashFontRenderer() {
			super(Minecraft.getMinecraft().gameSettings, fontTexture.getLocation(), null, false);
			super.onResourceManagerReload(null);
		}

		@Override
		protected void bindTexture(@Nonnull ResourceLocation location) {
			if (location != locationFontTexture)
				throw new IllegalArgumentException();
			fontTexture.bind();
		}

		@Nonnull
		@Override
		protected IResource getResource(@Nonnull ResourceLocation location) throws IOException {
			DefaultResourcePack pack = Minecraft.getMinecraft().defaultResourcePack;
			return new SimpleResource(pack.getPackName(), location, pack.getInputStream(location), null, null);
		}

	}

	public static void checkGLError(String where) {
		int err = GL11.glGetError();
		if (err != 0) {
			throw new IllegalStateException(where + ": " + GLU.gluErrorString(err));
		}
	}

	private InputStream open(ResourceLocation loc, @Nullable ResourceLocation fallback, boolean allowResourcePack)
			throws IOException {
		if (!allowResourcePack)
			return mcPack.getInputStream(loc);

		if (miscPack.resourceExists(loc)) {
			return miscPack.getInputStream(loc);
		} else if (fmlPack.resourceExists(loc)) {
			return fmlPack.getInputStream(loc);
		} else if (!mcPack.resourceExists(loc) && fallback != null) {
			return open(fallback, null, true);
		}
		return mcPack.getInputStream(loc);
	}

}
