package cs3500.pa02.filescraper;

/**
 * Represents a system that scrapes key info from a list of .md (Markdown) files, outputting the
 * questions to a question bank
 */
public class QuestionScraper extends AbstractInfoScraper {

  /**
   * Scrapes the content of a header in a Markdown file. Does nothing since question banks
   * do not contain headers.
   *
   * @param fileInfo    the scraped contents of the file so far
   * @param currentLine the current line being scanned for information
   */
  @Override
  public void getHeader(StringBuilder fileInfo, StringBuilder currentLine) {
    return;
  }

  /**
   * Adds important information to a StringBuilder of file information if the content
   * of the important information is deemed necessary to add (i.e. if it is a question).
   *
   * @param fileInfo StringBuilder containing the file's information
   * @param content the content of the important information
   */
  @Override
  public void addIfNecessary(StringBuilder fileInfo, String content) {
    // if the important info within the brackets is a question, add it to the question bank
    if (content.contains(":::")) {
      String question = String.format("- HARD***" + content + "%n");
      fileInfo.append(question);
    }
  }




}
