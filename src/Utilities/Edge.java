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
        float[] sCoords = start.getCoords();
        float[] eCoords = end.getCoords();
        float[] vCoords = v.getCoords();

        if (sCoords[0] == eCoords[0])
            return sCoords[0] == vCoords[0];

        float dir = Math.signum(eCoords[0] - sCoords[0]);
        if (Math.signum(vCoords[0] - sCoords[0]) != dir || Math.signum(eCoords[0] - vCoords[0]) != dir)
            return false;

        float intersection = (eCoords[1] - sCoords[1]) * (vCoords[0] - sCoords[0]) / (eCoords[0] - sCoords[0]) + sCoords[1];
        return intersection >= vCoords[1];
    }
}
