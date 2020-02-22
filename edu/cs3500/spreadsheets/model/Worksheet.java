package edu.cs3500.spreadsheets.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.StriVal;

/**
 * Represents a simple type of worksheet.
 */
public class Worksheet implements IWorksheet<Cell>, IViewWorksheet<Cell> {
  // map of cells created
  private final HashMap<Coord, Cell> sheet;
  // map of cells evaluations
  private final HashMap<Coord, IValue> evaluations;
  // map of the parent cells for each individual cell
  private final HashMap<Coord, HashSet<Coord>> parentCellsMap;
  // largest Column
  private int largestCol;
  // largest Row
  private int largestRow;
  // row heights (row index to row height)
  private HashMap<Integer, Integer> rowHeights;
  // column widths
  private HashMap<Integer, Integer> columnWidths;

  /**
   * Constructs a model of a worksheet with a set of cells.
   * @param sheet the map of non-empty cells by their locations
   * @param evaluations stores the evaluated values of cells
   * @param parentCellsMap maps each cell to the set of parent cells (they reference the cell)
   */
  public Worksheet(HashMap<Coord, Cell> sheet, HashMap<Coord, IValue> evaluations,
                   HashMap<Coord, HashSet<Coord>> parentCellsMap) {
    this.sheet = sheet;
    this.evaluations = evaluations;
    this.parentCellsMap = parentCellsMap;
    //default size of spreadsheet
    this.largestCol = 20;
    this.largestRow = 20;
    this.rowHeights = new HashMap<>();
    this.columnWidths = new HashMap<>();
  }

  @Override
  public Coord getLargestCoord() {
    return new Coord(largestCol, largestRow);
  }

  @Override
  public HashMap<Coord, Cell> getAllCells() {
    return new HashMap<Coord, Cell>(sheet);
  }

  @Override
  public Cell getCellAt(int col, int row) throws IllegalArgumentException {
    badCoords(col, row);
    // cells are equal if they have the same coord
    return this.sheet.getOrDefault(new Coord(col, row),
            new Cell("", new Coord(col, row)));
  }

  @Override
  public Integer getRowHeight(int row) {
    return rowHeights.get(row);
  }

  @Override
  public Integer getColumnWidth(int col) {
    return columnWidths.get(col);
  }

  @Override
  public HashMap<Integer, Integer> rowHeights() {
    return new HashMap<>(this.rowHeights);
  }

  @Override
  public HashMap<Integer, Integer> columnWidths() {
    return new HashMap<>(this.columnWidths);
  }

  @Override
  public void updateCell(int col, int row, String contents) throws IllegalArgumentException {
    badCoords(col, row);
    if (col > largestCol) {
      this.largestCol = col;
    }
    if (row > largestRow) {
      this.largestRow = row;
    }

    if (contents == null || contents.equals("")) {
      sheet.remove(new Coord(col, row));
    }
    else {
      sheet.put(new Coord(col, row), new Cell(contents, new Coord(col, row)));
    }
    // evaluate cell?
    HashSet<Coord> newParents = parentCellsMap.getOrDefault(new Coord(col,row),
            new HashSet<Coord>());
    for (Coord c : newParents) {
      evaluateCell(c.col, c.row);
    }
    // update any cells that reference the coordinate that was just added
  }

  @Override
  public void setRowHeight(int row, int height) throws IllegalArgumentException {
    rowHeights.put(row, height);
  }

  @Override
  public void setColumnWidth(int column, int width) {
    columnWidths.put(column, width);
  }


  @Override
  // used to start a cell evaluation with raw input or looks up the value in the hash map
  public IValue evaluateCell(int col, int row) {
    badCoords(col, row);
    return evaluations.getOrDefault(new Coord(col, row),
            evaluateCell(col, row, new HashSet<Coord>()));
  }

  // used as a helper for stopping self-referential calls
  IValue evaluateCell(int col, int row, HashSet<Coord> parentCells)
          throws IllegalArgumentException {
    badCoords(col, row);
    // add the parent cells found from this evaluation to the hash map
    HashSet<Coord> parentsSoFar = parentCellsMap.getOrDefault(new Coord(col, row),
            new HashSet<>());
    parentsSoFar.addAll(parentCells);
    parentCellsMap.put(new Coord(col, row), parentsSoFar);
    boolean addedToSet = parentCells.add(new Coord(col, row));
    // the cell that is being evaluated is referring to itself
    if (!addedToSet) {
      throw new IllegalArgumentException("Self referential");
    }
    // cell is not self-referential - try evaluating its input
    else {
      IValue evaluation;
      // check if cell is empty
      if (getCellAt(col, row).isEmpty()) {
        evaluation = new StriVal("");
      }
      else {
        // this is a new cell to evaluate (raw input could start with an equal sign)
        // could throw an error
        evaluation = parseEvaluateCell(col, row, parentCells);
      }
      // update cell's IValue in the map
      evaluations.put(new Coord(col, row), evaluation);
      return evaluation;
    }
  }

  // will distinguish between a formula vs. a value
  // turns the contents of a cell into an Sexp (using the parser)
  // then, based on if the raw input began with "=", returns the output of the cell
  // the set of parent cells are for keeping track of self-referential calls (cells seen already)
  private IValue parseEvaluateCell(int col, int row, HashSet<Coord> parentCells)
          throws IllegalArgumentException {
    String input = getCellAt(col, row).getForm();

    // this cell potentially has a formula (value, cell reference, or function)
    if (input.startsWith("=")) {
      // removes equal sign from input for parser
      input = input.substring(1);
      // check that the cell doesn't have a block reference (error)
      if (Coord.isBlockCellRef(input)) {
        throw new IllegalArgumentException("A cell cannot only contain a block cell reference.");
      }
      // check that the cell doesn't have a column reference (error)
      if (Coord.isColRef(input)) {
        throw new IllegalArgumentException("A cell cannot only contain a column reference.");
      }
      // evaluate input as a formula
      return Parser.parse(input).accept(new EvaluateFormula(parentCells, this));
    }
    // any SLists or Cell References returned from the parser are seen as Strings
    // this cell potentially has a value (boolean, String, or number)
    else {
      return Parser.parse(input).accept(new EvaluateValue());

    }
  }

  @Override
  public boolean equals(Object object) {
    return sheet.equals(((Worksheet) object).sheet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sheet);
  }

  // throws an error if cell coordinates are not both positive
  private void badCoords(int col, int row) {
    if (col < 1 || row < 1) {
      throw new IllegalArgumentException("Index out of bounds: " + col + ", " + row);
    }
  }
}
