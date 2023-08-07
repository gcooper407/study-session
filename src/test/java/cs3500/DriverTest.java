package cs3500;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import cs3500.pa02.Driver;
import cs3500.pa02.controller.Controller;
import cs3500.pa02.controller.StudySessionController;
import cs3500.pa02.view.StudySessionView;
import cs3500.pa02.view.View;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Tests the elements (constructors and methods) of the Driver class.
 */
public class DriverTest {


  /**
   * Tests the main method of the Driver class (with 3 command line arguments).
   * Note that testing the branch of main with no args is impossible (at least
   * with my capabilities) because it relies on System.in input, but
   * testing the StudySessionController is sufficient for testing the
   * underlying functionality (as this branch is dedicated to creating
   * an instance of the StudySessionController class).
   */
  @Test
  public void main() {
    String[] args = {"src/test/resources/Examples/",
        "filename",
        "src/test/resources/driverOutput.md"};
    String[] argsFail = {"src/test/resources/FakeDirectory/",
        "filename",
        "src/test/resources/driverOutput.md"};

    // tests (implicit) constructor
    Driver d = new Driver();

    assertThrows(RuntimeException.class, () -> Driver.main(argsFail));

    // implicitly tests that main runs properly when given valid inputs
    Driver.main(args);

    String expectedMd = """
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

    // tests that the inputs produce some expected output, located
    // in a test .md file
    try {
      assertEquals(expectedMd,
          Files.readString(Path.of("src/test/resources/driverOutput.md")));
    } catch (IOException e) {
      fail();
    }


    String expectedSr = String.format("- HARD***When creating an array, what must you "
        + "match?:::Data types of the reference and array%n- HARD***What is another way to think "
        + "of a vector?:::As a 2D array.%n");

    // tests that the inputs produce some expected output, located
    // in a test file
    try {
      assertEquals(expectedSr,
          Files.readString(Path.of("src/test/resources/driverOutput.sr")));
    } catch (IOException e) {
      fail();
    }

  }
}