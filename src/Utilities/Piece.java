package Utilities;

public class Piece {
    private Vertex globalOffset;
    private final Edge[] localEdges;

    public Piece(int pieceID) {
        int[][][] allVertices = new int[][][] {
                {{0, 0}, {0, 1}, {1, 1}, {1, 0}}
        };

        int[][] vertices = allVertices[pieceID];

        localEdges = new Edge[vertices.length];
        Vertex startVertex = new Vertex(vertices[0]);
        for (int i = 0; i < vertices.length; i++) {
            Vertex endVertex = i == vertices.length - 1 ?  localEdges[0].start : new Vertex(vertices[i + 1]);
            localEdges[i] = new Edge(startVertex, endVertex);
        }
    }

    public boolean encapsulates(Vertex v) {
        int count = 0;

        for (Edge localEdge : this.localEdges) {
            if (localEdge.add(globalOffset).intersectsWithRayAt(v))
                count++;
        }

        return count == 1;
    }

    public boolean collidesWith(Piece p) {
        for (Edge localEdge : localEdges) {
            if (p.encapsulates(localEdge.start)) {
                return true;
            }
        }

        return false;
    }
}
