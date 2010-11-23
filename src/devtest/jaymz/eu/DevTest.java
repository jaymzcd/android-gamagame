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
import devtest.jaymz.eu.Player;
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
        private Player player;
        private float posX, posY;
        private int score = 0;

        private Typeface zombieFace;
        private Typeface solsticeFace;
        private Paint infoPaint;
        private Paint msgPaint;

        private long gameTimer;
        private long startTime;

        private final int COUNTDOWN = 5;
        private final int MAX_SWARMS = 5;

        public Panel(Context context) {
            super(context);

            mp = MediaPlayer.create(context, R.raw.zod);

            /*soundManager = new SoundManager();
            soundManager.initSounds(getBaseContext());
            soundManager.addSound(1, R.raw.zod);*/

            background = BitmapFactory.decodeResource(getResources(), R.drawable.base);
            player = new Player(context);
            zombieFace = Typeface.createFromAsset(getContext().getAssets(), "zombie.ttf");
            solsticeFace = Typeface.createFromAsset(getContext().getAssets(), "solstice.ttf");

            infoPaint = new Paint();
            infoPaint.setStyle(Paint.Style.FILL);
            infoPaint.setAntiAlias(true);
            infoPaint.setTextSize(16);
            infoPaint.setTypeface(zombieFace);
            infoPaint.setShadowLayer(1, 0, 5, 0);

            msgPaint = new Paint();
            msgPaint.setStyle(Paint.Style.FILL);
            msgPaint.setAntiAlias(true);
            msgPaint.setTextSize(34);
            msgPaint.setTypeface(solsticeFace);
            msgPaint.setShadowLayer(3, 0, 5, 0);
            msgPaint.setColor(Color.WHITE);

            getHolder().addCallback(this);
            setFocusable(true);
        }


        public boolean checkForSwarms() {
            // Introduces new swarms every few seconds as long as the
            // current total is within the limit 
            if((int)gameSeconds() % 3 == 0 && (swarms.size() < MAX_SWARMS)) {
                Swarm swarm = new Swarm(getContext(), _r.nextInt(200)+50, 80);
                swarms.add(swarm);
                return true;
            }
            return false;
        }

        public void onDraw(Canvas canvas) {
            // Update our timers and swarm enemies
            gameTimer = System.currentTimeMillis() - startTime;
            checkForSwarms();

            // Start drawing bits - background & player layers
            canvas.drawBitmap(background, 0, 0, null);
            drawPlayer(canvas);

            // Update and draw the enemies, checking if we have any "dead"
            // swarms first and if so removing them so they can be repopulated
            for(int i=0; i<swarms.size(); i++) {
                Swarm swarm = (Swarm)swarms.get(i);
                if(swarm.isAlive()) {
                    swarm.draw(canvas);
                } else {
                    swarms.remove(swarm);
                }
            }

            // Time dependant drawing
            if(gameSeconds()>COUNTDOWN) {
                score += _r.nextInt(20);
            }
            if (gameSeconds()<COUNTDOWN) {
                drawUIText(canvas, "GET READY", msgPaint, 250);
            }

            // Finally info / score
            drawHUD(canvas);
        }

        public void drawPlayer(Canvas canvas) {
            float playerX = posX - (player.getGraphic().getWidth()/2);
            float playerY = posY - (player.getGraphic().getHeight()/2);
            canvas.drawBitmap(player.getGraphic(), playerX, playerY, null);
        }

        public void drawHUD(Canvas canvas) {
            // Draws common information on the very top of the canvas
            infoPaint.setColor(Color.RED);
            drawUIText(canvas, "SWARMS: "+swarms.size(), infoPaint, 10, 20);
            infoPaint.setColor(Color.GREEN);
            drawUIText(canvas, "SCORE: "+score, infoPaint, 120, 20);
            infoPaint.setColor(Color.YELLOW);
            drawUIText(canvas, "TIME: "+(int)gameSeconds(), infoPaint, 250, 20);
        }

        public void drawUIText(Canvas canvas, String msg, Paint paint, float xpos, float ypos) {
            // Helper to draw text in the canvas
            canvas.drawText(msg, xpos, ypos, paint);
        }

        public void drawUIText(Canvas canvas, String msg, Paint paint, float ypos) {
            // Draws Text centered with vertical offset
            float textWidth = paint.measureText(msg);
            float xpos = (canvas.getWidth() - textWidth) / 2;
            canvas.drawText(msg, xpos, ypos, paint);
        }

        public float gameSeconds() {
            // Returns the total seconds for the game so far (eg 4.125)
            return (float)(gameTimer/1000.0);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //soundManager.playSound(1);
            //Log.d("GBB", "Surface created");
            startTime = System.currentTimeMillis();

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
            // Following store position for player via touch
            posX = event.getX();
            posY = event.getY();

            synchronized (_thread.getSurfaceHolder()) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                }
                return true;
            }
        }

    }
}
