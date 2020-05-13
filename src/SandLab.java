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
    SandLab lab = new SandLab(120, 80); // the window dimensions. change if you want a larger/smaller area
    lab.run();
  }

  //add constants for particle types here
  public static final int EMPTY = 0;
  public static final int METAL = 1;
  public static final int SAND = 2;
  public static final int WATER = 3;
  public static final int OIL = 4;
  public static final int WOOD = 5;
  public static final int LEAF = 6;
  public static final int ICE = 7;
  public static final int FIRE = 8;
  public static final int LAVA = 9;
  public static final int STONE = 10;
  public static final int OBSIDIAN = 11;
  public static final int TNT = 12;
  public static final int SOIL = 13;
  public static final int STEAM = 14;
  public static final int GRASS = 15;

  //do not add any more fields
  private int[][] grid;
  private SandDisplay display;

  public SandLab(int numRows, int numCols) {
    grid = new int[numRows][numCols];
    String[] names;
    names = new String[16];
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
    names[SOIL] = "Soil";
    names[STEAM] = "Steam";
    names[GRASS] = "Grass";
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
        switch(grid[row][col]) {
          case EMPTY:
            display.setColor(row, col, new Color(0, 0, 0));
            break;
          case METAL:
            display.setColor(row, col, new Color(30, 30, 30));
            break;
          case SAND:
            display.setColor(row, col, new Color(255, 200, 0));
            break;
          case WATER:
            display.setColor(row, col, new Color(0, 128, 255));
            break;
          case OIL:
            display.setColor(row, col, new Color(40, 20, 5));
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
            int offset = (int)(Math.random()*40-20);
            display.setColor(row, col, new Color(255 + offset, 100 + offset, 0));
            break;
          case LAVA:
            display.setColor(row, col, new Color(255, 50, 0));
            break;
          case STONE:
            display.setColor(row, col, new Color(150, 150, 150));
            break;
          case OBSIDIAN:
            display.setColor(row, col, new Color(50, 30, 160));
            break;
          case TNT:
            display.setColor(row, col, new Color(170, 0, 0));
            break;
          case SOIL:
            display.setColor(row, col, new Color(160, 65, 0));
            break;
          case STEAM:
            display.setColor(row, col, new Color(120, 210, 255));
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
      case SAND:
        // Interactable Objects
        int[] interactableObj = {EMPTY, WATER};

        // If the sand is not reached to the bottom yet.
        if (row < grid.length -1) {
          // can be -1, EMPTY, or WATER.
          int type = checkType(grid[row + 1][col], interactableObj);

          // If nothing or water exist under the sand, fall down one row.
          if (type != -1) {
            grid[row][col] = type;
            grid[row + 1][col] = SAND;
          } else if (grid[row + 1][col] == SAND) { // If there is a another sand exist under the sand.
            if (col == grid[0].length - 1) { // If the location is rightmost
              int left = checkType(grid[row + 1][col - 1], interactableObj);
              if (left != -1) { // Check left side
                grid[row][col] = left;
                grid[row + 1][col - 1] = SAND;
              }
              return;
            } else if (col == 0) { // If the location is leftmost
              int right = checkType(grid[row + 1][col + 1], interactableObj);
              if (right != -1) { // Check right side
                grid[row][col] = right;
                grid[row + 1][col + 1] = SAND;
              }
              return;
            }
            int left = checkType(grid[row + 1][col - 1], interactableObj);
            int right = checkType(grid[row + 1][col + 1], interactableObj);
            // If either left or right side of the bottom sand is empty,
            if (left != -1 && right != -1) {
              // randomly assigned the sand either left or right side.
              int LR = (int) (Math.random() * 2);
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
      case WATER:
        if (row < grid.length -1) {
          if (grid[row + 1][col] == EMPTY) {
            grid[row][col] = EMPTY;
            grid[row + 1][col] = WATER;
          } else if (grid[row + 1][col] == WATER) {
            int leftEmpty = 0;
            int rightEmpty = 0;
            for (int i = 0; i < col + 1; i++) {
              if (grid[row + 1][col - i] == EMPTY) {
                leftEmpty = i;
                break;
              } else if (grid[row + 1][col - i] != WATER) {
                break;
              }
            }
            for (int i = 0; i < grid[0].length - col; i++) {
              if (grid[row + 1][col + i] == EMPTY) {
                rightEmpty = i;
                break;
              } else if (grid[row + 1][col + i] != WATER) {
                break;
              }
            }
            if (leftEmpty == rightEmpty && leftEmpty == 0) {
              return;
            } else {
              grid[row][col] = EMPTY;
              if (leftEmpty > rightEmpty)
                grid[row + 1][col - leftEmpty] = WATER;
              else
                grid[row + 1][col + rightEmpty] = WATER;
            }
          }
        }
        break;
      case OIL:
        display.setColor(row, col, new Color(40, 20, 5));
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
        int offset = (int)(Math.random()*20-10);
        display.setColor(row, col, new Color(255 + offset, 100 + offset, 0));
        break;
      case LAVA:
        display.setColor(row, col, new Color(255, 50, 0));
        break;
      case STONE:
        display.setColor(row, col, new Color(150, 150, 150));
        break;
      case OBSIDIAN:
        display.setColor(row, col, new Color(50, 30, 160));
        break;
      case TNT:
        display.setColor(row, col, new Color(170, 0, 0));
        break;
      case SOIL:
        display.setColor(row, col, new Color(160, 65, 0));
        break;
      case STEAM:
        display.setColor(row, col, new Color(120, 210, 255));
        break;
      default:
        break;
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