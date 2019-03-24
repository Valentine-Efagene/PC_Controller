package com.example.valentyne.pc_controller;

import android.util.Log;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public class MouseSketch extends PApplet {
    private static final String TAG = "MouseSketch";
    int x, y;
    MouseActivity activity;
    int ANDROID_ALLOWANCE = 150;
    PFont font;
    int w, h;
    Mouse mouse;
    String screenSize;
    int screenWidth;
    int screenHeight;
    private Clicker left;
    private Clicker right;
    private float lastClick = millis();
    private boolean doubleClicked = false;
    private int clickerDefaultColor = color(213, 33, 323);

    public MouseSketch(MouseActivity activity) {
        this.activity = activity;
        w = activity.getDisplayMetrics().widthPixels;
        h = activity.getDisplayMetrics().heightPixels;
        h = activity.getWindowManager().getDefaultDisplay().getHeight();

        if(activity.getScreenSize() == null) {
            delay(100);
        }

        screenSize = activity.getScreenSize();
        screenWidth = getWidth(screenSize);
        screenHeight = getHeight(screenSize);
    }

    int getWidth(String s)
    {
        char[] a = s.toCharArray();
        int c, offset = 0, n = 0;

        for (c = offset; a[c] >= '0' && a[c] <= '9'; c++)
        {
            n = n * 10 + a[c] - '0';
        }

        return n;
    }

    int getHeight(String s)
    {
        char[] a = s.toCharArray();
        int c, offset = 0, i = 0, n = 0;

        while (a[i] != ' ') {
            offset = i + 2;
            i++;
        }

        for (c = offset; a[c] >= '0' && a[c] <= '9'; c++)
        {
            n = n * 10 + a[c] - '0';
        }

        return n;
    }

    public void settings() {
        //fullScreen();
        size(w,h - ANDROID_ALLOWANCE);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setup() {
        stroke(0);
        font = createFont("SansSerif", 40);
        mouse = new Mouse(width/2, height/2);
        left = new Clicker(0, height - 2 * ANDROID_ALLOWANCE);
        right = new Clicker(0, ANDROID_ALLOWANCE);
    }

    public void draw() {
        background(128);
        fill(128, 22, 23);
        rectMode(CENTER);
        rect(width/2, height/2, width - 2 * ANDROID_ALLOWANCE, height - 2 * ANDROID_ALLOWANCE);
        rectMode(CORNER);
        mouse.draw();
        left.draw();
        right.draw();
        x = (int)mouse.getPosition().y;
        y = width - (int)mouse.getPosition().x;
        activity.setX((int)map(x, ANDROID_ALLOWANCE, height - ANDROID_ALLOWANCE, 0, screenWidth));
        activity.setY((int)map(y, ANDROID_ALLOWANCE, width - ANDROID_ALLOWANCE, 0, screenHeight));

        textFont(font);
        text(screenWidth + ", " + screenHeight, 100, ANDROID_ALLOWANCE + 50);
    }

    public void mouseDragged() {
        if(mouseX < ANDROID_ALLOWANCE || mouseX > width - ANDROID_ALLOWANCE ||
                mouseY < ANDROID_ALLOWANCE || mouseY > height - ANDROID_ALLOWANCE) {
            return;
        }

        mouse.move(mouseX, mouseY);
    }

    public void mouseReleased() {
        left.color = clickerDefaultColor;
        right.color = clickerDefaultColor;
    }

    public void mousePressed() {
        if(mouseX < ANDROID_ALLOWANCE || mouseX > width - ANDROID_ALLOWANCE ||
                mouseY < ANDROID_ALLOWANCE || mouseY > width - ANDROID_ALLOWANCE) {
            Log.w(TAG, "Pressed");
            if(mouseX > left.pos.x && mouseX < left.pos.x + left.w &&
                    mouseY > left.pos.y && mouseY < left.pos.y + left.h) {
                activity.setAction("A");
                left.color = color(0, 0, 255);
                Log.w(TAG, "Left click");
            }

            if(mouseX > right.pos.x && mouseX < right.pos.x + right.w &&
                    mouseY > right.pos.y && mouseY < right.pos.y + right.h) {
                activity.setAction("B");
                right.color = color(0, 0, 255);
                Log.w(TAG, "Right click");
            }

            /*if(doubleClick()) {
                doubleClicked = true;
            }

            if(get(mouseX, mouseY) == leftClicker.getColor() && !doubleClicked) {
                keys += "A";
                Log.w(TAG, "Single Clicked");
                activity.setPressed(true);
            }else if(get(mouseX, mouseY) == leftClicker.getColor() && doubleClicked){
                keys += "AA";
                Log.w(TAG, "Double Clicked");
                activity.setPressed(true);
            }

            doubleClicked = false;*/
            return;
        }

        mouse.move(mouseX, mouseY);
    }

    boolean doubleClick() {
        float currentClick = millis();
        float clickDifference = currentClick - lastClick;
        lastClick = currentClick;
        float doubleClickSpeed = 500;

        if(clickDifference < doubleClickSpeed) {
            return true;
        }

        return false;
    }

    public class Clicker{
        PVector pos;
        int w, h;
        int color;

        public Clicker(int x, int y) {
            pos = new PVector(x, y);
            color = clickerDefaultColor;
            w = 150;
            h = ANDROID_ALLOWANCE;
        }

        public void draw() {
            fill(color);
            rect(pos.x, pos.y, w, h);
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

    public class Mouse {
        private PVector position;
        final int DIAMETER = 100;
        private int color;

        public int getColor() {
            return color;
        }


        public Mouse(int x, int y){
            position = new PVector();
            this.position.x = x;
            this.position.y = y;
            color = color(0);
        }

        public void draw()
        {
            fill(color);
            ellipse(position.x, position.y, DIAMETER, DIAMETER);
        }

        public void move(int x, int y) {
            position.x = x;
            position.y = y;
        }

        public PVector getPosition(){
            return position;
        }
    }
}
