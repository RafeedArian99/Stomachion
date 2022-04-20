package Utilities;

public class Piece {
    private Vertex globalOffset;
    private final Edge[] localEdges;
    private final int pieceID;
    private int rotation;

    /**
     * Constructs a Piece object
     *
     * @param pieceID Unique ID for the piece (0-13)
     */
    public Piece(int pieceID) {
        this.pieceID = pieceID;
        this.rotation = 0;

        float[][][] allVertices = new float[][][]{
                {{0, 0}, {0, 1}, {1, 1}, {1, 0}}
        };

        float[][] vertices = allVertices[pieceID];

        localEdges = new Edge[vertices.length];
        Vertex startVertex = new Vertex(vertices[0]);
        for (int i = 0; i < vertices.length; i++) {
            Vertex endVertex = i == vertices.length - 1 ? localEdges[0].start : new Vertex(vertices[i + 1]);
            localEdges[i] = new Edge(startVertex, endVertex);
            startVertex = endVertex;
        }

        this.globalOffset = new Vertex(0, 0);
    }

    public void setGlobalOffset(float x, float y) {
        this.globalOffset = new Vertex(x, y);
    }

    /**
     * Returns true if this piece encapsulates the specified point.
     *
     * @param v point whose encapsulation is to be tested
     * @return true if the piece encapsulates the specified point
     */
    public boolean encapsulates(Vertex v) {
        int count = 0;

        for (Edge localEdge : this.localEdges) {
            if (localEdge.add(globalOffset).intersectsWithRayAt(v))
                count++;
        }

        System.out.println(count);
        return count == 1;
    }

    /**
     * Returns true if this piece is colliding with another one.
     *
     * @param p piece whose collision is to be tested
     * @return true if this piece is colliding with another one
     */
    public boolean collidesWith(Piece p) {
        for (Edge localEdge : localEdges) {
            if (p.encapsulates(localEdge.start)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        String rep = "{PieceID=" + pieceID + ",GlobalOffset=" + globalOffset + ",Rotation=" + rotation + ",LocalEdges={";
        int i = 0;
        for (Edge localEdge : localEdges)
            rep += localEdge + (i++ == localEdges.length - 1 ? "" : ",");
        rep += "}}";

        return rep;
    }
}
