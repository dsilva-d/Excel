package edu.cs3500.spreadsheets.model;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A factory for reading inputs and producing Worksheets: given a
 * {@link WorksheetBuilder} to produce the new Worksheet itself,
 * this class can parse the given input and populate the worksheet.
 */
public final class WorksheetReader {
  /**
   * A builder pattern for producing Worksheets.
   * @param <T> the type of Worksheet to produce
   */
  public interface WorksheetBuilder<T> {
    /**
     * Creates a new cell at the given coordinates and fills in its raw contents.
     * @param col the column of the new cell (1-indexed)
     * @param row the row of the new cell (1-indexed)
     * @param contents the raw contents of the new cell: may be {@code null}, or any string.
     *                 Strings beginning with an {@code =} character should be
     *                 treated as formulas; all other strings should be treated as number or
     *                 boolean values if possible, and string values otherwise.
     * @return this {@link WorksheetBuilder}
     */
    WorksheetBuilder<T> createCell(int col, int row, String contents);

    /**
     * Finalizes the construction of the worksheet and returns it.
     * @return the fully-filled-in worksheet
     */
    T createWorksheet();

    /**
     * Adds a specified row height to the model.
     * @param row index of row
     * @param height number representation of the row height
     * @return this {@link WorksheetBuilder}
     */
    WorksheetBuilder<T> addRowHeight(int row, int height);

    /**
     * Adds a specified column width to the model.
     * @param col index of col
     * @param width number representation of the col width
     * @return this {@link WorksheetBuilder}
     */
    WorksheetBuilder<T> addColumnWidth(int col, int width);
  }

  /**
   * <p>A factory for producing Worksheets.  The file format is</p>
   * <pre>
   *   &lt;cell coordinates, in A# format&gt; &lt;the raw contents of the cell&gt;
   *   ...
   * </pre>
   * <p>e.g.</p>
   * <pre>
   *   A1 5
   *   A2 6
   *   A3 =(SUM A1 A2:A2)
   * </pre>
   * <p>Line-comments are indicated by <code>#</code>, and are allowed either at the start
   * of a line or between the cell coordinate and its contents.</p>
   * <p>There is no requirement that cells are filled in in order of their dependencies,
   * since no cell evaluation occurs during this creation process.</p>
   * @param builder The source of the new Worksheet object
   * @param readable the input source for the contents of this Worksheet
   * @param <T> the type of Worksheet to produce
   * @return the fully-filled-in Worksheet
   */
  public static <T> T read(WorksheetBuilder<T> builder, Readable readable) {
    Scanner scan = new Scanner(readable);
    final Pattern cellRef = Pattern.compile("([A-Za-z]+)([1-9][0-9]*)");
    final Pattern colName = Pattern.compile("[A-Za-z]+");
    final Pattern rowName = Pattern.compile("[1-9][0-9]*");
    scan.useDelimiter("\\s+");
    while (scan.hasNext()) {
      int col = -1;
      int row = -1;
      while (scan.hasNext("#.*")) {
        scan.nextLine();
        scan.skip("\\s*");
      }
      String name = scan.next();
      Matcher m = cellRef.matcher(name);
      Matcher a = colName.matcher(name);
      Matcher b = rowName.matcher(name);
      if (m.matches()) {
        col = Coord.colNameToIndex(m.group(1));
        row = Integer.parseInt(m.group(2));
      } else if (a.matches()) {
        col = Coord.colNameToIndex(a.group(0));
      } else if (b.matches()) {
        row = Integer.parseInt(b.group(0));
      }
      else {
        throw new IllegalStateException("Expected cell ref, column name, or row name");
      }
      scan.skip("\\s*");
      while (scan.hasNext("#.*")) {
        scan.nextLine();
        scan.skip("\\s*");
      }
      if ((a.matches() || b.matches()) && scan.hasNextInt()) {
        int size = scan.nextInt();
        if (a.matches()) {
          builder = builder.addColumnWidth(col, size);
        }
        else {
          builder = builder.addRowHeight(row, size);
        }
      }
      else {
        String contents = scan.nextLine();
        if (m.matches()) {
          builder = builder.createCell(col, row, contents);
        }
      }
    }

    return builder.createWorksheet();
  }
}
