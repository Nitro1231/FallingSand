/**
 * SandLab.java: an expandible falling-sand game.
 * My custom particles and their are listed below.
 *
 * **See attached MAP.png**
 *
 * Empty:
 *   Eraser...
 *
 * Sand:
 *   Falling down in form of triangle.
 *   Sink down when reacted with WATER, LAVA, STEAM, OIL, and GAS (or more).
 *
 *   Falling down, check which side does not have water, and filled that equally as possible.
 *
 * Oil:
 *   Same as WATER, but stay above the WATER.
 *   Burnable object, and burn fast.
 *
 * Lava:
 *   Works similar as Fire and WATER.
 *   It will form STONE if WATER is under the LABA.
 *   It will form OBSIDIAN if WATER is above the LABA.
 *
 * Fire:
 *   Burn up things or spread fire at different speeds.Or disappear after a random time.
 *   Just burn in different speed: FIRE, TNT, GAS, OIL, LEAF, WOOD
 *   WATER: formed STEAM when it reacts with.
 *   ICE: formed STEAM and WATER when it reacts with.
 *   SAND: turn into glass.
 *
 * Ice:
 *   Freeze WATER. When it reacts with STEAM, formed WATER.
 *
 * Steam:
 *   Works similarly with WATER, but upside down.
 *   It will be formed when WATER reacts with FIRE or LAVA.
 *   Formed WATER when it reacts with ICE.
 *
 * Stone:
 *   Fall down straightly.
 *
 * Metal:
 *   Just stay at position, not burnable.
 *
 * Obsidian:
 *   Same as metal; It will formed when WATER is above the LABA.
 *
 * Glass:
 *   Same as metal; It will be formed when SAND is heated.
 *
 * Wood:
 *   Same as METAL, but burnable.
 *   WOOD burns slower than LEAF.
 *
 * Leaf:
 *   Same as METAL, but burnable.
 *   LEAF burns faster than WOOD.
 *
 * TNT:
 *   Burn Fastly... (no idea how to make it explode)
 *
 * Gas:
 *   Spread randomly; Burn fast as TNT.
 *
 * Virus:
 *   Randomly spread through the object, and randomly die.
 *
 * Clear:
 *   Clear all.
 *
 * @author Jun Park
 */

import java.awt.*;
import java.util.*;

public class SandLab {
  public static void main(String[] args) {
    SandLab lab = new SandLab(150, 100); // The window dimensions. Change if you want a larger/smaller area.
    lab.run();
  }

  // Add constants for particle types here
  public static final int EMPTY = 0;
  public static final int SAND = 1;
  public static final int WATER = 2;
  public static final int OIL = 3;
  public static final int LAVA = 4;
  public static final int FIRE = 5;
  public static final int ICE = 6;
  public static final int STEAM = 7;
  public static final int STONE = 8;
  public static final int METAL = 9;
  public static final int OBSIDIAN = 10;
  public static final int GLASS = 11;
  public static final int WOOD = 12;
  public static final int LEAF = 13;
  public static final int TNT = 14;
  public static final int GAS = 15;
  public static final int VIRUS = 16;

  public static final int CLEAR = 17;

  // Do not add any more fields
  private int[][] grid;
  private SandDisplay display;

  public SandLab(int numRows, int numCols) {
    grid = new int[numRows][numCols]; // Initialize the board.
    String[] names;
    names = new String[18];
    names[EMPTY] = "Empty";
    names[SAND] = "Sand";
    names[WATER] = "Water";
    names[OIL] = "Oil";
    names[LAVA] = "Lava";
    names[FIRE] = "Fire";
    names[ICE] = "Ice";
    names[STEAM] = "Steam";
    names[STONE] = "Stone";
    names[METAL] = "Metal";
    names[OBSIDIAN] = "Obsidian";
    names[GLASS] = "Glass";
    names[WOOD] = "Wood";
    names[LEAF] = "Leaf";
    names[TNT] = "TNT";
    names[GAS] = "Gas";
    names[VIRUS] = "Virus";
    names[CLEAR] = "Clear";
    display = new SandDisplay("Falling Sand - Jun Park", numRows, numCols, names);
  }

  // Called when the user clicks on a location using the given tool
  private void locationClicked(int row, int col, int tool) {
    grid[row][col] = tool;
  }

  // Copies each element of grid into the display
  public void updateDisplay() {
    // Go though all grid.
    for(int row = 0; row < grid.length; row++) {
      for(int col = 0; col < grid[0].length; col++) {
        int offset = (int)(Math.random()*40-20); // Random color offset. (Range: -20~20)
        switch(grid[row][col]) {
          case EMPTY:
            display.setColor(row, col, new Color(10, 10, 10));
            break;
          case SAND:
            display.setColor(row, col, new Color(255, 200, 0));
            break;
          case WATER:
            // Colors that are changing its color randomly should be in the correct range. Since offsets can be -20 to 20, it should be at least 20 up to 235.
            display.setColor(row, col, new Color(30 + offset, 128 + offset, 255));
            break;
          case OIL:
            display.setColor(row, col, new Color(40 + offset, 30 + offset, 5));
            break;
          case LAVA:
            display.setColor(row, col, new Color(215 + offset, 50 + offset, 30 + offset));
            break;
          case FIRE:
            display.setColor(row, col, new Color(215 + offset, 100 + offset, 0));
            break;
          case ICE:
            display.setColor(row, col, new Color(170, 220, 255));
            break;
          case STEAM:
            display.setColor(row, col, new Color(120 + offset, 210 + offset, 255));
            break;
          case STONE:
            display.setColor(row, col, new Color(150, 150, 150));
            break;
          case METAL:
            display.setColor(row, col, new Color(40, 40, 40));
            break;
          case OBSIDIAN:
            display.setColor(row, col, new Color(110, 0, 180));
            break;
          case GLASS:
            display.setColor(row, col, new Color(230, 230, 230));
            break;
          case WOOD:
            display.setColor(row, col, new Color(120, 60, 0));
            break;
          case LEAF:
            display.setColor(row, col, new Color(70, 160, 0));
            break;
          case TNT:
            display.setColor(row, col, new Color(170, 0, 0));
            break;
          case GAS:
            display.setColor(row, col, new Color(230 + offset, 160 + offset, 190 + offset));
            break;
          case VIRUS:
            display.setColor(row, col, new Color(100 + offset, 225 + offset, 30 + offset));
            break;
        }
      }
    }
  }

  // Called repeatedly.
  // Causes one random particle to maybe do something.
  public void step() {
    // Randomly pick one particle from the grid.
    int row = (int)(Math.random() * grid.length);
    int col = (int)(Math.random() * grid[0].length);
    switch(grid[row][col]) {
      case SAND: {
        if (row < grid.length - 1 && delay(3)) { // If the SAND is not reached to the bottom yet.
          int[] intObj = {EMPTY, WATER, OIL, LAVA, STEAM, GAS}; // Interactable Objects
          int type = checkType(grid[row + 1][col], intObj); // Check what is exist under the current object. Return -1 if the object is not a interactable.

          if (type != -1) { // If an interactable object exists under the SAND, swap the object with SAND.
            grid[row][col] = type;
            grid[row + 1][col] = SAND;
          } else if (grid[row + 1][col] == SAND) { // If there is a another SAND exist under the SAND, try to make SAND triangle form.
            if (col >= grid[0].length - 1) { // If the location is rightmost
              int left = checkType(grid[row + 1][col - 1], intObj); // Check what is exist on left side.
              if (left != -1) { // If the left side is a interactable Objects, swap the object with SAND.
                grid[row][col] = left;
                grid[row + 1][col - 1] = SAND;
              }
              return; // Escape the case to prevent the other if statement.
            } else if (col <= 0) { // If the location is leftmost
              int right = checkType(grid[row + 1][col + 1], intObj);
              if (right != -1) { // If the right side is a interactable Objects, swap the object with SAND.
                grid[row][col] = right;
                grid[row + 1][col + 1] = SAND;
              }
              return; // Escape the case to prevent the other if statement.
            }

            // If the SAND is not located at either rightmost nor leftmost, check both side.
            int left = checkType(grid[row + 1][col - 1], intObj);
            int right = checkType(grid[row + 1][col + 1], intObj);
            if (left != -1 && right != -1) { // If either left or right side of the bottom SAND is an interactable Objects,
              int LR = (int) (Math.random() * 2); // Randomly swap the SAND with either left or right side object.
              if (LR == 0) {
                grid[row][col] = right; // Swap with right side obj.
                grid[row + 1][col + 1] = SAND;
              } else {
                grid[row][col] = left; // Swap with left side obj.
                grid[row + 1][col - 1] = SAND;
              }
            } else if (right != -1) { // If the right side of the bottom SAND is an interactable Objects, swap the object.
              grid[row][col] = right;
              grid[row + 1][col + 1] = SAND;
            } else if (left != -1) { // If the right side of the bottom SAND is an interactable Objects, swap the object.
              grid[row][col] = left;
              grid[row + 1][col - 1] = SAND;
            }
          }
        }
        break; // End of SAND mechanism.
      }
      case WATER: {
        if (row < grid.length - 1 && delay(3)) { // If the WATER is not reached to the bottom yet.
          int[] intObj = {EMPTY, OIL, STEAM, GAS, FIRE}; // Interactable Objects
          waterPhysics(row, col, WATER, intObj); // Apply water physics.
        }
        break;
      }
      case OIL: {
        if (row < grid.length - 1 && delay(3)) {
          int[] intObj = {EMPTY}; // Interactable Objects
          waterPhysics(row, col, OIL, intObj); // Apply water physics to OIL.
        }
        break;
      }
      case LAVA: {
        if (row < grid.length - 1 && delay(20)) { // Add delay to make it slower than WATER and OIL.
          if (grid[row + 1][col] == WATER) { // If LAVA is exist above the WATER,
            grid[row][col] = STEAM;
            grid[row + 1][col] = STONE; // Formed STONE.
          } else if (row > 0 && grid[row - 1][col] == WATER) { // If WATER is exist above the LAVA,
            grid[row][col] = STEAM;
            grid[row + 1][col] = OBSIDIAN; // Formed OBSIDIAN.
          }
          int[] intObj = {EMPTY, STEAM, GAS, FIRE}; // Objects that LAVA will ignore.
          waterPhysics(row, col, LAVA, intObj); // Apply water physics to LAVA.
        }
        int[] burnableObj = {TNT, GAS, OIL, LEAF, WOOD, SAND, ICE, VIRUS}; // Burnable Objects; not including WATER and ICE, because we code already handle it in 272~278 line.
        burnObject(row, col, burnableObj); // Randomly select object in 3*3 area, and burn it.
        break;
      }
      case FIRE: {
        if (delay(80)) // Self destroy.
          grid[row][col] = EMPTY;
        if (row - 1 > 0 && delay(85)) { // Spread upward.
          if (grid[row - 1][col] == EMPTY)
            grid[row - 1][col] = FIRE;
        }
        int[] burnableObj = {TNT, GAS, OIL, LEAF, WOOD, WATER, ICE, SAND, VIRUS}; // Burnable Objects.
        burnObject(row, col, burnableObj); // Randomly select object in 3*3 area, and burn it.
        break;
      }
      case ICE: {
        if (delay(15)) {
          int[] newLoc = surroundCheck(row, col); // Get random loc
          if (grid[newLoc[0]][newLoc[1]] == WATER) // If there is WATER near by ICE, frozen it.
            grid[newLoc[0]][newLoc[1]] = ICE;
          if (grid[newLoc[0]][newLoc[1]] == STEAM) // If there is STEAM near by ICE, form the WATER.
            grid[newLoc[0]][newLoc[1]] = WATER;
        }
        break;
      }
      case STEAM: { // STEAM is basically inverse of WATER.
        if (row > 0 && delay(10)) {
          int[] intObj = {EMPTY, SAND, GAS, OIL, WATER, LAVA, FIRE}; // Interactable Objects

          int type = checkType(grid[row - 1][col], intObj); // Check if there is any interactable Objects above the STEAM.
          if (type != -1) { // If there is a interactable Objects exist, swap the object.
            grid[row][col] = type;
            grid[row - 1][col] = STEAM;
          } else if (grid[row - 1][col] == STEAM) { // If there is a STEAM exist above the STEAM, Do same thing with WATER but inverse.
            int leftEmpty = 0;
            int rightEmpty = 0;
            int left = 0, right = 0;
            for (int i = 0; i < col + 1; i++) {
              left = checkType(grid[row - 1][col - i], intObj);
              if (left != -1) {
                leftEmpty = i;
                break;
              } else if (grid[row - 1][col - i] != STEAM) {
                break;
              }
            }
            for (int i = 0; i < grid[0].length - col; i++) {
              right = checkType(grid[row - 1][col + i], intObj);
              if (right != -1) {
                rightEmpty = i;
                break;
              } else if (grid[row - 1][col + i] != STEAM) {
                break;
              }
            }
            if (leftEmpty == rightEmpty && leftEmpty == 0) {
              int rd = (int) (Math.random() * 3) - 1;
              if (col + rd >= 0 && col + rd <= grid[0].length - 1) {
                int change = checkType(grid[row][col + rd], intObj);
                if (change != -1) {
                  grid[row][col] = change;
                  grid[row][col + rd] = STEAM;
                }
              }
            } else {
              if (rightEmpty == 0 || leftEmpty >= rightEmpty) {
                grid[row][col] = left;
                grid[row - 1][col - leftEmpty] = STEAM;
              } else if (leftEmpty == 0) {
                grid[row][col] = right;
                grid[row - 1][col + rightEmpty] = STEAM;
              }
            }
          }
        }
        break;
      }
      case STONE: {
        if (row < grid.length - 1 && delay(5)) { // If the sand is not reached to the bottom yet.
          int[] intObj = {EMPTY, WATER, OIL, LAVA, GAS, FIRE, STEAM}; // Interactable Objects
          int type = checkType(grid[row + 1][col], intObj); // Can be -1, EMPTY, or WATER.

          if (type != -1) { // If nothing or water exist under the sand, fall down one row.
            grid[row][col] = type;
            grid[row + 1][col] = STONE;
          }
        }
        break;
      }
      case GAS: {
        if (delay(5))
          return;

        int newRow = row, newCol = col; // Get random location but only top, left, right, and bottom. Otherwise, the gas will escape from the container sometimes.
        switch ((int) (Math.random() * 5) + 1) {
          case 1: // Top
            newRow = row - 1;
            break;
          case 2: // Left
            newCol = col - 1;
            break;
          case 3: // Right
            newCol = col + 1;
            break;
          case 4: // Bottom
            newRow = row + 1;
            break;
        }
        if (newRow < 0 || newRow > grid.length - 1 || newCol < 0 || newCol > grid[0].length - 1) {
          newRow = row;
          newCol = col;
        }
        if (grid[newRow][newCol] == EMPTY) { // Swap object.
          grid[row][col] = EMPTY;
          grid[newRow][newCol] = GAS;
        }
        break;
      }
      case VIRUS: {
        if (delay(5)) // Delay.
          return;

        if (delay(100)) {
          int[] newLoc = surroundCheck(row, col); // Pick random location.
          if (grid[newLoc[0]][newLoc[1]] != EMPTY) // If the location is not empty.
            grid[newLoc[0]][newLoc[1]] = VIRUS; // Infect object.
        }
        if (delay(400)) // Destroy itself.
          grid[row][col] = EMPTY;
        break;
      }
      case CLEAR: { // Clear all.
        grid = new int[grid.length][grid[0].length]; // Initialize the board again.
        break;
      }
      default: {
        break;
      }
    }
  }

  /** Check if obj exists in the array.
   *  @return Return the block id (int) if the obj exists in the array.
   *          Return -1 if obj does not exist in the array.
   */
  public int checkType(int obj, int[] arr) {
    for (int i : arr)
      if (obj == i)
        return i;
    return -1;
  }

  /** Water Physics... This physics will apply on WATER, OIL, and LAVA.
   * @param obj Main object that the physics will apply on.
   * @param intObj Int array that holds the interactable Objects
   */
  public void waterPhysics(int row, int col, int obj, int[] intObj) {
    int type = checkType(grid[row + 1][col], intObj); // Check what is exist under the current object. Return -1 if the object is not a interactable.

    if (type != -1) { // If an interactable object exists under the WATER, swap the object with WATER.
      grid[row][col] = type;
      grid[row + 1][col] = obj;
    } else if (grid[row + 1][col] == obj) { // If there is a another WATER exist under the WATER,
      // This chunk of code will find the closest empty place form the under row, and filled the water to that place.
      // This mechanism will make WATER acts more like a WATER than just random movement.
      int leftEmpty = 0; // Closest empty spot from the left side.
      int rightEmpty = 0; // Closest empty spot from the right side.
      int left = 0, right = 0;
      for (int i = 0; i < col + 1; i++) {
        left = checkType(grid[row + 1][col - i], intObj);
        if (left != -1) {
          leftEmpty = i;
          break;
        } else if (grid[row + 1][col - i] != obj) {
          break;
        }
      }
      for (int i = 0; i < grid[0].length - col; i++) {
        right = checkType(grid[row + 1][col + i], intObj);
        if (right != -1) {
          rightEmpty = i;
          break;
        } else if (grid[row + 1][col + i] != obj) {
          break;
        }
      }
      if (leftEmpty == rightEmpty && leftEmpty == 0) {
        int rd = (int) (Math.random() * 3) - 1;
        if (col + rd >= 0 && col + rd <= grid[0].length - 1) {
          int change = checkType(grid[row][col + rd], intObj);
          if (change != -1) {
            grid[row][col] = change;
            grid[row][col + rd] = obj;
          }
        }
      } else {
        if (rightEmpty == 0 || leftEmpty >= rightEmpty) {
          grid[row][col] = left;
          grid[row + 1][col - leftEmpty] = obj;
        } else if (leftEmpty == 0) {
          grid[row][col] = right;
          grid[row + 1][col + rightEmpty] = obj;
        }
      }
    }
  }

  /** Burn objects... This physics will apply on FIRE and LAVA.
   * @param burnableObj Array of int that holds the objects that are burnable.
   */
  public void burnObject(int row, int col, int[] burnableObj){
    int[] newLoc = surroundCheck(row, col); // Get random location in 3*3 area.
    int type = checkType(grid[newLoc[0]][newLoc[1]], burnableObj); // Check which object is exists at newLoc.
    switch (type) {
      case TNT:
      case GAS:
        if (delay(3))
          grid[newLoc[0]][newLoc[1]] = FIRE;
        break;
      case OIL:
        if (delay(7))
          grid[newLoc[0]][newLoc[1]] = FIRE;
        break;
      case LEAF:
        if (delay(20))
          grid[newLoc[0]][newLoc[1]] = FIRE;
        break;
      case WOOD:
        if (delay(30))
          grid[newLoc[0]][newLoc[1]] = FIRE;
        break;
      case VIRUS:
        if (delay(15))
          grid[newLoc[0]][newLoc[1]] = FIRE;
        break;
      case WATER: // Formed STEAM when WATER evaporate.
        if (newLoc[0] - 1 > 0 && delay(100)) {
          grid[row][col] = EMPTY;
          grid[newLoc[0]][newLoc[1]] = FIRE;
          grid[newLoc[0] - 1][newLoc[1]] = STEAM;
        }
        break;
      case ICE: // Formed STEAM when ICE melted.
        if (newLoc[0] - 1 > 0 && delay(200)) {
          grid[row][col] = EMPTY;
          grid[newLoc[0]][newLoc[1]] = WATER;
          grid[newLoc[0] - 1][newLoc[1]] = STEAM;
        }
        break;
      case SAND: // Turn SAND into the GLASS
        if (delay(20)) {
          grid[row][col] = GLASS;
          grid[newLoc[0]][newLoc[1]] = GLASS;
        }
        break;
    }
  }

  /** Randomly select one of the 3 * 3 areas that is adjacent to the object.
   * @return Array of int that hold row and col value. First array would hold row and second would hold col.
   */
  public int[] surroundCheck (int row, int col) {
    int[] newLoc = {row, col};
    switch ((int) (Math.random() * 9) + 1) { // Randomly select the area.
      case 1: // Left && Top
        newLoc[0] = row - 1;
        newLoc[1] = col - 1;
        break;
      case 2: // Top
        newLoc[0] = row - 1;
        break;
      case 3: // Top && Right
        newLoc[0] = row - 1;
        newLoc[1] = col + 1;
        break;
      case 4: // Left
        newLoc[1] = col - 1;
        break;
      case 5: // Right
        newLoc[1] = col + 1;
        break;
      case 6: // Bottom && Left
        newLoc[0] = row + 1;
        newLoc[1] = col - 1;
        break;
      case 7: // Bottom
        newLoc[0] = row + 1;
        break;
      case 8: // Bottom && Right
        newLoc[0] = row + 1;
        newLoc[1] = col + 1;
        break;
    }
    // Check if newLoc is out of the bound.
    if (newLoc[0] < 0 || newLoc[0] > grid.length - 1 || newLoc[1] < 0 || newLoc[1] > grid[0].length - 1) {
      newLoc[0] = row;
      newLoc[1] = col;
    }
    return newLoc;
  }

  /** This method will call a random number with a range of 1 ~ interval. If the number is 1, this method will return true; otherwise, it will return false.
   * @param interval High interval will increase the delay.
   * @return boolean; it will return true if random number is 1, otherwise, return false.
   */
  public boolean delay(int interval) {
    return ((int) (Math.random() * interval) + 1) == 1;
  }

  // Do not modify
  public void run() {
    while (true) {
      for (int i = 0; i < display.getSpeed(); i++)
        step();
      updateDisplay();
      display.repaint();
      display.pause(1);  // Wait for redrawing and for mouse
      int[] mouseLoc = display.getMouseLocation();
      if (mouseLoc != null)  // Test if mouse clicked
        locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
    }
  }
}