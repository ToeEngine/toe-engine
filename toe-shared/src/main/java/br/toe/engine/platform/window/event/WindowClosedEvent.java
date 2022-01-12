package br.toe.engine.platform.window.event;

import br.toe.engine.event.*;

public final class WindowClosedEvent extends WindowEvent {

    public WindowClosedEvent () {
        super(EventType.WINDOW_CLOSED);
    }
}
