package devtest.jaymz.eu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import java.util.Random;
import java.util.ArrayList;

import devtest.jaymz.eu.Sprite;
import devtest.jaymz.eu.Enemy;
import devtest.jaymz.eu.Swarm;

class Bullet extends Sprite {

    public Bullet() {
        super();
        setVertSpeed(-25); // goes up
        setHorizSpeed(0); // no drift
        setScale(0.2f);
    }

    public int detectCollisions(ArrayList<Swarm> swarms, int inputScore) {
        for(int s=0; s<swarms.size(); s++) {
            Swarm swarm = (Swarm)swarms.get(s);
            ArrayList<Enemy> enemies = swarm.getEnemies();
            for(int e=0; e<enemies.size(); e++) {
                Enemy enemy = (Enemy)enemies.get(e);
                if(enemy.coordinatesWithinBounds(this.getCoordinates())) {
                    enemies.remove(e);
                    inputScore += 100;
                    Log.d("BUL", "Removed enemy # "+e+" from swarm #"+s);
                }
            }
        }
        return inputScore;
    }

}