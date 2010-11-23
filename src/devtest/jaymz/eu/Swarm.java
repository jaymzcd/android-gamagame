package devtest.jaymz.eu;

import java.util.Random;
import java.util.ArrayList;
import android.util.Log;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.BitmapFactory;

import devtest.jaymz.eu.Enemy;

class Swarm {
    private ArrayList<Enemy> sprites = new ArrayList<Enemy>();
    private int cnt = 2; // number to make
    private Random _r = new Random();
    private boolean alive = true; // whether or not all our sprites are "dead"

    public Swarm(Context context, float x, float y) {
        for(int i=0; i<cnt; i++) {
            Enemy sprite = new Enemy(BitmapFactory.decodeResource(context.getResources(), getRandomEnemy()));
            sprite.getCoordinates().setX((int) x - sprite.getGraphic().getWidth() / 2 + _r.nextInt(10)-20);
            sprite.getCoordinates().setY((int) y - sprite.getGraphic().getHeight() / 2 + _r.nextInt(10)-20);
            sprites.add(sprite);
        }
    }

    public void draw(Canvas canvas) {
        for(int i=0; i<sprites.size(); i++) {
            Enemy sprite = (Enemy)sprites.get(i);
            if (sprite.isAlive(canvas.getHeight())) {
                sprite.Update();
                canvas.drawBitmap(sprite.getGraphic(), sprite.getMatrix(), sprite.getPaint());

            } else {
                sprites.remove(i);
            }
        }
    }

    public int getRandomEnemy() {
        int roll = _r.nextInt(5);
        switch(roll) {
            case 0:
                return R.drawable.philly1;
            case 1:
                return R.drawable.luke1;
            case 2:
                return R.drawable.joe1;
            case 3:
                return R.drawable.paul1;
            case 4:
                return R.drawable.domo1;
        }
        return R.drawable.philly1;
    }

    public boolean isAlive() {
        if(sprites.size()==0) {
            alive = false;
        }
        return alive;
    }
}