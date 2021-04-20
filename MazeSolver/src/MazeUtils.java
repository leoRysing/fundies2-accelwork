import java.util.*;

import javalib.worldimages.WorldImage;

// represents a util for adding edge lists
class EdgeWeightUtil {
  // effect; add this edge into the correct place. Put the edge
  // after the last edge with a higher or equal priority to itself
  // (compareTo returns a negative or 0 number)
  void insertEdge(ArrayList<Edge> arr, Edge edge) {
    // loop through all edges in the list; when you encounter the first edge
    // with a larger weight/ lower priority than itself
    for (int i = 0; i < arr.size(); i += 1) {
      if (edge.compareWeights(arr.get(i)) < 0) {
        arr.add(i, edge);
        return;
      }
    }
    arr.add(edge);
  }

  // 0 = no preference
  // -1 = horizontal preference
  // 1 = vertical preference
  // Generates a weight for the edge based on whether or not the edge is vertical
  // and what the preference is, either horizontal or vertical corridors
  int generateWeight(Random rand, int range, int preference, boolean isVertical) {
    if (preference > 1 || preference < -1) {
      throw new IllegalArgumentException("Preference must be 1, -1, or 0");
    }
    else if ((preference == 1 && isVertical) || (preference == -1 && !isVertical)) {
      return rand.nextInt((int) Math.ceil(range / 3.0));
    }
    else {
      return rand.nextInt(range);
    }
  }
}

// represents a structure for union findings, with representatives/ block
class UnionFind {
  HashMap<AMazeCell, AMazeCell> representatives;

  // Generates a union find with the given Hash Map
  UnionFind(HashMap<AMazeCell, AMazeCell> representatives) {
    this.representatives = representatives;
  }

  // Finds the end representative of a given cell
  AMazeCell find(AMazeCell start) {
    AMazeCell reference = start;
    while (!reference.equals(representatives.get(reference))) {
      reference = representatives.get(reference);
    }
    return reference;
  }

  // given another cell, sets the representative of one cell
  // to the representative of another
  void union(AMazeCell cell1, AMazeCell cell2) {
    // ToDo: Update the references in the cells themselves
    if (this.countLevel(cell1) < this.countLevel(cell2)) {
      representatives.put(this.find(cell1), this.find(cell2));
    }
    else {
      representatives.put(this.find(cell2), this.find(cell1));
    }
  }

  // Counts the number of cells before getting to the representatives
  // Used for optimizing union, so that small trees get added to big trees
  private int countLevel(AMazeCell start) {
    AMazeCell reference = start;
    int levelCount = 0;
    while (!reference.equals(representatives.get(reference))) {
      reference = representatives.get(reference);
      levelCount += 1;
    }
    return levelCount;
  }
}

//Image util used for moving the pinhole in the recursive drawing method
class ImageUtil {

  // Moves the pinhole of the given image based on the cell size and direction
  WorldImage movePinhole(WorldImage image, String dir, int cellSize) {
    if (dir.equalsIgnoreCase("Up")) {
      image = image.movePinhole(0, cellSize / 2);
    }
    else if (dir.equalsIgnoreCase("Down")) {
      image = image.movePinhole(0, -cellSize / 2);
    }
    else if (dir.equalsIgnoreCase("Left")) {
      image = image.movePinhole(cellSize / 2, 0);
    }
    else if (dir.equalsIgnoreCase("Right")) {
      image = image.movePinhole(-cellSize / 2, 0);
    }
    return image;
  }
}

//Represents a mutable collection of items
interface ICollection<T> {
  // Is this collection empty?
  boolean isEmpty();

  // EFFECT: adds the item to the collection
  void add(T item);

  // Returns the first item of the collection
  // EFFECT: removes that first item
  T remove();
}

// Represents a Stack collection
class Stack<T> implements ICollection<T> {
  Deque<T> contents;

  // Initializes an empty Stack
  Stack() {
    this.contents = new ArrayDeque<T>();
  }

  // Returns whether or not the stack is empty
  public boolean isEmpty() {
    return this.contents.isEmpty();
  }

  // Removes the first item in a Stack
  public T remove() {
    return this.contents.removeFirst();
  }

  // Adds an item on the top of a Stack
  public void add(T item) {
    this.contents.addFirst(item);
  }
}

// Represents a Queue of items
class Queue<T> implements ICollection<T> {
  Deque<T> contents;

  // Initializes an empty Queue
  Queue() {
    this.contents = new ArrayDeque<T>();
  }

  // Returns whether or not the Queue is empty
  public boolean isEmpty() {
    return this.contents.isEmpty();
  }

  // Removes the first item of the Queue
  public T remove() {
    return this.contents.removeFirst();
  }

  // Adds an item at the end of the Queue
  public void add(T item) {
    this.contents.addLast(item); // NOTE: Different from Stack!
  }
}