package cs3500.pa02.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *  Tests the elements (constructors and methods) of the Question class, which also
 *  serves to test all related helper methods.
 */
class QuestionTest {

  private Question hardQ;
  private Question easyQ;

  /**
   * Initializes the fields of this tester class.
   */
  @BeforeEach
  public void initQuestions() {
    this.hardQ = new Question("- HARD***What would we do without OOD?:::Suffer");
    this.easyQ = new Question("- EASY***What is the meaning of life?:::OOD");
  }

  @Test
  public void getQuestion() {
    assertEquals("What would we do without OOD?", this.hardQ.getQuestion());
    assertEquals("What is the meaning of life?", this.easyQ.getQuestion());

    assertNotEquals("Suffer", this.hardQ.getQuestion());
    assertNotEquals("OOD", this.easyQ.getQuestion());
  }

  @Test
  public void getAnswer() {
    assertEquals("Suffer", this.hardQ.getAnswer());
    assertEquals("OOD", this.easyQ.getAnswer());

    assertNotEquals("Suffer", this.easyQ.getAnswer());
    assertNotEquals("OOD", this.hardQ.getAnswer());
  }

  @Test
  public void getDifficulty() {
    assertEquals(Difficulty.HARD, this.hardQ.getDifficulty());
    assertEquals(Difficulty.EASY, this.easyQ.getDifficulty());

    assertNotEquals(Difficulty.EASY, this.hardQ.getDifficulty());
    assertNotEquals(Difficulty.HARD, this.easyQ.getDifficulty());
  }

  @Test
  public void setDifficulty() {
    // tests that setting a question to its existing difficulty doesn't change the difficulty
    assertEquals(Difficulty.HARD, this.hardQ.getDifficulty());
    this.hardQ.setDifficulty(Difficulty.HARD);
    assertEquals(Difficulty.HARD, this.hardQ.getDifficulty());

    assertEquals(Difficulty.EASY, this.easyQ.getDifficulty());
    this.easyQ.setDifficulty(Difficulty.EASY);
    assertEquals(Difficulty.EASY, this.easyQ.getDifficulty());

    // tests that setting a question to its opposite difficulty (HARD to EASY or EASY to HARD)
    // changes the difficulty
    assertEquals(Difficulty.HARD, this.hardQ.getDifficulty());
    this.hardQ.setDifficulty(Difficulty.EASY);
    assertEquals(Difficulty.EASY, this.hardQ.getDifficulty());
    assertNotEquals(Difficulty.HARD, this.hardQ.getDifficulty());

    assertEquals(Difficulty.EASY, this.easyQ.getDifficulty());
    this.easyQ.setDifficulty(Difficulty.HARD);
    assertEquals(Difficulty.HARD, this.easyQ.getDifficulty());
    assertNotEquals(Difficulty.EASY, this.easyQ.getDifficulty());
  }

  /**
   * Tests the showingQuestion() method on the Question class. Also tests the side effects
   * of the flipQA() method on the Question class.
   */
  @Test
  public void showingQuestion() {
    // Initially, Question objects are showing the question
    assertTrue(this.hardQ.showingQuestion());
    assertTrue(this.easyQ.showingQuestion());

    this.hardQ.flipQandA();

    // only after being flipped (once) do they show the answer
    assertFalse(this.hardQ.showingQuestion());
    assertTrue(this.easyQ.showingQuestion());

    this.easyQ.flipQandA();
    this.hardQ.flipQandA();

    // after being flipped again, they show the question once more
    assertTrue(this.hardQ.showingQuestion());
    assertFalse(this.easyQ.showingQuestion());
  }

  @Test
  public void flipQandA() {
    // tests that initially a Question object is showing the question;
    // flipping once returns false, and then the Question object is showing the answer;
    // flipping again returns true, then the Question object then is showing the question again
    assertTrue(this.hardQ.showingQuestion());
    assertFalse(this.hardQ.flipQandA());
    assertFalse(this.hardQ.showingQuestion());
    assertTrue(this.hardQ.flipQandA());
    assertTrue(this.hardQ.showingQuestion());

    assertTrue(this.easyQ.showingQuestion());
    assertFalse(this.easyQ.flipQandA());
    assertFalse(this.easyQ.showingQuestion());
    assertTrue(this.easyQ.flipQandA());
    assertTrue(this.easyQ.showingQuestion());
  }

  @Test
  public void testToString() {
    assertEquals("- HARD***What would we do without OOD?:::Suffer\n", this.hardQ.toString());
    assertEquals("- EASY***What is the meaning of life?:::OOD\n", this.easyQ.toString());

    this.hardQ.setDifficulty(Difficulty.EASY);
    assertEquals("- EASY***What would we do without OOD?:::Suffer\n", this.hardQ.toString());

    this.hardQ.setDifficulty(Difficulty.HARD);
    assertEquals("- HARD***What would we do without OOD?:::Suffer\n", this.hardQ.toString());

    this.easyQ.setDifficulty(Difficulty.EASY);
    assertEquals("- EASY***What is the meaning of life?:::OOD\n", this.easyQ.toString());

  }
}