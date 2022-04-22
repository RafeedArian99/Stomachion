package Utilities;

public class Vertex {
    private final double[] coords;

    /**
     * Constructs a Vertex object
     *
     * @param x x-coordinate of the vertex
     * @param y y-coordinate of the vertex
     */
    public Vertex(double x, double y) {
        coords = new double[]{x, y};
    }

    /**
     * Constructs a Vertex object
     *
     * @param coords coordinates of the vertex
     */
    public Vertex(double[] coords) {
        this.coords = coords;
    }

    public void setX(double x) {
        coords[0] = x;
    }

    public void setY(double y) {
        coords[1] = y;
    }

    public double getX() {
        return coords[0];
    }

    public double getY() {
        return coords[1];
    }

    public double[] getCoords() {
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
