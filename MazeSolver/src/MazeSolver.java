import javalib.impworld.*;

// Represents a maze solver, which is used to solve a randomly generated maze
class MazeSolver extends World {
  // the maze
  private Maze maze;
  // is the maze being constructed (are walls being knocked down still?)
  private boolean underConstruction;
  // viewMode of the maze; regular, or showing a red-blue gradient (0 is normal,
  // 1 and 2 are the red-blue)
  private int viewMode;
  // how is the maze being solved?
  private int solveMode;
  // ----------- these fields are stored, in order to be able to reset the maze
  // the maze width
  private int width;
  // the maze height
  private int height;
  // the preference of the maze; 0 is normal, 1 is prefer vertical,
  // - 1 is prefer horizontal
  private int preference;
  // size of the cell
  private int cellSize;
  // -------------------
  // is the maze solved?
  private boolean isSolved;
  // this boolean is used in the case of restarting the maze; it is used so that
  // you keep the construction process the same (if you don't animate, never
  // animate,
  // if you do, always animate it
  private final boolean skipConstruction;

  // create a MazeSolver from a <width>, <height>, <cellSize>,
  // <preference>, and construction preference boolean
  MazeSolver(int width, int height, int cellSize, int preference, boolean skipConstruction) {
    if (width == 1 && height == 1) {
      throw new IllegalArgumentException("Cannot have a 1x1 maze");
    }
    this.maze = new Maze(width, height, cellSize, preference, skipConstruction);
    this.skipConstruction = skipConstruction;
    this.width = width;
    this.height = height;
    this.preference = preference;
    this.underConstruction = !skipConstruction;
    this.viewMode = 0;
    this.cellSize = cellSize;
  }

  // based on the mode of this mazeSolver, advance the maze world (continue to
  // construct it, or
  // search through the maze using breadth/ depth first search, or else manually
  // traverse
  public void onTick() {
    if (underConstruction) {
      this.underConstruction = this.maze.constructMaze();
    }
    else if (!this.isSolved && (this.solveMode == 1 || this.solveMode == 2)) {
      this.isSolved = this.maze.findPath();
    }
    else if (!this.isSolved && this.solveMode == 3) {
      this.isSolved = this.maze.solvedManually();
    }
  }

  // make the scene that represents the world
  public WorldScene makeScene() {
    return this.maze.observeMaze(this.viewMode);
  }

  // based on a <key> input, adjust some part of the maze (restart it,
  // change view modes, search through it, or manually traverse it)
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      this.maze = new Maze(this.width, this.height, this.cellSize, this.preference,
          this.skipConstruction);
      this.viewMode = 0;
      this.solveMode = 0;
      this.underConstruction = true;
      this.isSolved = false;
    }
    if (!this.underConstruction) {
      if (key.equals("s")) {
        this.viewMode = 1;
        this.maze.calculateLongestPath(true);
      }
      else if (key.equals("e")) {
        this.viewMode = -1;
        this.maze.calculateLongestPath(false);
      }
      else if (key.equals("n")) {
        this.viewMode = 0;
      }
      else if (key.equals("d") && this.solveMode == 0) {
        this.solveMode = 1;
        this.maze.initializeSearch(true);
      }
      else if (key.equals("b") && this.solveMode == 0) {
        this.solveMode = 2;
        this.maze.initializeSearch(false);
      }
      else if (key.equals("m") && this.solveMode == 0) {
        this.solveMode = 3;
      }
      else if ((key.equals("up") || key.equals("left") || key.equals("down") || key.equals("right"))
          && this.solveMode == 3 && !this.isSolved) {
        this.maze.moveManually(key);
      }
    }
  }

  // get the image height of this maze world
  public int worldHeight() {
    return this.cellSize * this.maze.mazeHeight();
  }

  // get the image width of this maze world
  public int worldWidth() {
    return this.cellSize * this.maze.mazeWidth();
  }

  // returns the mode of solving the world
  // Used for testing
  public int getSolveMode() {
    return this.solveMode;
  }

  // returns the view mode of the world
  // Used for testing
  public int getViewMode() {
    return this.viewMode;
  }

  // checks whether the maze has been solved yet
  // Used for testing
  public boolean solvedYet() {
    return this.isSolved;
  }

  // checks whether the maze is still being built
  // Used for testing
  public boolean stillConstructing() {
    return this.underConstruction;
  }

}