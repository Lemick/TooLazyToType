package com.toolazytotype.archive;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class ImageToText {

    //private TextRecognizer textRecognizer;
    private Context context;

    public ImageToText(Context context) {
        this.context = context;
        //textRecognizer = new TextRecognizer.Builder(context).build();

    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    public void takeSnap() {
        Screenshoter screenshoter = new Screenshoter(context);
        screenshoter.startCapture();
    }
}
