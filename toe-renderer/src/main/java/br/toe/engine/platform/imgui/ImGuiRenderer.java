package br.toe.engine.platform.imgui;

import br.toe.framework.cdi.*;
import br.toe.utils.*;
import imgui.*;

public interface ImGuiRenderer extends Lifecycle {

    static ImGuiRenderer get() {
        return Factory.create(ImGuiRenderer.class);
    }

    void render(ImDrawData data);

}
