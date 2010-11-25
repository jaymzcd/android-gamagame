package devtest.jaymz.eu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Color;
import android.graphics.Matrix;
import android.content.Context;

import java.util.Random;
import android.util.Log;

import devtest.jaymz.eu.Sprite;
import devtest.jaymz.eu.Bullet;

class Player extends Sprite {

    private Bitmap bulletImage;
    private int lastFired = 0;
    private int fireDelay = 2;

    public Player(Context context) {
        super();
        setGraphic(BitmapFactory.decodeResource(context.getResources(), R.drawable.guitar));
        bulletImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        setScale(0.5f);
        int offsets[] = {0, -40};
        setOffset(offsets);
    }

    public Bullet fireBullet() {
        if((ticker-lastFired)>fireDelay) {
            Bullet bullet = new Bullet();
            bullet.setGraphic(bulletImage);
            bullet.getCoordinates().setX(this.getCoordinates().getX());
            bullet.getCoordinates().setY(this.getCoordinates().getY());
            lastFired = ticker;
            return bullet;
        }
        return null;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public void Update() {
        ticker++;
    }
}