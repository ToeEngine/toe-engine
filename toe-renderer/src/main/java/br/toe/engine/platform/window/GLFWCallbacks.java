package br.toe.engine.platform.window;

import br.toe.engine.event.*;
import br.toe.engine.platform.input.event.*;
import br.toe.engine.platform.logger.*;
import br.toe.engine.platform.window.event.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.*;
import org.lwjgl.system.*;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

public class GLFWCallbacks {
    private static final Logger LOGGER = LoggerFactory.getLogger(GLFWCallbacks.class);
    private final Stack<Callback> callbacks = new Stack<>();
    private final EventBus bus = EventBus.get();

    public GLFWCallbacks () {
        final var callback = GLFWErrorCallback.createPrint(System.err);
        glfwSetErrorCallback(callback);
        callbacks.add(callback);
    }

    public void setUpWindowCallback(final long handler) {
        LOGGER.trace("Creating Window Callbacks");
        final var locals = new Stack<Callback>();

        locals.push(new GLFWWindowCloseCallback() {
            @Override
            public void invoke(long window) {
                bus.post(new WindowClosedEvent());
            }
        });

        glfwSetWindowCloseCallback(handler, (GLFWWindowCloseCallback) locals.peek());

        locals.add(new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                bus.post(new WindowResizedEvent(width, height));
            }
        });

        glfwSetFramebufferSizeCallback(handler, (GLFWFramebufferSizeCallback) locals.peek());

        locals.add(new GLFWWindowFocusCallback() {
            @Override
            public void invoke(long window, boolean focused) {
                if (focused)
                    bus.post(new WindowFocusGainedEvent());
                else
                    bus.post(new WindowFocusLostEvent());
            }
        });

        glfwSetWindowFocusCallback(handler, (GLFWWindowFocusCallback) locals.peek());

        locals.add(new GLFWWindowIconifyCallback() {
            @Override
            public void invoke(long window, boolean iconified) {
                if (iconified)
                    bus.post(new WindowIconifiedEvent());
                else
                    bus.post(new WindowRestoredEvent());
            }
        });

        glfwSetWindowIconifyCallback(handler, (GLFWWindowIconifyCallback) locals.peek());

        locals.add(new GLFWWindowMaximizeCallback() {
            @Override
            public void invoke(long window, boolean maximized) {
                if (maximized)
                    bus.post(new WindowMaximizedEvent());
                else
                    bus.post(new WindowRestoredEvent());

            }
        });

        glfwSetWindowMaximizeCallback(handler, (GLFWWindowMaximizeCallback) locals.peek());

        callbacks.addAll(locals);
    }

    public void setUpInputCallback(final long handler) {
        setUpKeyboardCallback(handler);
        setUpMouseCallback(handler);
    }

    private void setUpKeyboardCallback(final long handler) {
        LOGGER.trace("Creating Keyboard Callbacks");
        final var locals = new Stack<Callback>();
        locals.add(new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == GLFW.GLFW_PRESS)
                    bus.post(new KeyPressedEvent(key));

                if (action == GLFW.GLFW_RELEASE)
                    bus.post(new KeyReleasedEvent(key));
            }
        });

        GLFW.glfwSetKeyCallback(handler, (GLFWKeyCallbackI) locals.peek());

        locals.add(new GLFWCharCallback() {
            @Override
            public void invoke (long window, int codepoint) {
                bus.post(new KeyTypedEvent(codepoint));
            }
        });
        GLFW.glfwSetCharCallback(handler, (GLFWCharCallbackI) locals.peek());

        callbacks.addAll(locals);
    }

    private void setUpMouseCallback(final long handler) {
        LOGGER.trace("Creating Mouse Callbacks");
        final var locals = new Stack<Callback>();

        locals.add(new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (action == GLFW.GLFW_PRESS)
                    bus.post(new MouseButtonPressedEvent(button));

                if (action == GLFW.GLFW_RELEASE)
                    bus.post(new MouseButtonReleasedEvent(button));
            }
        });

        glfwSetMouseButtonCallback(handler, (GLFWMouseButtonCallback) locals.peek());

        locals.add(new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                bus.post(new MouseMovedEvent((float) xpos, (float) ypos));
            }
        });

        glfwSetCursorPosCallback(handler, (GLFWCursorPosCallback) locals.peek());

        locals.add(new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                bus.post(new MouseScrolledEvent((float) xpos, (float) ypos));
            }
        });

        glfwSetScrollCallback(handler, (GLFWScrollCallback) locals.peek());

        callbacks.addAll(locals);
    }

    public void destroy() {
        LOGGER.trace("Destroying Callbacks");
        for (final var callback : callbacks) {
            callback.free();
        }
    }
}
