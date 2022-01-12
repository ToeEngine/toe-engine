package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;
import lombok.*;
import org.joml.*;

public final class MouseMovedEvent extends MouseEvent {

    @Getter
    private final Vector2fc position;

    public MouseMovedEvent (float x, float y) {
        super(EventType.MOUSE_MOVED);
        this.position = new Vector2f(x, y);
    }
}
