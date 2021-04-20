import java.util.*;
import javalib.impworld.WorldScene;

// represents a Maze structure
class Maze {

  // the first cell of the maze, the start
  private StartCell start;

  // the last cell of the maze, the end
  private EndCell end;

  // an ArrayList of ArrayLists of Maze cells, used for
  // help of drawing in progress mazes/ simple mazes
  private ArrayList<ArrayList<AMazeCell>> maze;

  // HashMap of references for each node -> references
  // are used to help with the "blob" concept discussed in class
  private HashMap<AMazeCell, AMazeCell> references;

  // Edges used in the Maze
  private final ArrayList<Edge> edges;

  // iterator of the edges, used for construction
  private Iterator<Edge> edgeIterator;

  // cellSize of the maze
  private final int cellSize;

  // a worklist collection; used during the initial creation of the maze
  private ICollection<AMazeCell> workList;

  // ArrayDeque of alreadySeen items; used to make sure that during searching/
  // traversal, cells that have already been seen to do not get used/ looked
  // at multiple times
  private ArrayDeque<AMazeCell> alreadySeen;

  // The current cell you are at for manual traversal
  private AMazeCell currentManual;

  // Stores the current view of the maze for optimized rendering
  private WorldScene currentMazeScene;

  // Stores the heat map from the start of the maze for optimized rendering
  private WorldScene heatMapFromStart;

  // Stores the heat map from the end of the maze for optimized rendering
  private WorldScene heatMapFromEnd;

  // Keeps track of whether or not the heat maps have been generated yet
  private boolean heatMapsConstructed;

  // create a maze, taking in the width and height, in terms of maze cells, and
  // the image width and height you want to display it too
  // <skipConstruction> is used to decide if you want to animate the construction
  // or not
  public Maze(int width, int height, int cellSize, int preference, boolean skipConstruction) {
    if (width <= 1 && height <= 1) {
      throw new IllegalArgumentException("Cannot have a 1x1 maze");
    }
    int randWeightMax = (width + 1) / 2;
    this.maze = new ArrayList<ArrayList<AMazeCell>>();
    this.cellSize = cellSize;
    this.edges = new ArrayList<Edge>();
    this.references = new HashMap<AMazeCell, AMazeCell>();
    this.alreadySeen = new ArrayDeque<AMazeCell>();
    // Generate the maze with completely walled cells
    this.generateClosedMaze(width, height, cellSize);

    this.currentManual = this.start;
    // make all the edges in the maze
    ArrayList<Edge> allEdges = new ArrayList<Edge>();
    this.generateEdges(width, height, randWeightMax, new Random(), preference, allEdges);

    // randomize the order of those edges
    this.generateRandomConnections(new Random(), allEdges);

    this.edgeIterator = this.edges.iterator();

    this.currentMazeScene = this.drawMaze(width * cellSize, height * cellSize);

    boolean underConstruction = true;
    if (skipConstruction) {
      while (underConstruction) {
        underConstruction = this.constructMaze();
      }
    }
  }

  // generate a closed maze, of the correct width and height, with the correct
  // cell sizes
  private void generateClosedMaze(int width, int height, int size) {
    ArrayList<AMazeCell> rowList = new ArrayList<AMazeCell>();

    // iterate height times, creating each row of walled off cells in the map
    for (int row = 0; row < height; row += 1) {
      // iterate width times, creating the individual row of the maze
      for (int col = 0; col < width; col += 1) {
        if (row == 0 && col == 0) {
          StartCell start = new StartCell(size, col, row);
          this.start = start;
          rowList.add(start);
          references.put(start, start);

        }
        else if (row == height - 1 && col == width - 1) {
          EndCell end = new EndCell(size, col, row);
          this.end = end;
          rowList.add(end);
          references.put(end, end);

        }
        else {
          AMazeCell cell = new MazeCell(size, col, row);
          rowList.add(cell);
          references.put(cell, cell);
        }
      }
      this.maze.add(rowList);
      rowList = new ArrayList<AMazeCell>();
    }
  }

  // generate all edges in the maze, adding them to the <allEdges> arrayList,
  // randomizing each edge weight
  private void generateEdges(int width, int height, int randWeightMax, Random rand, int preference,
      ArrayList<Edge> allEdges) {
    // make all the edges in the maze
    EdgeWeightUtil forWeight = new EdgeWeightUtil();
    // iterate height times, creating all relevant right and down edges in the row
    for (int row = 0; row < height; row += 1) {
      // iterate width times, creating all the relevant right and down edges in the
      // row, based on the given randWeightMax, a random object, an edge preference
      for (int col = 0; col < width; col += 1) {
        if (row != height - 1 && col != width - 1) {
          Edge downEdge = new Edge(this.maze.get(row).get(col), this.maze.get(row + 1).get(col),
              true, forWeight.generateWeight(rand, randWeightMax, preference, true));
          Edge rightEdge = new Edge(this.maze.get(row).get(col), this.maze.get(row).get(col + 1),
              false, forWeight.generateWeight(rand, randWeightMax, preference, false));

          forWeight.insertEdge(allEdges, downEdge);
          forWeight.insertEdge(allEdges, rightEdge);
        }
        else if (row == height - 1 && col != width - 1) {
          Edge rightEdge = new Edge(this.maze.get(row).get(col), this.maze.get(row).get(col + 1),
              false, forWeight.generateWeight(rand, randWeightMax, preference, false));

          forWeight.insertEdge(allEdges, rightEdge);
        }
        else if (row != height - 1 && col == width - 1) {
          Edge downEdge = new Edge(this.maze.get(row).get(col), this.maze.get(row + 1).get(col),
              true, forWeight.generateWeight(rand, randWeightMax, preference, false));

          forWeight.insertEdge(allEdges, downEdge);
        }
      }
    }
  }

  // effect; using the list of edges in priority/ random order,
  // connect relevant nodes in the maze, knocking down the corresponding walls
  private void generateRandomConnections(Random rand, ArrayList<Edge> allEdges) {
    // Collections.shuffle(this.edges, rand);

    UnionFind ufUtil = new UnionFind(this.references);

    // TODO; give option to not make the edges, only add to the thing
    // ArrayList<Edge> workList = new ArrayList<Edge>();
    // start by going through the list of edges
    while (this.edges.size() < this.references.size() - 1) {
      Edge edge = allEdges.get(0);
      if (ufUtil.find(edge.observeCell(true)).equals(ufUtil.find(edge.observeCell(false)))) {
        allEdges.remove(0);
      }
      else {
        ufUtil.union(edge.observeCell(true), edge.observeCell(false));
        // edge.makeConnection();
        this.edges.add(allEdges.remove(0));
      }
    }
  }

  // if you still can iterate over the edges, make the next connection for an edge
  public boolean constructMaze() {
    if (this.edgeIterator.hasNext()) {
      Edge next = this.edgeIterator.next();
      next.makeConnection();
      this.updateMazeScene(next.observeCell(true));
      this.updateMazeScene(next.observeCell(false));
      return true;
    }
    else {

      return false;
    }
  }

  // place each cell of the maze, onto a worldscene, in order to draw the complete
  // maze
  WorldScene drawMaze(int mazeWidth, int mazeHeight) {
    WorldScene maze = new WorldScene(mazeWidth, mazeHeight);
    // Iterate through all the rows of cells
    for (int i = 0; i < this.maze.size(); i += 1) {
      // Iterate through all the columns of cells
      for (int j = 0; j < this.maze.get(0).size(); j += 1) {
        int xPlace = (j * this.cellSize) + this.cellSize / 2;
        int yPlace = (i * this.cellSize) + this.cellSize / 2;
        maze.placeImageXY(this.maze.get(i).get(j).drawCell(), xPlace, yPlace);
      }
    }
    return maze;
  }

  // observe the height of this maze; this is a method that helps with testing
  // purposes
  // also used to check if the correct number of edges have been added
  int mazeHeight() {
    return this.maze.size();
  }

  // observe the width of this maze; this is a method that helps with testing
  // purposes
  // also used to check if the correct number of edges have been added
  int mazeWidth() {
    return this.maze.get(0).size();
  }

  // check that this maze has the correct number of edges added (nodes - 1)
  // also used for checking
  boolean checkValidMinSpanningTree() {
    return this.mazeHeight() * this.mazeWidth() - 1 == this.edges.size()
        && this.edges.size() == this.checkNumConnectedEdges();
  }

  // initialize the search worklists; if <isDepthFirst> is true, set up the
  // correct structure for depth first search, else set up the structure for
  // breadth first
  void initializeSearch(boolean isDepthFirst) {
    this.references = new HashMap<AMazeCell, AMazeCell>();
    this.references.put(start, start);
    if (isDepthFirst) {
      this.workList = new Stack<AMazeCell>();
      this.workList.add(this.start);
    }
    else {
      this.workList = new Queue<AMazeCell>();
      this.workList.add(this.start);
    }
  }

  // while searching, search in the corresponding way (based on if set up for
  // breadth or depth
  // first search. If you are at your target, do nothing, else
  boolean findPath() {
    // As long as the worklist isn't empty...
    if (this.workList.isEmpty()) {
      throw new IllegalStateException("Maze should be solvable, if this is thrown, it's not");
    }
    else {
      AMazeCell next = this.workList.remove();
      next.wasChecked(false);
      this.updateMazeScene(next);
      if (next.equals(this.end)) {
        this.showCorrectPath();
        return true; // Success!
      }
      else if (!this.alreadySeen.contains(next)) {
        // Iterate through all the cells neighbors and add them
        // to the workList
        for (AMazeCell cell : next.getNeighbors()) {
          if (!this.alreadySeen.contains(cell)) {
            this.workList.add(cell);
            this.references.put(cell, next);
          }
        }
        this.alreadySeen.add(next);
        // do nothing: we've already seen this one
      }
    }
    // We haven't found the to AMazeCell, and there are no more to try
    return false;
  }

  // show the correct path; reconstruct the path that goes
  // from the end path to the start path
  private void showCorrectPath() {
    this.start.wasChecked(true);
    this.updateMazeScene(this.start);
    AMazeCell nextCell = this.end;
    // while the cell's reference is not itself (there is a cell you can
    // get to in the path -> StartCell points to itself) -> make each cell
    // in this group now draw in dark blue
    while (!nextCell.equals(this.start)) {
      nextCell.wasChecked(true);
      this.updateMazeScene(nextCell);
      nextCell = this.references.get(nextCell);
    }
  }

  // calculate the longest path from either the start (if <fromStart> is true)
  // or from the end
  public int calculateLongestPath(boolean fromStart) {
    if (fromStart) {
      return this.start.longestPath("Start");
    }
    else {
      return this.end.longestPath("Start");
    }
  }

  // is the manual traversal solved? Is the current manual cell the end?
  // if it is, show the correct path
  public boolean solvedManually() {
    if (this.end.equals(this.currentManual)) {
      this.showCorrectPath();
      return true;
    }
    else {
      return false;
    }
  }

  //move manually through the maze, given a String direction representing the
  // direction
  // to move in. Only move if needed
  public void moveManually(String dir) {
    AMazeCell nextCell;
    if (this.currentManual.equals(this.start) && !this.alreadySeen.contains(this.start)) {
      this.alreadySeen.add(this.start);
    }
    nextCell = this.currentManual.move(dir);
    this.updateMazeScene(this.currentManual);
    if (!nextCell.equals(this.currentManual) && !this.currentManual.equals(nextCell)) {
      if (!this.alreadySeen.contains(nextCell)) {
        this.references.put(nextCell, this.currentManual);
        this.alreadySeen.add(nextCell);
      }
      this.currentManual = nextCell;
    }
    this.updateMazeScene(nextCell);
  }

  // Whenever a cell is updated, this method updates the maze scene to reflect
  // that update
  // given the updated cell
  private void updateMazeScene(AMazeCell cell) {
    // places this cell after it has been update onto the scene
    this.currentMazeScene.placeImageXY(cell.drawCell(),
        (cell.xCoord * this.cellSize) + this.cellSize / 2,
        (cell.yCoord * this.cellSize) + this.cellSize / 2);
  }

  // Makes the heat maps if they haven't been made yet
  private void constructHeatMaps() {
    int startLen = this.calculateLongestPath(true);
    int endLen = this.calculateLongestPath(false);

    this.start.updateHeats("Start", 0, true);
    this.end.updateHeats("Start", 0, false);

    this.drawHeatMap(true, startLen);
    this.drawHeatMap(false, endLen);
  }

  // Makes a heat map from either the start or end, given the longest path to a
  // cell
  // and stores them in the heatMap fields
  private void drawHeatMap(Boolean fromStart, int longestPath) {
    WorldScene maze = new WorldScene(this.mazeWidth() * this.cellSize,
        this.mazeHeight() * this.cellSize);
    // Iterate through the rows of cells
    for (int i = 0; i < this.maze.size(); i += 1) {
      // Iterate through the columns of cells
      for (int j = 0; j < this.maze.get(0).size(); j += 1) {
        int xPlace = (j * this.cellSize) + this.cellSize / 2;
        int yPlace = (i * this.cellSize) + this.cellSize / 2;
        maze.placeImageXY(this.maze.get(i).get(j).drawHeatCell(fromStart, longestPath), xPlace,
            yPlace);
      }
    }

    if (fromStart) {
      this.heatMapFromStart = maze;
    }
    else {
      this.heatMapFromEnd = maze;
    }
  }

  // Returns the requested scene of the maze, and constructs the heatMaps
  // if they haven't been already made
  public WorldScene observeMaze(int mode) {
    if (!heatMapsConstructed) {
      this.constructHeatMaps();
    }
    if (mode == 0) {
      return this.currentMazeScene;
    }
    else if (mode == 1) {
      return this.heatMapFromStart;
    }
    else if (mode == -1) {
      return this.heatMapFromEnd;
    }
    else {
      throw new IllegalArgumentException("Invalid Viewing Mode");
    }
  }

  // -------------- methods for testing
  // check the number of connected edges in this maze
  int checkNumConnectedEdges() {
    int i = 0;
    // Iterate through all the edges in edges and make sure they are connected
    for (Edge edge : this.edges) {
      if (edge.isConnected()) {
        i += 1;
      }
    }
    return i;
  }

  // check that the search has been initialized
  boolean searchInitilized(int solveMode) {
    return (solveMode == 3 && this.workList == null)
        || (solveMode == 1 && this.workList instanceof Stack<?>)
        || (solveMode == 2 && this.workList instanceof Queue<?>);
  }

  // check the number of cells searched through/ traversed
  // check from alreadySeen
  int checkNumSeen() {
    return this.alreadySeen.size();
  }

  public int numOfEdges() {
    return this.edges.size();
  }
}

// represents an edge in a maze, connecting two maze cells together
class Edge {
  // <isVertical> represents if this is a vertical or horizontal edge,
  // which is used to add context for cell1 and cell2
  private final boolean isVertical;

  // <cell1> is the left cell in an edge if it is horizontal, or the
  // upper cell in an edge if it is vertical
  private final AMazeCell cell1;
  // <cell1> is the right cell in an edge if it is horizontal, or the
  // lower cell in an edge if it is vertical
  private final AMazeCell cell2;
  // represents the weight of this edge
  private final int weight;

  // create an edge from two cells, its vertically boolean, and weight
  Edge(AMazeCell cell1, AMazeCell cell2, boolean isVertical, int weight) {
    this.cell1 = cell1;
    this.cell2 = cell2;
    this.isVertical = isVertical;
    this.weight = weight;
  }

  // observe either the first or second cell, based on which one is
  // of interest. This method is used with the find method of the Union/Find
  // data structure
  AMazeCell observeCell(boolean isCell1) {
    if (isCell1) {
      return this.cell1;
    }
    else {
      return this.cell2;
    }
  }

  // make a connection between the two cells in a given edge
  void makeConnection() {
    this.cell1.updateCell(this.cell2, this.isVertical);
  }

  // compare the two weights of the edges. If this weight is higher (lower
  // priority),
  // return a + number number. If they have the same weight, return 0. Other wise,
  // return a negative number (this has a higher priority)
  int compareWeights(Edge other) {
    return this.weight - other.weight;
  }

  // ---------------------------------------- methods for testing
  // are the two cells in this edge connected yet?
  boolean isConnected() {
    return this.cell1.connectCell(this.cell2, this.isVertical);
  }
}