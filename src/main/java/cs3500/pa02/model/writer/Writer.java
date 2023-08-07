package cs3500.pa02.model.writer;

/**
 * Interface for a writer that writes String content
 */
public interface Writer {

  /**
   * Writes content from a String
   *
   * @param content String whose content is written from.
   */
  public void write(String content);
}
