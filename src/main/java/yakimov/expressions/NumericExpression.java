package yakimov.expressions;

public class NumericExpression extends IExpression {

  private final double value;

  public NumericExpression(double value) {
    this.value = value;
  }

  @Override
  public double count() {
    return value;
  }

  public double getValue() {
    return value;
  }

}
