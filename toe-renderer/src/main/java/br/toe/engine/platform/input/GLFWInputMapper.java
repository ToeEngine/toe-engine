package br.toe.engine.platform.input;

import br.toe.framework.cdi.annotation.*;

import static org.lwjgl.glfw.GLFW.*;

@Singleton
public final class GLFWInputMapper implements InputMapper {

    public InputMouse inputMouse(int keycode) {
        return switch (keycode) {
            case GLFW_MOUSE_BUTTON_LEFT -> InputMouse.BUTTON_LEFT;
            case GLFW_MOUSE_BUTTON_RIGHT -> InputMouse.BUTTON_RIGHT;
            case GLFW_MOUSE_BUTTON_MIDDLE -> InputMouse.BUTTON_MIDDLE;

            default -> throw new InputException("Unknown Mouse Button [%s]".formatted(keycode));
        };
    }

    public InputKey inputKey(int keycode) {
        return switch (keycode) {
            case GLFW_KEY_A -> InputKey.KEY_A;
            case GLFW_KEY_B -> InputKey.KEY_B;
            case GLFW_KEY_C -> InputKey.KEY_C;
            case GLFW_KEY_D -> InputKey.KEY_D;
            case GLFW_KEY_E -> InputKey.KEY_E;
            case GLFW_KEY_F -> InputKey.KEY_F;
            case GLFW_KEY_G -> InputKey.KEY_G;
            case GLFW_KEY_H -> InputKey.KEY_H;
            case GLFW_KEY_I -> InputKey.KEY_I;
            case GLFW_KEY_J -> InputKey.KEY_J;
            case GLFW_KEY_K -> InputKey.KEY_K;
            case GLFW_KEY_L -> InputKey.KEY_L;
            case GLFW_KEY_M -> InputKey.KEY_M;
            case GLFW_KEY_N -> InputKey.KEY_N;
            case GLFW_KEY_O -> InputKey.KEY_O;
            case GLFW_KEY_P -> InputKey.KEY_P;
            case GLFW_KEY_Q -> InputKey.KEY_Q;
            case GLFW_KEY_R -> InputKey.KEY_R;
            case GLFW_KEY_S -> InputKey.KEY_S;
            case GLFW_KEY_T -> InputKey.KEY_T;
            case GLFW_KEY_U -> InputKey.KEY_U;
            case GLFW_KEY_V -> InputKey.KEY_V;
            case GLFW_KEY_W -> InputKey.KEY_W;
            case GLFW_KEY_X -> InputKey.KEY_X;
            case GLFW_KEY_Y -> InputKey.KEY_Y;
            case GLFW_KEY_Z -> InputKey.KEY_Z;
            case GLFW_KEY_APOSTROPHE -> InputKey.KEY_APOSTROPHE;
            case GLFW_KEY_COMMA -> InputKey.KEY_COMMA;
            case GLFW_KEY_MINUS -> InputKey.KEY_MINUS;
            case GLFW_KEY_PERIOD -> InputKey.KEY_PERIOD;
            case GLFW_KEY_SLASH -> InputKey.KEY_SLASH;
            case GLFW_KEY_0 -> InputKey.KEY_0;
            case GLFW_KEY_1 -> InputKey.KEY_1;
            case GLFW_KEY_2 -> InputKey.KEY_2;
            case GLFW_KEY_3 -> InputKey.KEY_3;
            case GLFW_KEY_4 -> InputKey.KEY_4;
            case GLFW_KEY_5 -> InputKey.KEY_5;
            case GLFW_KEY_6 -> InputKey.KEY_6;
            case GLFW_KEY_7 -> InputKey.KEY_7;
            case GLFW_KEY_8 -> InputKey.KEY_8;
            case GLFW_KEY_9 -> InputKey.KEY_9;
            case GLFW_KEY_SEMICOLON -> InputKey.KEY_SEMICOLON;
            case GLFW_KEY_EQUAL -> InputKey.KEY_EQUAL;
            case GLFW_KEY_LEFT_BRACKET -> InputKey.KEY_LEFT_BRACKET;
            case GLFW_KEY_BACKSLASH -> InputKey.KEY_BACKSLASH;
            case GLFW_KEY_RIGHT_BRACKET -> InputKey.KEY_RIGHT_BRACKET;
            case GLFW_KEY_GRAVE_ACCENT -> InputKey.KEY_GRAVE_ACCENT;

            case GLFW_KEY_ESCAPE -> InputKey.KEY_ESCAPE;
            case GLFW_KEY_ENTER -> InputKey.KEY_ENTER;
            case GLFW_KEY_SPACE -> InputKey.KEY_SPACE;
            case GLFW_KEY_TAB -> InputKey.KEY_TAB;
            case GLFW_KEY_BACKSPACE -> InputKey.KEY_BACKSPACE;
            case GLFW_KEY_INSERT -> InputKey.KEY_INSERT;
            case GLFW_KEY_DELETE -> InputKey.KEY_DELETE;
            case GLFW_KEY_UP -> InputKey.KEY_UP;
            case GLFW_KEY_DOWN -> InputKey.KEY_DOWN;
            case GLFW_KEY_LEFT -> InputKey.KEY_LEFT;
            case GLFW_KEY_RIGHT -> InputKey.KEY_RIGHT;
            case GLFW_KEY_PAGE_UP -> InputKey.KEY_PAGE_UP;
            case GLFW_KEY_PAGE_DOWN -> InputKey.KEY_PAGE_DOWN;
            case GLFW_KEY_HOME -> InputKey.KEY_HOME;
            case GLFW_KEY_END -> InputKey.KEY_END;
            case GLFW_KEY_CAPS_LOCK -> InputKey.KEY_CAPS_LOCK;
            case GLFW_KEY_SCROLL_LOCK -> InputKey.KEY_SCROLL_LOCK;
            case GLFW_KEY_NUM_LOCK -> InputKey.KEY_NUM_LOCK;
            case GLFW_KEY_PRINT_SCREEN -> InputKey.KEY_PRINT_SCREEN;
            case GLFW_KEY_PAUSE -> InputKey.KEY_PAUSE;
            case GLFW_KEY_F1 -> InputKey.KEY_F1;
            case GLFW_KEY_F2 -> InputKey.KEY_F2;
            case GLFW_KEY_F3 -> InputKey.KEY_F3;
            case GLFW_KEY_F4 -> InputKey.KEY_F4;
            case GLFW_KEY_F5 -> InputKey.KEY_F5;
            case GLFW_KEY_F6 -> InputKey.KEY_F6;
            case GLFW_KEY_F7 -> InputKey.KEY_F7;
            case GLFW_KEY_F8 -> InputKey.KEY_F8;
            case GLFW_KEY_F9 -> InputKey.KEY_F9;
            case GLFW_KEY_F10 -> InputKey.KEY_F10;
            case GLFW_KEY_F11 -> InputKey.KEY_F11;
            case GLFW_KEY_F12 -> InputKey.KEY_F12;

            case GLFW_KEY_LEFT_ALT -> InputKey.KEY_LEFT_ALT;
            case GLFW_KEY_LEFT_SHIFT -> InputKey.KEY_LEFT_SHIFT;
            case GLFW_KEY_LEFT_CONTROL -> InputKey.KEY_LEFT_CTRL;
            case GLFW_KEY_RIGHT_ALT -> InputKey.KEY_RIGHT_ALT;
            case GLFW_KEY_RIGHT_CONTROL -> InputKey.KEY_RIGHT_CTRL;
            case GLFW_KEY_RIGHT_SHIFT -> InputKey.KEY_RIGHT_SHIFT;

            case GLFW_KEY_KP_0 -> InputKey.KEY_KP_0;
            case GLFW_KEY_KP_1 -> InputKey.KEY_KP_1;
            case GLFW_KEY_KP_2 -> InputKey.KEY_KP_2;
            case GLFW_KEY_KP_3 -> InputKey.KEY_KP_3;
            case GLFW_KEY_KP_4 -> InputKey.KEY_KP_4;
            case GLFW_KEY_KP_5 -> InputKey.KEY_KP_5;
            case GLFW_KEY_KP_6 -> InputKey.KEY_KP_6;
            case GLFW_KEY_KP_7 -> InputKey.KEY_KP_7;
            case GLFW_KEY_KP_8 -> InputKey.KEY_KP_8;
            case GLFW_KEY_KP_9 -> InputKey.KEY_KP_9;
            case GLFW_KEY_KP_DECIMAL -> InputKey.KEY_KP_DECIMAL;
            case GLFW_KEY_KP_DIVIDE -> InputKey.KEY_KP_DIVIDE;
            case GLFW_KEY_KP_MULTIPLY -> InputKey.KEY_KP_MULTIPLY;
            case GLFW_KEY_KP_SUBTRACT -> InputKey.KEY_KP_SUBTRACT;
            case GLFW_KEY_KP_ADD -> InputKey.KEY_KP_ADD;
            case GLFW_KEY_KP_ENTER -> InputKey.KEY_KP_ENTER;
            case GLFW_KEY_KP_EQUAL -> InputKey.KEY_KP_EQUAL;
            case GLFW_KEY_MENU -> InputKey.KEY_MENU;
            case GLFW_KEY_LEFT_SUPER -> InputKey.KEY_LEFT_SUPER;
            case GLFW_KEY_RIGHT_SUPER -> InputKey.KEY_RIGHT_SUPER;

            default -> throw new InputException("Unknown keycode [%s]".formatted(keycode));
        };
    }
}
