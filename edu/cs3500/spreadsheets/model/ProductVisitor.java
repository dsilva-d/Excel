package edu.cs3500.spreadsheets.model;

import java.util.List;

import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.ValueVisitor;

/**
 * A function that takes in an IValue and outputs a numerical representation. Non-double values
 * are evaluated as zero. Intended for ProductFunction. A list of values is multiplied together.
 */
public class ProductVisitor implements ValueVisitor<Double> {
  @Override
  public Double visitDouble(double d) {
    return d;
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
    // multiplies all the values in the list together
    double productSoFar = 1.0;
    for (IValue value : list) {
      productSoFar *= value.accept(new ProductVisitor());
    }
    return productSoFar;
  }
}
