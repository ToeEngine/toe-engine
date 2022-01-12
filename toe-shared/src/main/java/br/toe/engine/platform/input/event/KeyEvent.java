package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;
import lombok.*;

abstract class KeyEvent extends Event {

    @Getter
    private final int keycode;

    protected KeyEvent (EventType type, int keycode) {
        super(type, EventCategory.INPUT, EventCategory.KEYBOARD);
        this.keycode = keycode;
    }
}
