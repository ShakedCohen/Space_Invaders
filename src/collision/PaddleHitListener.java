package collision;

import gameobjects.Ball;


/**
 * The type Paddle hit listener.
 */
public class PaddleHitListener implements HitListener {

    private Boolean hits;


    /**
     * Instantiates a new Paddle hit listener.
     *
     * @param hits the hits
     */
    public PaddleHitListener(Boolean hits) {
        this.hits = hits;
    }

    /**
     * This method is called whenever the beingHit object is hit.
     * first dec the hit points, then calls the hit event
     *
     * @param beingHit the block thats being hit
     * @param hitter   The hitter parameter is the gameobjects.Ball that's doing the hitting.
     */
    @Override
    public void hitEvent(Collidable beingHit, Ball hitter) {
        this.hits = true;
    }
}
