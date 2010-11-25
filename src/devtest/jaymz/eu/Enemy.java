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

    Bitmap explodeGraphic;
    int explodeTicker;
    boolean exploding = false;
    
    public Enemy (Bitmap bitmap) {
        super(bitmap);
    }

    public Enemy(Bitmap bitmap, Bitmap exploder) {
        super(bitmap);
        explodeGraphic = exploder;

       /* Create a randomish green hue to paint over the base white
           of the sprite. Apply via the colorfilter and store for later */
        Paint enemyPaint = new Paint();
        float[] hsv = new float[3];
        hsv[0] = 93 - _r.nextInt(20);
        hsv[1] = 0xff;
        hsv[2] = 0xff;
        ColorFilter f = new LightingColorFilter(Color.HSVToColor(hsv), 1);
        enemyPaint.setColorFilter(f);
        setPaint(enemyPaint);
        setPaint(null);
    }

    public void setDynamics() {
        horizSpeed = getRandomDirection();
        angularVelocity = 1;
        angularDirection = getRandomDirection();
        if(angularDirection==0) {
            angularDirection = 1;
        }
        vertSpeed += _r.nextInt(5);
    }

    public void Update() {
        super.Update();
        if (ticker % 50 == 0) {
            horizSpeed = getRandomDirection(); // switch randomly
        }
    }

    public int getRandomDirection() {
        return (_r.nextInt(3) - 1);
    }

    public void explode() {
        kill();
        if(explodeGraphic!=null) {
            setGraphic(explodeGraphic);
            setPaint(null);
            exploding = true;
            explodeTicker = ticker;
        }
    }

    public boolean isExploding() {
        if(exploding) {
            if((ticker-explodeTicker)<2) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}