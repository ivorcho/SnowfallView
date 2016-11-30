package com.ivorcho.snowfallview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * SnowfallView is a simple view that can animate any image as a snowflake falling down the screen.
 *
 * See {@link com.ivorcho.snowfallview.R.styleable#SnowfallView SnowfallView Attributes}
 *
 * @attr ref com.ivorcho.snowfallview.R.styleable#SnowfallView_snowflake
 */
public class SnowfallView extends View {

    private Random random = new Random();

    private Bitmap flakeImage;
    private Set<Snowflake> flakes       = new HashSet<>();
    private Set<Snowflake> trashyFlakes = new HashSet<>();

    private              int   wind           = 0;
    private              long  lastWindUpdate = System.currentTimeMillis();
    private              int   windCurveIndex = 0;
    private final static int[] windCurve      = { 0, 1, 0, 1, 2, 1, 0, -1, -2, -1, 0, 1 };

    private long snowflakeSpawnRate = 800;
    private long lastFlakeSpawn     = 0;


    public SnowfallView(Context context, Bitmap flakeImage) {
        super(context);
        this.flakeImage = flakeImage;
    }


    public SnowfallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SnowfallView, 0, 0);

        try {
            int resId = a.getResourceId(R.styleable.SnowfallView_snowflake, 0);
            if (resId != 0) {
                flakeImage = BitmapFactory.decodeResource(a.getResources(), resId);
            }

            snowflakeSpawnRate = a.getInt(R.styleable.SnowfallView_spawnRate, 800);
        } finally {
            a.recycle();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (flakeImage != null) {
            update(canvas);
            invalidate();
        }
    }


    /**
     * Sets a bitmap image which to be used as a snowflake
     *
     * @param flakeImage Bitmap
     */
    public void setFlakeImage(Bitmap flakeImage) {
        this.flakeImage = flakeImage;
    }


    /**
     * Sets a image resource which to be used as a snowflake
     *
     * @param resId int
     */
    public void setFlakeImage(int resId) {
        flakeImage = BitmapFactory.decodeResource(getContext().getResources(), resId);
    }


    /**
     * Sets the rate at which a new snowflake is spawned
     *
     * @param rate long the rate at which a new snowflake is spawned in milliseconds
     */
    public void setSnowflakeSpawnRate(long rate) {
        this.snowflakeSpawnRate = rate;
    }


    private void update(Canvas c) {
        long nowTime = System.currentTimeMillis();

        if (nowTime - lastFlakeSpawn > snowflakeSpawnRate) {
            flakes.add(new Snowflake(random.nextFloat() * c.getWidth(), -flakeImage.getWidth(), random.nextInt(7),
                    flakeImage));
            lastFlakeSpawn = nowTime;
        }

        //update the wind every 3 seconds
        if (nowTime - lastWindUpdate > 3000) {
            if (windCurveIndex < windCurve.length - 1) {
                wind = windCurve[++windCurveIndex];
                lastWindUpdate = nowTime;
            } else {
                windCurveIndex = 0;
                wind = 0;
                lastWindUpdate = nowTime;
            }
        }

        for (Snowflake flake : flakes) {
            flake.update(wind);
            c.drawBitmap(flake.bitmap, flake.x, flake.y, null);

            if (flake.y > c.getHeight()) {
                trashyFlakes.add(flake);
            }
        }

        flakes.removeAll(trashyFlakes);
        trashyFlakes.clear();
    }


    class Snowflake {

        Bitmap bitmap;
        float x;
        float y;
        int     bias      = 0;
        int     biasTime  = 0;
        boolean interBias = false;
        int speed;


        Snowflake(float x, float y, int s, Bitmap bitmap) {
            this.x = x;
            this.y = y;
            this.speed = s;
            this.bitmap = bitmap;
        }


        void update(int wind) {
            if (biasTime < 0) {
                if (interBias) {
                    bias = ((Integer) random.nextInt(3)).compareTo(1);
                    interBias = false;
                } else {
                    bias = 0;
                    interBias = true;
                }

                biasTime = random.nextInt(30);
            }

            biasTime--;
            x += bias + wind;
            y += 4 + speed;
        }
    }
}
