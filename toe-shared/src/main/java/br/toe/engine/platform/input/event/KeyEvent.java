package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;
import br.toe.engine.platform.input.*;
import lombok.*;

abstract class KeyEvent extends Event {

    @Getter
    private final InputKey key;

    protected KeyEvent (EventType type, InputKey key) {
        super(type, EventCategory.INPUT, EventCategory.KEYBOARD);
        this.key = key;
    }
}
