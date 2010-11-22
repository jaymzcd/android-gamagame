package devtest.jaymz.eu;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Color;
import android.graphics.Canvas;
import java.util.Random;
import android.util.Log;

class Sprite {
    private Bitmap _bitmap;
    private Coordinates _coordinates;
    private int _ticker = 0;
    private int _direction = 1;
    private int _speed = 1;
    private int _rotation = 0;
    private float _scale = 2;
    private String _name;
    static protected int cnt = 0;
    
    private Random _r = new Random();
    private Paint _paint;

    public Sprite(Bitmap bitmap) {
        Sprite.cnt += 1;
        
        _direction = _r.nextInt(3) - 1;
        _speed += _r.nextInt(5);
        _bitmap = bitmap;
        _coordinates = new Coordinates();

        /* Create a randomish green hue to paint over the base white
           of the sprite. Apply via the colorfilter and store for later */
        _paint = new Paint();
        float[] hsv = new float[3];
        hsv[0] = 93 - _r.nextInt(20);
        hsv[1] = 0xff;
        hsv[2] = 0xff;

        //ColorFilter f = new LightingColorFilter(Color.HSVToColor(hsv), 1);
        ColorFilter f = new LightingColorFilter(Color.rgb(255, 255 ,255), 1);
        _paint.setColorFilter(f);

        Log.d("SPR", "Made new sprite: " + this.getName());
        this.getCnt();
    }

    static public void getCnt() {
        Log.d("SPR", "Now: " + Sprite.cnt);
    }

    public void kill() {
        Log.d("SPR", "Killing sprite: " + this.getName());
        this.getCnt();
        Sprite.cnt -= 1;
    }

    public String getName() {
        return "Sprite #" + Sprite.cnt;
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

    public int rotation() {
        _rotation += 1;
        return _rotation;
    }

    public float getScale() {
        _scale -= (float)0.002;
        if (_scale > (float)0.5) {
            return _scale;
        } else {
            return (float)0.5;
        }
    }
    
    public Canvas Update(Canvas canvas) {
        _coordinates.setX(_coordinates.getX()+1*_direction);
        _coordinates.setY(_coordinates.getY()+_speed);
        float scale = this.getScale();
        canvas.rotate(this.rotation()*_direction, _coordinates.getX(), _coordinates.getY());
        canvas.scale(scale, scale, _coordinates.getX(), _coordinates.getY());
        canvas.drawBitmap(this.getGraphic(), this.getCoordinates().getX(), this.getCoordinates().getY(), this.getPaint());
        return canvas;
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