package cs3500.pa02.view;

import cs3500.pa02.model.Question;
import java.io.IOException;

/**
 * Represents a View object for a study session, displaying messages and information to a user
 */
public class StudySessionView implements View {

  private final Appendable output;

  /**
   * Creates a StudySessionView object with a given appendable output source to write messages and
   * information to
   *
   * @param output Appendable source of output to write messages to
   */
  public StudySessionView(Appendable output) {
    this.output = output;
  }

  /**
   * Displays a message asking the user for an input path to take study questions from.
   */
  @Override
  public void askForPath() {
    try {
      output.append(String.format("Welcome to your study session!%nPlease input the filepath of "
          + "the question bank you would like to study with:%n"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Displays a message asking the user for the number of questions they would like to study in
   * a study session
   */
  @Override
  public void askForNumQuestions() {
    try {
      output.append(String.format("Now, please input the number of questions you would like to "
          + "practice in this session:%n"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Displays the content of the given question to the user
   */
  @Override
  public void displayContent(Question question) {
    try {
      output.append(question.showingQuestion()
          ? String.format("%nQUESTION: " + question.getQuestion() + "%n") :
          String.format("%nANSWER: " + question.getAnswer() + "%n"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Displays question-related options to a user for them to choose from in order to advance
   * the study session, such as marking the given question as easy, marking it as hard, showing
   * either the question or the answer (i.e. the opposite of whatever is currently being shown),
   * or exit the study session.
   */
  @Override
  public void optionQuery(Question question) {
    try {
      output.append(String.format("Please choose an option and input the corresponding number:%n"));
      output.append(String.format("1. Mark Easy%n"));
      output.append(String.format("2. Mark Hard%n"));
      output.append(question.showingQuestion()
          ?  String.format("3. Show Answer%n") : String.format("3. Show Question%n"));
      output.append(String.format("4. Exit%n"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Displays numerical stats about a study session to the user, including the number of questions
   * answered, the number of questions that went from easy to hard, the number of questions that
   * went from hard to easy, the number of hard questions now in the question bank and the number
   * of easy questions now in the question bank
   */
  @Override
  public void displayStats(int[] stats) {
    try {
      output.append(String.format("%nSESSION STATS:%n"));
      output.append(String.format("You answered " + stats[0] + " question(s).%n"));
      output.append(String.format(stats[1] + " question(s) went from easy to hard.%n"));
      output.append(String.format(stats[2] + " question(s) went from hard to easy.%n"));
      output.append(String.format("%nCurrent Counts in Question Bank:%n"));
      output.append(String.format(stats[3] + " hard questions%n"));
      output.append(String.format(stats[4] + " easy questions%n"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Displays a message to the user containing some error message
   */
  @Override
  public void displayError(String msg) {
    try {
      output.append(String.format(msg + "%n"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


}
