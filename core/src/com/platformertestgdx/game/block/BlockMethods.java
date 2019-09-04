package com.platformertestgdx.game.block;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public interface BlockMethods {

    int getBlockX();
    int getBlockY();

    int getBlockWidth();
    int getBlockHeight();

    void update(float dt, Vector2 playerPosition);
    void render(SpriteBatch sb, List<Block> delayedBlocks, TextureRegion[] tiles, int[][] tileLayer);

    String toString();
}
