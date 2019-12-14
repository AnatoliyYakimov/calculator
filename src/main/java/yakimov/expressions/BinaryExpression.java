package yakimov.expressions;

import yakimov.operations.BinaryOperation;

public class BinaryExpression extends IExpression {

  private IExpression left;
  private IExpression right;
  private BinaryOperation op;

  public BinaryExpression(IExpression left, IExpression right, BinaryOperation op) {
    this.left = left;
    this.right = right;
    this.op = op;
  }

  @Override
  public double count() {
    return op.perform(left, right);
  }

  public BinaryOperation getOp() {
    return op;
  }

  public void setOp(BinaryOperation op) {
    this.op = op;
  }

  public IExpression getLeft() {
    return left;
  }

  public void setLeft(IExpression left) {
    this.left = left;
  }

  public IExpression getRight() {
    return right;
  }

  public void setRight(IExpression right) {
    this.right = right;
  }
}
