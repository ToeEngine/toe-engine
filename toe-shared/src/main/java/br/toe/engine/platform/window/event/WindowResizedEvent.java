package br.toe.engine.platform.window.event;

import br.toe.engine.event.*;
import lombok.*;
import org.joml.*;

public final class WindowResizedEvent extends WindowEvent {

    @Getter
    private final Vector2ic size;

    public WindowResizedEvent (int x, int y) {
        super(EventType.WINDOW_RESIZED);
        this.size = new Vector2i(x, y);
    }
}
