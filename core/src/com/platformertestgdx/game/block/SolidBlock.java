package com.platformertestgdx.game.block;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.platformertestgdx.game.gameentities.GameEntity;
import com.platformertestgdx.game.gameentities.Player;

public class SolidBlock extends Block {

    // Simple Collision Keys
    private static final int PASS_THROUGH        = 0;
    private static final int FULL_SOLID          = 1;
    private static final int TOP_ONLY            = 2;
    private static final int BOTTOM_ONLY         = 3;
    private static final int LEFT_ONLY           = 4;
    private static final int RIGHT_ONLY          = 5;
    private static final int TOP_LEFT_CORNER     = 6;
    private static final int TOP_RIGHT_CORNER    = 7;
    private static final int BOTTOM_LEFT_CORNER  = 8;
    private static final int BOTTOM_RIGHT_CORNER = 9;

    // Arc Shaped Collision Keys
    private static final int TOP_ARC    = 10;
    private static final int BOTTOM_ARC = 11;
    private static final int LEFT_ARC   = 12;
    private static final int RIGHT_ARC  = 13;

    // Slope Collision Keys
    private static final int SLOPE_FORWARD   = 14;
    private static final int SLOPE_BACKWARD  = 15;

    // Parallel Collision Keys
    private static final int PARALLEL_HORIZONTAL = 16;
    private static final int PARALLEL_VERTICAL   = 17;

    protected boolean isSlope;
    protected boolean isFloor;

    protected Polygon bounds;
    protected Vector2 tl,tr,bl,br;

    protected int collisionKey;

    public SolidBlock() {
        collisionKey = 1;
        isSlope = false;
        isFloor = true;
    }

    public void setSlope(boolean slope) { isSlope = slope; }
    public boolean isSlope() { return isSlope; }

    public void setFloor(boolean floor) { isFloor = floor; }
    public boolean isFloor() { return isFloor; }

    public void setCollisionKey(int collisionKey) { this.collisionKey = collisionKey; }
    public void setCollisionKey(String collisionKey) {
        int key = resolveCollisionKey(collisionKey);
        if(key == -1) return;
        setCollisionKey(key);
    }
    public int getCollisionKey() { return collisionKey; }

    public Vector2 getTopLeftCorner() {
        return tl;
    }
    public Vector2 getTopRightCorner() {
        return tr;
    }
    public Vector2 getBottomLeftCorner() {
        return bl;
    }
    public Vector2 getBottomRightCorner() {
        return br;
    }

    public void renderCollision(ShapeRenderer sr) {
        if (collisionKey == FULL_SOLID) {
            sr.rectLine(blockX, blockY+blockHeight/2, blockX+blockWidth, blockY+blockHeight/2, blockHeight);
        }
        else if (collisionKey == SLOPE_FORWARD && isFloor) {
            sr.triangle(bl.x, bl.y, tr.x, tr.y, br.x , br.y);
        }
        else if (collisionKey == SLOPE_FORWARD && !isFloor) {
            sr.triangle(bl.x, bl.y, tl.x, tl.y, tr.x, tr.y);
        }
        else if (collisionKey == SLOPE_BACKWARD && isFloor) {
            sr.triangle(bl.x, bl.y, tl.x, tl.y, br.x, br.y);
        }
        else if (collisionKey == SLOPE_BACKWARD && !isFloor) {
            sr.triangle(tl.x, tl.y, tr.x, tr.y, br.x, br.y);
        }
    }

    public void createCollision() {
        bounds = new Polygon();
        tl = new Vector2(blockX, blockY+blockHeight);
        tr = new Vector2(blockX+blockWidth, blockY+blockHeight);
        bl = new Vector2(blockX, blockY);
        br = new Vector2(blockX+blockWidth, blockY);
        if (!isSlope) {
            float[] vertices = {bl.x,bl.y,tl.x,tl.y,tr.x,tr.y,br.x,br.y};
            bounds.setVertices(vertices);
        }
        else if (collisionKey == SLOPE_FORWARD && isFloor) {
            float[] vertices = {bl.x,bl.y,tr.x,tr.y,br.x,br.y};
            bounds.setVertices(vertices);
        }
        else if (collisionKey == SLOPE_FORWARD && !isFloor) {
            float[] vertices = {bl.x,bl.y,tl.x,tl.y,tr.x,tr.y};
            bounds.setVertices(vertices);
        }
        else if (collisionKey == SLOPE_BACKWARD && isFloor) {
            float[] vertices = {bl.x,bl.y,tl.x,tl.y,br.x,br.y};
            bounds.setVertices(vertices);
        }
        else if (collisionKey == SLOPE_BACKWARD && !isFloor) {
            float[] vertices = {tl.x,tl.y,tr.x,tr.y,br.x,br.y};
            bounds.setVertices(vertices);
        }
    }

    public int collidesWithPlayer(Player player, Vector2 speed) {
        if (!nearPlayer) return 0;

        Vector2[][] trajectory = player.getEdgesTrajectory();
        int collisions[] = {0,0,0,0};
        int collisionPriority = 0;
        boolean[] sides = {false,false,false,false};

        for (int i = 0; i < trajectory.length; i++) {
            for (int j = 0; j < trajectory[0].length; j++) {
                if (bounds.contains(trajectory[i][j])) {
                    collisions[i] += 1;
                }
            }
            if (collisions[i] > 0 && i == 0) {
                System.out.println("Player top is colliding on " + collisions[i] + " points!");
            }
            if (collisions[i] > 0 && i == 1) {
                System.out.println("Player bottom is colliding on " + collisions[i] + " points!");
            }
            if (collisions[i] > 0 && i == 2) {
                System.out.println("Player left is colliding on " + collisions[i] + " points!");
            }
            if (collisions[i] > 0 && i == 3) {
                System.out.println("Player right is colliding on " + collisions[i] + " points!");
            }
        }


        for (int i = 0; i < sides.length; i++) {
            if (collisions[i] > 0) sides[i] = true;
        }
        if (isSlope && (sides[2] || sides[3])) {
            sides[2] = collisions[2] >= collisions[3];
            sides[3] = collisions[3] >= collisions[2];

            if (blockHeight <= blockWidth) {
                if (isFloor) sides[1] = true;
                else sides[0] = true;
            }
            else {
                speed.x *= 0.9;
                speed.y *= 0.9;
            }
        }
        else {
            for (int i = 0; i < sides.length; i++) {
                if (sides[i]) {
                    for (int j = i+1; j < sides.length; j++) {
                        if (collisions[i] > collisions[j]) {
                            sides[j] = false;
                        }
                        else if (collisions[j] > collisions[i]) {
                            sides[i] = false;
                        }
                    }
                }
            }
        }

        if (sides[0]) collisionPriority += GameEntity.TOP_LINE;
        if (sides[1]) collisionPriority += GameEntity.BOTTOM_LINE;
        if (sides[2]) collisionPriority += GameEntity.LEFT_LINE;
        if (sides[3]) collisionPriority += GameEntity.RIGHT_LINE;

        return collisionPriority;
    }

    @Override
    public int collidesWithEntity(GameEntity entity) {
        return 0;
    }

    private int resolveCollisionKey(String collisionKey) {
        int iKey;


        if      (collisionKey.equalsIgnoreCase("BLOCK_DEFAULT"))       iKey = -1;
        else if (collisionKey.equalsIgnoreCase("PASS_THROUGH"))        iKey = 0;
        else if (collisionKey.equalsIgnoreCase("FULL_SOLID"))          iKey = 1;
        else if (collisionKey.equalsIgnoreCase("TOP_ONLY"))            iKey = 2;
        else if (collisionKey.equalsIgnoreCase("BOTTOM_ONLY"))         iKey = 3;
        else if (collisionKey.equalsIgnoreCase("LEFT_ONLY"))           iKey = 4;
        else if (collisionKey.equalsIgnoreCase("RIGHT_ONLY"))          iKey = 5;
        else if (collisionKey.equalsIgnoreCase("TOP_LEFT_CORNER"))     iKey = 6;
        else if (collisionKey.equalsIgnoreCase("TOP_RIGHT_CORNER"))    iKey = 7;
        else if (collisionKey.equalsIgnoreCase("BOTTOM_LEFT_CORNER"))  iKey = 8;
        else if (collisionKey.equalsIgnoreCase("BOTTOM_RIGHT_CORNER")) iKey = 9;
        else if (collisionKey.equalsIgnoreCase("TOP_ARC"))             iKey = 10;
        else if (collisionKey.equalsIgnoreCase("BOTTOM_ARC"))          iKey = 11;
        else if (collisionKey.equalsIgnoreCase("LEFT_ARC"))            iKey = 12;
        else if (collisionKey.equalsIgnoreCase("RIGHT_ARC"))           iKey = 13;
        else if (collisionKey.equalsIgnoreCase("SLOPE_FORWARD"))       iKey = 14;
        else if (collisionKey.equalsIgnoreCase("SLOPE_BACKWARD"))      iKey = 15;
        else if (collisionKey.equalsIgnoreCase("PARALLEL_HORIZONTAL")) iKey = 16;
        else if (collisionKey.equalsIgnoreCase("PARALLEL_VERTICAL"))   iKey = 17;
        else iKey = 0;

        return iKey;
    }

    public String toString() {
        return super.toString() + "collision type:" + collisionKey + "   ";
    }
}
