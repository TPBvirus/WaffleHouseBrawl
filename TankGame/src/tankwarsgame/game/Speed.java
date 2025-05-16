package tankwarsgame.game;

import tankwarsgame.GameConstants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Speed extends GameObject {
    private float speedBuff = 1;

    private int x;
    private int y;

    private BufferedImage img;

    public Speed(int x, int y, float buff) {
        this.x = x;
        this.y = y;
        this.speedBuff = buff;
        this.img = ResourceManager.getSprite("speed");
        super.hitbox = new Rectangle(x+20, y+20, GameConstants.NON_PLAYER_OBJECT_HITBOX_WIDTH_HEIGHT, GameConstants.NON_PLAYER_OBJECT_HITBOX_WIDTH_HEIGHT);
    }

    public float getSpeedBuff() {
        return speedBuff;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.cyan);
        g2d.draw(super.hitbox);
        g2d.drawImage(this.img, x, y, null);
    }
}
