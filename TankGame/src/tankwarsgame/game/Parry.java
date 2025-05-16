package tankwarsgame.game;

import tankwarsgame.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Parry extends GameObject implements Updateable {
    private int TID;
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;
    private float R;
    private float ROTATIONSPEED = 3.0f;

    private long parryWindow = 500;
    private long timeWhenSpawned;

    private final int hitboxWidth = 120;
    private final int hitboxHeight = 120;

    public Parry(float x, float y, float angle, float r, int TID) {
        this.angle = angle;
        this.R = r;
        this.x = x-30;
        this.y = y-30;
        this.vx = 0;
        this.vy = 0;
        this.TID = TID;
        this.timeWhenSpawned = System.currentTimeMillis();
        //load animation here
        super.hitbox = new Rectangle((int)x,(int)y, hitboxWidth,hitboxHeight);
    }

    public int getTID(){
        return this.TID;
    }

    public Rectangle getHitbox() {
        return hitbox.getBounds();
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    public void update(GameWorld gw) {
        long currentTime = System.currentTimeMillis();
        if(currentTime - timeWhenSpawned > parryWindow) {
            gw.gameObjects.remove(this);
        }
        else {
            for (int i = gw.gameObjects.size() - 1; i >= 0; i--) {
                if (gw.gameObjects.get(i) instanceof Tank t) {
                    if (t.getTID() == this.TID) {
                        this.x = t.getX() - 30;
                        this.y = t.getY() - 30;
                    }
                }
            }
            super.hitbox = new Rectangle((int) x, (int) y, hitboxWidth, hitboxHeight);
            checkBorder();
        }
    }

    private void checkBorder() {
        if (x < 100) {
            x = 100;
        }
        if (x > (GameConstants.WORLD_RESOLUTION_WIDTH-100)){
            x = GameConstants.WORLD_RESOLUTION_WIDTH-100;
        }
        if (y < 100) {
            y = 100;
        }
        if (y > (GameConstants.WORLD_RESOLUTION_HEIGHT)-100){
            y = GameConstants.WORLD_RESOLUTION_HEIGHT-100;
        }
        //if (y >= GameConstants.GAME_SCREEN_HEIGHT - 80) {y = GameConstants.GAME_SCREEN_HEIGHT - 80;}
    }
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}


    public void draw(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x-15, y-15);
        //rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.green);
        g2d.draw(super.hitbox);

    }
}
