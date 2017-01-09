package tom.archillespaint;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.gpu.KuwaharaFilterTransformation;

/**
 * Created by sasinda on 1/9/17.
 */

public class ArtsyService {


    public String getImgURL(){
        return null;
    }

    public ImageView loadImage(ImageView imgView, Context context){

        Picasso.with(context).load("http://previews.123rf.com/images/kabby/kabby1006/kabby100600002/7090722-Abstract-fun-cheerful-artsy-rainbow-backgrounds-design-Stock-Photo.jpg")
//                .transform(new SketchFilterTransformation(context))
                .transform(new KuwaharaFilterTransformation(context, 10))
                .error(R.drawable.aw_snap)
                .into(imgView);

        return imgView;
    }
}
