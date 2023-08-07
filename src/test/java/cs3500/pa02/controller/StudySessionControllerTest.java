package cs3500.pa02.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa02.model.writer.FileWriter;
import cs3500.pa02.model.writer.Writer;
import cs3500.pa02.view.StudySessionView;
import cs3500.pa02.view.View;
import java.io.File;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *  Tests the elements (constructors and methods) of the StudySessionController class, which also
 *  serves to test all related helper methods.
 */
class StudySessionControllerTest {

  private Appendable output;
  private View view;

  private Readable inputEndedEarly;
  private StudySessionController controllerEndedEarly;
  private String expectedEndedEarly;

  private Readable inputFull;
  private StudySessionController controllerFull;
  private String expectedFull;

  /**
   * Initializes the fields of this method class
   * Note that question set randomness is tested in StudySessionDataTest (proves that
   * question sets are randomly generated). Thus, these tests use a set of only two
   * questions in order to make it possible to test with an expected output.
   *
   */
  @BeforeEach
  public void initData() {
    String str = """
        - HARD***What have we learned in OOD?:::Nothing.
        - EASY***Is OOD useless so far?:::Yes.
        """;

    Writer writer = new FileWriter(new File("src/test/resources/controllerTest.sr"));
    writer.write(str);

    this.output = new StringBuilder();
    this.view = new StudySessionView(this.output);


    this.inputEndedEarly = new StringReader("""
        src/test/resources/controllerTest.sr
        2
        3
        1
        4
        """);

    this.controllerEndedEarly = new StudySessionController(inputEndedEarly, output, view);

    this.expectedEndedEarly = """
        Welcome to your study session!
        Please input the filepath of the question bank you would like to study with:
        Now, please input the number of questions you would like to practice in this session:
        
        QUESTION: What have we learned in OOD?
        Please choose an option and input the corresponding number:
        1. Mark Easy
        2. Mark Hard
        3. Show Answer
        4. Exit
                
        ANSWER: Nothing.
        Please choose an option and input the corresponding number:
        1. Mark Easy
        2. Mark Hard
        3. Show Question
        4. Exit
                
        QUESTION: Is OOD useless so far?
        Please choose an option and input the corresponding number:
        1. Mark Easy
        2. Mark Hard
        3. Show Answer
        4. Exit
                
        SESSION STATS:
        You answered 1 question(s).
        0 question(s) went from easy to hard.
        1 question(s) went from hard to easy.
                
        Current Counts in Question Bank:
        0 hard questions
        2 easy questions
        """;



    this.inputFull = new StringReader("""
        nonexistent.txt
        nonexistent.sr
        src/test/resources/controllerTest.sr
        -2
        eleven
        3
        5
        3
        1
        2
        """);

    this.controllerFull = new StudySessionController(inputFull, output, view);

    this.expectedFull = String.format("Welcome to your study session!%n"
        + "Please input the filepath of the question bank you would like to study with:%n"
        + "The given filepath does not lead to a valid .sr (question bank) file. "
        + "Please input a valid filepath.%n"
        + "The given filepath does not lead to a valid .sr (question bank) file. "
        + "Please input a valid filepath.%n"
        + """
        Now, please input the number of questions you would like to practice in this session:
        Input must be a natural number. Please input a valid (natural) number.
        Input must be a natural number. Please input a valid (natural) number.
        
        QUESTION: What have we learned in OOD?
        Please choose an option and input the corresponding number:
        1. Mark Easy
        2. Mark Hard
        3. Show Answer
        4. Exit
        
        Input must be a valid option number (1-4). Please input a valid option number.
        
        ANSWER: Nothing.
        Please choose an option and input the corresponding number:
        1. Mark Easy
        2. Mark Hard
        3. Show Question
        4. Exit
                
        QUESTION: Is OOD useless so far?
        Please choose an option and input the corresponding number:
        1. Mark Easy
        2. Mark Hard
        3. Show Answer
        4. Exit
                
        SESSION STATS:
        You answered 2 question(s).
        1 question(s) went from easy to hard.
        1 question(s) went from hard to easy.
                
        Current Counts in Question Bank:
        1 hard questions
        1 easy questions
        """);
  }

  @Test
  public void runEndEarly() {
    assertTrue(this.output.toString().isEmpty());
    this.controllerEndedEarly.run();
    assertFalse(this.output.toString().isEmpty());
    assertEquals(this.expectedEndedEarly, this.output.toString());
  }

  @Test
  public void runFull() {
    assertTrue(this.output.toString().isEmpty());
    this.controllerFull.run();
    assertFalse(this.output.toString().isEmpty());
    assertEquals(this.expectedFull, this.output.toString());
  }

}