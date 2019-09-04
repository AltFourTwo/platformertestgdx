package com.platformertestgdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.platformertestgdx.game.CNSTNT;
import com.platformertestgdx.game.gameentities.Player;
import com.platformertestgdx.game.gameroom.GameRoomManager;
import com.platformertestgdx.game.guihud.Controller;
import com.platformertestgdx.game.guihud.HeadsUpDisplay;

public class PlayState extends State {

    private GameRoomManager grm;
    private Sound sd;

    private Skin testskin;

    private Controller controls;
    private HeadsUpDisplay hud;

    private FitViewport fvp;

    private Player player;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, CNSTNT.WIDTH, CNSTNT.HEIGHT);
        guiCam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        testskin = new Skin(Gdx.files.internal("Skins/testskin2/superSkin.json"));

        sd = Gdx.audio.newSound(Gdx.files.internal("Sounds/testsound.mp3"));

        cam.position.x = 192;
        cam.position.y = 192;
        player = new Player(192,192);
        grm =  new GameRoomManager();

        fvp = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), guiCam);
        stage = new Stage(fvp, sb);

        controls = new Controller(stage, testskin, 50f, 0.75f, 1f, 1f);
        hud = new HeadsUpDisplay(stage, testskin, player, 1f, 1f, 1f);

        stage.setDebugAll(true);
        im = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(im);

    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) sd.play(0.2f);
    }

    @Override
    public void update(float dt) {
        handleInput();

        grm.update(dt, player.getPosition());
        cam.translate(player.update(dt, grm, controls.getJoystick()));

        cam.update();
    }

    @Override
    public void render() {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        grm.render(sb, shaper);
        player.render(sb, shaper);
        grm.delayedRender(sb);
        sb.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        sd.dispose();
        grm.dispose();
        player.dispose();
        testskin.dispose();
        stage.dispose();
        System.out.println("Play State Disposed");
    }

    @Override
    public boolean keyDown(int keycode) {
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return super.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        cam.zoom += 0.5/amount;
        return super.scrolled(amount);
    }
}
