package br.toe.engine.platform.window.event;

import br.toe.engine.event.*;

public final class WindowRestoredEvent extends WindowEvent {

    public WindowRestoredEvent () {
        super(EventType.WINDOW_RESTORED);
    }
}
