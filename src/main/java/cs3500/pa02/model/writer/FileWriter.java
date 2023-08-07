package cs3500.pa02.model.writer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Represents an object that writes to a File.
 */
public class FileWriter implements Writer {

  private final File filepath;

  /**
   * Creates a FileWriter to write to a non-null File object
   *
   * @param filepath non-null File object
   */
  public FileWriter(File filepath) {
    this.filepath = Objects.requireNonNull(filepath);
  }

  /**
   * Writes content from a String
   *
   * @param content String whose content is written from.
   */
  @Override
  public void write(String content) {
    try {
      Files.write(this.filepath.toPath(), content.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
