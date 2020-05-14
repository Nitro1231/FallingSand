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

//https://sandspiel.club/

public class SandLab {
  public static void main(String[] args) {
    SandLab lab = new SandLab(150, 100); // the window dimensions. change if you want a larger/smaller area
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

  //do not add any more fields
  private int[][] grid;
  private SandDisplay display;

  public SandLab(int numRows, int numCols) {
    grid = new int[numRows][numCols];
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
    display = new SandDisplay("Falling Sand", numRows, numCols, names);
  }

  //called when the user clicks on a location using the given tool
  private void locationClicked(int row, int col, int tool) {
    grid[row][col] = tool;
  }

  //copies each element of grid into the display
  public void updateDisplay() {
    for(int row = 0; row < grid.length; row++) {
      for(int col = 0; col < grid[0].length; col++) {
        int offset = (int)(Math.random()*40-20);
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

  //called repeatedly.
  //causes one random particle to maybe do something.
  public void step() {
    int row = (int)(Math.random() * grid.length);
    int col = (int)(Math.random() * grid[0].length);
    switch(grid[row][col]) {
      case SAND: {
        if (row < grid.length - 1) { // If the sand is not reached to the bottom yet.
          int[] intObj = {EMPTY, WATER, OIL, LAVA, STEAM, GAS}; // Interactable Objects
          int type = checkType(grid[row + 1][col], intObj); // Can be -1, EMPTY, or WATER.

          if (type != -1) { // If nothing or water exist under the sand, fall down one row.
            grid[row][col] = type;
            grid[row + 1][col] = SAND;
          } else if (grid[row + 1][col] == SAND) { // If there is a another sand exist under the sand.
            if (col >= grid[0].length - 1) { // If the location is rightmost
              int left = checkType(grid[row + 1][col - 1], intObj);
              if (left != -1) { // Check left side
                grid[row][col] = left;
                grid[row + 1][col - 1] = SAND;
              }
              return;
            } else if (col <= 0) { // If the location is leftmost
              int right = checkType(grid[row + 1][col + 1], intObj);
              if (right != -1) { // Check right side
                grid[row][col] = right;
                grid[row + 1][col + 1] = SAND;
              }
              return;
            }

            int left = checkType(grid[row + 1][col - 1], intObj);
            int right = checkType(grid[row + 1][col + 1], intObj);
            if (left != -1 && right != -1) { // If either left or right side of the bottom sand is empty,
              int LR = (int) (Math.random() * 2); // randomly assigned the sand either left or right side.
              if (LR == 0) {
                grid[row][col] = right; // Exchange with right side obj.
                grid[row + 1][col + 1] = SAND;
              } else {
                grid[row][col] = left; // Exchange with left side obj.
                grid[row + 1][col - 1] = SAND;
              }
            } else if (right != -1) { // If the right side of the bottom sand is empty,
              grid[row][col] = right;
              grid[row + 1][col + 1] = SAND;
            } else if (left != -1) { // If the right side of the bottom sand is empty,
              grid[row][col] = left;
              grid[row + 1][col - 1] = SAND;
            }
          }
        }
        break;
      }
      case WATER: {
        if (row < grid.length - 1) {
          int[] intObj = {EMPTY, OIL, STEAM, GAS, FIRE};

          int type = checkType(grid[row + 1][col], intObj);
          if (type != -1) {
            grid[row][col] = type;
            grid[row + 1][col] = WATER;
          } else if (grid[row + 1][col] == WATER) {
            int leftEmpty = 0;
            int rightEmpty = 0;
            int left = 0, right = 0;
            for (int i = 0; i < col + 1; i++) {
              left = checkType(grid[row + 1][col - i], intObj);
              if (left != -1) {
                leftEmpty = i;
                break;
              } else if (grid[row + 1][col - i] != WATER) {
                break;
              }
            }
            for (int i = 0; i < grid[0].length - col; i++) {
              right = checkType(grid[row + 1][col + i], intObj);
              if (right != -1) {
                rightEmpty = i;
                break;
              } else if (grid[row + 1][col + i] != WATER) {
                break;
              }
            }
            if (leftEmpty == rightEmpty && leftEmpty == 0) {
              int rd = (int) (Math.random() * 3) - 1;
              if (col + rd >= 0 && col + rd <= grid[0].length - 1) {
                int change = checkType(grid[row][col + rd], intObj);
                if (change != -1) {
                  grid[row][col] = change;
                  grid[row][col + rd] = WATER;
                }
              }
            } else {
              if (leftEmpty > rightEmpty) {
                grid[row][col] = left;
                grid[row + 1][col - leftEmpty] = WATER;
              } else {
                grid[row][col] = right;
                grid[row + 1][col + rightEmpty] = WATER;
              }
            }
          }
        }
        break;
      }
      case OIL: {
        if (row < grid.length - 1) {
          if (grid[row + 1][col] == EMPTY) {
            grid[row][col] = EMPTY;
            grid[row + 1][col] = OIL;
          } else if (grid[row + 1][col] == OIL) {
            int leftEmpty = 0;
            int rightEmpty = 0;
            for (int i = 0; i < col + 1; i++) {
              if (grid[row + 1][col - i] == EMPTY) {
                leftEmpty = i;
                break;
              } else if (grid[row + 1][col - i] != OIL) {
                break;
              }
            }
            for (int i = 0; i < grid[0].length - col; i++) {
              if (grid[row + 1][col + i] == EMPTY) {
                rightEmpty = i;
                break;
              } else if (grid[row + 1][col + i] != OIL) {
                break;
              }
            }
            if (leftEmpty == rightEmpty && leftEmpty == 0) {
              int rd = (int) (Math.random() * 3) - 1;
              if (col + rd > 1 && col + rd < grid[0].length - 2) {
                if (grid[row][col + rd] == EMPTY) {
                  grid[row][col] = EMPTY;
                  grid[row][col + rd] = OIL;
                }
              }
            } else {
              grid[row][col] = EMPTY;
              if (leftEmpty > rightEmpty)
                grid[row + 1][col - leftEmpty] = OIL;
              else
                grid[row + 1][col + rightEmpty] = OIL;
            }
          }
        }
        break;
      }
      case ICE: {
        switch ((int) (Math.random() * 100) + 1) { // 4/100 chance
          case 1: // Up
            if (row - 1 > 0 && grid[row - 1][col] == WATER)
              grid[row - 1][col] = ICE;
            if (row - 1 > 0 && grid[row - 1][col] == STEAM)
              grid[row - 1][col] = WATER;
            break;
          case 2: // Down
            if (row + 1 < grid.length && grid[row + 1][col] == WATER)
              grid[row + 1][col] = ICE;
            if (row + 1 < grid.length && grid[row + 1][col] == STEAM)
              grid[row + 1][col] = WATER;
            break;
          case 3: // Left
            if (col > 0 && grid[row][col - 1] == WATER)
              grid[row][col - 1] = ICE;
            if (col > 0 && grid[row][col - 1] == STEAM)
              grid[row][col - 1] = WATER;
            break;
          case 4: // Right
            if (col < grid[0].length - 1 && grid[row][col + 1] == WATER)
              grid[row][col + 1] = ICE;
            if (col < grid[0].length - 1 && grid[row][col + 1] == STEAM)
              grid[row][col + 1] = WATER;
            break;
        }
        break;
      }
      case FIRE: {
        int[] burnableObj = {FIRE, TNT, GAS, OIL, LEAF, WOOD, WATER, ICE, SAND};
        int newRow = row, newCol = col;
        switch ((int) (Math.random() * 9) + 1) {
          case 1: // Left && Top
            newRow = row - 1;
            newCol = col - 1;
            break;
          case 2: // Top
            newRow = row - 1;
            break;
          case 3: // Top && Right
            newRow = row - 1;
            newCol = col + 1;
            break;
          case 4: // Left
            newCol = col - 1;
            break;
          case 5: // Right
            newCol = col + 1;
            break;
          case 6: // Bottom && Left
            newRow = row + 1;
            newCol = col - 1;
            break;
          case 7: // Bottom
            newRow = row + 1;
            break;
          case 8: // Bottom && Right
            newRow = row + 1;
            newCol = col + 1;
            break;
        }
        if (newRow < 0 || newRow > grid.length - 1 || newCol < 0 || newCol > grid[0].length - 1) {
          newRow = row;
          newCol = col;
        }
        int type = checkType(grid[newRow][newCol], burnableObj);
        switch (type) {
          case FIRE:
            if (((int) (Math.random() * 80) + 1) == 1)
              grid[newRow][newCol] = EMPTY;
            if (newRow - 1 > 0 && ((int) (Math.random() * 85) + 1) == 1)
              if (grid[newRow - 1][newCol] == EMPTY)
                grid[newRow - 1][newCol] = FIRE;
            break;
          case TNT:
          case GAS:
            if (((int) (Math.random() * 3) + 1) == 1)
              grid[newRow][newCol] = FIRE;
            break;
          case OIL:
            if (((int) (Math.random() * 7) + 1) == 1)
              grid[newRow][newCol] = FIRE;
            break;
          case LEAF:
            if (((int) (Math.random() * 30) + 1) == 1)
              grid[newRow][newCol] = FIRE;
            break;
          case WOOD:
            if (((int) (Math.random() * 40) + 1) == 1)
              grid[newRow][newCol] = FIRE;
            break;
          case WATER:
            if (newRow - 1 > 0 && ((int) (Math.random() * 200) + 1) == 1) {
              grid[row][col] = EMPTY;
              grid[newRow][newCol] = FIRE;
              grid[newRow - 1][newCol] = STEAM;
            }
            break;
          case ICE:
            if (newRow - 1 > 0 && ((int) (Math.random() * 250) + 1) == 1) {
              grid[row][col] = EMPTY;
              grid[newRow][newCol] = WATER;
              grid[newRow - 1][newCol] = STEAM;
            }
            break;
          case SAND:
            grid[row][col] = GLASS;
            grid[newRow][newCol] = GLASS;
            break;
        }
        break;
      }
      case LAVA: {
        if (row < grid.length - 1 && ((int) (Math.random() * 10) + 1) == 1) {
          if (grid[row + 1][col] == WATER) {
            if (((int) (Math.random() * 5) + 1) == 1){
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
          int type = checkType(grid[row + 1][col], intObj);
          if (type != -1) {
            grid[row][col] = type;
            grid[row + 1][col] = LAVA;
          } else if (grid[row + 1][col] == LAVA) {
            int leftEmpty = 0;
            int rightEmpty = 0;
            int left = 0, right = 0;
            for (int i = 0; i < col + 1; i++) {
              left = checkType(grid[row + 1][col - i], intObj);
              if (left != -1) {
                leftEmpty = i;
                break;
              } else if (grid[row + 1][col - i] != LAVA) {
                break;
              }
            }
            for (int i = 0; i < grid[0].length - col; i++) {
              right = checkType(grid[row + 1][col + i], intObj);
              if (right != -1) {
                rightEmpty = i;
                break;
              } else if (grid[row + 1][col + i] != LAVA) {
                break;
              }
            }
            if (leftEmpty == rightEmpty && leftEmpty == 0) {
              int rd = (int) (Math.random() * 3) - 1;
              if (col + rd > 1 && col + rd < grid[0].length - 2) {
                int change = checkType(grid[row][col + rd], intObj);
                if (change != -1) {
                  grid[row][col] = change;
                  grid[row][col + rd] = LAVA;
                }
              }
            } else {
              if (leftEmpty > rightEmpty) {
                grid[row][col] = left;
                grid[row + 1][col - leftEmpty] = LAVA;
              } else {
                grid[row][col] = right;
                grid[row + 1][col + rightEmpty] = LAVA;
              }
            }
          }
        }

        int[] burnableObj = {TNT, GAS, OIL, LEAF, WOOD, WATER, ICE, SAND};
        int newRow = row, newCol = col;
        switch ((int) (Math.random() * 9) + 1) {
          case 1: // Left && Top
            newRow = row - 1;
            newCol = col - 1;
            break;
          case 2: // Top
            newRow = row - 1;
            break;
          case 3: // Top && Right
            newRow = row - 1;
            newCol = col + 1;
            break;
          case 4: // Left
            newCol = col - 1;
            break;
          case 5: // Right
            newCol = col + 1;
            break;
          case 6: // Bottom && Left
            newRow = row + 1;
            newCol = col - 1;
            break;
          case 7: // Bottom
            newRow = row + 1;
            break;
          case 8: // Bottom && Right
            newRow = row + 1;
            newCol = col + 1;
            break;
        }
        if (newRow < 0 || newRow > grid.length - 1 || newCol < 0 || newCol > grid[0].length - 1) {
          newRow = row;
          newCol = col;
        }
        int type = checkType(grid[newRow][newCol], burnableObj);
        switch (type) {
          case TNT:
          case GAS:
            if (((int) (Math.random() * 3) + 1) == 1)
              grid[newRow][newCol] = FIRE;
            break;
          case OIL:
            if (((int) (Math.random() * 7) + 1) == 1)
              grid[newRow][newCol] = FIRE;
            break;
          case LEAF:
            if (((int) (Math.random() * 30) + 1) == 1)
              grid[newRow][newCol] = FIRE;
            break;
          case WOOD:
            if (((int) (Math.random() * 40) + 1) == 1)
              grid[newRow][newCol] = FIRE;
            break;
          case ICE:
            if (newRow - 1 > 0 && ((int) (Math.random() * 250) + 1) == 1) {
              grid[row][col] = LAVA;
              grid[newRow][newCol] = WATER;
              grid[newRow - 1][newCol] = STEAM;
            }
            break;
          case SAND:
            grid[row][col] = EMPTY;
            grid[newRow][newCol] = GLASS;
            break;
        }
        break;
      }
      case STONE: {
        if (row < grid.length - 1 && ((int) (Math.random() * 5) + 1) == 1) { // If the sand is not reached to the bottom yet.
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
        if (row > 0 && ((int) (Math.random() * 15) + 1) == 1) {
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
              if (leftEmpty > rightEmpty) {
                grid[row][col] = left;
                grid[row - 1][col - leftEmpty] = STEAM;
              } else {
                grid[row][col] = right;
                grid[row - 1][col + rightEmpty] = STEAM;
              }
            }
          }
        }
        break;
      }
      case GAS: {
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
        if (grid[newRow][newCol] == EMPTY && ((int) (Math.random() * 15) + 1) == 1) {
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

  /** Check if obj exists in arr.
   *  @return Return the block id (int) if the obj exists in arr.
   *          Return -1 if obj does not exist in arr.
   */
  public int checkType(int obj, int[] arr) {
    for (int i : arr)
      if (obj == i)
        return i;
    return -1;
  }

  //do not modify
  public void run() {
    while (true) {
      for (int i = 0; i < display.getSpeed(); i++)
        step();
      updateDisplay();
      display.repaint();
      display.pause(1);  //wait for redrawing and for mouse
      int[] mouseLoc = display.getMouseLocation();
      if (mouseLoc != null)  //test if mouse clicked
        locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
    }
  }
}