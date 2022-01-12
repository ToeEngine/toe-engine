package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;

public final class KeyTypedEvent extends KeyEvent {

    public KeyTypedEvent (int keycode) {
        super(EventType.KEY_TYPED, keycode);
    }
}
