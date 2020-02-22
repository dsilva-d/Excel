package edu.cs3500.spreadsheets.model;

import java.util.List;

import edu.cs3500.spreadsheets.value.IValue;
import edu.cs3500.spreadsheets.value.ValueVisitor;

/**
 * A function only used for the < function. It takes a list of two numbers and determines
 * if the first one is less than the second one.
 */
public class LessThanVisitor implements ValueVisitor<Boolean> {

  @Override
  public Boolean visitDouble(double d) {
    throw new IllegalArgumentException("< function needs two double inputs.");
  }

  @Override
  public Boolean visitBoolean(boolean b) {
    throw new IllegalArgumentException("< function needs two double inputs.");
  }

  @Override
  public Boolean visitString(String s) {
    throw new IllegalArgumentException("< function needs two double inputs.");
  }

  @Override
  public Boolean visitList(List<IValue> list) {
    // at this point, we know each value in list is a DoubVal
    // there are only two values in the list
    return list.get(0).accept(new SumVisitor())
            < list.get(1).accept(new SumVisitor());
  }
}
