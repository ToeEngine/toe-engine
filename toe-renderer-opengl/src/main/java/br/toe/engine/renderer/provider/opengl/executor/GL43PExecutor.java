package br.toe.engine.renderer.provider.opengl.executor;

import br.toe.engine.renderer.provider.*;
import br.toe.game.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL43C.*;
import static org.lwjgl.system.MemoryUtil.*;

final class GL43PExecutor extends AbstractGLExecutor {

    public GL43PExecutor() {
        glEnable(GL_DEBUG_OUTPUT);
        glDebugMessageCallback(new GLDebugMessageCallback() {
            @Override
            public void invoke(int source, int type, int id, int severity, int length, long message, long userParam) {
                if (severity == GL_DEBUG_SEVERITY_NOTIFICATION)
                    return;

                GameState.running = false;
                throw new RendererProviderException("OpenGL ERROR:%n%nID: 0x%X%nType: %s%nSeverity: %s%nSource: %s%nMessage: %s%n".formatted(
                        id, getDebugType(type), getDebugSeverity(severity), getDebugSource(source), memUTF8(memByteBuffer(message, length))
                ));
            }
        }, GL_ZERO);
    }

    public <T> T call (String target, Object... args) {
        return super.call(target, args);
    }

    private String getDebugSource(int source) {
        return switch (source) {
            case GL_DEBUG_SOURCE_API -> "API";
            case GL_DEBUG_SOURCE_WINDOW_SYSTEM -> "WINDOW SYSTEM";
            case GL_DEBUG_SOURCE_SHADER_COMPILER -> "SHADER COMPILER";
            case GL_DEBUG_SOURCE_THIRD_PARTY -> "THIRD PARTY";
            case GL_DEBUG_SOURCE_APPLICATION -> "APPLICATION";
            case GL_DEBUG_SOURCE_OTHER -> "OTHER";
            default -> "???";
        };
    }

    private String getDebugType(int type) {
        return switch (type) {
            case GL_DEBUG_TYPE_ERROR -> "ERROR";
            case GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR -> "DEPRECATED BEHAVIOR";
            case GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR -> "UNDEFINED BEHAVIOR";
            case GL_DEBUG_TYPE_PORTABILITY -> "PORTABILITY";
            case GL_DEBUG_TYPE_PERFORMANCE -> "PERFORMANCE";
            case GL_DEBUG_TYPE_OTHER -> "OTHER";
            case GL_DEBUG_TYPE_MARKER -> "MARKER";
            default -> "???";
        };
    }

    private String getDebugSeverity(int severity) {
        return switch (severity) {
            case GL_DEBUG_SEVERITY_HIGH -> "HIGH";
            case GL_DEBUG_SEVERITY_MEDIUM -> "MEDIUM";
            case GL_DEBUG_SEVERITY_LOW -> "LOW";
            case GL_DEBUG_SEVERITY_NOTIFICATION -> "NOTIFICATION";
            default -> "???";
        };
    }

}
