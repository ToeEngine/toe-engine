package br.toe.engine.event;

import br.toe.framework.cdi.*;
import br.toe.framework.reflection.*;

public interface EventBus {

    static EventBus get () { return Factory.create(EventBus.class); }

    void post(Event event);
    <T extends Event> void callback(Class<T> event, Callback callback);
    <T extends Event> void callback(Class<T> event, EventProcessor<T> processor);
    void callback(EventProcessor<?> processor);
}
