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
    volatile int pedal_press=0;
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

    public void updateStretch(final int angle) {
        this.angle = angle;

        if (!inRep && rep < 6) {
            myActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusTxtv.setText("Start Stretching!!!");
                    new CountDownTimer(COUNT_DOWN_STRETCH, 1000) {
                        public void onTick(long millisUntilFinished) {
                            int secs=COUNT_DOWN_STRETCH/1000 - (int)(millisUntilFinished / 1000);
                            if (angle > 50) {
                                inRep = true;
                                statusTxtv.setText("nice work keep stretching! time: " +secs);
                            } else {
                                statusTxtv.setText("Try to stretch more! time remaining: " + millisUntilFinished / 1000);
                            }
                            if(secs%10==0){
                                System.out.println("Blur Radius: "+10*(COUNT_DOWN_STRETCH/1000-secs));
                                ArtsyService.applyBlur(2);
                            }

                        }

                        public void onFinish() {
                            statusTxtv.setText("rep " + rep + " done!");
                            inRep = false;
                            statusTxtv.setText("Well done! you did it!");
                            rep=0;
                            ArtsyService.applyBlur(3);
                        }
                    }.start();
                }
            });
            rep += 1;
        }
    }

    public void resetStretch(final int angle) {
        this.angle=angle;
        inRep=false;
        rep=0;
    }


    public void updatePedalPress() {
        pedal_press+=1;

        myActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusTxtv.setText("Active Stretch: Completed " + pedal_press + "/5");
            }
        });

    }

    public void resetPedalPress(){
        pedal_press=0;
    }
}
