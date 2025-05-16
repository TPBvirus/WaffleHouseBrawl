package tankwarsgame.game;

import java.awt.*;

public abstract class GameObject {
    // add methods common between classes
    protected Rectangle hitbox;
    protected boolean hasCollided = false;

    public static GameObject newInstance(String objType, int x, int y){
        return switch (objType){
            case "9" -> new Wall(1, x, y); // unbreakable
            case "8" -> new Wall(2, x, y); // breakable wall
            case "7" -> new Wall(3, x, y); // half hp wall
            case "6" -> new Damage(x, y, 2); // damage buffs
            case "5" -> new Damage(x,y, 1);
            case "4" -> new Speed(x, y, 1f); // damage buff
            case "3" -> new Speed(x, y, 0.5f); // speed buff
            case "2" -> new Health(x, y, 5); //health buff
            case "1" -> new Health(x, y, 10);
            case "0" -> new Wall(0, x, y); // blank tile
            default -> throw new IllegalArgumentException("Unsupported object type");
        };
    }

    public boolean isCollided(){
        return hasCollided;
    }
    public void setHasCollided(boolean hasCollided){
        this.hasCollided = hasCollided;
    }

    public Rectangle getHitbox() {
        return hitbox.getBounds();
    }

    public abstract void draw(Graphics g);
}
