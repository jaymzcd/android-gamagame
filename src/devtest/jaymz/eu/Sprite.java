package devtest.jaymz.eu;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Color;
import android.graphics.Matrix;
import java.lang.Math;
import java.util.Random;
import android.util.Log;

class Sprite {
    protected Bitmap _bitmap;
    protected Coordinates coordinates ;
    protected int horizSpeed;
    protected int speed;
    protected int vertSpeed;
    protected boolean alive = true;
    protected float scale;
    protected float angularVelocity;
    protected int angularDirection;
    protected Random _r = new Random();
    protected Paint paint;
    protected Matrix matrix;
    protected int xOffset = 0;
    protected int yOffset = 0;
    protected boolean applyOffset = true;

    protected int ticker = 0; // used to make changes as updates ticks by

    private boolean DEBUG = false;

    public Sprite() {
        coordinates  = new Coordinates();
        matrix = new Matrix();
        paint = new Paint();
        setDynamics();
        setPaint(null);
        setScale(1);
    }

    public Sprite(Bitmap bitmap) {
        this();
        setGraphic(bitmap);
    }

    public void setDynamics() {
        horizSpeed = 0;
        vertSpeed = 0;
        angularVelocity = 0;
        angularDirection = 0;
    }

    public void setPaint(Paint inputPaint) {
        paint = inputPaint;
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
        coordinates.setX(coordinates.getX()+horizSpeed);
        coordinates.setY(coordinates.getY()+vertSpeed);
    }

    public boolean withinCanvas(Canvas canvas) {
        // Check if sprite is within the canvas
        int maxY = canvas.getHeight();
        int maxX = canvas.getWidth();

        if ((coordinates._y > maxY)||(coordinates._y < 0)||(coordinates._x > maxX)||(coordinates._x < 0)) {
            return false;
        }
        return true;
    }

    public void enableOffset() {
        applyOffset = true;
    }

    public void disableOffset() {
        applyOffset = false;
    }

    public Matrix getMatrix() {
        // Used to transform and pass position info to canvas
        int x = coordinates.getX();
        int y = coordinates.getY();
        matrix.setTranslate(coordinates._x, coordinates._y);
        if(this.applyOffset) {
            matrix.postTranslate(xOffset, yOffset);
        }
        matrix.postScale(scale, scale, x, y);
        if(Math.abs(angularVelocity)>0) {
            matrix.postRotate(ticker*angularVelocity*angularDirection, x, y);
        }
        return matrix;
    }

    public void setScale(float _scale) {
        scale = _scale;
    }

    public void setVertSpeed(int direction) {
        vertSpeed = direction;
    }

    public void setHorizSpeed(int speed) {
        horizSpeed = speed;
    }

    public void setOffset(int offset[]) {
        // Force a reposition the sprite when drawing on top of
        // any dynamic or event co-ords
        xOffset = offset[0];
        yOffset = offset[1];
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(getGraphic(), getMatrix(), getPaint());
    }


    /* Following 4 methods are helpers to get our bounding box
       for the basic rectangular "collision detection" */
    public int topPoint() {
        return coordinates._y;
    }

    public int leftPoint() {
        return coordinates._x;
    }

    public int rightPoint() {
        return coordinates._x + _bitmap.getWidth();
    }
    
    public int bottomPoint() {
        return coordinates._y + _bitmap.getWidth();
    }

    /* Simple rectangular collision detection */
    public boolean coordinatesWithinBounds(Sprite.Coordinates coordinates) {
        if(DEBUG) {
            Log.d("SPR", "Bounds for sprite ("+leftPoint()+", "+rightPoint()+"), ("+topPoint()+", "+bottomPoint()+")");
            Log.d("SPR", "Test coords ("+coordinates._x+", "+coordinates._y+")");
        }
        if((coordinates.getX()>leftPoint()&&coordinates.getX()<rightPoint())&&(coordinates.getY()>topPoint()&&coordinates.getY()<bottomPoint())) {
            if(DEBUG)
                Log.d("SPR", "Within BOUNDS!");
            return true;
        }
        return false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
    }

    public void setAlive() {
        alive = true;
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