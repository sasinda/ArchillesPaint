package tom.archillespaint;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by sasinda on 1/10/17.
 */

public class ExerciseTracker {

    public static final int COUNT_DOWN_STRETCH = 30000;
    volatile int rep = 0;
    volatile int angle = 0;
    volatile boolean inRep = false;
    private Activity myActivity;
    private TextView statusTxtv;



    private static ExerciseTracker instance;


    private ExerciseTracker() {
    }

    public ExerciseTracker initialize(Activity myActivity, TextView statusTxtv) {
        this.myActivity = myActivity;
        this.statusTxtv = statusTxtv;
        return this;
    }

    public static ExerciseTracker getInstance() {
        if (instance == null) {
            instance = new ExerciseTracker();
        }
        return instance;
    }

    public void updateAngle(final int angle) {
        this.angle = angle;
        if (!inRep && rep < 6) {
            inRep = true;
            myActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusTxtv.setText("Start Stretching!!!");
                    new CountDownTimer(COUNT_DOWN_STRETCH, 1000) {
                        public void onTick(long millisUntilFinished) {
                            int secs=COUNT_DOWN_STRETCH - (int)(millisUntilFinished / 1000);
                            if (angle > 50) {
                                statusTxtv.setText("nice work keep stretching! time: " +secs);
                            } else {
                                statusTxtv.setText("stretch more! time remaining: " + millisUntilFinished / 1000);
                            }

                            if(secs%10==0){
                                ArtsyService.applyKuwaharaFilter(10*(COUNT_DOWN_STRETCH-secs));
                            }

                        }

                        public void onFinish() {
                            statusTxtv.setText("rep " + rep + " done!");
                            ArtsyService.applyKuwaharaFilter(1);
                            inRep = false;
                            if (rep > 5) {
                                rep = 0;
                                statusTxtv.setText("Well done! you did 5 reps!");
                            }
                        }
                    }.start();
                }
            });

            rep += 1;
        }


    }


}
