package com.platformertestgdx.game.block;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.platformertestgdx.game.gameentities.GameEntity;
import com.platformertestgdx.game.gameentities.Player;

import java.util.List;

public class PassThroughBlock extends Block implements PassThroughBlockMethods {

    private boolean isInBackground;
    private boolean canFade;
    private boolean isVisible;
    private float alpha;

    public PassThroughBlock() {
        isInBackground = false;
        canFade = false;
        isVisible = true;
        alpha = 1;
    }

    public boolean canFade() { return canFade; }
    public void setCanFade(boolean canFade) { this.canFade = canFade; }
    public boolean isVisible() { return isVisible; }
    public void setVisible(boolean visible) { isVisible = visible; }
    public boolean isInBackground() { return isInBackground; }
    public void setInBackground(boolean inBackground) { isInBackground = inBackground; }

    @Override
    public void update(float dt, Vector2 playerPosition) {
        super.update(dt, playerPosition);
        if (canFade) {
            if (isVisible) alpha += 0.02;
            else alpha -= 0.02;
            if (alpha > 1) alpha = 1;
            else if (alpha < 0) alpha = 0;
        }
    }

    @Override
    public int collidesWithPlayer(Player player, Vector2 speed) {
        if (!nearPlayer || !canFade) return 0;

        float pX = player.getPosition().x;
        float pY = player.getPosition().y;
        boolean overlapsPlayer = false;
        if (pX > blockX && pX < blockX+blockWidth && pY > blockY && pY < blockY + blockHeight)
            overlapsPlayer = true;
        isVisible = !overlapsPlayer;

        return 0;
    }

    @Override
    public int collidesWithEntity(GameEntity entity) {
        return 0;
    }

    @Override
    public void render(SpriteBatch sb, List<Block> delayedBlocks, TextureRegion[] tiles, int[][] tileLayer) {
        if(nearPlayer) {
            if (isInBackground) {
                super.render(sb, delayedBlocks, tiles, tileLayer);
            }
            else {
                if (delayedBlocks != null) {
                    delayedBlocks.add(this);
                    return;
                }
                sb.setColor(1,1,1,alpha);
                super.render(sb, delayedBlocks, tiles, tileLayer);
                sb.setColor(1,1,1,1);
            }
        }
    }

}
