import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javalib.worldimages.AlignModeX;
import javalib.worldimages.AlignModeY;
import javalib.worldimages.LineImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.OverlayImage;
import javalib.worldimages.OverlayOffsetAlign;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;
import tester.Tester;

// represents examples of mazes
class ExamplesMazeSolver {
  Maze maze1;
  Maze maze2;
  Maze maze3;
  Maze maze4;

  MazeSolver world1;
  MazeSolver world2;
  MazeSolver world3;
  MazeSolver world4;

  AMazeCell cell0_0;
  AMazeCell cell0_1;
  AMazeCell cell0_2;
  AMazeCell cell1_0;
  AMazeCell cell1_1;
  AMazeCell cell1_2;
  AMazeCell cell2_0;
  AMazeCell cell2_1;
  AMazeCell cell2_2;

  AMazeCell testCell1R1;
  AMazeCell testCell2R1;
  AMazeCell testCell3R1;

  AMazeCell testCell1R2;
  AMazeCell testCell2R2;
  AMazeCell testCell3R2;

  Maze horizontalWorld;

  AMazeCell cell1R1;
  AMazeCell cell2R1;
  AMazeCell cell3R1;

  AMazeCell cell1R2;
  AMazeCell cell2R2;
  AMazeCell cell3R2;

  AMazeCell cell1R3;
  AMazeCell cell2R3;
  AMazeCell cell3R3;

  Edge edge0;
  Edge edge1;
  Edge edge2;
  Edge edge3;
  Edge edge4;
  Edge edge5;
  Edge edge6;
  Edge edge7;

  // Initialize the example worlds
  void initFields() {
    maze1 = new Maze(100, 60, 10, -1, false);
    maze2 = new Maze(10, 10, 50, 0, true);
    maze3 = new Maze(4, 50, 20, 1, true);
    maze4 = new Maze(1, 3, 10, 0, false);

    world1 = new MazeSolver(30, 30, 20, 1, true);
    world2 = new MazeSolver(10, 10, 50, 0, false);
    world3 = new MazeSolver(5, 5, 200, 0, true);
    world4 = new MazeSolver(100, 60, 12, 0, false);

    cell0_0 = new StartCell(10, 0, 0);
    cell0_1 = new MazeCell(10, 0, 1);
    cell0_2 = new MazeCell(10, 0, 2);
    cell1_0 = new MazeCell(10, 1, 0);
    cell1_1 = new MazeCell(10, 1, 1);
    cell1_2 = new MazeCell(10, 1, 2);
    cell2_0 = new MazeCell(10, 2, 0);
    cell2_1 = new MazeCell(10, 2, 1);
    cell2_2 = new EndCell(10, 2, 2);

    edge0 = new Edge(cell0_0, cell0_1, false, 1);
    edge1 = new Edge(cell1_1, cell2_1, true, 2);
    edge2 = new Edge(cell2_0, cell2_1, false, 3);
    edge3 = new Edge(cell0_2, cell1_2, true, 4);
    edge4 = new Edge(cell1_0, cell1_1, false, 5);
    edge5 = new Edge(cell0_0, cell1_0, true, 1);
    edge6 = new Edge(cell1_1, cell1_2, false, 2);
    edge7 = new Edge(cell1_2, cell2_2, true, 3);

    testCell1R1 = new StartCell(25, 0, 0);
    testCell2R1 = new MazeCell(25, 0, 1);
    testCell3R1 = new MazeCell(25, 0, 2);

    testCell1R2 = new MazeCell(25, 1, 0);
    testCell2R2 = new MazeCell(25, 1, 1);
    testCell3R2 = new EndCell(25, 1, 2);

    testCell1R1.updateCell(testCell1R2, true);
    testCell2R1.updateCell(testCell2R2, true);
    testCell3R1.updateCell(testCell3R2, true);
    testCell1R2.updateCell(testCell2R2, false);
    testCell2R1.updateCell(testCell3R1, false);

    horizontalWorld = new Maze(6, 1, 35, 0, true);

    cell1R1 = new StartCell(25, 0, 0);
    cell2R1 = new MazeCell(25, 0, 1);
    cell3R1 = new MazeCell(25, 0, 2);

    cell1R2 = new MazeCell(25, 1, 1);
    cell2R2 = new MazeCell(25, 1, 1);
    cell3R2 = new MazeCell(25, 1, 1);

    cell1R3 = new MazeCell(25, 2, 1);
    cell2R3 = new MazeCell(25, 2, 1);
    cell3R3 = new EndCell(25, 2, 0);

    cell1R1.updateCell(cell2R1, false);
    cell1R1.updateCell(cell1R2, true);
    cell1R2.updateCell(cell1R3, true);
    cell2R2.updateCell(cell2R3, true);
    cell3R1.updateCell(cell3R2, true);
    cell1R3.updateCell(cell2R3, false);
    cell3R2.updateCell(cell3R3, true);
    cell2R2.updateCell(cell3R2, false);
  }

  void initMaze() {
    edge0.makeConnection();
    edge1.makeConnection();
    edge2.makeConnection();
    edge3.makeConnection();
    edge4.makeConnection();
    edge5.makeConnection();
    edge6.makeConnection();
    edge7.makeConnection();
  }

  boolean testBigBang(Tester t) {
    this.initFields();
    world1.bigBang(world1.worldWidth(), world1.worldHeight(), 0.001);
    return true;
  }

  // Cell Tests

  // test update a maze cell's neighbors
  boolean testUpdateCell(Tester t) {
    this.initFields();
    boolean countBefore = t.checkExpect(cell0_0.countNeighbors(), 0)
        && t.checkExpect(cell0_1.countNeighbors(), 0) && t.checkExpect(cell1_1.countNeighbors(), 0)
        && t.checkExpect(cell2_1.countNeighbors(), 0) && t.checkExpect(cell2_2.countNeighbors(), 0);

    cell0_0.updateCell(cell0_1, false);
    cell1_1.updateCell(cell2_1, true);
    cell2_1.updateCell(cell2_2, false);

    boolean countAfter = t.checkExpect(cell0_0.countNeighbors(), 1)
        && t.checkExpect(cell0_1.countNeighbors(), 1) && t.checkExpect(cell1_1.countNeighbors(), 1)
        && t.checkExpect(cell2_1.countNeighbors(), 2) && t.checkExpect(cell2_2.countNeighbors(), 1);
    return countBefore && countAfter;
  }

  // test drawing an individual cell (cell and it's wall neighbors
  boolean testDrawCell(Tester t) {
    this.initFields();

    WorldImage wallsLG = new RectangleImage(10, 10, OutlineMode.OUTLINE, Color.LIGHT_GRAY);
    WorldImage wallsG = new RectangleImage(10, 10, OutlineMode.OUTLINE, Color.GREEN);
    WorldImage wallsM = new RectangleImage(10, 10, OutlineMode.OUTLINE, Color.MAGENTA);
    WorldImage newWall;
    newWall = new LineImage(new Posn(10, 0), Color.DARK_GRAY);
    wallsLG = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.TOP, newWall, 0, 0, wallsLG);
    wallsG = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.TOP, newWall, 0, 0, wallsG);
    wallsM = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.TOP, newWall, 0, 0, wallsM);

    newWall = new LineImage(new Posn(10, 0), Color.DARK_GRAY);
    wallsLG = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, newWall, 0, 0, wallsLG);
    wallsG = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, newWall, 0, 0, wallsG);
    wallsM = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, newWall, 0, 0, wallsM);

    newWall = new LineImage(new Posn(0, 10), Color.DARK_GRAY);
    wallsLG = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.MIDDLE, newWall, 0, 0, wallsLG);
    wallsG = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.MIDDLE, newWall, 0, 0, wallsG);
    wallsM = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.MIDDLE, newWall, 0, 0, wallsM);

    newWall = new LineImage(new Posn(0, 10), Color.DARK_GRAY);
    wallsLG = new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.MIDDLE, newWall, 0, 0, wallsLG);
    wallsG = new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.MIDDLE, newWall, 0, 0, wallsG);
    wallsM = new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.MIDDLE, newWall, 0, 0, wallsM);

    WorldImage walledOffMazeCell = new OverlayImage(wallsLG,
        new RectangleImage(10, 10, OutlineMode.SOLID, Color.LIGHT_GRAY));
    WorldImage walledOffStartCell = new OverlayImage(wallsG,
        new RectangleImage(10, 10, OutlineMode.SOLID, Color.GREEN));
    WorldImage walledOffEndCell = new OverlayImage(wallsM,
        new RectangleImage(10, 10, OutlineMode.SOLID, Color.MAGENTA));

    return t.checkExpect(cell0_0.drawCell(), walledOffStartCell)
        && t.checkExpect(cell1_2.drawCell(), walledOffMazeCell)
        && t.checkExpect(cell2_2.drawCell(), walledOffEndCell);
  }

  // test drawHeat
  boolean testDrawHeat(Tester t) {
    this.initFields();
    this.initMaze();

    cell0_0.updateHeats("Start", 0, true);
    Color middleColor = new Color(128, 0, 127);

    WorldImage wallStart = new RectangleImage(10, 10, OutlineMode.OUTLINE, Color.RED);
    WorldImage wallEnd = new RectangleImage(10, 10, OutlineMode.OUTLINE, Color.BLUE);
    WorldImage wallMiddle = new RectangleImage(10, 10, OutlineMode.OUTLINE, middleColor);
    WorldImage newWall;

    newWall = new LineImage(new Posn(10, 0), Color.DARK_GRAY);
    wallStart = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.TOP, newWall, 0, 0, wallStart);
    wallMiddle = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.TOP, newWall, 0, 0,
        wallMiddle);

    newWall = new LineImage(new Posn(10, 0), Color.DARK_GRAY);
    wallEnd = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, newWall, 0, 0, wallEnd);

    newWall = new LineImage(new Posn(0, 10), Color.DARK_GRAY);
    wallStart = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.MIDDLE, newWall, 0, 0,
        wallStart);
    wallEnd = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.MIDDLE, newWall, 0, 0, wallEnd);

    newWall = new LineImage(new Posn(0, 10), Color.DARK_GRAY);
    wallEnd = new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.MIDDLE, newWall, 0, 0, wallEnd);

    WorldImage cellStart = new RectangleImage(10, 10, OutlineMode.SOLID, Color.RED);
    WorldImage cellEnd = new RectangleImage(10, 10, OutlineMode.SOLID, Color.BLUE);
    WorldImage cellMiddle = new RectangleImage(10, 10, OutlineMode.SOLID, middleColor);

    cellStart = new OverlayImage(wallStart, cellStart);
    cellEnd = new OverlayImage(wallEnd, cellEnd);
    cellMiddle = new OverlayImage(wallMiddle, cellMiddle);
    // return t.checkExpect(cell0_0.countNeighbors(), 2);

    return t.checkExpect(cell0_0.drawHeatCell(true, 4), cellStart)
        && t.checkExpect(cell2_2.drawHeatCell(true, 4), cellEnd)
        && t.checkExpect(cell1_1.drawHeatCell(true, 4), cellMiddle);
  }

  // test longestPath
  boolean testLongestPath(Tester t) {
    this.initFields();
    this.initMaze();
    return t.checkExpect(cell0_0.longestPath("Start"), 5)
        && t.checkExpect(cell2_2.longestPath("Start"), 6);
  }

  // test getNeighbors
  boolean testGetNeighbors(Tester t) {
    this.initFields();
    this.initMaze();
    return t.checkExpect(cell0_0.getNeighbors().size(), 2)
        && t.checkExpect(cell1_2.getNeighbors().size(), 3)
        && t.checkExpect(cell2_0.getNeighbors().size(), 1)
        && t.checkExpect(cell0_2.getNeighbors().size(), 1);
  }

  // test wasChecked
  boolean testWasChecked(Tester t) {
    this.initFields();
    WorldImage walls = new RectangleImage(10, 10, OutlineMode.OUTLINE, Color.LIGHT_GRAY);
    WorldImage wallsVisited = new RectangleImage(10, 10, OutlineMode.OUTLINE, Color.CYAN);
    WorldImage wallsCorrect = new RectangleImage(10, 10, OutlineMode.OUTLINE, Color.BLUE);
    WorldImage newWall;
    newWall = new LineImage(new Posn(10, 0), Color.DARK_GRAY);
    walls = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.TOP, newWall, 0, 0, walls);
    wallsVisited = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.TOP, newWall, 0, 0,
        wallsVisited);
    wallsCorrect = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.TOP, newWall, 0, 0,
        wallsCorrect);

    newWall = new LineImage(new Posn(10, 0), Color.DARK_GRAY);
    walls = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, newWall, 0, 0, walls);
    wallsVisited = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, newWall, 0, 0,
        wallsVisited);
    wallsCorrect = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, newWall, 0, 0,
        wallsCorrect);

    newWall = new LineImage(new Posn(0, 10), Color.DARK_GRAY);
    walls = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.MIDDLE, newWall, 0, 0, walls);
    wallsVisited = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.MIDDLE, newWall, 0, 0,
        wallsVisited);
    wallsCorrect = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.MIDDLE, newWall, 0, 0,
        wallsCorrect);

    newWall = new LineImage(new Posn(0, 10), Color.DARK_GRAY);
    walls = new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.MIDDLE, newWall, 0, 0, walls);
    wallsVisited = new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.MIDDLE, newWall, 0, 0,
        wallsVisited);
    wallsCorrect = new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.MIDDLE, newWall, 0, 0,
        wallsCorrect);

    WorldImage regularCell = new OverlayImage(walls,
        new RectangleImage(10, 10, OutlineMode.SOLID, Color.LIGHT_GRAY));
    WorldImage visitedCell = new OverlayImage(wallsVisited,
        new RectangleImage(10, 10, OutlineMode.SOLID, Color.CYAN));
    WorldImage correctCell = new OverlayImage(wallsCorrect,
        new RectangleImage(10, 10, OutlineMode.SOLID, Color.BLUE));

    boolean testRegular = t.checkExpect(cell1_1.drawCell(), regularCell);

    cell1_1.wasChecked(false);

    boolean testVisited = t.checkExpect(cell1_1.drawCell(), visitedCell);

    cell1_1.wasChecked(true);

    boolean testCorrect = t.checkExpect(cell1_1.drawCell(), correctCell);

    return testRegular && testVisited && testCorrect;
  }

  // test move
  boolean testMove(Tester t) {
    this.initFields();
    this.initMaze();

    return t.checkExpect(cell0_0.move("right").countNeighbors(), 1)
        && t.checkExpect(cell2_1.move("up").countNeighbors(), 3)
        && t.checkExpect(cell0_2.move("left").countNeighbors(), 1)
        && t.checkExpect(cell1_0.move("down").countNeighbors(), 2);
  }

  // test connectCell()
  boolean testConnectCell(Tester t) {
    this.initFields();
    return t.checkExpect(testCell1R1.connectCell(testCell1R2, true), true)
        && t.checkExpect(testCell1R2.connectCell(testCell2R2, false), true)
        && t.checkExpect(testCell1R1.connectCell(testCell3R2, true), false)
        && t.checkExpect(testCell1R1.connectCell(testCell2R1, false), false)
        && t.checkExpect(testCell1R1.connectCell(testCell3R1, false), false);
  }

  // Edge Tests

  // test observing specific edges from an edge
  boolean testObserveCell(Tester t) {
    this.initFields();
    return t.checkExpect(edge0.observeCell(true), cell0_0)
        && t.checkExpect(edge1.observeCell(false), cell2_1)
        && t.checkExpect(edge2.observeCell(true), cell2_0)
        && t.checkExpect(edge3.observeCell(false), cell1_2)
        && t.checkExpect(edge4.observeCell(true), cell1_0);
  }

  // test the connection making from an edge
  boolean testMakeConnection(Tester t) {
    this.initFields();
    boolean countBefore = t.checkExpect(cell0_0.countNeighbors(), 0)
        && t.checkExpect(cell1_1.countNeighbors(), 0) && t.checkExpect(cell2_0.countNeighbors(), 0)
        && t.checkExpect(cell0_2.countNeighbors(), 0) && t.checkExpect(cell1_0.countNeighbors(), 0);

    edge0.makeConnection();
    edge1.makeConnection();
    edge2.makeConnection();
    edge3.makeConnection();
    edge4.makeConnection();

    boolean countAfter = t.checkExpect(cell0_1.countNeighbors(), 1)
        && t.checkExpect(cell2_1.countNeighbors(), 2) && t.checkExpect(cell2_0.countNeighbors(), 1)
        && t.checkExpect(cell1_2.countNeighbors(), 1) && t.checkExpect(cell1_1.countNeighbors(), 2);
    return countBefore && countAfter;
  }

  // test comparing two edges, by their weights
  boolean testCompareWeights(Tester t) {
    this.initFields();
    return t.checkExpect(edge0.compareWeights(edge0), 0)
        && t.checkExpect(edge2.compareWeights(edge1), 1)
        && t.checkExpect(edge3.compareWeights(edge0), 3)
        && t.checkExpect(edge1.compareWeights(edge4), -3)
        && t.checkExpect(edge3.compareWeights(edge4), -1);
  }

  // test isConnected
  boolean testIsConnected(Tester t) {
    this.initFields();
    Edge edge1 = new Edge(testCell1R1, testCell1R2, true, 3);
    Edge edge2 = new Edge(testCell1R2, testCell2R2, false, 2);
    Edge edge3 = new Edge(testCell1R1, testCell3R2, true, 0);
    Edge edge4 = new Edge(testCell1R1, testCell2R1, false, 6);
    Edge edge5 = new Edge(testCell1R1, testCell3R1, false, 3);
    return t.checkExpect(edge1.isConnected(), true) && t.checkExpect(edge2.isConnected(), true)
        && t.checkExpect(edge3.isConnected(), false) && t.checkExpect(edge4.isConnected(), false)
        && t.checkExpect(edge5.isConnected(), false);
  }

  // Maze Tests

  // test constructMaze()
  boolean testConstructMaze(Tester t) {
    this.initFields();
    boolean checkBoolean = t.checkExpect(maze1.constructMaze(), true)
        && t.checkExpect(maze2.constructMaze(), false)
        && t.checkExpect(maze3.constructMaze(), false)
        && t.checkExpect(maze4.constructMaze(), true);

    boolean checkNumConnections1 = t.checkExpect(maze1.checkNumConnectedEdges(), 1)
        && t.checkExpect(maze2.checkNumConnectedEdges(), 10 * 10 - 1)
        && t.checkExpect(maze3.checkNumConnectedEdges(), 4 * 50 - 1)
        && t.checkExpect(maze4.checkNumConnectedEdges(), 1);

    maze1.constructMaze();
    maze2.constructMaze();
    maze3.constructMaze();
    maze4.constructMaze();
    maze1.constructMaze();
    maze4.constructMaze();

    boolean checkNumConnections2 = t.checkExpect(maze1.checkNumConnectedEdges(), 3)
        && t.checkExpect(maze2.checkNumConnectedEdges(), 10 * 10 - 1)
        && t.checkExpect(maze3.checkNumConnectedEdges(), 4 * 50 - 1)
        && t.checkExpect(maze4.checkNumConnectedEdges(), 2);

    return checkBoolean && checkNumConnections1 && checkNumConnections2;
  }

  // test the generation of a closed Maze (generate a maze of the correct height,
  // since
  // the maze's (currently) are not left in a state where they are unconnected
  // test that its correct size
  boolean testGenerateClosedMaze(Tester t) {
    this.initFields();
    return t.checkExpect(maze1.mazeHeight(), 60) && t.checkExpect(maze2.mazeHeight(), 10)
        && t.checkExpect(maze3.mazeHeight(), 50) && t.checkExpect(maze1.mazeWidth(), 100)
        && t.checkExpect(maze2.mazeWidth(), 10) && t.checkExpect(maze3.mazeWidth(), 4);
  }

  // check that the spanning tree that is created is a valid spanning tree, ie
  // there are
  // the minimum number of edges (nodes - 1)
  boolean testGenerateRandomConnections(Tester t) {
    this.initFields();
    return t.checkExpect(maze2.checkValidMinSpanningTree(), true)
        && t.checkExpect(maze3.checkValidMinSpanningTree(), true);
  }

  // test the drawing of the maze (correct width and height image)
  boolean testDrawMaze(Tester t) {
    this.initFields();
    return t.checkExpect(maze1.drawMaze(1000, 600).width, 1000)
        && t.checkExpect(maze2.drawMaze(500, 500).width, 500)
        && t.checkExpect(maze3.drawMaze(80, 1000).width, 80)
        && t.checkExpect(maze4.drawMaze(10, 30).width, 10)
        && t.checkExpect(maze1.drawMaze(1000, 600).height, 600)
        && t.checkExpect(maze2.drawMaze(500, 500).height, 500)
        && t.checkExpect(maze3.drawMaze(80, 1000).height, 1000)
        && t.checkExpect(maze4.drawMaze(10, 30).height, 30);
  }

  // test mazeHeight()
  // test mazeWidth
  boolean testMazeDimensions(Tester t) {
    this.initFields();
    return t.checkExpect(maze1.mazeHeight(), 60) && t.checkExpect(maze2.mazeHeight(), 10)
        && t.checkExpect(maze3.mazeHeight(), 50) && t.checkExpect(maze4.mazeHeight(), 3)
        && t.checkExpect(maze1.mazeWidth(), 100) && t.checkExpect(maze2.mazeWidth(), 10)
        && t.checkExpect(maze3.mazeWidth(), 4) && t.checkExpect(maze4.mazeWidth(), 1);
  }

  // test checkValidMinSpanningTree() - tester
  boolean testCheckValidMinSpanningTree(Tester t) {
    this.initFields();
    boolean checkFirst = t.checkExpect(maze1.checkValidMinSpanningTree(), false)
        && t.checkExpect(maze2.checkValidMinSpanningTree(), true)
        && t.checkExpect(maze3.checkValidMinSpanningTree(), true)
        && t.checkExpect(maze4.checkValidMinSpanningTree(), false);

    for (int i = 0; i < 5; i += 1) {
      maze1.constructMaze();
      maze4.constructMaze();
    }

    boolean checkSecond = t.checkExpect(maze1.checkValidMinSpanningTree(), false)
        && t.checkExpect(maze4.checkValidMinSpanningTree(), true);

    for (int i = 0; i < 6000; i += 1) {
      maze1.constructMaze();
    }

    boolean finalCheck = t.checkExpect(maze1.checkValidMinSpanningTree(), true);

    return checkFirst && checkSecond && finalCheck;
  }

  // test initializeSearch()
  boolean testInitializeSearch(Tester t) {
    this.initFields();
    boolean firstCheck = t.checkExpect(maze1.searchInitilized(1), false)
        && t.checkExpect(maze1.searchInitilized(2), false)
        && t.checkExpect(maze1.searchInitilized(3), true)
        && t.checkExpect(maze3.searchInitilized(1), false)
        && t.checkExpect(maze3.searchInitilized(2), false)
        && t.checkExpect(maze3.searchInitilized(3), true);

    for (int i = 0; i < 6000; i += 1) {
      maze1.constructMaze();
      maze4.constructMaze();
    }

    maze1.initializeSearch(true);
    maze2.initializeSearch(false);

    boolean secondCheck = t.checkExpect(maze1.searchInitilized(1), true)
        && t.checkExpect(maze2.searchInitilized(2), true)
        && t.checkExpect(maze3.searchInitilized(3), true)
        && t.checkExpect(maze2.searchInitilized(1), false)
        && t.checkExpect(maze1.searchInitilized(3), false)
        && t.checkExpect(maze1.searchInitilized(2), false);

    return firstCheck && secondCheck;
  }

  // test findPath()
  boolean testFindPath(Tester t) {
    this.initFields();
    for (int i = 0; i < 6000; i += 1) {
      maze1.constructMaze();
      maze4.constructMaze();
    }

    maze1.initializeSearch(true);
    maze2.initializeSearch(false);
    maze4.initializeSearch(true);

    boolean check = true;
    for (int i = 0; i < 2; i += 1) {
      check = check && t.checkExpect(maze1.findPath(), false)
          && t.checkExpect(maze2.findPath(), false) && t.checkExpect(maze4.findPath(), false)
          && t.checkExpect(maze1.checkNumSeen(), i + 1)
          && t.checkExpect(maze2.checkNumSeen(), i + 1)
          && t.checkExpect(maze4.checkNumSeen(), i + 1);
    }

    check = check && t.checkExpect(maze4.findPath(), true)
        && t.checkExpect(maze4.checkNumSeen(), 2);

    for (int i = 2; i < 11; i += 1) {
      check = check && t.checkExpect(maze1.findPath(), false)
          && t.checkExpect(maze2.findPath(), false) && t.checkExpect(maze1.checkNumSeen(), i + 1)
          && t.checkExpect(maze2.checkNumSeen(), i + 1);
    }
    return check;
  }

  // test calculateLongestPath(true)
  boolean testCalculateLongestPath(Tester t) {
    this.initFields();
    for (int i = 0; i < 3; i += 1) {
      maze4.constructMaze();
    }
    boolean checkMaze = t.checkExpect(maze4.calculateLongestPath(true), 3)
        && t.checkExpect(maze4.calculateLongestPath(false), 3)
        && t.checkExpect(horizontalWorld.calculateLongestPath(true), 6)
        && t.checkExpect(horizontalWorld.calculateLongestPath(false), 6);

    // due to the random nature of mazes, i have created two proxies for testing
    // these are a collection of cells, that have been updated like a maze
    boolean checkMaze2 = t.checkExpect(testCell1R1.longestPath("Start"), 6)
        && t.checkExpect(testCell3R2.longestPath("Start"), 6)
        && t.checkExpect(cell1R1.longestPath("Start"), 7)
        && t.checkExpect(cell3R3.longestPath("Start"), 8);
    return checkMaze && checkMaze2;
  }

  // solvedManually
  // moveManually()
  void testMoveManually(Tester t) {
    this.initFields();
    for (int i = 0; i < 3; i += 1) {
      maze4.constructMaze();
    }
    maze4.moveManually("down");
    t.checkExpect(maze4.checkNumSeen(), 2);

    // cannot move left or right; only walls, so no change
    maze4.moveManually("left");
    t.checkExpect(maze4.checkNumSeen(), 2);
    maze4.moveManually("right");
    t.checkExpect(maze4.checkNumSeen(), 2);

    // moving up adds start to already seen; need to go down twice
    // moving down once goes over a previously seen cell; does not add it
    maze4.moveManually("up");
    t.checkExpect(maze4.checkNumSeen(), 2);

    maze4.moveManually("down");
    t.checkExpect(maze4.checkNumSeen(), 2);
    maze4.moveManually("down");
    t.checkExpect(maze4.checkNumSeen(), 3);

    // does not move down, but adds start cell in
    horizontalWorld.moveManually("down");
    t.checkExpect(horizontalWorld.checkNumSeen(), 1);
    horizontalWorld.moveManually("up");
    t.checkExpect(horizontalWorld.checkNumSeen(), 1);

    horizontalWorld.moveManually("right");
    t.checkExpect(horizontalWorld.checkNumSeen(), 2);
    horizontalWorld.moveManually("right");
    t.checkExpect(horizontalWorld.checkNumSeen(), 3);

    // does not add previously seen cells, even if you move to them
    horizontalWorld.moveManually("left");
    t.checkExpect(horizontalWorld.checkNumSeen(), 3);
    horizontalWorld.moveManually("right");
    t.checkExpect(horizontalWorld.checkNumSeen(), 3);
    horizontalWorld.moveManually("right");
    t.checkExpect(horizontalWorld.checkNumSeen(), 4);
  }

  // observeMaze() - hard to test, as a method that returns a worldscene
  // -------------------
  // the effects of the private mehtods have been tested;
  // they are all essential helpers to the methods tested above
  // constructHeatMaps() - private
  // drawHeatMap - private can't test
  // showcorrectPath - private (can't test)
  // test drawMaze (can't test)
  // updateMazeScene - private can't test

  // UnionFind Tests

  // test find in the Union/Find data structure -> finding the
  // reference cell from one cell
  boolean testFind(Tester t) {
    this.initFields();
    HashMap<AMazeCell, AMazeCell> references = new HashMap<AMazeCell, AMazeCell>();

    references.put(cell0_0, cell0_0);
    references.put(cell0_1, cell0_0);
    references.put(cell0_2, cell1_0);
    references.put(cell1_0, cell1_0);
    references.put(cell1_1, cell0_1);
    references.put(cell1_2, cell1_2);
    references.put(cell2_0, cell2_0);
    references.put(cell2_1, cell2_2);
    references.put(cell2_2, cell0_2);

    UnionFind uf1 = new UnionFind(references);

    return t.checkExpect(uf1.find(cell0_0), cell0_0) && t.checkExpect(uf1.find(cell0_2), cell1_0)
        && t.checkExpect(uf1.find(cell2_1), cell1_0) && t.checkExpect(uf1.find(cell1_1), cell0_0);
  }

  // testing the union of two cells, so they they will have the same reference
  boolean testUnion(Tester t) {
    this.initFields();
    HashMap<AMazeCell, AMazeCell> references = new HashMap<AMazeCell, AMazeCell>();

    references.put(cell0_0, cell0_0);
    references.put(cell0_1, cell0_0);
    references.put(cell0_2, cell1_0);
    references.put(cell1_0, cell1_0);
    references.put(cell1_1, cell0_1);
    references.put(cell1_2, cell1_2);
    references.put(cell2_0, cell2_0);
    references.put(cell2_1, cell2_2);
    references.put(cell2_2, cell0_2);

    UnionFind uf1 = new UnionFind(references);

    return t.checkExpect(uf1.find(cell0_0), cell0_0) && t.checkExpect(uf1.find(cell0_2), cell1_0)
        && t.checkExpect(uf1.find(cell2_1), cell1_0) && t.checkExpect(uf1.find(cell1_1), cell0_0);
  }

  // Edge UtilTests

  // test inserting an edge into an arrayList of edges, into the correct
  // priority structure (put it in its correct priority given the current set)
  boolean testInsertEdge(Tester t) {
    this.initFields();
    EdgeWeightUtil ewu = new EdgeWeightUtil();
    ArrayList<Edge> edges = new ArrayList<Edge>();

    ewu.insertEdge(edges, edge3);
    ewu.insertEdge(edges, edge0);

    boolean checkFirst = t.checkExpect(edges.get(0), edge0) && t.checkExpect(edges.get(1), edge3)
        && t.checkExpect(edges.size(), 2);

    ewu.insertEdge(edges, edge1);
    ewu.insertEdge(edges, edge2);
    ewu.insertEdge(edges, edge4);

    boolean checkLast = t.checkExpect(edges.get(0), edge0) && t.checkExpect(edges.get(1), edge1)
        && t.checkExpect(edges.get(2), edge2) && t.checkExpect(edges.get(3), edge3)
        && t.checkExpect(edges.get(4), edge4) && t.checkExpect(edges.size(), 5);

    return checkFirst && checkLast;
  }

  // generate a weight for an edge, given the plausible range, a Random object,
  // a preference (0 is none, 1 is vertical, -1 is horizontal), and a boolean
  // of if its a vertical or horizontal edge
  boolean testGenerateWeight(Tester t) {
    this.initFields();
    EdgeWeightUtil ewu = new EdgeWeightUtil();
    Random rand = new Random(5);

    return t.checkExpect(ewu.generateWeight(rand, 10, 0, false), 7)
        && t.checkExpect(ewu.generateWeight(rand, 10, -1, true), 2)
        && t.checkExpect(ewu.generateWeight(rand, 10, -1, false), 0)
        && t.checkExpect(ewu.generateWeight(rand, 10, 1, true), 2)
        && t.checkExpect(ewu.generateWeight(rand, 10, 1, false), 6);
  }

  // World Tests

  // testWorldWidth
  boolean testWorldWidth(Tester t) {
    this.initFields();
    return t.checkExpect(world4.worldWidth(), 1200) && t.checkExpect(world2.worldWidth(), 500)
        && t.checkExpect(world3.worldWidth(), 1000);
  }

  // test WorldHeight
  boolean testWorldHeight(Tester t) {
    this.initFields();
    return t.checkExpect(world4.worldHeight(), 720) && t.checkExpect(world2.worldHeight(), 500)
        && t.checkExpect(world3.worldHeight(), 1000);
  }

  boolean testOnTick(Tester t) {
    this.initFields();

    boolean test1 = t.checkExpect(world2.stillConstructing(), true)
        && t.checkExpect(world3.stillConstructing(), false);

    world3.onTick();

    for (int i = 0; i <= 99; i += 1) {
      world2.onTick();
    }

    world2.onKeyEvent("b");
    world3.onKeyEvent("b");

    boolean test2 = t.checkExpect(world2.stillConstructing(), false)
        && t.checkExpect(world3.stillConstructing(), false)
        && t.checkExpect(world2.solvedYet(), false) && t.checkExpect(world3.solvedYet(), false);

    for (int i = 0; i <= 99; i += 1) {
      world2.onTick();
    }

    for (int i = 0; i <= 25; i += 1) {
      world3.onTick();
    }

    boolean test3 = t.checkExpect(world2.solvedYet(), true)
        && t.checkExpect(world3.solvedYet(), true);

    return test1 && test2 && test3;
  }

  // test onKeyEvent
  boolean testOnKeyEvent(Tester t) {
    // s e n d b m

    this.initFields();

    for (int i = 0; i <= 99; i += 1) {
      world2.onTick();
    }

    boolean test1 = t.checkExpect(world2.getViewMode(), 0) && t.checkExpect(world3.getViewMode(), 0)
        && t.checkExpect(world2.getSolveMode(), 0) && t.checkExpect(world3.getSolveMode(), 0);

    world2.onKeyEvent("s");
    world3.onKeyEvent("s");
    world2.onKeyEvent("d");
    world3.onKeyEvent("d");

    boolean test2 = t.checkExpect(world2.getViewMode(), 1) && t.checkExpect(world3.getViewMode(), 1)
        && t.checkExpect(world2.getSolveMode(), 1) && t.checkExpect(world3.getSolveMode(), 1);

    this.initFields();

    for (int i = 0; i <= 99; i += 1) {
      world2.onTick();
    }

    world2.onKeyEvent("e");
    world3.onKeyEvent("e");
    world2.onKeyEvent("b");
    world3.onKeyEvent("b");

    boolean test3 = t.checkExpect(world2.getViewMode(), -1)
        && t.checkExpect(world3.getViewMode(), -1) && t.checkExpect(world2.getSolveMode(), 2)
        && t.checkExpect(world3.getSolveMode(), 2);

    this.initFields();

    for (int i = 0; i <= 99; i += 1) {
      world2.onTick();
    }

    world2.onKeyEvent("n");
    world3.onKeyEvent("n");
    world2.onKeyEvent("m");
    world3.onKeyEvent("m");

    boolean test4 = t.checkExpect(world2.getViewMode(), 0) && t.checkExpect(world3.getViewMode(), 0)
        && t.checkExpect(world2.getSolveMode(), 3) && t.checkExpect(world3.getSolveMode(), 3);

    return test1 && test2 && test3 && test4;
  }
}