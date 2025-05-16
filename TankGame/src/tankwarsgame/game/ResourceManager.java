package tankwarsgame.game;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();
    private final static Map<String, Sound> sounds = new HashMap<String, Sound>();
    private final static Map<String, List<BufferedImage>> animationMap = new HashMap<String, List<BufferedImage>>();
    private final static Map<String, Integer> animInfo = new HashMap<>(){{
        put("parry", 14);
        put("explosion_lg", 7);
    }};

    public static void initSprites() throws IOException {
        ResourceManager.sprites.put("t1", loadSprite("tank/tank1.png"));
        ResourceManager.sprites.put("t2", loadSprite("tank/tank2.png"));
        ResourceManager.sprites.put("wall1", loadSprite("tank/wall1.png"));
        ResourceManager.sprites.put("wall2", loadSprite("tank/wall2.png"));
        ResourceManager.sprites.put("wall3", loadSprite("tank/wall3.png"));
        ResourceManager.sprites.put("blanktile", loadSprite("blanktile.png"));
        ResourceManager.sprites.put("health", loadSprite("maps/Medkit.png"));
        ResourceManager.sprites.put("speed", loadSprite("maps/Speedup.png"));
        ResourceManager.sprites.put("damage", loadSprite("maps/Damage.png"));
        ResourceManager.sprites.put("bullet_1", loadSprite("lasers/11.png"));
        ResourceManager.sprites.put("bullet_2", loadSprite("lasers/12.png"));
        ResourceManager.sprites.put("bullet_3", loadSprite("lasers/13.png"));
        ResourceManager.sprites.put("bullet_4", loadSprite("lasers/14.png"));
        ResourceManager.sprites.put("bullet_5", loadSprite("lasers/chair.png"));
        ResourceManager.sprites.put("background", loadSprite("tank/wafflehousestage.jpg"));
        ResourceManager.sprites.put("player1wins", loadSprite("tank/player1wins.png"));
        ResourceManager.sprites.put("player2wins", loadSprite("tank/player2wins.png"));
    }

    public static BufferedImage loadSprite (String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResource(path), "Image %s not be found.".formatted(path) ));
    }
    public static void loadAssets(){
        try{
            initSprites();
            loadAnims();
            loadSounds();
        }
        catch(IOException e){
            throw new RuntimeException("Loading assets failed", e);
        }

    }
    public static void loadAnims(){
        String format = "Animations/%s/%s_%04d.png";
        ResourceManager.animInfo.forEach((animationName, frameCount) ->{
            List<BufferedImage> frames = new ArrayList<BufferedImage>(frameCount);
            try{
                for (int i = 1; i < frameCount; i++) {
                    String path = String.format(format, animationName, animationName, i);
                    System.out.println("Loading animation: " + path);
                    frames.add(loadSprite(path));
                }
                ResourceManager.animationMap.put(animationName, frames);
            }
            catch(IOException e){
                throw new RuntimeException("Loading animation failed", e);
            }

        });
    }

    public static List<BufferedImage> getAnimations(String key){
        return animationMap.get(key);
    }

    public static void loadSounds(){
        try {
            ResourceManager.sounds.put("background", loadSound("Sounds/background.wav"));
            ResourceManager.sounds.put("brawl sounds", loadSound("Sounds/brawl.wav"));
            ResourceManager.sounds.put("chair" , loadSound("Sounds/chair.wav"));
            ResourceManager.sounds.put("beam" , loadSound("Sounds/beam.wav"));
            ResourceManager.sounds.put("parry" , loadSound("Sounds/parry.wav"));
            ResourceManager.sounds.put("damage" , loadSound("Sounds/damage.wav"));
            ResourceManager.sounds.put("death", loadSound("Sounds/death.wav"));
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private static Sound loadSound(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResource(path),
                        "Sound %s not found.".formatted(path)
                )
        );

        Clip c = AudioSystem.getClip();
        c.open(ais);
        Sound s = new Sound(c);
        return s;
    }

    public static Sound getSound(String key){
        return sounds.get(key);
    }
    public static BufferedImage getSprite(String key){
        if(!sprites.containsKey(key)){
            throw new IllegalArgumentException("Sprite %s not found".formatted(key));
        }
        return ResourceManager.sprites.get(key);
    }
    public static void main(String[] args){
        ResourceManager.loadAssets();
    }
}
