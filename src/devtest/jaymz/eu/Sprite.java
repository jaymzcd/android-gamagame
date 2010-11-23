package devtest.jaymz.eu;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Color;
import android.graphics.Matrix;
import java.util.Random;
import android.util.Log;

class Sprite {
    protected Bitmap _bitmap;
    protected Coordinates coordinates ;
    protected int _ticker = 0;
    protected int direction = 1;
    protected int speed = 1;
    protected boolean alive = true;
    protected Matrix matrix;
    protected float scale;
    protected float angularVelocity;
    protected int angularDirection;

    protected Random _r = new Random();
    protected Paint paint;

    protected int ticker = 0; // used to make changes as updates ticks by

    public Sprite() {
        coordinates  = new Coordinates();
        matrix = new Matrix();
        paint = new Paint();
        setDynamics();
        setPaint();
    }

    public Sprite(Bitmap bitmap) {
        this();
        setGraphic(bitmap);
    }

    public void setDynamics() {
        direction = 1;
        speed = 10;
        angularVelocity = 0;
        angularDirection = 0;
    }

    public void setPaint() {
        // Holder
    }

    public Bitmap getGraphic() {
        return _bitmap;
    }

    public void setGraphic(Bitmap bitmap) {
        _bitmap = bitmap;
    }

    public Coordinates getCoordinates() {
        return coordinates ;
    }

    public Paint getPaint() {
        return paint;
    }

    public void Update() {
        ticker++;
        coordinates.setX(coordinates.getX()+direction);
        coordinates.setY(coordinates.getY()+speed);
    }

    public boolean isAlive(float bounds) {
        // If sprite y-coord is outside bounds it's dead
        if (coordinates._y > bounds) {
            alive = false;
        }
        return alive;
    }

    public Matrix getMatrix() {
        // Used to transform and pass position info to canvas
        matrix.setRotate(ticker*angularVelocity*angularDirection);
        matrix.postTranslate(coordinates._x, coordinates._y);
        matrix.postScale(scale, scale);
        return matrix;
    }

    public void setScale(float _scale) {
        scale = _scale;
    }

    // Co-ordinate holding class
    public class Coordinates {
        protected int _x = 100;
        protected int _y = 0;

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