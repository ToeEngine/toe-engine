package br.toe.game;

public abstract class GameState {
    private GameState (){}

    public static volatile boolean running;
}
