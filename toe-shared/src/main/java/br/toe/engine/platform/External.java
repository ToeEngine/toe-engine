package br.toe.engine.platform;

public abstract class External<T> {

    private T handle;

    public final T getHandle() {
        return handle;
    }

    protected final void setHandle(T handle) {
        this.handle = handle;
    }
}
