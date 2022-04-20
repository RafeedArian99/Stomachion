package Utilities;

public class Vertex {
    public final int x, y;

    /**
     * Constructs a Vertex object
     *
     * @param x x-coordinate of the vertex
     * @param y y-coordinate of the vertex
     */
    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a Vertex object
     *
     * @param coords coordinates of the vertex
     */
    public Vertex(int[] coords) {
        this.x = coords[0];
        this.y = coords[1];
    }

    /**
     * Adds an offset to this vertex, then returns the result. Does not modify this vertex.
     *
     * @param offset offset vertex to be added
     * @return new vertex that results from the addition of the offset
     */
    public Vertex add(Vertex offset) {
        return new Vertex(this.x + offset.x, this.y + offset.y);
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
