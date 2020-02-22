package edu.cs3500.spreadsheets.value;

/**
 * Represents a number output value.
 */

public class DoubVal implements IValue {
  private double val;

  public DoubVal(double val) {
    this.val = val;
  }

  @Override
  public <R> R accept(ValueVisitor<R> visitor) {
    return visitor.visitDouble(val);
  }

  @Override
  public String toString() {
    String formatted = String.format("%f", val);
    if (val >= 0) {
      formatted = formatted.replaceAll( "-", "");
    }
    return formatted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DoubVal doub = (DoubVal) o;
    Double compare = doub.val;
    return compare.equals(val);
  }

  @Override
  public int hashCode() {
    return Double.hashCode(val);
  }
}


