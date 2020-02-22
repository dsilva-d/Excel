package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.value.DoubVal;
import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.ListVal;

/**
 * Finds the total sum of all given inputs. Its arguments could be references of arbitrary size,
 * and the sum should cover all the values in the region being referenced. Any arguments whose
 * contents are blank or not numeric should be ignored. If there are no numeric arguments at all,
 * the default value is zero.
 */
public class SumFunction implements IFunction {

  @Override
  public IValue apply(ListVal inputs) throws IllegalArgumentException {
    double sumSoFar = inputs.accept(new SumVisitor());
    return new DoubVal(sumSoFar);
  }
}
