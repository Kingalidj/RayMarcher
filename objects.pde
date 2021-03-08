class object {
    PVector pos;

    float getDist(PVector point) {
        return 0;
    }
    
    void show() {
    }

    void move(float x, float y) {
        pos = new PVector(x, y);
    }

    object isSelected() {
        return null;
    }
}

class circle extends object {
    float radius;

    circle(float x, float y, float r) {
        pos = new PVector(x, y);
        radius = r;
    }

    float getDist(PVector point) {
        return dist(point.x, point.y, pos.x, pos.y) - radius;
    }

    void show() {
        noFill();
        ellipse(pos.x, pos.y, radius * 2, radius * 2);
    }

    circle isSelected() {
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

    float getDist(PVector point) {
        float dx = max(abs(point.x - pos.x) - width / 2, 0);
        float dy = max(abs(point.y - pos.y) - height / 2, 0);
        float dist = sqrt(dx * dx + dy * dy);
        if (dist > 0)return dist;
        dist = max(abs(point.x - pos.x) - width / 2, abs(point.y - pos.y) - height / 2);
        return dist;
    }

    rectangle isSelected() {
        float dx = max(abs(mouseX - pos.x) - width / 2, 0);
        float dy = max(abs(mouseY - pos.y) - height / 2, 0);
        float dist = sqrt(dx * dx + dy * dy);
        if (dist <= 0)return this;
        return null;
    }

    void show() {
        rectMode(CENTER);
        rect(pos.x, pos.y, width, height);
    }
}
