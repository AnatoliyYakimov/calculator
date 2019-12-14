package yakimov.operations;

import yakimov.exceptions.ParseException;
import yakimov.expressions.IExpression;

public enum BinaryOperation {
  PLUS,
  SUB,
  MULT,
  DIV;

  public static BinaryOperation parse(char ch) throws ParseException {
    BinaryOperation res = null;
    switch (ch) {
      case '+': res = PLUS; break;
      case '-': res = SUB; break;
      case '*': res = MULT; break;
      case '/': res = DIV; break;
      default: throw new ParseException("Указан неверный символ в качестве бинарного выражения");
    }
    return res;
  }
  
  public double perform(IExpression left, IExpression right) {
    double res = 0;
    switch (this) {
      case DIV: res = left.count() / right.count(); break;
      case SUB: res = left.count() - right.count(); break;
      case MULT: res = left.count() * right.count(); break;
      case PLUS: res = left.count() + right.count(); break;
    }
    return res;
  } 
}
