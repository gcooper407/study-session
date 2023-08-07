package cs3500.pa02.model.reader;

/**
 * Interface for a reader of a readable object.
 */
public interface Reader {

  /**
   * Reads from a readable object to a String
   *
   * @return a String containing information read from the readable object.
   */
  public String read();
}
