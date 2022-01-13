package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;
import br.toe.engine.platform.input.*;

public final class KeyReleasedEvent extends KeyEvent {

    public KeyReleasedEvent (InputKey key) {
        super(EventType.INPUT_RELEASED, key);
    }
}
