package Utilities;

public class Edge {
    public final Vertex start, end;

    public Edge(Vertex a, Vertex b) {
        this.start = a;
        this.end = b;
    }

    public Edge add(Vertex v) {
        return new Edge(start.add(v), end.add(v));
    }

    protected boolean intersectsWithRayAt(Vertex v) {
        if (start.x == end.x)
            return start.x == v.x;

        float result = (end.y - start.y) * (v.x - start.x) / (end.x - start.x + 1.0f) + start.y;
        return result >= v.y;
    }

    @Override
    public String toString() {
        return "[" + start + "->" + end + "]";
    }
}
