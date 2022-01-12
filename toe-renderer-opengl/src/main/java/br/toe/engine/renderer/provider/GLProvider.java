package br.toe.engine.renderer.provider;

import br.toe.engine.platform.imgui.*;
import br.toe.engine.platform.window.*;
import br.toe.engine.renderer.*;
import br.toe.engine.renderer.provider.opengl.*;
import br.toe.engine.renderer.provider.opengl.buffer.*;
import br.toe.engine.renderer.provider.opengl.shader.*;
import br.toe.framework.cdi.*;

public final class GLProvider implements RendererProvider {

    @Override
    public void initialize () {
        Registry.register(Window.class, GLWindow.class);
        Registry.register(RendererApi.class, GLRenderer.class);
        Registry.register(Mesh.class, VertexArrayObject.class);
        Registry.register(Shader.class, GLShader.class);
        Registry.register(ImGuiRenderer.class, ImGuiGLRenderer.class);
    }

    @Override
    public void destroy () {

    }
}
