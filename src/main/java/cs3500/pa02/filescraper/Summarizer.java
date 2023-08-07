package cs3500.pa02.filescraper;

/**
 * Represents a system that scrapes key info from a list of .md (Markdown) files, outputting the
 * info to a study guide
 */
public class Summarizer extends AbstractInfoScraper {

  /**
   * Scrapes the content of a header in a Markdown file.
   *
   * @param fileInfo the scraped contents of the file so far
   * @param currentLine the current line being scanned for information
   */
  @Override
  public void getHeader(StringBuilder fileInfo, StringBuilder currentLine) {
    // if the study guide file is empty, add the header without a new line before it
    if (fileInfo.isEmpty() && allInfo.isEmpty()) {
      fileInfo.append(currentLine).append("\n");
    } else { // otherwise, add the header with a new line before it
      fileInfo.append("\n").append(currentLine).append("\n");
    }
  }

  /**
   * Adds important information to a StringBuilder of file information if the content
   * of the important information is deemed necessary to add (i.e. if it is not a question).
   *
   * @param fileInfo StringBuilder containing the file's information
   * @param content the content of the important information
   */
  @Override
  public void addIfNecessary(StringBuilder fileInfo, String content) {
    // if the important info within the brackets is not a question, add it to the study guide
    if (!content.contains(":::")) {
      String importantInfo = String.format("- " + content + "%n");
      fileInfo.append(importantInfo);
    }
  }

}
