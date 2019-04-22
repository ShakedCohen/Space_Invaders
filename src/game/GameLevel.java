package game;

import aliens.Alien;
import aliens.AlienBundle;
import aliens.AlienCol;
import aliens.ShotFactory;
import animation.Animation;
import animation.AnimationRunner;
import animation.KeyPressStoppableAnimation;
import animation.PauseScreen;
import animation.CountdownAnimation;


import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import collision.Collidable;
import collision.SheildsRemover;
import collision.HitListener;
import collision.BallRemover;
import collision.PaddleHitListener;
import collision.ScoreTrackingListener;
import collision.BlockRemover;
import gameobjects.Block;
import gameobjects.Counter;
import gameobjects.Ball;
import gameobjects.Paddle;
import gameobjects.DeathBlock;
import geometry.Point;
import geometry.Rectangle;
import indicators.LevelIndicator;
import indicators.LivesIndicator;
import indicators.ScoreIndicator;
import sprite.RectBackground;
import sprite.Sprite;
import sprite.RectColorBackground;
import sprite.SpriteCollection;
import sprite.SIBackground;
import sprite.RectImageBackground;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * game.GameLevel class. defines the game course
 */
public class GameLevel implements Animation {
    private SpriteCollection sprites = new SpriteCollection();
    private GameEnvironment environment = new GameEnvironment();
    private KeyboardSensor keyboard;
    //NOT INCLUDING DEATH BLOCKS
    private Counter remainingBalls = new Counter();
    private Paddle gamePaddle = null;
    private java.util.List<Ball> gameBallList = new ArrayList<Ball>();
    private java.util.List<HitListener> blockHitListeners = new ArrayList<HitListener>();
    private Counter score;
    private Counter numberOfLives;
    private AnimationRunner runner;
    private boolean running;
    private boolean gameStart = true;

    private BallRemover ballRemover;


    private Double alienSpeed;
    private Counter remainingAliens;
    private boolean playerBeenHit = false;
    private AlienBundle swarm;
    private ShotFactory factory;
    private Counter levelNumber;
    private Double startSpeedThisRound;


    /**
     * constructor for the game- creates game environment and sprite.Sprite collection.
     *
     * @param keyboardSensor1  the keyboard sensor 1
     * @param animationRunner1 the animation runner 1
     * @param curLives         the cur lives
     * @param curScore         the cur score
     * @param alienSpeed       the alien speed
     * @param levelNumber      the level number
     */
    public GameLevel(KeyboardSensor keyboardSensor1, AnimationRunner animationRunner1,
                     Counter curLives, Counter curScore, Double alienSpeed, int levelNumber) {
        this.keyboard = keyboardSensor1;
        this.runner = animationRunner1;
        this.numberOfLives = curLives;
        this.score = curScore;


        this.alienSpeed = alienSpeed;
        this.levelNumber = new Counter(levelNumber - 1);


        this.remainingAliens = new Counter();


        this.remainingAliens = new Counter();

        this.factory = new ShotFactory(this, this.environment);
        this.swarm = new AlienBundle(this.alienSpeed, this.factory);

    }


    /**
     * Initialize.
     */
    public void initialize() {

        BlockRemover blockRemover = new BlockRemover(this, remainingAliens);
        this.blockHitListeners.add(blockRemover);
        ScoreTrackingListener scoreTracker = new ScoreTrackingListener(score);
        this.blockHitListeners.add(scoreTracker);

        this.ballRemover = new BallRemover(this, remainingBalls);

        this.sprites.addSprite(new SIBackground());

        createScoreIndicator();
        createLivesIndicator();
        createLevelIndicator();

        //aliens need score tracker and block remover
        //sheilds need block remover


//        frameBlockCreator(0, Constants.SCORE_PANEL_HEIGHT, 800,
//                Constants.FRAME_BLOCK_WIDTH, 1000);
//
//        //left
//        frameBlockCreator(0, Constants.SCORE_PANEL_HEIGHT, Constants.FRAME_BLOCK_WIDTH,
//                600, 1000);
//
//        //right
//        frameBlockCreator(Constants.GUI_WIDTH - Constants.FRAME_BLOCK_WIDTH,
//                Constants.SCORE_PANEL_HEIGHT, Constants.FRAME_BLOCK_WIDTH, 600, 1000);

        //bottom DEATH BLOCK
        deathBlockCreator(0, 606, 800, 5, Color.magenta,
                100);
        //top DEATH BLOCK
        deathBlockCreator(0, 30, 800, 5, Color.magenta,
                100);

        generateSwarm();
        this.swarm.addToGame(this);
        generateSheilds();
    }

        /**
         * generates sheilds.
         */
    private void generateSheilds() {
        SheildsRemover sheildsRemover = new SheildsRemover(this);

        for (int m = 0; m < 3; m++) {
            for (int j = 0; j < 3; j++) {
                for (int i = 0; i < 25; i++) {
                    Map<Integer, RectBackground> backgroundMap = new HashMap<Integer, RectBackground>();
                    backgroundMap.put(1, new RectColorBackground(Color.cyan));

                    Block sheildBlock = new Block(new Rectangle(new Point(50 + (m * 275) + (i * 6),
                            500 + j * 6), 6, 6), null, 1, backgroundMap);


                    sheildBlock.addToGame(this);
                    sheildBlock.addHitListener(sheildsRemover);
                    sheildBlock.addHitListener(this.ballRemover);
                }
            }
        }


    }

    /**
     * generates warm.
     */
    private void generateSwarm() {

        Map<Integer, RectBackground> map = new HashMap<Integer, RectBackground>();
        BufferedImage image;
        InputStream is = null;
        try {
            is = ClassLoader.getSystemClassLoader().getResourceAsStream("enemy.png");
            image = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        RectBackground rectBackground = new RectImageBackground(image);
        map.put(1, rectBackground);

        for (int i = 0; i < 10; i++) {
            //one column
            AlienCol col = new AlienCol();

            for (int j = 0; j < 5; j++) {
                Alien curAlien = new Alien(new Rectangle(
                        new Point(50 + i * 50, 50 + j * 40), 40, 30), null,
                        1, map, this, this.score);

                curAlien.addHitListener(this.blockHitListeners.get(0));
                curAlien.addHitListener(this.ballRemover);
                curAlien.addHitListener(this.blockHitListeners.get(1));
                curAlien.addHitListener(col);
                col.addToColumn(curAlien);
                curAlien.addToGame(this);
                this.remainingAliens.increase(1);
            }
            swarm.addColumnToBundle(col);
        }
    }


    /**
     * Create blocks.
     *
     * @param listeners     the listeners
     * @param blockToCreate the block to create
     */
    public void createBlocks(List<HitListener> listeners, List<Block> blockToCreate) {
        for (Block block : blockToCreate) {
            for (HitListener hLis : listeners) {
                block.addHitListener(hLis);
            }
            block.addToGame(this);
        }
    }

    /**
     * Run the game create paddle and start the animation loop.
     */
    public void playOneTurn() {
        //if there is a paddle, remove him.
        if (this.gamePaddle != null) {
            this.gamePaddle.removeFromGame(this);
        }
        //create paddle
        paddleCreator(Constants.GUI_WIDTH / 2 - Constants.PADDLE_WIDTH / 2, 585,
                Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, Color.yellow, Constants.PADDLE_SPEED);
        this.levelNumber.increase(1);

        this.alienSpeed += 80;
        this.startSpeedThisRound = this.alienSpeed;
        this.swarm.setAliensSpeed(this.alienSpeed);


        this.running = true;
        // use our runner to run the current animation -- which is one turn of
        // the game.
        this.runner.run(this);
    }


    /**
     * Gets remaining blocks.
     *
     * @return the remaining blocks
     */
    public Counter getRemainingAliens() {
        return remainingAliens;
    }

    /**
     * draw one frame of he animation.
     *
     * @param d  the drawsurface
     * @param dt dt val
     */
    @Override
    public void doOneFrame(DrawSurface d, double dt) {
        // the logic from the previous playOneTurn method goes here.
        // the `return` or `break` statements should be replaced with
        // this.running = false;


        if (this.gameStart) {
            this.runner.run(new CountdownAnimation(2, 3, this.sprites));
            this.gameStart = false;
        }

        //draw the sprites
        this.sprites.drawAllOn(d);
        this.sprites.notifyAllTimePassed();

        //pause
        if (this.keyboard.isPressed("p")) {
            this.runner.run(new KeyPressStoppableAnimation(this.keyboard, KeyboardSensor.SPACE_KEY, new PauseScreen()));
        }
        //no blocks remaining
        if (this.swarm.getAllAliens().size() <= 0) {
            this.running = false;
        }
        this.playerBeenHit = this.gamePaddle.isBeenHit();
        //end of round
        if (this.playerBeenHit || this.swarm.reachedSheilds()) {
            this.numberOfLives.decrease(1);

            if (this.numberOfLives.getValue() == 0) {
                this.running = false;
                return;
            } else {
                //init for next run
                this.swarm.toInitPositions();
                this.gamePaddle.setUpperLeft(new Point(
                        Constants.GUI_WIDTH / 2 - Constants.PADDLE_WIDTH / 2, 585));

                factory.removeAlienShotsFromGame();
                factory.removePaddleShotsFromGame();
                this.alienSpeed = this.startSpeedThisRound;
                this.swarm.setAliensSpeed(this.alienSpeed);
                this.playerBeenHit = false;

                gameStart = true;

            }


        }

    }


    /**
     * checks the stop condition for the animation.
     *
     * @return true if the animation should run, else false
     */
    @Override
    public boolean shouldStop() {
        return !this.running;
    }


    /**
     * Death block creator.
     *
     * @param xUpperLeft  the x upper left
     * @param yUpperLeft  the y upper left
     * @param width       the width
     * @param height      the height
     * @param color       the color
     * @param hitPoints   the hit points
     */
    public void deathBlockCreator(int xUpperLeft, int yUpperLeft, int width, int height,
                                  Color color, int hitPoints) {
        Block b = new DeathBlock(new Rectangle(new Point(xUpperLeft, yUpperLeft),
                width, height), color, hitPoints, ballRemover);
        b.addToGame(this);
    }

//    /**
//     * Ball creator.
//     *
//     * @param xCenter  the x center
//     * @param yCenter  the y center
//     * @param radius   the radius
//     * @param color    the color
//     * @param velocity the velocity
//     */
//    public void ballCreator(int xCenter, int yCenter, int radius, Color color, Velocity velocity) {
//        Ball ball = new Ball(new Point(xCenter, yCenter), radius, color, this.environment);
//        ball.setVelocity(velocity);
//        this.gameBallList.add(ball);
//        this.remainingBalls.increase(1);
//        ball.addToGame(this);
//    }

    /**
     * Paddle creator.
     *
     * @param xUpperLeft  the x upper left
     * @param yUpperLeft  the y upper left
     * @param width       the width
     * @param height      the height
     * @param color       the color
     * @param paddleSpeed the paddle speed
     */
    public void paddleCreator(int xUpperLeft, int yUpperLeft, int width, int height, Color color, int paddleSpeed) {

        PaddleHitListener paddleHitListener = new PaddleHitListener(this.playerBeenHit);

        Paddle paddle = new Paddle(new Rectangle(new Point(xUpperLeft, yUpperLeft), width, height),
                color, paddleSpeed, keyboard, this, this.factory);

        this.gamePaddle = paddle;
        paddle.addHitListener(paddleHitListener);
        paddle.addHitListener(this.ballRemover);
        paddle.addToGame(this);
    }

    /**
     * Create score indicator.
     */
    public void createScoreIndicator() {
        ScoreIndicator scoreIndicator = new ScoreIndicator(this.score);
        this.addSprite(scoreIndicator);
    }

    /**
     * Create lives indicator.
     */
    public void createLivesIndicator() {
        LivesIndicator livesIndicator = new LivesIndicator(this.numberOfLives);
        this.addSprite(livesIndicator);
    }

    /**
     * Create level indicator.
     */
    public void createLevelIndicator() {
        LevelIndicator levelIndicator = new LevelIndicator(this.levelNumber);
        this.addSprite(levelIndicator);
    }

    /**
     * Create balls on top of paddle.
     */
//    public void createBallsOnTopOfPaddle() {
//        if (this.gamePaddle == null) {
//            throw new RuntimeException("cant create balls on top of a paddle if there is no paddle,"
//                    + " what the hell dude?");
//        }
//
//        ballCreator((int) gamePaddle.getCollisionRectangle().getTopLine().middle().getX(),
//                (int) gamePaddle.getCollisionRectangle().getTopLine().middle().getY()
//                        - Constants.PADDLE_HEIGHT / 2 - 1,
//                Constants.BALL_SIZE, Color.darkGray,
//                new Velocity(Velocity.fromAngleAndSpeed(0,Constants.SHOT_SPEED)));
//
//
//
//    }


    /**
     * adds a collidable to the list.
     *
     * @param collidableObj the obj
     */
    public void addCollidable(Collidable collidableObj) {
        this.environment.addCollidable(collidableObj);
    }

    /**
     * adds a sprite to the list.
     *
     * @param spriteObj obj
     */
    public void addSprite(Sprite spriteObj) {
        this.sprites.addSprite(spriteObj);
    }

    /**
     * Remove collidable.
     *
     * @param c the c
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    /**
     * Remove sprite.
     *
     * @param s the s
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }


}












