package cs3500.pa02.model.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *  Tests the elements (constructors and methods) of the FileWriter class, which also
 *  serves to test all related helper methods.
 */
class FileWriterTest {

  private File file;
  private File fakeFile;
  private FileWriter writer;
  private FileWriter fakeWriter;
  private String msg;

  /**
   * Initializes the fields of this tester class.
   */
  @BeforeEach
  public void initData() {
    this.file = new File("src/test/resources/fileWriterTest.sr");
    this.fakeFile = new File("src/test/fakeFolder/nonexistent.sr");
    this.writer = new FileWriter(this.file);
    this.fakeWriter = new FileWriter(this.fakeFile);
    this.msg = """
        - HARD***This question?:::Hard.
        - EASY***That question?:::Easy.
        """;
  }

  /**
   * Deletes the output path that the FileWriter class writes to in this class's test,
   * to test certain functionality (specifically regarding FileWriter's ability to
   * create a new file).
   */
  @AfterEach
  public void deleteOutFile() {
    try {
      Files.deleteIfExists(this.file.toPath());
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Tests the write() method on the FileWriter class; specifically, tests
   * that write() properly creates a new file with the proper content
   * when the file defined by the output path doesn't already exist.
   */
  @Test
  public void writeCreates() {

    assertFalse(Files.exists(this.file.toPath()));
    this.writer.write(msg);
    assertTrue(Files.exists(this.file.toPath()));

    try {
      assertEquals(msg, Files.readString(this.file.toPath()));
    } catch (IOException e) {
      fail();
    }

    assertThrows(RuntimeException.class, () -> fakeWriter.write(msg));
  }

  /**
   * Tests the write() method on the FileWriter class; specifically, tests
   * that write() properly overwrites an existing file with the proper content
   * when the file defined by the output path already exists.
   */
  @Test
  public void writeOverwrites() {

    byte[] bytes = "nothing here".getBytes();

    try {
      Files.write(this.file.toPath(), bytes);
      assertNotEquals(this.msg, Files.readString(this.file.toPath()));
    } catch (IOException e) {
      fail();
    }

    writer.write(msg);

    try {
      assertEquals(msg, Files.readString(this.file.toPath()));
    } catch (IOException e) {
      fail();
    }

  }

}