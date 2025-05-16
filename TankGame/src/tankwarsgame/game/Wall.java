package tankwarsgame.game;

import tankwarsgame.GameConstants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObject {
    private int wallType; //depending on the walltype change the health
    private int x;
    private int y;
    private int health;
    private BufferedImage img;

    //get rid of hitbox when health reduced to 0
    public Wall(int wallType, int x, int y) {
        this.wallType = wallType;
        switch(wallType){
            case 1:
                this.health = 1000;
                this.img = ResourceManager.getSprite("wall1");
                super.hitbox = new Rectangle(x, y, GameConstants.NON_PLAYER_OBJECT_HITBOX_WIDTH_HEIGHT, GameConstants.NON_PLAYER_OBJECT_HITBOX_WIDTH_HEIGHT);
                break;
            case 2:
                this.health = 10;
                this.img = ResourceManager.getSprite("wall2");
                super.hitbox = new Rectangle(x, y, GameConstants.NON_PLAYER_OBJECT_HITBOX_WIDTH_HEIGHT, GameConstants.NON_PLAYER_OBJECT_HITBOX_WIDTH_HEIGHT);
                break;
            case 3:
                this.health = 5;
                this.img = ResourceManager.getSprite("wall3");
                super.hitbox = new Rectangle(x, y, GameConstants.NON_PLAYER_OBJECT_HITBOX_WIDTH_HEIGHT, GameConstants.NON_PLAYER_OBJECT_HITBOX_WIDTH_HEIGHT);
                break;
            default:
                this.health = 0;
                this.img = ResourceManager.getSprite("blanktile");
                super.hitbox = new Rectangle(0,0, -1, -1); // instantiate a negative size hurtbox to show this does not have a hurtbox
                break;
        }
        this.x = x;
        this.y = y;

    }

    public void damage(int damage) {
        this.health -= damage;
    }

    public int getHealth() {
        return this.health;
    }
    /**
     * Fetches remaining health of the wall and updates the sprite to match
     */
    public void resetWallType(){
        if (this.health <= 10){
            this.img = ResourceManager.getSprite("wall2");
        }
        if (this.health <= 5){
            this.img = ResourceManager.getSprite("wall3");
        }
        if (this.health <= 0){
            this.img = ResourceManager.getSprite("blanktile");
            super.hitbox = new Rectangle(0,0, -1, -1);
        }
    }

    public int getWallType(){
        return this.wallType;
    }
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.green);
        if(super.hitbox != null){
            g2d.draw(super.hitbox);
        }
        if(this.health < 20){
            drawHealthBar(g2d);
        }
        g2d.drawImage(this.img, x, y, null);
    }

    public void drawHealthBar(Graphics2D g2d) {
        if(health > 5){
            g2d.setColor(Color.GREEN);
            g2d.fillRect((int)x-10, (int)y - 10 , health*5, 10);
        }
        else if(health >= 3){
            g2d.setColor(Color.YELLOW);
            g2d.fillRect((int)x-10, (int)y - 10 , health*4, 10);
        }
        else if (health < 2){
            g2d.setColor(Color.RED);
            g2d.fillRect((int)x-10, (int)y - 10 , health*3, 10);
        }
    }
}
