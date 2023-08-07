package cs3500.pa02.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *  Tests the elements (constructors and methods) of the StudySessionData class, which also
 *  serves to test all related helper methods.
 */
class StudySessionDataTest {


  private StudySessionData sampleData1;
  private StudySessionData sampleData2;
  private File sampleFile;
  private Question easyQ1;
  private Question easyQ2;
  private Question hardQ1;
  private Question hardQ2;

  /**
   * Initializes the fields of this tester class.
   */
  @BeforeEach
  void initData() {


    // this.sampleData = new StudySessionData(new File("sample.sr"), 6);

    try {
      this.sampleFile = File.createTempFile("temp", ".sr");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    QuestionBank emptyBank = new QuestionBank();

    this.easyQ1 = new Question("- EASY***How big is the Earth?:::Big");
    this.easyQ2 = new Question("- EASY***How big is the Sun?:::Bigger");
    this.hardQ1 = new Question("- HARD***How big is the moon?:::Small");
    this.hardQ2 = new Question("- HARD***How big is nothing?:::Smaller");

    ArrayList<Question> easyQs = new ArrayList<>();
    ArrayList<Question> hardQs = new ArrayList<>();

    easyQs.add(this.easyQ1);
    easyQs.add(this.easyQ2);
    hardQs.add(this.hardQ1);
    hardQs.add(this.hardQ2);

    QuestionBank fullBank = new QuestionBank(hardQs, easyQs);

    this.sampleData1 = new StudySessionData(this.sampleFile, fullBank,
        3, new Random(1));

    this.sampleData2 = new StudySessionData(this.sampleFile, fullBank,
        5, new Random(-2));

  }

  @Test
  void generateQuestionSet() {
    assertNull(this.sampleData1.getCurrQuestion());
    assertTrue(this.sampleData1.getSessionQuestions().isEmpty());

    this.sampleData1.generateQuestionSet();

    assertNotNull(this.sampleData1.getCurrQuestion());
    assertFalse(this.sampleData1.getSessionQuestions().isEmpty());

    // although 3 questions are initially added to the list of session questions,
    // the method's call to .nextQuestion() sets currQ equal to the first question in
    // the list, which also removes it from the list.
    assertEquals(2, this.sampleData1.getSessionQuestions().size());

    // based on random seed 1
    assertEquals(this.hardQ2, this.sampleData1.getCurrQuestion());
    assertEquals(this.hardQ1, this.sampleData1.getSessionQuestions().get(0));
    assertEquals(this.easyQ1, this.sampleData1.getSessionQuestions().get(1));



    assertTrue(this.sampleData2.getSessionQuestions().isEmpty());
    this.sampleData2.generateQuestionSet();
    assertFalse(this.sampleData2.getSessionQuestions().isEmpty());

    assertEquals(3, this.sampleData2.getSessionQuestions().size());

    // based on random seed -2
    assertEquals(this.hardQ1, this.sampleData2.getCurrQuestion());
    assertEquals(this.hardQ2, this.sampleData2.getSessionQuestions().get(0));
    assertEquals(this.easyQ2, this.sampleData2.getSessionQuestions().get(1));
    assertEquals(this.easyQ1, this.sampleData2.getSessionQuestions().get(2));
  }

  @Test
  void getCurrQuestion() {
    this.sampleData1.generateQuestionSet();

    assertEquals(this.hardQ2, this.sampleData1.getCurrQuestion());
    this.sampleData1.nextQuestion();
    assertEquals(this.hardQ1, this.sampleData1.getCurrQuestion());
    this.sampleData1.nextQuestion();
    assertEquals(this.easyQ1, this.sampleData1.getCurrQuestion());
  }

  /**
   * Also tests updateBank method()
   */
  @Test
  void nextQuestion() {
    this.sampleData2.generateQuestionSet();

    assertEquals(this.hardQ1, this.sampleData2.getCurrQuestion());
    this.sampleData2.nextQuestion();
    assertEquals(this.hardQ2, this.sampleData2.getCurrQuestion());
    this.sampleData2.nextQuestion();
    assertEquals(this.easyQ2, this.sampleData2.getCurrQuestion());
    this.sampleData2.nextQuestion();
    assertEquals(this.easyQ1, this.sampleData2.getCurrQuestion());
    assertThrows(IndexOutOfBoundsException.class, () -> this.sampleData2.nextQuestion());
  }

  /**
   * This also tests that effect that getSessionStats(), by calling updateBank(),
   * modifies the file containing the questions
   */
  @Test
  void getSessionStats() {
    String expected;

    // session with two hard questions, then two easy questions
    this.sampleData2.generateQuestionSet();
    // equivalent to exiting before answering question 1/4
    assertArrayEquals(new int[]{0, 0, 0, 2, 2}, this.sampleData2.getSessionStats(true));
    // labeling q1 as easy, moving to question 2/4
    this.sampleData2.setDifficulty(Difficulty.EASY);
    this.sampleData2.nextQuestion();
    // labeling q2 as hard, moving to question 3/4
    this.sampleData2.setDifficulty(Difficulty.HARD);
    this.sampleData2.nextQuestion();
    // equivalent to exiting before answering question 3/4
    assertArrayEquals(new int[]{2, 0, 1, 1, 3}, this.sampleData2.getSessionStats(true));

    String updatedQs = """
        - HARD***How big is nothing?:::Smaller
        - EASY***How big is the Earth?:::Big
        - EASY***How big is the Sun?:::Bigger
        - EASY***How big is the moon?:::Small
        """;

    try {
      expected = Files.readString(this.sampleFile.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    // tests that the question bank file has been properly updated
    assertEquals(expected, updatedQs);


    // labeling q3 as hard, moving to question 4/4
    this.sampleData2.setDifficulty(Difficulty.HARD);
    this.sampleData2.nextQuestion();
    // equivalent to exiting before answering question 4/4
    assertArrayEquals(new int[]{3, 1, 1, 2, 2}, this.sampleData2.getSessionStats(true));
    // labeling q4 as hard, showing final results (not exiting early)
    this.sampleData2.setDifficulty(Difficulty.EASY);
    assertArrayEquals(new int[]{4, 1, 1, 2, 2}, this.sampleData2.getSessionStats(false));

    updatedQs = """
        - HARD***How big is nothing?:::Smaller
        - HARD***How big is the Sun?:::Bigger
        - EASY***How big is the moon?:::Small
        - EASY***How big is the Earth?:::Big
        """;

    try {
      expected = Files.readString(this.sampleFile.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // tests that the question bank file has been properly updated once again
    assertEquals(expected, updatedQs);

  }

  @Test
  void setDifficulty() {
    this.sampleData1.generateQuestionSet();


    assertEquals(Difficulty.HARD, this.sampleData1.getCurrQuestion().getDifficulty());
    assertArrayEquals(new int[]{0, 0, 0, 2, 2}, this.sampleData1.getSessionStats(true));
    this.sampleData1.setDifficulty(Difficulty.EASY);
    assertEquals(Difficulty.EASY, this.sampleData1.getCurrQuestion().getDifficulty());
    assertArrayEquals(new int[]{0, 0, 1, 1, 3}, this.sampleData1.getSessionStats(true));

    this.sampleData1.nextQuestion();

    assertEquals(Difficulty.HARD, this.sampleData1.getCurrQuestion().getDifficulty());
    assertArrayEquals(new int[]{1, 0, 1, 1, 3}, this.sampleData1.getSessionStats(true));
    this.sampleData1.setDifficulty(Difficulty.HARD);
    assertEquals(Difficulty.HARD, this.sampleData1.getCurrQuestion().getDifficulty());
    assertArrayEquals(new int[]{1, 0, 1, 1, 3}, this.sampleData1.getSessionStats(true));

    this.sampleData1.nextQuestion();

    assertEquals(Difficulty.EASY, this.sampleData1.getCurrQuestion().getDifficulty());
    assertArrayEquals(new int[]{2, 0, 1, 1, 3}, this.sampleData1.getSessionStats(true));
    this.sampleData1.setDifficulty(Difficulty.HARD);
    assertEquals(Difficulty.HARD, this.sampleData1.getCurrQuestion().getDifficulty());
    assertArrayEquals(new int[]{2, 1, 1, 2, 2}, this.sampleData1.getSessionStats(true));
  }

  @Test
  void flipQandA() {
    this.sampleData2.generateQuestionSet();

    assertTrue(this.sampleData2.getCurrQuestion().showingQuestion());
    this.sampleData2.flipQandA();
    assertFalse(this.sampleData2.getCurrQuestion().showingQuestion());
    this.sampleData2.flipQandA();
    assertTrue(this.sampleData2.getCurrQuestion().showingQuestion());
  }


}