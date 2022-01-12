package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;

public final class MouseButtonReleasedEvent extends MouseButtonEvent {
    public MouseButtonReleasedEvent (int keycode) {
        super(EventType.INPUT_RELEASED, keycode);
    }
}
