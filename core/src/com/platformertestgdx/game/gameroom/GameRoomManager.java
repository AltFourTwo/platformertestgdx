package com.platformertestgdx.game.gameroom;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.platformertestgdx.game.collision.CollisionMap;
import com.platformertestgdx.game.xmldatareading.RoomParser;


public class GameRoomManager {

    private GameRoom room;

    public GameRoomManager() {
        room = RoomParser.getMapData(5);
    }

    public GameRoom getRoom() {
        return room;
    }

    public void update(float dt, Vector2 playerPosition) {
        room.update(dt, playerPosition);
    }

    public void changeRoom(int id) {
        //room = RoomParser.getMapData(3);
    }

    public void render(SpriteBatch sb, ShapeRenderer shaper) {
        room.render(sb);
        CollisionMap.render(sb, shaper, room.getBlocks());
    }

    public void delayedRender(SpriteBatch sb) {
        room.delayedRender(sb);
    }

    public void dispose() {
        room.dispose();
    }
}
