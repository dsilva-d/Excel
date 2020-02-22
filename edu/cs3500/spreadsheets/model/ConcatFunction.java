package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.ListVal;

/**
 * Concatenates all inputs into a single String. Its arguments could be references of arbitrary
 * size. Any arguments whose contents are blank should be ignored. Boolean values and numbers
 * will be read with their toString formatting.
 */
public class ConcatFunction implements IFunction {

  @Override
  public IValue apply(ListVal inputs) throws IllegalArgumentException {
    return inputs.accept(new ConcatVisitor());
  }
}
