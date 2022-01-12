package br.toe.engine.platform.window.event;

import br.toe.engine.event.*;

abstract class WindowEvent extends Event {

    protected WindowEvent (EventType type) {
        super(type, EventCategory.APPLICATION);
    }

}
