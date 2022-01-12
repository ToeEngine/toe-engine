package br.toe.engine.platform.window.event;

import br.toe.engine.event.*;

public final class WindowFocusLostEvent extends WindowEvent {

    public WindowFocusLostEvent () {
        super(EventType.WINDOW_FOCUS_LOST);
    }
}
