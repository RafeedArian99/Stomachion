package Utilities;

public class Vertex {
    public final int x, y;

    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vertex(int[] coords) {
        this.x = coords[0];
        this.y = coords[1];
    }

    public Vertex add(Vertex v) {
        return new Vertex(this.x + v.x, this.y + v.y);
    }

    @Override
    public boolean equals(Object o) {
        assert o instanceof Vertex;
        Vertex v = (Vertex) o;
        return v.x == this.x && v.y == this.y;
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
}
