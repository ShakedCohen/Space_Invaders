package collision;

import aliens.Alien;
import gameobjects.Ball;
import gameobjects.Counter;

/**
 * The type score tracking listener.
 */
public class ScoreTrackingListener implements HitListener {

    private Counter currentScore;

    /**
     * Instantiates a new score tracking listener.
     *
     * @param scoreCounter the score counter
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }


    /**
     * This method is called whenever the beingHit object is hit.
     *
     * @param beingHit the block that is being hit
     * @param hitter   The hitter parameter is the gameobjects.Ball that's doing the hitting.
     */
    @Override
    public void hitEvent(Collidable beingHit, Ball hitter) {
        if (beingHit instanceof Alien) {
            this.currentScore.increase(100);
        }
    }
}
