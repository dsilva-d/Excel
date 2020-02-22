package edu.cs3500.spreadsheets.value;


/**
 * Represents an output value of String. If it's the empty String, the cell is considered empty
 * (both when raw input is null or the empty String).
 */

public class StriVal implements IValue {
  String val;

  public StriVal(String val) {
    this.val = val;
  }

  @Override
  public <R> R accept(ValueVisitor<R> visitor) {
    return visitor.visitString(val);
  }

  @Override
  public String toString() {
    if (val == null) {
      return "";
    }
    return val;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StriVal stringCompare = (StriVal) o;
    return this.toString().equals(stringCompare.toString());
  }

  @Override
  public int hashCode() {
    if (val == null) {
      return "".hashCode();
    }
    return val.hashCode();
  }
}


