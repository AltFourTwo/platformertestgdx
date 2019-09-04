package com.platformertestgdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.platformertestgdx.game.CNSTNT;

public class Player {

    private static final int SPEED = 100;

    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Texture img;
    private Animation playerAnim;
    private Sound bop;

    public Player(int x, int y) {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0,0,0);
        img = new Texture("testtileset.png");
        playerAnim = new Animation(img, 2, 2, 0.5f);
        bounds = new Rectangle(x, y, img.getWidth()/2, img.getHeight()/2);
        bop = Gdx.audio.newSound(Gdx.files.internal("testsound.mp3"));
    }

    public void update(float dt) {
        playerAnim.update(dt);
        if (position.y > 0) velocity.add(0, -CNSTNT.GRAVITY, 0);
        velocity.scl(dt);
        position.add(SPEED * dt, velocity.y,0);
        if (position.y < 0) position.y = 0;

        velocity.scl(1/dt);
        bounds.setPosition(position.x, position.y);
    }

    public Vector3 getPosition() {
        return position;
    }

    public Texture getImg() {
        return img;
    }

    public TextureRegion getFrameImg() {
        return playerAnim.getFrame();
    }

    public void jump() {
        velocity.y = 350;
        bop.play();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void render(SpriteBatch sb) {
        sb.draw(playerAnim.getFrame(), position.x, position.y);
    }

    public void dispose() {
        img.dispose();
        bop.dispose();
    }
}
