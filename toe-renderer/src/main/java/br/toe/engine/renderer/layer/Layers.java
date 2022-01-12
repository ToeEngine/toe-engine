package br.toe.engine.renderer.layer;

import java.util.*;
import java.util.concurrent.*;

public abstract class Layers {

    private static final Queue<Layer> stack = new ConcurrentLinkedQueue<>();

    private Layers(){}

    public static void push(Layer layer) {
        stack.add(layer);
    }

    public static Layer pop() {
        return stack.remove();
    }

    public static Layer[] layers() {
        return stack.toArray(new Layer[0]);
    }
}
