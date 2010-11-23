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

class Player extends Sprite {
    public Player(Context context) {
        super();
        setGraphic(BitmapFactory.decodeResource(context.getResources(), R.drawable.guitar));
    }
}