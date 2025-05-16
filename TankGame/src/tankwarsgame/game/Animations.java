package tankwarsgame.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Animations {
    private float x,y;
    private List<BufferedImage> frames;
    private long delay = 40;
    private int currentFrame = 0;
    private long timeSinceLastFrame = 0;
    private boolean running = false;

    public Animations(float x, float y, List<BufferedImage> frames) {
        this.x = x;
        this.y = y;
        this.frames = frames;
        this.running = true;
    }

    public void update(){
        long currentTime = System.currentTimeMillis();
        if(currentTime - timeSinceLastFrame >= delay){
            timeSinceLastFrame = currentTime;
            this.currentFrame++;
            //if end of animation play once
            if(currentFrame == frames.size()){
                this.running = false;
            }

        }
    }
    public void render(Graphics g){
        if(this.running){
            g.drawImage(frames.get(currentFrame), (int)x, (int)y, null);
        }
    }
}
