package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheet;
import edu.cs3500.spreadsheets.view.IView;

/**
 * Represents the ability to connect the worksheet itself with how the worksheet is seen. Relays
 * user actions to changes in the model and the view.
 */
public class Controller implements Features {
  private IView v;
  private IWorksheet<Cell> w;

  /**
   * Constructs a controller with a worksheet model that can be given a visual view.
   * @param w the worksheet model
   */
  public Controller(IWorksheet<Cell> w) {
    this.w = w;
  }

  @Override
  public void setView(IView v) {
    this.v = v;
    this.v.addFeatures(this);
  }

  @Override
  public String selectForm(Coord c) {
    return w.getCellAt(c.col, c.row).getForm();
  }

  @Override
  public void updateCell(Coord c, String input) {
    try {
      w.updateCell(c.col, c.row, input);
      v.render();
    }
    catch (IllegalArgumentException e) {
      v.render();
    }
  }

  @Override
  public void setRowHeight(int r, int l) {
    w.setRowHeight(r,l);
  }

  @Override
  public void setColumnWidth(int c, int l) {
    w.setColumnWidth(c,l);
  }
}