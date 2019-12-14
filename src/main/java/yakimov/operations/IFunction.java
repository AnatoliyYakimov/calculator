package yakimov.operations;

import java.util.List;

@FunctionalInterface
public interface IFunction {
  double count(List<Double> args);
}
