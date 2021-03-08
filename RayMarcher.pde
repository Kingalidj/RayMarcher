object[] objects = new object[10];
object selected = null;
RayMarch rm;

void setup() {
  size(1200, 1200, P3D);
  background(0);
  rm = new RayMarch(2000);

  for (int i = 0; i < objects.length; i++) {
    if (random(1) > 0.5) {
      objects[i] = new circle(random(width), random(height), random(25, 100));
    } else {
      objects[i] = new rectangle(random(width), random(height), random(50, 200), random(50, 200));
    }
  }
}

void draw() {
  background(0);
  rm.cycle(objects);
  show(objects);
}

class RayMarch {
  PVector origin, rayPos;
  float angle = 0, minDist = 0.01, maxDist = 2 * width;
  float incr = 0.5;
  int steps = 100;

  RayMarch(int res) {
    incr = 360.0 / float(res);
    origin = new PVector(width / 2, height / 2);
    rayPos = origin.copy();
  }

  void cycle(object[] objects) {
    for (float i = 0; i <= 360; i += incr) {
      this.march(objects);
      this.update();
    }
    stroke(0);
    noFill();
    ellipse(origin.x, origin.y, 20, 20);
    angle = 0;
  }

  void move(float x, float y) {
    origin = new PVector(x, y);
  }

  void update() {
    rayPos = origin.copy();
    angle += incr;
  }

  void march(object[] objects) {
    PVector rayStep = PVector.fromAngle(radians(angle));
    for (int i = 0; i < steps; i++) {
      float dist = shortestDist(rayPos, objects);
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

float smoothDist(PVector pos, object [] objects, float k) {
  float h = max(k - abs(objects[0].getDist(pos) - objects[1].getDist(pos)), 0) / k;
  return abs(min(objects[0].getDist(pos), objects[1].getDist(pos)) - h*h*h*k*1/6.0);
}

float shortestDist(PVector pos, object [] objects){
  float dist = objects[0].getDist(pos);
  for (int i = 1; i < objects.length; i++) {
    dist = min(dist, objects[i].getDist(pos));
  }
  return abs(dist);
}

void show(object[] objects) {
  for (int i = 0; i < objects.length; i++) {
    noFill();
    stroke(200, 0, 100);
    strokeWeight(5);
    objects[i].show();
  }
}

void mouseDragged() {
  if (selected != null) {
    selected.move(mouseX, mouseY);
  } else {
    rm.move(mouseX, mouseY);
  }
}

void mousePressed() {
  for (int i = 0; i < objects.length; i++) {
    selected = objects[i].isSelected();
    if (selected != null)break;
  }
}
