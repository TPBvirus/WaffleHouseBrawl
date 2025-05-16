package tankwarsgame.game;

import tankwarsgame.GameConstants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Health extends GameObject {
    private int healthRestored;
    private int x;
    private int y;
    private BufferedImage img;

    public Health( int x, int y, int health) {
        this.x = x;
        this.y = y;
        this.healthRestored = health;
        this.img = ResourceManager.getSprite("health");
        super.hitbox = new Rectangle(x+20, y+20, GameConstants.NON_PLAYER_OBJECT_HITBOX_WIDTH_HEIGHT, GameConstants.NON_PLAYER_OBJECT_HITBOX_WIDTH_HEIGHT);
    }
    public int getHealthRestored() {
        return healthRestored;
    }
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.cyan);
        g2d.draw(super.hitbox);
        g.drawImage(this.img, x, y, null);
    }
}
