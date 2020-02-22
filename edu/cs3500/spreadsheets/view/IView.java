package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.Features;

/**
 * A visual representation of a spreadsheet.
 */
public interface IView {
  /**
   * Displays a spreadsheet.
   **/
  void render();

  /**
   * Allows the view to communicate with the controller.
   * @param features the controller
   */
  void addFeatures(Features features);
}
