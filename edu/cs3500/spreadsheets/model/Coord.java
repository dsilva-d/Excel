package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * A value type representing coordinates in a {@link IWorksheet}.
 */
public class Coord {
  public final int row;
  public final int col;

  /**
   * A constructor for a Coord.
   * @param col the col of the coordinate
   * @param row the row of the coordinate
   */
  public Coord(int col, int row) {
    if (row < 1 || col < 1) {
      throw new IllegalArgumentException("Coordinates should be strictly positive");
    }
    this.row = row;
    this.col = col;
  }

  /**
   * Converts from the A-Z column naming system to a 1-indexed numeric value.
   * @param name the column name
   * @return the corresponding column index
   */
  public static int colNameToIndex(String name) {
    name = name.toUpperCase();
    int ans = 0;
    for (int i = 0; i < name.length(); i++) {
      ans *= 26;
      ans += (name.charAt(i) - 'A' + 1);
    }
    return ans;
  }

  /**
   * Converts a 1-based column index into the A-Z column naming system.
   * @param index the column index
   * @return the corresponding column name
   */
  public static String colIndexToName(int index) {
    StringBuilder ans = new StringBuilder();
    while (index > 0) {
      int colNum = (index - 1) % 26;
      ans.insert(0, Character.toChars('A' + colNum));
      index = (index - colNum) / 26;
    }
    return ans.toString();
  }

  /**
   * Determines if the given string is a single cell reference.
   * @param s the string to test
   * @return true if the string is a single cell reference
   */
  public static boolean isCellRef(String s) {
    // looks for letters at front of string, then checks if the rest are numbers
    // lowercase column names are valid
    boolean lookingForLetters = true;

    for (int i = 0; i < s.length(); i++) {
      if (lookingForLetters) {
        if (!Character.isLetter(s.charAt(i))) {
          lookingForLetters = false;
        }
      }
      // checking that rest of string is made of digits
      else {
        if (!Character.isDigit(s.charAt(i))) {
          return false;
        }
      }
    }
    return Character.isDigit(s.charAt(s.length() - 1));
  }

  /**
   * Converts a string that represents a cell reference into a Coord.
   * @param s string to convert
   * @return the corresponding Coord
   * @throws IllegalArgumentException if the string is not formatted as a single cell reference
   */
  public static Coord symbolToCoord(String s) throws IllegalArgumentException {
    if (isCellRef(s)) {
      int i;
      for (i = 0; i < s.length(); i++) {
        if (Character.isDigit(s.charAt(i))) {
          break;
        }
      }
      // one-indexed
      return new Coord(Coord.colNameToIndex(s.substring(0, i)),
              Integer.parseInt(s.substring(i)));
    }
    else {
      throw new IllegalArgumentException("Input must be a correctly formatted single cell" +
              " reference.");
    }
  }

  /**
   * Determines if a string is a block cell reference, i.e. A1:B5.
   * @param s the string to test
   * @return true if the string is a valid block cell reference
   */
  public static boolean isBlockCellRef(String s) {
    if (s.contains(":")) {
      String firstCell = s.substring(0, s.indexOf(":"));
      String secondCell = s.substring(s.indexOf(":") + 1);
      if (isCellRef(firstCell) && isCellRef(secondCell)) {
        Coord firstCoord = symbolToCoord(firstCell);
        Coord secondCoord = symbolToCoord(secondCell);
        if (firstCoord.row <= secondCoord.row && firstCoord.col <= secondCoord.col) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Determines if a string is a column reference, i.e. B:B or A:ZZ.
   * @param s the string to test
   * @return true if the string is a valid reference to a column/columns
   */
  public static boolean isColRef(String s) {
    if (s.contains(":")) {
      int split = s.indexOf(":");
      for (int i = 0; i < s.length(); i++) {
        if (i != split && !Character.isLetter(s.charAt(i))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return colIndexToName(this.col) + this.row;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coord coord = (Coord) o;
    return row == coord.row
        && col == coord.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }
}

