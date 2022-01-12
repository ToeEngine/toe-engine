package br.toe.engine.platform.window.event;

import br.toe.engine.event.*;

public final class WindowMaximizedEvent extends WindowEvent {

    public WindowMaximizedEvent () {
        super(EventType.WINDOW_MAXIMIZED);
    }
}
