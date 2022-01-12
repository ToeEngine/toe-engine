package br.toe.engine.platform.window.event;

import br.toe.engine.event.*;

public final class WindowFocusGainedEvent extends WindowEvent {

    public WindowFocusGainedEvent () {
        super(EventType.WINDOW_FOCUS_GAINED);
    }
}
