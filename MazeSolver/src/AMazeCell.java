import java.util.*;
import javalib.worldimages.*;
import java.awt.Color;

// Represents a maze cell, can be a standard maze cell, a start cell, an end cell,
// or a wall. Also includes a cell size for rendering
abstract class AMazeCell {
  protected AMazeCell up;
  protected AMazeCell down;
  protected AMazeCell left;
  protected AMazeCell right;
  protected final int cellSize;
  protected int solveState;
  // These two are public so that we can place the cell in the correct
  // spot on the scene when rendering it. They are final because a cell
  // should never change it's coords
  final int xCoord;
  final int yCoord;

  private int startHeat;
  private int endHeat;

  // Constructs an AMazeCell with a given up, down, left and right neighbor,
  // as well as a cell size. Mainly used for constructing walls
  public AMazeCell(AMazeCell up, AMazeCell down, AMazeCell left, AMazeCell right, int cellSize) {
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;
    this.cellSize = cellSize;
    this.solveState = 0;
    this.xCoord = 0;
    this.yCoord = 0;
  }

  // Makes a default maze cell with 4 walls as neighbors
  AMazeCell(int cellSize, int xCoord, int yCoord) {
    this.up = new Wall(cellSize);
    this.down = new Wall(cellSize);
    this.left = new Wall(cellSize);
    this.right = new Wall(cellSize);
    this.cellSize = cellSize;
    this.solveState = 0;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
  }

  // Given a cell and a direction, either vertical or horizontal, we set the
  // neighbors of each cell to each other. Cells will only be connected via edges
  // so the directions will only be down or right for the current cell
  void updateCell(AMazeCell other, boolean isVertical) {
    if (isVertical) {
      this.down = other;
      other.up = this;
    }
    else {
      this.right = other;
      other.left = this;
    }
  }

  // Draws the cell with a specific color based on the type of cell. Used in
  // drawing the maze row by row
  abstract WorldImage drawCell();

  // Recurses through the maze and updates the distance from the start or end
  // depending on the previous direction, so that the method does not go backwards
  public void updateHeats(String prevDir, int change, boolean isFromStart) {
    if (!prevDir.equals("Up") && !prevDir.equals("Down") && !prevDir.equals("Left")
        && !prevDir.equals("Right") && !prevDir.equals("Start")) {
      throw new IllegalArgumentException("A non start or end cell has to come from somewhere");
    }

    if (!prevDir.equalsIgnoreCase("Down")) {
      this.up.updateHeats("Up", change + 1, isFromStart);
    }

    if (!prevDir.equalsIgnoreCase("Right")) {
      this.left.updateHeats("Left", change + 1, isFromStart);
    }

    if (!prevDir.equalsIgnoreCase("Left")) {
      this.right.updateHeats("Right", change + 1, isFromStart);
    }

    if (!prevDir.equalsIgnoreCase("Up")) {
      this.down.updateHeats("Down", change + 1, isFromStart);
    }

    if (isFromStart) {
      this.startHeat = change;
    }
    else {
      this.endHeat = change;
    }
  }

  // observe the length of the longest path from this cell without going backwards
  int longestPath(String dir) {
    if (!dir.equals("Up") && !dir.equals("Down") && !dir.equals("Left") && !dir.equals("Right")
        && !dir.equals("Start")) {
      throw new IllegalArgumentException("A non start or end cell has to come from somewhere");
    }
    int max = 0;
    if (!dir.equalsIgnoreCase("Down")) {
      max = Math.max(max, 1 + this.up.longestPath("Up"));
    }

    if (!dir.equalsIgnoreCase("Right")) {
      max = Math.max(max, 1 + this.left.longestPath("Left"));
    }

    if (!dir.equalsIgnoreCase("Left")) {
      max = Math.max(max, 1 + this.right.longestPath("Right"));
    }

    if (!dir.equalsIgnoreCase("Up")) {
      max = Math.max(max, 1 + this.down.longestPath("Down"));
    }

    return max;
  }

  // Draws the walls of a cell based on its which neighbors are walls. Used for
  // drawing the maze row by row
  protected WorldImage drawWall(Color color) {
    WorldImage walls = new RectangleImage(this.cellSize, this.cellSize, OutlineMode.OUTLINE, color);
    WorldImage newWall;
    if (this.up.up == null) {
      newWall = new LineImage(new Posn(this.cellSize, 0), Color.DARK_GRAY);
      walls = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.TOP, newWall, 0, 0, walls);
    }
    if (this.down.down == null) {
      newWall = new LineImage(new Posn(this.cellSize, 0), Color.DARK_GRAY);
      walls = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, newWall, 0, 0, walls);
    }
    if (this.left.left == null) {
      newWall = new LineImage(new Posn(0, this.cellSize), Color.DARK_GRAY);
      walls = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.MIDDLE, newWall, 0, 0, walls);
    }
    if (this.right.right == null) {
      newWall = new LineImage(new Posn(0, this.cellSize), Color.DARK_GRAY);
      walls = new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.MIDDLE, newWall, 0, 0, walls);
    }

    return walls;
  }

  // Draws the maze cell with the correct color
  WorldImage drawHeatCell(boolean fromStart, int longestPath) {
    Color color;
    int delta;
    if (fromStart) {
      delta = (int) (((1.0 * this.startHeat) / longestPath) * 255);
    }
    else {
      delta = (int) (((1.0 * this.endHeat) / longestPath) * 255);
    }
    color = new Color(255 - delta, 0, delta);
    WorldImage cellDrawing = new RectangleImage(this.cellSize, this.cellSize, OutlineMode.SOLID,
        color);
    cellDrawing = new OverlayImage(this.drawWall(color), cellDrawing);
    return cellDrawing;
  }

  // Counts the number of neighbors a cell has connections to.
  // Used for testing
  public int countNeighbors() {
    int count = 0;
    if (!(this.up instanceof Wall)) {
      count += 1;
    }
    if (!(this.down instanceof Wall)) {
      count += 1;
    }
    if (!(this.left instanceof Wall)) {
      count += 1;
    }
    if (!(this.right instanceof Wall)) {
      count += 1;
    }
    return count;
  }

  // Returns the neighbors of a cell in a list, used for finding
  // a path through the maze
  public ArrayList<AMazeCell> getNeighbors() {
    ArrayList<AMazeCell> neighbors = new ArrayList<AMazeCell>();
    if (!(this.right instanceof Wall)) {
      neighbors.add(right);
    }
    if (!(this.down instanceof Wall)) {
      neighbors.add(this.down);
    }
    if (!(this.left instanceof Wall)) {
      neighbors.add(this.left);
    }
    if (!(this.up instanceof Wall)) {
      neighbors.add(this.up);
    }
    return neighbors;
  }

  // Changes the solved state based on whether or not the cell
  // is part of the correct path
  public void wasChecked(boolean isCorrect) {
    if (!isCorrect) {
      this.solveState = 1;
    }
    else {
      this.solveState = 2;
    }
  }

  // Returns the neighbor in a given certain direction. If the
  // requested neighbor is a wall, returns the current cell
  public AMazeCell move(String dir) {
    AMazeCell moveTo = this;
    this.solveState = 1;

    if (dir.equals("up") && !(this.up instanceof Wall)) {
      moveTo = this.up;
    }
    else if (dir.equals("left") && !(this.left instanceof Wall)) {
      moveTo = this.left;
    }
    else if (dir.equals("down") && !(this.down instanceof Wall)) {
      moveTo = this.down;
    }
    else if (dir.equals("right") && !(this.right instanceof Wall)) {
      moveTo = this.right;
    }

    moveTo.solveState = 3;
    return moveTo;
  }

  // ---------- methods for testing
  // check if the two cells are connected, given if they are vertically to
  // be checked or horizontally
  boolean connectCell(AMazeCell other, boolean isVertical) {
    return (isVertical && (this.up.equals(other) && other.down.equals(this))
        || (this.down.equals(other) && other.up.equals(this)))
        || ((this.left.equals(other) && other.right.equals(this))
            || (this.right.equals(other) && other.left.equals(this)));

  }
}

// Represents the start of a maze
class StartCell extends AMazeCell {

  // Constructs a start cell with a given size and four wall neighbors
  public StartCell(int cellSize, int xCoord, int yCoord) {
    super(cellSize, xCoord, yCoord);
  }

  // Draws a green StartCell at the current cell size
  WorldImage drawCell() {
    Color color;
    if (this.solveState == 2) {
      color = Color.BLUE;
    }
    else if (this.solveState == 3) {
      color = Color.RED;
    }
    else {
      color = Color.GREEN;
    }
    WorldImage cellDrawing = new RectangleImage(this.cellSize, this.cellSize, OutlineMode.SOLID,
        color);
    cellDrawing = new OverlayImage(this.drawWall(color), cellDrawing);
    return cellDrawing;
  }
}

// Represents the end cell of a maze
class EndCell extends AMazeCell {

  // Instantiates an End cell with 4 walls at a given size
  public EndCell(int cellSize, int xCoord, int yCoord) {
    super(cellSize, xCoord, yCoord);
  }

  // Draws a purple end cell with any walls on top
  WorldImage drawCell() {
    Color color;
    if (this.solveState == 2) {
      color = Color.BLUE;
    }
    else if (this.solveState == 3) {
      color = Color.RED;
    }
    else {
      color = Color.MAGENTA;
    }
    WorldImage cellDrawing = new RectangleImage(this.cellSize, this.cellSize, OutlineMode.SOLID,
        color);
    cellDrawing = new OverlayImage(this.drawWall(color), cellDrawing);
    return cellDrawing;
  }
}

// A generic cell of a maze
class MazeCell extends AMazeCell {

  // Instantiates a MazeCell with a 4 wall neighbors
  public MazeCell(int cellSize, int xCoord, int yCoord) {
    super(cellSize, xCoord, yCoord);
  }

  // Draws the maze cell with the correct color
  WorldImage drawCell() {
    Color color;
    if (this.solveState == 2) {
      color = Color.BLUE;
    }
    else if (this.solveState == 1) {
      color = Color.CYAN;
    }
    else if (this.solveState == 3) {
      color = Color.RED;
    }
    else {
      color = Color.LIGHT_GRAY;
    }
    WorldImage cellDrawing = new RectangleImage(this.cellSize, this.cellSize, OutlineMode.SOLID,
        color);
    cellDrawing = new OverlayImage(this.drawWall(color), cellDrawing);
    return cellDrawing;
  }
}

// Represents a Wall in the maze, basically the lack of a neighbor
class Wall extends AMazeCell {

  // Instantiates a wall without neighbors and a given cell size
  public Wall(int cellSize) {
    super(null, null, null, null, cellSize);
  }

  // Throws an exception because we don't draw walls in row by row method
  WorldImage drawCell() {
    throw new IllegalStateException("Should not call to draw a wall cell");
  }

  // throws an error because you cannot draw walls on a wall
  WorldImage drawWalls() {
    throw new IllegalStateException("Should not call to draw a wall cell's walls");
  }

  // observe the length of the longest path from this cell
  int longestPath(String dir) {
    return 0;
  }

  public void updateHeats(String prevDir, int change, boolean isFromStart) {
    // do literally nothing
  }
}