package br.toe.engine.renderer.provider.opengl;

import br.toe.engine.renderer.*;
import br.toe.engine.renderer.provider.*;
import br.toe.engine.utils.*;
import br.toe.framework.logging.*;
import br.toe.game.*;
import org.joml.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.system.MemoryUtil.*;

public final class GLRenderer extends GLObject implements RendererApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(GLRenderer.class);

    @Override
    public void initialize() {
        LOGGER.info("Initializing OpenGL");

        final var capabilities = GL.createCapabilities();
        LOGGER.debug("---------- OpenGL ----------");
        LOGGER.debug("Vendor: %s", glGetString(GL_VENDOR));
        LOGGER.debug("Version: %s", glGetString(GL_VERSION));
        LOGGER.debug("---------- OpenGL ----------");

        glEnable(GL_DEPTH_TEST);

        final var color = GameSettings.get("renderer.background.color", Vector4fc.class);
        glClearColor(color.x(), color.y(), color.z(), color.w());

        if (capabilities.OpenGL43) {
            glEnable(GL_DEBUG_OUTPUT);
            glDebugMessageCallback(new GLDebugMessageCallback() {
                @Override
                public void invoke(int source, int type, int id, int severity, int length, long message, long userParam) {
                    if (severity == GL_DEBUG_SEVERITY_NOTIFICATION)
                        return;

                    GameState.running = false;
                    throw new RendererProviderException(
                            "OpenGL ERROR:%n%nID: 0x%X%nType: %s%nSeverity: %s%nSource: %s%nMessage: %s%n".formatted(
                                    id,
                                    GLConstants.getDebugType(type),
                                    GLConstants.getDebugSeverity(severity),
                                    GLConstants.getDebugSource(source),
                                    memUTF8(memByteBuffer(message, length))
                    ));
                }
            }, GL_ZERO);
        }
    }

    @Override
    public void setWireframe (boolean desired) {
        glPolygonMode(GL_FRONT_AND_BACK, desired ? GL_LINE : GL_FILL);
    }

    @Override
    public void clear () {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void draw (Mesh mesh, Shader shader) {
        shader.bind();
        mesh.bind();
        glDrawElements(GL_TRIANGLES, mesh.use(), GL11C.GL_UNSIGNED_INT, 0L);
    }

    @Override
    public void destroy() {
        LOGGER.trace("GL.setCapabilities(null)");
        GL.setCapabilities(null);
    }
}
