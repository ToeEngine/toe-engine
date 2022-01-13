package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;
import lombok.*;

public final class KeyTypedEvent extends Event {

    @Getter
    private int codepoint;

    public KeyTypedEvent (int codepoint) {
        super(EventType.KEY_TYPED);

        this.codepoint = codepoint;
    }
}
