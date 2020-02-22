package edu.cs3500.spreadsheets.model;

import java.util.HashMap;
import java.util.HashSet;


/**
 * Presents a worksheet of cells.
 */
public class SimpleWorksheetBuilder implements WorksheetReader.WorksheetBuilder<Worksheet> {
  private Worksheet worksheet;

  // brand new worksheet
  public SimpleWorksheetBuilder() {
    this.worksheet = new Worksheet(new HashMap<>(), new HashMap<>(),
            new HashMap<Coord, HashSet<Coord>>());
  }

  // for adding on to a previously made worksheet
  public SimpleWorksheetBuilder(Worksheet worksheet) {
    this.worksheet = worksheet;
  }


  /**
   * Makes a new cell in the worksheet.
   */
  @Override
  public WorksheetReader.WorksheetBuilder<Worksheet> createCell(int col, int row,
                                                                String contents) {
    this.worksheet.updateCell(col, row, contents); // add cell checks coordinates
    return new SimpleWorksheetBuilder(this.worksheet);
  }

  @Override
  public Worksheet createWorksheet() {
    return this.worksheet;
  }

  @Override
  public WorksheetReader.WorksheetBuilder<Worksheet> addRowHeight(int row, int height) {
    this.worksheet.setRowHeight(row, height);
    return new SimpleWorksheetBuilder(this.worksheet);
  }

  @Override
  public WorksheetReader.WorksheetBuilder<Worksheet> addColumnWidth(int col, int width) {
    this.worksheet.setColumnWidth(col, width);
    return new SimpleWorksheetBuilder(this.worksheet);
  }

}
