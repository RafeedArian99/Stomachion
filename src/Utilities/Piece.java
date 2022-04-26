package Utilities;

public class Piece {
    private final Vertex globalOffset;
    private final Edge[] localEdges;
    private final int pieceID;
    private int rotation;
    private boolean flipped;

    /**
     * Constructs a Piece object
     *
     * @param pieceID Unique ID for the piece (0-13)
     */
    public Piece(int pieceID) {
        this.pieceID = pieceID;
        this.rotation = 0;

        double[][][] allVertices = new double[][][]{
                {{0, 0}, {3, 6}, {6, 4}, {6, 0}},           // ( 0) Opera House (24)
                {{0, 0}, {0, 6}, {1, 8}, {3, 6}, {3, 0}},   // ( 1) House (21)
                {{0, 0}, {0, 12}, {2, 10}},                 // ( 2) Big Icicle (12)
                {{0, 0}, {2, 10}, {4, 8}},                  // ( 3) Big Dagger (12)
                {{0, 0}, {-2, 2}, {0, 3}, {6, 0}},          // ( 4) Kite (12)
                {{0, 0}, {-4, 4}, {2, 4}},                  // ( 5) High Tent 1 (12)
                {{0, 0}, {-4, 4}, {2, 4}},                  // ( 6) High Tent 2 (12)
                {{0, 0}, {3, 6}, {3, 0}},                   // ( 7) Big Ramp (9)
                {{0, 0}, {2, 4}, {3, 0}},                   // ( 8) Medium Tent (6)
                {{0, 0}, {-2, 2}, {4, 2}},                  // ( 9) Low Tent 1 (6)
                {{0, 0}, {0, 6}, {2, 4}},                   // (10) Low Tent 2 (6)
                {{0, 0}, {-6, 3}, {-4, 4}},                 // (11) Small Dagger (6)
                {{0, 0}, {-1, 4}, {0, 6}},                  // (12) Small Icicle (3)
                {{0, 0}, {-3, 2}, {0, 2}},                   // (13) Small Ramp (3)
        };

        double[][] vertices = allVertices[pieceID];

        localEdges = new Edge[vertices.length];
        Vertex startVertex = new Vertex(vertices[0]);
        for (int i = 0; i < vertices.length; i++) {
            Vertex endVertex = i == vertices.length - 1 ? localEdges[0].start : new Vertex(vertices[i + 1]);
            localEdges[i] = new Edge(startVertex, endVertex);
            startVertex = endVertex;
        }

        this.globalOffset = new Vertex(0, 0);
    }

    public double[][] getAllCoords(double cellSize) {
        double[][] allCoords = new double[2][localEdges.length];
        for (int i = 0; i < localEdges.length; i++) {
            Vertex globalVertex = localEdges[i].start.add(globalOffset);
            allCoords[0][i] = globalVertex.getX() * cellSize;
            allCoords[1][i] = globalVertex.getY() * cellSize;
        }

        return allCoords;
    }

    public void addToGlobalOffset(double x, double y) {
        this.globalOffset.setX(this.globalOffset.getX() + x);
        this.globalOffset.setY(this.globalOffset.getY() + y);
    }

    /**
     * Returns true if this piece is colliding with another one.
     *
     * @param other piece whose collision is to be tested
     * @return true if this piece is colliding with another one
     */
    public boolean collidesWith(Piece other) {
        for (Edge localEdge : localEdges) {
            if (other.encapsulates(localEdge.start)) {
                return true;
            }
        }

        return false;
    }

    // TODO: Implement correctly
    public void rotate(double x, double y, boolean isClockwise) {
        Vertex rotationPoint = new Vertex(x, y);

        Vertex adjustedOffset = globalOffset.add(rotationPoint.inverse());
        adjustedOffset.rotate(isClockwise);
        adjustedOffset = adjustedOffset.add(rotationPoint);

        for (Edge localEdge : localEdges) {
            Vertex adjustedVertex = localEdge.start.add(globalOffset).add(rotationPoint.inverse());
            adjustedVertex.rotate(isClockwise);
            adjustedVertex = adjustedVertex.add(rotationPoint).add(adjustedOffset.inverse());

            localEdge.start.setX(adjustedVertex.getX());
            localEdge.start.setY(adjustedVertex.getY());
        }

        globalOffset.setX(adjustedOffset.getX());
        globalOffset.setY(adjustedOffset.getY());
    }

    public void rotateCounterClockwiseAbout(double x, double y) {

    }

    // TODO: Implement
    public void flipVerticallyAbout(double x, double y) {
        flipped = !flipped;
    }

    // TODO: Implement
    public void flipHorizontallyAbout(double x, double y) {
        flipped = !flipped;
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

    public boolean encapsulates(double x, double y) {
        return this.encapsulates(new Vertex(x, y));
    }

    /* Private functions */

    private boolean encapsulates(Vertex v) {
        int count = 0;

        for (Edge localEdge : this.localEdges) {
            if (localEdge.add(globalOffset).intersectsWithRayAt(v))
                count++;
        }

        return count == 1;
    }
}
