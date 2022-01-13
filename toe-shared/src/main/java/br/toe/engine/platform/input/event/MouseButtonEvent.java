package br.toe.engine.platform.input.event;

import br.toe.engine.event.*;
import br.toe.engine.platform.input.*;
import lombok.*;

abstract class MouseButtonEvent extends MouseEvent {

    @Getter
    private final InputMouse button;

    protected MouseButtonEvent (EventType type, InputMouse button) {
        super(type);
        this.button = button;
    }
}
