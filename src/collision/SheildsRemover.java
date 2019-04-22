package collision;

import game.GameLevel;
import gameobjects.Ball;
import gameobjects.Block;

/**
 * The type Sheilds remover.
 */
public class SheildsRemover implements HitListener {

    private GameLevel game;

    /**
     * Instantiates a new Sheilds remover.
     *
     * @param game the game
     */
    public SheildsRemover(GameLevel game) {
        this.game = game;
    }

    /**
     * hit.
     * @param beingHit the block thats being hit
     * @param hitter   The hitter parameter is the gameobjects.Ball that's doing the hitting.
     */
    public void hitEvent(Collidable beingHit, Ball hitter) {

        if (beingHit instanceof Block) {
            Block blockHit = (Block) beingHit;
            blockHit.removeHitListener(this);
            blockHit.removeFromGame(this.game);
        }
    }


}
