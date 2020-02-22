package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.ListVal;

/**
 * An abstracted function object for processing a list of {@link IValue}s. Always returns
 * a result in the form of another {@link IValue}.
 */
public interface IFunction {

  /**
   * Applies the function to a list of IValues and returns a single IValue.
   * @param inputs a list of IValue inputs (which itself is an IValue)
   * @return the result of calling the function on the list of inputs
   * @throws IllegalArgumentException if there is an error in the function
   */
  IValue apply(ListVal inputs) throws IllegalArgumentException;
}
