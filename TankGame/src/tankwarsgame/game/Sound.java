package tankwarsgame.game;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

    private int loopCount;
    private Clip clip;

    public Sound(Clip c, int loopCount) {
        this.clip = c;
        this.loopCount = loopCount;
        this.clip.loop(this.loopCount);
    }

    public Sound(Clip c) {
        this.clip = c;
        this.loopCount = 0;
    }

    public void play() {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void stop() {
        this.clip.stop();
    }
    public void loopContinuously(){
        this.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void setVolume(float level) {
        FloatControl volume = (FloatControl)  this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        volume.setValue( level );

    }

    public void loop(int loopCount) {
        this.clip.loop(loopCount);
    }
}
