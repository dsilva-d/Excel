package edu.cs3500.spreadsheets.model;

/**
 * Represents any type of worksheet using a map of coordinates to cells that can be edited.
 * @param <T> the type of cells in the Worksheet
 */
public interface IWorksheet<T> extends IViewWorksheet<T> {

  /**
   * Adds a cell to the worksheet at the given coordinates with contents.
   * @param col col of new cell
   * @param row row of new cell
   * @param contents input of new cell
   * @throws IllegalArgumentException cell indices are out of bounds
   */
  void updateCell(int col, int row, String contents) throws IllegalArgumentException;

  /**
   * Sets the height of a specified row.
   * @param row index of the row
   * @param height number representation of the height of the row
   */
  void setRowHeight(int row, int height);

  /**
   * Sets the width of a specified column.
   * @param column index of the column
   * @param width number representation of the width of the column
   */
  void setColumnWidth(int column, int width);
}