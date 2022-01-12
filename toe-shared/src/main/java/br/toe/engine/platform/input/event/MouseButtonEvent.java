package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;
import lombok.*;

abstract class MouseButtonEvent extends MouseEvent {

    @Getter
    private final int keycode;

    protected MouseButtonEvent (EventType type, int keycode) {
        super(type);
        this.keycode = keycode;
    }
}
