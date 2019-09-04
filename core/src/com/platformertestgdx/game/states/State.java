package com.platformertestgdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.platformertestgdx.game.MyGdxGame;

public abstract class State implements InputProcessor, GestureDetector.GestureListener {

    protected Stage stage;
    protected Skin skin;
    protected Table table;

    protected InputMultiplexer im;

    protected GameStateManager gsm;
    protected MyGdxGame game;


    protected Vector3 mouse;

    protected SpriteBatch sb;
    protected ShapeRenderer shaper;
    protected OrthographicCamera cam;
    protected OrthographicCamera guiCam;

    protected State(GameStateManager gsm) {
        this.gsm = gsm;
        game = gsm.game();
        sb = game.getSpriteBatch();
        shaper = game.getShapeRenderer();
        cam = game.getCam();
        guiCam = game.getGuiCam();
        mouse = new Vector3(); // Player position

        skin = new Skin(Gdx.files.internal("Skins/ogtest/uiskin.json"));
        stage = new Stage();
        table = new Table();

        InputMultiplexer im = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(im);
    }

    protected abstract void handleInput();
    public abstract void update(float dt); // deltaTime
    public abstract void render();
    public abstract void dispose();

    // InputListener/Processor Methods
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


    // Gesture Methods
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
