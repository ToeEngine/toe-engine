package br.toe.engine.renderer.provider.opengl.buffer;

import br.toe.engine.renderer.provider.*;
import br.toe.engine.renderer.provider.opengl.*;
import br.toe.utils.*;
import lombok.*;

import static org.lwjgl.opengl.GL46C.*;

@AllArgsConstructor
public class ArrayBufferObject extends GLObject implements Lifecycle {
    final int position;
    final MemoryLayout layout;
    float[] data;

    @Override
    public void initialize() {
        setHandle(glGenBuffers());
    }

    public void build() {
        glBindBuffer(GL_ARRAY_BUFFER, getHandle());
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
        glVertexAttribPointer(position, layout.getLength(), layout.getType(), false, layout.getStride(),  GL_ZERO);

        data = null;
    }

    @Override
    public void destroy() {
        if (getHandle() == null)
            throw new RendererProviderException("Buffer not initialized");

        glDeleteBuffers(getHandle());
    }
}
