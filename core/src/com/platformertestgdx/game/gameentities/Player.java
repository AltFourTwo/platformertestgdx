package com.platformertestgdx.game.gameentities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.platformertestgdx.game.block.Block;
import com.platformertestgdx.game.gameroom.GameRoom;
import com.platformertestgdx.game.gameroom.GameRoomManager;
import com.platformertestgdx.game.guihud.ControlPad;
import com.platformertestgdx.game.guihud.PointBar;

import java.util.List;

public class Player extends GameEntity {

    private static final int RIGHT      = 0;
    private static final int UPRIGHT    = 1;
    private static final int DOWNRIGHT  = -1;
    private static final int UP         = 2;
    private static final int DOWN       = -2;
    private static final int UPLEFT     = 3;
    private static final int DOWNLEFT   = -3;
    private static final int LEFT       = 4;
    private static final int LEFT2      = -4;

    private PlayerStates playerStates;

    private PointBar otherbar;

    private int power;
    private int maxPower;

    private int speedIncrement;
    private int updateCounter;

    public Player(int x, int y) {
        playerStates = new PlayerStates();
        position.x = x;
        position.y = y;

        createVector();
        for (int i = 0; i < 5; i++) {
            edges[0][i].set(x-24+i*12,y+30);
            edges[1][i].set(x-24+i*12,y-30);
            edges[2][i].set(x-30,y-24+i*12);
            edges[3][i].set(x+30,y-24+i*12);

            edgesNext[0][i].set(x-24+i*12,y+30);
            edgesNext[1][i].set(x-24+i*12,y-30);
            edgesNext[2][i].set(x-30,y-24+i*12);
            edgesNext[3][i].set(x+30,y-24+i*12);
        }

        speedIncrement = 3;
        maxSpeed = 10;

        maxHP = 100;
        currentHP = 100;
        redHP = 0;

        power = 0;
        maxPower = 20;

        img = new Texture(Gdx.files.internal("TileSets/testtileset.png"));
    }

    public void setOtherbar(PointBar otherbar) { this.otherbar = otherbar; }

    public int getPower() { return power; }
    public int getMaxPower() { return maxPower; }

    public void handleInput(ControlPad controls) {
        int direction = controls.getDirection();
        if (controls.getKnobPercentX()+controls.getKnobPercentY() != 0) {
            setMoving(true);
            switch (direction) {
                case RIGHT:
                    pressingDirections(false,false,false,true);
                    movement.x += speedIncrement/2;
                    movement.y = movement.y*0.9f;
                    if (movement.y < 0.3f || movement.y > -0.3f) movement.y = 0;
                    break;
                case UPRIGHT:
                    pressingDirections(true,false,false,true);
                    movement.x += speedIncrement/2*(Math.sqrt(2)/2);
                    movement.y += speedIncrement/2*(Math.sqrt(2)/2);
                    break;
                case DOWNRIGHT:
                    pressingDirections(false,true,false,true);
                    movement.x += speedIncrement/2*(Math.sqrt(2)/2);
                    movement.y -= speedIncrement/2*(Math.sqrt(2)/2);
                    break;
                case UP:
                    pressingDirections(true,false,false,false);
                    movement.y += speedIncrement/2;
                    movement.x = movement.x*0.9f;
                    if (movement.x < 0.3f || movement.x > -0.3f) movement.x = 0;
                    break;
                case DOWN:
                    pressingDirections(false,true,false,false);
                    movement.y -= speedIncrement/2;
                    movement.x = movement.x*0.9f;
                    if (movement.x < 0.3f || movement.x > -0.3f) movement.x = 0;
                    break;
                case UPLEFT:
                    pressingDirections(true, false,true,false);
                    movement.x -= speedIncrement/2*(Math.sqrt(2)/2);
                    movement.y += speedIncrement/2*(Math.sqrt(2)/2);
                    break;
                case DOWNLEFT:
                    pressingDirections(false,true, true, false);
                    movement.x -= speedIncrement/2*(Math.sqrt(2)/2);
                    movement.y -= speedIncrement/2*(Math.sqrt(2)/2);
                    break;
                case LEFT:
                case LEFT2:
                    pressingDirections(false,false,true,false);
                    movement.x -= speedIncrement/2;
                    movement.y = movement.y*0.9f;
                    if (movement.y < 0.3f || movement.y > -0.3f) movement.y = 0;
                    break;
            }
            correctSpeed(movement);
        }
        else {
            pressingDirections(false,false,false,false);
            setMoving(false);
            movement.x = movement.x*0.9f;
            movement.y = movement.y*0.9f;
            if (movement.x < 0.2f && movement.x > -0.2f) movement.x = 0;
            if (movement.y < 0.2f && movement.y > -0.2f) movement.y = 0;
        }

        //pressingJump(false);
    }

    public Vector2 update(float dt, GameRoomManager grm, ControlPad controls) {
        handleInput(controls);
        movePlayer(grm.getRoom().getBlocks());
        outOfBounds(grm.getRoom());

        updateCounter++;

        if (updateCounter %61 == 60) {
            currentHP -= 20;
            redHP = 20;
        }
        if (currentHP < 0) {
            currentHP = maxHP;
            redHP = 0;
        }
        if (updateCounter %4 == 3 && redHP > 0) {
            redHP *= 0.9f;
            redHP -= 1;
            if (redHP < 1) redHP = 0;
        }
        if (updateCounter %13 == 12) {
            power++;
            if (power > 20) power = 0;
        }
        hpbar.setValue(currentHP);
        hpbar.setValue2(currentHP+redHP);
        otherbar.setValue(power);

        return movement;
    }


    public void movePlayer(List<Block> blocks) {
        //boolean collisionClean;

        int lastSideToCollide = checkCollision(blocks, movement);
        int counter = 0;

        if (lastSideToCollide > 0) {
            Vector2 tempMovement = movement;
            boolean collisionClean = false;
            while(!collisionClean) {
                counter++;
                if (counter %6 == 5) {
                    tempMovement.x *= 0.9;
                    tempMovement.y *= 0.9;
                }
                System.out.println("tempMovement x:"+ tempMovement.x + "   tempMovement.y:" + tempMovement.y);
                switch (lastSideToCollide) {
                    case TOP_LINE:
                        tempMovement.y -= 1;
                        break;
                    case BOTTOM_LINE:
                        tempMovement.y += 1;
                        break;
                    case LEFT_LINE:
                        tempMovement.x += 1;
                        break;
                    case RIGHT_LINE:
                        tempMovement.x -= 1;
                        break;
                    case TOP_LINE+LEFT_LINE:
                        tempMovement.y -= 1;
                        break;
                    case TOP_LINE+RIGHT_LINE:
                        tempMovement.y -= 1;
                        break;
                    case BOTTOM_LINE+LEFT_LINE:
                        tempMovement.y += 1;
                        break;
                    case BOTTOM_LINE+RIGHT_LINE:
                        tempMovement.y += 1;
                        break;



//                    case Block.LEFT_LINE:
//                        tempMovement.y += movement.x - movement.y;
//                        if (checkCollision(blocks, tempMovement) > 0) {
//                            tempMovement.y = movement.y;
//                            tempMovement.x += 1;
//                        } else {
//                            tempMovement.x *= (Math.sqrt(2)/2);
//                            tempMovement.y *= (Math.sqrt(2)/2);
//                        }
//                        break;
//                    case Block.RIGHT_LINE:
//                        tempMovement.y += movement.x - movement.y;
//                        if (checkCollision(blocks, tempMovement) > 0) {
//                            tempMovement.y = movement.y;
//                            tempMovement.x -= 1;
//                        } else {
//                            tempMovement.x *= (Math.sqrt(2)/2);
//                            tempMovement.y *= (Math.sqrt(2)/2);
//                        }
//                        break;
                }
                tempMovement.x = Math.round(tempMovement.x);
                tempMovement.y = Math.round(tempMovement.y);
                correctSpeed(tempMovement);
                System.out.println("tempMovement x:"+ tempMovement.x + "   tempMovement.y:" + tempMovement.y);
                lastSideToCollide = checkCollision(blocks, tempMovement);
                if (lastSideToCollide == 0) collisionClean = true;
            }
            movement = tempMovement;
        }
        proceedMove();
    }

    public void proceedMove() {
        position.x += movement.x;
        position.y += movement.y;
        hitbox.translate(movement.x, movement.y);
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges[0].length; j++) {
                edges[i][j].x += movement.x;
                edges[i][j].y += movement.y;
            }
        }
    }

    public int checkCollision(List<Block> blocks, Vector2 movement) {
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < (edges[0]).length; j++) {
                edgesNext[i][j].x = edges[i][j].x + movement.x;
                edgesNext[i][j].y = edges[i][j].y + movement.y;
            }
        }
        int sideCollide = 0;
        for (Block b: blocks) {
            sideCollide = b.collidesWithPlayer(this, movement);
            if (sideCollide > 0) return sideCollide;
        }
        return sideCollide;
    }

    public void correctSpeed(Vector2 speed) {
        if(speed.x > maxSpeed) speed.x = maxSpeed;
        if(speed.x < -maxSpeed) speed.x = -maxSpeed;
        if(speed.y > maxSpeed) speed.y = maxSpeed;
        if(speed.y < -maxSpeed) speed.y = -maxSpeed;
    }

    public void setPosition(int x, int y) {
        super.setPosition(x,y);
        movement.x = 0;
        movement.y = 0;
        for (int i = 0; i < 5; i++) {
            edges[0][i].set(position.x-24+i*12,position.y+30);
            edges[1][i].set(position.x-24+i*12,position.y-30);
            edges[2][i].set(position.x-30,position.y-24+i*12);
            edges[3][i].set(position.x+30,position.y-24+i*12);
        }
    }

    public void outOfBounds(GameRoom gameRoom) {
        if (position.x < 0 || position.x > gameRoom.getRoomWidth()*32 || position.y < 0 || position.y > gameRoom.getRoomHeight()*32) {
            setPosition(192,192);
        }
    }

    public void createVector() {
        edges = new Vector2[][] {
                new Vector2[] {new Vector2(),new Vector2(),new Vector2(),new Vector2(),new Vector2()},
                new Vector2[] {new Vector2(),new Vector2(),new Vector2(),new Vector2(),new Vector2()},
                new Vector2[] {new Vector2(),new Vector2(),new Vector2(),new Vector2(),new Vector2()},
                new Vector2[] {new Vector2(),new Vector2(),new Vector2(),new Vector2(),new Vector2()}};
        edgesNext = new Vector2[][] {
                new Vector2[] {new Vector2(),new Vector2(),new Vector2(),new Vector2(),new Vector2()},
                new Vector2[] {new Vector2(),new Vector2(),new Vector2(),new Vector2(),new Vector2()},
                new Vector2[] {new Vector2(),new Vector2(),new Vector2(),new Vector2(),new Vector2()},
                new Vector2[] {new Vector2(),new Vector2(),new Vector2(),new Vector2(),new Vector2()}};
    }

    public boolean isPressingUp()    { return playerStates.pressingUp; }
    public boolean isPressingDown()  { return playerStates.pressingDown; }
    public boolean isPressingLeft()  { return playerStates.pressingLeft; }
    public boolean isPressingRight() { return playerStates.pressingRight; }
    public boolean isPressingJump()  { return playerStates.pressingJump;}
    public boolean hasJumped()       { return playerStates.hasJumped; }

    public void pressingDirections(boolean up, boolean down, boolean left, boolean right) {
        playerStates.pressingUp = up;
        playerStates.pressingDown = down;
        playerStates.pressingLeft = left;
        playerStates.pressingRight = right;
    }

    public void pressingJump(boolean jump) {
        playerStates.pressingJump = true;
    }




    public boolean isClinging() { return playerStates.isClinging; }
    public void setClinging(boolean clinging) { playerStates.isClinging = clinging; }
}
