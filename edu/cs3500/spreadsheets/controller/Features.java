package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.view.IView;

/**
 * An interface for a worksheet controller. The purpose of the Features interface is to keep
 * controller implementations from being required to use Java Swing components.
 */
public interface Features {
  /**
   * Gets formula of selected cell.
   * @param c location of selected cell
   * @return the raw input formula of the cell
   */
  String selectForm(Coord c);

  /**
   * Updates the raw contents of a cell's formula.
   * @param input the formula being added
   */
  void updateCell(Coord c, String input);

  /**
   * Gives the controller a view to communicate with. Also gives the view access to
   * this controller.
   * @param view the visual rendering the controller works with
   */
  void setView(IView view);

  /**
   * Changes a row's height.
   * @param r row being changed
   * @param l how much is being changed
   */
  void setRowHeight(int r, int l);

  /**
   * Changes a column's width.
   * @param c row being changed
   * @param l how much is being changed
   */
  void setColumnWidth(int c, int l);
}