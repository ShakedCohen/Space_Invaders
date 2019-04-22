package main;

import animation.AnimationRunner;
import animation.HighScoresAnimation;
import animation.KeyPressStoppableAnimation;
import biuoop.DialogManager;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import game.Constants;
import menu.Menu;
import menu.ShowHighScoresTask;
import menu.QuitGameTask;
import menu.MenuAnimation;
import menu.StartGameSetTask;
import menu.Task;
import score.HighScoresTable;

import java.io.File;
import java.io.IOException;


/**
 * the ASS6Game class.
 * runs the game.
 */
public class Ass7Game {

    /**
     * main that runs the program.
     * 0 is not a valid level
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {


        Menu<Task<Void>> mainMenu;

        GUI gui = new GUI("Space Invaders", Constants.GUI_WIDTH, Constants.GUI_HEIGHT);

        AnimationRunner runner = new AnimationRunner(gui, Constants.FRAMES_PER_SECOND);
        KeyboardSensor sensor = gui.getKeyboardSensor();
        DialogManager dManager = gui.getDialogManager();


        while (true) {
            mainMenu = initMenu(runner, sensor, dManager);
            runner.run(mainMenu);

            // wait for user selection
            Task<Void> task = mainMenu.getStatus();

            task.run();
        }

    }

    /**
     * initiates the menu.
     *
     * @param runner   animation runner
     * @param sensor   the keyboard sensor
     * @param dManager the dialog manager
     * @return returns a menu
     */
    private static Menu<Task<Void>> initMenu(AnimationRunner runner, KeyboardSensor sensor,
                                             DialogManager dManager) {


        Menu<Task<Void>> mainMenu = new MenuAnimation<Task<Void>>("Arkanoid", runner, sensor);

        mainMenu.addSelection("s", "Start Game", new StartGameSetTask(runner, sensor, dManager));

        mainMenu.addSelection("h", "High Scores",
                new ShowHighScoresTask(runner, new KeyPressStoppableAnimation(
                        sensor, KeyboardSensor.SPACE_KEY, new HighScoresAnimation(loadHighScores()))));

        mainMenu.addSelection("q", "quit", new QuitGameTask());
        return mainMenu;
    }


    /**
     * try to load highscore table.
     *
     * @return highscore table
     */
    private static HighScoresTable loadHighScores() {

        HighScoresTable highScores = new HighScoresTable(Constants.HIGHSCORE_TABLE_SIZE);

        try {
            highScores.load(new File(Constants.HIGHSCORES_FILE_NAME));
        } catch (IOException e) {
            throw new RuntimeException("you know");
        }
        return highScores;
    }

}
