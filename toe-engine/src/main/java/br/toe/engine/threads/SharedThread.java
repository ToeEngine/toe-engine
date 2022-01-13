package br.toe.engine.threads;

import br.toe.utils.*;

abstract class SharedThread extends ToeThread {

    protected static volatile int frame;
    private long nanos = 0L;

    protected abstract void perFrame();
    protected abstract void perSecond();

    @Override
    protected final void doUpdate () {
        perFrame();

        nanos += delta;
        if (nanos >= Time.NANOS) {
            perSecond();
            nanos = 0;
        }
    }
}
