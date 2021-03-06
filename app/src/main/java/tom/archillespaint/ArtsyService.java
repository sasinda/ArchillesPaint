package tom.archillespaint;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.gpu.KuwaharaFilterTransformation;

/**
 * Created by sasinda on 1/9/17.
 */


public class ArtsyService extends AsyncTask<Object, Object, Void> {
    URL artsyUrl = null;
    HttpURLConnection urlConnection = null;
    String imgUrlString = null;
    static ImageView imageview;
    static Context context;
    private static RequestCreator picassoReq;
    private static RequestCreator imgReq1;
    private static RequestCreator imgReq2;
    private static RequestCreator imgReq3;
    private static RequestCreator original;

    public ArtsyService(ImageView imv, Context c) {
        imageview = imv;
        context = c;
    }

    public String getImgURL() {
        return null;
    }

    public RequestCreator loadImage(ImageView imgView, Context context) {

        System.out.println("imgUrlString" + imgUrlString);

        RequestCreator imgReq = Picasso.with(context).load(imgUrlString);
//                .transform(new SketchFilterTransformation(context))
//                .transform(new KuwaharaFilterTransformation(context, 120))
//                .transform(new KuwaharaFilterTransformation(context, 1))
        imgReq.error(R.drawable.aw_snap).into(imgView);
        return null;
    }

    @Override
    protected Void doInBackground(Object... objects) {

        StringBuilder result = new StringBuilder();
        try {
            artsyUrl = new URL("https://api.artsy.net/api/artworks?sample");
            urlConnection = (HttpURLConnection) artsyUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("X-Xapp-Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE0ODQ1NjY0MjksImlhdCI6MTQ4Mzk2MTYyOSwiYXVkIjoiNTg3Mzc1MWRjOWRjMjQ1MWQ0MDA0M2ZhIiwiaXNzIjoiR3Jhdml0eSIsImp0aSI6IjU4NzM3NTFkOWMxOGRiNTkyMzAwNDk4NSJ9.9moCAHjQw1N6V8MVLQv7rVxY-_7vXupWWpmJEnpUcdM");
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //this is where the error happens
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            System.out.println("printing result: " + result);
            System.out.println(result.getClass());
            Gson gson = new Gson();
            Map<String, Object> json = new Gson().fromJson(result.toString(), Map.class);
            imgUrlString = ((Map<String, String>) ((Map<String, Object>) json.get("_links")).get("image")).get("href");
            imgUrlString = imgUrlString.replace("{image_version}", "normalized");
            System.out.println("imgUrl over here" + imgUrlString);

        } catch (Exception e) {
            System.out.println("Error message is: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void result) {
        System.out.println("on post execute!" + imgUrlString);
        original =Picasso.with(context).load(imgUrlString);
        imgReq1 = Picasso.with(context).load(imgUrlString).transform(new BlurTransformation(context, 50));
        imgReq2 = Picasso.with(context).load(imgUrlString).transform(new BlurTransformation(context, 20));
        imgReq3 = Picasso.with(context).load(imgUrlString);



//                .transform(new SketchFilterTransformation(context))
//                .transform(new KuwaharaFilterTransformation(context, 1))

        original.fetch();
        original.transform(new BlurTransformation(context, 50)).error(R.drawable.aw_snap).into(imageview);
//        imgReq1.error(R.drawable.aw_snap).into(imageview);
//        imgReq2.fetch();
//        imgReq3.fetch();

    }

    public static void applyKuwaharaFilter(int radius) {
        System.out.println("radius: " + radius);
        picassoReq.transform(new KuwaharaFilterTransformation(context, radius)).into(imageview);
    }

    public static void applyBlur(int id) {
        switch (id) {
            case 1:
//                imgReq1.into(imageview);
                original.transform(new BlurTransformation(context, 50)).into(imageview);
                break;
            case 2:
//                imgReq2.into(imageview);
                original.transform(new BlurTransformation(context, 20)).into(imageview);
                break;
            case 3:
//                imgReq3.into(imageview);
                original.into(imageview);
                break;
        }
    }

}
