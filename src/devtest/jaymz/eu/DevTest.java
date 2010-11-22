package devtest.jaymz.eu;

import java.util.ArrayList;
import java.util.Random;
import java.lang.System;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Paint;
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
        private long gameTimer;
        private long startTime;

        public TutorialThread(SurfaceHolder surfaceHolder, Panel panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
            startTime = System.currentTimeMillis();
        }

        public void setRunning(boolean run) {
            _run = run;
        }

        @Override
        public void run() {
            Canvas c;
            while (_run) {
                c = null;
                gameTimer = System.currentTimeMillis() - startTime;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        _panel.onDraw(c, gameTimer);
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
        private int score = 0;
        private Typeface zombieFace;
        private Paint paint;
        
        public Panel(Context context) {
            super(context);

            mp = MediaPlayer.create(context, R.raw.zod);

            /*soundManager = new SoundManager();
            soundManager.initSounds(getBaseContext());
            soundManager.addSound(1, R.raw.zod);*/

            background = BitmapFactory.decodeResource(getResources(), R.drawable.base);
            player = BitmapFactory.decodeResource(getResources(), R.drawable.guitar);
            zombieFace = Typeface.createFromAsset(getContext().getAssets(), "ZOMBIE.TTF");

            paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setTextSize(16);
            paint.setTypeface(zombieFace);
            paint.setShadowLayer(1, 0, 5, 0);

            getHolder().addCallback(this);
            setFocusable(true);
        }

        public void onDraw(Canvas canvas, long gameTime) {
            canvas.drawBitmap(background, 0, 0, null);
            canvas.drawBitmap(player, posX, posY, null);
            for(Swarm swarm : swarms) {
                swarm.draw(canvas);
            }

            score += _r.nextInt(20);

            paint.setColor(Color.RED);
            canvas.drawText("SWARMS: "+swarms.size(), 10, 20, paint);
            paint.setColor(Color.GREEN);
            canvas.drawText("SCORE: "+score, 120, 20, paint);
            paint.setColor(Color.YELLOW);
            canvas.drawText("TIME: "+gameTime/1000, 250, 20, paint);
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
