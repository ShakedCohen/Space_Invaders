package indicators;

import biuoop.DrawSurface;
import game.Constants;
import gameobjects.Counter;
import sprite.Sprite;

import java.awt.Color;

/**
 * The type Level indicator.
 */
public class LevelIndicator implements Sprite {

    private Counter levelNum;

    /**
     * Instantiates a new Level indicator.
     *
     * @param levelNum the level name
     */
    public LevelIndicator(Counter levelNum) {
        this.levelNum = levelNum;
    }

    /**
     * draw the sprite to the screen.
     *
     * @param d the drawsurface
     */
    @Override
    public void drawOn(DrawSurface d) {

        final int panelHeight = Constants.SCORE_PANEL_HEIGHT;

        d.setColor(Color.black);
        int x = (int) (d.getWidth() * 0.60);
        int y = (int) (panelHeight * 0.75);
        String s = "Level Name: " + "Battle no. " + this.levelNum.getValue();
        int size = 15;
        d.drawText(x, y, s, size);
    }

    /**
     * passes time.
     * @param dt the dt
     */
    @Override
    public void timePassed(double dt) {

    }
}
