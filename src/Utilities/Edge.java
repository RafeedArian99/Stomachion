package Utilities;

public class Edge {
    public final Vertex start, end;

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
    protected boolean intersectsWithRayAt(Vertex v) {
        if (start.x == end.x)
            return start.x == v.x;

        float result = (end.y - start.y) * (v.x - start.x) / (end.x - start.x + 1.0f) + start.y;
        return result >= v.y;
    }
}
