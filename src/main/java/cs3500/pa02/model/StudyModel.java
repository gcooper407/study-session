package cs3500.pa02.model;

import java.util.ArrayList;

/**
 * Represents the model of a program for studying, which stores important data and functions.
 */
public interface StudyModel {

  /**
   * Generates a set of questions to study with.
   */
  public void generateQuestionSet();

  /**
   * Gets the current question being studied.
   *
   * @return the current Question being studied.
   */
  public Question getCurrQuestion();

  /**
   * Moves to the next question in the set of questions.
   *
   * @throws IndexOutOfBoundsException if there is no next question
   */
  public void nextQuestion() throws IndexOutOfBoundsException;

  /**
   * Sets the difficulty of the current question to the given difficulty
   *
   * @param diff Difficulty (either EASY or HARD) to set this question's difficulty to
   */
  public void setDifficulty(Difficulty diff);

  /**
   * Flips what element of the current question should currently be shown -- flipping either from
   * question to answer or answer to question (like flipping a flashcard).
   */
  public void flipQandA();

  /**
   * Gets the list of questions left in the study session.
   *
   * @return ArrayList of Questions that are left in the study session.
   */
  public ArrayList<Question> getSessionQuestions();

  /**
   * Obtains an array of important numerical stats about this study session.
   *
   * @param endedEarly true if the study session was ended before the last question was completed
   * @return an array containing important numerical stats about this study session
   */
  public int[] getSessionStats(boolean endedEarly);


}
