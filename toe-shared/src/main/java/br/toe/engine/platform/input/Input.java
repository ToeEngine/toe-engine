package br.toe.engine.platform.input;

import br.toe.framework.cdi.*;
import org.joml.*;

public interface Input {

    static Input get() {
        return Factory.create(Input.class);
    }

    boolean isKeyPressed(int keycode);
    boolean isButtonPressed(int button);
    Vector2fc getMousePosition();
}
