package edu.cs3500.spreadsheets.value;

/**
 * Represents a true or false output value.
 */
public class BoolVal implements IValue {
  private boolean val;

  public BoolVal(boolean val) {
    this.val = val;
  }

  @Override
  public <R> R accept(ValueVisitor<R> visitor) {
    return visitor.visitBoolean(val);
  }

  @Override
  public String toString() {
    return Boolean.toString(val);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoolVal bool = (BoolVal) o;
    return val == bool.val;
  }

  @Override
  public int hashCode() {
    return Boolean.hashCode(val);
  }
}


