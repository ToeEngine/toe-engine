package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;
import br.toe.engine.platform.input.*;

public final class MouseButtonReleasedEvent extends MouseButtonEvent {
    public MouseButtonReleasedEvent (InputMouse button) {
        super(EventType.INPUT_RELEASED, button);
    }
}
