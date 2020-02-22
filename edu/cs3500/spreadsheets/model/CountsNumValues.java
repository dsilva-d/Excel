package edu.cs3500.spreadsheets.model;

import java.util.List;

import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.ValueVisitor;

/**
 * Counts the number of values in the IValue.
 */
public class CountsNumValues implements ValueVisitor<Double> {

  @Override
  public Double visitDouble(double d) {
    return 1.0;
  }

  @Override
  public Double visitBoolean(boolean b) {
    return 1.0;
  }

  @Override
  public Double visitString(String s) {
    return 1.0;
  }

  @Override
  public Double visitList(List<IValue> list) {
    return Double.valueOf(list.size());
  }
}
