package br.toe.engine.platform.window.event;

import br.toe.engine.event.*;

public final class WindowIconifiedEvent extends WindowEvent {

    public WindowIconifiedEvent () {
        super(EventType.WINDOW_ICONIFIED);
    }
}
