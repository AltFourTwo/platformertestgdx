package com.platformertestgdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.platformertestgdx.game.CNSTNT;

public class MenuState extends State implements InputProcessor {

    private TextButton startButton;

    private Texture background;

    private GlyphLayout testText;
    private BitmapFont font;
    private BitmapFont.BitmapFontData fontData;


    public MenuState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, CNSTNT.WIDTH, CNSTNT.HEIGHT);



        startButton = new TextButton("PLAY", skin, "default");
        startButton.setTransform(true);
        //startButton.getLabel().setFontScale(0.1f);
        //startButton.setScale(2f);
        //startButton.setWidth(200);
        //startButton.setHeight(50);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleInput();
            }
        });


        stage = new Stage(new ScreenViewport());

        table = new Table();
        table.setWidth(stage.getWidth());
        //table.setHeight(stage.getHeight());

        table.add(startButton);

        table.align(Align.center);
        table.setPosition(0, Gdx.graphics.getHeight()/2);

        //table.pack();

        //table.padTop()

        stage.setDebugAll(true);
        stage.addActor(table);

        InputMultiplexer im = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(im);


        background = new Texture("Backgrounds/testbg.png");
        testText = new GlyphLayout();
        font = new BitmapFont(Gdx.files.internal("Fonts/gamefont.fnt"));
        fontData = font.getData();
        fontData.scaleX = 1/4f;
        fontData.scaleY = 1/4f;
        font.setColor(0.7f , 0f, 0.7f, 1f);
        testText.setText(font, "SUPER MAGIC");
    }

    @Override
    public void handleInput() {
//        if(Gdx.input.justTouched()) {
            gsm.setState(42);
//        }
    }

    @Override
    public void update(float dt) {
        //handleInput();
    }

    @Override
    public void render() {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0,0, CNSTNT.WIDTH, CNSTNT.HEIGHT);
        font.draw(sb, testText, CNSTNT.WIDTH /2 - testText.width/2, CNSTNT.HEIGHT - 12);
        sb.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();
        stage.dispose();
        System.out.println("Menu State Disposed");
    }

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
}
