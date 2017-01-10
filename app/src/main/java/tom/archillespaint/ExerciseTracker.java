package tom.archillespaint;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by sasinda on 1/10/17.
 */

public class ExerciseTracker {

    volatile int rep=0;
    volatile int angle=0;
    volatile boolean inRep =false;
    TextView statusTxtv;

    private static ExerciseTracker instance;


    private ExerciseTracker() {
    }

    public void initialize(TextView status_txtv){
         this.statusTxtv =status_txtv;
    }

    public static ExerciseTracker getInstance(){
        if (instance==null){
            instance=new ExerciseTracker();
        }
        return instance;
    }

    public void setAngle(final int angle){
        this.angle=angle;
        if (!inRep && rep<6){
            inRep =true;
            statusTxtv.setText("Get ready for the next stretch!");
            new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                    if(angle>50){
                        statusTxtv.setText("nice work keep stretching! time remaining: " + millisUntilFinished / 1000);
                    }else {
                        statusTxtv.setText("stretch more! time remaining: " + millisUntilFinished / 1000);
                    }

                }

                public void onFinish() {
                    statusTxtv.setText("rep "+rep+" done!");
                    inRep =false;
                    if (rep>5){
                        rep = 0;
                        statusTxtv.setText("Well done! you did 5 reps!");
                    }
                }
            }.start();
            rep+=1;
        }


    }




}
