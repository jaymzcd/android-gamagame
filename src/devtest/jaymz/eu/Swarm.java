package devtest.jaymz.eu;

import java.util.Random;
import java.util.ArrayList;
import android.util.Log;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.BitmapFactory;

import devtest.jaymz.eu.Sprite;

class Swarm {
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    private int cnt = 4; // number to make
    private Random _r = new Random();

    public Swarm(Context context, float x, float y, int imageResource) {
        for(int i=0; i<cnt; i++) {
            Sprite sprite = new Sprite(BitmapFactory.decodeResource(context.getResources(), imageResource));
            sprite.getCoordinates().setX((int) x - sprite.getGraphic().getWidth() / 2 + _r.nextInt(10)-20);
            sprite.getCoordinates().setY((int) y - sprite.getGraphic().getHeight() / 2 + _r.nextInt(10)-20);
            sprites.add(sprite);
        }
    }

    public void draw(Canvas canvas) {
        for(Sprite sprite : sprites) {
            sprite.Update();
            canvas.drawBitmap(sprite.getGraphic(), sprite.getCoordinates().getX(), sprite.getCoordinates().getY(), sprite.getPaint());
        }
    }
}