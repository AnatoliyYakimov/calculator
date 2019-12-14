package yakimov.expressions;

import java.util.List;
import java.util.stream.Collectors;
import yakimov.operations.IFunction;

public class FunctionalExpression extends IExpression {

  private final List<IExpression> args;
  private final IFunction function;

  public FunctionalExpression(List<IExpression> args, IFunction function) {
    this.args = args;
    this.function = function;
  }

  public List<IExpression> getArgs() {
    return args;
  }

  public IFunction getFunction() {
    return function;
  }

  @Override
  public double count() {
    return function.count(args.stream().map(IExpression::count).collect(Collectors.toList()));
  }
}
