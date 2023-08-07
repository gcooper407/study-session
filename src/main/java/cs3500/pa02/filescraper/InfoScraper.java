package cs3500.pa02.filescraper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents an interface of the framework for a program that scrapes information
 * from .md (Markdown) files.
 */
public interface InfoScraper {

  /**
   * Scrapes key info from a given list of MarkdownFiles -- headers, important information
   * (denoted by double square brackets, [[*info*]]), questions, etc. -- and outputs this info
   * into a file at the given filepath
   *
   * @param input the ArrayList of MarkdownFiles to be summarized
   * @param outPath the filepath at which to store all the key info from the input files
   */
  void scrapeFiles(ArrayList<MarkdownFile> input, Path outPath);

  /**
   * Scrapes the important information of a given MarkdownFile, compiling it into
   * a single StringBuilder.
   *
   * @param file MarkdownFile to scrape important information from.
   * @return a StringBuilder containing a compilation of all the important information
   *         scraped from this MarkdownFile.
   */
  StringBuilder scrapeFile(MarkdownFile file);

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
  void getKeyInfo(Scanner sc, StringBuilder fileInfo, StringBuilder currentInfo,
                  boolean inBrackets);

  /**
   * Scrapes the content of a header in a Markdown file.
   *
   * @param fileInfo the scraped contents of the file so far
   * @param currentLine the current line being scanned for information
   */
  void getHeader(StringBuilder fileInfo, StringBuilder currentLine);

}
