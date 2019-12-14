package yakimov;

import yakimov.exceptions.ParseException;
import yakimov.expressions.BinaryExpression;
import yakimov.expressions.FunctionalExpression;
import yakimov.expressions.IExpression;
import yakimov.expressions.NumericExpression;
import java.util.ArrayList;
import java.util.List;
import yakimov.operations.BinaryOperation;
import yakimov.operations.FunctionalOperator;

public class ExpressionParser {

  /**
   * Метод-оболочка
   * @param expressionStr - строка с выражением, которую вычислить
   * @return - Дерево вражения
   * @throws ParseException
   */
  public static IExpression parse(String expressionStr) throws ParseException {
    expressionStr = expressionStr.replaceAll(" ", "");
    char[] chars = expressionStr.toCharArray();
    return parseInternal(chars, 0, expressionStr.length());
  }

  /**
   * Рекурсивный метод вычисления выражения
   * @param chars - строка с вражением
   * @param from - нижняя граница
   * @param to - верхняя граница
   * @return - Дерево выражения
   * @throws ParseException
   */
  private static IExpression parseInternal(final char[] chars, int from, int to) throws ParseException {
    if (from >= to) {
      return null;
    }
    char ch = chars[from];
    if (ch == ')') {
      from++;
      if (from >= to) {
        return null;
      }
      ch = chars[from];
    }
    /**
     * Если открывающаяся скобка - ищем закрывающую скобку и парсим дальше
     */
    if (ch == '(') {
      int closingBracketPos = findClosingBracket(chars, from, to);
      if (closingBracketPos == -1) {
        throw new ParseException("Brackets is not balanced", from);
      } else {
        IExpression left = parseInternal(chars, from + 1, closingBracketPos);
        if (closingBracketPos == to - 1) {
          return left;
        }
        BinaryOperation op = null;
        try {
          op = BinaryOperation.parse(chars[closingBracketPos + 1]);
          IExpression right = parseInternal(chars, closingBracketPos + 2, to);
          return new BinaryExpression(left, right, op);
        } catch (ParseException e) {
          throw new ParseException(e.getMessage(), from);
        }
      }
    } else if (Character.isLetterOrDigit(ch)) {
      int maxPriorityOpPos = findLeastPriorityOperation(chars, from, to);
      if (maxPriorityOpPos == -1) {
        return parseSingleOperand(chars, from, to);
      }
      IExpression left = parseInternal(chars, from, maxPriorityOpPos);
      IExpression right = parseInternal(chars, maxPriorityOpPos + 1, to);
      BinaryOperation op = BinaryOperation.parse(chars[maxPriorityOpPos]);
      return new BinaryExpression(left, right, op);
    }
    throw new ParseException(String.format("Unknown symbol \"%s\"", ch), from);
  }

  /**
   *
   * @param chars
   * @param from
   * @param to
   * @return
   * @throws ParseException
   */
  private static IExpression parseSingleOperand(char[] chars, int from, int to) throws ParseException {
    char ch = chars[from];
    if (Character.isDigit(ch)) {
      int numEndPos = findNumEndPos(chars, from, to);
      IExpression expr = parseNumber(chars, from, numEndPos);
      if (numEndPos == to - 1) {
        return expr;
      }
    } else if (Character.isLetter(ch)) {
      String exprString = String.valueOf(chars, from, to - from);
      try {
        FunctionalOperator op = FunctionalOperator.parse(exprString);
        while (chars[from] != '(') {
          ++from;
        }
        to = findClosingBracket(chars, from, to);
        return parseFunctionalExpression(chars, from + 1, to, op);
      } catch (ParseException e) {
        throw new ParseException(String.format("Can`t parse substring: \"%s\"", exprString), from);
      }
    }
    throw new ParseException(String.format("Unknown symbol \"%s\"", ch), from);
  }

  private static IExpression parseFunctionalExpression(char[] chars, int from, int to, FunctionalOperator op)
      throws ParseException {
    int argsCount = op.getArgsCount();
    List<IExpression> args = new ArrayList<>(argsCount);
    for (int i = 0; i < argsCount - 1; i++) {
      int j = from;
      while (chars[j] != ',' && j < to) {
        j++;
      }
      if (j >= to) {
        throw new ParseException("Error while parsing functional operator", from);
      }
      args.add(parseInternal(chars, from, j));
      from = j + 1;
    }
    args.add(parseInternal(chars, from, to));
    return new FunctionalExpression(args, op.getFunction());
  }

  private static int findLeastPriorityOperation(char[] chars, int from, int to) throws ParseException {
    int i;
    for (i = to; --i > from;) {
      if (chars[i] == ')') {
        i = findOpeningBracketPos(chars, from, i) - 1;
        if (i < 0) {
          throw new ParseException("Unbalanced brackets", from);
        }
      }
      if (chars[i] == '+' || chars[i] == '-') {
        return i;
      }
    }
    for (i = to; --i > from;) {
      if (chars[i] == ')') {
        i = findOpeningBracketPos(chars, from, i) - 1;
      }
      if (chars[i] == '*' || chars[i] == '/') {
        return i;
      }
    }
    return -1;
  }

  private static int findNumEndPos(char[] chars, int i, int to) {
    boolean num = true;
    int j = i + 1;
    while (j < to && isNum(chars[j])) {
      j++;
    }
    return j - 1;
  }

  private static boolean isNum(char ch) {
    return ch >= '0' && ch <= '9';
  }

  private static IExpression parseNumber(char[] chars, int i, int to) {
    return new NumericExpression(Integer.parseInt(new String(chars, i, to - i + 1), 10));
  }

  private static int findClosingBracket(char[] chars, int from, int to) {
    int openedBracketsCount = 1;
    int j;
    for (j = from + 1; j < to && openedBracketsCount != 0; j++) {
      char ch = chars[j];
      switch (ch) {
        case '(':
          openedBracketsCount++;
          break;
        case ')':
          openedBracketsCount--;
          break;
      }
    }
    return openedBracketsCount == 0 ? j - 1 : -1;
  }

  private static int findOpeningBracketPos(char[] chars, int from, int to) {
    int closingBracketsCount = 1;
    int j;
    for (j = to - 1; j >= from && closingBracketsCount != 0; --j) {
      char ch = chars[j];
      switch (ch) {
        case ')':
          closingBracketsCount++;
          break;
        case '(':
          closingBracketsCount--;
          break;
      }
    }
    return closingBracketsCount == 0 ? j : -1;
  }
}
