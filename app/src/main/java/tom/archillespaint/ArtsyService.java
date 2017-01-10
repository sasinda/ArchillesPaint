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

import jp.wasabeef.picasso.transformations.gpu.KuwaharaFilterTransformation;

/**
 * Created by sasinda on 1/9/17.
 */




public class ArtsyService extends AsyncTask<Void, Void, Void>{
    URL artsyUrl = null;
    HttpURLConnection urlConnection = null;

    public String getImgURL(){
        return null;
    }

    public ImageView loadImage(ImageView imgView, Context context){

        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png")
//                .transform(new SketchFilterTransformation(context))
                .transform(new KuwaharaFilterTransformation(context, 1))
                .error(R.drawable.aw_snap)
                .into(imgView);

        return imgView;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        StringBuilder result = new StringBuilder();
        try {
            artsyUrl = new URL("https://api.artsy.net/api/artworks?sample");
            urlConnection =  (HttpURLConnection) artsyUrl.openConnection();
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
            Map<String,Object> json = new Gson().fromJson(result.toString(), Map.class);
            String imgUrl=((Map<String,String>)((Map<String,Object>)json.get("_links")).get("image")).get("href");


        }catch(Exception e){
            System.out.println("errorrrrr");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
