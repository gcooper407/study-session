package cs3500.pa02.filescraper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the QuestionScraper class
 */
class QuestionScraperTest {

  QuestionScraper questionScraper;

  Path arrayPath;
  BasicFileAttributes arrayAttr;
  MarkdownFile arrays;

  Path vectorPath;
  BasicFileAttributes vectorAttr;
  MarkdownFile vectors;

  Path fakePath;
  MarkdownFile fakeFile;

  ArrayList<MarkdownFile> fileList = new ArrayList<>();

  Path outPath;

  String expected = """
      - HARD***When creating an array, what must you match?:::Data types of the reference and array
      - HARD***What is another way to think of a vector?:::As a 2D array.
        """;

  /**
   * Initializes the fields of this tester class.
   */
  @BeforeEach
  public void initSrFile() {
    this.questionScraper = new QuestionScraper();

    this.arrayPath = new File("src/test/resources/Examples/arrays.md").toPath();
    try {
      this.arrayAttr = Files.readAttributes(arrayPath, BasicFileAttributes.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.arrays = new MarkdownFile(arrayPath, arrayAttr);

    this.vectorPath = new File("src/test/resources/Examples/vectors.md").toPath();
    try {
      this.vectorAttr = Files.readAttributes(vectorPath, BasicFileAttributes.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.vectors = new MarkdownFile(vectorPath, vectorAttr);

    this.fakePath = Path.of("src/test/resources/FakeDirectory/fakeFile.md");
    this.fakeFile = new MarkdownFile(fakePath, vectorAttr);

    this.fileList = new ArrayList<>();
    this.fileList.add(this.arrays);
    this.fileList.add(this.vectors);

    this.outPath = new File("src/test/resources/questionScraperOutput.sr").toPath();
  }

  /**
   * Deletes the output path that the QuestionScraper class writes to in this class's test,
   * to test certain functionality (specifically regarding an InfoScraper's ability to
   * create a new file).
   */
  @AfterEach
  public void deleteOutFile() {
    try {
      Files.deleteIfExists(this.outPath);
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Tests the scrapeFiles() method on the QuestionScraper class; specifically, tests
   * that scrapeFiles() properly creates a new file with the proper content
   * when the file defined by the output path doesn't already exist.
   */
  @Test
  public void scrapeFilesCreates() {
    assertFalse(Files.exists(this.outPath));
    questionScraper.scrapeFiles(this.fileList, this.outPath);
    assertTrue(Files.exists(this.outPath));

    try {
      assertEquals(this.expected, Files.readString(this.outPath));
    } catch (IOException e) {
      fail();
    }

    assertThrows(RuntimeException.class, () ->
        questionScraper.scrapeFiles(this.fileList,
            new File("src/test/fakeDirectory/summarizeOutput.md").toPath()));
  }

  /**
   * Tests the scrapeFiles() method on the QuestionScraper class; specifically, tests
   * that scrapeFiles() properly overwrites an existing file with the proper content
   * when the file defined by the output path already exists.
   */
  @Test
  public void scrapeFilesOverwrites() {

    byte[] bytes = "nothing here".getBytes();

    try {
      Files.write(this.outPath, bytes);
      assertNotEquals(this.expected, Files.readString(this.outPath));
    } catch (IOException e) {
      fail();
    }

    questionScraper.scrapeFiles(this.fileList, this.outPath);


    try {
      assertEquals(expected, Files.readString(this.outPath));
    } catch (IOException e) {
      fail();
    }

  }


  /**
   * Tests that the scrapeFiles() method fails when a nonexistent file is passed in
   * as an element of the input list.
   */
  @Test
  public void scrapeFilesFails() {
    this.fileList.add(this.fakeFile);

    assertThrows(RuntimeException.class,
        () -> questionScraper.scrapeFiles(this.fileList, this.outPath));
  }

  @Test
  public void getHeader() {
    StringBuilder fileInfo = new StringBuilder();
    StringBuilder currentLine = new StringBuilder("# Header example");

    assertTrue(fileInfo.isEmpty());
    this.questionScraper.getHeader(fileInfo, currentLine);
    assertTrue(fileInfo.isEmpty());
  }

  @Test
  public void addIfNecessary() {
    StringBuilder fileInfo = new StringBuilder();
    String content1 = "Is this a question?:::Yes";
    String expected = String.format("- HARD***Is this a question?:::Yes%n");

    assertTrue(fileInfo.isEmpty());
    this.questionScraper.addIfNecessary(fileInfo, content1);
    assertEquals(expected, fileInfo.toString());

    String content2 = "Not a question";

    this.questionScraper.addIfNecessary(fileInfo, content2);
    assertEquals(expected, fileInfo.toString());
  }
}