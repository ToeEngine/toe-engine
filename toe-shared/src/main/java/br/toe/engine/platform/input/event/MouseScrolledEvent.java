package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;
import lombok.*;
import org.joml.*;

public final class MouseScrolledEvent extends MouseEvent {

    @Getter
    private final Vector2fc position;

    public MouseScrolledEvent (float x, float y) {
        super(EventType.MOUSE_SCROLLED);
        this.position = new Vector2f(x, y);
    }
}
