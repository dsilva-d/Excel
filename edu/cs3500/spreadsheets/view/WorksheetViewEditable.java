package edu.cs3500.spreadsheets.view;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IViewWorksheet;

/**
 * Represents a frame window for an editable worksheet GUI built with Swing.
 */
public class WorksheetViewEditable extends JFrame implements IView {
  private DrawSpreadsheet spread;
  private JButton conf;
  private JButton canc;
  private JButton delete;
  private JTextField inp;
  private Point cCoord;

  /**
   * Constructs an editable worksheet GUI with all the Swing components inside the frame.
   *
   * @param ivw the worksheet to be viewed and edited
   */
  public WorksheetViewEditable(IViewWorksheet ivw) {
    super();
    IViewWorksheet worksheet = ivw;
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(new Dimension(850, 450));
    this.spread = new DrawSpreadsheet(ivw);
    ScrollView sv = new ScrollView(ivw, this.spread);

    conf = new JButton("✓"); //check
    conf.setActionCommand("Cell Conf");
    canc = new JButton("Cancel");
    canc.setActionCommand("Cell Canc");
    delete = new JButton("Delete");
    delete.setActionCommand("Cell Del");
    inp = new JTextField(10);
    JLabel helpMenu = new JLabel("Formulas: numbers, strings, booleans, " +
            "references to other cells, column references, SUM, PRODUCT, <, CONCAT");

    JPanel toolbar = new JPanel();
    toolbar.add(conf, BorderLayout.WEST);
    toolbar.add(canc, BorderLayout.CENTER);
    toolbar.add(delete, BorderLayout.CENTER);
    toolbar.add(inp, BorderLayout.EAST);

    this.add(toolbar, BorderLayout.NORTH);
    this.add(sv, BorderLayout.CENTER);
    this.add(helpMenu, BorderLayout.SOUTH);
  }

  // returns the currently selected cell
  private Coord getFocusCell() {
    return this.spread.getFocusCell();
  }

  @Override
  public void render() {
    this.setVisible(true);
    this.repaint();
  }

  // helper for clearing the selected cell's formula
  // specifically, this method displays a blank String in the text field
  private String deleteCellForm() {
    inp.setText("");
    return "";
  }

  // displays the current formula of the selected cell in the text field
  private void echoCurrentForm(Features f) {
    inp.setText(f.selectForm(WorksheetViewEditable.this.getFocusCell()));
  }

  @Override
  public void addFeatures(Features f) {
    // shows formula in cell A1 when user first opens view
    echoCurrentForm(f);
    conf.addActionListener(evt -> f.updateCell(this.getFocusCell(), inp.getText()));
    canc.addActionListener(evt -> echoCurrentForm(f));
    delete.addActionListener(evt -> f.updateCell(this.getFocusCell(), deleteCellForm()));
    this.spread.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        return;
      }

      @Override
      public void mousePressed(MouseEvent e) {
        cCoord = e.getLocationOnScreen();

        spread.setFocusCell(locToCoord(e.getX(), e.getY()));
        echoCurrentForm(f);
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        int rowIndex = getFocusCell().row;
        int colIndex = getFocusCell().col;
        // click and drag motion creates a box
        int deltaY = e.getLocationOnScreen().y - cCoord.y;
        int deltaX = e.getLocationOnScreen().x - cCoord.x;
        if (spread.rowHeight(rowIndex) + deltaY < DrawSpreadsheet.CELL_HEIGHT) {
          deltaY = 0;
          f.setRowHeight(rowIndex, DrawSpreadsheet.CELL_HEIGHT);
        }
        if (spread.colWidth(colIndex) + deltaX < DrawSpreadsheet.CELL_WIDTH) {
          deltaX = 0;
          f.setColumnWidth(colIndex, DrawSpreadsheet.CELL_WIDTH);
        }
        f.setRowHeight(rowIndex, spread.rowHeight(rowIndex) + deltaY);
        f.setColumnWidth(colIndex, spread.colWidth(colIndex) + deltaX);
        WorksheetViewEditable.this.repaint();
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        return;
      }

      @Override
      public void mouseExited(MouseEvent e) {
        return;
      }

      private Coord locToCoord(int x, int y) {
        try {
          int col = 0;
          int row = 0;
          int xMarker = 0;
          int yMarker = 0;
          // keep calculating sum of column widths until it passes the x-coord of the mouse click
          while (x > xMarker) {
            col++;
            xMarker += spread.colWidth(col - 1);
          }
          // same as above, calculate sum of row heights until it passes y-coord
          while (y > yMarker) {
            row++;
            yMarker += spread.rowHeight(row - 1);
          }
          return new Coord(col - 1, row - 1);
        }
        // any out of bounds coordinates
        catch (IllegalArgumentException e) {
          return spread.getFocusCell();
        }
      }
    });
  }


}
