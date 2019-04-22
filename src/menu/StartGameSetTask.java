package menu;

import animation.AnimationRunner;
import biuoop.DialogManager;
import biuoop.KeyboardSensor;
import game.GameFlow;

/**
 * The type Start game set task.
 */
public class StartGameSetTask implements Task<Void> {


    private AnimationRunner runner;
    private KeyboardSensor sensor;
    private DialogManager dManager;


    /**
     * Instantiates a new Start game set task.
     *
     * @param runner   the runner
     * @param sensor   the sensor
     * @param dManager the d manager
     */
    public StartGameSetTask(AnimationRunner runner, KeyboardSensor sensor, DialogManager dManager) {
        this.runner = runner;
        this.sensor = sensor;
        this.dManager = dManager;

    }

    @Override
    public Void run() {
        GameFlow game = new GameFlow(this.runner, this.sensor, this.dManager);
        game.runLevels();
        return null;
    }


}


