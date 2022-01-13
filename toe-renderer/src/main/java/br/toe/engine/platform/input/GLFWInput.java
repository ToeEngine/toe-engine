package br.toe.engine.platform.input;

import br.toe.engine.platform.*;
import br.toe.engine.platform.window.*;
import br.toe.framework.cdi.annotation.*;
import org.joml.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.GLFW.*;

@Singleton
public final class GLFWInput implements Input {

    @Override
    public boolean isKeyPressed (int keycode) {
        final var state = glfwGetKey(getWindowPtr(), keycode);
        return state == GLFW_PRESS || state == GLFW_REPEAT;
    }

    @Override
    public boolean isButtonPressed (int keycode) {
        final var state = glfwGetKey(getWindowPtr(), keycode);
        return state == GLFW_PRESS || state == GLFW_REPEAT;
    }

    @Override
    public Vector2fc getMousePosition () {
        final var position = new Vector2f();

        try (var stack = MemoryStack.stackPush()){
            final var px = stack.mallocDouble(1);
            final var py = stack.mallocDouble(1);

            glfwGetCursorPos(getWindowPtr(), px, py);
            position.set((float) px.get(), (float) py.get());
        }

        return position;
    }

    @SuppressWarnings("unchecked")
    private long getWindowPtr() {
        final var window = Window.get();
        return ((External<Long>) window).getHandle();
    }
}
