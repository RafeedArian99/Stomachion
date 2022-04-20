package Utilities;

public class Vertex {
    private float[] coords;
    public static final Object RETAIN = null;

    /**
     * Constructs a Vertex object
     *
     * @param x x-coordinate of the vertex
     * @param y y-coordinate of the vertex
     */
    public Vertex(float x, float y) {
        coords = new float[] {x, y};
    }

    /**
     * Constructs a Vertex object
     *
     * @param coords coordinates of the vertex
     */
    public Vertex(float[] coords) {
        this.coords = coords;
    }

    public void setCoords(Float x, Float y) {
        if (x != RETAIN)
            coords[0] = x;
        if (y != RETAIN)
            coords[1] = y;
    }

    public float[] getCoords() {
        return this.coords;
    }

    /**
     * Adds an offset to this vertex, then returns the result. Does not modify this vertex.
     *
     * @param offset offset vertex to be added
     * @return new vertex that results from the addition of the offset
     */
    public Vertex add(Vertex offset) {
        return new Vertex(this.coords[0] + offset.coords[0], this.coords[1] + offset.coords[1]);
    }

    @Override
    public boolean equals(Object o) {
        assert o instanceof Vertex;
        Vertex v = (Vertex) o;
        return v.coords[0] == this.coords[0] && v.coords[1] == this.coords[1];
    }

    @Override
    public String toString() {
        return "(" + this.coords[0] + "," + this.coords[1] + ")";
    }
}
