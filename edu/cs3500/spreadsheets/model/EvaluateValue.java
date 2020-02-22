package edu.cs3500.spreadsheets.model;

import java.util.List;

import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import edu.cs3500.spreadsheets.value.BoolVal;
import edu.cs3500.spreadsheets.value.DoubVal;
import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.StriVal;

/**
 * <p>
 * Evaluates an {@code Sexp} as if it's a known cell value (boolean, String, or number). A list of
 * {@code Sexp} or symbols will be read as a {@code String}. This visitor is meant to be called for
 * raw cell inputs that do not start with an equal sign (=).
 * </p>
 * <p>
 *   For example, a valid cell reference or function will not be evaluated as a cell reference or
 *   function, because it is assumed there was no equal sign that preceded it.
 * </p>
 */
public class EvaluateValue implements SexpVisitor<IValue> {
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
    return new StriVal(new SList(l).toString());
  }

  @Override
  public IValue visitSymbol(String s) {
    return new StriVal(s);
  }

  @Override
  public IValue visitString(String s) {
    return new StriVal(s);
  }
}
