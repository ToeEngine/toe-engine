package br.toe.engine.renderer.provider.opengl.shader;

import br.toe.engine.renderer.*;
import br.toe.engine.renderer.provider.*;
import br.toe.engine.renderer.provider.opengl.*;
import br.toe.utils.*;

import java.util.*;

import static org.lwjgl.opengl.GL46C.*;

public final class GLShader extends GLObject implements Shader {

    private boolean linked;
    private final List<Tuple<ShaderType, Integer, CharSequence>> shaders = new ArrayList<>();

    @Override
    public void initialize() {
        if (getHandle() != null)
            throw new RendererProviderException("Shader already initialized");

        setHandle(glCreateProgram());
        if (getHandle() == GL_ZERO)
            throw new RendererProviderException("Unable to create Shader Program");

        build();
    }

    @Override
    public void bind() {
        glUseProgram(getHandle());
    }

    @Override
    public boolean has(ShaderType type) {
        for (var pair : shaders)
            if (pair.getA() == type)
                return true;

        return false;
    }

    public void attach(ShaderType type, String source) {
        if (linked)
            throw new RendererProviderException("Shader Program already built");

        shaders.add(new Tuple<>(type, GL_ZERO, source));
    }

    private void build() {
        for (var tuple : shaders)
            tuple.setB(create(tuple.getA(), tuple.getC()));

        compileAndLink();

        for (var tuple : shaders)
            glDeleteShader(tuple.getB());

        shaders.clear();
        linked = true;
    }

    private int create(ShaderType type, CharSequence source) {
        final var shader = glCreateShader(parse(type));
        if (shader == GL_ZERO)
            throw new RendererProviderException("Unable to create shader [%s]".formatted(type));

        glShaderSource(shader, source);
        glCompileShader(shader);

        if ((int) glGetShaderi(shader, GL_COMPILE_STATUS) == GL_ZERO)
            throw new RendererProviderException("Unable to compile shader.\nCause: %s".formatted(glGetShaderInfoLog(shader)));

        glAttachShader(getHandle(), shader);

        return shader;
    }

    private void compileAndLink() {
        glLinkProgram(getHandle());
        if ((int) glGetProgrami(getHandle(), GL_LINK_STATUS) == GL_ZERO)
            throw new RendererProviderException("Unable to link Shader Program.\nCause: %s".formatted(glGetProgramInfoLog(getHandle())));

        glValidateProgram(getHandle());
        if ((int) glGetProgrami(getHandle(), GL_VALIDATE_STATUS) == GL_ZERO)
            throw new RendererProviderException("Unable to link Shader Program.\nCause: %s".formatted(glGetProgramInfoLog(getHandle())));
    }

    private int parse(ShaderType type) {
        return switch (type) {
            case VERTEX -> GL_VERTEX_SHADER;
            case FRAGMENT -> GL_FRAGMENT_SHADER;
            case GEOMETRY -> GL_GEOMETRY_SHADER;
            case COMPUTE -> GL_COMPUTE_SHADER;
            case TESSELATION_EVALUATION -> GL_TESS_EVALUATION_SHADER;
            case TESSELLATION_CONTROL -> GL_TESS_CONTROL_SHADER;
        };
    }

    @Override
    public void unbind() {
        glUseProgram(GL_ZERO);
    }

    @Override
    public void destroy() {
        if (!linked)
            return ;

        unbind();
        if (getHandle() != GL_ZERO)
            glDeleteProgram(getHandle());

        shaders.clear();
        linked = false;
    }
}
