package br.toe.engine.renderer.provider.opengl.buffer;

import lombok.*;

@Getter
@AllArgsConstructor
public final class MemoryLayout {

    private final int length;
    private final int type;
    private final int size;

    public int getStride() {
        return length * size;
    }
}
