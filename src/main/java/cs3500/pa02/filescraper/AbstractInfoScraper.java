package cs3500.pa02.filescraper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a system that scrapes key info from a list of .md (Markdown) files, outputting the
 * necessary info into another file
 */
public abstract class AbstractInfoScraper implements InfoScraper {

  public StringBuilder allInfo;

  /**
   * Creates an AbstractInfoScraper with an empty StringBuilder of info
   */
  public AbstractInfoScraper() {
    this.allInfo = new StringBuilder();
  }

  /**
   * Scrapes key info from a given list of MarkdownFiles -- headers, important information
   * (denoted by double square brackets, [[*info*]]), questions, etc. -- and outputs this info
   * into a file at the given filepath
   *
   * @param input the ArrayList of MarkdownFiles to be summarized
   * @param outPath the filepath at which to store all the key info from the input files
   */
  @Override
  public void scrapeFiles(ArrayList<MarkdownFile> input, Path outPath) {

    // append the summary of each input file to contents
    for (MarkdownFile file : input) {
      allInfo.append(this.scrapeFile(file));
    }

    // Convert String to data for writing ("raw" byte data)
    byte[] data = allInfo.toString().getBytes();

    // The path may not exist, or we may not have permissions to write to it,
    // in which case we need to handle that error (hence try-catch)
    try {
      Files.write(outPath, data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Scrapes the important information of a given MarkdownFile, compiling it into
   * a single StringBuilder.
   *
   * @param file MarkdownFile to scrape important information from.
   * @return a StringBuilder containing a compilation of all the important information
   *         scraped from this MarkdownFile.
   */
  @Override
  public StringBuilder scrapeFile(MarkdownFile file) {
    // get the path of the given file
    Path p = file.getPath();

    // Initialize a Scanner to read the given file
    Scanner sc;
    // The file may not exist, in which case we need to handle that error
    try {
      sc = new Scanner(new FileInputStream(p.toFile()));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    // create a StringBuilder to store key info of the current file
    StringBuilder fileInfo = new StringBuilder();

    // while the file has a next line to scan
    while (sc.hasNextLine()) {
      // store the contents of the next line in a new StringBuilder
      StringBuilder currentLine = new StringBuilder(sc.nextLine());

      // if the line starts with a #, it is a header
      if (currentLine.indexOf("#") == 0) {
        // , so, store the entire line in the keyInfo
        this.getHeader(fileInfo, currentLine);
      } else { // otherwise, the line is not a header, so get the important body info
        this.getKeyInfo(sc, fileInfo, currentLine, false);
      }
    }

    return fileInfo;
  }


  /**
   * Scrapes the content of the important information (denoted with square brackets)
   * in a Markdown file
   *
   * @param sc Scanner that scans each line of a Markdown file
   * @param fileInfo a StringBuilder in which to store all the important information
   *                 scraped from the MarkdownFile being scanned.
   * @param currentInfo the current information being scanned for important information
   * @param inBrackets true if the information being currently scanned is within square brackets
   */
  @Override
  public void getKeyInfo(Scanner sc, StringBuilder fileInfo, StringBuilder currentInfo,
                               boolean inBrackets) {
    // if you're currently not within brackets, check for open brackets
    if (!inBrackets) {
      // get the index of the open brackets (-1 if they don't exist)
      int open = currentInfo.indexOf("[[");
      // if the current line contains open brackets, call this method on everything after
      // the open brackets; otherwise, do nothing
      if (open != -1) {
        this.getKeyInfo(sc, fileInfo, currentInfo.delete(0, open + 2), true);
      }
    } else { // else, you're currently within brackets (not yet found closing brackets)
      // get the index of the closing brackets (-1 if they don't exist)
      int close = currentInfo.indexOf("]]");

      // if current line doesn't contain closing brackets...
      if (close == -1) {

        // call this method on everything in brackets so far PLUS the next line
        // (guaranteed to exist, based on Assumption #1 from PA01)
        this.getKeyInfo(sc, fileInfo, currentInfo.append(sc.nextLine()), true);
      } else { // otherwise, the closing brackets are in the current line, so...

        // add everything before the closed brackets to allInfo if necessary
        this.addIfNecessary(fileInfo, currentInfo.substring(0, close));

        // store the rest of the line, and if it has more open brackets, get the important info
        // from it
        StringBuilder restOfLine = new StringBuilder(currentInfo.substring(close));
        if (restOfLine.indexOf("[[") != -1) {
          this.getKeyInfo(sc, fileInfo, restOfLine, false);
        }
      }
    }
  }

  /**
   * Scrapes the content of a header in a Markdown file.
   *
   * @param fileInfo the scraped contents of the file so far
   * @param currentLine the current line being scanned for information
   */
  @Override
  public abstract void getHeader(StringBuilder fileInfo, StringBuilder currentLine);

  /**
   * Adds important information to a StringBuilder of file information if the content
   * of the important information is deemed necessary to add
   *
   * @param fileInfo StringBuilder containing the file's information
   * @param content the content of the important information
   */
  public abstract void addIfNecessary(StringBuilder fileInfo, String content);

}
