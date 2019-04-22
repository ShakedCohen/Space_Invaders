package aliens;

import game.GameLevel;
import gameobjects.Ball;
import gameobjects.Block;
import gameobjects.Counter;
import gameobjects.Velocity;
import geometry.Point;
import geometry.Rectangle;
import sprite.RectBackground;

import java.awt.Color;
import java.util.Map;

/**
 * The type Alien.
 */
public class Alien extends Block {


    private Point initialUpper;

    private GameLevel game;
    private AlienBundle swarm;
    private Counter score;

    /**
     * Instantiates a new Alien.
     *
     * @param shape1     the shape 1
     * @param stroke1    the stroke 1
     * @param hitPoints1 the hit points 1
     * @param fillForHP1 the fill for hp 1
     * @param game1      the game 1
     * @param score      the score
     */
    public Alien(Rectangle shape1, Color stroke1, int hitPoints1, Map<Integer, RectBackground> fillForHP1,
                 GameLevel game1, Counter score) {
        super(shape1, stroke1, hitPoints1, fillForHP1);
        this.initialUpper = new Point(this.getShape().getUpperLeft());
        this.game = game1;
        this.score = score;
    }

    /**
     * Sets swarm.
     *
     * @param swarm1 the swarm 1
     */
    public void setSwarm(AlienBundle swarm1) {
        this.swarm = swarm1;
    }

    /**
     * Sets to initial.
     */
    public void setToInitial() {
        this.setUpperLeft(new Point(this.initialUpper));
    }

    /**
     * sets upper left.
     * @param upperLeft upperleft
     */
    public void setUpperLeft(Point upperLeft) {
        super.setUpperLeft(upperLeft);
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(int y) {
        super.setUpperY(y);
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(int x) {
        super.setUpperX(x);
    }


    /**
     * notify the sprite that time has passed.
     *
     * @param dt the dt
     */
    @Override
    public void timePassed(double dt) {
    }

    /**
     * Notify the object that we collided with it at collisionPoint with a given velocity.
     * The return is the new velocity expected after the hit
     * (based on the force the object inflicted on us).
     *
     * @param hitter          the hitter ball
     * @param collisionPoint  the col point
     * @param currentVelocity the cur velocity
     * @return the new velocity
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        if (hitter.isPaddleShot()) {
            this.removeFromGame(game);
            this.swarm.removeFromSwarm(this);
            hitter.removeFromGame(game);
            this.score.increase(100);

            //this.swarm.removeFromGame(game);
            //this.swarm.removeFromSwarm(this);
        } else {
            hitter.removeFromGame(game);
        }
        return null;
    }

    /**
     * Move.
     *
     * @param dist the dist
     */
    public void move(double dist) {
        super.setUpperX(((int) super.getShape().getUpperLeft().getX() + (int) dist));
    }

    /**
     * add block to gameLevel.
     *
     * @param gameLevel the gameLevel obj
     */
    @Override
    public void addToGame(GameLevel gameLevel) {
        gameLevel.addCollidable(this);
    }


}
