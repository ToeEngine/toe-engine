package br.toe.engine.event;

import lombok.*;

public abstract class Event {

    @Getter
    private final long timeStamp = System.nanoTime();

    @Getter
    private final EventType type;
    private final EventCategory[] categories;

    @Getter
    private boolean handled;

    protected Event(EventType type, EventCategory ... categories) {
        this.type = type;
        this.categories = categories;
    }

    public final void setHandled() {
        handled = true;
    }

    public final boolean isInCategory(EventCategory desired) {
        for (var category : categories)
            if (category == desired)
                return true;

        return false;
    }

    public String toString() {
        return "%s [%d]".formatted(getClass().getSimpleName(), timeStamp);
    }
}
