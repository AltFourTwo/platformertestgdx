package com.platformertestgdx.game.block;

import com.badlogic.gdx.math.Vector2;
import com.platformertestgdx.game.gameentities.GameEntity;
import com.platformertestgdx.game.gameentities.Player;

public class SemiSolidBlock extends SolidBlock {

    public SemiSolidBlock() {
        collisionKey = 2;
    }

    @Override
    public int collidesWithPlayer(Player player, Vector2 speed) {
        if (!nearPlayer) return 0;

        boolean pressingDownPlusJump = player.isPressingDown() && player.isPressingJump();
        Vector2 pPos = player.getPosition();
        Vector2[] trajectory = player.getEdgesTrajectory()[1];

        if (pPos.y-30 > blockY+blockHeight &&   // If player position is higher than the platform
            pPos.x+30 > blockX &&               // and within the blocks width
            pPos.x-30 < blockX + blockWidth &&
            !pressingDownPlusJump) {            // and not pressing down plus jump

            for (int i = 0; i < trajectory.length; i++) {
                if (trajectory[i].y <= blockY+blockHeight) return 2;  // Collision happen
            }
        }
        return 0;
    }

    public int collidesWithEntity(GameEntity entity) {
        return 0;
    }
}
