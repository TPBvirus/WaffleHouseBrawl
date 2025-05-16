package tankwarsgame.game;


import tankwarsgame.GameConstants;
import tankwarsgame.Launcher;
import tankwarsgame.menus.EndGamePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private int p1Lives = 2;
    private int p2Lives = 2;
    private final Launcher lf;
    private long timeSinceLastLifeP1 = 0 ;
    private long timeSinceLastLifeP2 = 0 ;
    private long tick = 0;

    ArrayList<GameObject> gameObjects = new ArrayList<GameObject>(5000);
    List<Animations> anims = new ArrayList<>();
    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        Sound bg = ResourceManager.getSound("brawl sounds");
        Sound deathSound = ResourceManager.getSound("death");
        deathSound.setVolume(5);
        bg.loopContinuously();
        bg.setVolume(-5);
        bg.play();
        try {
            while (true) {
                this.tick++;
                for (int i = this.gameObjects.size() - 1; i >= 0; i--){
                    if(this.gameObjects.get(i) instanceof Updateable updateable){
                        updateable.update(this);// update tank
                    }else{
                        break;
                    }
                }
                for(int i = 0; i < this.anims.size(); i++){
                    this.anims.get(i).update();
                }

                this.checkCollisions();
                this.gameObjects.removeIf(g -> g.hasCollided);

                //if either of these happen end game
                if(this.t1.isDead() ){
                    deathSound.play();
                    System.out.println("PLAYER 2 WINS: FLAWLESS VICTORY");
                    bg.stop();
                    resetGame();
                    this.p1Lives--;
                    System.out.println(this.p1Lives);

                }
                if(this.t2.isDead()){
                    deathSound.play();
                    System.out.println("PLAYER 1 WINS: FLAWLESS VICTORY");
                    bg.stop();
                    resetGame();
                    this.p2Lives--;
                    System.out.println(this.p2Lives);
                }
                if(this.p2Lives < -1){
                    lf.setFrame("end");
                    resetGame();
                    resetGame();
                    this.p1Lives =2;
                    this.p2Lives =2;
                    JOptionPane.showMessageDialog(null,"PLAYER 1 WINS");
                    break;
                }
                if (this.p1Lives < -1){
                    lf.setFrame("end");
                    resetGame();
                    this.p1Lives =2;
                    this.p2Lives =2;
                    JOptionPane.showMessageDialog(null,"PLAYER 2 WINS");
                    break;
                }

                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println("ended game");
        }

    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.gameObjects.clear();
        InitializeGame();
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.WORLD_RESOLUTION_WIDTH,
                GameConstants.WORLD_RESOLUTION_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        InputStreamReader isr = new InputStreamReader(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResourceAsStream("maps/gameMap.csv")
                )
        );
        try(BufferedReader mapReader = new BufferedReader(isr)){
            int row = 0;
            while(mapReader.ready()){
                String line = mapReader.readLine();
                String[] tokens = line.split(",");
                System.out.println(Arrays.toString(tokens));

                for(int col = 0; col < tokens.length; col++){
                    String gameItems = tokens[col];
                    this.gameObjects.add(GameObject.newInstance(gameItems, col*100, row*100));
                }
                row++;
            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        t1 = new Tank(400, 1300, 0, 0, (short) 0, ResourceManager.getSprite("t1"));
        t2 = new Tank(2200, 1300, 0, 0, (short) 180, ResourceManager.getSprite("t2"));
        this.addGameObject(t1);
        this.addGameObject(t2);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE, KeyEvent.VK_F);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_ENTER, KeyEvent.VK_U);
        this.lf.getJf().addKeyListener(tc2);
        this.lf.getJf().addKeyListener(tc1);
    }

    static double scale_factor = .075;
    private void displayMinimap(Graphics2D onScreenPanel){
        BufferedImage mm = this.world.getSubimage(0,0,GameConstants.WORLD_RESOLUTION_WIDTH , GameConstants.WORLD_RESOLUTION_HEIGHT);
        double mmx = GameConstants.GAME_SCREEN_WIDTH/2.0 - (GameConstants.WORLD_RESOLUTION_WIDTH*scale_factor)/2;
        double mmy = 0;
        AffineTransform scaler = AffineTransform.getTranslateInstance(mmx,mmy);
        scaler.scale(scale_factor,scale_factor);
        onScreenPanel.drawImage(mm,scaler,null);
    }

    private void displaySplitScreen(Graphics2D onScreenPanel){
        BufferedImage left = this.world.getSubimage((int)this.t1.getScreen_x(), (int)this.t1.getScreen_y() , GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage right = this.world.getSubimage((int)this.t2.getScreen_x(), (int)this.t2.getScreen_y() , GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT);
        onScreenPanel.drawImage(left, 0, 0 ,null);
        onScreenPanel.drawImage(right, GameConstants.GAME_SCREEN_WIDTH/2, 0, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        buffer.drawImage(ResourceManager.getSprite("background"), 0, 0, GameConstants.WORLD_RESOLUTION_WIDTH, GameConstants.WORLD_RESOLUTION_HEIGHT, null);

        this.gameObjects.forEach(obj -> obj.draw(buffer));

        for(int i = 0; i < this.anims.size(); i++){
            this.anims.get(i).render(buffer);
        }

        this.displaySplitScreen(g2);
        this.displayMinimap(g2);

        long currentTimeP1 = System.currentTimeMillis();
        long currentTimeP2 = System.currentTimeMillis();
        int delay = 150;
        for(int i = 0 ; i <= this.p1Lives; i++){
            if(i == this.p1Lives && currentTimeP1 > delay + timeSinceLastLifeP1) {
                this.timeSinceLastLifeP1 = currentTimeP1;
                g2.drawImage(ResourceManager.getSprite("t1"), i * 50, 20, null);
            }
        }
        for(int i = 0 ; i <= this.p1Lives-1; i++){
            g2.drawImage(ResourceManager.getSprite("t1"), i * 50, 20, null);
        }
        for(int i = 0 ; i <= this.p2Lives; i++){
            if(i == this.p2Lives && currentTimeP2 > delay + timeSinceLastLifeP2){
                this.timeSinceLastLifeP2 = currentTimeP2;
                g2.drawImage(ResourceManager.getSprite("t2"), 1300 - i*50, 20, null );
            }
        }
        for(int i = 0 ; i <= this.p2Lives-1; i++){
            g2.drawImage(ResourceManager.getSprite("t2"), 1300 - i * 50, 20, null);
        }
        //g2.drawImage(world, 0, 0, null);
    }

    private void checkCollisions(){
        for( int i = 0; i < this.gameObjects.size(); i++){
            GameObject obj1 = this.gameObjects.get(i);
            for (int k = 0; k < this.gameObjects.size(); k++){
                GameObject obj2 = this.gameObjects.get(k);
                if(obj1 instanceof Tank t){
                    if(t.getHitbox().intersects(obj2.getHitbox())){
                        this.handleTankCollision(t, obj2);
                    }
                }
                if(obj1 instanceof Bullet b){
                    if(b.getHitbox().intersects(obj2.getHitbox())){
                        this.handleBulletCollision(b, obj2);
                    }
                }
            }

        }
    }

    public void handleTankCollision(Tank t, GameObject obj){
        if(obj instanceof Wall){
            t.moveFromWall();
        }
        if(obj instanceof Bullet b){
            if(b.getTID() != t.getTID()){
                System.out.println("HIT BY ENEMY TANK");
                System.out.println(t.getHealth());
                t.setHealth(b.getDamage());
                System.out.println("Remaining health:" + t.getHealth());
                Animations explosion = new Animations(b.getX() + 50, b.getY()+50, ResourceManager.getAnimations("explosion_lg"));
                addAnimation(explosion);
                Sound damage = ResourceManager.getSound("damage");
                damage.play();
                b.setHasCollided(true);
            }
        }
        if(obj instanceof Damage d){
            t.increaseDamage(d);
            d.setHasCollided(true);
        }
        if(obj instanceof Speed s){
            t.increaseSpeed(s);
            s.setHasCollided(true);
        }
        if(obj instanceof Health h){
            t.increaseHealth(h);
            h.setHasCollided(true);
        }
    }

    public void handleBulletCollision(Bullet b, GameObject obj){
        if(obj instanceof Wall w){
            switch(w.getWallType()){
                case 1:
                    System.out.println("Unbreakable");
                    w.damage(b.getDamage());
                    System.out.println(w.getHealth());
                    w.resetWallType();
                    b.setHasCollided(true);
                    break;
                case 2:
                    System.out.println("Breakable 1");
                    w.damage(b.getDamage());
                    System.out.println(w.getHealth());
                    w.resetWallType();
                    b.setHasCollided(true);
                    break;
                case 3:
                    System.out.println("Breakable 2");
                    w.damage(b.getDamage());
                    System.out.println(w.getHealth());
                    w.resetWallType();
                    b.setHasCollided(true);
                    break;
                default:
                    break;
            }
            Animations explosion = new Animations(b.getX() + 50, b.getY()+50, ResourceManager.getAnimations("explosion_lg"));
            addAnimation(explosion);
        }
        if (obj instanceof Parry p){
            Sound parry = ResourceManager.getSound("parry");
            parry.setVolume(-5);
            parry.play();
            b.setHasCollided(true);
            p.setHasCollided(true);
        }

    }
    public void addGameObject(GameObject obj){
        this.gameObjects.add(obj);
    }

    public void removeGameObject(GameObject obj){
        this.gameObjects.remove(obj);
    }

    public void addAnimation(Animations a){
        this.anims.add(a);
    }
}
