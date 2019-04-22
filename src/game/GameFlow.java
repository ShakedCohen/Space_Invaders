package game;

import animation.AnimationRunner;
import animation.EndScreen;
import animation.HighScoresAnimation;
import animation.KeyPressStoppableAnimation;
import biuoop.DialogManager;
import biuoop.KeyboardSensor;
import gameobjects.Counter;
import score.HighScoresTable;
import score.ScoreInfo;

import java.io.File;
import java.io.IOException;

/**
 * The type Game flow.
 */
public class GameFlow {

    private AnimationRunner animationRunner;
    private KeyboardSensor keyboardSensor;
    private Counter numberOfLives = new Counter(Constants.LIVES);
    private Counter score = new Counter(0);
    private HighScoresTable highScores = new HighScoresTable(Constants.HIGHSCORE_TABLE_SIZE);
    private DialogManager dialogManager;

    private int levelNumber;
    private Double alienSpeed;


    /**
     * Instantiates a new Game flow.
     *
     * @param animationRunner1 the animation runner 1
     * @param keyboardSensor1  the keyboard sensor 1
     * @param dialogManager1   the dialog manager 1
     */
    public GameFlow(AnimationRunner animationRunner1, KeyboardSensor keyboardSensor1, DialogManager dialogManager1) {
        this.animationRunner = animationRunner1;
        this.keyboardSensor = keyboardSensor1;
        this.dialogManager = dialogManager1;
        try {
            this.highScores.load(new File(Constants.HIGHSCORES_FILE_NAME));
        } catch (IOException e) {
            throw new RuntimeException("you know");
        }

        //init vals
        this.levelNumber = 0;

        this.alienSpeed = 100.0;


    }

    /**
     * Run levels.
     */
    public void runLevels() {


        while (this.numberOfLives.getValue() != 0) {
            this.levelNumber++;
            GameLevel level = new GameLevel(this.keyboardSensor, this.animationRunner, numberOfLives,
                    score, alienSpeed, levelNumber);

            level.initialize();

            level.playOneTurn();

            //this.alienSpeed += 40;

        }

        if (this.highScores.isScoreShouldBeAdded(this.score.getValue())) {

            String name = this.dialogManager.showQuestionDialog("Name", "What is your name?", "");
            this.highScores.add(new ScoreInfo(name, this.score.getValue()));
            try {
                this.highScores.save(Constants.HIGHSCORES_FILE_NAME);
            } catch (IOException e) {
                throw new RuntimeException("you know");
            }
        }

        animationRunner.run(new KeyPressStoppableAnimation(
                this.keyboardSensor, KeyboardSensor.SPACE_KEY, new EndScreen(score, numberOfLives)));

        animationRunner.run(new KeyPressStoppableAnimation(
                this.keyboardSensor, KeyboardSensor.SPACE_KEY, new HighScoresAnimation(this.highScores)));
    }
}