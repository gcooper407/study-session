package cs3500.pa02.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa02.model.Question;
import cs3500.pa02.model.writer.MockAppendable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *  Tests the elements (constructors and methods) of the StudySessionView class, which also
 *  serves to test all related helper methods.
 */
class StudySessionViewTest {

  private Appendable appendable;
  private StudySessionView view;
  private static final int[] ARR = new int[]{2, 1, 0, 2, 2};
  private static final Question QUESTION = new Question("- EASY***Question?:::Answer");
  private static final String ERR_MSG = String.format("some error message%n");

  /**
   * Initializes the fields of this tester class.
   */
  @BeforeEach
  void initView() {
    this.appendable = new StringBuilder();
    this.view = new StudySessionView(this.appendable);
  }

  @Test
  void askForPath() {
    String msg = String.format("Welcome to your study session!%nPlease input the filepath of "
        + "the question bank you would like to study with:%n");

    assertTrue(this.appendable.toString().isEmpty());
    assertNotEquals(msg, this.appendable.toString());

    this.view.askForPath();

    assertFalse(this.appendable.toString().isEmpty());
    assertEquals(msg, this.appendable.toString());
  }

  @Test
  void askForNumQuestions() {
    String msg = String.format("Now, please input the number of questions you would like to "
        + "practice in this session:%n");

    assertTrue(this.appendable.toString().isEmpty());
    assertNotEquals(msg, this.appendable.toString());

    this.view.askForNumQuestions();
    assertFalse(this.appendable.toString().isEmpty());
    assertEquals(msg, this.appendable.toString());
  }

  @Test
  void displayContentQuestion() {
    String msg = String.format("%nQUESTION: Question?%n");

    assertTrue(this.appendable.toString().isEmpty());
    assertNotEquals(msg, this.appendable.toString());

    this.view.displayContent(QUESTION);

    assertFalse(this.appendable.toString().isEmpty());
    assertEquals(msg, this.appendable.toString());
  }

  @Test
  void displayContentAnswer() {
    String msg = String.format("%nANSWER: Answer%n");

    QUESTION.flipQandA();

    assertTrue(this.appendable.toString().isEmpty());
    assertNotEquals(msg, this.appendable.toString());

    this.view.displayContent(QUESTION);

    assertFalse(this.appendable.toString().isEmpty());
    assertEquals(msg, this.appendable.toString());

    // reset q back to displaying question first (for future tests)
    QUESTION.flipQandA();
  }

  @Test
  void optionQueryQuestion() {
    String msg = """
        Please choose an option and input the corresponding number:
        1. Mark Easy
        2. Mark Hard
        3. Show Answer
        4. Exit
        """;

    assertTrue(this.appendable.toString().isEmpty());
    assertNotEquals(msg, this.appendable.toString());
    this.view.optionQuery(QUESTION);
    assertFalse(this.appendable.toString().isEmpty());
    assertEquals(msg, this.appendable.toString());
  }

  @Test
  void optionQueryAnswer() {
    String msg = """
        Please choose an option and input the corresponding number:
        1. Mark Easy
        2. Mark Hard
        3. Show Question
        4. Exit
        """;

    QUESTION.flipQandA();

    assertTrue(this.appendable.toString().isEmpty());
    assertNotEquals(msg, this.appendable.toString());
    this.view.optionQuery(QUESTION);
    assertFalse(this.appendable.toString().isEmpty());
    assertEquals(msg, this.appendable.toString());

    // reset q back to displaying question first (for future tests)
    QUESTION.flipQandA();
  }

  @Test
  void displayStats() {
    String msg = """

        SESSION STATS:
        You answered 2 question(s).
        1 question(s) went from easy to hard.
        0 question(s) went from hard to easy.
        
        Current Counts in Question Bank:
        2 hard questions
        2 easy questions
        """;

    assertTrue(this.appendable.toString().isEmpty());
    assertNotEquals(msg, this.appendable.toString());
    this.view.displayStats(ARR);
    assertFalse(this.appendable.toString().isEmpty());
    assertEquals(msg, this.appendable.toString());
  }

  @Test
  void displayError() {
    assertTrue(this.appendable.toString().isEmpty());
    assertNotEquals(ERR_MSG, this.appendable.toString());
    this.view.displayError("some error message");
    assertFalse(this.appendable.toString().isEmpty());
    assertEquals(ERR_MSG, this.appendable.toString());
  }

  @Test
  void testFailure() {
    StudySessionView failView = new StudySessionView(new MockAppendable());
    Question q = new Question("- EASY***Question:::Answer");


    Exception excAskPath = assertThrows(RuntimeException.class, () -> failView.askForPath(),
        "Mock throwing an error");
    assertEquals("java.io.IOException: Mock throwing an error",
        excAskPath.getMessage());

    Exception excNumQs = assertThrows(RuntimeException.class, () -> failView.askForNumQuestions(),
        "Mock throwing an error");
    assertEquals("java.io.IOException: Mock throwing an error",
        excNumQs.getMessage());

    Exception excContentQ = assertThrows(RuntimeException.class, () -> failView.displayContent(q),
        "Mock throwing an error");
    assertEquals("java.io.IOException: Mock throwing an error",
        excContentQ.getMessage());

    q.flipQandA();

    Exception excContentA = assertThrows(RuntimeException.class, () -> failView.displayContent(q),
        "Mock throwing an error");
    assertEquals("java.io.IOException: Mock throwing an error",
        excContentA.getMessage());

    Exception excOptions = assertThrows(RuntimeException.class, () -> failView.optionQuery(q),
        "Mock throwing an error");
    assertEquals("java.io.IOException: Mock throwing an error",
        excOptions.getMessage());

    Exception excStats = assertThrows(RuntimeException.class, () -> failView.displayStats(ARR),
        "Mock throwing an error");
    assertEquals("java.io.IOException: Mock throwing an error",
        excStats.getMessage());

    Exception excError = assertThrows(RuntimeException.class, () -> failView.displayError(ERR_MSG),
        "Mock throwing an error");
    assertEquals("java.io.IOException: Mock throwing an error",
        excError.getMessage());
  }
}