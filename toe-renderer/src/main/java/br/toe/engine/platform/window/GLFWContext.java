package br.toe.engine.platform.window;

import br.toe.engine.platform.logger.*;
import br.toe.utils.*;

import static org.lwjgl.glfw.GLFW.*;

public final class GLFWContext implements Lifecycle {
    private static final Logger LOGGER = LoggerFactory.getLogger(GLFWContext.class);

    @Override
    public void initialize() {
        LOGGER.trace("GLFW.glfwInit()");
        if ( ! glfwInit()) {
            glfwTerminate();
            throw new WindowException("[GLFW] Unable to initialize");
        }

        LOGGER.debug("----------- GLFW -----------");
        LOGGER.debug("Version: %s", glfwGetVersionString());
        LOGGER.debug("----------- GLFW -----------");
    }

    @Override
    public void destroy() {
        LOGGER.trace("GLFW.glfwTerminate()");
        glfwTerminate();
    }
}
