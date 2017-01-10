package tom.archillespaint;

import android.os.AsyncTask;

/**
 * Created by sasinda on 1/10/17.
 */

public class ImageBlurTask extends AsyncTask<Object, Object, Void> {


    @Override
    protected Void doInBackground(Object... params) {
        ArtsyService.applyBlur(40);
        return null;
    }

    @Override
    protected void onPostExecute(Void result){

    }

}
