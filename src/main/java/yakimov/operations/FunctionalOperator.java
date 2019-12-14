package yakimov.operations;

import yakimov.exceptions.ParseException;
import java.util.regex.Pattern;


public enum FunctionalOperator {
  LN("ln\\(.+\\)", 1, (args) -> Math.log(args.get(0))),
  SQRT("sqrt\\(.+\\)", 1, (args) -> Math.sqrt(args.get(0))),
  POW("pow\\(.+,.+\\)", 2, (args -> Math.pow(args.get(0), args.get(1))));

  private final Pattern pattern;
  private final int argsCount;
  private final IFunction function;

  public IFunction getFunction() {
    return function;
  }

  FunctionalOperator(String regex, int argCount, IFunction function) {
    this.pattern = Pattern.compile(regex);
    this.argsCount = argCount;
    this.function = function;
  }

  public static FunctionalOperator parse(String expr) throws ParseException {
    CharSequence sequence = expr.subSequence(0, expr.length());
    for (FunctionalOperator op : FunctionalOperator.values()) {
      if (op.pattern.matcher(sequence).matches()) {
        return op;
      }
    }
    throw new ParseException("Ошибка при парсинге функционального оператора");
  }

  public Pattern getPattern() {
    return pattern;
  }

  public int getArgsCount() {
    return argsCount;
  }
}
