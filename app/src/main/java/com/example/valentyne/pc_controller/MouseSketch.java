package com.example.valentyne.pc_controller;

import android.util.Log;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public class MouseSketch extends PApplet {
    private static final String TAG = "MouseSketch";
    private static final float SCROLL_MAX = 50;
    private int x, y, scrollValue, scrollValueBuf;
    private MouseActivity activity;
    private int ANDROID_ALLOWANCE = 150;
    private PFont font;
    private int w, h;
    private Mouse mouse;
    private int screenWidth;
    private int screenHeight;
    private Clicker left;
    private Clicker right;
    private Clicker drag;
    private float lastClick = millis();
    private int clickerDefaultColor = color(213, 33, 323);
    private boolean holding = false;
    private ArrayList<String> keysPressed = new ArrayList<String>();
    private Scroller scroller;

    public MouseSketch(MouseActivity activity) {
        String screenSize;
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
        scroller = new Scroller((int)(height - 0.5 * ANDROID_ALLOWANCE) );
        mouse = new Mouse(width/2, height/2);
        left = new Clicker(0, ANDROID_ALLOWANCE);
        drag = new Clicker(width - ANDROID_ALLOWANCE, height - 2 * ANDROID_ALLOWANCE );
        right = new Clicker(0, height - 2 * ANDROID_ALLOWANCE);
    }

    public void draw() {
        background(128);
        fill(128, 22, 23);
        rectMode(CENTER);
        rect(width/2, height/2, width - 2 * ANDROID_ALLOWANCE, height - 2 * ANDROID_ALLOWANCE);
        rectMode(CORNER);

        if(!mousePressed || (mousePressed && get(mouseX, mouseY) == scroller.getColor())){
            if(!(scroller.getPosition().x == width/2)){
                scrollValueBuf = (int)scroller.getPosition().x - width/2;

                if(scrollValueBuf < 0){
                    scrollValue = (int)map(scrollValueBuf, 0, -width/2, 0, -SCROLL_MAX);
                }else{
                    scrollValue = (int)map(scrollValueBuf, 0, width/2, 0, SCROLL_MAX);
                }

                activity.setAction(new Integer(scrollValue).toString());
                scroller.setX(width/2);
            }
        }

        if(!mousePressed) {
            left.setColor(clickerDefaultColor);
            drag.setColor(clickerDefaultColor);
            right.setColor(clickerDefaultColor);
        }

        scroller.draw();
        mouse.draw();
        left.draw();
        drag.draw();
        right.draw();
        x = (int)mouse.getPosition().y;
        y = width - (int)mouse.getPosition().x;
        activity.setX((int)map(x, ANDROID_ALLOWANCE, height - ANDROID_ALLOWANCE, 0, screenWidth));
        activity.setY((int)map(y, ANDROID_ALLOWANCE, width - ANDROID_ALLOWANCE, 0, screenHeight));

        fill(0);
        textFont(font);
        text(screenWidth + ", " + screenHeight, 50, 50);

        for(int i = 0; i < keysPressed.size(); i++) {
            if(keysPressed.get(i) == "C") {
                text("Dragging", width - 2 * ANDROID_ALLOWANCE, 50);
            }
        }
    }

    public void mouseDragged() {
        if(mouseX < ANDROID_ALLOWANCE || mouseX > width - ANDROID_ALLOWANCE ||
                mouseY < ANDROID_ALLOWANCE || mouseY > height - ANDROID_ALLOWANCE) {
            if(get(mouseX, mouseY) == scroller.getColor()){
                text(new Integer((int)map(mouseX - width/2, 0, 740, 0,
                        SCROLL_MAX)).toString(), width - 2 * ANDROID_ALLOWANCE, 50);
                scroller.setX(mouseX);
            }

            return;
        }

        mouse.move(mouseX, mouseY);
    }

    public void mouseReleased() {
        left.color = clickerDefaultColor;
        right.color = clickerDefaultColor;

        if(mouseX < ANDROID_ALLOWANCE || mouseX > width - ANDROID_ALLOWANCE ||
                mouseY < ANDROID_ALLOWANCE || mouseY > width - ANDROID_ALLOWANCE) {
            Log.w(TAG, "Released");
            if(mouseX > left.pos.x && mouseX < left.pos.x + left.w &&
                    mouseY > left.pos.y && mouseY < left.pos.y + left.h) {
                activity.setAction("C");
                left.color = color(0, 0, 255);
                Log.w(TAG, "Left Release");
            }

            if(mouseX > right.pos.x && mouseX < right.pos.x + right.w &&
                    mouseY > right.pos.y && mouseY < right.pos.y + right.h) {
                activity.setAction("D");
                right.color = color(0, 0, 255);
                Log.w(TAG, "Right Release");
            }
        }
    }

    public void mousePressed() {
        if(mouseX < ANDROID_ALLOWANCE || mouseX > width - ANDROID_ALLOWANCE ||
                mouseY < ANDROID_ALLOWANCE || mouseY > width - ANDROID_ALLOWANCE) {
            Log.w(TAG, "Pressed");
            if(mouseX > left.pos.x && mouseX < left.pos.x + left.w &&
                    mouseY > left.pos.y && mouseY < left.pos.y + left.h) {
                if(keysPressed.contains("C")) {
                    keysPressed.remove("C");
                    activity.setAction("D");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                activity.setAction("A");
                left.color = color(0, 0, 255);
                Log.w(TAG, "Left click");
            }

            if(mouseX > right.pos.x && mouseX < right.pos.x + right.w &&
                    mouseY > right.pos.y && mouseY < right.pos.y + right.h) {
                if(keysPressed.contains("C")) {
                    keysPressed.remove("C");
                    activity.setAction("D");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                activity.setAction("B");
                right.color = color(0, 0, 255);
                Log.w(TAG, "Right click");
            }

            if(mouseX > drag.pos.x && mouseX < drag.pos.x + drag.w &&
                    mouseY > drag.pos.y && mouseY < drag.pos.y + drag.h) {
                drag.color = right.color = color(0, 0, 255);;
                holding = !holding;

                if(holding) {
                    if(!keysPressed.contains("C")) {
                        keysPressed.add("C");
                        activity.setAction("C");
                    }
                }else{
                    if(keysPressed.contains("C")) {
                        keysPressed.remove("C");
                        activity.setAction("D");
                    }
                }
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

        public void moveX(int x) {
            position.x = x;
        }

        public void moveY(int y) {
            position.y = y;
        }

        public PVector getPosition(){
            return position;
        }
    }

    public class Scroller {
        private PVector position;
        final int DIAMETER = 100;
        private int colour;

        public int getColor() {
            return colour;
        }

        public Scroller(int y){
            position = new PVector();
            this.position.x = width/2;
            this.position.y = y;
            colour = color(32, 3, 32);
        }

        public void draw(){
            fill(color(23, 32, 54));
            rectMode(CENTER);
            rect(width/2, position.y, width, 10);
            rectMode(CORNER);
            fill(colour);
            ellipse(position.x, position.y, DIAMETER, DIAMETER);
        }

        public PVector getPosition(){
            return position;
        }

        public void setX(int x){
            position.x = x;
        }

        public void setY(int y) {
            position.y = y;
        }
    }
}
