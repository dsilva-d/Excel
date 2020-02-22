package edu.cs3500.spreadsheets.model;

import java.util.List;

import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.ValueVisitor;

/**
 * A function that takes in an IValue and outputs a numerical representation. Non-double values
 * are evaluated as zero. Intended for SumFunction. A list of values is added together.
 */
public class SumVisitor implements ValueVisitor<Double> {

  @Override
  public Double visitDouble(double d) {
    return d;
  }

  @Override
  public Double visitBoolean(boolean b) {
    return 0.0;
  }

  @Override
  public Double visitString(String s) {
    return 0.0;
  }

  @Override
  public Double visitList(List<IValue> list) {
    // sums all the IValues inside the list
    double sum = 0;
    for (IValue value : list) {
      sum += value.accept(new SumVisitor());
    }
    return sum;
  }
}
