package devtest.jaymz.eu;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Color;
import java.util.Random;
import android.util.Log;

class Sprite {
    private Bitmap _bitmap;
    private Coordinates _coordinates;
    private int _ticker = 0;
    private int _direction = 1;
    private int _speed = 1;
    
    private Random _r = new Random();
    private Paint _paint;

    public Sprite(Bitmap bitmap) {

        _direction = _r.nextInt(3) - 1;
        _speed += _r.nextInt(5);
        _bitmap = bitmap;

        _coordinates = new Coordinates();
        Log.d("SPR", "Direction is " + _direction);

        /* Create a randomish green hue to paint over the base white
           of the sprite. Apply via the colorfilter and store for later */
        _paint = new Paint();
        float[] hsv = new float[3];
        hsv[0] = 93 - _r.nextInt(20);
        hsv[1] = 0xff;
        hsv[2] = 0xff;
        Log.d("SPR", "HSV: "+hsv[0]+" "+hsv[1]+" "+hsv[2]);

        ColorFilter f = new LightingColorFilter(Color.HSVToColor(hsv), 1);
        _paint.setColorFilter(f);
    }

    public Bitmap getGraphic() {
        return _bitmap;
    }

    public Coordinates getCoordinates() {
        return _coordinates;
    }

    public Paint getPaint() {
        return _paint;
    }
    
    public void Update() {
        _coordinates.setX(_coordinates.getX()+1*_direction);
        _coordinates.setY(_coordinates.getY()+_speed);
    }


    // Co-ordinate holding class
    public class Coordinates {
        private int _x = 100;
        private int _y = 0;

        public int getX() {
            return _x + _bitmap.getWidth() / 2;
        }

        public void setX(int value) {
            _x = value - _bitmap.getWidth() / 2;
        }

        public int getY() {
            return _y + _bitmap.getHeight() / 2;
        }

        public void setY(int value) {
            _y = value - _bitmap.getHeight() / 2;
        }

        public String toString() {
            return "Coordinates: ("+_x+", "+_y+")";
        }
    }
}