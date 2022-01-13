package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;
import br.toe.engine.platform.input.*;

public final class KeyPressedEvent extends KeyEvent {

    public KeyPressedEvent (InputKey key) {
        super(EventType.INPUT_PRESSED, key);
    }
}
