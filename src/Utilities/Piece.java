package Utilities;

import javafx.scene.paint.Color;

public class Piece {
    public static final boolean VERTICAL_FLIP = true;
    public static final boolean HORIZONTAL_FLIP = false;
    public static final boolean CLOCKWISE = true;
    public static final boolean COUNTER_CLOCKWISE = false;

    private Vertex globalOffset;
    private final Edge[] localEdges;
    private final int pieceID;
    private int rotation;
    private boolean flipped;

    private boolean selected = false;
    private PieceState highlighted = PieceState.NEUTRAL;
    private Color color;

    public enum PieceState {
        NEUTRAL, VALID, INVALID
    }

    /**
     * Constructs a Piece object
     *
     * @param pieceID Unique ID for the piece (0-13)
     */
    public Piece(int pieceID, Color color) {
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

    public Color getColor() {
        return this.color;
    }

    public void setSelected(boolean state) {
        this.selected = state;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void highlight(PieceState state) {
        this.highlighted = state;
    }

    public PieceState getHighlightState() {
        return this.highlighted;
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

    public void setGlobalOffset(double x, double y) {
        this.globalOffset.setX(x);
        this.globalOffset.setY(y);
    }

    public void snapGlobalOffset() {
        this.globalOffset.setX(Math.round(this.globalOffset.getX()));
        this.globalOffset.setY(Math.round(this.globalOffset.getY()));
    }

    public double getGlobalX() {
        return this.globalOffset.getX();
    }

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
    	
    	
    	
    	// FIX THIS: IT IS CURRENTLY PRETTY BASIC AND ONLY CHECKS VERTICES RATHER THAN EDGES
    	
    	
    	
        for (Edge localEdge : localEdges) {
        	//System.out.println("khehc");
            if (other.encapsulates(localEdge.start)) {
                return true;
            }
        }
        for (Edge otherEdge : other.getEdges()) {
        	//System.out.println("check");
            if (this.encapsulates(otherEdge.start)) {
                return true;
            }
        }
        return false;
    }
    
    public Edge[] getEdges() {
    	return localEdges;
    }

    /**
     * Rotates this piece about a given coordinate in global space.
     *
     * @param x           x-coordinate of rotation point
     * @param y           y-coordinate of rotation point
     * @param isClockwise whether or not piece is to rotate clockwise or counter-clockwise
     */
    public void rotateAbout(double x, double y, boolean isClockwise) {
        Vertex rotationPoint = new Vertex(x, y);

        Vertex adjustedOffset = globalOffset.add(rotationPoint.inverse());
        adjustedOffset.rotate(isClockwise);
        adjustedOffset = adjustedOffset.add(rotationPoint);
        globalOffset.setX(adjustedOffset.getX());
        globalOffset.setY(adjustedOffset.getY());

        for (Edge localEdge : localEdges) {
            localEdge.start.rotate(isClockwise);
        }

        rotation = Math.floorMod(rotation + (isClockwise ? 90 : -90), 360);
    }

    /**
     * Flips this piece vertically about a given y-coordinate
     *
     * @param x          x-coordinate of flip axis
     * @param y          y-coordinate of flip axis
     * @param isVertical whether or not piece is to be flipped vertically or horizontally
     */
    public void flipAbout(double x, double y, boolean isVertical) {
        for (Edge localEdge : localEdges) {
            if (isVertical)
                localEdge.start.setY(-localEdge.start.getY());
            else
                localEdge.start.setX(-localEdge.start.getX());
        }

        if (isVertical)
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

    public boolean encapsulates(double x, double y) {
        return this.encapsulates(new Vertex(x, y));
    }

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
