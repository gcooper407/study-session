package cs3500.pa02.model.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the FileReader class
 */
class FileReaderTest {

  private File file;
  private File fakeFile;
  private FileReader reader;
  private FileReader fakeReader;

  /**
   * Initializes the fields of this tester class.
   */
  @BeforeEach
  public void initData() {
    this.file = new File("sample.sr");
    this.fakeFile = new File("nonexistent.sr");
    this.reader = new FileReader(this.file);
    this.fakeReader = new FileReader(this.fakeFile);
  }

  @Test
  public void read() {
    String str = """
        - HARD***What is the highest waterfall in the world?:::Angel Falls (in Venezuela).
        - HARD***Which country is the largest in terms of land area?:::Russia.
        - HARD***What is the tallest mountain in North America?:::Denali (or Mount McKinley).
        - HARD***Which country is known as the Land of the Rising Sun?:::Japan.
        - EASY***What is the capital of Canada?:::Ottawa.
        - EASY***What is the largest river in Africa?:::The Nile River.
        - EASY****What is the official language of Japan?:::Japanese.
        - EASY***Which continent is known as the "Roof of the World"?:::Asia.
        """;

    assertEquals(str, this.reader.read());
    assertThrows(RuntimeException.class, () -> this.fakeReader.read());
  }
}