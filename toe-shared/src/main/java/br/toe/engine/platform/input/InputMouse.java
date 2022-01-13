package br.toe.engine.platform.input;

public enum InputMouse {
    BUTTON_LEFT(1),
    BUTTON_MIDDLE(2),
    BUTTON_RIGHT(3);

    private final int keycode;

    InputMouse(int keycode) {
        this.keycode = keycode;
    }

    public int getKeycode() {
        return keycode;
    }
}
