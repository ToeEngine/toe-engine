package br.toe.engine.renderer;

import br.toe.framework.cdi.*;
import br.toe.utils.*;

public interface Mesh extends Bindable, Lifecycle {

    static Mesh create() {
        return Factory.create(Mesh.class);
    }

    int use();
    int use(String element);
    void add(String name, int ... data);

    void add(int position, int length, float ... data);

}
