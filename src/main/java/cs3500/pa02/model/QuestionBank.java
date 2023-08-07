package cs3500.pa02.model;

import cs3500.pa02.model.reader.FileReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a bank of questions to study with.
 */
public class QuestionBank {

  private ArrayList<Question> hardQs;
  private ArrayList<Question> easyQs;

  /**
   * Creates a question bank with no hard questions and no easy questions
   */
  public QuestionBank() {
    this.hardQs = new ArrayList<>();
    this.easyQs = new ArrayList<>();
  }

  /**
   * Creates a question bank with the given list of hard questions a given list of easy questions
   *
   * @param hardQs ArrayList of hard questions
   * @param easyQs ArrayList of easy questions
   */
  public QuestionBank(ArrayList<Question> hardQs, ArrayList<Question> easyQs) {
    this.hardQs = hardQs;
    this.easyQs = easyQs;
  }

  /**
   * Generates a question bank from questions located in a given File
   *
   * @param filePath File containing set of questions to make a study bank with
   */
  public void generateBank(File filePath) {
    String bank = new FileReader(filePath).read();

    // Initialize a Scanner to read the String of contents from the file
    Scanner sc = new Scanner(bank);

    // while the String has a next line to scan
    while (sc.hasNextLine()) {
      // create a Question using the contents of the next line
      // (proper formatting is given due to how I decided to structure .sr files)
      Question q = new Question(sc.nextLine());

      if (q.getDifficulty() == Difficulty.HARD) {
        this.hardQs.add(q);
      } else {
        this.easyQs.add(q);
      }
    }
  }

  /**
   * Gets the list of hard questions in this question bank
   *
   * @return an ArrayList containing all the hard questions in this question bank
   */
  public ArrayList<Question> getHardQuestions() {
    return this.hardQs;
  }

  /**
   * Gets the list of easy questions in this question bank
   *
   * @return an ArrayList containing all the easy questions in this question bank
   */
  public ArrayList<Question> getEasyQuestions() {
    return this.easyQs;
  }

  /**
   * Sets the given question in this question bank to the given Difficulty (EASY or HARD) and
   * updates this question bank's list of easy and hard questions accordingly
   *
   * @param q Question in question bank
   * @param diff Difficulty to set the question to
   */
  public void setQuestionDifficulty(Question q, Difficulty diff) {
    if (q.getDifficulty() == Difficulty.HARD) {
      this.hardQs.remove(q);
    } else {
      this.easyQs.remove(q);
    }

    q.setDifficulty(diff);

    if (diff == Difficulty.HARD) {
      this.hardQs.add(q);
    } else {
      this.easyQs.add(q);
    }
  }

  /**
   * Produces a String representation of this question bank
   *
   * @return String representation of this question bank
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (Question q : hardQs) {
      result.append(q.toString());
    }
    for (Question q : easyQs) {
      result.append(q.toString());
    }

    return result.toString();
  }



}
