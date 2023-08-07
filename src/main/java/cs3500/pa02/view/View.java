package cs3500.pa02.view;

import cs3500.pa02.model.Question;

/**
 * A View of a study program, displaying messages and information to a user
 */
public interface View {

  /**
   * Displays a message asking the user for an input path to take study questions from
   */
  public void askForPath();

  /**
   * Displays a message asking the user for the number of questions to study in a study session
   */
  public void askForNumQuestions();

  /**
   * Displays the content of the given question to the user
   */
  public void displayContent(Question question);

  /**
   * Displays question-related options to a user for them to choose from in order to advance
   * the study session
   */
  public void optionQuery(Question question);

  /**
   * Displays numerical stats about a study session to the user
   */
  public void displayStats(int[] stats);

  /**
   * Displays a message to the user containing some error message
   */
  public void displayError(String msg);
}
