package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;

abstract class MouseEvent extends Event {

    protected MouseEvent (EventType type) {
        super(type, EventCategory.INPUT, EventCategory.MOUSE);
    }
}
