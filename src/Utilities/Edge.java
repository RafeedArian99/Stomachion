package Utilities;

class Edge {
    public final Vertex start, end;
    public static final boolean DOWNCAST = false;
    public static final boolean UPCAST = true;

    /**
     * Constructs an Edge object
     *
     * @param start starting vertex
     * @param end ending vertex
     */
    public Edge(Vertex start, Vertex end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Adds an offset to this edge, then returns the result. Does not modify this edge.
     *
     * @param offset offset to be added
     * @return new edge that results from the addition of the offset
     */
    public Edge add(Vertex offset) {
        return new Edge(start.add(offset), end.add(offset));
    }

    @Override
    public String toString() {
        return "[" + start + "->" + end + "]";
    }

    /* Private and protected methods */

    // Checks to see if the edge intersects with a downwards-pointing ray originating at a specified vertex
    protected boolean intersectsWithRayFrom(Vertex v, boolean downcast) {
        if (start.getX() == end.getX() && start.getX() == v.getX()) {
            double dir = Math.signum(end.getY() - start.getY());
            return Math.signum(end.getY() - v.getY()) == dir && Math.signum(v.getY() - start.getY()) == dir;
        }

        double endXVector = end.getX() - start.getX();
        double vXVector = v.getX() - start.getX();
        double dir = Math.signum(endXVector);
        if ((Math.signum(vXVector) != 0 && Math.signum(vXVector) != dir) || Math.signum(end.getX() - v.getX()) != dir)
            return false;

        double yIntersection = (end.getY() - start.getY()) * vXVector / endXVector + start.getY();
        return downcast ? yIntersection >= v.getY() : yIntersection <= v.getY();
    }
}