import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.*;

public class WordCounter {

    public static int processText(StringBuffer text, String stopword) throws InvalidStopwordException, TooSmallText {
        Pattern regex = Pattern.compile("[a-zA-Z0-9']+");
        Matcher matcher = regex.matcher(text);
        int wordCount = 0;
        boolean stopwordFound = (stopword == null);

        while (matcher.find()) {
            wordCount++;
            if (!stopwordFound && matcher.group().equals(stopword)) {
                stopwordFound = true;
                break; // Stop counting words after finding the stopword
            }
        }

        if (!stopwordFound && stopword != null) {
            throw new InvalidStopwordException("Couldn't find stopword: " + stopword);
        }
        if (wordCount < 5) {
            throw new TooSmallText("Only found " + wordCount + " words.");
        }
        return wordCount;
    }

    public static StringBuffer processFile(String path) throws EmptyFileException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String fileContent = getFileContents(path);
                if (fileContent.trim().isEmpty()) {
                    throw new EmptyFileException(path + " was empty");
                }
                return new StringBuffer(fileContent);
            } catch (EmptyFileException e) {
                throw e; // Propagate the exception upwards
            } catch (IOException e) {
                System.out.println("File not found: " + path + ". Please re-enter the filename.");
                if (scanner.hasNextLine()) {
                    path = scanner.nextLine();
                } else {
                    // If no input is available (e.g., during testing), exit the loop
                    break;
                }
            }
        }
        return null;
    }

    public static String getFileContents(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int option = 0;
        while (option != 1 && option != 2) {
//            System.out.println("Choose option: 1 for file, 2 for text");
            try {
                option = Integer.parseInt(scanner.nextLine());
                if (option != 1 && option != 2) {
                    System.out.println("Invalid option. Please enter 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter 1 or 2.");
            }
        }

        if (args.length < 1) {
            System.out.println("No filename or text provided.");
            return;
        }

        String input = args[0];
        String stopword = args.length > 1 ? args[1] : null;

        try {
            if (option == 1) {
                String filename = input;
                StringBuffer content = processFile(filename);
                if (content != null) {
                    int wordCount = processText(content, stopword);
                    System.out.println("Found " + wordCount + " words.");
                } else {
                    System.out.println("No content to process.");
                }
            } else if (option == 2) {
                String text = input;
                int wordCount = processText(new StringBuffer(text), stopword);
                System.out.println("Found " + wordCount + " words.");
            }
        } catch (EmptyFileException e) {
            System.out.println(e.getMessage());
        } catch (InvalidStopwordException e) {
            System.out.println(e.getMessage());
            System.out.print("Enter a valid stopword: ");
            stopword = scanner.nextLine();
            try {
                if (option == 1) {
                    String filename = input;
                    StringBuffer content = processFile(filename);
                    if (content != null) {
                        int wordCount = processText(content, stopword);
                        System.out.println("Found " + wordCount + " words.");
                    } else {
                        System.out.println("No content to process.");
                    }
                } else if (option == 2) {
                    String text = input;
                    int wordCount = processText(new StringBuffer(text), stopword);
                    System.out.println("Found " + wordCount + " words.");
                }
            } catch (InvalidStopwordException ex) {
                System.out.println("Couldn't find the stopword again: " + ex.getMessage());
            } catch (TooSmallText ex) {
                System.out.println("Warning: " + ex.getMessage());
            } catch (EmptyFileException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (TooSmallText e) {
            System.out.println("Warning: " + e.getMessage());
        }
    }
}