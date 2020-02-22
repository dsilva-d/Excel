package edu.cs3500.spreadsheets.model;

import java.util.HashMap;

import edu.cs3500.spreadsheets.value.IValue;

/**
 * A type of worksheet that doesn't have any setter methods. This is part of the adapter pattern
 * for a view communicating with a worksheet model.
 * @param <T> type of cell in this worksheet model
 */
public interface IViewWorksheet<T> {
  /**
   * Gets the coordinate of the cell at the bottom right corner.
   * @return the expression of the cell
   */
  Coord getLargestCoord();

  /**
   * Gets all the non-empty cells in the worksheet.
   * @return a map of non-empty cells by their locations
   */
  HashMap<Coord, Cell> getAllCells();

  /**
   * Evaluates the formula of a cell into an expression.
   * @return the expression of the cell
   * @throws IllegalArgumentException Index is out of bounds.
   */
  IValue evaluateCell(int col, int row) throws IllegalArgumentException;

  /**
   * Finds a cell at a certain coordinate with one-indexing.
   * @return The cell found at this coordinate
   * @throws IllegalArgumentException Index is out of bounds.
   */
  T getCellAt(int col, int row) throws IllegalArgumentException;

  /**
   * Gives the height of a row.
   * @param row index of the row (one-indexing)
   * @return number representing the visual height of the row
   */
  Integer getRowHeight(int row);

  /**
   * Give the width of a column.
   * @param col index of the column (one-indexing)
   * @return number representing the width of the column
   */
  Integer getColumnWidth(int col);

  /**
   * Gets a mapping of row indices to row heights for any rows with a non-default height.
   * @return a copy of the row heights map
   */
  HashMap<Integer, Integer> rowHeights();

  /**
   * Gets a mapping of column indices to column widths for any columns with a non-default width.
   * @return a copy of the column widths map
   */
  HashMap<Integer, Integer> columnWidths();
}
