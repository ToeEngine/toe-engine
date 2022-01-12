package br.toe.engine.renderer.provider.opengl.buffer;

import br.toe.engine.renderer.provider.*;
import br.toe.engine.renderer.provider.opengl.*;
import br.toe.utils.*;
import lombok.*;

import static org.lwjgl.opengl.GL46C.*;

@AllArgsConstructor
public class ElementBufferObject extends GLObject implements Lifecycle {

    final String name;
    int[] data;
    final int size;

    public ElementBufferObject(String name, int ... data) {
        this.name = name;
        this.data = data;

        size = data.length;
    }

    @Override
    public void initialize() {
        setHandle(glGenBuffers());
    }

    public void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, getHandle());
    }

    public void build() {
        bind();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);

        data = null;
    }

    @Override
    public void destroy() {
        if (getHandle() == null)
            throw new RendererProviderException("Buffer not initialized");

        glDeleteBuffers(getHandle());
    }
}
