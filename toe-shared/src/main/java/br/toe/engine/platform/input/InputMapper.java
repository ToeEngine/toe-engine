package br.toe.engine.platform.input;

import br.toe.framework.cdi.*;

public interface InputMapper {

    static InputMapper get() {
        return Factory.create(InputMapper.class);
    }

    InputMouse inputMouse(int keycode);
    InputKey inputKey(int keycode);

}
