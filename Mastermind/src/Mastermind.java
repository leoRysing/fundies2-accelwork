import javalib.funworld.*;
import javalib.worldimages.*;
import java.awt.Color;
import java.util.Random;
import tester.*;

// represents a list of colors
interface ILoColor {
  /*
   * To check when right colors but wrong positions:
   * Do the method shown in Exam 1 practice:
   * Make a counting class where we count the number of circles of each color then
   * compare to the guess
   * Make sure we black out correct guesses first
   */

  // compare this list of colors (the guess) to the secret list of colors and
  // returns a string describing their relationship
  // Note; this method is designed with this design decision; like in the
  // real game of Mastermind, you should only check your guess if/ when
  // it has the same number of elements as your secret. If this method
  // is called and secret is not the same length, throw an error
  String compare(ILoColor secret);

  // returns a string saying how many elements in this list are the same color and
  // position as the secret and
  // how many colors in this guess are in the wrong position but have the right
  // color compared to the secret list
  // Assume that this list and the secret are of the same length
  String compareHelp(ILoColor secret, int matchesSoFar, ILoColor revisedGuess,
      ILoColor revisedSecret);

  // returns a string saying how many elements in this list are the same color and
  // position as the secret and
  // how many colors in this guess are in the wrong position but have the right
  // color compared to the secret list given the guess to compare is not empty
  // Assume that this list and the secret are of the same length
  String compareCons(ConsLoColor guess, int matchesSoFar, ILoColor revisedGuess,
      ILoColor revisedSecret);

  // checks to see how many colors in this guess are in the wrong position but
  // have
  // the right color compared to the secret list
  int numberNearby(ILoColor secret);

  // get the nth color in the list
  Color getNthColor(int colorNum);

  // get the total number of times this color appears in the list
  int countOfColor(Color toFind);

  // remove all instances of the color _toRemove_ from this list
  ILoColor removeColor(Color toRemove);

  // get the length of the list of colors
  int length();

  // adds the color _toAdd_ into the innermost layer of this ILoColor
  ILoColor insertInside(Color toAdd);

  // draw this list of colors as a palette of circles,
  // in one line
  WorldImage draw(int radius);

  // Adds <numElem> number of elements with color <c> to the end of this list
  ILoColor fill(Color c, int numElem);

  // Checks if all elements in this list are in the other list
  Boolean allContained(ILoColor other);

  // randomly generates a list of colors of length width using colors from this
  // list
  ILoColor randomColors(int width, Random rand);

}

// empty list of colors
class MtLoColor implements ILoColor {

  // compare this list of colors (the guess) to the secret list of colors and
  // returns a string describing their relationship
  public String compare(ILoColor secret) {
    /*
     * TEMPLATE:
     * Param:
     * ... secret ... --ILoColor
     * Methods on params:
     * secret.compare(ILoColor) ... String
     * secret.compareHelp(ILoColor, int, IloColor, IloColor) ... String
     * secret.compareCorrectColor(ILoColor) ... int
     * secret.countOfColor(Color) ... int
     * secret.removeColor(Color) ... ILoColor
     * secret.length() ... int
     * secret.insertInside(Color) ... ILoColor
     * secret.drawPalette() ... WorldImage
     */
    if (secret.length() != 0) {
      throw new IllegalStateException("Secret and guess must be of same length");
    }
    else {
      return "0 correct; 0 nearby";
    }
  }

  // returns a string saying how many elements in this list are the same color and
  // position as the secret and
  // how many colors in this guess are in the wrong position but have the right
  // color compared to the secret list
  // Assume that this list and the secret are of the same length
  public String compareHelp(ILoColor secret, int matchesSoFar, ILoColor revisedGuess,
      ILoColor revisedSecret) {
    return Integer.toString(matchesSoFar) + " correct; "
        + Integer.toString(revisedGuess.numberNearby(revisedSecret)) + " nearby";
  }

  // returns a string saying how many elements in this list are the same color and
  // position as the secret and
  // how many colors in this guess are in the wrong position but have the right
  // color compared to the secret list
  // Assume that this list and the secret are of the same length
  public String compareCons(ConsLoColor guess, int matchesSoFar, ILoColor revisedGuess,
      ILoColor revisedSecret) {
    throw new IllegalStateException("Can't compare Cons on an empty case");
  }

  // checks to see which colors in this guess are in the wrong position but have
  // the right color compared to the secret list
  public int numberNearby(ILoColor secret) {
    return 0;
  }

  // get the nth color in the list
  public Color getNthColor(int colorNum) {
    throw new IllegalStateException("No items in list");
  }

  // get the total number of times this color appears in the list
  public int countOfColor(Color toFind) {
    return 0;
  }

  // remove all instances of the color _toRemove_ from this list
  public ILoColor removeColor(Color toRemove) {
    return new MtLoColor();
  }

  // get the length of the list of colors
  public int length() {
    return 0;
  }

  // adds the color _toAdd_ into the innermost layer of this ILoColor
  public ILoColor insertInside(Color toAdd) {
    return new ConsLoColor(toAdd, new MtLoColor());
  }

  // draw this list of colors as a palette of circles,
  // in one line
  public WorldImage draw(int radius) {
    return new EmptyImage();
  }

  // Adds <numElem> number of elements with color <c> to the end of this list
  public ILoColor fill(Color c, int numElem) {
    if (numElem < 0) {
      throw new IllegalArgumentException("Cannot add a negative number of elements to a list");
    }
    else if (numElem == 0) {
      return this;
    }
    else {
      return this.insertInside(c).fill(c, numElem - 1);
    }
  }

  // Checks if all elements in this list are in the other list
  public Boolean allContained(ILoColor other) {
    return true;
  }

  // randomly generates a list of colors of length width using colors from this
  // list
  public ILoColor randomColors(int width, Random rand) {
    throw new IllegalStateException(
        "Cannot generate a random list of colors from an empty list of colors");
  }
}

// cons case of list of colors
class ConsLoColor implements ILoColor {
  Color first;
  ILoColor rest;

  ConsLoColor(Color first, ILoColor rest) {
    this.first = first;
    this.rest = rest;
  }

  // compare this list of colors (the guess) to the secret list of colors and
  // returns a string describing their relationship
  public String compare(ILoColor secret) {
    if (this.length() != secret.length()) {
      throw new IllegalStateException("Secret and guess must be of same length");
    }
    else {
      return this.compareHelp(secret, 0, new MtLoColor(), new MtLoColor());
    }
  }

  // returns a string saying how many elements in this list are the same color and
  // position as the secret and
  // how many colors in this guess are in the wrong position but have the right
  // color compared to the secret list
  // Assume that this list and the secret are of the same length
  public String compareHelp(ILoColor secret, int matchesSoFar, ILoColor revisedGuess,
      ILoColor revisedSecret) {
    return secret.compareCons(this, matchesSoFar, revisedGuess, revisedSecret);
  }

  // returns a string saying how many elements in this list are the same color and
  // position as the secret and
  // how many colors in this guess are in the wrong position but have the right
  // color compared to the secret list
  // Assume that this list and the secret are of the same length
  public String compareCons(ConsLoColor guess, int matchesSoFar, ILoColor revisedGuess,
      ILoColor revisedSecret) {
    if (this.first.equals(guess.first)) {
      return guess.rest.compareHelp(this.rest, matchesSoFar + 1, revisedGuess, revisedSecret);
    }
    else {
      return guess.rest.compareHelp(this.rest, matchesSoFar, revisedGuess.insertInside(guess.first),
          revisedSecret.insertInside(this.first));
    }
  }

  // checks to see which colors in this guess are in the wrong position but have
  // the right color compared to the secret list
  public int numberNearby(ILoColor secret) {
    return Math.min(this.countOfColor(this.first), secret.countOfColor(this.first))
        + this.rest.removeColor(this.first).numberNearby(secret);
  }

  // get the nth color in the list
  public Color getNthColor(int colorNum) {
    if (colorNum == 0) {
      return this.first;
    }
    else {
      return this.rest.getNthColor(colorNum - 1);
    }
  }

  // get the total number of times this color appears in the list
  public int countOfColor(Color toFind) {
    if (this.first.equals(toFind)) {
      return 1 + this.rest.countOfColor(toFind);
    }
    else {
      return this.rest.countOfColor(toFind);
    }
  }

  // remove all instances of the color _toRemove_ from this list
  public ILoColor removeColor(Color toRemove) {
    if (this.first.equals(toRemove)) {
      return this.rest.removeColor(toRemove);
    }
    else {
      return new ConsLoColor(first, this.rest.removeColor(toRemove));
    }
  }

  // get the length of the list of colors
  public int length() {
    return 1 + this.rest.length();
  }

  // adds the color _toAdd_ into the innermost layer of this ILoColor
  public ILoColor insertInside(Color toAdd) {
    return new ConsLoColor(this.first, this.rest.insertInside(toAdd));
  }

  // draw this list of colors as a palette of circles,
  // in one line
  public WorldImage draw(int radius) {
    return new BesideImage(new CircleImage(radius, OutlineMode.SOLID, this.first),
        (this.rest.draw(radius)));
  }

  // Adds <numElem> number of elements with color <c> to the end of this list
  public ILoColor fill(Color c, int numElem) {
    if (numElem < 0) {
      throw new IllegalArgumentException("Cannot add a negative number of elements to a list");
    }
    else if (numElem == 0) {
      return this;
    }
    else {
      return this.insertInside(c).fill(c, numElem - 1);
    }
  }

  // Checks if all elements in this list are in the other list
  public Boolean allContained(ILoColor other) {
    return other.countOfColor(this.first) > 0 && this.rest.allContained(other);
  }

  // randomly generates a list of colors of length width using colors from this
  // list
  public ILoColor randomColors(int width, Random rand) {
    if (width == 0) {
      return new MtLoColor();
    }
    else {
      int randomSlot = rand.nextInt(this.length());
      return new ConsLoColor(this.getNthColor(randomSlot), this.randomColors(width - 1, rand));
    }
  }
}

class GuessPair {
  ILoColor list;
  String guessOutput;

  // create a typical guess pair
  public GuessPair(ILoColor list, String guessOutput) {
    super();
    this.list = list;
    this.guessOutput = guessOutput;
  }

  // draw this guess pair as a palette of colors (using radius as the size of the
  // colored circes), and a
  // string representing how many circles are correct in the right and wrong place
  WorldImage draw(int radius) {
    return new BesideAlignImage("middle", this.list.draw(radius),
        new FrameImage(new TextImage(this.guessOutput, 18, Color.black), Color.white));
  }
}

// represents a list of guess pairs
interface ILoGuessPair {
  // Draws this list, using the given radius for the size of the circles of color
  WorldImage draw(int radius);

  // Gets the length of this list
  int length();
}

// empty list guess pairs
class MtLoGuessPair implements ILoGuessPair {

  // Draws this list, using the given radius for the size of the circles of color
  public WorldImage draw(int radius) {
    return new RectangleImage(20, 10, OutlineMode.SOLID, Color.WHITE);
  }

  // Gets the length of this empty list
  public int length() {
    return 0;
  }
}

// cons of list of guess pairs
class ConsLoGuessPair implements ILoGuessPair {
  GuessPair first;
  ILoGuessPair rest;

  ConsLoGuessPair(GuessPair first, ILoGuessPair rest) {
    this.first = first;
    this.rest = rest;
  }

  // Draws this list, using the given radius for the size of the circles of color
  public WorldImage draw(int radius) {
    return new AboveAlignImage(AlignModeX.LEFT, this.rest.draw(radius), this.first.draw(radius));
  }

  // Gets the length of this empty list
  public int length() {
    return 1 + this.rest.length();
  }

}

// represents our current world state
class YourWorld extends World {
  static final int radius = 40;
  static final int diameter = radius * 2;
  static final int shiftDown = 10;
  static final int textSize = 24;
  ILoColor colorPalette;
  ILoColor secret;
  int width;
  int numGuesses;
  ILoColor currGuess;
  ILoGuessPair prevGuesses;

  // non random object; pass in a random seed object. Used for debugging
  YourWorld(ILoColor colorPalette, Random rand, int width, int numGuesses, ILoColor currGuess,
      ILoGuessPair prevGuesses) {
    if (currGuess.allContained(colorPalette)) {
      this.colorPalette = colorPalette;
      this.secret = this.colorPalette.randomColors(width, rand);
      this.width = width;
      this.numGuesses = numGuesses;
      this.currGuess = currGuess;
      this.prevGuesses = prevGuesses;
    }
    else {
      throw new IllegalArgumentException("Guess must use colors in the palette");
    }
  }

  // truly random method, which creates a fully random world. Used in practice
  YourWorld(ILoColor colorPalette, int width, int numGuesses, ILoColor currGuess,
      ILoGuessPair prevGuesses) {
    if (currGuess.allContained(colorPalette)) {
      this.colorPalette = colorPalette;
      this.secret = this.colorPalette.randomColors(width, new Random());
      this.width = width;
      this.numGuesses = numGuesses;
      this.currGuess = currGuess;
      this.prevGuesses = prevGuesses;
    }
    else {
      throw new IllegalArgumentException("Guess must use colors in the palette");
    }
  }

  // first round debugging constructor, without any randomization at all
  // it is also used to update the world once a secret has been randomized,
  // if needed
  YourWorld(ILoColor colorPalette, ILoColor secret, int width, int numGuesses, ILoColor currGuess,
      ILoGuessPair prevGuesses) {
    if (currGuess.allContained(colorPalette)) {
      this.colorPalette = colorPalette;
      this.secret = secret;
      this.width = width;
      this.numGuesses = numGuesses;
      this.currGuess = currGuess;
      this.prevGuesses = prevGuesses;
    }
    else {
      throw new IllegalArgumentException("Guess must use colors in the palette");
    }
  }

  // make the world scene for this
  public WorldScene makeScene() {
    WorldImage world = this.drawGame();
    double imgWidth = world.getWidth();
    double imgHeight = world.getHeight();
    return new WorldScene((int) imgWidth, (int) imgHeight + 3 * YourWorld.shiftDown)
        .placeImageXY(world, (int) (imgWidth / 2), (int) (imgHeight / 2) + YourWorld.shiftDown);
  }

  WorldImage drawGame() {
    WorldImage paletteInstruct = new TextImage(
        "Click a Color in the Palette to Add it to Your Guess", YourWorld.textSize, FontStyle.BOLD,
        Color.black);
    WorldImage paletteImg = this.colorPalette.draw(radius);
    WorldImage prevGuessImg = this.prevGuesses.draw(radius);
    ILoColor currGuessFill = this.currGuess.fill(Color.black,
        Math.max(0, width - this.currGuess.length()));
    WorldImage currGuessImg = currGuessFill.draw(radius);
    if (prevGuesses.length() == 0) {
      WorldImage guessesInstruct = new FrameImage(
          new TextImage("Your Current Guess", YourWorld.textSize, FontStyle.BOLD, Color.black),
          Color.white);
      WorldImage combinedImg = new FrameImage(new AboveAlignImage(AlignModeX.LEFT, paletteInstruct,
          paletteImg, guessesInstruct, prevGuessImg, currGuessImg), Color.WHITE);
      return combinedImg;
    }
    else {

      WorldImage prevGuessesInstruct = new FrameImage(
          new TextImage("Your Previous Guesses, With Current Guess at the Bottom", 24,
              FontStyle.BOLD, Color.black),
          Color.white);
      WorldImage prevCombinedImg = new AboveAlignImage(AlignModeX.LEFT, paletteInstruct, paletteImg,
          prevGuessesInstruct, prevGuessImg, currGuessImg);
      return prevCombinedImg;
    }
  }

  // returns the world with an updated currentGuess based on the mouse click
  public YourWorld onMouseClicked(Posn pos) {
    int yMin = YourWorld.textSize;
    int yMax = yMin + YourWorld.diameter;
    int xMax = this.colorPalette.length() * YourWorld.diameter;
    if ((this.currGuess.length() < this.secret.length()) && (pos.y >= yMin) && (pos.y <= yMax)
        && (pos.x <= xMax)) {
      int colorIndex = pos.x / YourWorld.diameter;
      Color thisColor = this.colorPalette.getNthColor(colorIndex);
      return new YourWorld(this.colorPalette, this.secret, this.width, this.numGuesses,
          this.currGuess.insertInside(thisColor), this.prevGuesses);
    }
    else {
      return this;
    }
  }

  // shows a final screen based on the game status
  public WorldScene lastScene(String arg) {
    WorldImage world = this.drawGame();
    WorldImage finalWorld = new AboveAlignImage(AlignModeX.LEFT, world,
        new FrameImage(new TextImage(arg, YourWorld.textSize * 2, Color.DARK_GRAY), Color.WHITE));
    double imgWidth = finalWorld.getWidth();
    double imgHeight = finalWorld.getHeight();
    WorldImage finalWithBack = new OverlayImage(finalWorld,
        new RectangleImage((int) imgWidth, (int) imgHeight, OutlineMode.SOLID, Color.WHITE));
    return new WorldScene((int) imgWidth, (int) imgHeight + 3 * YourWorld.shiftDown).placeImageXY(
        finalWithBack, (int) (imgWidth / 2), (int) (imgHeight / 2) + YourWorld.shiftDown);
  }

  // adjusts the world based on the length of the guess
  // If the number of accumulated guesses is the same as the secret
  // width, get the guess, and
  public World onTick() {
    if (numGuesses == 0) {
      return this.endOfWorld("Out of Guesses");
    }
    else {
      return this;
    }
  }

  public World onKeyEvent(String key) {
    if (key.equals("backspace")) {
      return new YourWorld(this.colorPalette, this.secret, this.width, this.numGuesses,
          new MtLoColor(), this.prevGuesses);
    }
    else if (key.equals("enter") && this.currGuess.length() == width) {
      return this.addGuessToPrev();
    }
    else {
      return this;
    }
  }

  World addGuessToPrev() {
    if (this.currGuess.length() != this.width) {
      throw new IllegalStateException(
          "The guess must be the length of the secret to check its correctness");
    }
    String compare = this.currGuess.compare(this.secret);
    if (Integer.parseInt(compare.substring(0, 1)) == this.width) {
      return this.endOfWorld("YOU WIN");
    }
    else {
      return new YourWorld(this.colorPalette, this.secret, this.width, this.numGuesses - 1,
          new MtLoColor(),
          new ConsLoGuessPair(new GuessPair(this.currGuess, compare), this.prevGuesses));
    }
  }
}

class ExamplesMastermind {
  ILoColor colorPalette = new ConsLoColor(Color.BLUE, new ConsLoColor(Color.CYAN,
      new ConsLoColor(Color.RED, new ConsLoColor(Color.ORANGE, new MtLoColor()))));

  ILoColor colorPaletteLarge = new ConsLoColor(Color.GREEN,
      new ConsLoColor(Color.PINK, new ConsLoColor(Color.BLUE, new ConsLoColor(Color.CYAN,
          new ConsLoColor(Color.RED, new ConsLoColor(Color.ORANGE, new MtLoColor()))))));

  ILoColor mt = new MtLoColor();
  ILoColor colors1 = new ConsLoColor(Color.BLUE, mt);
  ILoColor colors2 = new ConsLoColor(Color.RED, colors1);
  ILoColor colors3 = new ConsLoColor(Color.ORANGE, new ConsLoColor(Color.BLUE, new MtLoColor()));

  ILoColor secret1 = new ConsLoColor(Color.BLUE, mt);
  ILoColor secret2 = new ConsLoColor(Color.ORANGE, secret1);

  ILoColor secret3 = new ConsLoColor(Color.PINK,
      new ConsLoColor(Color.RED, new ConsLoColor(Color.BLUE, mt)));
  ILoColor secret4 = new ConsLoColor(Color.BLUE,
      new ConsLoColor(Color.pink, new ConsLoColor(Color.BLUE, new MtLoColor())));
  ILoColor secret5 = new ConsLoColor(Color.ORANGE, secret4);

  ILoColor colors8 = new ConsLoColor(Color.red,
      new ConsLoColor(Color.orange, new ConsLoColor(Color.green, new MtLoColor())));
  ILoColor colors9 = new ConsLoColor(Color.pink,
      new ConsLoColor(Color.blue, new ConsLoColor(Color.blue, new MtLoColor())));
  ILoColor colors10 = new ConsLoColor(Color.blue, new ConsLoColor(Color.orange,
      new ConsLoColor(Color.blue, new ConsLoColor(Color.pink, new MtLoColor()))));

  ILoColor colors4 = new ConsLoColor(Color.PINK,
      new ConsLoColor(Color.GREEN, new ConsLoColor(Color.CYAN, mt)));
  ILoColor colors5 = new ConsLoColor(Color.PINK,
      new ConsLoColor(Color.BLUE, new ConsLoColor(Color.RED, mt)));
  ILoColor colors7 = new ConsLoColor(Color.RED,
      new ConsLoColor(Color.GREEN, new ConsLoColor(Color.ORANGE, new MtLoColor())));

  // 1 fully correct 1 wrong
  YourWorld world1 = new YourWorld(colorPalette, secret2, 2, 4, colors2, new MtLoGuessPair());

  // all fully correct
  YourWorld world2 = new YourWorld(colorPalette, colors2, 2, 4, colors2, new MtLoGuessPair());

  // 2 wrong position but right color
  YourWorld world3 = new YourWorld(colorPalette, secret2, 2, 4, colors3, new MtLoGuessPair());

  // 1 fully correct; 2 right color
  YourWorld world4 = new YourWorld(colorPaletteLarge, secret3, 3, 5, colors5,
      new ConsLoGuessPair(new GuessPair(colors4, "1 correct; 0 nearby"), new MtLoGuessPair()));

  // all wrong
  YourWorld world5 = new YourWorld(colorPaletteLarge, secret3, 3, 6, colors7, new MtLoGuessPair());

  // no current guess
  YourWorld world6 = new YourWorld(colorPaletteLarge, secret3, 3, 6, mt, new MtLoGuessPair());

  // guess being built
  YourWorld world7 = new YourWorld(colorPaletteLarge, secret3, 3, 6, colors2, new MtLoGuessPair());

  // last guess about to be checked and wrong
  YourWorld world8 = new YourWorld(colorPaletteLarge, secret2, 2, 1, colors3,
      new ConsLoGuessPair(new GuessPair(colors3, "0 correct; 2 nearby"),
          new ConsLoGuessPair(new GuessPair(colors3, "0 correct; 2 nearby"), new MtLoGuessPair())));

  // repeat colors in secret with guess completely wrong
  YourWorld world9 = new YourWorld(colorPaletteLarge, secret4, 3, 6, colors8, new MtLoGuessPair());

  // repeat colors in secret with guess having 2 colors close 1 right
  YourWorld world10 = new YourWorld(colorPaletteLarge, secret4, 3, 6, colors9, new MtLoGuessPair());

  // repeats colors in secret with fully right guess
  YourWorld world11 = new YourWorld(colorPaletteLarge, secret4, 3, 6, secret4, new MtLoGuessPair());

  // repeats colors in secret with guess having all colors nearby
  YourWorld world12 = new YourWorld(colorPaletteLarge, secret5, 4, 6, colors10,
      new MtLoGuessPair());

  // repeats colors in secret with fully right guess
  YourWorld world14 = new YourWorld(colorPaletteLarge, secret4, 3, 0, secret4, new MtLoGuessPair());

  // example with one current color in the list of guesses
  // repeat colors in secret with guess completely wrong
  YourWorld world15 = new YourWorld(colorPaletteLarge, secret4, 3, 6,
      new ConsLoColor(Color.GREEN, new MtLoColor()), new MtLoGuessPair());

  boolean testBigBang(Tester t) {
    int WIDTH = 1000;
    int HEIGHT = 1000;
    return world6.bigBang(WIDTH, HEIGHT, 0.1);
  }

  ILoColor colors11 = new ConsLoColor(Color.BLUE,
      new ConsLoColor(Color.CYAN, new ConsLoColor(Color.ORANGE, colors7)));
  ILoGuessPair guessPairList1 = new ConsLoGuessPair(new GuessPair(colors3, "0 correct; 2 nearby"),
      new ConsLoGuessPair(new GuessPair(colors3, "0 correct; 2 nearby"), new MtLoGuessPair()));
  ILoGuessPair guessPairList2 = new ConsLoGuessPair(new GuessPair(colors2, "1 correct; 0 nearby"),
      new ConsLoGuessPair(new GuessPair(colors3, "2 correct; 0 nearby"),
          new ConsLoGuessPair(new GuessPair(colors5, "1 correct; 2 nearby"), new MtLoGuessPair())));

  GuessPair gP1 = new GuessPair(colors2, "1 correct; 0 nearby");
  GuessPair gP2 = new GuessPair(colors11, colors11.compare(colors11));
  ILoColor colors7wrong = new ConsLoColor(Color.BLUE,
      new ConsLoColor(Color.CYAN, new ConsLoColor(Color.YELLOW, new MtLoColor())));

  // check draw (on ConLoGuessPair, ILoCOlor, GuessPair)
  // simple check -> see if width or height is expected
  boolean testDraw(Tester t) {
    // ConsLoGuessPair
    return t.checkExpect(Math.abs(new MtLoGuessPair().draw(40).getHeight() - 10.00) < 0.01, true)
        && t.checkExpect(Math.abs(this.guessPairList1.draw(40).getHeight() - 170.0) < 0.01, true)
        && t.checkExpect(Math.abs(this.guessPairList2.draw(40).getHeight() - 250.0) < 0.01, true)
        // ILoColor
        && t.checkExpect(Math.abs(this.mt.draw(40).getWidth() - 0.0) < 0.01, true)
        && t.checkExpect(Math.abs(this.colors5.draw(40).getWidth() - 240.0) < 0.01, true)
        && t.checkExpect(Math.abs(this.colors11.draw(40).getWidth() - 480.0) < 0.01, true)
        // GuessPair
        && t.checkExpect(Math.abs(this.gP1.draw(40).getWidth() - 308.0) < 0.01, true)
        && t.checkExpect(Math.abs(this.gP2.draw(40).getWidth() - 630.0) < 0.01, true);
  }

  // check allFill
  boolean testFill(Tester t) {
    return t.checkExpect(colorPalette.fill(Color.BLACK, 0), colorPalette)
        && t.checkExpect(colors5.fill(Color.BLACK, 2),
            new ConsLoColor(Color.PINK, new ConsLoColor(Color.BLUE,
                new ConsLoColor(Color.RED,
                    new ConsLoColor(Color.BLACK, new ConsLoColor(Color.BLACK, new MtLoColor()))))))
        && t.checkException(
            new IllegalArgumentException("Cannot add a negative number of elements to a list"),
            colors11, "fill", Color.BLACK, -2)
        && t.checkExpect(colors3.fill(Color.WHITE, 4), new ConsLoColor(Color.ORANGE,
            new ConsLoColor(Color.BLUE, new ConsLoColor(Color.WHITE, new ConsLoColor(Color.WHITE,
                new ConsLoColor(Color.WHITE, new ConsLoColor(Color.WHITE, new MtLoColor())))))));
  }

  // check allContained
  boolean testAllContained(Tester t) {
    ILoColor allB = new ConsLoColor(Color.BLACK, new ConsLoColor(Color.BLACK,
        new ConsLoColor(Color.BLACK, new ConsLoColor(Color.BLACK, new MtLoColor()))));
    ILoColor someB = new ConsLoColor(Color.BLUE, new ConsLoColor(Color.BLACK, new MtLoColor()));
    return t.checkExpect(colors3.allContained(colorPaletteLarge), true)
        && t.checkExpect(colors11.allContained(colorPaletteLarge), true)
        && t.checkExpect(someB.allContained(colorPaletteLarge), false)
        && t.checkExpect(allB.allContained(colorPalette), false);
  }

  // check insertInside
  boolean testInsertInside(Tester t) {
    return t.checkExpect(mt.insertInside(Color.GREEN), new ConsLoColor(Color.green, mt))
        && t.checkExpect(colors11.insertInside(Color.CYAN),
            new ConsLoColor(Color.BLUE,
                new ConsLoColor(Color.CYAN,
                    new ConsLoColor(Color.ORANGE,
                        new ConsLoColor(Color.RED,
                            new ConsLoColor(Color.GREEN,
                                new ConsLoColor(Color.ORANGE,
                                    new ConsLoColor(Color.CYAN, new MtLoColor()))))))))
        && t.checkExpect(colors3.insertInside(Color.RED), new ConsLoColor(Color.ORANGE,
            new ConsLoColor(Color.BLUE, new ConsLoColor(Color.red, new MtLoColor()))));
  }

  // check removeColor
  boolean testRemoveColor(Tester t) {
    ILoColor allB = new ConsLoColor(Color.BLACK, new ConsLoColor(Color.BLACK,
        new ConsLoColor(Color.BLACK, new ConsLoColor(Color.BLACK, new MtLoColor()))));
    return t.checkExpect(allB.removeColor(Color.BLACK), new MtLoColor())
        && t.checkExpect(
            colors7.removeColor(Color.ORANGE), new ConsLoColor(Color.RED,
                new ConsLoColor(Color.GREEN, new MtLoColor())))
        && t.checkExpect(colors7.removeColor(Color.RED),
            new ConsLoColor(Color.GREEN, new ConsLoColor(Color.ORANGE, new MtLoColor())))
        && t.checkExpect(colors7.removeColor(Color.GREEN), new ConsLoColor(Color.RED,
            new ConsLoColor(Color.ORANGE, new MtLoColor())))
        && t.checkExpect(
            new ConsLoColor(Color.BLUE,
                new ConsLoColor(Color.GREEN,
                    new ConsLoColor(Color.RED, new ConsLoColor(Color.GREEN, new MtLoColor()))))
                        .removeColor(Color.GREEN),
            new ConsLoColor(Color.BLUE, new ConsLoColor(Color.RED, new MtLoColor())));
  }

  // check countOfColor
  boolean testCountOfColor(Tester t) {
    ILoColor allB = new ConsLoColor(Color.BLACK, new ConsLoColor(Color.BLACK,
        new ConsLoColor(Color.BLACK, new ConsLoColor(Color.BLACK, new MtLoColor()))));
    return t.checkExpect(allB.countOfColor(Color.BLACK), 4)
        && t.checkExpect(allB.countOfColor(Color.BLUE), 0)
        && t.checkExpect(colors3.countOfColor(Color.BLUE), 1)
        && t.checkExpect(colors11.countOfColor(Color.CYAN), 1)
        && t.checkExpect(colors5.countOfColor(Color.BLACK), 0)
        && t.checkExpect(colors10.countOfColor(Color.BLUE), 2)
        && t.checkExpect(colorPaletteLarge.countOfColor(Color.green), 1)
        && t.checkExpect(colorPaletteLarge.countOfColor(Color.green), 1);
  }

  // to test the getNthColor method
  boolean testGetNthColor(Tester t) {
    return t.checkException(new IllegalStateException("No items in list"), new MtLoColor(),
        "getNthColor", 0)
        && t.checkException(new IllegalStateException("No items in list"), colorPalette,
            "getNthColor", -1)
        && t.checkException(new IllegalStateException("No items in list"), colorPalette,
            "getNthColor", 4)
        && t.checkExpect(colorPaletteLarge.getNthColor(0), Color.green)
        && t.checkExpect(colorPaletteLarge.getNthColor(5), Color.ORANGE)
        && t.checkExpect(colors11.getNthColor(4), Color.GREEN)
        && t.checkExpect(colors11.getNthColor(5), Color.ORANGE);
  }

  // to test individual comparisons of a guess and a secret
  boolean testCompareSecret(Tester t) {
    return t.checkExpect(colors2.compare(secret2), "1 correct; 0 nearby")
        && t.checkExpect(colors3.compare(secret2), "2 correct; 0 nearby")
        && t.checkExpect(colors5.compare(secret3), "1 correct; 2 nearby")
        && t.checkExpect(colors7.compare(secret3), "0 correct; 1 nearby")
        && t.checkExpect(colors2.compare(secret2), "1 correct; 0 nearby")
        && t.checkExpect(colors10.compare(secret5), "0 correct; 4 nearby")
        && t.checkExpect(colors8.compare(secret4), "0 correct; 0 nearby")
        && t.checkExpect(colors9.compare(secret4), "1 correct; 2 nearby")
        && t.checkExpect(secret4.compare(secret4), "3 correct; 0 nearby")
        && t.checkExpect(colors10.compare(secret5), "0 correct; 4 nearby")
        && t.checkExpect(colors7.compare(colors7wrong), "0 correct; 0 nearby")
        && t.checkException(new IllegalStateException("Secret and guess must be of same length"),
            colors5, "compare", secret2);
  }

  // check NumberNearby()
  boolean testNumberNearby(Tester t) {
    return t.checkExpect(colors2.numberNearby(secret2), 1)
        && t.checkExpect(colors5.numberNearby(secret3), 3)
        && t.checkExpect(colors11.numberNearby(colors11), 6)
        && t.checkExpect(new ConsLoColor(Color.PINK, new ConsLoColor(Color.BLUE, mt))
            .numberNearby(new ConsLoColor(Color.BLUE, new ConsLoColor(Color.PINK, mt))), 2)
        && t.checkExpect(mt.numberNearby(mt), 0)
        && t.checkExpect(colors10.numberNearby(secret5), 4);
  }

  // check compareHelp
  boolean testCompareHelp(Tester t) {
    return t.checkExpect(colors2.compareHelp(secret2, 0, mt, mt), "1 correct; 0 nearby")
        && t.checkExpect(colors3.compareHelp(secret2, 0, mt, mt), "2 correct; 0 nearby")
        && t.checkExpect(colors5.compareHelp(secret3, 0, mt, mt), "1 correct; 2 nearby")
        && t.checkExpect(colors7.compareHelp(secret3, 3, mt, mt), "3 correct; 1 nearby")
        && t.checkExpect(colors10.compareHelp(secret5, 0, colors2, secret2), "0 correct; 5 nearby")
        && t.checkExpect(colors8.compareHelp(secret4, 2, colors2, secret2), "2 correct; 2 nearby")
        && t.checkExpect(colors9.compareHelp(secret4, 3, colors10, secret5), "4 correct; 6 nearby")
        && t.checkExpect(mt.compareHelp(mt, 0, mt, mt), "0 correct; 0 nearby")
        && t.checkExpect(mt.compareHelp(mt, 0, colors2, secret2), "0 correct; 1 nearby");
  }

  // Tests the compareCons method on ILoColor
  boolean testCompareCons(Tester t) {
    return t.checkExpect(secret2.compareCons((ConsLoColor) colors2, 0, mt, mt),
        "1 correct; 0 nearby")
        && t.checkExpect(secret2.compareCons((ConsLoColor) colors3, 0, mt, mt),
            "2 correct; 0 nearby")
        && t.checkExpect(secret3.compareCons((ConsLoColor) colors5, 0, mt, mt),
            "1 correct; 2 nearby")
        && t.checkExpect(secret3.compareCons((ConsLoColor) colors7, 3, mt, mt),
            "3 correct; 1 nearby")
        && t.checkExpect(secret5.compareCons((ConsLoColor) colors10, 0, colors2, secret2),
            "0 correct; 5 nearby")
        && t.checkExpect(secret4.compareCons((ConsLoColor) colors8, 2, colors2, secret2),
            "2 correct; 2 nearby")
        && t.checkExpect(secret4.compareCons((ConsLoColor) colors9, 3, colors10, secret5),
            "4 correct; 6 nearby")
        && t.checkException(new IllegalStateException("Can't compare Cons on an empty case"), mt,
            "compareCons", (ConsLoColor) colors2, 0, colors2, secret2);
  }

  // to test functionality of on tick handling guesses
  boolean testOnTick(Tester t) {
    ILoColor colors11 = new ConsLoColor(Color.black, colors9);

    return t.checkExpect(world1.onTick(), world1)
        && t.checkExpect(world2.onTick(), world2.endOfWorld("YOU WIN!"))
        && t.checkExpect(world14.onTick(), world14.endOfWorld("Out of Guesses"))
        && t.checkExpect(world11.onTick(), world11.endOfWorld("YOU WIN!"))
        // test the creation of a world where the guess is made of colors not in the
        // palette
        && t.checkConstructorException(
            new IllegalArgumentException("Guess must use colors in the palette"), "YourWorld",
            colorPaletteLarge, secret5, 4, 6, colors11, new MtLoGuessPair());
  }

  // to test clicking the mouse on a circle
  boolean testOnClick(Tester t) {
    return t.checkExpect(world1.onMouseClicked(new Posn(160, 24)),
        new YourWorld(colorPalette, secret2, 2, 4,
            new ConsLoColor(Color.RED, new ConsLoColor(Color.BLUE, new MtLoColor())),
            new MtLoGuessPair()))
        && t.checkExpect(world1.onMouseClicked(new Posn(80, 24)),
            new YourWorld(colorPalette, secret2, 2, 4,
                new ConsLoColor(Color.RED, new ConsLoColor(Color.BLUE, new MtLoColor())),
                new MtLoGuessPair()))
        && t.checkExpect(world15.onMouseClicked(new Posn(0, 24)),
            new YourWorld(colorPaletteLarge, secret4, 3, 6,
                new ConsLoColor(Color.GREEN, new ConsLoColor(Color.GREEN, new MtLoColor())),
                new MtLoGuessPair()))
        && t.checkExpect(world15.onMouseClicked(new Posn(40, 24)),
            new YourWorld(colorPaletteLarge, secret4, 3, 6,
                new ConsLoColor(Color.GREEN, new ConsLoColor(Color.GREEN, new MtLoColor())),
                new MtLoGuessPair()))
        && t.checkExpect(world15.onMouseClicked(new Posn(154, 24)),
            new YourWorld(colorPaletteLarge, secret4, 3, 6,
                new ConsLoColor(Color.GREEN, new ConsLoColor(Color.PINK, new MtLoColor())),
                new MtLoGuessPair()));
  }

  // testing the length method for list of color
  boolean testLength(Tester t) {
    return t.checkExpect(mt.length(), 0) && t.checkExpect(colors1.length(), 1)
        && t.checkExpect(colors2.length(), 2) && t.checkExpect(colors3.length(), 2)
        && t.checkExpect(secret2.length(), 2) && t.checkExpect(colors7.length(), 3)
        && t.checkExpect(colors4.length(), 3)
        && t.checkExpect(new ConsLoColor(Color.BLACK,
            new ConsLoColor(Color.BLACK, new ConsLoColor(Color.BLACK,
                new ConsLoColor(Color.BLACK, new ConsLoColor(Color.BLACK,
                    new ConsLoColor(Color.BLACK, new ConsLoColor(Color.BLACK, new MtLoColor())))))))
                        .length(),
            7);
  }

  // to test the drawing on the World
  boolean testDrawGame(Tester t) {
    return t.checkExpect(Math.abs(world1.drawGame().getHeight() - 200) < 0.01, true)
        && t.checkExpect(Math.abs(world8.drawGame().getHeight() - 360) < 0.01, true);
  }

  // Tests the onKey method in YourWorld
  boolean testOnKey(Tester t) {
    return t.checkExpect(world1.onKeyEvent(""), world1)
        && t.checkExpect(world8.onKeyEvent("space"), world8)
        && t.checkExpect(world2.onKeyEvent("backspace"),
            new YourWorld(colorPalette, colors2, 2, 4, new MtLoColor(), new MtLoGuessPair()))
        && t.checkExpect(world3.onKeyEvent("backspace"),
            new YourWorld(colorPalette, secret2, 2, 4, new MtLoColor(), new MtLoGuessPair()))
        && t.checkExpect(world6.onKeyEvent("backspace"),
            new YourWorld(colorPaletteLarge, secret3, 3, 6, mt, new MtLoGuessPair()))
        && t.checkExpect(world6.onKeyEvent("enter"), world6)
        && t.checkExpect(world1.onKeyEvent("enter"),
            new YourWorld(colorPalette, secret2, 2, 3, new MtLoColor(), new ConsLoGuessPair(
                new GuessPair(colors2, "1 correct; 0 nearby"), new MtLoGuessPair())))
        && t.checkExpect(world2.onKeyEvent("enter"), world2);
  }

  // Tests the addGuesstoPrev method in YourWorld
  boolean testAddGuess(Tester t) {
    return t.checkException(
        new IllegalStateException(
            "The guess must be the length of the secret to check its correctness"),
        world6, "addGuessToPrev")
        && t.checkExpect(world1.addGuessToPrev(),
            new YourWorld(colorPalette, secret2, 2, 3, new MtLoColor(),
                new ConsLoGuessPair(new GuessPair(colors2, "1 correct; 0 nearby"),
                    new MtLoGuessPair())))
        && t.checkExpect(world11.addGuessToPrev(), world11)
        && t.checkExpect(world12.addGuessToPrev(),
            new YourWorld(colorPaletteLarge, secret5, 4, 5, new MtLoColor(), new ConsLoGuessPair(
                new GuessPair(colors10, "0 correct; 4 nearby"), new MtLoGuessPair())));
  }

  // Tests the randomColors method in ILoC
  boolean testRandomColors(Tester t) {
    return t.checkException(
        new IllegalStateException(
            "Cannot generate a random list of colors from an empty list of colors"),
        mt, "randomColors", 5, new Random(1))
        && t.checkExpect(colorPalette.randomColors(0, new Random(1)), new MtLoColor())
        && t.checkExpect(colorPalette.randomColors(1, new Random(1)).length(), 1)
        && t.checkExpect(colorPaletteLarge.randomColors(18, new Random(5)).length(), 18);
  }

  // tests the length method on IListColor
  boolean testLengthColor(Tester t) {
    return t.checkExpect(mt.length(), 0) && t.checkExpect(colors1.length(), 1)
        && t.checkExpect(colors2.length(), 2) && t.checkExpect(colors3.length(), 2)
        && t.checkExpect(secret2.length(), 2) && t.checkExpect(colors7.length(), 3)
        && t.checkExpect(colors4.length(), 3)
        && t.checkExpect(new ConsLoColor(Color.BLACK,
            new ConsLoColor(Color.BLACK, new ConsLoColor(Color.BLACK,
                new ConsLoColor(Color.BLACK, new ConsLoColor(Color.BLACK,
                    new ConsLoColor(Color.BLACK, new ConsLoColor(Color.BLACK, new MtLoColor())))))))
                        .length(),
            7);
  }

  // tests the length method on IListGuessPair
  boolean testLengthGuess(Tester t) {
    return t.checkExpect(new MtLoGuessPair().length(), 0)
        && t.checkExpect(new ConsLoGuessPair(new GuessPair(colors3, "0 correct; 2 nearby"),
            new ConsLoGuessPair(new GuessPair(colors3, "0 correct; 2 nearby"), new MtLoGuessPair()))
                .length(),
            2)
        && t.checkExpect(new ConsLoGuessPair(new GuessPair(colors1, "3 correct; 82 nearby"),
            new ConsLoGuessPair(new GuessPair(colors3, "0 correct; 2 nearby"), new MtLoGuessPair()))
                .length(),
            2)
        && t.checkExpect(new ConsLoGuessPair(new GuessPair(colors1, "0 correct; 0 nearby"),
            new ConsLoGuessPair(new GuessPair(colors1, "0 correct; 0 nearby"),
                new ConsLoGuessPair(new GuessPair(colors1, "0 correct; 0 nearby"),
                    new ConsLoGuessPair(new GuessPair(colors1, "0 correct; 0 nearby"),
                        new ConsLoGuessPair(new GuessPair(colors1, "0 correct; 0 nearby"),
                            new ConsLoGuessPair(new GuessPair(colors3, "6 correct; 0 nearby"),
                                new MtLoGuessPair())))))).length(),
            6);
  }
}
