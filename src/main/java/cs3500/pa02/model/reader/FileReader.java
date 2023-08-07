package cs3500.pa02.model.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents an object that reads the content of a file to a String
 */
public class FileReader implements Reader {

  private final File file;

  /**
   * Creates a FileReader object to read from a non-null File object
   *
   * @param file non-null file to read from
   */
  public FileReader(File file) {
    this.file = Objects.requireNonNull(file);
  }

  /**
   * Reads from this FileReader's File object to a String
   *
   * @return a String containing information read from the file.
   */
  @Override
  public String read() {
    // Initialize a Scanner to read the given file
    Scanner sc;
    // The file may not exist, in which case we need to handle that error
    try {
      sc = new Scanner(new FileInputStream(this.file));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    // create a StringBuilder to store key info of the current file
    StringBuilder output = new StringBuilder();

    // while the file has more to read
    while (sc.hasNextLine()) {
      // append the contents of the next line to the output StringBuilder
      // (with line breaks for delineation, to maintain formatting from the file)
      output.append(sc.nextLine()).append("\n");
    }

    // When we've read all there is to read ... return the output as a String
    return output.toString();
  }

}
