package com.craftingdead.immerse.client.gui.component;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.immerse.client.gui.property.IAnimatableProperty;
import com.craftingdead.immerse.client.gui.property.Property;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Cubic;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public abstract class Component<SELF extends Component<SELF>> extends AbstractGui
    implements IRenderable, IGuiEventListener {

  private final IEventBus eventBus = BusBuilder.builder().build();

  private final Property<ILocation> x;
  private final Property<ILocation> y;
  private final Property<ISize> width;
  private final Property<ISize> height;

  private final Property<ParentComponent<?>> parent = new Property<>(null);

  private final TweenManager tweenManager = new TweenManager();

  static {
    Tween.setCombinedAttributesLimit(4);
  }

  public Component(RegionBuilder regionBuilder) {
    this.x = new Property<>(regionBuilder.x);
    this.y = new Property<>(regionBuilder.y);
    this.width = new Property<>(regionBuilder.width);
    this.height = new Property<>(regionBuilder.height);
  }

  protected abstract void tick();

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.tweenManager.update(partialTicks * 50);
  }

  protected void mouseEntered() {
    this.post(new MouseEnterEvent());
  }

  protected void mouseLeft() {
    this.post(new MouseLeaveEvent());
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
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

  public SELF addHoverAnimation(Function<SELF, IAnimatableProperty<?>> target, float[] to,
      float duration) {
    final float[] from = Arrays.copyOf(target.apply(this.self()).getAnimatedValues(), 4);
    this
        .addListener(MouseEnterEvent.class,
            (component, event) -> Tween
                .to(target.apply(this.self()), 0, duration)
                .ease(Cubic.INOUT)
                .target(to)
                .start(this.tweenManager))
        .addListener(MouseLeaveEvent.class,
            (component, event) -> Tween
                .to(target.apply(this.self()), 0, duration)
                .ease(Cubic.INOUT)
                .target(from)
                .start(this.tweenManager));
    return this.self();
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
        .addListener(EventPriority.NORMAL, false, eventType,
            event -> consumer.accept(this.self(), event));
    return this.self();
  }

  public boolean post(Event event) {
    return this.eventBus.post(event);
  }

  public int getX() {
    return this.x.get().getX(this);
  }

  public int getY() {
    return this.y.get().getY(this);
  }

  public int getWidth() {
    return this.width.get().getWidth(this);
  }

  public int getHeight() {
    return this.height.get().getHeight(this);
  }

  protected Property<ParentComponent<?>> getParentProperty() {
    return this.parent;
  }

  public TweenManager getTweenManager() {
    return this.tweenManager;
  }

  @SuppressWarnings("unchecked")
  protected SELF self() {
    return (SELF) this;
  }

  public static class RegionBuilder {

    private ILocation x;
    private ILocation y;
    private ISize width;
    private ISize height;

    public RegionBuilder inherit() {
      this.setLocation(new InheritedLocation());
      this.setSize(new InheritedSize());
      return this;
    }

    public RegionBuilder setLocation(ILocation location) {
      this.x = this.y = location;
      return this;
    }

    public RegionBuilder setLocation(String x, String y) {
      return this.setX(x).setY(y);
    }

    public RegionBuilder setSize(ISize size) {
      this.width = this.height = size;
      return this;
    }

    public RegionBuilder setSize(String width, String height) {
      return this.setWidth(width).setHeight(height);
    }

    public RegionBuilder setX(ILocation x) {
      this.x = x;
      return this;
    }

    public RegionBuilder setY(ILocation y) {
      this.y = y;
      return this;
    }

    public RegionBuilder setX(String x) {
      this.x = parse(x, PercentLocation::new, AbsoluteLocation::new);
      return this;
    }

    public RegionBuilder setY(String y) {
      this.y = parse(y, PercentLocation::new, AbsoluteLocation::new);
      return this;
    }

    public RegionBuilder setWidth(ISize width) {
      this.width = width;
      return this;
    }

    public RegionBuilder setHeight(ISize height) {
      this.height = height;
      return this;
    }

    public RegionBuilder setWidth(String width) {
      this.width = parse(width, PercentSize::new, FixedSize::new);
      return this;
    }

    public RegionBuilder setHeight(String height) {
      this.height = parse(height, PercentSize::new, FixedSize::new);
      return this;
    }

    private static <T> T parse(String str, Function<Float, ? extends T> pct,
        Function<Integer, ? extends T> px) {
      try {
        if (str.contains("%")) {
          float pctSize = Integer.parseInt(str.split("%")[0]) / 100;
          return pct.apply(pctSize);
        } else if (str.contains("px")) {
          int pxSize = Integer.parseInt(str.split("px")[0]);
          return px.apply(pxSize);
        } else {
          throw new IllegalArgumentException();
        }
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid location string");
      }
    }
  }
}
