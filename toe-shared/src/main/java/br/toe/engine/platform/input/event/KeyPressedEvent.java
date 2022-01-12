package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;

public final class KeyPressedEvent extends KeyEvent {

    public KeyPressedEvent (int keycode) {
        super(EventType.INPUT_PRESSED, keycode);
    }
}
