package br.toe.engine.renderer.provider.opengl.executor;

public interface GLExecutor {

    <T> T call(String target, Object ... args);

}
