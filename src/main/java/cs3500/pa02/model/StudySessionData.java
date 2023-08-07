package cs3500.pa02.model;

import cs3500.pa02.model.writer.FileWriter;
import cs3500.pa02.model.writer.Writer;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a collection of important data and functions for a study session.
 */
public class StudySessionData implements StudyModel {

  private File filepath;
  private QuestionBank questionBank;
  private Random rand;
  private ArrayList<Question> sessionQs;
  private Question currQ;
  private int numQs;
  private int numEasyToHard;
  private int numHardToEasy;

  /**
   * Initializes the data of a study session with a File to store updated questions, a QuestionBank
   * with questions to study with, a number of questions to study, and a Random object to randomize
   * the set of study questions. Initially, there is no current question, no session questions,
   * and no questions that have gone from hard to easy nor easy to hard.
   *
   * @param bankPath File object to store updated questions in
   * @param qb QuestionBank with Questions to Study with
   * @param numQs number of questions to study
   * @param rand Random object for randomizing the set of study questions
   */
  public StudySessionData(File bankPath, QuestionBank qb, int numQs, Random rand) {
    this.filepath = bankPath;
    this.questionBank = qb;
    this.currQ = null;
    this.rand = rand;
    this.sessionQs = new ArrayList<>();
    this.numQs = numQs;
    this.numEasyToHard = 0;
    this.numHardToEasy = 0;
  }

  /**
   * Initializes the data of a study session with a File to a bank of questions to study from (and
   * to update as the session progresses) and a number of questions to study.
   *
   * @param bankPath File object representing the path to the question bank
   * @param numQs number of questions to study with
   */
  public StudySessionData(File bankPath, int numQs) {
    this(bankPath, new QuestionBank(), numQs, new Random());

    this.questionBank.generateBank(bankPath);
    this.generateQuestionSet();
  }

  /**
   * Generates a random set of however many questions are to be studied in this session.
   */
  @Override
  public void generateQuestionSet() {

    ArrayList<Question> hard = new ArrayList<>(this.questionBank.getHardQuestions());
    ArrayList<Question> easy = new ArrayList<>(this.questionBank.getEasyQuestions());

    for (int i = 0; i < this.numQs; i += 1) {
      int idx;
      Question q;

      if (!hard.isEmpty()) {
        idx = this.rand.nextInt(hard.size());
        q = hard.remove(idx);
        this.sessionQs.add(q);
      } else if (!easy.isEmpty()) {
        idx = this.rand.nextInt(easy.size());
        q = easy.remove(idx);
        this.sessionQs.add(q);
      } else {
        break;
      }
    }

    // update numQs in case the user input exceeds the total number of
    // questions available in the bank (without this, numQs can be greater
    // than the number of actual questions that get seen, throwing off
    // the accuracy of the stats)
    this.numQs = this.sessionQs.size();
    this.nextQuestion();
  }

  /**
   * Gets the current question being studied in this session.
   *
   * @return the current Question being studied
   */
  @Override
  public Question getCurrQuestion() {
    return this.currQ;
  }

  /**
   * Moves this study session on to the next question in the set, throwing an exception if
   * there are no more questions to study.
   *
   * @throws IndexOutOfBoundsException if there are no more questions to study
   */
  @Override
  public void nextQuestion() throws IndexOutOfBoundsException {
    this.currQ = this.sessionQs.remove(0);
  }

  /**
   * Sets the difficulty of the current question to the given difficulty
   *
   * @param diff Difficulty (either EASY or HARD) to set this question's difficulty to
   */
  @Override
  public void setDifficulty(Difficulty diff) {
    Difficulty old = this.currQ.getDifficulty();

    this.questionBank.setQuestionDifficulty(this.currQ, diff);

    if (old == Difficulty.EASY && diff == Difficulty.HARD) {
      this.numEasyToHard += 1;
    } else if (old == Difficulty.HARD && diff == Difficulty.EASY) {
      this.numHardToEasy += 1;
    }
  }

  /**
   * Flips what element of the current question should currently be shown -- flipping either from
   * question to answer or answer to question (like flipping a flashcard).
   */
  @Override
  public void flipQandA() {
    this.currQ.flipQandA();
  }

  /**
   * Gets the list of questions left in the study session.
   *
   * @return ArrayList of Questions that are left in the study session.
   */
  @Override
  public ArrayList<Question> getSessionQuestions() {
    return this.sessionQs;
  }

  /**
   * Produces an array of important numerical stats about this study session, such as number of
   * questions answered, number of questions that went from easy to hard, number of questions
   * that went from hard to easy, number of hard questions now in the question bank, and number
   * of easy questions now in the question bank.
   *
   * @param endedEarly true if the study session was ended before the last question was completed
   * @return an array of important numerical stats about this study session
   */
  @Override
  public int[] getSessionStats(boolean endedEarly) {
    int[] stats =  new int[5];
    stats[0] = this.numQs - this.sessionQs.size();
    if (endedEarly) {
      stats[0] = stats[0] - 1;
    }
    stats[1] = this.numEasyToHard;
    stats[2] = this.numHardToEasy;
    stats[3] = this.questionBank.getHardQuestions().size();
    stats[4] = this.questionBank.getEasyQuestions().size();

    this.updateBank();

    return stats;
  }

  /**
   * Updates the File containing the question bank based on how this study session affected
   * the difficulty of certain questions.
   */
  private void updateBank() {
    Writer writer = new FileWriter(this.filepath);

    writer.write(this.questionBank.toString());
  }


}
