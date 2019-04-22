package sprite;

import biuoop.DrawSurface;
import game.Constants;

import java.awt.Color;

/**
 * The type Si background.
 */
public class SIBackground implements Sprite {
    /**
     * draw the sprite to the screen.
     *
     * @param d the drawsurface
     */
    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(Color.black);
        d.fillRectangle(0, 0, Constants.GUI_WIDTH, Constants.GUI_HEIGHT);
    }

    /**
     * notify the sprite that time has passed.
     *
     * @param dt the dt
     */
    @Override
    public void timePassed(double dt) {

    }
}
