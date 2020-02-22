package edu.cs3500.spreadsheets.model;

import java.util.List;

import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.ValueVisitor;

/**
 * Determines if the value is a number.
 */
public class IsDoubleVisitor implements ValueVisitor<Boolean> {
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
    return false;
  }

  @Override
  public Boolean visitList(List<IValue> list) {
    return false;
  }
}
