package br.toe.engine.platform.window;

import br.toe.engine.platform.Platform;
import br.toe.engine.platform.logger.*;
import br.toe.framework.cdi.annotation.*;
import org.lwjgl.glfw.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

@Singleton
public final class GLWindow extends GLFWWindow {
    private static final Logger LOGGER = LoggerFactory.getLogger(GLWindow.class);

    private Callback frambufferCallback;

    @Override
    protected void doInitialize () {
        LOGGER.trace("Creating window");
        glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        if (Platform.IS_MAC_OSX)
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);


        setHandle(glfwCreateWindow(
                size.x(), size.y(),
                "Default Window Title",
                GLFW_FALSE, GLFW_FALSE
        ));

        if (getHandle() == GLFW_FALSE)
            throw new WindowException("[GLFW] Unable to create window");

        glfwMakeContextCurrent(getHandle());
        glfwSwapInterval(0);

        frambufferCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke (long window, int width, int height) {
                glViewport(0, 0, width, height);
            }
        };

        glfwSetFramebufferSizeCallback(getHandle(), (GLFWFramebufferSizeCallbackI)frambufferCallback);
    }

    @Override
    public void refresh () {
        glfwSwapBuffers(getHandle());
        glfwPollEvents();
    }

    @Override
    protected void doDestroy () {
        if (frambufferCallback != null)
            frambufferCallback.free();
    }
}
