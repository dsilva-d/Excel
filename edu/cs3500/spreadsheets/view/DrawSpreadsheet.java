package edu.cs3500.spreadsheets.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Shape;
import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IViewWorksheet;

/**
 * A panel that draws all the cells in a worksheet, the numbered row labels, and the lettered
 * column labels.
 */
public class DrawSpreadsheet extends JPanel {
  private IViewWorksheet worksheet;
  private Coord gc; // how far to draw the spreadsheet
  private Coord fc; // current highlighted cell
  static final int CELL_WIDTH = 40;
  static final int CELL_HEIGHT = 20;
  static final int DEFAULT_STROKE = 1;
  static final int BOLD_STROKE = 2;
  static final int PADDING = 2;
  //initial cell highlight
  static final Coord INITIAL_CELL = new Coord(1,1);

  /**
   * Constructs the interface of labels and cells for the GUI.
   * @param ivw the worksheet model to be rendered visually
   */
  public DrawSpreadsheet(IViewWorksheet ivw) {
    super();
    this.worksheet = ivw;
    this.gc = ivw.getLargestCoord();
    this.fc = INITIAL_CELL;
  }


  int rowHeight(int row) {
    if (worksheet.getRowHeight(row) == null || worksheet.getRowHeight(row) < CELL_HEIGHT) {
      return CELL_HEIGHT;
    }
    return worksheet.getRowHeight(row);
  }

  int colWidth(int col) {
    if (worksheet.getColumnWidth(col) == null || worksheet.getColumnWidth(col) < CELL_WIDTH) {
      return CELL_WIDTH;
    }
    return worksheet.getColumnWidth(col);
  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    // drawing cells
    int xAccu = 0;
    int yAccu = 0;
    for (int c = 0; c <= gc.col; c++) {
      for (int r = 0; r <= gc.row; r++) {
        // first column (numbered labels)
        if (c == 0) {
          // draw gray label box
          g2d.setPaint(Color.gray);
          g2d.fill(new Rectangle2D.Double(xAccu, yAccu, colWidth(c), rowHeight(r)));
          if (r != 0) {
            int textWidth = g.getFontMetrics().stringWidth(Integer.toString(r));
            int textHeight = g.getFontMetrics().getHeight();
            int textAscent = g.getFontMetrics().getAscent();
            // draw box outline
            g2d.setPaint(Color.black);
            g2d.drawRect(xAccu, yAccu, colWidth(c), rowHeight(r));
            // draw label
            g2d.drawString(String.valueOf(r), colWidth(c) / 2 - textWidth / 2,
                    yAccu + (rowHeight(r) - textHeight) / 2 + textAscent);
          }
        }
        // first row (letter labels)
        else if (r == 0) {
          // draw gray label box
          g2d.setPaint(Color.gray);
          g2d.fill(new Rectangle2D.Double(xAccu, yAccu, colWidth(c), rowHeight(r)));
          // draw box outline
          g2d.setPaint(Color.black);
          g2d.drawRect(xAccu, yAccu, colWidth(c), rowHeight(r));
          // draw label
          int textWidth = g.getFontMetrics().stringWidth(Coord.colIndexToName(c));
          int textHeight = g.getFontMetrics().getHeight();
          int textAscent = g.getFontMetrics().getAscent();
          g2d.drawString(Coord.colIndexToName(c), xAccu + colWidth(c) / 2 - textWidth / 2,
                  (rowHeight(r) - textHeight) / 2 + textAscent);
        }
        // cells with possible contents
        else {
          Shape oldClip = g2d.getClip();
          // if cell is the currently selected cell, draw a red outline
          if (fc.col == c && fc.row == r) {
            g2d.setStroke(new BasicStroke(BOLD_STROKE));
            g2d.setPaint(Color.red);
          }
          else {
            g2d.setPaint(Color.black);
          }
          g2d.drawRect(xAccu, yAccu, colWidth(c), rowHeight(r));
          g2d.setStroke(new BasicStroke(DEFAULT_STROKE));
          g2d.clip(new Rectangle(xAccu - PADDING, yAccu, colWidth(c), rowHeight(r)));
          try {
            g2d.drawString(worksheet.evaluateCell(c, r).toString(),
                    xAccu + PADDING, yAccu + rowHeight(r) - PADDING);
          } catch (IllegalArgumentException e) {
            g2d.drawString("Error!", xAccu + PADDING, yAccu + rowHeight(r) - PADDING);
          }
          g2d.setClip(oldClip);
        }
        yAccu += this.rowHeight(r);
      } // finish a row
      yAccu = 0;
      xAccu += this.colWidth(c);
    } // finish a col
  }

  // increments largest row to be drawn
  void addRow() {
    this.gc = new Coord(gc.col, gc.row + 1);
  }

  // increments largest column to be drawn
  void addCol() {
    this.gc = new Coord(gc.col + 1, gc.row);
  }

  // sets the currently highlighted cell's location
  void setFocusCell(Coord c) {
    this.fc = c;
  }

  // gets the location of the currently highlighted cell
  Coord getFocusCell() {
    return this.fc;
  }
}