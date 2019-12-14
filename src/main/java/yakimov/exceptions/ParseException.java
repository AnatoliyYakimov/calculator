package yakimov.exceptions;

public class ParseException extends Exception {

  private int position;

  public ParseException(int position) {
    this.position = position;
  }

  public ParseException(String message) {
    super(message);
  }

  public ParseException(String message, int position) {
    super(message);
    this.position = position;
  }

  @Override
  public String toString() {
    return "yakimov.exceptions.ParseException{" +
        "position=" + position +
        ", message= \"" + getMessage() +
        "\"}";
  }
}
