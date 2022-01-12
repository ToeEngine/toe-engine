package br.toe.engine.platform.imgui;

import br.toe.engine.platform.*;
import br.toe.engine.renderer.*;
import br.toe.engine.renderer.provider.opengl.*;
import br.toe.utils.*;
import imgui.*;
import imgui.callback.*;
import imgui.flag.*;
import imgui.type.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL46C.*;

public final class ImGuiGLRenderer extends GLObject implements ImGuiRenderer {
    private ImGuiIO io;
    private Shader shader;
    private GLCapabilities capabilities;
    private int gFontTexture = 0;

    @Override
    public void initialize () {
        capabilities = GL.getCapabilities();
        shader = Shader.create();

        io = ImGui.getIO();
        io.setBackendRendererName(getClass().getSimpleName());

        // We can honor the ImDrawCmd::VtxOffset field, allowing for large meshes.
        if (capabilities.OpenGL32)
            io.addBackendFlags(ImGuiBackendFlags.RendererHasVtxOffset);

        createObjects();

        // We can create multi-viewports on the Renderer side (optional)
        io.addBackendFlags(ImGuiBackendFlags.RendererHasViewports);

        if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable))
            ImGui.getPlatformIO().setRendererRenderWindow(new ImPlatformFuncViewport() {
                @Override
                public void accept(final ImGuiViewport viewport) {
                    if ( !viewport.hasFlags(ImGuiViewportFlags.NoRendererClear) ) {
                        GL46C.glClearColor(0, 0, 0, 0);
                        GL46C.glClear(GL_COLOR_BUFFER_BIT);
                    }

                    render(viewport.getDrawData());
                }
            });
    }

    private void createObjects() {
        // Backup GL state
        final var lastTexture = new int[1];
        final var lastArrayBuffer = new int[1];
        final var lastVertexArray = new int[1];
        glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture);
        glGetIntegerv(GL_ARRAY_BUFFER_BINDING, lastArrayBuffer);
        glGetIntegerv(GL_VERTEX_ARRAY_BINDING, lastVertexArray);

        initializeShader();
        updateFontTexture();

        // Create buffers
        gVboHandle = glGenBuffers();
        gElementsHandle = glGenBuffers();

        // Restore modified GL state
        glBindTexture(GL_TEXTURE_2D, lastTexture[0]);
        glBindBuffer(GL_ARRAY_BUFFER, lastArrayBuffer[0]);
        glBindVertexArray(lastVertexArray[0]);
    }

    // GLData
    private int gAttribLocationTex = 0;
    private int gAttribLocationProjMtx = 0;
    private int gAttribLocationVtxPos = 0;
    private int gAttribLocationVtxUV = 0;
    private int gAttribLocationVtxColor = 0;
    private void initializeShader() {
        if (capabilities.OpenGL41) {
            shader.attach(ShaderType.VERTEX, CurrentThread.read("shader/imgui-v410-vertex.glsl"));
            shader.attach(ShaderType.FRAGMENT, CurrentThread.read("shader/imgui-v410-fragment.glsl"));
        } else {
            shader.attach(ShaderType.VERTEX, CurrentThread.read("shader/imgui-v130-vertex.glsl"));
            shader.attach(ShaderType.FRAGMENT, CurrentThread.read("shader/imgui-v130-fragment.glsl"));
        }

        shader.initialize();
        final var gShaderHandle = ((External<Integer>) shader).getHandle();
        gAttribLocationTex = glGetUniformLocation(gShaderHandle, "Texture");
        gAttribLocationProjMtx = glGetUniformLocation(gShaderHandle, "ProjMtx");
        gAttribLocationVtxPos = glGetAttribLocation(gShaderHandle, "Position");
        gAttribLocationVtxUV = glGetAttribLocation(gShaderHandle, "UV");
        gAttribLocationVtxColor = glGetAttribLocation(gShaderHandle, "Color");

    }

    // Used to store tmp renderer data
    private final ImVec4 clipRect = new ImVec4();
    private final ImVec2 framebufferScale = new ImVec2();

    @Override
    public void render (ImDrawData data) {
        if (data.getCmdListsCount() <= 0)
            return;

        // Will project scissor/clipping rectangles into framebuffer space
        data.getDisplaySize(displaySize);           // (0,0) unless using multi-viewports
        data.getFramebufferScale(framebufferScale); // (1,1) unless using retina display which are often (2,2)

        // Avoid rendering when minimized, scale coordinates for retina displays (screen coordinates != framebuffer coordinates)
        final var fbWidth = (int) (displaySize.x * framebufferScale.x);
        final var fbHeight = (int) (displaySize.y * framebufferScale.y);

        if (fbWidth <= 0 || fbHeight <= 0)
            return;

        backupGlState();
        bind(fbWidth, fbHeight);

        // Render command lists
        for (int i = 0; i < data.getCmdListsCount(); i++) {
            // Upload vertex/index buffers
            glBufferData(GL_ARRAY_BUFFER, data.getCmdListVtxBufferData(i), GL_STREAM_DRAW);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, data.getCmdListIdxBufferData(i), GL_STREAM_DRAW);

            draw(data, i, fbHeight);
        }

        unbind();
        restoreModifiedGlState();
    }

    private void draw(ImDrawData data, int command, int fbHeight) {
        data.getDisplayPos(displayPos);

        final var clipOffX = displayPos.x;
        final var clipOffY = displayPos.y;
        final var clipScaleX = framebufferScale.x;
        final var clipScaleY = framebufferScale.y;

        for (int j = 0; j < data.getCmdListCmdBufferSize(command); j++) {
            data.getCmdListCmdBufferClipRect(command, j, clipRect);

            final var clipMinX = (clipRect.x - clipOffX) * clipScaleX;
            final var clipMinY = (clipRect.y - clipOffY) * clipScaleY;
            final var clipMaxX = (clipRect.z - clipOffX) * clipScaleX;
            final var clipMaxY = (clipRect.w - clipOffY) * clipScaleY;

            if (clipMaxX <= clipMinX || clipMaxY <= clipMinY)
                continue;

            // Apply scissor/clipping rectangle (Y is inverted in OpenGL)
            glScissor((int) clipMinX, (int) (fbHeight - clipMaxY), (int) (clipMaxX - clipMinX), (int) (clipMaxY - clipMinY));

            // Bind texture, Draw

            glBindTexture(GL_TEXTURE_2D, data.getCmdListCmdBufferTextureId(command, j));

            final var elemCount = data.getCmdListCmdBufferElemCount(command, j);
            final var indices = data.getCmdListCmdBufferIdxOffset(command, j) * ImDrawData.SIZEOF_IM_DRAW_IDX;
            if (capabilities.OpenGL32)
                glDrawElementsBaseVertex(GL_TRIANGLES, elemCount, GL_UNSIGNED_SHORT, (long) indices, data.getCmdListCmdBufferVtxOffset(command, j));

            else
                glDrawElements(GL_TRIANGLES, elemCount, GL_UNSIGNED_SHORT, (long) indices);
        }
    }

    // Variables used to backup GL state before and after the rendering of Dear ImGui
    private final int[] lastActiveTexture = new int[1];
    private final int[] lastProgram = new int[1];
    private final int[] lastTexture = new int[1];
    private final int[] lastArrayBuffer = new int[1];
    private final int[] lastVertexArrayObject = new int[1];
    private final int[] lastViewport = new int[4];
    private final int[] lastScissorBox = new int[4];
    private final int[] lastBlendSrcRgb = new int[1];
    private final int[] lastBlendDstRgb = new int[1];
    private final int[] lastBlendSrcAlpha = new int[1];
    private final int[] lastBlendDstAlpha = new int[1];
    private final int[] lastBlendEquationRgb = new int[1];
    private final int[] lastBlendEquationAlpha = new int[1];
    private boolean lastEnableBlend = false;
    private boolean lastEnableCullFace = false;
    private boolean lastEnableDepthTest = false;
    private boolean lastEnableStencilTest = false;
    private boolean lastEnableScissorTest = false;

    private void backupGlState() {
        glGetIntegerv(GL_ACTIVE_TEXTURE, lastActiveTexture);
        glActiveTexture(GL_TEXTURE0);
        glGetIntegerv(GL_CURRENT_PROGRAM, lastProgram);
        glGetIntegerv(GL_TEXTURE_BINDING_2D, lastTexture);
        glGetIntegerv(GL_ARRAY_BUFFER_BINDING, lastArrayBuffer);
        glGetIntegerv(GL_VERTEX_ARRAY_BINDING, lastVertexArrayObject);
        glGetIntegerv(GL_VIEWPORT, lastViewport);
        glGetIntegerv(GL_SCISSOR_BOX, lastScissorBox);
        glGetIntegerv(GL_BLEND_SRC_RGB, lastBlendSrcRgb);
        glGetIntegerv(GL_BLEND_DST_RGB, lastBlendDstRgb);
        glGetIntegerv(GL_BLEND_SRC_ALPHA, lastBlendSrcAlpha);
        glGetIntegerv(GL_BLEND_DST_ALPHA, lastBlendDstAlpha);
        glGetIntegerv(GL_BLEND_EQUATION_RGB, lastBlendEquationRgb);
        glGetIntegerv(GL_BLEND_EQUATION_ALPHA, lastBlendEquationAlpha);
        lastEnableBlend = glIsEnabled(GL_BLEND);
        lastEnableCullFace = glIsEnabled(GL_CULL_FACE);
        lastEnableDepthTest = glIsEnabled(GL_DEPTH_TEST);
        lastEnableStencilTest = glIsEnabled(GL_STENCIL_TEST);
        lastEnableScissorTest = glIsEnabled(GL_SCISSOR_TEST);
    }

    private void restoreModifiedGlState() {
        glUseProgram(lastProgram[0]);
        glBindTexture(GL_TEXTURE_2D, lastTexture[0]);
        glActiveTexture(lastActiveTexture[0]);
        glBindVertexArray(lastVertexArrayObject[0]);
        glBindBuffer(GL_ARRAY_BUFFER, lastArrayBuffer[0]);
        glBlendEquationSeparate(lastBlendEquationRgb[0], lastBlendEquationAlpha[0]);
        glBlendFuncSeparate(lastBlendSrcRgb[0], lastBlendDstRgb[0], lastBlendSrcAlpha[0], lastBlendDstAlpha[0]);
        // @formatter:off CHECKSTYLE:OFF
        if (lastEnableBlend) glEnable(GL_BLEND); else glDisable(GL_BLEND);
        if (lastEnableCullFace) glEnable(GL_CULL_FACE); else glDisable(GL_CULL_FACE);
        if (lastEnableDepthTest) glEnable(GL_DEPTH_TEST); else glDisable(GL_DEPTH_TEST);
        if (lastEnableStencilTest) glEnable(GL_STENCIL_TEST); else glDisable(GL_STENCIL_TEST);
        if (lastEnableScissorTest) glEnable(GL_SCISSOR_TEST); else glDisable(GL_SCISSOR_TEST);
        // @formatter:on CHECKSTYLE:ON
        glViewport(lastViewport[0], lastViewport[1], lastViewport[2], lastViewport[3]);
        glScissor(lastScissorBox[0], lastScissorBox[1], lastScissorBox[2], lastScissorBox[3]);
    }


    // Used to store tmp renderer data
    private final ImVec2 displaySize = new ImVec2();
    private final ImVec2 displayPos = new ImVec2();
    private final float[] orthoProjMatrix = new float[4 * 4];

    // OpenGL Data
    private int gVertexArrayObjectHandle = 0;
    private int gVboHandle = 0;
    private int gElementsHandle = 0;

    // Setup desired GL state
    private void bind(final int fbWidth, final int fbHeight) {
        // Recreate the VAO every time (this is to easily allow multiple GL contexts to be rendered to. VAO are not shared among GL contexts)
        // The renderer would actually work without any VAO bound, but then our VertexAttrib calls would overwrite the default one currently bound.
        gVertexArrayObjectHandle = glGenVertexArrays();

        // Setup render state: alpha-blending enabled, no face culling, no depth testing, scissor enabled, polygon fill
        glEnable(GL_BLEND);
        glBlendEquation(GL_FUNC_ADD);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_STENCIL_TEST);
        glEnable(GL_SCISSOR_TEST);

        // Setup viewport, orthographic projection matrix
        // Our visible imgui space lies from draw_data->DisplayPos (top left) to draw_data->DisplayPos+data_data->DisplaySize (bottom right).
        // DisplayPos is (0,0) for single viewport apps.
        glViewport(0, 0, fbWidth, fbHeight);
        final float left = displayPos.x;
        final float right = displayPos.x + displaySize.x;
        final float top = displayPos.y;
        final float bottom = displayPos.y + displaySize.y;

        // Orthographic matrix projection
        orthoProjMatrix[0] = 2.0f / (right - left);
        orthoProjMatrix[5] = 2.0f / (top - bottom);
        orthoProjMatrix[10] = -1.0f;
        orthoProjMatrix[12] = (right + left) / (left - right);
        orthoProjMatrix[13] = (top + bottom) / (bottom - top);
        orthoProjMatrix[15] = 1.0f;

        // Bind shader
        shader.bind();
        glUniform1i(gAttribLocationTex, 0);
        glUniformMatrix4fv(gAttribLocationProjMtx, false, orthoProjMatrix);

        glBindVertexArray(gVertexArrayObjectHandle);

        // Bind vertex/index buffers and setup attributes for ImDrawVert
        glBindBuffer(GL_ARRAY_BUFFER, gVboHandle);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, gElementsHandle);
        glEnableVertexAttribArray(gAttribLocationVtxPos);
        glEnableVertexAttribArray(gAttribLocationVtxUV);
        glEnableVertexAttribArray(gAttribLocationVtxColor);
        glVertexAttribPointer(gAttribLocationVtxPos, 2, GL_FLOAT, false, ImDrawData.SIZEOF_IM_DRAW_VERT, 0L);
        glVertexAttribPointer(gAttribLocationVtxUV, 2, GL_FLOAT, false, ImDrawData.SIZEOF_IM_DRAW_VERT, 8L);
        glVertexAttribPointer(gAttribLocationVtxColor, 4, GL_UNSIGNED_BYTE, true, ImDrawData.SIZEOF_IM_DRAW_VERT, 16L);

    }

    private void unbind() {
        // Destroy the temporary VAO
        glDeleteVertexArrays(gVertexArrayObjectHandle);
    }

    public void updateFontTexture () {
        glDeleteTextures(gFontTexture);

        gFontTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, gFontTexture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        final var atlas = io.getFonts();
        final var width = new ImInt();
        final var height = new ImInt();
        final var buffer = atlas.getTexDataAsRGBA32(width, height);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        atlas.setTexID(gFontTexture);
    }

    @Override
    public void destroy () {

    }
}
