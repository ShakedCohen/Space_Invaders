package aliens;

import biuoop.DrawSurface;
import game.GameLevel;
import sprite.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The type Alien bundle.
 */
public class AlienBundle implements Sprite {

    private List<AlienCol> alienColumns = new ArrayList<AlienCol>();
    private Double aliensSpeed;
    private double timeSinceLastShot = 0;
    private int leftOrRight = 1;
    private ShotFactory factory;


    /**
     * Instantiates a new Alien bundle.
     *
     * @param speed    the speed
     * @param factory1 the factory 1
     */
    public AlienBundle(Double speed, ShotFactory factory1) {
        this.aliensSpeed = speed;
        this.factory = factory1;
    }


    /**
     * notify the sprite that time has passed.
     *
     * @param dt the dt
     */
    @Override
    public void timePassed(double dt) {
        this.timeSinceLastShot += dt;
        //removes empty cols

        //create copy
        List<AlienCol> copyCols = this.alienColumns;
        for (AlienCol col : copyCols) {
            if (col.getColAliens().size() == 0) {
                this.alienColumns.remove(col);
                continue;
            }
            //check if reached wall limits
            if (col.reachedWall()) {
                this.leftOrRight *= -1;
                this.downRow();
                this.aliensSpeed = this.aliensSpeed * 1.1;
            }

        }

        for (AlienCol col : this.alienColumns) {
            col.move(this.leftOrRight * dt * this.aliensSpeed);

            if (this.timeSinceLastShot >= 0.5) {
                this.timeSinceLastShot = 0;
                //shoot

                Random rand = new Random();
                int shooterCol = rand.nextInt(this.alienColumns.size());
                this.alienColumns.get(shooterCol).shootFromCol();
            }
        }
        //generate shot if needed
    }

    /**
     * Remove from game.
     *
     * @param game the game
     */
    public void removeFromGame(GameLevel game) {
        game.removeSprite(this);
        for (AlienCol col : this.alienColumns) {
            for (Alien a : col.getColAliens()) {
                a.removeFromGame(game);
            }
        }
    }

    /**
     * Remove from swarm.
     *
     * @param alien the alien
     */
    public void removeFromSwarm(Alien alien) {

        for (AlienCol col : this.alienColumns) {
            List<Alien> newList = new ArrayList<Alien>();
            for (Alien a : col.getColAliens()) {
                if (a != alien) {
                    newList.add(a);
                }
            }
            col.setColAliens(newList);
            if (newList.size() == 0) {
                this.removeEmptyColsFromSwarm();
            }

        }
    }

    /**
     * removes empty cols.
     */
    private void removeEmptyColsFromSwarm() {
        List<AlienCol> cols = new ArrayList<AlienCol>();
        for (AlienCol column : this.alienColumns) {
            if (column.getColAliens().size() > 0) {
                cols.add(column);
            }
        }
        this.alienColumns = cols;
    }

    /**
     * moves row down.
     */
    private void downRow() {
        for (AlienCol col : this.alienColumns) {
            col.downRow();
        }
    }


    /**
     * Sets aliens speed.
     *
     * @param aliensSpeed1 the aliens speed
     */
    public void setAliensSpeed(Double aliensSpeed1) {
        this.aliensSpeed = aliensSpeed1;
    }


    /**
     * Gets alien columns.
     *
     * @return the alien columns
     */
    public List<AlienCol> getAlienColumns() {
        return alienColumns;
    }

    /**
     * Gets all aliens.
     *
     * @return the all aliens
     */
    public List<Alien> getAllAliens() {
        List<Alien> all = new ArrayList<Alien>();
        for (AlienCol col : this.alienColumns) {
            for (Alien a : col.getColAliens()) {
                all.add(a);
            }
        }
        return all;
    }

    /**
     * Reached sheilds boolean.
     *
     * @return the boolean
     */
    public boolean reachedSheilds() {
        for (AlienCol col : this.alienColumns) {
            if (col.reachedSheilds()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Add column to bundle.
     *
     * @param col the col
     */
    public void addColumnToBundle(AlienCol col) {
        col.setFactory(this.factory);
        this.alienColumns.add(col);
        for (Alien alien : col.getColAliens()) {
            alien.setSwarm(this);
        }
    }


    /**
     * Add to game.
     *
     * @param game the game
     */
    public void addToGame(GameLevel game) {
        game.addSprite(this);
    }

    /**
     * To init positions.
     */
    public void toInitPositions() {
        for (AlienCol c : this.alienColumns) {
            c.setColToInit();
        }
    }


    /**
     * draw the sprite to the screen.
     *
     * @param d the drawsurface
     */
    @Override
    public void drawOn(DrawSurface d) {
        for (AlienCol a : this.alienColumns) {
            if (a.getColAliens().size() > 0) {
                a.drawOn(d);
            }
        }
    }
}
