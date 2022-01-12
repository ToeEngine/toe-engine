package br.toe.engine.platform.window;

import br.toe.framework.cdi.*;
import org.joml.*;

public interface Monitor {

    static Monitor get () {
        return Factory.create(Monitor.class);
    }

    String getName();
    int getRefreshRate();
    Vector3ic getColorBits();

    Vector2ic getSize();
    Vector2fc getScale();
    Vector2ic getPosition();
    Vector2ic getResolution();

}
