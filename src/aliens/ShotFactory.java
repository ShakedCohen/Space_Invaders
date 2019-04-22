package aliens;

import game.Constants;
import game.GameEnvironment;
import game.GameLevel;
import gameobjects.Ball;
import gameobjects.Velocity;
import geometry.Point;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Shot factory.
 */
public class ShotFactory {


    private GameLevel game;
    private GameEnvironment gameEnv;
    private List<Ball> aliensShotsOnGame = new ArrayList<Ball>();
    private List<Ball> paddleShots = new ArrayList<Ball>();

    /**
     * Instantiates a new Shot factory.
     *
     * @param gameLevel       the game level
     * @param gameEnvironment the game environment
     */
    public ShotFactory(GameLevel gameLevel, GameEnvironment gameEnvironment) {
        this.game = gameLevel;
        this.gameEnv = gameEnvironment;
    }

    /**
     * Create alien shot.
     *
     * @param initPoint the init point
     */
    public void createAlienShot(Point initPoint) {
        Ball shot = new Ball(new Point(initPoint.getX(), initPoint.getY() + 10), 3, Color.red,
                this.gameEnv, false);
        shot.setVelocity(Velocity.fromAngleAndSpeed(180, Constants.SHOT_SPEED));
        shot.addToGame(game);
        aliensShotsOnGame.add(shot);
    }

    /**
     * Remove alien shots from game.
     */
    public void removeAlienShotsFromGame() {
        for (Ball shot : this.aliensShotsOnGame) {
            shot.removeFromGame(game);
        }
    }

    /**
     * Create paddle shot.
     *
     * @param initPoint the init point
     */
    public void createPaddleShot(Point initPoint) {
        Ball shot = new Ball(new Point(initPoint.getX(), initPoint.getY() - 10), 3,
                Color.white, this.gameEnv, true);
        shot.setVelocity(Velocity.fromAngleAndSpeed(0, Constants.SHOT_SPEED + 100));
        shot.addToGame(game);
        paddleShots.add(shot);
    }

    /**
     * Remove paddle shots from game.
     */
    public void removePaddleShotsFromGame() {
        for (Ball shot : this.paddleShots) {
            shot.removeFromGame(game);
        }
    }


}
