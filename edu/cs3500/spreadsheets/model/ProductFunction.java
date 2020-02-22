package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.value.DoubVal;
import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.ListVal;

/**
 * A function that multiplies all the values of all of its arguments. Its arguments could be
 * references of arbitrary size. Any arguments whose contents are blank or not numeric should be
 * ignored. If there are no numeric arguments at all, the default value is zero.
 */
public class ProductFunction implements IFunction {

  @Override
  public IValue apply(ListVal inputs) throws IllegalArgumentException {
    if (inputs.accept(new HasDoubleVisitor())) {
      double productSoFar = inputs.accept(new ProductVisitor());
      return new DoubVal(productSoFar);
    }
    else {
      return new DoubVal(0.0);
    }
  }
}
