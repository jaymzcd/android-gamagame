package devtest.jaymz.eu;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.View;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.MotionEvent;
import android.view.Window;

import devtest.jaymz.eu.Sprite;

public class DevTest extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new Panel(this));
    }

    class TutorialThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private Panel _panel;
        private boolean _run = false;

        public TutorialThread(SurfaceHolder surfaceHolder, Panel panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }

        public void setRunning(boolean run) {
            _run = run;
        }

        @Override
        public void run() {
            Canvas c;
            while (_run) {
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        _panel.onDraw(c);
                    }
                } finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }

        }

        public SurfaceHolder getSurfaceHolder() {
            return _surfaceHolder;
        }

    }

    class Panel extends SurfaceView implements SurfaceHolder.Callback {

        private TutorialThread _thread;
        private ArrayList<Sprite> _sprites = new ArrayList<Sprite>();
        private Random _r = new Random();
        
        public Panel(Context context) {
            super(context);
            getHolder().addCallback(this);
            _thread = new TutorialThread(getHolder(), this);
            setFocusable(true);
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawColor(Color.BLACK);
            for(Sprite sprite : _sprites) {
                sprite.Update();
                Paint p = new Paint();
                ColorFilter f = new LightingColorFilter(Color.rgb(_r.nextInt(255), _r.nextInt(255), _r.nextInt(255)), 1);
                p.setColorFilter(f);
                canvas.drawBitmap(sprite.getGraphic(), sprite.getCoordinates().getX(), sprite.getCoordinates().getY(), p);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            _thread.setRunning(true);
            _thread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            _thread.setRunning(false);
            while (retry) {
                try {
                    _thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    // we will try it again and again...
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            synchronized (_thread.getSurfaceHolder()) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Sprite sprite = new Sprite(BitmapFactory.decodeResource(getResources(), R.drawable.invader));
                    sprite.getCoordinates().setX((int) event.getX() - sprite.getGraphic().getWidth() / 2);
                    sprite.getCoordinates().setY((int) event.getY() - sprite.getGraphic().getHeight() / 2);
                    _sprites.add(sprite);
                }
                return true;
            }
        }

    }
}
