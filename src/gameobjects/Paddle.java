package gameobjects;

import aliens.ShotFactory;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import collision.Collidable;
import collision.HitListener;
import collision.HitNotifier;
import game.Constants;
import game.GameLevel;
import geometry.Point;
import geometry.Rectangle;
import sprite.RectColorBackground;
import sprite.Sprite;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


/**
 * gameobjects.Paddle class. implemets sprite.Sprite and collision.Collidable interfaces.
 * implements methods on a paddle
 */
public class Paddle implements Sprite, Collidable, HitNotifier {
    private biuoop.KeyboardSensor keyboard;
    private Rectangle shape;
    private Color color;
    private int paddleSpeed;
    private List<HitListener> listeners = new ArrayList<HitListener>();
    private boolean beenHit = false;
    private GameLevel game;
    private ShotFactory factory;
    private double timeFromLastShot = 0;

    /**
     * constructor.
     *
     * @param shape       rectangle of paddle
     * @param color       color of the paddle
     * @param paddleSpeed the paddle speed
     * @param keyboard    keyboard sensor for paddle
     * @param game1       the game 1
     * @param factory     the factory
     */
    public Paddle(Rectangle shape, Color color, int paddleSpeed,
                  biuoop.KeyboardSensor keyboard, GameLevel game1,
                  ShotFactory factory) {
        this.shape = shape;
        this.color = color;
        this.keyboard = keyboard;
        this.paddleSpeed = paddleSpeed;
        this.game = game1;
        this.factory = factory;
    }

    /**
     * Sets upper left.
     *
     * @param p the p
     */
    public void setUpperLeft(Point p) {
        this.shape.setUpperLeft(p);
    }

    /**
     * moves the paddle left (if possible).
     *
     * @param dt the dt
     */
    public void moveLeft(double dt) {
        int frameWidth = Constants.FRAME_BLOCK_WIDTH;
        if (this.shape.getUpperLeft().getX() >= frameWidth + 3) {
            this.shape.setUpperLeft(new Point(this.shape.getUpperLeft().getX()
                    - this.paddleSpeed * dt, this.shape.getUpperLeft().getY()));
        }

    }

    /**
     * moves the paddle right (if possible).
     *
     * @param dt the dt
     */
    public void moveRight(double dt) {
        int frameWidth = Constants.FRAME_BLOCK_WIDTH;
        if (this.shape.getUpperLeft().getX() + this.shape.getWidth()
                <= Constants.GUI_WIDTH - frameWidth + 3) {
            this.shape.setUpperLeft(new Point(this.shape.getUpperLeft().getX()
                    + this.paddleSpeed * dt,
                    this.shape.getUpperLeft().getY()));
        }
    }

    /**
     * passes time.
     *
     * @param dt the dt
     */
    public void timePassed(double dt) {
        timeFromLastShot += dt;
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            this.moveLeft(dt);
        }
        if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            this.moveRight(dt);
        }
        if (this.keyboard.isPressed(KeyboardSensor.SPACE_KEY)) {
            this.tryShoot();
        }
    }

    /**
     * Try shoot.
     */
    public void tryShoot() {
        if (this.timeFromLastShot >= 0.35) {
            factory.createPaddleShot(new Point(this.shape.getTopLine().middle()));
            this.timeFromLastShot = 0;
        }
    }

    /**
     * draws the paddle on screen. (sprite interface)
     *
     * @param d the drawsurface
     */
    public void drawOn(DrawSurface d) {
        this.shape.drawRect(d, new RectColorBackground(this.color));
    }

    /**
     * getter for the paddle rect. collidable interface.
     *
     * @return the rect
     */
    public Rectangle getCollisionRectangle() {
        return this.shape;
    }

    /**
     * Is been hit boolean.
     *
     * @return the boolean
     */
    public boolean isBeenHit() {
        if (beenHit) {
            beenHit = false;
            return true;
        }
        return false;
    }

    /**
     * given collision point and current velocity, returns a new velocity after hit (based of ht region).
     * part of collidable interface
     *
     * @param hitter          the hitter ball
     * @param collisionPoint  the collision point
     * @param currentVelocity the current velocity before hit
     * @return new velocity that should be after the hit.
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        this.beenHit = true;
        hitter.removeFromGame(this.game);


//        Velocity newVelocity = new Velocity(currentVelocity);
//        Integer region = this.shape.getTopLine().findRegion(collisionPoint);
//        //if hit is not on top - treat it line a normal block
//        if (region == null) {
//            //hit on sides
//            if (shape.getLeftLine().isOnLine(collisionPoint.getX(), collisionPoint.getY())
//                    || shape.getRightLine().isOnLine(collisionPoint.getX(), collisionPoint.getY())) {
//                newVelocity.setDx(-currentVelocity.getDx());
//                return newVelocity;
//            }
//            //hit on top/bottom
//            if (shape.getTopLine().isOnLine(collisionPoint.getX(), collisionPoint.getY())
//                    || shape.getBottomLine().isOnLine(collisionPoint.getX(), collisionPoint.getY())) {
//                newVelocity.setDy(-currentVelocity.getDy());
//
//            }
//            return newVelocity;
//        }
//        //if hit on top
//        double velocitySize = currentVelocity.getVelocitySize();
//        //change velocity according to region
//        if (region == 1) {
//            newVelocity = Velocity.fromAngleAndSpeed(300, velocitySize);
//        } else if (region == 2) {
//            newVelocity = Velocity.fromAngleAndSpeed(330, velocitySize);
//        } else if (region == 3) {
//            newVelocity.setDy(-newVelocity.getDy());
//        } else if (region == 4) {
//            newVelocity = Velocity.fromAngleAndSpeed(30, velocitySize);
//        } else if (region == 5) {
//            newVelocity = Velocity.fromAngleAndSpeed(60, velocitySize);
//        } else {
//            //if region is not between 1-5
//            throw new RuntimeException("region cant be diff from 1-5");
//        }
//        //return the new velocity after hit
//        return newVelocity;
        return null;
    }

    /**
     * Add this paddle to the game.
     *
     * @param g the game to add the paddle to
     */
    public void addToGame(GameLevel g) {
        g.addCollidable(this);
        g.addSprite(this);
    }


    /**
     * Remove from game.
     *
     * @param g the g
     */
//function not asked- i added!
    public void removeFromGame(GameLevel g) {
        g.removeCollidable(this);
        g.removeSprite(this);
    }


    /**
     * Add hl as a listener to hit events.
     *
     * @param hl listener to add
     */
    @Override
    public void addHitListener(HitListener hl) {
        this.listeners.add(hl);
    }

    /**
     * Remove hl from the list of listeners to hit events.
     *
     * @param hl listener to remove
     */
    @Override
    public void removeHitListener(HitListener hl) {
        this.listeners.remove(hl);
    }

    /**
     * notifies hit.
     * @param hitter hitter ball
     */
    private void notifyHit(Ball hitter) {
        List<HitListener> listeners1 = new ArrayList<HitListener>(this.listeners);
        for (HitListener hl : listeners1) {
            hl.hitEvent(this, hitter);
        }
    }
}

