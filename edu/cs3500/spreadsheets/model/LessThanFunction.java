package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.value.BoolVal;
import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.ListVal;

/**
 * A < function, that takes exactly two values and returns whether the first is less than the
 * second. If either value is missing or is not a number, the function should error. This function
 * only works with single cell references, and not block cell references.
 */
public class LessThanFunction implements IFunction {

  @Override
  public IValue apply(ListVal inputs) throws IllegalArgumentException {
    // input size is 2 and all inputs are numbers
    if (inputs.accept(new CountsNumValues()) == 2
        && inputs.accept(new AllDoubleVisitor())) {
      return new BoolVal(inputs.accept(new LessThanVisitor()));
    }
    else {
      throw new IllegalArgumentException("Needs exactly two inputs.");
    }
  }
}
