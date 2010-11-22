package devtest.jaymz.eu;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.MotionEvent;
import android.view.Window;
import android.media.MediaPlayer;
import android.util.Log;

import devtest.jaymz.eu.Sprite;
import devtest.jaymz.eu.Swarm;
import devtest.jaymz.eu.SoundManager;

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
        private ArrayList<Swarm> swarms = new ArrayList<Swarm>();
        private Random _r = new Random();
        private SoundManager soundManager;
        private MediaPlayer mp;
        private Bitmap background;
        private Bitmap player;
        private float posX, posY;
        
        public Panel(Context context) {
            super(context);

            mp = MediaPlayer.create(context, R.raw.zod);

            /*soundManager = new SoundManager();
            soundManager.initSounds(getBaseContext());
            soundManager.addSound(1, R.raw.zod);*/

            background = BitmapFactory.decodeResource(getResources(), R.drawable.base);
            player = BitmapFactory.decodeResource(getResources(), R.drawable.guitar);

            getHolder().addCallback(this);
            setFocusable(true);
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawBitmap(background, 0, 0, null);
            canvas.drawBitmap(player, posX, posY, null);
            for(Swarm swarm : swarms) {
                swarm.draw(canvas);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //soundManager.playSound(1);
            //Log.d("GBB", "Surface created");
            mp.start();
            _thread = new TutorialThread(getHolder(), this);
            _thread.setRunning(true);
            _thread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            mp.stop();
            _thread.setRunning(false);
            while (retry) {
                try {
                    _thread.join();
                    retry = false;
                    //Log.d("GBB", "Thread stopped");
                } catch (InterruptedException e) {
                    // we will try it again and again...
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            posX = event.getX();
            posY = event.getY();

            synchronized (_thread.getSurfaceHolder()) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Swarm swarm = new Swarm(getContext(), posX, posY);
                    swarms.add(swarm);
                }
                return true;
            }
        }

    }
}
