package Utilities;

/**
 * This class models a piece of the Stomachion puzzle.
 */
public class Piece {
    public static final boolean VERTICAL = true;
    public static final boolean HORIZONTAL = false;
    public static final boolean CLOCKWISE = true;
    public static final boolean COUNTERCLOCKWISE = false;

    private Vertex globalOffset;
    private final Edge[] localEdges;
    private final int pieceID;
    private int rotation;
    private boolean flipped;

    private boolean selected = false;
    private PieceState highlighted = PieceState.NEUTRAL;
    private final int[] color;

    public enum PieceState {
        NEUTRAL, VALID, INVALID
    }

    /**
     * Constructs a Piece object
     *
     * @param pieceID Unique ID for the piece (0-13)
     */
    public Piece(int pieceID, int[] color) {
        this.pieceID = pieceID;
        this.color = color;

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
                {{0, 0}, {0, 12}, {12, 12}, {12, 0}}
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

    /**
     * Get the color assigned to this piece.
     *
     * @return the color assigned to this piece
     */
    public int[] getColor() {
        return this.color;
    }

    /**
     * Sets whether or not piece is "selected"
     *
     * @param state new PieceState of the piece
     */
    public void setSelected(boolean state) {
        this.selected = state;
    }

    /**
     * Checks if the piece is "selected".
     *
     * @return true if the piece is "selected"
     */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * Sets the state of the piece.
     *
     * @param state new state of the current piece
     */
    public void highlight(PieceState state) {
        this.highlighted = state;
    }

    /**
     * Checks whether the piece is "highlighted".
     *
     * @return true if the piece is "highlighted"
     */
    public PieceState getHighlightState() {
        return this.highlighted;
    }

    /**
     * Gets all the coordinated of the piece.
     *
     * @return all the coordinated of the piece
     */
    public double[][] getAllCoords() {
        double[][] allCoords = new double[2][localEdges.length];
        for (int i = 0; i < localEdges.length; i++) {
            Vertex globalVertex = localEdges[i].start.add(globalOffset);
            allCoords[0][i] = globalVertex.getX();
            allCoords[1][i] = globalVertex.getY();
        }

        return allCoords;
    }

    /**
     * Adds to the piece's global offset
     *
     * @param x x offset
     * @param y y offset
     */
    public void addToGlobalOffset(double x, double y) {
        this.globalOffset.setX(this.globalOffset.getX() + x);
        this.globalOffset.setY(this.globalOffset.getY() + y);
    }

    /**
     * Sets the piece's global coordinates
     *
     * @param x x-coordinate of new global offset
     * @param y y-coordinate of new global offset
     */
    public void setGlobalOffset(double x, double y) {
        this.globalOffset.setX(x);
        this.globalOffset.setY(y);
    }

    /**
     * Snaps the global offset to a nearest peg.
     */
    public void snapGlobalOffset() {
        this.globalOffset.setX(Math.round(this.globalOffset.getX()));
        this.globalOffset.setY(Math.round(this.globalOffset.getY()));
    }

    /**
     * Gets the x-coordinate of the global offset.
     *
     * @return the x-coordinate of the global offset
     */
    public double getGlobalX() {
        return this.globalOffset.getX();
    }

    /**
     * Gets the y-coordinate of the global offset.
     *
     * @return the y-coordinate of the global offset
     */
    public double getGlobalY() {
        return this.globalOffset.getY();
    }

    /**
     * Returns true if this piece is colliding with another one.
     *
     * @param other piece whose collision is to be tested
     * @return true if this piece is colliding with another one
     */
    public boolean collidesWith(Piece other) {
        // Check edges for intersection
        for (Edge localEdge : this.localEdges) {
            Edge localEdgeCorrected = localEdge.add(globalOffset);
            for (Edge otherEdge : other.localEdges) {
                Edge otherEdgeCorrected = otherEdge.add(other.globalOffset);
                if (localEdgeCorrected.getsIntersectedBy(otherEdgeCorrected) && otherEdgeCorrected.getsIntersectedBy(localEdgeCorrected))
                    return true;
            }
        }

        // Check vertices for encapsulation
        for (Edge localEdge : this.localEdges) {
            if (other.encapsulates(localEdge.start.add(globalOffset))) {
                return true;
            }
        }
        for (Edge otherEdge : other.localEdges) {
            if (this.encapsulates(otherEdge.start.add(other.globalOffset))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Rotates this piece about a given coordinate in global space.
     *
     * @param x   x-coordinate of rotation point
     * @param y   y-coordinate of rotation point
     * @param dir whether or not piece is to rotate clockwise or counter-clockwise
     */
    public void rotateAbout(double x, double y, boolean dir) {
        Vertex rotationPoint = new Vertex(x, y);

        Vertex adjustedOffset = globalOffset.add(rotationPoint.inverse());
        adjustedOffset.rotate(dir);
        adjustedOffset = adjustedOffset.add(rotationPoint);
        globalOffset.setX(adjustedOffset.getX());
        globalOffset.setY(adjustedOffset.getY());

        for (Edge localEdge : localEdges) {
            localEdge.start.rotate(dir);
        }

        rotation = Math.floorMod(rotation + (dir ? 90 : -90), 360);
    }

    /**
     * Flips this piece vertically about a given y-coordinate
     *
     * @param x   x-coordinate of flip axis
     * @param y   y-coordinate of flip axis
     * @param dir whether or not piece is to be flipped vertically or horizontally
     */
    public void flipAbout(double x, double y, boolean dir) {
        for (Edge localEdge : localEdges) {
            if (dir)
                localEdge.start.setY(-localEdge.start.getY());
            else
                localEdge.start.setX(-localEdge.start.getX());
        }

        if (dir)
            globalOffset.setY(2 * y - globalOffset.getY());
        else
            globalOffset.setX(2 * x - globalOffset.getX());

        flipped = !flipped;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Piece))
            return false;

        Piece other = (Piece) o;
        if (other.localEdges.length != this.localEdges.length)
            return false;
        if (!other.globalOffset.equals(this.globalOffset))
            return false;

        for (int i = 0; i < this.localEdges.length; i++) {
            if (!this.localEdges[i].start.equals(other.localEdges[i].start))
                return false;
        }

        return true;
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

    /**
     * Checks if a point is encapsulated by the piece.
     *
     * @param x x-coordinate of point
     * @param y y-coordinate of point
     * @return true if point is encapsulated by the piece
     */
    public boolean encapsulates(double x, double y) {
        return this.encapsulates(new Vertex(x, y));
    }

    /**
     * Resets the global offset back to global origin
     */
    public void resetGlobalOffset() {
        this.globalOffset = new Vertex(0, 0);
    }

    /* Private functions */

    private boolean encapsulates(Vertex v) {
        int upCount = 0, downCount = 0;

        for (Edge localEdge : this.localEdges) {
            if (localEdge.add(globalOffset).intersectsWithRayFrom(v, Edge.DOWNCAST))
                downCount++;
            if (localEdge.add(globalOffset).intersectsWithRayFrom(v, Edge.UPCAST))
                upCount++;
        }

        return downCount == 1 && upCount == 1;
    }

}