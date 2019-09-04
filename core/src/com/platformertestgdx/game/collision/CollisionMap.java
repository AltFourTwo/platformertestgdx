package com.platformertestgdx.game.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.platformertestgdx.game.block.*;

import java.util.List;

public class CollisionMap {

    public static void render(SpriteBatch sb, ShapeRenderer shaper, List<Block> blocks) {
        sb.end();
        shaper.setProjectionMatrix(sb.getProjectionMatrix());
        shaper.begin(ShapeType.Line);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        for (Block b: blocks) {
            if (b instanceof SolidBlock) {
                shaper.setColor(1f, 0f,0f,1f);
                ((SolidBlock)(b)).renderCollision(shaper);
            }
            else {
                shaper.setColor(0f,1f,0f,1f);
                b.renderBorders(shaper);
            }
        }
        shaper.setColor(1f,1f,1f,1f);
        shaper.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        sb.begin();
    }
}
