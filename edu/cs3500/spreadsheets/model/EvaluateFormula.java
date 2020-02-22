package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import edu.cs3500.spreadsheets.value.BoolVal;
import edu.cs3500.spreadsheets.value.DoubVal;
import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.ListVal;
import edu.cs3500.spreadsheets.value.StriVal;

/**
 * <p>
 *  Evaluates an {@code Sexp} as if it is a valid formula (value, cell reference, or function).
 *  This visitor is meant to be called for raw cell inputs that start with an equal sign (=), or
 *  for {@code Sexp}s that are inputs of functions.
 *  </p>
 */
public class EvaluateFormula implements SexpVisitor<IValue> {
  // the set of cells that reference the current cell being evaluated
  private HashSet<Coord> parentCells;
  private Worksheet worksheet;

  public EvaluateFormula(HashSet<Coord> parentCells, Worksheet worksheet) {
    this.parentCells = parentCells;
    this.worksheet = worksheet;
  }

  @Override
  public IValue visitBoolean(boolean b) {
    return new BoolVal(b);
  }

  @Override
  public IValue visitNumber(double d) {
    return new DoubVal(d);
  }


  @Override
  public IValue visitSList(List<Sexp> l) {
    // list of inputs (without function symbol)
    List<Sexp> lNew = l.subList(1, l.size());
    List<IValue> evaluatedInputs = new ArrayList<>();
    for (Sexp exp : lNew) {
      // every Sexp is valid, no need to check for "="
      // all Sexp treated like formulas (functions and cell references are evaluated)

      evaluatedInputs.add(exp.accept(new EvaluateFormula(new HashSet<>(parentCells), worksheet)));
    }
    ListVal inputsAsValue = new ListVal(evaluatedInputs);

    switch (l.get(0).toString()) {
      // applying functions may result in IllegalArgumentException - caught in Worksheet
      case "SUM":
        // returns DoubVal
        return new SumFunction().apply(inputsAsValue);
      case "PRODUCT":
        // returns DoubVal
        return new ProductFunction().apply(inputsAsValue);
      case "<":
        // returns BoolVal
        return new LessThanFunction().apply(inputsAsValue);
      case "CONCAT":
        // returns StriVal
        return new ConcatFunction().apply(inputsAsValue);
      default:
        throw new IllegalArgumentException("Function not found.");
    }

  }

  @Override
  public IValue visitSymbol(String s) {
    // find all cells in the block ref
    if (Coord.isBlockCellRef(s)) {
      Coord firstCoord = Coord.symbolToCoord(s.substring(0, s.indexOf(":")));
      Coord secondCoord = Coord.symbolToCoord(s.substring(s.indexOf(":") + 1));
      List<IValue> outputs = new ArrayList<>();
      for (int row = firstCoord.row; row <= secondCoord.row; row++) {
        for (int col = firstCoord.col; col <= secondCoord.col; col++) {
          // evaluate the raw inputs of each cell in the block of cell references
          outputs.add(worksheet.evaluateCell(col, row, parentCells));
        }
      }
      return new ListVal(outputs);
    }
    // find all cells in the column(s)
    else if (Coord.isColRef(s)) {
      int split = s.indexOf(":");
      String firstCol = s.substring(0,split);
      String lastCol = s.substring(split + 1);
      // set of all column indices to look for
      HashSet<Integer> colIndices = new HashSet<>();
      for (int i = Coord.colNameToIndex(firstCol); i <= Coord.colNameToIndex(lastCol); i++) {
        colIndices.add(i);
      }
      List<IValue> outputs = new ArrayList<>();
      // get all cells that are in target column(s)
      for (Coord c : worksheet.getAllCells().keySet()) {
        if (colIndices.contains(c.col)) {
          outputs.add(worksheet.evaluateCell(c.col, c.row, parentCells));
        }
      }
      return new ListVal(outputs);
    }
    else if (Coord.isCellRef(s)) {
      // evaluate the raw input of the cell being referenced
      return worksheet.evaluateCell(Coord.symbolToCoord(s).col,
              Coord.symbolToCoord(s).row, parentCells);
    }
    else {
      throw new IllegalArgumentException("Invalid symbol");
    }
  }


  @Override
  public IValue visitString(String s) {
    return new StriVal(s);
  }


}
