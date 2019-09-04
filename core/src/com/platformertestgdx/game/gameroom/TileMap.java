package com.platformertestgdx.game.gameroom;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileMap {

    private Texture tileset;
    private TextureRegion[] tiles;
    private int[][] underPlayer;
    private int[][] abovePlayer;

    // TODO exception when given a file that has inappropriate dimensions
    public TileMap(FileHandle tilesetFile, int[][] underPlayer, int[][] abovePlayer) {
        this.underPlayer = underPlayer;
        this.abovePlayer = abovePlayer;
        tileset = new Texture(tilesetFile);
        int tilesetW = tileset.getWidth()/32;
        int tilesetH = tileset.getHeight()/32;
        tiles = new TextureRegion[tilesetW*tilesetH];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new TextureRegion(tileset,(i%tilesetW)*32,(i/tilesetW)*32, 32, 32);
        }
    }

    public TextureRegion[] getTiles() {
        return tiles;
    }

    public int[][] getUnderLayer() {
        return underPlayer;
    }

    public int[][] getAboveLayer() {
        return abovePlayer;
    }



    public void dispose() {
        tileset.dispose();
    }
}
