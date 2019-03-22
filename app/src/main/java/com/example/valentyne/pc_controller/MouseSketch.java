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

    public MouseSketch(MouseActivity activity) {
        this.activity = activity;
        w = activity.getDisplayMetrics().widthPixels;
        h = activity.getDisplayMetrics().heightPixels;
        h = activity.getWindowManager().getDefaultDisplay().getHeight();
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
        x = width / 2;
        y = height / 2;
        stroke(0);
        font = createFont("SansSerif", 40);
    }

    public void draw() {
        translate(width / 2, height / 2);
        background(255, 255, 255);
        fill(128);
        ellipse(0, 0, RADIUS * 2, RADIUS * 2);
        fill(128);
        line(0, -height / 2, 0, height / 2);
        line(-width / 2, 0, width / 2, 0);

        fill(77, 181, 171);
        ellipse(x, y, RADIUS * 2, RADIUS * 2);

        activity.setX(x);
        activity.setY(-y);

        fill(0);
        textFont(font);
        text(x + ", " + -y, 100, -height / 2 + ANDROID_ALLOWANCE + 50);

        if (!mousePressed) {
            x = 0;
            y = 0;
        }
    }

    public void mouseDragged() {
        x = mouseX - width / 2;
        y = mouseY - height / 2;

        if (dist(0, 0, x, y) > ALLOWANCE) {
            pos.x = x;
            pos.y = y;
            pos.normalize();
            pos.mult(ALLOWANCE);
            x = (int) pos.x;
            y = (int) pos.y;
        }
    }
}
