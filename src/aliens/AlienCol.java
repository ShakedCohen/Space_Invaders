package aliens;

import biuoop.DrawSurface;
import collision.Collidable;
import collision.HitListener;
import gameobjects.Ball;
import gameobjects.Velocity;
import geometry.Point;
import geometry.Rectangle;
import sprite.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Alien col.
 */
public class AlienCol implements Sprite, Collidable, HitListener {


    private List<Alien> colAliens = new ArrayList<Alien>();
    private ShotFactory factory;


    /**
     * Move.
     *
     * @param dist the dist
     */
    public void move(double dist) {
        for (Alien a : this.colAliens) {
            a.move(dist);
        }
    }

    /**
     * Gets num of aliens.
     *
     * @return the num of aliens
     */
    public int getNumOfAliens() {
        return colAliens.size();
    }

    /**
     * Gets col aliens.
     *
     * @return the col aliens
     */
    public List<Alien> getColAliens() {
        return colAliens;
    }

    /**
     * Sets col aliens.
     *
     * @param colAliens1 the col aliens
     */
    public void setColAliens(List<Alien> colAliens1) {
        this.colAliens = colAliens1;
    }

    /**
     * Add to column.
     *
     * @param a the a
     */
    public void addToColumn(Alien a) {
        this.colAliens.add(a);
    }

    /**
     * Remove from column.
     *
     * @param a the a
     */
    public void removeFromColumn(Alien a) {
        this.colAliens.remove(a);
    }

    /**
     * Gets lowest alien.
     *
     * @return the lowest alien
     */
    public Alien getLowestAlien() {
        Alien curLowest = this.colAliens.get(0);
        for (Alien a : this.colAliens) {
            if (a.getShape().getUpperLeft().getY() > curLowest.getShape().getUpperLeft().getY()) {
                curLowest = a;
            }
        }
        return curLowest;
    }

    /**
     * Reached wall boolean.
     *
     * @return the boolean
     */
    public boolean reachedWall() {
        double x = this.colAliens.get(0).getShape().getUpperLeft().getX();
        return (x <= 20 || x >= 800 - 50);
    }

    /**
     * Sets col to init.
     */
    public void setColToInit() {
        for (Alien a : this.colAliens) {
            a.setToInitial();
        }
    }

    /**
     * Sets factory.
     *
     * @param factory1 the factory 1
     */
    public void setFactory(ShotFactory factory1) {
        this.factory = factory1;
    }

    /**
     * Shoot from col.
     */
    public void shootFromCol() {
        this.factory.createAlienShot(this.getLowestAlien().getShape().getBottomLine().middle());
    }

    /**
     * Reached sheilds boolean.
     *
     * @return the boolean
     */
    public boolean reachedSheilds() {
        return this.getLowestAlien().getShape().getUpperLeft().getY() >= 450;

    }

    /**
     * draw the sprite to the screen.
     *
     * @param d the drawsurface
     */
    @Override
    public void drawOn(DrawSurface d) {
        for (Alien a : this.colAliens) {
            a.drawOn(d);
        }
    }

    /**
     * notify the sprite that time has passed.
     *
     * @param dt the dt
     */
    @Override
    public void timePassed(double dt) {
        for (Alien a : this.colAliens) {
            a.timePassed(dt);
        }

    }

    /**
     * Down row.
     */
    public void downRow() {
        for (Alien a : this.colAliens) {
            a.setY((int) a.getShape().getUpperLeft().getY() + 15);
        }
    }


    /**
     * Gets collision rectangle.
     *
     * @return Return the "collision shape" of the object.
     */
    @Override
    public Rectangle getCollisionRectangle() {
        return null;
    }

    /**
     * Notify the object that we collided with it at collisionPoint with a given velocity.
     * The return is the new velocity expected after the hit
     * (based on the force the object inflicted on us).
     *
     * @param hitter          the hitter
     * @param collisionPoint  the col point
     * @param currentVelocity the cur velocity
     * @return new velocity
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        for (Alien a : this.colAliens) {
            if (a.getCollisionRectangle().insideRectangle(collisionPoint)) {
                a.hit(hitter, collisionPoint, currentVelocity);
            }
        }
        return null;
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
        this.colAliens.remove((Alien) beingHit);
    }


}
