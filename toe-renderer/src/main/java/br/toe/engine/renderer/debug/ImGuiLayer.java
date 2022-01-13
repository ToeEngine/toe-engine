package br.toe.engine.renderer.debug;

import br.toe.engine.event.*;
import br.toe.engine.platform.Platform;
import br.toe.engine.platform.*;
import br.toe.engine.platform.imgui.*;
import br.toe.engine.platform.input.*;
import br.toe.engine.platform.input.event.*;
import br.toe.engine.platform.window.*;
import br.toe.engine.platform.window.event.*;
import br.toe.engine.renderer.layer.*;
import br.toe.framework.logging.*;
import imgui.*;
import imgui.flag.*;
import org.lwjgl.glfw.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.GLFW.*;

public class ImGuiLayer extends Layer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImGuiLayer.class);

    private final ImGuiRenderer renderer = ImGuiRenderer.get();
    private Window window;
    private long windowPtr;

    private ImGuiIO io;

    private boolean hasNewCursors;
    private boolean hasKeyName;
    private boolean hasWindowTopmost;
    private boolean hasWindowAlpha;
    private boolean hasPerMonitorDpi;
    private boolean hasFocusWindow;
    private boolean hasFocusOnShow;
    private boolean hasMonitorWorkArea;

    @Override
    public String getName () {
        return "ImGui";
    }

    @Override
    protected void doInitialize () {
        ImGui.createContext();
        ImGui.styleColorsDark();

        final int version;
        try (var stack = MemoryStack.stackPush()){
            final var major = stack.mallocInt(1);
            final var minor = stack.mallocInt(1);
            final var revision = stack.mallocInt(1);

            glfwGetVersion(major, minor, revision);
            version = major.get() * 1000 + minor.get() * 100 + revision.get() * 10;
        }

        if (version >= 3200) {
            hasKeyName = true;
            hasWindowTopmost = true;
            hasFocusWindow = true;
        }

        if (version >= 3300) {
            hasWindowAlpha = true;
            hasPerMonitorDpi = true;
            hasFocusOnShow = true;
            hasMonitorWorkArea = true;
        }

        hasNewCursors = version >= 3400;

        io = ImGui.getIO();
        io.setBackendRendererName("TOE Imgui Renderer");
        io.setBackendFlags(io.getBackendFlags() | ImGuiBackendFlags.HasMouseCursors); // We can honor GetMouseCursor() values (optional)
        io.setBackendFlags(io.getBackendFlags() | ImGuiBackendFlags.HasSetMousePos); // We can honor io.WantSetMousePos requests (optional, rarely used)

        window = Window.get();
        final var size = window.getSize();
        io.setDisplaySize(size.x(), size.y());

        final var viewport = ImGui.getMainViewport();
        windowPtr = ((External<Long>) window).getHandle();
        viewport.setPlatformHandle(windowPtr);

        if (Platform.IS_WINDOWS)
            viewport.setPlatformHandleRaw(GLFWNativeWin32.glfwGetWin32Window(windowPtr));

        // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
        final int[] keyMap = new int[ImGuiKey.COUNT];
        keyMap[ImGuiKey.Tab] = InputKey.KEY_TAB.getKeycode();
        keyMap[ImGuiKey.LeftArrow] = InputKey.KEY_LEFT.getKeycode();
        keyMap[ImGuiKey.RightArrow] = InputKey.KEY_RIGHT.getKeycode();
        keyMap[ImGuiKey.UpArrow] = InputKey.KEY_UP.getKeycode();
        keyMap[ImGuiKey.DownArrow] = InputKey.KEY_DOWN.getKeycode();
        keyMap[ImGuiKey.PageUp] = InputKey.KEY_KP_9.getKeycode();
        keyMap[ImGuiKey.PageDown] = InputKey.KEY_KP_3.getKeycode();
        keyMap[ImGuiKey.Home] = InputKey.KEY_HOME.getKeycode();
        keyMap[ImGuiKey.End] = InputKey.KEY_END.getKeycode();
        keyMap[ImGuiKey.Insert] = InputKey.KEY_INSERT.getKeycode();
        keyMap[ImGuiKey.Delete] = InputKey.KEY_DELETE.getKeycode();
        keyMap[ImGuiKey.Backspace] = InputKey.KEY_BACKSPACE.getKeycode();
        keyMap[ImGuiKey.Space] = InputKey.KEY_SPACE.getKeycode();
        keyMap[ImGuiKey.Enter] = InputKey.KEY_ENTER.getKeycode();
        keyMap[ImGuiKey.Escape] = InputKey.KEY_ESCAPE.getKeycode();
        keyMap[ImGuiKey.KeyPadEnter] = InputKey.KEY_KP_ENTER.getKeycode();
        keyMap[ImGuiKey.A] = InputKey.KEY_A.getKeycode();
        keyMap[ImGuiKey.C] = InputKey.KEY_C.getKeycode();
        keyMap[ImGuiKey.V] = InputKey.KEY_V.getKeycode();
        keyMap[ImGuiKey.X] = InputKey.KEY_X.getKeycode();
        keyMap[ImGuiKey.Y] = InputKey.KEY_Y.getKeycode();
        keyMap[ImGuiKey.Z] = InputKey.KEY_Z.getKeycode();
        io.setKeyMap(keyMap);

        renderer.initialize();
    }

    @Override
    public void update () {
        ImGui.newFrame();

        ImGui.showDemoWindow();

        ImGui.render();
        renderer.render(ImGui.getDrawData());
    }

    @Override
    public void handle (Event e) {
        if (glfwGetWindowAttrib(windowPtr, GLFW_FOCUSED) == GLFW_FALSE)
            return;

        switch (e.getClass().getSimpleName()) {
            default -> {}
            case "MouseMovedEvent"  -> {
                final var event = (MouseMovedEvent) e;

                io.setMousePos(event.getPosition().x(), event.getPosition().y());
            }

            case "MouseScrolledEvent" -> {
                final var event = (MouseScrolledEvent) e;

                io.setMouseWheelH(io.getMouseWheelH() + event.getPosition().x());
                io.setMouseWheel(io.getMouseWheel() + event.getPosition().y());
            }

            case "MouseButtonPressedEvent" -> {
                final var event = (MouseButtonPressedEvent) e;

                io.setMouseDown(switch (event.getButton()) {
                    case BUTTON_LEFT -> 0;
                    case BUTTON_RIGHT -> 1;
                    case BUTTON_MIDDLE -> 2;
                }, true);
            }

            case "MouseButtonReleasedEvent" -> {
                final var event = (MouseButtonReleasedEvent) e;

                io.setMouseDown(switch (event.getButton()) {
                    case BUTTON_LEFT -> 0;
                    case BUTTON_RIGHT -> 1;
                    case BUTTON_MIDDLE -> 2;
                }, false);
            }

            case "WindowResizedEvent" -> {
                final var event = (WindowResizedEvent) e;

                final var size = event.getSize();
                io.setDisplaySize(size.x(), size.y());
                io.setDisplayFramebufferScale(1f, 1f);
            }

            case "KeyPressedEvent" -> {
                final var event = (KeyPressedEvent) e;

                io.setKeysDown(event.getKey().getKeycode(), true);
                io.setKeyCtrl(io.getKeysDown(InputKey.KEY_LEFT_CTRL.getKeycode()) || io.getKeysDown(InputKey.KEY_RIGHT_CTRL.getKeycode()));
                io.setKeyShift(io.getKeysDown(InputKey.KEY_LEFT_SHIFT.getKeycode()) || io.getKeysDown(InputKey.KEY_RIGHT_SHIFT.getKeycode()));
                io.setKeyAlt(io.getKeysDown(InputKey.KEY_LEFT_ALT.getKeycode()) || io.getKeysDown(InputKey.KEY_RIGHT_ALT.getKeycode()));
                io.setKeySuper(io.getKeysDown(InputKey.KEY_LEFT_SUPER.getKeycode()) || io.getKeysDown(InputKey.KEY_RIGHT_SUPER.getKeycode()));
            }

            case "KeyReleasedEvent" -> {
                final var event = (KeyReleasedEvent) e;

                io.setKeysDown(event.getKey().getKeycode(), false);
            }

            case "KeyTypedEvent" -> {
                final var event = (KeyTypedEvent) e;

                final var codepoint = event.getCodepoint();
                if (codepoint > 0 && codepoint < 0x10000)
                    io.addInputCharacter(codepoint);
            }
        }
    }

    @Override
    protected void doDestroy () {
        renderer.destroy();
    }
}

