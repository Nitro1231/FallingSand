/**
 * SandLab.java: an expandible falling-sand game.
 * My custom particles and their are listed below.
 *
 * Metal
 * Sand
 * Water
 * Oil
 * Wood
 * Leaf
 * Ice
 * Fire
 * Lava
 * Stone
 * Obsidian
 * TNT
 * Soil
 * Steam
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

  //add constants for particle types here
  public static final int EMPTY = 0;
  public static final int METAL = 1;      // JUST STAY THERE
  public static final int SAND = 2;       // *SAND* < WATER, OIL, STEAM, GAS
  public static final int WATER = 3;      // SAND < *WATER* < OIL, GAS
  public static final int OIL = 4;        // Work same as WATER, but above WATER
  public static final int WOOD = 5;       // SAME AS METAL, but burnable
  public static final int LEAF = 6;       // SAME AS METAL, but burnable
  public static final int ICE = 7;        // Freeze water / Melt by FIRE
  public static final int FIRE = 8;       // Interval: TNT, GAS < OIL < LEAF < WOOD < WATER < ICE / Melt ICE / Form STEAM when WATER or ICE is exist
  public static final int LAVA = 9;       // If WATER is under the LAVA ==> STONE / If Water is above the LAVA ==> OBSIDIAN
  public static final int STONE = 10;     // Fall down straightly
  public static final int OBSIDIAN = 11;  // Same as metal
  public static final int TNT = 12;       // Explosion / React with FIRE and LAVA
  public static final int STEAM = 13;     // SAND, SOIL, GAS, OIL, WATER < STEAM
  public static final int GLASS = 14;     // Formed when SAND react with LAVA and FIRE
  public static final int GAS = 15;       // Explosion / React with FIRE and LAVA

  public static final int CLEAR = 16;     // Clear all

  // Do not add any more fields
  private int[][] grid;
  private SandDisplay display;

  public SandLab(int numRows, int numCols) {
    grid = new int[numRows][numCols]; // Initialize the board.
    String[] names;
    names = new String[17];
    names[EMPTY] = "Empty";
    names[METAL] = "Metal";
    names[SAND] = "Sand";
    names[WATER] = "Water";
    names[OIL] = "Oil";
    names[WOOD] = "Wood";
    names[LEAF] = "Leaf";
    names[ICE] = "Ice";
    names[FIRE] = "Fire";
    names[LAVA] = "Lava";
    names[STONE] = "Stone";
    names[OBSIDIAN] = "Obsidian";
    names[TNT] = "TNT";
    names[STEAM] = "Steam";
    names[GLASS] = "Glass";
    names[GAS] = "Gas";
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
          case METAL:
            display.setColor(row, col, new Color(40, 40, 40));
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
          case WOOD:
            display.setColor(row, col, new Color(120, 60, 0));
            break;
          case LEAF:
            display.setColor(row, col, new Color(70, 160, 0));
            break;
          case ICE:
            display.setColor(row, col, new Color(170, 220, 255));
            break;
          case FIRE:
            display.setColor(row, col, new Color(215 + offset, 100 + offset, 0));
            break;
          case LAVA:
            display.setColor(row, col, new Color(215 + offset, 50 + offset, 30 + offset));
            break;
          case STONE:
            display.setColor(row, col, new Color(150, 150, 150));
            break;
          case OBSIDIAN:
            display.setColor(row, col, new Color(110, 0, 180));
            break;
          case TNT:
            display.setColor(row, col, new Color(170, 0, 0));
            break;
          case STEAM:
            display.setColor(row, col, new Color(120 + offset, 210 + offset, 255));
            break;
          case GLASS:
            display.setColor(row, col, new Color(230, 230, 230));
            break;
          case GAS:
            display.setColor(row, col, new Color(230 + offset, 160 + offset, 190 + offset));
            break;
        }
      }
    }
  }

  // Called repeatedly.
  // Causes one random particle to maybe do something.
  public void step() {
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
        if (row < grid.length - 1 && delay(20)) {
          if (grid[row + 1][col] == WATER) {
            if (delay(5)){
              grid[row][col] = STEAM;
              grid[row + 1][col] = STONE;
            } else {
              grid[row][col] = STONE;
            }
          } else if (row > 0 && grid[row - 1][col] == WATER) {
            grid[row][col] = STEAM;
            grid[row + 1][col] = OBSIDIAN;
          }

          int[] intObj = {EMPTY, STEAM, GAS, FIRE};
          waterPhysics(row, col, LAVA, intObj); // Apply water physics to LAVA.
        }

        int[] burnableObj = {TNT, GAS, OIL, LEAF, WOOD, WATER, ICE, SAND};
        int[] newLoc = surroundCheck(row, col);
        int type = checkType(grid[newLoc[0]][newLoc[1]], burnableObj);
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
            if (delay(10))
              grid[newLoc[0]][newLoc[1]] = FIRE;
            break;
          case WOOD:
            if (delay(20))
              grid[newLoc[0]][newLoc[1]] = FIRE;
            break;
          case ICE:
            if (newLoc[0] - 1 > 0 && delay(40)) {
              grid[row][col] = LAVA;
              grid[newLoc[0]][newLoc[1]] = WATER;
              grid[newLoc[0] - 1][newLoc[1]] = STEAM;
            }
            break;
          case SAND:
            grid[row][col] = EMPTY;
            grid[newLoc[0]][newLoc[1]] = GLASS;
            break;
        }
        break;
      }
      case FIRE: {
        int[] burnableObj = {FIRE, TNT, GAS, OIL, LEAF, WOOD, WATER, ICE, SAND};
        int[] newLoc = surroundCheck(row, col);
        int type = checkType(grid[newLoc[0]][newLoc[1]], burnableObj);
        switch (type) {
          case FIRE:
            if (delay(80))
              grid[newLoc[0]][newLoc[1]] = EMPTY;
            if (newLoc[0] - 1 > 0 && delay(85))
              if (grid[newLoc[0] - 1][newLoc[1]] == EMPTY)
                grid[newLoc[0] - 1][newLoc[1]] = FIRE;
            break;
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
          case WATER:
            if (newLoc[0] - 1 > 0 && delay(100)) {
              grid[row][col] = EMPTY;
              grid[newLoc[0]][newLoc[1]] = FIRE;
              grid[newLoc[0] - 1][newLoc[1]] = STEAM;
            }
            break;
          case ICE:
            if (newLoc[0] - 1 > 0 && delay(200)) {
              grid[row][col] = EMPTY;
              grid[newLoc[0]][newLoc[1]] = WATER;
              grid[newLoc[0] - 1][newLoc[1]] = STEAM;
            }
            break;
          case SAND:
            if (delay(20)) {
              grid[row][col] = GLASS;
              grid[newLoc[0]][newLoc[1]] = GLASS;
            }
            break;
        }
        break;
      }
      case ICE: {
        if (delay(15)) {
          int[] newLoc = surroundCheck(row, col);
          if (grid[newLoc[0]][newLoc[1]] == WATER)
            grid[newLoc[0]][newLoc[1]] = ICE;
          if (grid[newLoc[0]][newLoc[1]] == STEAM)
            grid[newLoc[0]][newLoc[1]] = WATER;
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
      case STEAM: {
        if (row > 0 && delay(10)) {
          int[] intObj = {EMPTY, SAND, GAS, OIL, WATER, LAVA, FIRE}; // Interactable Objects

          int type = checkType(grid[row - 1][col], intObj); // Can be -1, EMPTY, or WATER.
          if (type != -1) { // If nothing or water exist under the sand, fall down one row.
            grid[row][col] = type;
            grid[row - 1][col] = STEAM;
          } else if (grid[row - 1][col] == STEAM) {
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
      case GAS: {
        if (delay(5))
          return;

        int newRow = row, newCol = col;
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
        if (grid[newRow][newCol] == EMPTY) {
          grid[row][col] = EMPTY;
          grid[newRow][newCol] = GAS;
        }
        break;
      }
      case CLEAR: {
        grid = new int[grid.length][grid[0].length];
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

  /** Randomly select one of the 3 * 3 areas that is adjacent to the object.
   * @return Array of int that hold row and col value. First array would hold row and second would hold col.
   */
  public int[] surroundCheck (int row, int col) {
    int[] newLoc = {row, col};
    switch ((int) (Math.random() * 9) + 1) {
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