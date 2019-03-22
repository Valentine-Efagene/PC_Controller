package com.example.valentyne.pc_controller;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public class MouseSketch extends PApplet {
    int x, y;
    final int ALLOWANCE = 150;
    final int RADIUS = 100;
    PVector pos = new PVector();
    MouseActivity activity;
    int ANDROID_ALLOWANCE = 150;
    PFont font;
    int w, h;
    Mouse mouse;
    String screenSize;
    int screenWidth;
    int screenHeight;

    public MouseSketch(MouseActivity activity) {
        this.activity = activity;
        mouse = new Mouse(w/2, h/2);
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
        size(w ,h - ANDROID_ALLOWANCE);
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
    }

    public void draw() {
        background(128);
        mouse.draw();
        x = (int)mouse.getPosition().y;
        y = (int)mouse.getPosition().x - w;
        activity.setX((int)map(x, 0, h, 0, screenWidth));
        activity.setY((int)map(y, 0, w, 0, screenHeight));

        textFont(font);
        text(screenWidth + ", " + screenHeight, 100, ANDROID_ALLOWANCE + 50);
    }

    public void mouseDragged() {
        mouse.move(mouseX, mouseY);
    }

    public class Mouse {
        private PVector position;
        final int DIAMETER = 100;

        public Mouse(int x, int y){
            position = new PVector();
            this.position.x = x;
            this.position.y = y;
        }

        public void draw()
        {
            fill(0);
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
