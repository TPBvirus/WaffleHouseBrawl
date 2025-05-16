package tankwarsgame.game;

import tankwarsgame.GameConstants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Damage extends GameObject {
    private int damage;
    private int x;
    private int y;
    private BufferedImage img;

    public Damage(int x, int y, int damage) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        super.hitbox = new Rectangle(x+20, y+20, GameConstants.NON_PLAYER_OBJECT_HITBOX_WIDTH_HEIGHT, GameConstants.NON_PLAYER_OBJECT_HITBOX_WIDTH_HEIGHT);
        this.img = ResourceManager.getSprite("damage");
    }
    public int getDamage(){
        return this.damage;
    }
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.cyan);
        g2d.draw(super.hitbox);
        g.drawImage(this.img, x, y, null);
    }
}
