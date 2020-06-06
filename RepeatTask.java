//John Koomson, Abel Legesse, and Naila Thevenot

import java.util.Timer;
import java.util.TimerTask;

public class RepeatTask {
    private Timer timer;

    //Constructor
    //Takes in the number of seconds at which to call and repeat action.
    public RepeatTask(int seconds) {
        //Create and instance of Timer class to get access to the methods (such as schedule) in the Timer class.
        timer = new Timer();

        //Schedule a task to be performed (TaskToPerform).
        //Since the period is in milliseconds, multiply by 1000 so it is actually in milliseconds.
        timer.schedule(new TaskToPerform(), 0, seconds * 1000);
    }

    //Task to perform in RepeatTask
    class TaskToPerform extends TimerTask {
        public void run() {
            //Only perform task when there are grenades in the plane and when game isn't paused.
            if (Main.plane.grenades.index < 20 && Main.tocontinue) {
                Main.plane.grenades.index += 1;
            }
        }
    }
}


