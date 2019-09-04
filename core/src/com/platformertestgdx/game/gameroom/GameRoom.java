package com.platformertestgdx.game.gameroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.platformertestgdx.game.Background;
import com.platformertestgdx.game.block.*;

import java.util.ArrayList;
import java.util.List;

public class GameRoom {

    private int id;
    private String roomName;
    private int globalX;
    private int globalY;
    private int roomWidth;
    private int roomHeight;
    private Background background;
    private TileMap tileMap;
    private List<Block> blocks;
    private List<Block> delayedRender;

    public GameRoom() {
        id = 0;
        roomName = "Error";
        globalX = 0;
        globalY = 0;
        roomWidth = 20;
        roomHeight = 10;
        delayedRender = new ArrayList<Block>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getGlobalX() {
        return globalX;
    }

    public void setGlobalX(int globalX) {
        this.globalX = globalX;
    }

    public int getGlobalY() {
        return globalY;
    }

    public void setGlobalY(int globalY) {
        this.globalY = globalY;
    }

    public Background getBackground() {
        return background;
    }

    public void setBackground(String backgroundFile) {
            this.background = new Background(backgroundFile);
    }


    public void setTileMap(String tilesetFile, int[][] underPlayer, int[][] abovePlayer) {
        tileMap = new TileMap(Gdx.files.internal("TileSets/"+ tilesetFile), underPlayer, abovePlayer);
    }

    public void setRoomDimensions(int roomWidth, int roomHeight) {
        setRoomWidth(roomWidth);
        setRoomHeight(roomHeight);
    }
    public void setRoomWidth(int roomWidth) { this.roomWidth = roomWidth; }
    public void setRoomHeight(int roomHeight) { this.roomHeight = roomHeight; }

    public int getRoomWidth() { return roomWidth; }
    public int getRoomHeight() { return roomHeight; }

    public void addBlock(Block block) { this.blocks.add(block); }
    public void setBlocks(List<Block> blocks) { this.blocks = new ArrayList<Block>(blocks); }
    public List<Block> getBlocks() { return blocks; }


    public void update(float dt, Vector2 playerPosition) {
        background.update(dt);
        for(Block b: blocks) {
            b.update(dt, playerPosition);
        }
    }

    public void render(SpriteBatch sb) {
        background.render(sb);
        for(Block b: blocks) {
            b.render(sb, delayedRender, tileMap.getTiles(), tileMap.getUnderLayer());
        }
    }

    public void delayedRender(SpriteBatch sb) {
        for(Block b: delayedRender) {
            b.render(sb, null, tileMap.getTiles(), tileMap.getAboveLayer());
        }
        delayedRender.clear();
    }

    public void dispose() {
        tileMap.dispose();
        background.dispose();
    }
}
