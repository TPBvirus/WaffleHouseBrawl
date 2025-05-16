package tankwarsgame.game;

import tankwarsgame.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject implements Updateable {
    private int TID;
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;
    private float R = 7;
    private float ROTATIONSPEED = 3.0f;
    private int damage;
    private BufferedImage img;

    private long timeShot;
    private final int shotDuration = 4000;

    private final int hitboxWidth = 40;
    private final int hitboxHeight = 20;

    public Bullet(float x, float y, float angle, int damage, int TID) {

        this.damage = damage;
        this.angle = angle;
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        switch (damage) {
            case 1:
                this.img = ResourceManager.getSprite("bullet_1");
                super.hitbox = new Rectangle((int) x + 100 , (int)y + 50, hitboxWidth, hitboxHeight);
                break;
            case 2:
                this.img = ResourceManager.getSprite("bullet_2");
                super.hitbox = new Rectangle((int) x + 100 , (int)y + 50, hitboxWidth, hitboxHeight);
                break;
            case 3:
                this.img = ResourceManager.getSprite("bullet_3");
                super.hitbox = new Rectangle((int) x + 100 , (int)y + 50, hitboxWidth, hitboxHeight);
                break;
            case 4:
                this.img = ResourceManager.getSprite("bullet_4");
                super.hitbox = new Rectangle((int) x + 100 , (int)y + 50, hitboxWidth, hitboxHeight);
                break;
            case 5:
                this.img = ResourceManager.getSprite("bullet_5");
                super.hitbox = new Rectangle((int) x + 100 , (int)y + 50, hitboxWidth, hitboxHeight);
                break;
            default:
                this.img = ResourceManager.getSprite("bullet_5");
                super.hitbox = new Rectangle((int) x + 100 , (int)y + 50, hitboxWidth+ 20, hitboxHeight+20);
                break;
        }
        this.timeShot = System.currentTimeMillis();
        this.TID = TID;

    }

    public int getDamage(){
        return this.damage;
    }

    public int getTID(){
        return this.TID;
    }

    public float getX(){ return this.x;}
    public float getY(){ return this.y;}

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
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        if(this.damage >=5 ){
            super.hitbox = new Rectangle((int) x + 100 , (int)y + 50, hitboxWidth+20, hitboxHeight+20);
        }
        else{
            super.hitbox = new Rectangle((int) x + 100, (int) y + 50, hitboxWidth, hitboxHeight);
        }
        if(System.currentTimeMillis() - timeShot > shotDuration){
            gw.gameObjects.remove(this);
        }
        checkBorder();
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
    public void setAngle(float angle) {this.angle = angle;}
    public void setRotationspeed(float rotationspeed) {this.ROTATIONSPEED = rotationspeed;}
    public void setDamage(int damage) {this.damage = damage;}


    public void draw(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        g2d.draw(super.hitbox);
        g2d.drawImage(this.img, rotation, null);

    }
}
