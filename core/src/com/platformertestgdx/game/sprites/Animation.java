package com.platformertestgdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animation {

    private Array<TextureRegion> frames;
    private float maxFrameTime;
    private float currentFrameTime;
    private int frameCount;
    private int frame;

    public Animation(Texture spriteSheet, int frameCountW, int frameCountH, float cycleTime) {
        frames = new Array<TextureRegion>();
        int frameWidth = spriteSheet.getWidth() / frameCountW;
        int frameHeight = spriteSheet.getWidth() / frameCountH;
        for (int i = 0; i < frameCountH; i++) {
            for (int j = 0; j < frameCountW; j++) {
                frames.add(new TextureRegion(spriteSheet, j*frameWidth, i*frameHeight, frameWidth, frameHeight));
            }
        }
        this.frameCount = frameCountW*frameCountH;
        maxFrameTime = cycleTime / frameCount;
        frame = 0;
    }

    public void update(float dt) {
        currentFrameTime += dt;
        if(currentFrameTime > maxFrameTime) {
            frame++;
            currentFrameTime -= maxFrameTime;
        }
        if (frame >= frameCount) {
            frame = 0;
        }
    }

    public TextureRegion getFrame() {
        return frames.get(frame);
    }
}
