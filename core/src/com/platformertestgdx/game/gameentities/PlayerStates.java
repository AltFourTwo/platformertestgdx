package com.platformertestgdx.game.gameentities;

public class PlayerStates {

    protected boolean pressingUp;
    protected boolean pressingDown;
    protected boolean pressingLeft;
    protected boolean pressingRight;
    protected boolean pressingJump;
    protected boolean hasJumped;
    protected boolean isClinging;

//    protected boolean pressingUp;
//    protected boolean pressingUp;
//    protected boolean pressingUp;
//    protected boolean pressingUp;

    public PlayerStates() {
        pressingUp = false;
        pressingDown = false;
        pressingLeft = false;
        pressingRight = false;
        pressingJump = false;
        hasJumped = false;
        isClinging = false;
    }


}
