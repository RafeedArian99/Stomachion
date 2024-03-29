package Utilities;

class Edge {
    public final Vertex start, end;
    public static final boolean DOWNCAST = false;
    public static final boolean UPCAST = true;

    /**
     * Constructs an Edge object
     *
     * @param start starting vertex
     * @param end   ending vertex
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

    /**
     * Checks if this edge is intersected by another edge.
     *
     * @param other other edge to check intersection with
     * @return true if this edge is intersected by another edge
     */
    public boolean getsIntersectedBy(Edge other, boolean checkCorners) {
        // Simplify all the edge ends
        double a = this.start.getX();
        double b = this.start.getY();
        double c = this.end.getX();
        double d = this.end.getY();
        double e = other.start.getX();
        double f = other.start.getY();
        double g = other.end.getX();
        double h = other.end.getY();

        // For vertical edge
        if (a == c) {
            double pointOfIntersection = (d * (a * (f - h) + e * h - f * g) + b * (c * (h - f) - e * h + f * g)) /
                    ((a - c) * (f - h) + (b - d) * (g - e));

            if (pointOfIntersection == b || pointOfIntersection == d)
                return checkCorners;

            double dir = Math.signum(d - b);
            return Math.signum(pointOfIntersection - b) == dir && Math.signum(d - pointOfIntersection) == dir;
        }
        // For any other edge
        else {
            double pointOfIntersection = (a * (d * e - d * g - e * h + f * g) + b * c * (g - e) + c * e * h - c * f * g) /
                    ((a - c) * (f - h) + b * (g - e) + d * (e - g));

            if (pointOfIntersection == a || pointOfIntersection == c)
                return checkCorners;

            double dir = Math.signum(c - a);
            return Math.signum(pointOfIntersection - a) == dir && Math.signum(c - pointOfIntersection) == dir;
        }
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
        if ((Math.signum(vXVector) != 0 && Math.signum(vXVector) != dir) || Math.signum(end.getX() - v.getX()) != dir) // Math.signum(vXVector) != 0 &&
            return false;

        double yIntersection = (end.getY() - start.getY()) * vXVector / endXVector + start.getY();
        return downcast ? yIntersection >= v.getY() : yIntersection <= v.getY();
    }
}