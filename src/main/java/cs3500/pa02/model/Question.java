package cs3500.pa02.model;

/**
 * Represents a Question with a question, an answer, and a difficulty of either easy or hard
 */
public class Question {

  private String question;
  private String answer;
  private Difficulty difficulty;
  private boolean showQuestion;

  /**
   * Creates a Question based on a String representation of a question in a .sr file, with the
   * question portion (as opposed to the answer portion) currently being shown.
   *
   * @param str String representation of a question in a .sr file
   */
  public Question(String str) {
    this.difficulty = Difficulty.valueOf(str.substring(2, str.indexOf("***")));
    this.question = str.substring(str.indexOf("***") + 3, str.indexOf(":::"));
    this.answer = str.substring(str.indexOf(":::") + 3);
    this.showQuestion = true;
  }

  /**
   * Gets the question represented of this question object (i.e. the String containing the actual
   * question).
   *
   * @return String containing the question represented by this question object
   */
  public String getQuestion() {
    return this.question;
  }

  /**
   * Gets the answer of this Question object
   *
   * @return String containing the answer of this Question object
   */
  public String getAnswer() {
    return this.answer;
  }

  /**
   * Gets the difficulty of this Question object, either EASY or HARD
   *
   * @return the Difficulty of this Question, either EASY or HARD
   */
  public Difficulty getDifficulty() {
    return this.difficulty;
  }

  /**
   * Sets the difficulty of this Question object to the given Difficulty
   *
   * @param difficulty Difficulty (either EASY or HARD) to set this Question to
   */
  public void setDifficulty(Difficulty difficulty) {
    this.difficulty = difficulty;
  }

  /**
   * Returns true if this Question object is currently showing its question
   * (as opposed to its answer)
   *
   * @return true if this Question is currently showing its question
   */
  public boolean showingQuestion() {
    return this.showQuestion;
  }

  /**
   * Flips what element of this Question is currently being shown -- flipping either from
   * question to answer or answer to question (like flipping a flashcard).
   *
   * @return true if the question portion of this Question object is now being shown
   */
  public boolean flipQandA() {
    this.showQuestion = !this.showQuestion;
    return this.showQuestion;
  }

  /**
   * Produces a String representation of this Question (with proper formatting for a .sr file)
   *
   * @return String representation of this Question (properly formatted for a .sr file)
   */
  @Override
  public String toString() {
    return "- " + this.difficulty.toString() + "***" + this.question + ":::" + this.answer + "\n";
  }
}
