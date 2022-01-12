package br.toe.engine.renderer.debug;

import br.toe.engine.event.*;
import br.toe.engine.platform.Platform;
import br.toe.engine.platform.*;
import br.toe.engine.platform.imgui.*;
import br.toe.engine.platform.input.event.*;
import br.toe.engine.platform.logger.*;
import br.toe.engine.platform.window.*;
import br.toe.engine.platform.window.event.*;
import br.toe.engine.renderer.layer.*;
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
        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
        keyMap[ImGuiKey.End] = GLFW_KEY_END;
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
        keyMap[ImGuiKey.A] = GLFW_KEY_A;
        keyMap[ImGuiKey.C] = GLFW_KEY_C;
        keyMap[ImGuiKey.V] = GLFW_KEY_V;
        keyMap[ImGuiKey.X] = GLFW_KEY_X;
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
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

        switch (e) {
            default -> {}
            case MouseMovedEvent event ->
                    io.setMousePos(event.getPosition().x(), event.getPosition().y());

            case MouseScrolledEvent event -> {
                io.setMouseWheelH(io.getMouseWheelH() + event.getPosition().x());
                io.setMouseWheel(io.getMouseWheel() + event.getPosition().y());
            }

            case MouseButtonPressedEvent event ->
                io.setMouseDown(event.getKeycode(), true);

            case MouseButtonReleasedEvent event ->
                io.setMouseDown(event.getKeycode(), false);

            case WindowResizedEvent event -> {
                final var size = event.getSize();
                io.setDisplaySize(size.x(), size.y());
                io.setDisplayFramebufferScale(1f, 1f);
            }

            case WindowClosedEvent event -> {

            }

            case KeyPressedEvent event -> {
                io.setKeysDown(event.getKeycode(), true);
                io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
                io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
                io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
                io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
            }

            case KeyReleasedEvent event ->
                io.setKeysDown(event.getKeycode(), false);

            case KeyTypedEvent event -> {
                final var keycode = event.getKeycode();

                if (keycode > 0 && keycode < 0x10000)
                    io.addInputCharacter(keycode);
            }
        }
    }

    @Override
    protected void doDestroy () {
        renderer.destroy();
    }
}

