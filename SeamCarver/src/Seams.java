import java.util.ArrayList;
import java.util.Random;
import tester.*;
import javalib.impworld.*;
import javalib.worldimages.*;
import java.awt.Color;

// to do; add horizontal removePixel methods (thre directions, 0 and 1 input)
// complex on tick
// horizontal removeSeam method, horizontal updateSentinals
// (potentially) add an on key method for xtra cred

// goal; 2D deque

// represents a pixel in an image
abstract class APixel {
  Color color;
  // some representation of the 4 neighbors
  APixel left;
  APixel right;
  APixel up;
  APixel down;

  // brightness of the specific pixel
  double brightness;

  // update the vertical reference of this pixel, in a double sided way,
  // such that it and its reference pixel point to each other
  // pixel
  void updateVerticalRef(boolean isUp, APixel ref) {
    if (isUp) {
      this.up = ref;
      ref.down = this;
    }
    else {
      this.down = ref;
      ref.up = this;
    }
  }

  // update the horizontal reference of this pixel, in a double sided way,
  // such that it and its reference pixel point to each other
  // pixel
  void updateHorizontalRef(boolean isLeft, APixel ref) {
    if (isLeft) {
      this.left = ref;
      ref.right = this;
    }
    else {
      this.right = ref;
      ref.left = this;
    }
  }

  // calculate the horizontal energy of this pixel
  double getHorizontalE() {
    APixel left = this.left;
    APixel right = this.right;
    return (left.up.brightness + 2 * (left.brightness) + left.down.brightness)
        - (right.up.brightness + 2 * (right.brightness) + right.down.brightness);
  }

  // calculate the vertical energy of this pixel
  double getVerticalE() {
    APixel up = this.up;
    APixel down = this.down;
    return (up.left.brightness + 2 * (up.brightness) + up.right.brightness)
        - (down.left.brightness + 2 * (down.brightness) + down.right.brightness);
  }

  // calculate the total energy of this pixel, based on
  // its horizontal and vertical energy (from its neighbors)
  double observeTotalE() {
    double energy = Math
        .sqrt(Math.pow(this.getHorizontalE(), 2) + Math.pow(this.getVerticalE(), 2));
    return energy;
  }

  // is this pixel the same as the other?
  // note; extentional equality
  // check that their colors are equal, and that if you start on a first call,
  // also check the colors of its 4 neighbors.
  boolean samePixel(APixel other, boolean secondCall) {
    if (secondCall) {
      return this.color.equals(other.color);
    }
    else {
      return this.color.equals(other.color) && this.left.samePixel(other.left, true)
          && this.right.samePixel(other.right, true) && this.up.samePixel(other.up, true)
          && this.down.samePixel(other.down, true);
    }
  }

  // effect; remove the pixel in a straight line pattern, and adjust the
  // connections as such
  void removeStraightLine() {
    throw new IllegalStateException("Cannot remove a sentinel via removeStraightLine");
  }

  // effect; remove the pixel from a right diagonal pattern, and adjust the
  // connections as such
  void removeRightDiagonal() {
    throw new IllegalStateException("Cannot remove a sentinel via removeRightDiagonal");
  }

  // effect; remove the pixel from a left diagonal pattern, and adjust the
  // connections as such
  void removeLeftDiagonal() {
    throw new IllegalStateException("Cannot remove a sentinel via removeLeftDiagonal");
  }

  // effect; remove the pixel horizontally from a right pattern, and adjust the
  // connections as such
  void removeRight() {
    throw new IllegalStateException("Cannot remove a sentinel via removeRight");
  }

  // effect; remove the pixel horizontally moving down left pattern, and adjust
  // the
  // connections as such
  void removeDownLeft() {
    throw new IllegalStateException("Cannot remove a sentinel via removeDownLeft");
  }

  // effect; remove the pixel horizontally moving down left pattern, and adjust
  // the
  // connections as such
  void removeUpLeft() {
    throw new IllegalStateException("Cannot remove a sentinel via removeUpLeft");
  }

  // effect; update the required sentinals based on the <distTraveled>,
  // magnitude corresponding to displacement, and sign corresponding to direction
  // traveled
  abstract void updateSents(boolean isVertical, int distTraveled);

  // effect; remove this singular pixel, without reference to a previous direction
  // return the distance traveled to between the pixels (0)
  int removePixel(boolean isVertical) {
    // used as a base case here
    throw new IllegalStateException("Cannot remove a Sentinel");
  }

  // effect; remove this singular pixel, with reference to a previous pixel <prev>
  // return the distance traveled to between the pixels
  // Assume; the two pixels are immediatly reachable, from an straight or diagonal
  // path
  int removePixel(boolean isVertical, APixel prev) {
    // used as a base case here
    throw new IllegalStateException("Cannot remove a Sentinel");
  }

  // effect; remove the top sentinal
  // specifics are written in the classes
  abstract void removeTopSent(boolean isLastImgRow);

  // effect; remove the top sentinal
  // specifics are written in the classes
  abstract void removeSideSent(boolean isLastImgRow);

  abstract void makeRed();
}

// represents a corner sentinel pixel (border pixel)
class CornerSent extends APixel {

  // create a new Corner sentinel
  CornerSent() {
    this.color = Color.BLACK;
    this.brightness = 0;
    this.left = this;
    this.right = this;
    this.up = this;
    this.down = this;
  }

  // effect; remove the top sentinal
  // a corner sentinal should never be accessed in this way directly
  public void removeTopSent(boolean isLastImgRow) {
    throw new IllegalStateException("Corner sent is not a top sentinel");
  }

  // effect; remove the top sentinal
  // a corner sentinal should never be accessed in this way directly
  public void removeSideSent(boolean isLastImgRow) {
    throw new IllegalStateException("Corner sent is not a top sentinel");
  }

  // -----
  // effect; update the required sentinals based on the <distTraveled>,
  // magnitude corresponding to displacement, and sign corresponding to direction
  // traveled
  // (note; nothing needs to be done in a corner sent
  void updateSents(boolean isVertical, int distance) {
    // nothing:D
  }

  void makeRed() {
    throw new IllegalStateException("Cannot make a Sentinel red");
  }
}

// represents a column sentinel pixal (border pixel)
class TopSent extends APixel {

  // create a top row sentinel
  TopSent(APixel left, APixel right) {
    this.color = Color.BLACK;
    this.brightness = 0;
    this.updateHorizontalRef(true, left);
    this.updateHorizontalRef(false, right);
    this.up = this;
    this.down = this;
  }

  // effect; remove this top sentinal, only if it is not treated as the last image
  // row
  // (<isLastImgRow> must be false). If this case is met, remove the references to
  // this sentinel
  // from the left and right pixels
  public void removeTopSent(boolean isLastImgRow) {
    if (isLastImgRow) {
      throw new IllegalStateException("A top sentinel is not an image pixel");
    }
    else {
      this.left.updateHorizontalRef(false, this.right);
    }
  }

  // ------
  // effect; update the required sentinals based on the <distTraveled>,
  // magnitude corresponding to displacement, and sign corresponding to direction
  // traveled. A positive number means distance was right, negative means distance
  // is left,
  // and zero is no distance/ direction
  void updateSents(boolean isVertical, int distance) {
    if (!isVertical) {
      throw new IllegalStateException("updating wrong sentinals (should be updating sides)");
    }
    if (distance >= 1) {
      this.updateVerticalRef(true, this.up.right);
      this.right.updateSents(true, distance - 1);
    }
    else if (distance <= -1) {
      this.updateVerticalRef(true, this.up.left);
      this.left.updateSents(true, distance + 1);
    }
    else {
      // do nothing
    }
  }

  void makeRed() {
    throw new IllegalStateException("Cannot make a Sentinel red");
  }

  // effect; remove the top sentinal
  // a side sentinal shouod never be called to remove a top sentinal
  public void removeSideSent(boolean isLastImgRow) {
    throw new IllegalStateException("A top sent is not an img pixel");
  }

}

// represents a row sentinel pixal (border pixel)
class SideSent extends APixel {

  // create a side column sentinel
  SideSent(APixel up, APixel down) {
    this.color = Color.BLACK;
    this.brightness = 0;
    this.left = this;
    this.right = this;
    this.updateVerticalRef(true, up);
    this.updateVerticalRef(false, down);
  }

  // effect; remove the top sentinal
  // a side sentinal shouod never be called to remove a top sentinal
  public void removeTopSent(boolean isLastImgRow) {
    throw new IllegalStateException("A side sent is not an img pixel");
  }

  // -------
  void makeRed() {
    throw new IllegalStateException("Cannot make a Sentinel red");
  }

  // effect; remove this top sentinal, only if it is not treated as the last image
  // row
  // (<isLastImgRow> must be false). If this case is met, remove the references to
  // this sentinel
  // from the left and right pixels
  public void removeSideSent(boolean isLastImgRow) {
    if (isLastImgRow) {
      throw new IllegalStateException("A side sentinel is not an image pixel");
    }
    else {
      this.up.updateVerticalRef(false, this.down);
    }
  }

  // effect; update the required sentinals based on the <distTraveled>,
  // magnitude corresponding to displacement, and sign corresponding to direction
  // traveled. A positive number means distance was right, negative means distance
  // is left,
  // and zero is no distance/ direction
  void updateSents(boolean isVertical, int distance) {
    if (isVertical) {
      throw new IllegalStateException("updating wrong sentinals (should be updating top)");
    }
    if (distance >= 1) {
      this.updateHorizontalRef(true, this.left.down);
      this.down.updateSents(false, distance - 1);
    }
    else if (distance <= -1) {
      this.updateHorizontalRef(true, this.left.up);
      this.up.updateSents(false, distance + 1);
    }
    else {
      // do nothing
    }
  }
}

// represents a genuine pixel in the image
class ImgPixel extends APixel {

  // create an image pixel from a color, and its 4 direct neighbors
  ImgPixel(Color color, APixel left, APixel right, APixel up, APixel down) {
    if (!new PixelStructInvariantUtil().checkStructure(left, up)) {
      throw new IllegalStateException(
          "invalid structure for pixels; must be constructed in correct way");
    }
    this.color = color;
    this.brightness = ((color.getRed() + color.getGreen() + color.getBlue()) / 3.0) / 255.0;
    this.updateHorizontalRef(true, left);
    this.updateHorizontalRef(false, right);
    this.updateVerticalRef(true, up);
    this.updateVerticalRef(false, down);
  }

  // constructor for testing purposes; to be able to get an invalid chain from a
  // corner sentinal,
  // in order to test the util
  ImgPixel(Color color, APixel up, APixel down) {
    this.color = color;
    this.brightness = ((color.getRed() + color.getGreen() + color.getBlue()) / 3.0) / 255.0;
    this.left = this;
    this.right = this;
    this.updateVerticalRef(true, up);
    this.updateVerticalRef(false, down);
  }

  // effect; remove this singular pixel, without reference to a previous direction
  // return the distance traveled to between the pixels (0)
  public int removePixel(boolean isVertical) {
    if (isVertical) {
      this.right.updateHorizontalRef(true, this.left);
      return 0;
    }
    else {
      this.down.updateVerticalRef(true, this.up);
      return 0;
    }
  }

  // effect; remove this singular pixel, with reference to a previous pixel <prev>
  // return the distance traveled to between the pixels
  // Assume; the two pixels are immediatly reachable, from an straight or diagonal
  // path
  public int removePixel(boolean isVertical, APixel prev) {
    if (isVertical && prev.up.equals(this)) {
      return this.removePixel(true);
    }
    else if (isVertical && prev.up.right.equals(this)) {
      this.removeRightDiagonal();
      return 1;
    }
    else if (isVertical && prev.up.left.equals(this)) {
      this.removeLeftDiagonal();
      return -1;
    }
    else if (!isVertical && prev.left.equals(this)) {
      return this.removePixel(false);
    }
    else if (!isVertical && prev.left.down.equals(this)) {
      this.removeDownLeft();
      return 1;
    }
    else if (!isVertical && prev.left.up.equals(this)) {
      this.removeUpLeft();
      return -1;
    }
    else {
      // program should never get here
      throw new IllegalStateException("the two pixels are not connected correctly");
    }
  }

  // effect; remove the pixel in a straight line pattern, and adjust the
  // connections as such; just fix the left and right to point each other
  public void removeStraightLine() {
    this.right.updateHorizontalRef(true, this.left);
  }

  // effect; remove the pixel from a right diagonal pattern, and adjust the
  // connections as such ; fix the left and right to point to each other,
  // and swap the left's down connection to the down of the removed pixel
  public void removeRightDiagonal() {
    this.right.updateHorizontalRef(true, this.left);
    this.left.updateVerticalRef(false, this.down);
  }

  // effect; remove the pixel from a left diagonal pattern, and adjust the
  // connections as such; fix the left and right to point to each other,
  // and swap the right's down connection to the down of the removed pixel
  public void removeLeftDiagonal() {
    this.right.updateHorizontalRef(true, this.left);
    this.right.updateVerticalRef(false, this.down);
  }

  // effect; remove the pixel horizontally moving down left pattern, and adjust
  // the
  // connections as such
  void removeDownLeft() {
    this.down.updateVerticalRef(true, this.up);
    this.up.updateHorizontalRef(false, this.right);
  }

  // effect; remove the pixel horizontally moving down left pattern, and adjust
  // the
  // connections as such
  void removeUpLeft() {
    this.down.updateVerticalRef(true, this.up);
    this.down.updateHorizontalRef(false, this.right);

  }

  // effect; remove the top sentinal
  // if this is the last image row, see if the top sentinal is indeed a top
  // sentinal
  // if it is not (if you get to another img pixel, by <isLastRow> being false),
  // throw an error
  public void removeTopSent(boolean isLastImgRow) {
    if (!isLastImgRow) {
      throw new IllegalStateException("tried to remove an img pixel instead of a top sentinel");
    }
    else {
      this.up.removeTopSent(false);
    }
  }

  // effect; remove the top sentinal
  // if this is the last image row, see if the top sentinal is indeed a top
  // sentinal
  // if it is not (if you get to another img pixel, by <isLastRow> being false),
  // throw an error
  public void removeSideSent(boolean isLastImgRow) {
    if (!isLastImgRow) {
      throw new IllegalStateException("tried to remove an img pixel instead of a top sentinel");
    }
    else {
      this.left.removeSideSent(false);
    }
  }

  // effect; update the required sentinals based on the <distTraveled>,
  // magnitude corresponding to displacement, and sign corresponding to direction
  // traveled
  // assume; updateSents should only ever be called on the first info in a
  // finished seam, so
  // it should always directly point down to a top sentinal
  void updateSents(boolean isVertical, int distance) {
    if (isVertical) {
      this.down.updateSents(isVertical, distance);
    }
    else {
      this.right.updateSents(isVertical, distance);
    }
  }

  void makeRed() {
    this.color = Color.RED;
    this.brightness = ((this.color.getRed() + this.color.getGreen() + this.color.getBlue()) / 3.0)
        / 255.0;
  }
}

// Util designed to check the structural invariant
class PixelStructInvariantUtil {

  // checks if this is a valid pixel structure invariant -
  // is the upper neighbor of the left pixel point to the same
  // pixel as the left neighbor of the upper pixel
  // Decision; aliasing is used to make sure that it is the exact same pixel,
  // in the exact correct placement
  boolean checkStructure(APixel left, APixel up) {
    return left.up == up.left;
  }

  // for debugging; check that a a corner sentinal side does not
  // lead directly to an image pixel (it only touches othe rsentinals directly)
  boolean checkHeaderNotImgPixel(APixel csent) {
    APixel checkSent = csent.down;
    if (checkSent instanceof CornerSent) {
      return false;
    }
    else if (checkSent instanceof SideSent) {
      return this.checkHeaderNotImgPixel(checkSent);
    }
    else {
      return true;
    }
  }

  // check that all connections from/ around a pixel have
  // correctly been updated, based on the direction of removal and
  // boolean of vertical or horizontal removal
  // a negative number is left, positive is right, 0 is no distance,
  boolean checkUpdated(boolean isVertical, APixel pix, int dir) {
    return (isVertical && ((dir == 0) && pix.left.right != pix && pix.right.left != pix)
        || ((dir > 0) && pix.left.right != pix && pix.right.left != pix
            && pix.left.down == pix.down)
        || ((dir < 0) && pix.left.right != pix && pix.right.left != pix
            && pix.right.down == pix.down))
        || ((dir == 0) && pix.up.down != pix && pix.down.up != pix)
        || ((dir > 0) && pix.up.down != pix && pix.down.up != pix && pix.up.right == pix.right)
        || ((dir < 0) && pix.up.down != pix && pix.down.up != pix && pix.down.right == pix.right);
  }

}

// represents a grid of pixels as a 2D deque of pixels
// Design decision; like in a regular Deque, PixelDeque and APixel are intrinsically
// tied together. because of this, we are making the design decision to be able to access
// APixel fields in this class (a decision we were able to do in Deque<T>
class PixelDeque {
  // the corner sentinel node this 2D deque points to
  CornerSent header;
  int width;
  int height;
  // a collection of the minimum SeamInfo's corresponding to each pixel in the
  // bottom row
  // of the image
  ArrayList<SeamInfo> seams;
  SeamInfo mostBoring;

  // PixelDeque constructor for testing purposes; taking in a corner sentinal,
  // and a width and height
  PixelDeque(CornerSent corner, int width, int height) {
    this.header = corner;
    this.width = width;
    this.height = height;
    this.seams = this.makeSeamInfoArray();
    this.mostBoring = this.getMinimumSeamInfo();
  }

  // create a PixelDeque from an image read from a file
  PixelDeque(FromFileImage fileImg) {
    this.header = new CornerSent();
    this.width = (int) fileImg.getWidth();
    this.height = (int) fileImg.getHeight();
    APixel top1 = new TopSent(this.header, this.header);
    APixel side1 = new SideSent(this.header, this.header);
    APixel prevPixel = side1;
    // loop through the rows to do
    for (int row = 0; row < fileImg.getHeight(); row += 1) {
      if (row != 0) {
        prevPixel = new SideSent(prevPixel.right, this.header);
      }

      // loop through each pixel in a row to add it to the deque
      for (int col = 0; col < fileImg.getWidth(); col += 1) {
        if (row == 0 && col != 0) {
          APixel newTopSentnew = new TopSent(prevPixel.up, this.header);
        }

        prevPixel = new ImgPixel(fileImg.getColorAt(col, row), prevPixel, prevPixel.right,
            prevPixel.up.right, prevPixel.down.right);
      }
    }

    // set all energies of all ImgPixels and set the list of the minimum seams
    // ending
    // in each of the pixels in the bottom row
    this.seams = this.makeSeamInfoArray();
    this.mostBoring = this.getMinimumSeamInfo();
  }

  // Observe the width of this 2D Pixel deque
  int dequeWidth() {
    return this.width;
  }

  // observe the height of the 2-D Pixel Deque
  int dequeHeight() {
    return this.height;
  }

  // get an arrayList of minimum seam infos for each pixel in the bottom row
  ArrayList<SeamInfo> makeSeamInfoArray() {
    // First our variables:
    ArrayList<SeamInfo> currentSeamInfoRow = new ArrayList<SeamInfo>();
    ArrayList<SeamInfo> previousSeamInfoRow = new ArrayList<SeamInfo>();
    APixel curPixel = this.header.down.right;

    // loop through the rows of the deque, constructing the minimum seams ending in
    // each pixel in that row
    for (int row = 0; row < this.height; row += 1) {
      // for each pixel in a row, find the minimum seam ending at that pixel
      for (int col = 0; col < this.width; col += 1) {
        // in row one, we start the seam infos without any previous seams
        if (row == 0) {
          currentSeamInfoRow.add(new SeamInfo(curPixel));
        }
        else {
          // otherwise, we want to get the smallest above seam and pass it into
          // this seams came from
          // we also need to make sure that if the pixel we are working with is
          // at the start or end of a row, we only grab the seam either to the right
          // or left respectively, so we don't have seams that cross the picture
          SeamInfo minimumSeam;
          if (col == 0) {
            minimumSeam = previousSeamInfoRow.get(0).getMinimumSeam(previousSeamInfoRow.get(1));

          }
          else if (col == this.width - 1) {
            minimumSeam = previousSeamInfoRow.get(col - 1)
                .getMinimumSeam(previousSeamInfoRow.get(col));

          }
          else {
            minimumSeam = previousSeamInfoRow.get(col)
                .getMinimumSeam(previousSeamInfoRow.get(col - 1), previousSeamInfoRow.get(col + 1));
          }
          currentSeamInfoRow.add(new SeamInfo(curPixel, minimumSeam));
        }
        // Now that we have made a seam info for this pixel, we need to move to the
        // right
        // and repeat our code on that pixel.
        curPixel = curPixel.right;
      }
      // After we complete each row of SeamInfo's, we need to head down and to the
      // right again
      // to get out of our Side sentinel column
      curPixel = curPixel.down.right;

      // we also need to make our prevSeamInfoRow equal our current seamInfo row\
      previousSeamInfoRow = currentSeamInfoRow;
      currentSeamInfoRow = new ArrayList<SeamInfo>();
    }

    // finally, we return the PrevRowInfo, which has the last row's
    // seam info's and all the links up to the top row
    // we can then use it to find the minimum seam info
    return previousSeamInfoRow;
  }

  // get an arrayList of minimum seam infos for each pixel in the bottom row
  ArrayList<SeamInfo> makeSeamInfoArrayHorizontal() {
    // First our variables:
    ArrayList<SeamInfo> currentSeamInfoRow = new ArrayList<SeamInfo>();
    ArrayList<SeamInfo> previousSeamInfoRow = new ArrayList<SeamInfo>();
    APixel curPixel = this.header.down;
    curPixel = curPixel.right;

    // loop through the columns of the deque, constructing the minimum seams ending
    // in
    // each pixel in that column;
    for (int col = 0; col < this.width; col += 1) {
      // for each pixel in a column (going down the row), find the minimum seam ending
      // at that pixel
      // moving left
      for (int row = 0; row < this.height; row += 1) {
        // in row one, we start the seam infos without any previous seams
        if (col == 0) {
          currentSeamInfoRow.add(new SeamInfo(curPixel));
        }
        else {
          // otherwise, we want to get the smallest left seam and pass it into
          // this seams came from
          // we also need to make sure that if the pixel we are working with is
          // at the start or end of a column, we only grab the seam either above
          // or below it respectively, so we don't have seams that cross the picture
          SeamInfo minimumSeam;
          if (row == 0) {
            minimumSeam = previousSeamInfoRow.get(0).getMinimumSeam(previousSeamInfoRow.get(1));

          }
          else if (row == this.height - 1) {
            minimumSeam = previousSeamInfoRow.get(row - 1)
                .getMinimumSeam(previousSeamInfoRow.get(row));

          }
          else {
            minimumSeam = previousSeamInfoRow.get(row)
                .getMinimumSeam(previousSeamInfoRow.get(row - 1), previousSeamInfoRow.get(row + 1));
          }
          currentSeamInfoRow.add(new SeamInfo(curPixel, minimumSeam));
        }
        // Now that we have made a seam info for this pixel, we need to move down
        // and repeat our code on that pixel.
        curPixel = curPixel.down;
      }
      // After we complete each row of SeamInfo's, we need to head down and to the
      // right again
      // to get out of our Side sentinel column
      curPixel = curPixel.right.down;

      // we also need to make our prevSeamInfoRow equal our current seamInfo row\
      previousSeamInfoRow = currentSeamInfoRow;
      currentSeamInfoRow = new ArrayList<SeamInfo>();
    }

    // finally, we return the PrevRowInfo, which has the last row's
    // seam info's and all the links up to the top row
    // we can then use it to find the minimum seam info
    return previousSeamInfoRow;
  }

  // observation; what is the minimum SeamInfo from the arraylist of seams in the
  // last row of the picture (complete length)
  // width will be added for part 2, but still works with this method
  SeamInfo getMinimumSeamInfo() {
    SeamInfo minimumSeam = this.seams.get(0);
    // loop through all the seams in the last row, and keeping the minimumSeam
    // always the minimum seamInfo so far
    for (int i = 1; i < this.seams.size(); i += 1) {
      minimumSeam = minimumSeam.getMinimumSeam(this.seams.get(i));
    }
    return minimumSeam;
  }

  // effect; remove the most boring seam from the picture deque grid
  // for this; find the most boring seam from this list of seams
  // remove all pixels that make up said seam (plus top sent of the seam)
  // update the relevant number of top sentinals that need to be updated
  // lower the width by 1
  // recalulate the minimum seams (in turn updating the energies)
  void removeBoringSeam(boolean isVertical) {
    int distance = mostBoring.removeSeam(isVertical);
    this.mostBoring.updateSentinels(isVertical, distance);
    if (isVertical) {
      this.width -= 1;
    }
    else {
      this.height -= 1;
    }
  }

  // draw the picture represented by this 2D deque of pixels, based on its image
  // <mode>
  WorldImage drawPicture(Boolean mode) {
    ComputedPixelImage generatedImage = new ComputedPixelImage(this.width, this.height);
    APixel currentPixel = this.header;
    // go through each row, and draw all relevant img pixels
    for (int row = 0; row < this.height; row += 1) {
      currentPixel = currentPixel.down.right;
      // go through all the img pixels in a row, adding their color one
      // at a time in the correct coardinate
      for (int col = 0; col < this.width; col += 1) {
        Color color;
        if (mode) {
          color = currentPixel.color;
        }
        else {
          int energy = (int) ((255 * currentPixel.observeTotalE()) / Math.sqrt(32));
          color = new Color(energy, energy, energy);
        }
        generatedImage.setPixel(col, row, color);
        currentPixel = currentPixel.right;
      }
    }
    return generatedImage;
  }

  // initialize a boring seam given if you are removing vertical or horizontal
  // seams,
  // then set it to be red
  void initializeBoringSeam(boolean isVertical) {
    if (isVertical) {
      this.seams = this.makeSeamInfoArray();
    }
    else {
      this.seams = this.makeSeamInfoArrayHorizontal();
    }
    this.mostBoring = this.getMinimumSeamInfo();
    this.mostBoring.makeRed();
  }

}

//represent a seam in the picture
class SeamInfo {
  APixel pixel;
  // weight of the seam so far
  double totalWeight;
  SeamInfo cameFrom;

  // create the base case of a seam
  SeamInfo(APixel pixel) {
    this.pixel = pixel;
    this.totalWeight = pixel.observeTotalE();
    this.cameFrom = null;
  }

  // create a seam info that comes from another non null seam info
  SeamInfo(APixel pixel, SeamInfo cameFrom) {
    if (cameFrom == null) {
      throw new IllegalStateException("Seam info you come from cannot be null");
    }
    this.pixel = pixel;
    this.totalWeight = cameFrom.totalWeight + pixel.observeTotalE();
    this.cameFrom = cameFrom;
  }

  // return the minimum seam info between this seaminfo, and a righter-more
  // seaminfo
  // if there is a tie, prefer this one (leftermost)
  SeamInfo getMinimumSeam(SeamInfo right) {
    if (this.totalWeight > right.totalWeight) {
      return right;
    }
    else {
      return this;
    }
  }

  // return the minimum seam info between this seaminfo, its left seaminfo,
  // and its right seaminfo.
  // if there is a tie, prefer the leftmost seaminfo
  SeamInfo getMinimumSeam(SeamInfo left, SeamInfo right) {
    return left.getMinimumSeam(this).getMinimumSeam(right);

  }

  // effect; remove all the pixels in the seam, and fix the connections
  // return the total net distance traveled by this seam
  int removeSeam(boolean isVertical) {
    // remove pixel from seam
    this.pixel.removePixel(isVertical);
    if (cameFrom == null && isVertical) {
      // this is the last ImgPixel in the seam
      this.pixel.removeTopSent(true);
      return 0;
    }
    else if (cameFrom == null && !isVertical) {
      // this is the last ImgPixel in the seam
      this.pixel.removeSideSent(true);
      return 0;
    }
    else {
      return this.cameFrom.removeSeamHelp(isVertical, this.pixel);
    }
  }

  // effect; remove all the pixels in the seam, and fix the connections, based on
  // the
  // direction
  // this seams pixel is to the previous removed pixel
  // return the total net distance traveled by this seam
  int removeSeamHelp(boolean isVertical, APixel prev) {
    // remove pixel from seam
    int change = this.pixel.removePixel(isVertical, prev);
    if (cameFrom == null && isVertical) {
      // this is the last ImgPixel in the seam
      this.pixel.removeTopSent(true);
      return change;
    }
    else if (cameFrom == null && !isVertical) {
      // this is the last ImgPixel in the seam
      this.pixel.removeSideSent(true);
      return change;
    }
    else {
      return change + this.cameFrom.removeSeamHelp(isVertical, this.pixel);
    }
  }

  // update sentinal after removing a seam, based on the distance traveled by the
  // seam
  // net right direction gets a positive distance
  void updateSentinels(boolean isVertical, int distance) {
    if (distance != 0) {
      this.pixel.updateSents(isVertical, distance);
    }
    else {
      // do nothing
    }
  }

  // make all the pixels in the seam red
  void makeRed() {
    this.pixel.makeRed();
    if (this.cameFrom != null) {
      this.cameFrom.makeRed();
    }
  }
}

// represents a PixelWorld, which represents a 2D deque correlating to a picture
class PixelWorld extends World {
  // PixelDeque corresponding to the picture represented by the world
  PixelDeque picture;
  // is it time for a redTick? ie, do you remove, or draw the mostBoring seam as
  // red;
  boolean redTick;
  // are you in the paused mode?
  boolean paused;
  // int representing mode you are in; 0 is random, 1 is vertical, -1 is
  // horizontal
  int mode;
  boolean cycleDirect;
  // greyScale (false), regular (true)
  boolean imageMode;

  // testing world in order to get the correct setUp
  PixelWorld(PixelDeque picture, boolean redTick, boolean paused, int mode, boolean cycleDirect,
      boolean imageMode) {
    this.picture = picture;
    this.redTick = redTick;
    this.paused = paused;
    this.mode = mode;
    this.cycleDirect = cycleDirect;
    this.imageMode = imageMode;
  }

  // create a PixelWorld from a string correlating to a file name
  PixelWorld(String pictureFileName) {
    this.picture = new PixelDeque(new FromFileImage(pictureFileName));
    this.redTick = true;
    this.paused = false;
    this.mode = 0;
    this.imageMode = true;
  }

  // draw the image corresponding to PixelDeque
  public WorldScene makeScene() {
    WorldScene output = new WorldScene(this.picture.dequeWidth(), this.picture.dequeHeight());
    output.placeImageXY(picture.drawPicture(this.imageMode), this.picture.dequeWidth() / 2,
        this.picture.dequeHeight() / 2);
    return output;
  }

  // remove the most boring seam from the picture, after finding it
  // and making it red, based on the current mode
  public void onTick() {
    if (!paused || !redTick) {
      if (redTick && this.mode == 0) {

        // generate a 1 or a 0; 1 is vertical, 0 is horizontal
        Random rand = new Random();
        this.cycleDirect = rand.nextInt(2) == 1;
        this.picture.initializeBoringSeam(this.cycleDirect);
      }
      else if (redTick && this.mode != 0) {
        this.cycleDirect = this.mode == 1;
        this.picture.initializeBoringSeam(this.cycleDirect);
      }
      else if (!redTick && this.mode == 0) {
        this.picture.removeBoringSeam(this.cycleDirect);
      }
      else {
        this.picture.removeBoringSeam(this.cycleDirect);
      }
      this.redTick = !this.redTick;
    }

    if (this.picture.dequeWidth() == 2 || this.picture.dequeHeight() == 2) {
      this.endOfWorld("End");
    }
  }

  // respond to keyevent inputs;
  // " " pauses the world, "h" sets it to horizontal removal,
  // "v" sets it to vertical removal, "r" brings it back to random,
  // and "g" sets it to and from grey scale
  public void onKeyEvent(String key) {
    if (key.equals(" ")) {
      this.paused = !this.paused;
    }
    else if (key.equals("v")) {
      this.mode = 1;
    }
    else if (key.equals("h")) {
      this.mode = -1;
    }
    else if (key.equals("r")) {
      this.mode = 0;
    }
    else if (key.equals("g")) {
      this.imageMode = !this.imageMode;
    }
  }

  // String msg does not matter for this program
  public WorldScene lastScene(String msg) {
    return this.makeScene();
  }
}

// represents examples of pIxelWorlds, Seams, and so on.
class ExamplesSeams {
  PixelWorld test;
  PixelWorld balloons;
  ArrayList<SeamInfo> testSeam;
  PixelWorld simpleTest;
  PixelWorld beans;
  PixelDeque itemCheck;
  PixelDeque smallWorld;
  PixelWorld smWorld;

  APixel corner;
  APixel top1;
  APixel top2;
  APixel top3;
  APixel side1;

  APixel pixel1R1;
  APixel pixel2R1;
  APixel pixel3R1;

  APixel side2;
  APixel pixel1R2;
  APixel pixel2R2;
  APixel pixel3R2;

  APixel side3;
  APixel pixel1R3;
  APixel pixel2R3;
  APixel pixel3R3;

  APixel sWCorner;
  APixel sWtop1;
  APixel sWtop2;
  APixel sWtop3;
  APixel sWtop4;
  APixel sWside1;

  APixel sWpixel1R1;
  APixel sWpixel2R1;
  APixel sWpixel3R1;
  APixel sWpixel4R1;

  APixel sWside2;
  APixel sWpixel1R2;
  APixel sWpixel2R2;
  APixel sWpixel3R2;
  APixel sWpixel4R2;

  APixel sWside3;
  APixel sWpixel1R3;
  APixel sWpixel2R3;
  APixel sWpixel3R3;
  APixel sWpixel4R3;

  APixel sWside4;
  APixel sWpixel1R4;
  APixel sWpixel2R4;
  APixel sWpixel3R4;
  APixel sWpixel4R4;

  APixel header1;
  APixel topSent0;
  APixel topSent1;
  APixel topSent2;
  APixel sideSent0;
  APixel sideSent1;
  APixel sideSent2;

  APixel imgPixel1_0_0;
  APixel imgPixel1_1_0;
  APixel imgPixel1_2_0;
  APixel imgPixel1_0_1;
  APixel imgPixel1_1_1;
  APixel imgPixel1_2_1;
  APixel imgPixel1_0_2;
  APixel imgPixel1_1_2;
  APixel imgPixel1_2_2;

  PixelDeque horiz;

  CornerSent horizCorn;
  APixel cornerH;
  APixel top1H;
  APixel top2H;
  APixel top3H;
  APixel side1H;

  APixel pixel1R1H;
  APixel pixel2R1H;
  APixel pixel3R1H;

  APixel side2H;
  APixel pixel1R2H;
  APixel pixel2R2H;
  APixel pixel3R2H;

  APixel side3H;
  APixel pixel1R3H;
  APixel pixel2R3H;
  APixel pixel3R3H;

  // initialize the fields
  void initFields() {
    test = new PixelWorld("basic-world-1.jpeg");
    testSeam = test.picture.seams;
    balloons = new PixelWorld("Balloons.jpeg");
    simpleTest = new PixelWorld("basic-world-1-ontick.jpeg");
    beans = new PixelWorld("beans-boys.jpeg");

    corner = new CornerSent();
    top1 = new TopSent(corner, corner);
    top2 = new TopSent(top1, corner);
    top3 = new TopSent(top2, corner);
    side1 = new SideSent(corner, corner);

    // ImgPixel(Color color, APixel left, APixel right, APixel up, APixel down) {
    pixel1R1 = new ImgPixel(Color.black, side1, side1, top1, top1);
    pixel2R1 = new ImgPixel(Color.black, pixel1R1, side1, top2, top2);
    pixel3R1 = new ImgPixel(Color.red, pixel2R1, side1, top3, top3);

    side2 = new SideSent(side1, corner);
    pixel1R2 = new ImgPixel(Color.black, side2, side2, pixel1R1, top1);
    pixel2R2 = new ImgPixel(Color.black, pixel1R2, side2, pixel2R1, top2);
    pixel3R2 = new ImgPixel(Color.blue, pixel2R2, side2, pixel3R1, top3);

    side3 = new SideSent(side2, corner);
    pixel1R3 = new ImgPixel(Color.black, side3, side3, pixel1R2, top1);
    pixel2R3 = new ImgPixel(Color.black, pixel1R3, side3, pixel2R2, top2);
    pixel3R3 = new ImgPixel(Color.red, pixel2R3, side3, pixel3R2, top3);

    itemCheck = new PixelDeque((CornerSent) corner, 3, 3);

    horizCorn = new CornerSent();
    top1H = new TopSent(horizCorn, horizCorn);
    top2H = new TopSent(top1H, horizCorn);
    top3H = new TopSent(top2H, horizCorn);
    side1H = new SideSent(horizCorn, horizCorn);

    // ImgPixel(Color color, APixel left, APixel right, APixel up, APixel down) {
    pixel1R1H = new ImgPixel(Color.black, side1H, side1H, top1H, top1H);
    pixel2R1H = new ImgPixel(Color.black, pixel1R1H, side1H, top2H, top2H);
    pixel3R1H = new ImgPixel(Color.black, pixel2R1H, side1H, top3H, top3H);

    side2H = new SideSent(side1H, horizCorn);
    pixel1R2H = new ImgPixel(Color.black, side2H, side2H, pixel1R1H, top1H);
    pixel2R2H = new ImgPixel(Color.black, pixel1R2H, side2H, pixel2R1H, top2H);
    pixel3R2H = new ImgPixel(Color.black, pixel2R2H, side2H, pixel3R1H, top3H);

    side3H = new SideSent(side2H, horizCorn);
    pixel1R3H = new ImgPixel(Color.red, side3H, side3H, pixel1R2H, top1H);
    pixel2R3H = new ImgPixel(Color.blue, pixel1R3H, side3H, pixel2R2H, top2H);
    pixel3R3H = new ImgPixel(Color.red, pixel2R3H, side3H, pixel3R2H, top3H);

    horiz = new PixelDeque(horizCorn, 3, 3);

    // small world example
    sWCorner = new CornerSent();
    sWtop1 = new TopSent(sWCorner, sWCorner);
    sWtop2 = new TopSent(sWtop1, sWCorner);
    sWtop3 = new TopSent(sWtop2, sWCorner);
    sWtop4 = new TopSent(sWtop3, sWCorner);
    sWside1 = new SideSent(sWCorner, sWCorner);

    FromFileImage grid = new FromFileImage("simple-world.jpeg");

    // img; left, right, up, down
    sWpixel1R1 = new ImgPixel(grid.getColorAt(0, 0), sWside1, sWside1, sWtop1, sWtop1);
    sWpixel2R1 = new ImgPixel(grid.getColorAt(1, 0), sWpixel1R1, sWside1, sWtop2, sWtop2);
    sWpixel3R1 = new ImgPixel(grid.getColorAt(2, 0), sWpixel2R1, sWside1, sWtop3, sWtop3);
    sWpixel4R1 = new ImgPixel(grid.getColorAt(3, 0), sWpixel3R1, sWside1, sWtop4, sWtop4);

    sWside2 = new SideSent(sWside1, sWCorner);
    sWpixel1R2 = new ImgPixel(grid.getColorAt(0, 1), sWside2, sWside2, sWpixel1R1, sWtop1);
    sWpixel2R2 = new ImgPixel(grid.getColorAt(1, 1), sWpixel1R2, sWside2, sWpixel2R1, sWtop2);
    sWpixel3R2 = new ImgPixel(grid.getColorAt(2, 1), sWpixel2R2, sWside2, sWpixel3R1, sWtop3);
    sWpixel4R2 = new ImgPixel(grid.getColorAt(3, 1), sWpixel3R2, sWside2, sWpixel4R1, sWtop4);

    sWside3 = new SideSent(sWside2, sWCorner);
    sWpixel1R3 = new ImgPixel(grid.getColorAt(0, 2), sWside3, sWside3, sWpixel1R2, sWtop1);
    sWpixel2R3 = new ImgPixel(grid.getColorAt(1, 2), sWpixel1R3, sWside3, sWpixel2R2, sWtop2);
    sWpixel3R3 = new ImgPixel(grid.getColorAt(2, 2), sWpixel2R3, sWside3, sWpixel3R2, sWtop3);
    sWpixel4R3 = new ImgPixel(grid.getColorAt(3, 2), sWpixel3R3, sWside3, sWpixel4R2, sWtop4);

    sWside4 = new SideSent(sWside3, sWCorner);
    sWpixel1R4 = new ImgPixel(grid.getColorAt(0, 3), sWside4, sWside4, sWpixel1R3, sWtop1);
    sWpixel2R4 = new ImgPixel(grid.getColorAt(1, 3), sWpixel1R4, sWside4, sWpixel2R3, sWtop2);
    sWpixel3R4 = new ImgPixel(grid.getColorAt(2, 3), sWpixel2R4, sWside4, sWpixel3R3, sWtop3);
    sWpixel4R4 = new ImgPixel(grid.getColorAt(3, 3), sWpixel3R4, sWside4, sWpixel4R3, sWtop4);

    smallWorld = new PixelDeque((CornerSent) sWCorner, 4, 4);

    header1 = new CornerSent();
    topSent0 = new TopSent(header1, header1);
    topSent1 = new TopSent(topSent0, header1);
    topSent2 = new TopSent(topSent1, header1);
    sideSent0 = new SideSent(header1, header1);
    sideSent1 = new SideSent(sideSent0, header1);
    sideSent2 = new SideSent(sideSent1, header1);

    imgPixel1_0_0 = new ImgPixel(Color.WHITE, sideSent0, sideSent0, topSent0, topSent0);
    imgPixel1_1_0 = new ImgPixel(Color.BLACK, imgPixel1_0_0, sideSent0, topSent1, topSent1);
    imgPixel1_2_0 = new ImgPixel(Color.RED, imgPixel1_1_0, sideSent0, topSent2, topSent2);

    imgPixel1_0_1 = new ImgPixel(Color.WHITE, sideSent1, sideSent1, imgPixel1_0_0, topSent0);
    imgPixel1_1_1 = new ImgPixel(Color.BLACK, imgPixel1_0_1, sideSent1, imgPixel1_1_0, topSent1);
    imgPixel1_2_1 = new ImgPixel(Color.YELLOW, imgPixel1_1_1, sideSent1, imgPixel1_2_0, topSent2);

    imgPixel1_0_2 = new ImgPixel(Color.WHITE, sideSent2, sideSent2, imgPixel1_0_1, topSent0);
    imgPixel1_1_2 = new ImgPixel(Color.BLACK, imgPixel1_0_2, sideSent2, imgPixel1_1_1, topSent1);
    imgPixel1_2_2 = new ImgPixel(Color.BLUE, imgPixel1_1_2, sideSent2, imgPixel1_2_1, topSent2);

    smWorld = new PixelWorld("simple-world.jpeg");
  }

  // test the big bang and the world
  boolean testWorld(Tester t) {
    this.initFields();
    // t.checkExpect(test.picture.getMinimumSeamInfo().pixel, null);
    beans.bigBang(beans.picture.dequeWidth(), beans.picture.dequeHeight(), 0.1);
    return true;
  }

  // test on ticking
  boolean testOnTick(Tester t) {
    initFields();
    // we want to make sure that testing horizontally and vertically works;
    // therefore, we will be artificially manipulating the values of the mode
    // and pause status
    test.mode = 1;
    test.cycleDirect = true;
    beans.mode = 1;
    beans.cycleDirect = true;
    smWorld.mode = -1;
    smWorld.cycleDirect = false;

    boolean checkBefore = t.checkExpect(test.picture.dequeHeight(), 10)
        && t.checkExpect(test.picture.dequeWidth(), 10)
        && t.checkExpect(beans.picture.dequeWidth(), 680);
    test.onTick();
    beans.onTick();

    PixelWorld worldSmallWorld = new PixelWorld("simple-world.jpeg");
    worldSmallWorld.mode = 1;
    worldSmallWorld.cycleDirect = true;
    worldSmallWorld.onTick();
    boolean checkRedSeam = t.checkExpect(test.picture.header,
        new PixelWorld("basic-world-1-redseam.jpeg").picture.header)
        && t.checkExpect(worldSmallWorld.picture.header,
            new PixelWorld("simple-world-redseam.jpeg").picture.header);
    // tests while paused;
    test.paused = true;
    // one more paused on tick won't stop stuff
    test.onTick();
    boolean paused = t.checkExpect(test.picture.dequeHeight(), 10)
        && t.checkExpect(test.picture.dequeWidth(), 9)
        && t.checkExpect(simpleTest.picture.dequeWidth(), 9);
    beans.onTick();
    boolean checkAfter = t.checkExpect(beans.picture.dequeWidth(), 679)
        && t.checkExpect(beans.picture.header, new PixelWorld("beans-boys-2.jpeg").picture.header);

    beans.paused = true;

    beans.onTick();
    paused = paused && t.checkExpect(beans.picture.dequeWidth(), 679)
        && t.checkExpect(beans.picture.header, new PixelWorld("beans-boys-2.jpeg").picture.header);

    beans.onTick();
    paused = paused && t.checkExpect(beans.picture.dequeWidth(), 679)
        && t.checkExpect(beans.picture.header, new PixelWorld("beans-boys-2.jpeg").picture.header);

    beans.onTick();
    paused = paused && t.checkExpect(beans.picture.dequeWidth(), 679)
        && t.checkExpect(beans.picture.header, new PixelWorld("beans-boys-2.jpeg").picture.header);
    // -------- resume
    beans.paused = false;

    beans.onTick();
    beans.onTick();
    boolean thirdTime = t.checkExpect(beans.picture.dequeWidth(), 678)
        && t.checkExpect(beans.picture.header, new PixelWorld("beans-boys-3.jpeg").picture.header);

    beans.onTick();
    beans.onTick();
    smWorld.onTick();
    smWorld.onTick();
    boolean fourthTime = t.checkExpect(beans.picture.dequeWidth(), 677)
        && t.checkExpect(beans.picture.header, new PixelWorld("beans-boys-4.jpeg").picture.header)
        && t.checkExpect(smWorld.picture.dequeHeight(), 3) && t.checkExpect(smWorld.picture.header,
            new PixelWorld("simple-world-h.jpeg").picture.header);
    fourthTime = true;

    return checkBefore && checkAfter && thirdTime && fourthTime && paused && checkRedSeam;

  }

  // test the onKeyEvent
  boolean testOnKey(Tester t) {
    this.initFields();
    boolean initialized = t.checkExpect(test.redTick, true) && t.checkExpect(test.paused, false)
        && t.checkExpect(test.mode, 0) && t.checkExpect(test.imageMode, true)
        && t.checkExpect(beans.redTick, true) && t.checkExpect(beans.paused, false)
        && t.checkExpect(beans.mode, 0) && t.checkExpect(beans.imageMode, true);

    test.onKeyEvent(" ");
    boolean testPause1 = t.checkExpect(beans.paused, false) && t.checkExpect(test.paused, true);

    test.onKeyEvent(" ");
    beans.onKeyEvent(" ");
    boolean testPause2 = t.checkExpect(beans.paused, true) && t.checkExpect(test.paused, false);

    beans.onKeyEvent(" ");
    boolean testPause3 = t.checkExpect(beans.paused, false) && t.checkExpect(test.paused, false);

    // for changing between random, horizontal, and vertical, cycleDirect wont
    // change until an
    // on key
    test.onKeyEvent("h");
    boolean hInit = t.checkExpect(test.mode, -1) && t.checkExpect(test.cycleDirect, false);

    beans.onKeyEvent("v");
    boolean vInit = t.checkExpect(beans.mode, 1) && t.checkExpect(test.cycleDirect, false);

    // onTick for each to check if cycleDirect set correctly based on the mode
    test.onTick();
    beans.onTick();
    boolean setCycle = t.checkExpect(test.cycleDirect, false)
        && t.checkExpect(beans.cycleDirect, true);

    // back to random
    test.onKeyEvent("r");
    beans.onKeyEvent("r");
    // cycle direct not changed, but mode changed (reflected after deletion)
    boolean rand = t.checkExpect(test.cycleDirect, false) && t.checkExpect(beans.cycleDirect, true)
        && t.checkExpect(test.mode, 0) && t.checkExpect(beans.mode, 0);

    test.onTick();
    test.onKeyEvent("g");
    // set greyscale mode
    t.checkExpect(test.imageMode, false);
    test.onKeyEvent("g");
    // exit greyscale mode
    t.checkExpect(test.imageMode, true);

    return initialized && testPause1 && testPause2 && testPause3 && hInit && vInit && setCycle
        && rand;
    //
  }

  // test removing the boring seam
  boolean testRemoveBoringSeam(Tester t) {
    initFields();
    PixelDeque testPD = new PixelDeque(new FromFileImage("basic-world-1.jpeg"));
    PixelDeque simpleTestPD = new PixelDeque(new FromFileImage("basic-world-1-ontick.jpeg"));
    PixelDeque beansPD = new PixelDeque(new FromFileImage("beans-boys.jpeg"));
    boolean checkBefore = t.checkExpect(testPD.dequeHeight(), 10)
        && t.checkExpect(testPD.dequeWidth(), 10) && t.checkExpect(beansPD.dequeWidth(), 680);
    testPD.initializeBoringSeam(true);
    beansPD.initializeBoringSeam(true);
    testPD.removeBoringSeam(true);
    beansPD.removeBoringSeam(true);
    boolean checkAfter = t.checkExpect(testPD.dequeHeight(), 10)
        && t.checkExpect(testPD.dequeWidth(), 9) && t.checkExpect(simpleTestPD.dequeWidth(), 9)
        && t.checkExpect(testPD.header, simpleTestPD.header)
        && t.checkExpect(beansPD.dequeWidth(), 679) && t.checkExpect(beansPD.header,
            new PixelDeque(new FromFileImage("beans-boys-2.jpeg")).header);
    beansPD.initializeBoringSeam(true);
    beansPD.removeBoringSeam(true);
    boolean thirdTime = t.checkExpect(beansPD.dequeWidth(), 678)
        && t.checkExpect(beansPD.header,
            new PixelDeque(new FromFileImage("beans-boys-3.jpeg")).header)
        && t.checkExpect(smallWorld.dequeHeight(), 4);
    beansPD.initializeBoringSeam(true);
    beansPD.removeBoringSeam(true);
    smallWorld.initializeBoringSeam(false);
    smallWorld.removeBoringSeam(false);
    boolean fourthTime = t.checkExpect(beansPD.dequeWidth(), 677)
        && t.checkExpect(beansPD.header,
            new PixelDeque(new FromFileImage("beans-boys-4.jpeg")).header)
        && t.checkExpect(smallWorld.dequeHeight(), 3) && t.checkExpect(smallWorld.header,
            new PixelDeque(new FromFileImage("simple-world-h.jpeg")).header);

    return checkBefore && checkAfter && thirdTime && fourthTime;
  }

  // test getting a boring seam
  void testGetBoringSeam(Tester t) {

    this.initFields();
    SeamInfo first = new SeamInfo(pixel1R1);
    SeamInfo second = new SeamInfo(pixel1R2, first);
    SeamInfo third = new SeamInfo(pixel1R3, second);
    itemCheck.initializeBoringSeam(true);
    t.checkExpect(itemCheck.getMinimumSeamInfo(), third);

    itemCheck.removeBoringSeam(true);
    SeamInfo firstv2 = new SeamInfo(pixel3R1);
    SeamInfo secondv2 = new SeamInfo(pixel3R2, firstv2);
    SeamInfo thirdv2 = new SeamInfo(pixel3R3, secondv2);
    itemCheck.initializeBoringSeam(true);
    t.checkExpect(itemCheck.getMinimumSeamInfo(), thirdv2);

    this.initFields();
    SeamInfo firstv3 = new SeamInfo(pixel1R1H);
    SeamInfo secondv3 = new SeamInfo(pixel2R1H, firstv3);
    SeamInfo thirdv3 = new SeamInfo(pixel3R1H, secondv3);
    horiz.initializeBoringSeam(false);
    t.checkExpect(horiz.getMinimumSeamInfo(), thirdv3);
  }

  boolean testDrawPicture(Tester t) {
    this.initFields();
    PixelDeque testPD = new PixelDeque(new FromFileImage("basic-world-1.jpeg"));
    PixelDeque simpleTestPD = new PixelDeque(new FromFileImage("basic-world-1-ontick.jpeg"));
    PixelDeque beansPD = new PixelDeque(new FromFileImage("beans-boys.jpeg"));
    return t.checkExpect(testPD.drawPicture(true).getWidth(), 10.0)
        && t.checkExpect(simpleTestPD.drawPicture(true).getWidth(), 9.0)
        && t.checkExpect(beansPD.drawPicture(true).getWidth(), 680.0)
        && t.checkExpect(testPD.drawPicture(false).getWidth(), 10.0)
        && t.checkExpect(simpleTestPD.drawPicture(false).getWidth(), 9.0)
        && t.checkExpect(beansPD.drawPicture(false).getWidth(), 680.0);
  }

  // test the PixelUtilClass
  boolean testPixelStructInvariantUtil(Tester t) {
    this.initFields();
    PixelStructInvariantUtil checker = new PixelStructInvariantUtil();

    boolean testCheckStructure = t.checkExpect(checker.checkStructure(pixel1R1, top3), false)
        && t.checkExpect(checker.checkStructure(pixel1R2, pixel1R1), false)
        && t.checkExpect(checker.checkStructure(corner, pixel2R2), false)
        && t.checkExpect(checker.checkStructure(pixel2R2, corner), false)
        && t.checkExpect(checker.checkStructure(pixel1R2, pixel2R1), true)
        && t.checkExpect(checker.checkStructure(side1, top1), true)
        && t.checkExpect(checker.checkStructure(side3, pixel1R2), true)
        && t.checkExpect(checker.checkStructure(pixel1R2, pixel2R1), true);

    APixel cornerv2 = new CornerSent();
    APixel side1v2 = new SideSent(cornerv2, cornerv2);
    APixel side2v2 = new SideSent(side1v2, cornerv2);

    APixel cornerv3 = new CornerSent();
    APixel side1v3 = new ImgPixel(Color.black, cornerv3, cornerv3);
    APixel side2v3 = new SideSent(side1v3, cornerv3);

    APixel cornerv4 = new CornerSent();
    APixel side1v4 = new SideSent(cornerv4, cornerv4);
    APixel side2v4 = new ImgPixel(Color.black, side1v4, cornerv4);

    boolean testCheckHeaderNotImgPixel = t.checkExpect(checker.checkHeaderNotImgPixel(cornerv2),
        false) && t.checkExpect(checker.checkHeaderNotImgPixel(cornerv3), true)
        && t.checkExpect(checker.checkHeaderNotImgPixel(cornerv4), true);

    SeamInfo first = new SeamInfo(pixel1R1);
    SeamInfo second = new SeamInfo(pixel1R2, first);
    SeamInfo third = new SeamInfo(pixel1R3, second);

    // small world examples
    SeamInfo sWFirst = new SeamInfo(sWpixel1R1);
    SeamInfo sWSecond = new SeamInfo(sWpixel2R2, sWFirst);
    SeamInfo sWThird = new SeamInfo(sWpixel3R3, sWSecond);
    SeamInfo sWFourth = new SeamInfo(sWpixel3R4, sWThird);

    third.removeSeam(true);
    sWFourth.removeSeam(true);

    while (third.cameFrom != null) {
      APixel left = third.pixel.left;
      APixel right = third.pixel.right;
      t.checkExpect(checker.checkUpdated(true, third.pixel, 0), true);
      third = third.cameFrom;
    }

    // level 1
    APixel left = sWFourth.pixel.left;
    APixel right = sWFourth.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel, 0), true);
    sWFourth = sWFourth.cameFrom;

    // level 2
    left = sWFourth.pixel.left;
    right = sWFourth.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel, 0), true);
    sWFourth = sWFourth.cameFrom;

    // level 3
    left = sWFourth.pixel.left;
    right = sWFourth.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel, -2), true);
    // check right is "untouched"
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel.right, 0), false);
    sWFourth = sWFourth.cameFrom;

    // level 4
    left = sWFourth.pixel.left;
    right = sWFourth.pixel.right;
    // t.checkExpect(checker.checkUpdated(sWFourth.pixel, -2), true);
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel, -2), true);
    // check right is "untouched"
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel.right, -1), false);

    this.initFields();
    SeamInfo sWFirstv3 = new SeamInfo(sWpixel3R1);
    SeamInfo sWSecondv3 = new SeamInfo(sWpixel3R2, sWFirstv3);
    SeamInfo sWThirdv3 = new SeamInfo(sWpixel2R3, sWSecondv3);
    SeamInfo sWFourthv3 = new SeamInfo(sWpixel1R4, sWThirdv3);

    boolean initial2 = t.checkExpect(smallWorld.dequeWidth(), 4)
        && t.checkExpect(sWFourthv3.removeSeam(true), 2);

    // level 1
    left = sWFourthv3.pixel.left;
    right = sWFourthv3.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWFourthv3.pixel, 0), true);
    sWFourthv3 = sWFourthv3.cameFrom;

    // level 2
    left = sWFourthv3.pixel.left;
    right = sWFourthv3.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWFourthv3.pixel, 1), true);
    sWFourthv3 = sWFourthv3.cameFrom;

    // level 3
    left = sWFourthv3.pixel.left;
    right = sWFourthv3.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWFourthv3.pixel, 1), true);
    sWFourthv3 = sWFourthv3.cameFrom;

    // level 4
    left = sWFourthv3.pixel.left;
    right = sWFourthv3.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWFourthv3.pixel, 0), true);

    SeamInfo hFirst = new SeamInfo(pixel1R1H);
    SeamInfo hSecond = new SeamInfo(pixel2R1H, hFirst);
    SeamInfo hThird = new SeamInfo(pixel3R1H, hSecond);
    boolean initialHorizontal = t.checkExpect(horiz.dequeHeight(), 3)
        && t.checkExpect(hThird.removeSeam(false), 0);

    // level 1
    t.checkExpect(checker.checkUpdated(false, hThird.pixel, 0), true);
    hThird = hThird.cameFrom;

    // level 2
    t.checkExpect(checker.checkUpdated(false, hThird.pixel, 0), true);
    hThird = hThird.cameFrom;

    // level 3
    t.checkExpect(checker.checkUpdated(false, hThird.pixel, 0), true);
    // check up and down is "untouched"
    t.checkExpect(checker.checkUpdated(false, hThird.pixel.up, 0), false);
    t.checkExpect(checker.checkUpdated(false, hThird.pixel.down, 0), false);
    hThird = hThird.cameFrom;

    this.initFields();
    SeamInfo hFirstv2 = new SeamInfo(pixel1R1H);
    SeamInfo hSecondv2 = new SeamInfo(pixel2R2H, hFirstv2);
    SeamInfo hThirdv2 = new SeamInfo(pixel3R3H, hSecondv2);
    initialHorizontal = initialHorizontal && t.checkExpect(horiz.dequeHeight(), 3)
        && t.checkExpect(hThirdv2.removeSeam(false), -2);

    // level 1
    t.checkExpect(checker.checkUpdated(false, hThirdv2.pixel, 0), true);
    hThirdv2 = hThirdv2.cameFrom;

    // level 2
    t.checkExpect(checker.checkUpdated(false, hThirdv2.pixel, -2), true);
    hThirdv2 = hThirdv2.cameFrom;

    // level 3
    t.checkExpect(checker.checkUpdated(false, hThirdv2.pixel, -1), true);
    // check up and down is "untouched"
    t.checkExpect(checker.checkUpdated(false, hThirdv2.pixel.up, -1), false);
    t.checkExpect(checker.checkUpdated(false, hThirdv2.pixel.down, -1), false);
    hThirdv2 = hThirdv2.cameFrom;

    this.initFields();
    SeamInfo hFirstv3 = new SeamInfo(pixel1R3H);
    SeamInfo hSecondv3 = new SeamInfo(pixel2R2H, hFirstv3);
    SeamInfo hThirdv3 = new SeamInfo(pixel3R1H, hSecondv3);
    initialHorizontal = initialHorizontal && t.checkExpect(horiz.dequeHeight(), 3)
        && t.checkExpect(hThirdv3.removeSeam(false), 2);

    // level 1
    t.checkExpect(checker.checkUpdated(false, hThirdv3.pixel, 0), true);
    hThirdv3 = hThirdv3.cameFrom;

    // level 2
    t.checkExpect(checker.checkUpdated(false, hThirdv3.pixel, 2), true);
    hThirdv3 = hThirdv3.cameFrom;

    // level 3
    t.checkExpect(checker.checkUpdated(false, hThirdv3.pixel, 1), true);
    // check up and down is "untouched"
    t.checkExpect(checker.checkUpdated(false, hThirdv3.pixel.up, 1), false);
    t.checkExpect(checker.checkUpdated(false, hThirdv3.pixel.down, 1), false);
    hThirdv3 = hThirdv3.cameFrom;

    return testCheckStructure && testCheckHeaderNotImgPixel;
  }

  // --------------------- update below
  boolean testInitializeBoringSeam(Tester t) {
    this.initFields();

    SeamInfo first = new SeamInfo(pixel1R1);
    SeamInfo second = new SeamInfo(pixel1R2, first);
    SeamInfo third = new SeamInfo(pixel1R3, second);

    SeamInfo firstv2 = new SeamInfo(pixel3R1);
    SeamInfo secondv2 = new SeamInfo(pixel3R2, firstv2);
    SeamInfo thirdv2 = new SeamInfo(pixel3R3, secondv2);

    SeamInfo firstv3 = new SeamInfo(pixel2R1);
    SeamInfo secondv3 = new SeamInfo(pixel2R2, firstv3);
    SeamInfo thirdv3 = new SeamInfo(pixel2R3, secondv3);

    SeamInfo firstHorizontal = new SeamInfo(pixel1R1);
    SeamInfo secondHorizontal = new SeamInfo(pixel2R1, firstHorizontal);
    SeamInfo thirdHorizontal = new SeamInfo(pixel3R2, secondHorizontal);

    boolean firstBoringSeamCheck = t.checkExpect(itemCheck.getMinimumSeamInfo(), third);

    this.initFields();

    itemCheck.seams = itemCheck.makeSeamInfoArrayHorizontal();

    boolean thirdBoringSeamCheck = t.checkExpect(itemCheck.getMinimumSeamInfo(), thirdHorizontal);

    return true;
  }

  // double check proper deque construction; checking it on every single APixel,
  // including the sentinel pixels
  void testConstruction(Tester t) {
    FromFileImage fileImg = new FromFileImage("basic-world-1.jpeg");
    PixelStructInvariantUtil checker = new PixelStructInvariantUtil();
    APixel corner = new CornerSent();
    t.checkExpect(checker.checkStructure(corner.left, corner.up), true);
    APixel top1 = new TopSent(corner, corner);
    t.checkExpect(checker.checkStructure(top1.left, top1.up), true);
    APixel side1 = new SideSent(corner, corner);
    APixel prevPixel = side1;
    // loop through the rows to do
    for (int row = 0; row < fileImg.getHeight(); row += 1) {
      if (row != 0) {
        prevPixel = new SideSent(prevPixel.right, corner);
      }

      // loop through each pixel in a row to add it to the deque
      for (int col = 0; col < fileImg.getWidth(); col += 1) {
        if (row == 0 && col != 0) {
          APixel newTopSent = new TopSent(prevPixel.up, corner);
          // t.checkExpect(checker.checkStructure(newTopSent.left, newTopSent.up), true);
        }

        prevPixel = new ImgPixel(fileImg.getColorAt(col, row), prevPixel, prevPixel.right,
            prevPixel.up.right, prevPixel.down.right);
        t.checkExpect(checker.checkStructure(prevPixel.left, prevPixel.up), true);
      }
    }
  }

  // test getting the minimus seam, with both 2 and 3 inputs
  boolean testGetMinimumSeam(Tester t) {
    this.initFields();
    SeamInfo first = new SeamInfo(pixel1R1);
    SeamInfo second = new SeamInfo(pixel1R2, first);
    SeamInfo third = new SeamInfo(pixel1R3, second);

    SeamInfo firstv2 = new SeamInfo(pixel3R1);
    SeamInfo secondv2 = new SeamInfo(pixel3R2, firstv2);
    SeamInfo thirdv2 = new SeamInfo(pixel3R3, secondv2);

    SeamInfo firstv3 = new SeamInfo(pixel2R1);
    SeamInfo secondv3 = new SeamInfo(pixel2R2, firstv3);
    SeamInfo thirdv3 = new SeamInfo(pixel2R3, secondv3);

    // small world examples
    SeamInfo sWFirst = new SeamInfo(sWpixel1R1);
    SeamInfo sWSecond = new SeamInfo(sWpixel2R2, sWFirst);
    SeamInfo sWThird = new SeamInfo(sWpixel3R3, sWSecond);
    SeamInfo sWFourth = new SeamInfo(sWpixel3R4, sWThird);

    SeamInfo sWFirstv2 = new SeamInfo(sWpixel1R1);
    SeamInfo sWSecondv2 = new SeamInfo(sWpixel1R2, sWFirstv2);
    SeamInfo sWThirdv2 = new SeamInfo(sWpixel2R3, sWSecondv2);
    SeamInfo sWFourthv2 = new SeamInfo(sWpixel2R4, sWThirdv2);

    SeamInfo sWFirstv3 = new SeamInfo(sWpixel3R1);
    SeamInfo sWSecondv3 = new SeamInfo(sWpixel3R2, sWFirstv3);
    SeamInfo sWThirdv3 = new SeamInfo(sWpixel2R3, sWSecondv3);
    SeamInfo sWFourthv3 = new SeamInfo(sWpixel1R4, sWThirdv3);

    return t.checkExpect(third.getMinimumSeam(thirdv2), third)
        && t.checkExpect(third.getMinimumSeam(thirdv3), third)
        && t.checkExpect(thirdv2.getMinimumSeam(third, thirdv3), third)
        && t.checkExpect(sWFourth.getMinimumSeam(sWFourthv2), sWFourth)
        && t.checkExpect(sWFourthv3.getMinimumSeam(sWFourth), sWFourth)
        && t.checkExpect(sWFourthv2.getMinimumSeam(sWFourth, sWFourthv3), sWFourth)
        && t.checkExpect(sWFourthv3.getMinimumSeam(sWFourthv3), sWFourthv3);
  }

  // test dequeWidth and dequeHeight
  boolean testDequeMeasurments(Tester t) {
    PixelDeque test = new PixelDeque(new FromFileImage("basic-world-1.jpeg"));
    PixelDeque balloons = new PixelDeque(new FromFileImage("Balloons.jpeg"));
    PixelDeque simpleTest = new PixelDeque(new FromFileImage("basic-world-1-ontick.jpeg"));
    PixelDeque beans = new PixelDeque(new FromFileImage("beans-boys.jpeg"));
    PixelDeque simpleD = new PixelDeque(new FromFileImage("simple-world.jpeg"));

    boolean firstCheck = t.checkExpect(test.dequeHeight(), 10)
        && t.checkExpect(test.dequeWidth(), 10) && t.checkExpect(balloons.dequeHeight(), 343)
        && t.checkExpect(balloons.dequeWidth(), 800) && t.checkExpect(beans.dequeHeight(), 675)
        && t.checkExpect(beans.dequeWidth(), 680) && t.checkExpect(simpleD.dequeHeight(), 4)
        && t.checkExpect(simpleD.dequeWidth(), 4);
    balloons.removeBoringSeam(true);
    beans.removeBoringSeam(true);
    simpleD.removeBoringSeam(true);
    boolean secondCheck = t.checkExpect(simpleTest.dequeHeight(), 10)
        && t.checkExpect(simpleTest.dequeWidth(), 9) && t.checkExpect(balloons.dequeHeight(), 343)
        && t.checkExpect(balloons.dequeWidth(), 799) && t.checkExpect(beans.dequeHeight(), 675)
        && t.checkExpect(beans.dequeWidth(), 679) && t.checkExpect(simpleD.dequeHeight(), 4)
        && t.checkExpect(simpleD.dequeWidth(), 3);
    return firstCheck && secondCheck;
  }

  // test remove a seam
  boolean testRemoveSeam(Tester t) {
    this.initFields();
    PixelStructInvariantUtil checker = new PixelStructInvariantUtil();
    SeamInfo first = new SeamInfo(pixel1R1);
    SeamInfo second = new SeamInfo(pixel1R2, first);
    SeamInfo third = new SeamInfo(pixel1R3, second);

    // small world examples
    SeamInfo sWFirst = new SeamInfo(sWpixel1R1);
    SeamInfo sWSecond = new SeamInfo(sWpixel2R2, sWFirst);
    SeamInfo sWThird = new SeamInfo(sWpixel3R3, sWSecond);
    SeamInfo sWFourth = new SeamInfo(sWpixel3R4, sWThird);

    boolean initial = t.checkExpect(itemCheck.dequeWidth(), 3)
        && t.checkExpect(smallWorld.dequeWidth(), 4) && t.checkExpect(third.removeSeam(true), 0)
        && t.checkExpect(sWFourth.removeSeam(true), -2);

    while (third.cameFrom != null) {
      APixel left = third.pixel.left;
      APixel right = third.pixel.right;
      t.checkExpect(checker.checkUpdated(true, third.pixel, 0), true);
      third = third.cameFrom;
    }

    // level 1
    APixel left = sWFourth.pixel.left;
    APixel right = sWFourth.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel, 0), true);
    // left and right "untouched"
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel.left, 0), false);
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel.right, 0), false);
    sWFourth = sWFourth.cameFrom;

    // level 2
    left = sWFourth.pixel.left;
    right = sWFourth.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel, 0), true);
    // left and right "untouched"
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel.left, 0), false);
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel.right, 0), false);
    sWFourth = sWFourth.cameFrom;

    // level 3
    left = sWFourth.pixel.left;
    right = sWFourth.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel, -2), true);
    // check right is "untouched"
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel.right, 0), false);
    sWFourth = sWFourth.cameFrom;

    // level 4
    left = sWFourth.pixel.left;
    right = sWFourth.pixel.right;
    // t.checkExpect(checker.checkUpdated(sWFourth.pixel, -2), true);
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel, -2), true);
    // check right is "untouched"
    t.checkExpect(checker.checkUpdated(true, sWFourth.pixel.right, -1), false);

    this.initFields();
    SeamInfo sWFirstv3 = new SeamInfo(sWpixel3R1);
    SeamInfo sWSecondv3 = new SeamInfo(sWpixel3R2, sWFirstv3);
    SeamInfo sWThirdv3 = new SeamInfo(sWpixel2R3, sWSecondv3);
    SeamInfo sWFourthv3 = new SeamInfo(sWpixel1R4, sWThirdv3);

    boolean initial2 = t.checkExpect(smallWorld.dequeWidth(), 4)
        && t.checkExpect(sWFourthv3.removeSeam(true), 2);

    // level 1
    left = sWFourthv3.pixel.left;
    right = sWFourthv3.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWFourthv3.pixel, 0), true);
    sWFourthv3 = sWFourthv3.cameFrom;

    // level 2
    left = sWFourthv3.pixel.left;
    right = sWFourthv3.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWFourthv3.pixel, 1), true);
    sWFourthv3 = sWFourthv3.cameFrom;

    // level 3
    left = sWFourthv3.pixel.left;
    right = sWFourthv3.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWFourthv3.pixel, 1), true);
    sWFourthv3 = sWFourthv3.cameFrom;

    // level 4
    left = sWFourthv3.pixel.left;
    right = sWFourthv3.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWFourthv3.pixel, 0), true);

    this.initFields();
    SeamInfo seam1 = new SeamInfo(imgPixel1_0_2);
    SeamInfo seam2 = new SeamInfo(imgPixel1_1_1, seam1);
    SeamInfo seam3 = new SeamInfo(imgPixel1_2_0, seam2);
    int distance;

    distance = seam3.removeSeam(false);

    seam3.updateSentinels(false, distance);

    boolean testRemovedDownSeam = t.checkExpect(topSent2.down.samePixel(imgPixel1_2_1, false), true)
        && t.checkExpect(imgPixel1_1_0.down.samePixel(imgPixel1_1_2, false), true)
        && t.checkExpect(imgPixel1_0_1.down.samePixel(topSent0, false), true)
        && t.checkExpect(topSent0.up.samePixel(imgPixel1_0_1, false), true)
        && t.checkExpect(imgPixel1_1_2.up.samePixel(imgPixel1_1_0, false), true)
        && t.checkExpect(imgPixel1_2_1.up.samePixel(topSent2, false), true);

    this.initFields();
    SeamInfo seam4 = new SeamInfo(imgPixel1_0_0);
    SeamInfo seam5 = new SeamInfo(imgPixel1_1_1, seam4);
    SeamInfo seam6 = new SeamInfo(imgPixel1_2_2, seam5);

    distance = seam6.removeSeam(false);

    seam6.updateSentinels(false, distance);

    boolean testRemovedUpSeam = t.checkExpect(topSent0.down.samePixel(imgPixel1_0_1, false), true)
        && t.checkExpect(imgPixel1_1_0.down.samePixel(imgPixel1_1_2, false), true)
        && t.checkExpect(imgPixel1_2_1.down.samePixel(topSent2, false), true)
        && t.checkExpect(imgPixel1_0_1.up.samePixel(topSent0, false), true)
        && t.checkExpect(imgPixel1_1_2.up.samePixel(imgPixel1_1_0, false), true)
        && t.checkExpect(topSent2.up.samePixel(imgPixel1_2_1, false), true);

    return initial && initial2 && testRemovedDownSeam && testRemovedUpSeam;
  }

  // test removing seam helper
  boolean testRemoveSeamHelp(Tester t) {
    this.initFields();
    PixelStructInvariantUtil checker = new PixelStructInvariantUtil();
    SeamInfo first = new SeamInfo(pixel1R1);
    SeamInfo second = new SeamInfo(pixel1R2, first);
    SeamInfo third = new SeamInfo(pixel1R3, second);

    // small world examples
    SeamInfo sWFirst = new SeamInfo(sWpixel1R1);
    SeamInfo sWSecond = new SeamInfo(sWpixel2R2, sWFirst);
    SeamInfo sWThird = new SeamInfo(sWpixel3R3, sWSecond);
    SeamInfo sWFourth = new SeamInfo(sWpixel3R4, sWThird);

    boolean initial = t.checkExpect(itemCheck.dequeWidth(), 3)
        && t.checkExpect(smallWorld.dequeWidth(), 4)
        && t.checkExpect(second.removeSeamHelp(true, pixel1R3), 0)
        && t.checkExpect(sWThird.removeSeamHelp(true, sWpixel3R4), -2);

    while (second.cameFrom != null) {
      APixel left = second.pixel.left;
      APixel right = second.pixel.right;
      t.checkExpect(checker.checkUpdated(true, second.pixel, 0), true);
      second = second.cameFrom;
    }

    // level 2
    APixel left = sWThird.pixel.left;
    APixel right = sWThird.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWThird.pixel, 0), true);
    // left and right "untouched"
    t.checkExpect(checker.checkUpdated(true, sWThird.pixel.left, 0), false);
    t.checkExpect(checker.checkUpdated(true, sWThird.pixel.right, 0), false);
    sWThird = sWThird.cameFrom;

    // level 3
    left = sWThird.pixel.left;
    right = sWThird.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWThird.pixel, -2), true);
    // check right is "untouched"
    t.checkExpect(checker.checkUpdated(true, sWThird.pixel.right, 0), false);
    sWThird = sWThird.cameFrom;

    // level 4
    left = sWThird.pixel.left;
    right = sWThird.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWThird.pixel, -2), true);
    // check right is "untouched"
    t.checkExpect(checker.checkUpdated(true, sWThird.pixel.right, -1), false);

    this.initFields();
    SeamInfo sWFirstv3 = new SeamInfo(sWpixel3R1);
    SeamInfo sWSecondv3 = new SeamInfo(sWpixel3R2, sWFirstv3);
    SeamInfo sWThirdv3 = new SeamInfo(sWpixel2R3, sWSecondv3);
    SeamInfo sWFourthv3 = new SeamInfo(sWpixel1R4, sWThirdv3);

    boolean initial2 = t.checkExpect(smallWorld.dequeWidth(), 4)
        && t.checkExpect(sWThirdv3.removeSeamHelp(true, sWpixel1R4), 2);

    // level 2
    left = sWThirdv3.pixel.left;
    right = sWThirdv3.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWpixel2R3, 2), true);
    sWThirdv3 = sWThirdv3.cameFrom;

    // level 3
    left = sWThirdv3.pixel.left;
    right = sWThirdv3.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWThirdv3.pixel, 2), true);
    sWThirdv3 = sWThirdv3.cameFrom;

    // level 4
    left = sWThirdv3.pixel.left;
    right = sWThirdv3.pixel.right;
    t.checkExpect(checker.checkUpdated(true, sWThirdv3.pixel, 0), true);

    this.initFields();
    SeamInfo seam1 = new SeamInfo(imgPixel1_0_2);
    SeamInfo seam2 = new SeamInfo(imgPixel1_1_1, seam1);
    SeamInfo seam3 = new SeamInfo(imgPixel1_2_0, seam2);
    int distance;

    distance = imgPixel1_2_0.removePixel(false);

    distance = distance + seam2.removeSeamHelp(false, imgPixel1_2_0);

    seam3.updateSentinels(false, distance);

    boolean testRemovedDownSeam = t.checkExpect(topSent2.down.samePixel(imgPixel1_2_1, false), true)
        && t.checkExpect(imgPixel1_1_0.down.samePixel(imgPixel1_1_2, false), true)
        && t.checkExpect(imgPixel1_0_1.down.samePixel(topSent0, false), true)
        && t.checkExpect(topSent0.up.samePixel(imgPixel1_0_1, false), true)
        && t.checkExpect(imgPixel1_1_2.up.samePixel(imgPixel1_1_0, false), true)
        && t.checkExpect(imgPixel1_2_1.up.samePixel(topSent2, false), true);

    this.initFields();
    SeamInfo seam4 = new SeamInfo(imgPixel1_0_0);
    SeamInfo seam5 = new SeamInfo(imgPixel1_1_1, seam4);
    SeamInfo seam6 = new SeamInfo(imgPixel1_2_2, seam5);

    distance = imgPixel1_2_2.removePixel(false);

    distance = distance + seam5.removeSeamHelp(false, imgPixel1_2_2);

    seam6.updateSentinels(false, distance);

    boolean testRemovedUpSeam = t.checkExpect(topSent0.down.samePixel(imgPixel1_0_1, false), true)
        && t.checkExpect(imgPixel1_1_0.down.samePixel(imgPixel1_1_2, false), true)
        && t.checkExpect(imgPixel1_2_1.down.samePixel(topSent2, false), true)
        && t.checkExpect(imgPixel1_0_1.up.samePixel(topSent0, false), true)
        && t.checkExpect(imgPixel1_1_2.up.samePixel(imgPixel1_1_0, false), true)
        && t.checkExpect(topSent2.up.samePixel(imgPixel1_2_1, false), true);

    return initial && initial2;
  }

  // test updating sentinels, from SeamInfo
  boolean testUpdateSentinels(Tester t) {
    this.initFields();
    PixelStructInvariantUtil checker = new PixelStructInvariantUtil();
    SeamInfo first = new SeamInfo(pixel1R1);
    SeamInfo second = new SeamInfo(pixel1R2, first);
    SeamInfo third = new SeamInfo(pixel1R3, second);

    // small world examples
    SeamInfo sWFirst = new SeamInfo(sWpixel1R1);
    SeamInfo sWSecond = new SeamInfo(sWpixel2R2, sWFirst);
    SeamInfo sWThird = new SeamInfo(sWpixel3R3, sWSecond);
    SeamInfo sWFourth = new SeamInfo(sWpixel3R4, sWThird);

    boolean initial = t.checkExpect(itemCheck.dequeWidth(), 3)
        && t.checkExpect(smallWorld.dequeWidth(), 4);

    int thirdDist = third.removeSeam(true);
    int fourthDist = sWFourth.removeSeam(true);

    third.updateSentinels(true, thirdDist);
    // t.checkExpect(itemCheck.dequeWidth(), 2);
    // update sentinels doesn't update the width of a deque
    t.checkExpect(checker.checkUpdated(true, pixel1R3.down, 0), true);

    sWFourth.updateSentinels(true, fourthDist);
    // t.checkExpect(smallWorld.dequeWidth(), 3);
    // update sentinels doesn't update the width of a deque
    t.checkExpect(checker.checkUpdated(true, sWpixel3R4, 0), true);

    this.initFields();
    SeamInfo sWFirstv3 = new SeamInfo(sWpixel3R1);
    SeamInfo sWSecondv3 = new SeamInfo(sWpixel3R2, sWFirstv3);
    SeamInfo sWThirdv3 = new SeamInfo(sWpixel2R3, sWSecondv3);
    SeamInfo sWFourthv3 = new SeamInfo(sWpixel1R4, sWThirdv3);

    boolean initial2 = t.checkExpect(smallWorld.dequeWidth(), 4);

    int fourthDistv2 = sWFourthv3.removeSeam(true);
    sWFourth.updateSentinels(true, fourthDistv2);
    // t.checkExpect(itemCheck.dequeWidth(), 3);
    // update sentinels doesn't update the width of a deque
    t.checkExpect(checker.checkUpdated(true, sWpixel1R4, 0), true);

    this.initFields();
    SeamInfo seam1 = new SeamInfo(imgPixel1_0_2);
    SeamInfo seam2 = new SeamInfo(imgPixel1_1_1, seam1);
    SeamInfo seam3 = new SeamInfo(imgPixel1_2_0, seam2);
    int distance;

    distance = seam3.removeSeam(false);

    seam3.updateSentinels(false, distance);

    boolean testUpdatedSentsUp = t.checkExpect(sideSent1.left.samePixel(imgPixel1_2_2, false), true)
        && t.checkExpect(sideSent0.left.samePixel(imgPixel1_2_1, false), true);

    this.initFields();
    SeamInfo seam4 = new SeamInfo(imgPixel1_0_0);
    SeamInfo seam5 = new SeamInfo(imgPixel1_1_1, seam4);
    SeamInfo seam6 = new SeamInfo(imgPixel1_2_2, seam5);

    distance = seam6.removeSeam(false);

    seam6.updateSentinels(false, distance);

    boolean testUpdatedSentsDown = t.checkExpect(sideSent1.left.samePixel(imgPixel1_2_0, false),
        true) && t.checkExpect(sideSent2.left.samePixel(imgPixel1_2_1, false), true);

    return initial && initial2 && testUpdatedSentsUp && testUpdatedSentsDown;
  }

  // testUpdateVerticalReference
  boolean testUpdateVerticalReference(Tester t) {
    this.initFields();
    boolean test1 = t.checkExpect(imgPixel1_0_0.up.samePixel(topSent0, false), true)
        && t.checkExpect(imgPixel1_1_1.down.samePixel(imgPixel1_1_2, false), true)
        && t.checkExpect(imgPixel1_2_0.up.samePixel(header1, false), false)
        && t.checkExpect(sideSent1.down.samePixel(topSent0, false), false);

    imgPixel1_0_0.updateVerticalRef(true, imgPixel1_2_2);
    imgPixel1_1_1.updateVerticalRef(false, imgPixel1_0_2);
    imgPixel1_2_0.updateVerticalRef(true, header1);
    sideSent1.updateVerticalRef(false, topSent0);

    boolean test2 = t.checkExpect(imgPixel1_0_0.up.samePixel(topSent0, false), false)
        && t.checkExpect(imgPixel1_1_1.down.samePixel(imgPixel1_1_2, false), false)
        && t.checkExpect(imgPixel1_2_0.up.samePixel(header1, false), true)
        && t.checkExpect(sideSent1.down.samePixel(topSent0, false), true);
    return test1 && test2;
  }

  // testUpdateHorizontalReference
  boolean testUpdateHorizontalReference(Tester t) {
    this.initFields();

    boolean test1 = t.checkExpect(imgPixel1_0_1.right.samePixel(imgPixel1_1_1, false), true)
        && t.checkExpect(imgPixel1_1_0.left.samePixel(imgPixel1_0_0, false), true)
        && t.checkExpect(imgPixel1_2_1.left.samePixel(header1, false), false)
        && t.checkExpect(topSent1.right.samePixel(sideSent2, false), false);

    imgPixel1_0_1.updateHorizontalRef(true, imgPixel1_2_2);
    imgPixel1_1_0.updateHorizontalRef(false, imgPixel1_0_2);
    imgPixel1_2_1.updateHorizontalRef(true, header1);
    topSent1.updateHorizontalRef(false, sideSent2);

    boolean test2 = t.checkExpect(imgPixel1_0_1.left.samePixel(imgPixel1_1_1, false), false)
        && t.checkExpect(imgPixel1_1_0.right.samePixel(imgPixel1_0_0, false), false)
        && t.checkExpect(imgPixel1_2_1.left.samePixel(header1, false), true)
        && t.checkExpect(topSent1.right.samePixel(sideSent2, false), true);
    return test1 && test2;

  }

  // testObserveTotalEnergy
  boolean testObserveTotalEnergy(Tester t) {
    this.initFields();
    boolean testObserve = t.checkExpect(imgPixel1_0_0.observeTotalE() - 2.3333333333 < 0.001, true)
        && t.checkExpect(imgPixel1_1_0.observeTotalE() - 2.357022 < 0.001, true)
        && t.checkExpect(imgPixel1_0_1.observeTotalE(), 0.0)
        && t.checkExpect(imgPixel1_1_1.observeTotalE() - 2.0 < 0.001, true);

    boolean testHorizontalEnergy = t.checkExpect(imgPixel1_0_0.getHorizontalE(), 0.0)
        && t.checkExpect(imgPixel1_1_0.getHorizontalE() - 1.6666666 < 0.001, true)
        && t.checkExpect(imgPixel1_0_1.getHorizontalE(), 0.0)
        && t.checkExpect(imgPixel1_1_1.getHorizontalE() - 2.0 < 0.001, true);

    boolean testVerticalEnergy = t.checkExpect(imgPixel1_0_0.getVerticalE() - 2.0 < 0.001, true)
        && t.checkExpect(imgPixel1_1_0.getVerticalE() + 1.666666 < 0.001, true)
        && t.checkExpect(imgPixel1_0_1.getVerticalE(), 0.0)
        && t.checkExpect(imgPixel1_1_1.getVerticalE(), 0.0);

    imgPixel1_0_0.updateVerticalRef(false, imgPixel1_2_2);
    imgPixel1_0_0.updateVerticalRef(false, imgPixel1_2_0);

    boolean testChange = t.checkExpect(imgPixel1_0_0.observeTotalE() - 0.666666666 < 0.001, true);

    return testObserve && testHorizontalEnergy && testVerticalEnergy && testChange;
  }

  // test removePixel, removeStraightLine, removeRightDiagonal
  // and removeLeftDiagonal
  // note; we added the errors so that in the debugging stage, we would know when
  // bad things
  // were happening; we have decided not to test them, because our implementation
  // does not
  // allow for this to happen
  boolean testRemovePixel(Tester t) {
    this.initFields();
    boolean testOriginal1 = t.checkExpect(imgPixel1_0_0.right.samePixel(imgPixel1_1_0, false), true)
        && t.checkExpect(imgPixel1_0_1.right.samePixel(imgPixel1_1_1, false), true)
        && t.checkExpect(imgPixel1_0_2.right.samePixel(imgPixel1_1_2, false), true)
        && t.checkExpect(imgPixel1_2_0.left.samePixel(imgPixel1_1_0, false), true)
        && t.checkExpect(imgPixel1_2_1.left.samePixel(imgPixel1_1_1, false), true)
        && t.checkExpect(imgPixel1_2_2.left.samePixel(imgPixel1_1_2, false), true);

    imgPixel1_1_2.removePixel(true);
    imgPixel1_1_1.removePixel(true, imgPixel1_1_2);
    imgPixel1_1_0.removePixel(true, imgPixel1_1_1);

    boolean testRemovedStraightLine = t
        .checkExpect(imgPixel1_0_0.right.samePixel(imgPixel1_2_0, false), true)
        && t.checkExpect(imgPixel1_0_1.right.samePixel(imgPixel1_2_1, false), true)
        && t.checkExpect(imgPixel1_0_2.right.samePixel(imgPixel1_2_2, false), true)
        && t.checkExpect(imgPixel1_2_0.left.samePixel(imgPixel1_0_0, false), true)
        && t.checkExpect(imgPixel1_2_1.left.samePixel(imgPixel1_0_1, false), true)
        && t.checkExpect(imgPixel1_2_2.left.samePixel(imgPixel1_0_2, false), true);

    this.initFields();

    boolean testOriginal2 = t.checkExpect(imgPixel1_1_0.right.samePixel(imgPixel1_2_0, false), true)
        && t.checkExpect(imgPixel1_0_1.right.samePixel(imgPixel1_1_1, false), true)
        && t.checkExpect(sideSent2.right.samePixel(imgPixel1_0_2, false), true)
        && t.checkExpect(sideSent0.left.samePixel(imgPixel1_2_0, false), true)
        && t.checkExpect(imgPixel1_2_1.left.samePixel(imgPixel1_1_1, false), true)
        && t.checkExpect(imgPixel1_1_2.left.samePixel(imgPixel1_0_2, false), true)
        && t.checkExpect(imgPixel1_1_0.down.samePixel(imgPixel1_1_1, false), true)
        && t.checkExpect(imgPixel1_1_2.up.samePixel(imgPixel1_1_1, false), true);

    imgPixel1_0_2.removePixel(true);
    imgPixel1_1_1.removePixel(true, imgPixel1_0_2);
    imgPixel1_2_0.removePixel(true, imgPixel1_1_1);

    boolean testRemovedRightDiagonal = t
        .checkExpect(imgPixel1_1_0.right.samePixel(sideSent0, false), true)
        && t.checkExpect(imgPixel1_0_1.right.samePixel(imgPixel1_2_1, false), true)
        && t.checkExpect(sideSent2.right.samePixel(imgPixel1_1_2, false), true)
        && t.checkExpect(sideSent0.left.samePixel(imgPixel1_1_0, false), true)
        && t.checkExpect(imgPixel1_2_1.left.samePixel(imgPixel1_0_1, false), true)
        && t.checkExpect(imgPixel1_1_2.left.samePixel(sideSent2, false), true)
        && t.checkExpect(imgPixel1_1_0.down.samePixel(imgPixel1_2_1, false), true)
        && t.checkExpect(imgPixel1_1_2.up.samePixel(imgPixel1_0_1, false), true);

    this.initFields();

    boolean testOriginal3 = t.checkExpect(sideSent0.right.samePixel(imgPixel1_0_0, false), true)
        && t.checkExpect(imgPixel1_0_1.right.samePixel(imgPixel1_1_1, false), true)
        && t.checkExpect(imgPixel1_1_2.right.samePixel(imgPixel1_2_2, false), true)
        && t.checkExpect(imgPixel1_1_0.left.samePixel(imgPixel1_0_0, false), true)
        && t.checkExpect(imgPixel1_2_1.left.samePixel(imgPixel1_1_1, false), true)
        && t.checkExpect(sideSent2.left.samePixel(imgPixel1_2_2, false), true)
        && t.checkExpect(imgPixel1_1_0.down.samePixel(imgPixel1_1_1, false), true)
        && t.checkExpect(imgPixel1_1_2.up.samePixel(imgPixel1_1_1, false), true);

    imgPixel1_2_2.removePixel(true);
    imgPixel1_1_1.removePixel(true, imgPixel1_2_2);
    imgPixel1_0_0.removePixel(true, imgPixel1_1_1);

    boolean testRemovedLeftDiagonal = t.checkExpect(sideSent0.right.samePixel(imgPixel1_1_0, false),
        true) && t.checkExpect(imgPixel1_0_1.right.samePixel(imgPixel1_2_1, false), true)
        && t.checkExpect(imgPixel1_1_2.right.samePixel(sideSent2, false), true)
        && t.checkExpect(imgPixel1_1_0.left.samePixel(sideSent0, false), true)
        && t.checkExpect(imgPixel1_2_1.left.samePixel(imgPixel1_0_1, false), true)
        && t.checkExpect(sideSent2.left.samePixel(imgPixel1_1_2, false), true)
        && t.checkExpect(imgPixel1_1_0.down.samePixel(imgPixel1_0_1, false), true)
        && t.checkExpect(imgPixel1_1_2.up.samePixel(imgPixel1_2_1, false), true);

    this.initFields();

    imgPixel1_2_0.removePixel(false);
    imgPixel1_1_1.removePixel(false, imgPixel1_2_0);
    imgPixel1_0_2.removePixel(false, imgPixel1_1_1);

    boolean testRemovedUpDiagonal = t.checkExpect(topSent2.down.samePixel(imgPixel1_2_1, false),
        true) && t.checkExpect(imgPixel1_1_0.down.samePixel(imgPixel1_1_2, false), true)
        && t.checkExpect(imgPixel1_0_1.down.samePixel(topSent0, false), true)
        && t.checkExpect(topSent0.up.samePixel(imgPixel1_0_1, false), true)
        && t.checkExpect(imgPixel1_1_2.up.samePixel(imgPixel1_1_0, false), true)
        && t.checkExpect(imgPixel1_2_1.up.samePixel(topSent2, false), true);

    this.initFields();

    imgPixel1_2_2.removePixel(false);
    imgPixel1_1_1.removePixel(false, imgPixel1_2_2);
    imgPixel1_0_0.removePixel(false, imgPixel1_1_1);

    boolean testRemovedDownDiagonal = t.checkExpect(topSent0.down.samePixel(imgPixel1_0_1, false),
        true) && t.checkExpect(imgPixel1_1_0.down.samePixel(imgPixel1_1_2, false), true)
        && t.checkExpect(imgPixel1_2_1.down.samePixel(topSent2, false), true)
        && t.checkExpect(imgPixel1_0_1.up.samePixel(topSent0, false), true)
        && t.checkExpect(imgPixel1_1_2.up.samePixel(imgPixel1_1_0, false), true)
        && t.checkExpect(topSent2.up.samePixel(imgPixel1_2_1, false), true);

    return testOriginal1 && testRemovedStraightLine && testOriginal2 && testRemovedRightDiagonal
        && testOriginal3 && testRemovedLeftDiagonal && testRemovedUpDiagonal
        && testRemovedDownDiagonal;
  }

  // test RemoveTopSent
  boolean testRemoveTopSent(Tester t) {
    this.initFields();

    boolean testOriginal = t.checkExpect(imgPixel1_0_0.up.samePixel(topSent0, false), true)
        && t.checkExpect(imgPixel1_1_0.up.samePixel(topSent1, false), true)
        && t.checkExpect(imgPixel1_2_0.up.samePixel(topSent2, false), true)
        && t.checkExpect(imgPixel1_0_2.down.samePixel(topSent0, false), true)
        && t.checkExpect(imgPixel1_1_2.down.samePixel(topSent1, false), true)
        && t.checkExpect(imgPixel1_2_2.down.samePixel(topSent2, false), true);

    imgPixel1_0_0.removeTopSent(true);
    imgPixel1_1_0.removeTopSent(true);
    topSent2.removeTopSent(false);

    boolean testRemovedAllTopSents = t.checkExpect(header1.left.samePixel(header1, false), true)
        && t.checkExpect(header1.right.samePixel(header1, false), true);

    return testOriginal && testRemovedAllTopSents;
  }

  boolean testRemoveSideSent(Tester t) {
    this.initFields();

    boolean testOriginal = t.checkExpect(imgPixel1_0_0.left.samePixel(sideSent0, false), true)
        && t.checkExpect(imgPixel1_0_1.left.samePixel(sideSent1, false), true)
        && t.checkExpect(imgPixel1_0_2.left.samePixel(sideSent2, false), true)
        && t.checkExpect(imgPixel1_2_0.right.samePixel(sideSent0, false), true)
        && t.checkExpect(imgPixel1_2_1.right.samePixel(sideSent1, false), true)
        && t.checkExpect(imgPixel1_2_2.right.samePixel(sideSent2, false), true);

    imgPixel1_0_0.removeSideSent(true);
    imgPixel1_0_1.removeSideSent(true);
    sideSent2.removeSideSent(false);

    boolean testRemovedAllSideSents = t.checkExpect(header1.up.samePixel(header1, false), true)
        && t.checkExpect(header1.down.samePixel(header1, false), true);

    return testOriginal && testRemovedAllSideSents;
  }

  // test UpdateSents
  boolean testUpdateSents(Tester t) {
    this.initFields();
    boolean testOriginal1 = t.checkExpect(topSent1.up.samePixel(imgPixel1_1_2, false), true)
        && t.checkExpect(topSent2.up.samePixel(imgPixel1_2_2, false), true);

    imgPixel1_2_2.removePixel(true);
    imgPixel1_1_1.removePixel(true, imgPixel1_2_2);
    imgPixel1_0_0.removePixel(true, imgPixel1_1_1);

    topSent0.removeTopSent(false);

    imgPixel1_2_2.updateSents(true, -2);

    boolean testUpdatedSentsLeft = t.checkExpect(topSent1.up.samePixel(imgPixel1_0_2, false), true)
        && t.checkExpect(topSent2.up.samePixel(imgPixel1_1_2, false), true);

    this.initFields();

    boolean testOriginal2 = t.checkExpect(topSent0.up.samePixel(imgPixel1_0_2, false), true)
        && t.checkExpect(topSent1.up.samePixel(imgPixel1_1_2, false), true);

    imgPixel1_0_2.removePixel(true);
    imgPixel1_1_1.removePixel(true, imgPixel1_0_2);
    imgPixel1_2_0.removePixel(true, imgPixel1_1_1);

    topSent2.removeTopSent(false);

    imgPixel1_0_2.updateSents(true, 2);

    boolean testUpdatedSentsRight = t.checkExpect(topSent0.up.samePixel(imgPixel1_1_2, false), true)
        && t.checkExpect(topSent1.up.samePixel(imgPixel1_2_2, false), true);

    this.initFields();

    imgPixel1_2_0.removePixel(false);
    imgPixel1_1_1.removePixel(false, imgPixel1_2_0);
    imgPixel1_0_2.removePixel(false, imgPixel1_1_1);

    sideSent2.removeSideSent(false);

    imgPixel1_2_0.updateSents(false, 2);

    boolean testUpdatedSentsUp = t.checkExpect(sideSent1.left.samePixel(imgPixel1_2_2, false), true)
        && t.checkExpect(sideSent0.left.samePixel(imgPixel1_2_1, false), true);

    this.initFields();

    imgPixel1_2_2.removePixel(false);
    imgPixel1_1_1.removePixel(false, imgPixel1_2_2);
    imgPixel1_0_0.removePixel(false, imgPixel1_1_1);

    sideSent0.removeSideSent(false);

    imgPixel1_2_2.updateSents(false, -2);

    boolean testUpdatedSentsDown = t.checkExpect(sideSent1.left.samePixel(imgPixel1_2_0, false),
        true) && t.checkExpect(sideSent2.left.samePixel(imgPixel1_2_1, false), true);

    return testOriginal1 && testUpdatedSentsLeft && testOriginal2 && testUpdatedSentsRight
        && testUpdatedSentsUp && testUpdatedSentsDown;
  }

  // testSamePixel
  boolean testSamePixel(Tester t) {
    this.initFields();
    return t.checkExpect(imgPixel1_0_0.samePixel(imgPixel1_0_0, false), true)
        && t.checkExpect(imgPixel1_1_2.samePixel(imgPixel1_1_2, false), true)
        && t.checkExpect(imgPixel1_2_2.samePixel(imgPixel1_2_2, true), true)
        && t.checkExpect(imgPixel1_0_0.samePixel(imgPixel1_1_0, false), false)
        && t.checkExpect(imgPixel1_2_1.samePixel(imgPixel1_0_2, true), false)
        && t.checkExpect(imgPixel1_1_1.samePixel(imgPixel1_0_0, false), false);
  }

  // test pixeConstruction
  boolean testSamePixelExceptions(Tester t) {
    this.initFields();
    APixel side4 = new SideSent(side3, corner);
    APixel pixel1R4 = new ImgPixel(Color.yellow, side4, side4, pixel1R3, top1);
    return t.checkConstructorException(
        new IllegalStateException(
            "invalid structure for pixels; must be constructed in correct way"),
        "ImgPixel", Color.red, side4, side4, pixel1R1, top1)
        && t.checkConstructorException(
            new IllegalStateException(
                "invalid structure for pixels; must be constructed in correct way"),
            "ImgPixel", Color.red, pixel1R4, side4, pixel1R2, top1)
        && t.checkConstructorException(
            new IllegalStateException(
                "invalid structure for pixels; must be constructed in correct way"),
            "ImgPixel", Color.cyan, side1, side4, pixel1R3, top1)
        && t.checkConstructorException(
            new IllegalStateException(
                "invalid structure for pixels; must be constructed in correct way"),
            "ImgPixel", Color.red, side4, side4, corner, top1)
        && t.checkConstructorException(
            new IllegalStateException(
                "invalid structure for pixels; must be constructed in correct way"),
            "ImgPixel", Color.red, corner, side4, pixel1R3, top1);
  }

  // test making seams/ pixels red
  boolean testMakeRed(Tester t) {
    this.initFields();
    SeamInfo first = new SeamInfo(pixel1R1);
    SeamInfo second = new SeamInfo(pixel1R2, first);
    SeamInfo third = new SeamInfo(pixel1R3, second);

    SeamInfo firstv2 = new SeamInfo(pixel3R1);
    SeamInfo secondv2 = new SeamInfo(pixel3R2, firstv2);
    SeamInfo thirdv2 = new SeamInfo(pixel3R3, secondv2);

    SeamInfo firstv3 = new SeamInfo(pixel2R1);
    SeamInfo secondv3 = new SeamInfo(pixel2R2, firstv3);
    SeamInfo thirdv3 = new SeamInfo(pixel2R3, secondv3);

    // small world examples
    SeamInfo sWFirst = new SeamInfo(sWpixel1R1);
    SeamInfo sWSecond = new SeamInfo(sWpixel2R2, sWFirst);
    SeamInfo sWThird = new SeamInfo(sWpixel3R3, sWSecond);
    SeamInfo sWFourth = new SeamInfo(sWpixel3R4, sWThird);

    SeamInfo sWFirstv2 = new SeamInfo(sWpixel1R1);
    SeamInfo sWSecondv2 = new SeamInfo(sWpixel1R2, sWFirstv2);
    SeamInfo sWThirdv2 = new SeamInfo(sWpixel2R3, sWSecondv2);
    SeamInfo sWFourthv2 = new SeamInfo(sWpixel2R4, sWThirdv2);

    SeamInfo sWFirstv3 = new SeamInfo(sWpixel3R1);
    SeamInfo sWSecondv3 = new SeamInfo(sWpixel3R2, sWFirstv3);
    SeamInfo sWThirdv3 = new SeamInfo(sWpixel2R3, sWSecondv3);
    SeamInfo sWFourthv3 = new SeamInfo(sWpixel1R4, sWThirdv3);

    sWFourth.makeRed();
    sWSecondv2.makeRed();
    sWFirstv3.makeRed();

    return t.checkExpect(sWpixel1R1.color, Color.RED) && t.checkExpect(sWpixel2R2.color, Color.RED)
        && t.checkExpect(sWpixel3R3.color, Color.RED) && t.checkExpect(sWpixel3R4.color, Color.RED)
        && t.checkExpect(sWpixel1R2.color, Color.RED) && t.checkExpect(sWpixel1R1.color, Color.RED)
        && t.checkExpect(sWpixel3R1.color, Color.RED);
  }
}