package com.cs571.nanjiang.ebaysearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by slyar on 4/30/15.
 */
public class getIMG extends AsyncTask<String, String, Bitmap> {

    public ImageView imageView = null;

    public getIMG(ImageView result_img) {
        this.imageView = result_img;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(urls[0]).getContent());
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            this.imageView.setImageBitmap(bitmap);
        }
    }
}
