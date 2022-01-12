package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;

public final class MouseButtonPressedEvent extends MouseButtonEvent {
    public MouseButtonPressedEvent (int keycode) {
        super(EventType.INPUT_PRESSED, keycode);
    }
}
