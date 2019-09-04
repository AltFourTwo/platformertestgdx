package com.platformertestgdx.game.gameentities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.platformertestgdx.game.guihud.HitPointBar;


public abstract class GameEntity {

    // Line Keys
    public static final int TOP_LINE               = 1;
    public static final int BOTTOM_LINE            = 2;
    public static final int LEFT_LINE              = 4;
    public static final int RIGHT_LINE             = 8;

    private EntityStates entityStates;

    protected HitPointBar hpbar;
    protected boolean hpbarVisible;

    protected Vector2 position;
    protected Vector2 movement;

    protected Polygon hitbox;
    protected Vector2[][] edges;
    protected Vector2[][] edgesNext;


    protected int maxHP;
    protected int currentHP;
    protected int redHP;
    protected int maxSpeed;
    protected int weight;

    protected Texture img;

    public GameEntity() {
        entityStates = new EntityStates();

        hpbarVisible = true;
        position = new Vector2(0,0);
        movement = new Vector2(0,0);

        hitbox = new Polygon();

        maxSpeed = 10;
        weight = 10;
    }

    public void setHpBar(HitPointBar hpbar) {
        this.hpbar = hpbar;
    }

    public void showHpBar(boolean show) { this.hpbarVisible = show; }
    public boolean isHpbarVisible() { return hpbarVisible; }

    public void update(float dt) {

    }

    public void render(SpriteBatch sb, ShapeRenderer shaper) {
        sb.draw(img, position.x - img.getWidth()/2, position.y - img.getHeight()/2, img.getWidth(), img.getHeight());
        sb.end();
        shaper.begin(ShapeRenderer.ShapeType.Filled);
        shaper.setColor(1f, 0f, 1f ,1f);
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges[0].length; j++) {
                shaper.point(edges[i][j].x, edges[i][j].y, 0);
            }
        }
        shaper.end();
        sb.begin();
    }

    public void dispose() {
        img.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public void setPosition(int x, int y) { this.position.x = x; this.position.y = y;}

    public Vector2 getTrajectory() {
        return movement;
    }
    public void setTrajectory(Vector2 movement) {
        this.movement = movement;
    }

    public Polygon getHitbox() { return hitbox; }
    public Vector2[][] getEdges() { return edges; }
    public Vector2[][] getEdgesTrajectory() { return edgesNext; }

    public boolean isMoving() {
        return entityStates.isMoving;
    }
    public void setMoving(boolean moving) {
        entityStates.isMoving = moving;
    }

    public int getMaxHP() {
        return maxHP;
    }
    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }
    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }
    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Texture getImg() {
        return img;
    }
    public void setImg(Texture img) {
        this.img = img;
    }
}
