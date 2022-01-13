package br.toe.engine.platform.window;

import br.toe.engine.platform.*;
import br.toe.framework.cdi.annotation.*;
import br.toe.framework.logging.*;
import org.joml.Math;
import org.joml.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.GLFW.*;

@Singleton
public final class GLFWMonitor extends External<Long> implements Monitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(GLFWMonitor.class);
    private static final double MM_TO_INCH = 0.0393701;

    private final String name;
    private final int refreshRate;
    private final Vector3ic color;
    private final Vector2ic size;
    private final Vector2fc scale;
    private final Vector2ic position;
    private final Vector2ic resolution;

    private GLFWMonitor() {
        setHandle(glfwGetPrimaryMonitor());

        name = glfwGetMonitorName(getHandle());

        final var mode = glfwGetVideoMode(getHandle());

        if (mode == null)
            throw new WindowException("Unable to glfwGetVideoMode(...)");

        refreshRate = mode.refreshRate();
        resolution = new Vector2i(mode.width(), mode.height());
        color = new Vector3i(mode.redBits(), mode.greenBits(), mode.blueBits());

        try (final var stack = MemoryStack.stackPush()) {
            final var px = stack.mallocFloat(1);
            final var py = stack.mallocFloat(1);

            GLFW.glfwGetMonitorContentScale(getHandle(), px, py);
            scale = new Vector2f(px.get(0), py.get(0));

            final var pi = stack.mallocInt(1);
            final var pj = stack.mallocInt(1);

            GLFW.glfwGetMonitorPhysicalSize(getHandle(), pi, pj);
            size = new Vector2i(pi.get(0), pj.get(0));

            GLFW.glfwGetMonitorPos(getHandle(), pi, pj);
            position = new Vector2i(pi.get(0), pj.get(0));
        }

        logMonitorInfo();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getRefreshRate() {
        return refreshRate;
    }

    @Override
    public Vector3ic getColorBits() {
        return color;
    }

    @Override
    public Vector2fc getScale() {
        return scale;
    }

    @Override
    public Vector2ic getSize() {
        return size;
    }

    @Override
    public Vector2ic getPosition() {
        return position;
    }

    @Override
    public Vector2ic getResolution() {
        return resolution;
    }

    private int gcd(int a, int b) {
        return b != 0 ? gcd(b, a % b) : a;
    }

    private String ratio(int w, int h) {
        int gcd = gcd(w, h);

        int ratioX = w / gcd;
        int ratioY = h / gcd;

        if (ratioX == 8) {
            ratioX <<= 1;
            ratioY <<= 1;
        }

        return ratioX + ":" + ratioY;
    }

    private void logMonitorInfo() {
        LOGGER.debug("---------- Monitor ----------");
        LOGGER.debug("Name            : %s (primary)", name);
        LOGGER.debug("Current mode    : %d x %d @ %d Hz (%s, R%d G%d B%d)",
                resolution.x(), resolution.y(),
                refreshRate,
                ratio(resolution.x(), resolution.y()),
                color.x(), color.y(), color.z()
        );

        LOGGER.debug("Content size    : %d x %d", Math.round(resolution.x() / scale.x()), Math.round(resolution.y() / scale.y()));

        final var inches = Math.round(Math.sqrt(size.x() * size.x() + (float) size.y() * size.y()) * MM_TO_INCH);
        final var dpi = Math.round(resolution.x() / (size.x() * MM_TO_INCH));
        LOGGER.debug("Physical size   : %dmm x %dmm (%d, %d ppi)", size.x(), size.y(), inches, dpi);

        LOGGER.debug("Virtual position: %d, %d", position.x(), position.y());
        LOGGER.debug("---------- Monitor ----------");
    }
}
