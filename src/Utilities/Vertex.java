package Utilities;

class Vertex {
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

    public Vertex inverse() {
        return new Vertex(-this.getX(), -this.getY());
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

    /**
     * Adds an offset to this vertex, then returns the result. Does not modify this vertex.
     *
     * @param offset offset vertex to be added
     * @return new vertex that results from the addition of the offset
     */
    public Vertex add(Vertex offset) {
        return new Vertex(this.getX() + offset.getX(), this.getY() + offset.getY());
    }

    /**
     * Rotates this vertex about (0, 0).
     * @param isClockwise whether or not vertex is to be rotated clockwise or counter-clockwise.
     */
    public void rotate(boolean isClockwise) {
        int dir = isClockwise ? -1 : 1;
        double temp = this.getX();

        this.setX(dir * this.getY());
        this.setY(-dir * temp);
    }

    // TODO: Implement
    public void flipVerticallyAbout(double y) {
        this.setY(2 * y - this.getY());
    }

    // TODO: Implement
    public void flipHorizontallyAbout(double x) {
        this.setX(2 * x - this.getX());
    }

    @Override
    public boolean equals(Object o) {
        assert o instanceof Vertex;
        Vertex v = (Vertex) o;
        return v.getX() == this.getX() && v.getY() == this.getY();
    }

    @Override
    public String toString() {
        return "(" + this.coords[0] + "," + this.coords[1] + ")";
    }
}
