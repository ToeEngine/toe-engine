package br.toe.engine.renderer.provider.opengl.executor;

import br.toe.engine.renderer.provider.*;

import static org.lwjgl.opengl.GL46C.*;

final class GL42LExecutor extends AbstractGLExecutor {
    private final StringBuilder builder = new StringBuilder();

    @SuppressWarnings("unchecked")
    public <T> T call (String target, Object... args) {
        final var result = super.call(target, args);

        var code = 0;
        while (( code = glGetError() ) != GL_NO_ERROR) {
            builder.append("  - ");
            builder.append(switch (code) {
                case GL_INVALID_ENUM -> "Invalid Enum";
                case GL_INVALID_OPERATION -> "Invalid Operation";
                case GL_INVALID_VALUE -> "Invalid Value";
                case GL_STACK_OVERFLOW -> "Stack Overflow";
                case GL_STACK_UNDERFLOW -> "Stack Underflow";
                case GL_OUT_OF_MEMORY -> "Out Of Memory";
                case GL_INVALID_FRAMEBUFFER_OPERATION -> "Invalid Framebuffer Operation";
                case GL_CONTEXT_LOST -> "Context Lost";
                default -> "???";
            });

            builder.append("\n");
        }

        if (builder.length() > 0)
            throw new RendererProviderException("OpenGL ERRORS:\n\n%s".formatted(builder.toString()));

        return (T) result;
    }
}
