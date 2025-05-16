package tankwarsgame.game;

import tankwarsgame.GameConstants;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject implements Updateable {

    private int TID;

    private float x;
    private float y;
    private float vx;
    private float vy;
    private float screen_x;
    private float screen_y;
    private float angle;
    private int health;
    private int damage;

    private boolean isDead = false;


    private float R = 2 ;
    private float ROTATIONSPEED = 3.0f;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;
    private boolean ParryPressed;

    private long reloadTime = 600;
    private long timeSinceLastShot = 0;
    private long parryTime = 1000;
    private long timesinceLastParried = 0;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.screen_x = x;
        this.screen_y = y;
        this.img = img;
        this.angle = angle;
        this.health = 20;
        this.damage = 1;
        this.TID = new Random().nextInt(2000);
        System.out.println(this.R);
        super.hitbox = new Rectangle((int) x, (int) y, img.getWidth(), img.getHeight());
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

    float getX(){ return x;}

    float getY(){ return y;}

    float getScreen_x(){ return screen_x;}

    float getScreen_y(){ return screen_y;}

    float getAngle(){ return angle;}

    float getVx(){ return vx;}

    float getVy(){ return vy;}

    float getR(){ return R;}

    void setVx(float vx){ this.vx = vx;}

    void setVy(float vy){ this.vy = vy;}

    public int getTID(){
        return this.TID;
    }

    public int getHealth(){
        return this.health;
    }

    //if tank dies signal gameworld that game is over and reset
    public void setHealth(int damage){
        this.health -= damage;
        if(this.health <= 0){
            this.isDead = true;
        }
    }

    public boolean isDead(){
        return this.isDead;
    }

    public void increaseDamage(Damage d){
        this.damage += d.getDamage();
    }

    public void increaseSpeed(Speed s){
        this.R += s.getSpeedBuff();
    }

    public void increaseHealth(Health h){
        this.health += h.getHealthRestored();
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void toggleShootPressed() {this.ShootPressed = true;}

    void toggleParryPressed() { this.ParryPressed = true; }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void unToggleShootPressed() { this.ShootPressed = false; }

    void unToggleParryPressed() { this.ParryPressed = false;}



    public void update(GameWorld gw) {
        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }

        long currentTime = System.currentTimeMillis();
        if (this.ShootPressed && currentTime > this.timeSinceLastShot + this.reloadTime){
            this.timeSinceLastShot = currentTime;
            this.Shoot(gw);
        }

        if ( this.ParryPressed && currentTime > this.timesinceLastParried + this.parryTime){
            this.timesinceLastParried = currentTime;
            this.Parry(gw);
        }

        super.hitbox = new Rectangle((int) x, (int) y, img.getWidth(), img.getHeight());
        checkCenter();
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void Shoot(GameWorld gw){
        //sets bullet width and height
        float width = ResourceManager.getSprite("bullet_1").getWidth() / 4f;
        float height = ResourceManager.getSprite("bullet_1").getHeight() / 4f;

        //init bullet
        Bullet b = new Bullet(x - width, y - height, this.angle, this.damage, this.TID );
        gw.addGameObject(b);
        System.out.println(width + " " + height);
        System.out.println(x  + " " + y + " " + this.angle);
        if (this.damage >= 5){
            Sound chairThrow = ResourceManager.getSound("chair");
            chairThrow.setVolume(-15);
            chairThrow.play();
        }
        else{
            Sound beam = ResourceManager.getSound("beam");
            beam.setVolume(-10);
            beam.play();
        }
    }

    private void Parry(GameWorld gw){
        Parry p = new Parry(x, y, this.angle, this.R, this.TID);
        Animations parryAnimation = new Animations(this.x -50, this.y-50, ResourceManager.getAnimations("parry"));
        gw.addAnimation(parryAnimation);
        gw.addGameObject(p);
        System.out.println(x + " " + y + " " + this.angle);
    }

    private void moveBackwards() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
       checkBorder();
    }

    public void moveFromWall() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }


    private void checkCenter(){
        this.screen_x = this.x - GameConstants.GAME_SCREEN_WIDTH/4f;
        this.screen_y = this.y - GameConstants.GAME_SCREEN_HEIGHT/2f;

        if(this.screen_x < 0){
            this.screen_x = 0;
        }
        if(this.screen_y < 0){
            this.screen_y = 0;
        }

        if(screen_x > (GameConstants.WORLD_RESOLUTION_WIDTH - GameConstants.GAME_SCREEN_WIDTH/2f) ){
            this.screen_x = GameConstants.WORLD_RESOLUTION_WIDTH - GameConstants.GAME_SCREEN_WIDTH/2f;
        }
        if(screen_y > GameConstants.WORLD_RESOLUTION_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT){
            this.screen_y = GameConstants.WORLD_RESOLUTION_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT;
        }

    }
    private void checkBorder() {
        if (x < 100) {
            x = 100;
        }
        if (x > (GameConstants.WORLD_RESOLUTION_WIDTH-150)){
            x = GameConstants.WORLD_RESOLUTION_WIDTH-150;
        }
        //if (x >= GameConstants.GAME_SCREEN_WIDTH - 88) {x = GameConstants.GAME_SCREEN_WIDTH - 88;}
        if (y < 100) {
            y = 100;
        }
        if (y > (GameConstants.WORLD_RESOLUTION_HEIGHT)-150){
            y = GameConstants.WORLD_RESOLUTION_HEIGHT-150;
        }
        //if (y >= GameConstants.GAME_SCREEN_HEIGHT - 80) {y = GameConstants.GAME_SCREEN_HEIGHT - 80;}
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void draw(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        g2d.draw(super.hitbox);
        drawHealthBar(g2d);
        g2d.drawImage(this.img, rotation, null);


    }

    public void drawHealthBar(Graphics2D g2d) {
        if(health >= 20){
            g2d.setColor(Color.cyan);
            g2d.fillRect((int)x-10, (int)y - 10 , health*3, 10);
        }
        else if(health > 15){
            g2d.setColor(Color.GREEN);
            g2d.fillRect((int)x-10, (int)y - 10 , health*4, 10);
        }
        else if(health >= 10){
            g2d.setColor(Color.YELLOW);
            g2d.fillRect((int)x-10, (int)y - 10 , health*3, 10);
        }
        else if (health < 10){
            g2d.setColor(Color.RED);
            g2d.fillRect((int)x-10, (int)y - 10 , health*2, 10);
        }
    }
}
