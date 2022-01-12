package br.toe.game;

import br.toe.engine.renderer.debug.*;

public final class Sandbox extends AbstractGame {

    public Sandbox() {
//        GameSettings.set("game.build", ReleaseType.PRODUCTION);
        push(new ImGuiLayer());
    }

}
