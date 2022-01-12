package br.toe.engine.event;

public interface EventProcessor<T extends Event> {

    void process(T event);

}
