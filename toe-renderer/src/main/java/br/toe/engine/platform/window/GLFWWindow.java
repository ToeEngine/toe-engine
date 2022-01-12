package br.toe.engine.platform.window;

import br.toe.engine.platform.*;
import br.toe.engine.platform.logger.*;
import br.toe.framework.cdi.*;
import br.toe.game.*;
import br.toe.utils.*;
import org.joml.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.GLFW.*;

public abstract class GLFWWindow extends External<Long> implements Window, Lifecycle {
    private static final Logger LOGGER = LoggerFactory.getLogger(GLFWWindow.class);

    private final GLFWContext context = new GLFWContext();

    protected final Monitor monitor;
    protected final GLFWCallbacks callbacks;
    protected final Vector2ic size;
    private String title;

    protected GLFWWindow() {
        context.initialize();

        monitor = Factory.create(Monitor.class);

        callbacks = new GLFWCallbacks();
        size = GameSettings.get("platform.window.size", Vector2ic.class);
    }

    protected abstract void doDestroy();
    protected abstract void doInitialize();

    @Override
    public final void initialize() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);

        doInitialize();

        glfwSetWindowPos(
                getHandle(),
                (monitor.getResolution().x() - size.x()) / 2,
                (monitor.getResolution().y() - size.y()) / 2
        );

        callbacks.setUpWindowCallback(getHandle());
        callbacks.setUpInputCallback(getHandle());
    }

    @Override
    public final String getTitle() {
        return title;
    }

    @Override
    public final void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(getHandle(), title);
    }

    @Override
    public final Vector2ic getSize() {
        return size;
    }

    @Override
    public final Vector2ic getPosition() {
        final var position = new Vector2i();

        try (var stack = MemoryStack.stackPush()) {
            final var px = stack.mallocInt(1);
            final var py = stack.mallocInt(1);
            glfwGetWindowPos(getHandle(), px, py);

            position.set(px.get(), py.get());
        }

        return position;
    }
    @Override
    public final void show() {
        glfwShowWindow(getHandle());
    }

    @Override
    public final void hide() {
        glfwHideWindow(getHandle());
    }

    @Override
    public final void destroy() {
        callbacks.destroy();
        doDestroy();

        LOGGER.trace("Destroying window");
        glfwDestroyWindow(getHandle());

        context.destroy();
    }
}
