package br.toe.engine.renderer.layer;

import br.toe.engine.event.*;
import br.toe.utils.*;

interface Base extends Lifecycle {

    String getName();

    void update ();
    void handle(Event event);

}
