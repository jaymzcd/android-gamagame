package devtest.jaymz.eu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Color;
import android.graphics.Matrix;
import android.content.Context;

import java.util.Random;
import android.util.Log;

import devtest.jaymz.eu.Sprite;

class Enemy extends Sprite {
    public Enemy (Bitmap bitmap) {
        super(bitmap);
    }

    public void setDynamics() {
        direction = getRandomDirection();
        angularVelocity = _r.nextFloat()+1;
        angularDirection = getRandomDirection();
        if(angularDirection==0) {
            angularDirection = 1;
        }
        speed += _r.nextInt(5);
    }

    public void setPaint() {
        /* Create a randomish green hue to paint over the base white
           of the sprite. Apply via the colorfilter and store for later */

        float[] hsv = new float[3];
        hsv[0] = 93 - _r.nextInt(20);
        hsv[1] = 0xff;
        hsv[2] = 0xff;

        ColorFilter f = new LightingColorFilter(Color.HSVToColor(hsv), 1);
        paint.setColorFilter(f);
    }

    public void Update() {
        super();
        if (ticker % 50 == 0) {
            direction = getRandomDirection(); // switch randomly
        }
    }

    public int getRandomDirection() {
        return (_r.nextInt(3) - 1);
    }

}