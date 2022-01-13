package br.toe.engine.event;

import br.toe.framework.cdi.annotation.*;
import br.toe.framework.logging.*;
import br.toe.framework.reflection.*;

import java.util.*;
import java.util.concurrent.*;

@Singleton
public final class DefaultEventBus implements EventBus {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEventBus.class);

    private final List<Callback> global = new ArrayList<>();
    private final Map<Class<? extends Event>, List<Callback>> callbacks = new ConcurrentHashMap<>();

    @Override
    public void post (Event event) {
        final var type = event.getClass();
        if (callbacks.containsKey(type))
            for (var callback : callbacks.get(type))
                if (handle(event, callback))
                    break;

        if (event.isHandled())
            return;

        for (var callback : global)
            if (handle(event, callback))
                break;
    }

    private boolean handle(Event event, Callback callback) {
        final var type = event.getClass();
        final var target = callback.getKey();
        final var method = callback.getValue();
        MethodUtils.invoke(target, method, event);

        if (event.isHandled()) {
            LOGGER.trace("Handled event [%s] with [%s.%s]",
                    type.getSimpleName(),
                    target.getClass().getSimpleName(),
                    method.getName()
            );
            return true;
        }

        return false;
    }

    @Override
    public <T extends Event>  void callback (Class<T> event, Callback callback) {
        if (! callbacks.containsKey(event))
            callbacks.put(event, new ArrayList<>());

        if (callbacks.get(event).contains(callback))
            throw new EventException("Callback already registered");

        callbacks.get(event).add(callback);
    }

    @Override
    public <T extends Event> void callback (Class<T> event, EventProcessor<T> processor) {
        final var methods = ClassUtils.getAllMethods(processor.getClass(), false);
        if (methods.size() != 1)
            throw new EventException("Something is not right");

        callback(event, new Callback(processor, methods.get(0)));
    }

    @Override
    public void callback (EventProcessor<?> processor) {
        final var methods = ClassUtils.getAllMethods(processor.getClass(), false);
        if (methods.size() != 1)
            throw new EventException("Something is not right");

        global.add(new Callback(processor, methods.get(0)));
    }

}
