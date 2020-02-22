package edu.cs3500.spreadsheets.view;

import java.io.IOException;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IViewWorksheet;

/**
 * Represents a textual view of a spreadsheet that only stores the raw input and location
 * of each cell.
 */
public class ViewText implements IView {
  IViewWorksheet<Cell> w;
  Appendable a;

  /**
   * Constructs a textual view of a worksheet and writes the text output.
   * @param w the worksheet to be viewed
   * @param a stores the text output
   */
  public ViewText(IViewWorksheet w, Appendable a) {
    this.a = a;
    this.w = w;
  }

  // appends a single cell name with its raw input
  void displayCell(int col, int row) throws IOException {
    String form;
    if (w.getCellAt(col, row).getForm() == null) {
      form = "";
    }
    else {
      form = w.getCellAt(col, row).getForm();
    }
    Coord c = new Coord(col, row);
    a.append(c.toString() + " " + form);
  }

  void showColWidth(int col, int width) throws IOException {
    a.append(Coord.colIndexToName(col) + " " + width);
  }

  void showRowHeight(int row, int height) throws IOException {
    a.append("" + row + " " + height);
  }

  @Override
  public void render() {
    try {
      for (Cell c : w.getAllCells().values()) {
        displayCell(c.getCoord().col, c.getCoord().row);
        a.append("\n");
      }
      for (Integer col : w.columnWidths().keySet()) {
        showColWidth(col, w.columnWidths().get(col));
        a.append("\n");
      }
      for (Integer row : w.rowHeights().keySet()) {
        showRowHeight(row, w.rowHeights().get(row));
        a.append("\n");
      }
    }
    catch (IOException e) {
      // don't throw an exception
    }
  }

  @Override
  public void addFeatures(Features features) {
    return;
  }
}
