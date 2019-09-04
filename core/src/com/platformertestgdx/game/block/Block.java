package com.platformertestgdx.game.block;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.platformertestgdx.game.gameentities.GameEntity;
import com.platformertestgdx.game.gameentities.Player;

import java.util.List;

public abstract class Block implements BlockMethods {

    // Object data
    protected int blockX;
    protected int blockY;
    protected int blockWidth;
    protected int blockHeight;

    protected boolean nearPlayer;

    public Block () {
        blockX = 0;
        blockY = 0;
        blockWidth = 32;
        blockHeight = 32;
    }

    public void setPosition(int blockX, int blockY) {
        this.blockX = blockX;
        this.blockY = blockY;
    }

    public void setDimensions(int blockWidth, int blockHeight) {
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
    }


    public int getBlockX() { return blockX; }
    public int getBlockY() { return blockY; }
    public int getBlockWidth() { return blockWidth; }
    public int getBlockHeight() { return blockHeight; }

    public boolean isNearPlayer() { return nearPlayer; }

    public void update(float dt, Vector2 playerPosition) {
        float posX = playerPosition.x;
        float posY = playerPosition.y;
        if (blockY > posY+240 ||
            blockY+blockHeight < posY-240 ||
            blockX > posX+380 ||
            blockX+blockWidth < posX-380)

            nearPlayer = false;
        else nearPlayer = true;
    }

    public void render(SpriteBatch sb, List<Block> delayedBlocks, TextureRegion[] tiles, int[][] tileLayer) {
        if (nearPlayer) {
            for (int i = blockX; i<blockX+blockWidth; i += 32) {
                for (int j = blockY; j<blockY+blockHeight; j += 32) {
                    sb.draw(tiles[(tileLayer[i/32][j/32])-1], i,j);
                }
            }
        }
    }

    public void renderBorders(ShapeRenderer sr) {
        sr.rectLine(blockX, blockY+blockHeight/2, blockX+blockWidth, blockY+blockHeight/2, blockHeight);
    }

    public void fixY(int roomHeight) {
        blockY = roomHeight - blockY - blockHeight;
    }

    public abstract int collidesWithPlayer(Player player, Vector2 speed);
    public abstract int collidesWithEntity(GameEntity entity);

    @Override
    public String toString() {
        return "BlockX:" + blockX + "   " +
                "BlockY:" + blockY + "   " +
                "BlockWidth:" + blockWidth + "   " +
                "BlockHeight:" + blockHeight + "   ";

    }



}
