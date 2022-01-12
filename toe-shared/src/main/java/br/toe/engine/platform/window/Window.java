package br.toe.engine.platform.window;

import br.toe.framework.cdi.*;
import br.toe.utils.*;
import org.joml.*;

public interface Window extends Lifecycle {

    static Window get () {
        return Factory.create(Window.class);
    }

    String getTitle();
    void setTitle(String title);

    Vector2ic getSize();
    Vector2ic getPosition();

    void show();
    void hide();

    void refresh();
}
