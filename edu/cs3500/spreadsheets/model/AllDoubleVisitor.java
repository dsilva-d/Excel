package edu.cs3500.spreadsheets.model;

import java.util.List;

import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.ValueVisitor;

/**
 * A function that takes in an IValue and determines if it's a double. Returns true if an empty
 * or null String. Returns false if the IValue is a list.
 */

public class AllDoubleVisitor implements ValueVisitor<Boolean> {
  @Override
  public Boolean visitDouble(double d) {
    return true;
  }

  @Override
  public Boolean visitBoolean(boolean b) {
    return false;
  }

  @Override
  public Boolean visitString(String s) {
    return s == null || s.equals("");
  }

  @Override
  public Boolean visitList(List<IValue> list) {
    for (IValue value : list) {
      if (value == null) {
        continue;
      }
      if (!value.accept(new IsDoubleVisitor())) {
        return false;
      }
    }
    return true;
  }
}
