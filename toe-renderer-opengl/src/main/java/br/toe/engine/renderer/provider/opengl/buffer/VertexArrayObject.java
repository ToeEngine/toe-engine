package br.toe.engine.renderer.provider.opengl.buffer;

import br.toe.engine.renderer.*;
import br.toe.engine.renderer.provider.*;
import br.toe.engine.renderer.provider.opengl.*;

import java.util.*;

import static org.lwjgl.opengl.GL46C.*;

public final class VertexArrayObject extends GLObject implements Mesh {

    private boolean initialized;
    private final List<ArrayBufferObject> objects = new ArrayList<>();
    private final List<ElementBufferObject> elements = new ArrayList<>();

    @Override
    public void initialize() {
        ensureNotInitialized();
        setHandle(glGenVertexArrays());
        glBindVertexArray(getHandle());

        for (var object : objects) {
            object.initialize();
            object.build();

            glEnableVertexAttribArray(object.position);
        }

        var swap = new int[0];
        var combined = new int[0];
        for (var object : elements) {
            object.initialize();

            swap = new int[combined.length];
            System.arraycopy(combined, 0, swap, 0, combined.length);

            combined = new int[combined.length + object.data.length];
            System.arraycopy(swap, 0, combined, 0, swap.length);
            System.arraycopy(object.data, 0, combined, swap.length, object.data.length);

            object.build();
        }

        elements.add(new ElementBufferObject("combined", combined));
        elements.get(elements.size() - 1).initialize();
        elements.get(elements.size() - 1).build();

        initialized = true;

        glBindBuffer(GL_ARRAY_BUFFER, GL_ZERO);
        glBindVertexArray(GL_ZERO);
    }
    
    @Override
    public void bind() {
        ensureInitialized();
        glBindVertexArray(getHandle());
    }

    public void add(int position, int length, float ... data) {
        add(position, new MemoryLayout(length, GL_FLOAT, Float.BYTES), data);
    }

    public void add(int position, MemoryLayout layout, float ... data) {
        ensureNotInitialized();
        objects.add(new ArrayBufferObject(position, layout, data));
    }

    public void add(String name, int ... data) {
        ensureNotInitialized();
        elements.add(new ElementBufferObject(name, data));
    }

    @Override
    public int use() {
        ensureInitialized();

        for (var element : elements)
            if (element.name.equals("combined")) {
                element.bind();
                return element.size;
            }

        throw new RendererProviderException("???");
    }

    @Override
    public int use(String name) {
        ensureInitialized();

        for (var element : elements)
            if (element.name.equals(name)) {
                element.bind();
                return element.size;
            }

        throw new RendererProviderException("Element [%s] not found".formatted(name));
    }

    private void ensureInitialized() {
        if (!initialized)
            throw new RendererProviderException("Mesh not initialized");
    }

    private void ensureNotInitialized() {
        if (initialized)
            throw new RendererProviderException("Mesh already initialized");
    }

    @Override
    public void unbind() {
        glBindVertexArray(GL_ZERO);
    }

    @Override
    public void destroy() {
        ensureInitialized();
        unbind();

        for (var object : objects)
            object.destroy();

        for (var object : elements)
            object.destroy();

        glDeleteVertexArrays(getHandle());
    }

}
