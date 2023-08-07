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
 *  Tests the elements (constructors and methods) of the Summarizer class, which also
 *  serves to test all related helper methods (and all non-abstract methods from the
 *  AbstractInfoScraper class).
 */
public class SummarizerTest {

  Summarizer summarizer;

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
        # Java Arrays
        - An **array** is a collection of variables of the same type
        
        ## Declaring an Array
        - General Form: type[] arrayName;
        - only creates a reference
        - no array has actually been created yet
        
        ## Creating an Array (Instantiation)
        - General form:  arrayName = new type[numberOfElements];
        - numberOfElements must be a positive Integer.
        - Gotcha: Array size is not modifiable once instantiated.
        
        # Vectors
        - Vectors act like resizable arrays
        
        ## Declaring a vector
        - General Form: Vector<type> v = new Vector();
        - type needs to be a valid reference type
        
        ## Adding an element to a vector
        - v.add(object of type);
        """;

  /**
   * Initializes the fields of this tester class.
   */
  @BeforeEach
  public void initMdFile() {
    this.summarizer = new Summarizer();

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

    this.outPath = new File("src/test/resources/summarizeOutput.md").toPath();
  }

  /**
   * Deletes the output path that the Summarizer class writes to in this class's test,
   * to test certain functionality (specifically regarding Summarizer's ability to
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
   * Tests the scrapeFiles() method on the Summarizer class; specifically, tests
   * that summarize() properly creates a new file with the proper content
   * when the file defined by the output path doesn't already exist.
   */
  @Test
  public void scrapeFilesCreates() {
    assertFalse(Files.exists(this.outPath));
    summarizer.scrapeFiles(this.fileList, this.outPath);
    assertTrue(Files.exists(this.outPath));

    try {
      assertEquals(this.expected, Files.readString(this.outPath));
    } catch (IOException e) {
      fail();
    }

    assertThrows(RuntimeException.class, () ->
        summarizer.scrapeFiles(this.fileList,
            new File("src/test/fakeDirectory/summarizeOutput.md").toPath()));
  }

  /**
   * Tests the scrapeFiles() method on the Summarizer class; specifically, tests
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

    summarizer.scrapeFiles(this.fileList, this.outPath);


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
        () -> summarizer.scrapeFiles(this.fileList, this.outPath));
  }

  /**
   * Tests the getHeader() method on the Summarizer class
   */
  @Test
  public void getHeader() {
    StringBuilder fileInfo = new StringBuilder();
    StringBuilder currentLine1 = new StringBuilder("# Header example");

    String expected1 = """
        # Header example
        """;

    assertTrue(fileInfo.isEmpty());
    this.summarizer.getHeader(fileInfo, currentLine1);
    assertEquals(expected1, fileInfo.toString());

    String expected2 = """
        # Header example
        
        ## Another header!
        """;
    StringBuilder currentLine2 = new StringBuilder("## Another header!");

    this.summarizer.getHeader(fileInfo, currentLine2);
    assertEquals(expected2, fileInfo.toString());
  }

  /**
   * Tests the addIfNecessary() method on the Summarizer class.
   */
  @Test
  public void addIfNecessary() {
    StringBuilder fileInfo = new StringBuilder();
    String content1 = "Not a question";
    String expected = String.format("- Not a question%n");

    assertTrue(fileInfo.isEmpty());
    this.summarizer.addIfNecessary(fileInfo, content1);
    assertEquals(expected, fileInfo.toString());

    String content2 = "Is this a question?:::Yes";

    this.summarizer.addIfNecessary(fileInfo, content2);
    assertEquals(expected, fileInfo.toString());
  }

}