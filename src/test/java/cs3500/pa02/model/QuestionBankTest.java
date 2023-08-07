package cs3500.pa02.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *  Tests the elements (constructors and methods) of the QuestionBank class, which also
 *  serves to test all related helper methods.
 */
class QuestionBankTest {

  private QuestionBank emptyBank;
  private QuestionBank fullBank;
  private ArrayList<Question> hardQs;
  private ArrayList<Question> easyQs;
  private Question easyQ1;
  private Question easyQ2;
  private Question hardQ1;
  private Question hardQ2;

  /**
   * Initializes the fields of this tester class.
   */
  @BeforeEach
  public void initBank() {
    this.emptyBank = new QuestionBank();

    this.easyQ1 = new Question("- EASY***How big is the Earth?::::Big");
    this.easyQ2 = new Question("- EASY***How big is the Sun?::::Bigger");
    this.hardQ1 = new Question("- HARD***How big is the moon?:::Small");
    this.hardQ2 = new Question("- HARD***How big is nothing?:::Smaller");

    easyQs = new ArrayList<>();
    hardQs = new ArrayList<>();

    easyQs.add(this.easyQ1);
    easyQs.add(this.easyQ2);
    hardQs.add(this.hardQ1);
    hardQs.add(this.hardQ2);

    this.fullBank = new QuestionBank(hardQs, easyQs);
  }

  @Test
  public void generateBank() {
    File temp;

    try {
      temp = File.createTempFile("temp", ".sr");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    String questions = """
        - HARD***How big is the moon?:::Small
        - HARD***How big is nothing?:::Smaller
        - EASY***How big is the Earth?::::Big
        - EASY***How big is the Sun?::::Bigger
        """;

    try {
      Files.write(temp.toPath(), questions.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    assertNotEquals(fullBank.toString(), emptyBank.toString());

    emptyBank.generateBank(temp);

    assertEquals(fullBank.toString(), emptyBank.toString());

    assertThrows(RuntimeException.class,
        () -> emptyBank.generateBank(new File("nonexistent.sr")));


  }

  @Test
  public void getHardQuestions() {
    assertEquals(this.hardQs, this.fullBank.getHardQuestions());
    assertNotEquals(this.easyQs, this.fullBank.getHardQuestions());

    assertEquals(2, this.fullBank.getHardQuestions().size());
    assertEquals(this.hardQ1, this.fullBank.getHardQuestions().get(0));
    assertEquals(this.hardQ2, this.fullBank.getHardQuestions().get(1));

    assertFalse(this.fullBank.getHardQuestions().contains(this.easyQ1));
    assertFalse(this.fullBank.getHardQuestions().contains(this.easyQ2));
  }

  @Test
  public void getEasyQuestions() {
    assertEquals(this.easyQs, this.fullBank.getEasyQuestions());
    assertNotEquals(this.hardQs, this.fullBank.getEasyQuestions());

    assertEquals(2, this.fullBank.getEasyQuestions().size());
    assertEquals(this.easyQ1, this.fullBank.getEasyQuestions().get(0));
    assertEquals(this.easyQ2, this.fullBank.getEasyQuestions().get(1));

    assertFalse(this.fullBank.getEasyQuestions().contains(this.hardQ1));
    assertFalse(this.fullBank.getEasyQuestions().contains(this.hardQ2));
  }

  @Test
  public void setQuestionDifficulty() {
    // tests that initially, a hard question's difficulty is HARD and exists within the
    // list of hard questions
    assertEquals(Difficulty.HARD, hardQ1.getDifficulty());
    assertTrue(hardQs.contains(hardQ1));
    assertNotEquals(Difficulty.EASY, hardQ1.getDifficulty());
    assertFalse(easyQs.contains(hardQ1));

    // set the difficulty of a hard question to EASY
    fullBank.setQuestionDifficulty(hardQ1, Difficulty.EASY);

    // tests that after setting the difficulty to EASY, a hard question's difficulty is EASY,
    // no longer HARD, and it exists in the list of easy questions
    assertEquals(Difficulty.EASY, hardQ1.getDifficulty());
    assertTrue(easyQs.contains(hardQ1));
    assertNotEquals(Difficulty.HARD, hardQ1.getDifficulty());
    assertFalse(hardQs.contains(hardQ1));


    // tests that initially, a hard question's difficulty is HARD and exists within the
    // list of hard questions
    assertEquals(Difficulty.EASY, easyQ2.getDifficulty());
    assertTrue(easyQs.contains(easyQ2));
    assertNotEquals(Difficulty.HARD, easyQ2.getDifficulty());
    assertFalse(hardQs.contains(easyQ2));

    // set the difficulty of a hard question to EASY
    fullBank.setQuestionDifficulty(easyQ2, Difficulty.HARD);

    // tests that after setting the difficulty to EASY, a hard question's difficulty is EASY,
    // no longer HARD, and it exists in the list of easy questions
    assertEquals(Difficulty.HARD, easyQ2.getDifficulty());
    assertTrue(hardQs.contains(easyQ2));
    assertNotEquals(Difficulty.EASY, easyQ2.getDifficulty());
    assertFalse(easyQs.contains(easyQ2));


    // tests that setting the difficulty of a question to its current difficulty does not
    // change the question's difficulty nor which list in exists in
    assertEquals(Difficulty.HARD, hardQ2.getDifficulty());
    assertTrue(hardQs.contains(hardQ2));

    fullBank.setQuestionDifficulty(hardQ2, Difficulty.HARD);

    assertEquals(Difficulty.HARD, hardQ2.getDifficulty());
    assertTrue(hardQs.contains(hardQ2));
  }

  @Test
  public void testToString() {
    String fullResult = """
        - HARD***How big is the moon?:::Small
        - HARD***How big is nothing?:::Smaller
        - EASY***How big is the Earth?::::Big
        - EASY***How big is the Sun?::::Bigger
        """;

    assertEquals("", this.emptyBank.toString());
    assertEquals(fullResult, this.fullBank.toString());
  }
}