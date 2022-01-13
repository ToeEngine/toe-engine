package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;
import br.toe.engine.platform.input.*;

public final class MouseButtonPressedEvent extends MouseButtonEvent {
    public MouseButtonPressedEvent (InputMouse button) {
        super(EventType.INPUT_PRESSED, button);
    }
}
