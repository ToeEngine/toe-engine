package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;

public final class KeyReleasedEvent extends KeyEvent {

    public KeyReleasedEvent (int keycode) {
        super(EventType.INPUT_RELEASED, keycode);
    }
}
