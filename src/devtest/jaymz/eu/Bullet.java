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
    private boolean DEBUG = true;
    private static int cnt = 0;
    public int name = 0;

    protected boolean spent = false; // whether this bullet has been "used"

    public Bullet() {
        super();
        setVertSpeed(-20); // goes up
        setHorizSpeed(0); // no drift
        setScale(0.3f);

        Bullet.cnt+=1;
        name = Bullet.cnt;
    }

    public int[] detectCollisions(ArrayList<Swarm> swarms) {
        int score = 0;
        int kills = 0;
        
        for(int s=0; s<swarms.size(); s++) {
            Swarm swarm = (Swarm)swarms.get(s);
            ArrayList<Enemy> enemies = swarm.getEnemies();
            for(int e=0; e<enemies.size(); e++) {
                Enemy enemy = (Enemy)enemies.get(e);
                if(enemy.isAlive()&&enemy.coordinatesWithinBounds(this.getCoordinates())&&!spent) {
                    enemy.explode();
                    spent = true;
                    score = 100;
                    kills = 1;
                    if(DEBUG)
                        Log.d("BUL", "TICKER: "+ticker+" Bullet #"+name+" Removed enemy #"+e+" from swarm #"+s+" Spent is "+spent);
                }
            }
        }
        int data[] = {score, kills};
        return data;
    }

}