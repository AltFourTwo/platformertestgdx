package com.platformertestgdx.game.states;

import com.platformertestgdx.game.MyGdxGame;

import java.util.Stack;

public class GameStateManager {

    private MyGdxGame game;

    private Stack<State> states;

    public static final int PLAY_STATE = 42;

    public static final int MENU_STATE = 1;


    public GameStateManager(MyGdxGame game) {
        this.game = game;
        states = new Stack<State>();
    }

    private State getState(int state) {
        if (state == PLAY_STATE) return new PlayState(this);
        else if (state == MENU_STATE) return new MenuState( this);
        return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        states.push(getState(state));
    }

    public void popState() {
        states.pop().dispose();
    }

    public  MyGdxGame game() { return game;}

    public void update(float dt) {
        states.peek().update(dt);
    }

    public void render() {
        states.peek().render();
    }
}
