package devtest.jaymz.eu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import java.util.Random;

import devtest.jaymz.eu.Sprite;

class Bullet extends Sprite {

    public Bullet() {
        super();
        setVertSpeed(-1); // goes up
        setHorizSpeed(0); // no drift
        setScale(0.2f);
    }

}