package cs3500.pa02;

import cs3500.pa02.controller.Controller;
import cs3500.pa02.controller.StudySessionController;
import cs3500.pa02.filescraper.FileTreeWalkerVisitor;
import cs3500.pa02.filescraper.InfoScraper;
import cs3500.pa02.filescraper.MarkdownFile;
import cs3500.pa02.filescraper.QuestionScraper;
import cs3500.pa02.filescraper.Summarizer;
import cs3500.pa02.view.StudySessionView;
import cs3500.pa02.view.View;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This is the main driver of this project. It contains the main method, which either:
 * - (if 3 args are given) generates a study guide based on a set of files in a user-specified
 * input path, in some order based on a user-specified ordering flag, which is output to some
 * user-specified output path. It also generates a question bank with questions found in the
 * set of files, outputted to a .sr of the same path as the given input .md path.
 * - (if no args are given) begins a study session
 *
 */
public class Driver {

  /**
   * Project entry point -- this either:
   * - (if 3 args are given) generates a study guide based on a set of files in a user-specified
   * input path, in some order based on a user-specified ordering flag, which is output to some
   * user-specified output path. It also generates a question bank with questions found in the
   * set of files, outputted to a .sr of the same path as the given input .md path.
   * - (if no args are given) begins a study session
   *
   * @param args either:
   *             - notesRoot, a string representing the directory containing markdown files to
   *             summarize; orderingFlag, a string representing how the files should be ordered
   *             in the summary; outputPath, a string representing the directory where the summary
   *             (study guide) file should be output to
   *             - empty (for a study session)
   *
   */
  public static void main(String[] args) {
    if (args.length != 0) {

      // get the root path from the user input
      Path notesRoot = Path.of(args[0]);

      // create a FileTreeWalkerVisitor
      FileTreeWalkerVisitor pf = new FileTreeWalkerVisitor();

      // Walk the file tree (if possible)
      try {
        Files.walkFileTree(notesRoot, pf);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      // get the list of markdown files to summarize, based on user-inputted flag
      ArrayList<MarkdownFile> files = pf.getOrderedFiles(args[1]);

      // create a new Summarizer and designated output path String
      InfoScraper rw = new Summarizer();
      String sgPath = args[2];

      // create a new QuestionScraper and designated output path String
      InfoScraper qs = new QuestionScraper();
      String qbPath = sgPath.substring(0, sgPath.length() - 2).concat("sr");

      // make Path objects for the study guide and question bank, respectively
      Path studyGuidePath = new File(sgPath).toPath();
      Path questionBankPath = new File(qbPath).toPath();

      // summarize the list of markdown files and store the result in the file of the study guide
      // output path
      rw.scrapeFiles(files, studyGuidePath);

      // scrape all the questions from the list of markdown files and store the result in the file
      // of the question bank output path
      qs.scrapeFiles(files, questionBankPath);
    } else {
      Readable input = new InputStreamReader(System.in);
      Appendable output = System.out;
      View view = new StudySessionView(output);

      Controller controller = new StudySessionController(input, output, view);


      controller.run();
    }
  }

}