package com.platformertestgdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Laser {
    public static final int LASER_WIDTH = 32;

    private static final int FLUCTUATION = 160;
    private static final int LASER_GAP = 100;
    private static final int LOWEST_OPENING = 60;
    private Texture topLaser, botLaser;
    private Vector2 posTopLaser, posBotLaser;
    private Rectangle boundsTop, boundsBot;
    private Random rand;

    public Laser(float x) {
        topLaser = new Texture("toplaser.png");
        botLaser = new Texture("bottomlaser.png");
        rand = new Random();

        posTopLaser = new Vector2(x, rand.nextInt(FLUCTUATION) + LASER_GAP + LOWEST_OPENING);
        posBotLaser = new Vector2(x, posTopLaser.y - LASER_GAP - topLaser.getHeight());

        boundsTop = new Rectangle(posTopLaser.x + 11, posTopLaser.y + 4, 9, topLaser.getHeight() - 4);
        boundsBot = new Rectangle(posBotLaser.x + 11, posBotLaser.y - 4, 9, botLaser.getHeight() - 4);
    }


    public Texture getTopLaser() {
        return topLaser;
    }

    public Texture getBotLaser() {
        return botLaser;
    }

    public Vector2 getPosTopLaser() {
        return posTopLaser;
    }

    public Vector2 getPosBotLaser() {
        return posBotLaser;
    }

    public void reposition(float x) {
        posTopLaser.set(x, rand.nextInt(FLUCTUATION) + LASER_GAP + LOWEST_OPENING);
        posBotLaser.set(x, posTopLaser.y - LASER_GAP - topLaser.getHeight());
        boundsTop.setPosition(posTopLaser.x + 11, posTopLaser.y + 4);
        boundsBot.setPosition(posBotLaser.x + 11, posBotLaser.y - 4);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsTop) || player.overlaps(boundsBot);
    }

    public void render(SpriteBatch sb) {
        sb.draw(topLaser, posTopLaser.x, posTopLaser.y);
        sb.draw(botLaser, posBotLaser.x, posBotLaser.y);
    }

    public void dispose() {
        topLaser.dispose();
        botLaser.dispose();
    }
}
