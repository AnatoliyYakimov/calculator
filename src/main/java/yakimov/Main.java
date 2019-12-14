package yakimov;

import yakimov.exceptions.ParseException;
import yakimov.expressions.IExpression;

public class Main {

  public static void main(String[] args) {
    if (args.length == 0) {
      return;
    }
    try {

      IExpression expr = ExpressionParser.parse(args[0]);
      System.out.println(expr.count());

    } catch (ParseException e) {
      System.err.println(e);
    }
  }

}
