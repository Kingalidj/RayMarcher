import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class RayMarcher extends PApplet {

object[] objects = new object[10];
object selected = null;
RayMarch rm;

public void setup() {
    
    background(0);
    rm = new RayMarch(2000);

    for (int i = 0; i < objects.length; i++) {
        if (random(1) > 0.5f) {
            objects[i] = new circle(random(width), random(height), random(25, 100));
        } else {
            objects[i] = new rectangle(random(width), random(height), random(50, 200), random(50, 200));
        }
    }
}

public void draw() {
    background(0);
    rm.cycle(objects);
    show(objects);
}

class RayMarch {
    PVector origin, rayPos;
    float angle = 0, minDist = 0.01f, maxDist = 2 * width;
    float incr = 0.5f;
    int steps = 100;

    RayMarch(int res) {
        incr = 360.0f / PApplet.parseFloat(res);
        origin = new PVector(width / 2, height / 2);
        rayPos = origin.copy();
    }

    public void cycle(object[] objects) {
        for (float i = 0; i <= 360; i += incr) {
            this.march(objects);
            this.update();
        }
        stroke(0);
        noFill();
        ellipse(origin.x, origin.y, 20, 20);
        angle = 0;
    }

    public void move(float x, float y) {
        origin = new PVector(x, y);
    }

    public void update() {
        rayPos = origin.copy();
        angle += incr;
    }

    public void march(object[] objects) {
        PVector rayStep = PVector.fromAngle(radians(angle));
        for (int i = 0; i < steps; i++) {
            float dist = smoothDist(rayPos, objects, 100);
            if (abs(dist) < minDist || abs(dist) > maxDist) {
                fill(255);
                noStroke();
                //ellipse(rayPos.x, rayPos.y, 5, 5);
                break;
            }
            rayStep.setMag(1);
            rayStep.mult(dist);
            //noFill();
            //stroke(255 / (i + 1));
            stroke(255);
            strokeWeight(3);
            line(rayPos.x, rayPos.y, rayPos.x + rayStep.x, rayPos.y + rayStep.y);
            //ellipse(rayPos.x, rayPos.y, dist * 2, dist * 2);
            rayPos.add(rayStep);
        }
    }
}

public float smoothDist(PVector pos, object [] objects, float k) {
    float h = max(k - abs(objects[0].getDist(pos) - objects[1].getDist(pos)), 0) / k;
    return abs(min(objects[0].getDist(pos), objects[1].getDist(pos)) - h*h*h*k*1/6.0f);
}

public float shortestDist(PVector pos, object [] objects){
    float dist = objects[0].getDist(pos);
    for (int i = 1; i < objects.length; i++) {
        dist = min(dist, objects[i].getDist(pos));
    }
    return abs(dist);
}

public void show(object[] objects) {
    for (int i = 0; i < objects.length; i++) {
        noFill();
        stroke(200, 0, 100);
        strokeWeight(5);
        objects[i].show();
    }
}

public void mouseDragged() {
    if (selected != null) {
        selected.move(mouseX, mouseY);
    } else {
        rm.move(mouseX, mouseY);
    }
}

public void mousePressed() {
    for (int i = 0; i < objects.length; i++) {
        selected = objects[i].isSelected();
        if (selected != null)break;
    }
}
class object {
    PVector pos;

    public float getDist(PVector point) {
        return 0;
    }
    
    public void show() {
    }

    public void move(float x, float y) {
        pos = new PVector(x, y);
    }

    public object isSelected() {
        return null;
    }
}

class circle extends object {
    float radius;

    circle(float x, float y, float r) {
        pos = new PVector(x, y);
        radius = r;
    }

    public float getDist(PVector point) {
        return dist(point.x, point.y, pos.x, pos.y) - radius;
    }

    public void show() {
        noFill();
        ellipse(pos.x, pos.y, radius * 2, radius * 2);
    }

    public circle isSelected() {
        if (dist(mouseX, mouseY, pos.x, pos.y) <= radius)return this;
        return null;
    }
}

class rectangle extends object {
    float width, height;

    rectangle(float x, float y, float w, float h){
        width = w;
        height = h;
        pos = new PVector(x, y);
    }

    public float getDist(PVector point) {
        float dx = max(abs(point.x - pos.x) - width / 2, 0);
        float dy = max(abs(point.y - pos.y) - height / 2, 0);
        float dist = sqrt(dx * dx + dy * dy);
        if (dist > 0)return dist;
        dist = max(abs(point.x - pos.x) - width / 2, abs(point.y - pos.y) - height / 2);
        return dist;
    }

    public rectangle isSelected() {
        float dx = max(abs(mouseX - pos.x) - width / 2, 0);
        float dy = max(abs(mouseY - pos.y) - height / 2, 0);
        float dist = sqrt(dx * dx + dy * dy);
        if (dist <= 0)return this;
        return null;
    }

    public void show() {
        rectMode(CENTER);
        rect(pos.x, pos.y, width, height);
    }
}
  public void settings() {  size(1200, 1200, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--hide-stop", "RayMarcher" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
