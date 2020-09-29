package com.craftingdead.immerse.client.gui.component;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.ClientDist;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import io.noties.tumbleweed.Timeline;
import io.noties.tumbleweed.Tween;
import io.noties.tumbleweed.TweenManager;
import io.noties.tumbleweed.TweenType;
import io.noties.tumbleweed.equations.Sine;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public abstract class Component<SELF extends Component<SELF>> extends AbstractGui
    implements IRenderable, IGuiEventListener {

  public static final TweenType<Component<?>> X_SCALE =
      new SimpleTweenType<>(Component::getXScale, Component::setXScale);
  public static final TweenType<Component<?>> Y_SCALE =
      new SimpleTweenType<>(Component::getYScale, Component::setYScale);
  public static final TweenType<Component<?>> X_TRANSLATION =
      new SimpleTweenType<>(Component::getXTranslation, Component::setXTranslation);
  public static final TweenType<Component<?>> Y_TRANSLATION =
      new SimpleTweenType<>(Component::getYTranslation, Component::setYTranslation);

  protected final Minecraft minecraft = Minecraft.getInstance();

  protected final MainWindow mainWindow = this.minecraft.getMainWindow();

  protected final ClientDist clientDist =
      (ClientDist) CraftingDeadImmerse.getInstance().getModDist();

  private final IEventBus eventBus = BusBuilder.builder().build();

  private final TweenManager tweenManager = TweenManager.create();

  private Supplier<Double> xFactory = () -> 0.0D;
  private Supplier<Double> yFactory = () -> 0.0D;
  private Supplier<Double> widthFactory =
      () -> this.parent.map(Component::getWidth).orElse((double) this.mainWindow.getScaledWidth());
  private Supplier<Double> heightFactory =
      () -> this.parent.map(Component::getHeight)
          .orElse((double) this.mainWindow.getScaledHeight());

  private boolean centre;

  private boolean scaleWidth = true;
  private boolean scaleHeight = true;

  private float xScale = 1.0F;
  private float yScale = 1.0F;

  private float xTranslation = 1.0F;
  private float yTranslation = 1.0F;

  protected Optional<ParentComponent<?>> parent = Optional.empty();

  private boolean wasMouseOver;

  private float lastTime = 0F;

  private Tooltip tooltip = new Tooltip(new StringTextComponent("Hello World!"));

  public static class Tooltip {

    private static final TweenType<Tooltip> ALPHA =
        new SimpleTweenType<>(Tooltip::getAlpha, Tooltip::setAlpha);
    private static final TweenType<Tooltip> TEXT_ALPHA =
        new SimpleTweenType<>(Tooltip::getTextAlpha, Tooltip::setTextAlpha);

    private final ITextComponent text;
    private float alpha = 0;
    private float textAlpha = 0;

    public Tooltip(ITextComponent text) {
      this.text = text;
    }

    public void render(FontRenderer fontRenderer, float x, float y, float partialTicks) {
      float width = 10.0F + fontRenderer.getStringWidth(this.text.getFormattedText());
      float height = 14;
      RenderSystem.enableBlend();
      RenderUtil.fill(x, y, x + width, y + height, +((int) (this.alpha * 0.5F * 255.0F) << 24));
      RenderUtil.fill(x, y + 1, x + width, y + height,
          0x808080 + ((int) (this.alpha * 255.0F) << 24));
      fontRenderer.drawString(this.text.getFormattedText(),
          x + (width - fontRenderer.getStringWidth(this.text.getFormattedText())) / 2, y + 4,
          0xFFFFFF + ((int) (this.textAlpha * 255.0F) << 24));
      RenderSystem.disableBlend();
    }

    public float getAlpha() {
      return this.alpha;
    }

    public void setAlpha(float alpha) {
      this.alpha = alpha;
    }

    public float getTextAlpha() {
      return this.textAlpha;
    }

    public void setTextAlpha(float textAlpha) {
      this.textAlpha = textAlpha;
    }
  }

  public Component() {
    this.addListener(MouseEnterEvent.class, (c, e) -> Timeline.createParallel(150)
        .push(Tween.to(this.tooltip, Tooltip.ALPHA)
            .target(1.0F))
        .push(Tween.to(this.tooltip, Tooltip.TEXT_ALPHA)
            .delay(100.0F)
            .target(1.0F)
            .ease(Sine.INOUT))
        .start(this.getTweenManager()));

    this.addListener(MouseLeaveEvent.class, (c, e) -> Timeline.createParallel(250)
        .push(Tween.to(this.tooltip, Tooltip.ALPHA)
            .target(0.0F))
        .push(Tween.to(this.tooltip, Tooltip.TEXT_ALPHA)
            .target(0.0F))
        .start(this.getTweenManager()));
  }

  protected void added() {
    if (this.overrideSize()) {
      if (!this.getBestWidth().isPresent() || !this.getBestHeight().isPresent()) {
        throw new IllegalStateException(
            "A preferred width and height must be specified for size override");
      }
      this.setAutoWidth().setAutoHeight();
    }
  }

  protected void removed() {}

  protected void tick() {}

  protected void resized() {}

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    float currentTime = this.lastTime + partialTicks;
    float deltaTime = (currentTime - this.lastTime) * 50;
    this.lastTime = currentTime;
    this.tweenManager.update(deltaTime);
    this.tooltip.render(this.minecraft.fontRenderer,
        10.0F + this.getXFloat() + this.getWidthFloat(), this.getYFloat(), partialTicks);
  }

  protected void mouseEntered() {
    this.post(new MouseEnterEvent());
  }

  protected void mouseLeft() {
    this.post(new MouseLeaveEvent());
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    boolean mouseOver = this.isMouseOver(mouseX, mouseY);
    if (mouseOver && !this.wasMouseOver) {
      this.mouseEntered();
    } else if (this.wasMouseOver && !mouseOver) {
      this.mouseLeft();
    }
    this.wasMouseOver = mouseOver;
    this.post(new MouseEvent.MoveEvent(mouseX, mouseY));
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return this.post(new MouseEvent.ButtonEvent(mouseX, mouseY, button, GLFW.GLFW_PRESS));
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    return this.post(new MouseEvent.ButtonEvent(mouseX, mouseY, button, GLFW.GLFW_RELEASE));
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX,
      double deltaY) {
    return this.post(new MouseEvent.DragEvent(mouseX, mouseY, button, deltaX, deltaY));
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    return this.post(new MouseEvent.ScrollEvent(mouseX, mouseY, scrollDelta));
  }

  @Override
  public boolean keyPressed(int key, int scancode, int mods) {
    return this.post(new KeyEvent(key, scancode, GLFW.GLFW_PRESS, mods));
  }

  @Override
  public boolean keyReleased(int key, int scancode, int mods) {
    return this.post(new KeyEvent(key, scancode, GLFW.GLFW_RELEASE, mods));
  }

  @Override
  public boolean charTyped(char character, int mods) {
    return this.post(new CharTypeEvent(character, mods));
  }

  @Override
  public boolean isMouseOver(double mouseX, double mouseY) {
    return mouseX > this.getX() && mouseX < this.getX() + this.getWidth() && mouseY > this.getY()
        && mouseY < this.getY() + this.getHeight();
  }

  /**
   * Helper method to add an animation triggered by {@link MouseEnterEvent} that will be reversed
   * upon {@link MouseLeaveEvent}.
   * 
   * @param target - the {@link IAnimatedProperty} to animate
   * @param to - the target value
   * @param fadeDuration - the duration of the animation
   * @return instance of self for easy construction
   */
  public SELF addHoverAnimation(TweenType<? super SELF> tweenType, float[] to, float duration) {
    float[] from = new float[tweenType.getValuesSize()];
    tweenType.getValues(this.self(), from);
    this
        .addListener(MouseEnterEvent.class,
            (component, event) -> Tween
                .to(this.self(), tweenType, duration)
                .target(to)
                .start(this.tweenManager))
        .addListener(MouseLeaveEvent.class,
            (component, event) -> Tween
                .to(this.self(), tweenType, duration)
                .target(from)
                .start(this.tweenManager));
    return this.self();
  }

  public SELF addClickSound(SoundEvent soundEvent) {
    return this.addListener(MouseEvent.ButtonEvent.class, (component, event) -> {
      if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT
          && event.getAction() == GLFW.GLFW_PRESS) {
        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(soundEvent, 1.0F));
      }
    });
  }

  public SELF addActionListener(Consumer<SELF> consumer) {
    this.addListener(MouseEvent.ButtonEvent.class, (component, event) -> {
      if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT
          && event.getAction() == GLFW.GLFW_PRESS) {
        consumer.accept(this.self());
      }
    });
    return this.self();
  }

  public <T extends Event> SELF addListener(Class<T> eventType, BiConsumer<SELF, T> consumer) {
    this.eventBus
        .addListener(EventPriority.NORMAL, true, eventType,
            event -> consumer.accept(this.self(), event));
    return this.self();
  }

  /**
   * Submit the event for dispatch to appropriate listeners.
   *
   * @param event - The event to dispatch to listeners
   * @return true if the event was cancelled
   */
  public boolean post(Event event) {
    return this.eventBus.post(event);
  }

  public float getXFloat() {
    return (float) this.getX();
  }

  public double getX() {
    double x =
        (this.parent.map(Component::getX).orElse(0.0D) + this.xFactory.get()) * this.xTranslation;
    return this.centre ? x - this.getWidth() / 2 : x;
  }

  public SELF setX(int x) {
    this.xFactory = () -> (double) x;
    return this.self();
  }

  public SELF setXPercent(float xPercent) {
    this.xFactory =
        () -> this.parent.map(Component::getWidth).orElse((double) this.mainWindow.getScaledWidth())
            * xPercent;
    return this.self();
  }

  public float getYFloat() {
    return (float) this.getY();
  }

  public double getY() {
    double y =
        (this.parent.map(Component::getY).orElse(0.0D) + this.yFactory.get()) * this.yTranslation;
    return this.centre ? y - this.getHeight() / 2 : y;
  }

  public SELF setY(int y) {
    this.yFactory = () -> (double) y;
    return this.self();
  }

  public SELF setYPercent(float yPercent) {
    this.yFactory =
        () -> this.parent.map(Component::getHeight)
            .orElse((double) this.mainWindow.getScaledHeight())
            * yPercent;
    return this.self();
  }

  public float getWidthFloat() {
    return (float) this.getWidth();
  }

  public double getWidth() {
    double width = this.widthFactory.get() * this.xScale;
    return this.scaleWidth ? width : width / this.mainWindow.getGuiScaleFactor();
  }

  public SELF setWidth(int width) {
    this.checkOverrideSize();
    this.widthFactory = () -> (double) width;
    return this.self();
  }

  public SELF setWidthPercent(float widthPercent) {
    this.checkOverrideSize();
    this.widthFactory =
        () -> this.parent.map(Component::getWidth).orElse((double) this.mainWindow.getScaledWidth())
            * widthPercent;
    return this.self();
  }

  public SELF setAutoWidth() {
    this.widthFactory = () -> this.getBestWidth().orElseGet(this.heightFactory);
    return this.self();
  }

  public float getHeightFloat() {
    return (float) this.getHeight();
  }

  public double getHeight() {
    double height = this.heightFactory.get() * this.yScale;
    return this.scaleHeight ? height : height / this.mainWindow.getGuiScaleFactor();
  }

  public SELF setHeight(int height) {
    this.checkOverrideSize();
    this.heightFactory = () -> (double) height;
    return this.self();
  }

  public SELF setHeightPercent(float heightPercent) {
    this.checkOverrideSize();
    this.heightFactory =
        () -> this.parent.map(Component::getHeight)
            .orElse((double) this.mainWindow.getScaledHeight())
            * heightPercent;
    return this.self();
  }

  public SELF setAutoHeight() {
    this.heightFactory = () -> this.getBestHeight().orElseGet(this.widthFactory);
    return this.self();
  }

  public SELF setCentre(boolean centre) {
    this.centre = centre;
    return this.self();
  }

  public SELF setScaleWidth(boolean scaleWidth) {
    this.scaleWidth = scaleWidth;
    return this.self();
  }

  public SELF setScaleHeight(boolean scaleHeight) {
    this.scaleHeight = scaleHeight;
    return this.self();
  }

  public float getXScale() {
    return this.xScale;
  }

  public SELF setXScale(float xScale) {
    this.xScale = xScale;
    return this.self();
  }

  public float getYScale() {
    return this.yScale;
  }

  public SELF setYScale(float yScale) {
    this.yScale = yScale;
    return this.self();
  }

  public SELF setScale(float scale) {
    return this.setXScale(scale).setYScale(scale);
  }

  public float getXTranslation() {
    return this.xTranslation;
  }

  public SELF setXTranslation(float xTranslation) {
    this.xTranslation = xTranslation;
    return this.self();
  }

  public float getYTranslation() {
    return this.yTranslation;
  }

  public SELF setYTranslation(float yTranslation) {
    this.yTranslation = yTranslation;
    return this.self();
  }

  protected Optional<Double> getBestWidth() {
    return Optional.empty();
  }

  protected Optional<Double> getBestHeight() {
    return Optional.empty();
  }

  private void checkOverrideSize() {
    if (this.overrideSize()) {
      throw new UnsupportedOperationException("Component not resizable");
    }
  }

  protected boolean overrideSize() {
    return false;
  }

  public TweenManager getTweenManager() {
    return this.tweenManager;
  }

  @SuppressWarnings("unchecked")
  protected SELF self() {
    return (SELF) this;
  }
}
