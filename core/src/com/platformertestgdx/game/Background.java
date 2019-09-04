package com.platformertestgdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Background {

    private Texture bg;
    private Vector2 position;
    private Vector2 movement;
    private int updateCalls;

    public Background(String backgroundFile) {
        bg = new Texture(Gdx.files.internal("Backgrounds/"+ backgroundFile));
        position = new Vector2(0,0);
        movement = new Vector2( -4, -2);
        updateCalls = 0;
    }

    public void update(float dt) {
        updateCalls++;

        if (updateCalls %3 == 0) {
            position.x += movement.x;

        }
        if (updateCalls %3 == 0 ) {
            position.y += movement.y;
        }
        if (updateCalls % 1201 == 1200) {
            movement.x = -movement.x;
            updateCalls = 0;
        }

        if (position.x > bg.getWidth()) position.x -= bg.getWidth();
        else if (position.x < 0) position.x += bg.getWidth();
        if (position.y > bg.getHeight()) position.y -= bg.getHeight();
        else if (position.y < 0) position.y += bg.getHeight();

    }

    public void render(SpriteBatch sb) {
        sb.draw(bg,position.x, position.y);
        sb.draw(bg,position.x - bg.getWidth(), position.y);
        sb.draw(bg,position.x, position.y - bg.getHeight());
        sb.draw(bg,position.x - bg.getWidth(), position.y - bg.getHeight());
    }

    public void dispose() {
        bg.dispose();
    }
}
