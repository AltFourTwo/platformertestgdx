package com.platformertestgdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.platformertestgdx.game.states.GameStateManager;

public class MyGdxGame extends ApplicationAdapter {

	public static final String TITLE = "GAME";

	private SpriteBatch batch;
	private ShapeRenderer shaper;
	private GameStateManager gsm;
	private OrthographicCamera cam;
    private OrthographicCamera guiCam;

	public static final float STEP = 1 / 60f;
	private float accum;

	private Music music;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shaper = new ShapeRenderer();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CNSTNT.WIDTH, CNSTNT.HEIGHT);
        guiCam = new OrthographicCamera();
        guiCam.setToOrtho(false, CNSTNT.WIDTH, CNSTNT.HEIGHT);
		gsm = new GameStateManager( this);



		music  = Gdx.audio.newMusic(Gdx.files.internal("Music/testmusic.mp3"));
		music.setLooping(true);
		music.setVolume(0.05f);
		music.play();

		Gdx.gl.glClearColor( 0.5f, 0f, 0.5f, 0f);
		gsm.pushState(1);
	}

	@Override
	public void render () {

	    accum+= Gdx.graphics.getDeltaTime();
	    while (accum >= STEP) {
	        accum -= STEP;

		    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		    gsm.update(STEP);
		    gsm.render();
        }
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		music.dispose();
	}


    public SpriteBatch getSpriteBatch() {
        return batch;
    }
    public ShapeRenderer getShapeRenderer() { return shaper; }

    public OrthographicCamera getCam() {
        return cam;
    }

    public OrthographicCamera getGuiCam() {
        return guiCam;
    }
}
