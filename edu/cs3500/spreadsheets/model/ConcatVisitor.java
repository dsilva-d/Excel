package edu.cs3500.spreadsheets.model;

import java.util.List;

import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.StriVal;
import edu.cs3500.spreadsheets.value.ValueVisitor;

/**
 * A function that takes in IValues and returns a concatenated String using the String format
 * of all the values.
 */
public class ConcatVisitor implements ValueVisitor<StriVal> {

  @Override
  public StriVal visitDouble(double d) {
    return new StriVal(String.valueOf(d));
  }

  @Override
  public StriVal visitBoolean(boolean b) {
    return new StriVal(String.valueOf(b));
  }

  @Override
  public StriVal visitString(String s) {
    return new StriVal(s);
  }

  @Override
  public StriVal visitList(List<IValue> list) {
    StringBuilder result = new StringBuilder();
    for (IValue value : list) {
      if (value == null) {
        continue;
      }
      result = result.append(value.accept(new ConcatVisitor()).toString());
    }
    return new StriVal(result.toString());
  }
}
