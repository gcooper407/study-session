package cs3500.pa02.controller;

import cs3500.pa02.model.Difficulty;
import cs3500.pa02.model.StudyModel;
import cs3500.pa02.model.StudySessionData;
import cs3500.pa02.view.View;
import java.io.File;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Scanner;

/**
 * Controller of a study session, running the study session and handling user input.
 */
public class StudySessionController implements Controller {

  private final Readable input;
  private final Appendable output;
  private final View view;

  /**
   * Initializes a study session controller with a non-null readable input,
   * a non-null appendable output, and a non-null View object
   *
   * @param input non-null readable input
   * @param output nun-null appendable output
   * @param view non-null view object
   */
  public StudySessionController(Readable input, Appendable output, View view) {
    this.input = Objects.requireNonNull(input);
    this.output = Objects.requireNonNull(output);
    this.view = Objects.requireNonNull(view);
  }

  /**
   * Runs this StudySessionController, beginning the session for the user and taking user
   * input for initial setup.
   */
  public void run() {
    Scanner sc = new Scanner(this.input);
    StudyModel model = this.beginStudySession(sc);

    view.displayContent(model.getCurrQuestion());
    view.optionQuery(model.getCurrQuestion());

    this.handleOptions(sc, model);
  }

  /**
   * Begins a study session for the user using spaced repetition.
   *
   * @param sc Scanner object used to collect input from the user
   * @return a StudyModel object containing the important data of this study session
   */
  private StudyModel beginStudySession(Scanner sc) {
    view.askForPath();
    String inputPath = sc.nextLine();

    while (!inputPath.endsWith(".sr") || !Files.exists(new File(inputPath).toPath())) {
      view.displayError("The given filepath does not lead to a valid .sr (question bank) "
          + "file. Please input a valid filepath.");
      inputPath = sc.nextLine();
    }

    File questionBank = new File(inputPath);

    view.askForNumQuestions();
    int numQuestions = -1;

    while (numQuestions <= 0) {
      try {
        numQuestions = Integer.parseInt(sc.nextLine());
        if (numQuestions <= 0) {
          view.displayError("Input must be a natural number. "
              + "Please input a valid (natural) number.");
        }
      } catch (Exception e) {
        view.displayError("Input must be a natural number. "
            + "Please input a valid (natural) number.");
      }
    }

    return new StudySessionData(questionBank, numQuestions);
  }

  /**
   * Handles input from the user in response to question-dependent options.
   *
   * @param sc Scanner object used to collect user input
   * @param model StudyModel object containing the important data of this study session
   */
  private void handleOptions(Scanner sc, StudyModel model) {
    boolean endedEarly = false;

    while (sc.hasNextLine()) {
      String userInput = sc.nextLine();

      if (userInput.equals("1") || userInput.equals("2")) {
        model.setDifficulty(userInput.equals("1") ? Difficulty.EASY : Difficulty.HARD);

        try {
          model.nextQuestion();
        } catch (IndexOutOfBoundsException e) {
          break;
        }

      } else if (userInput.equals("3")) {
        model.flipQandA();

      } else if (userInput.equals("4")) {
        endedEarly = true;
        break;

      } else {
        view.displayError("\nInput must be a valid option number (1-4). "
            + "Please input a valid option number.");
        continue;
      }

      view.displayContent(model.getCurrQuestion());
      view.optionQuery(model.getCurrQuestion());
    }

    view.displayStats(model.getSessionStats(endedEarly));
  }
}
