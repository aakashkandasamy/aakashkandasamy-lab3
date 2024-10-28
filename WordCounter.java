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
            }
        }

        if (wordCount < 5) {
            throw new TooSmallText("TooSmallText: Only found " + wordCount + " words.");
        }
        if (!stopwordFound) {
            throw new InvalidStopwordException("Couldn't find stopword: " + stopword);
        }
        return wordCount;
    }

    public static StringBuffer processFile(String path) throws EmptyFileException {
        StringBuffer content = new StringBuffer();

        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(path)));
            if (fileContent.trim().isEmpty()) {
                throw new EmptyFileException(path + " was empty");
            }
            content.append(fileContent);
        } catch (IOException e) {
            System.out.println("File not found: " + path + ". Please re-enter the filename.");
            Scanner scanner = new Scanner(System.in);
            return processFile(scanner.nextLine());
        }
        return content;
    } 

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int option = 0;
        while (option != 1 && option != 2) {
            System.out.println("Choose option: 1 for file, 2 for text");
            try {
                option = Integer.parseInt(scanner.nextLine());
                if (option != 1 && option != 2) {
                    System.out.println("Invalid option. Please enter 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter 1 or 2.");
            }
        }

        String stopword = args.length > 1 ? args[1] : null;
        try {
            if (option == 1) {
                // Process file
                System.out.print("Enter the filename: ");
                String filename = scanner.nextLine();
                StringBuffer content = processFile(filename);

                boolean validStopword = false;
                while (!validStopword) {
                    try {
                        int wordCount = processText(content, stopword);
                        System.out.println("Word count: " + wordCount);
                        validStopword = true;
                    } catch (InvalidStopwordException e) {
                        System.out.println(e.getMessage());
                        System.out.print("Enter a valid stopword: ");
                        stopword = scanner.nextLine();
                    } catch (TooSmallText e) {
                        System.out.println("Warning: " + e.getMessage());
                        validStopword = true;
                    }
                }
            } else if (option == 2) {

                System.out.print("Enter the text: ");
                String text = scanner.nextLine();

                try {
                    int wordCount = processText(new StringBuffer(text), stopword);
                    System.out.println("Word count: " + wordCount);
                } catch (TooSmallText e) {
                    System.out.println("Warning: " + e.getMessage());
                } catch (InvalidStopwordException e) {
                    System.out.println(e.getMessage());
                    System.out.print("Enter a valid stopword: ");
                    stopword = scanner.nextLine();
                    try {
                        int wordCount = processText(new StringBuffer(text), stopword);
                        System.out.println("Word count: " + wordCount);
                    } catch (InvalidStopwordException ex) {
                        System.out.println("Couldn't find the stopword again: " + ex.getMessage());
                    } catch (TooSmallText ex) {
                        System.out.println("Warning: " + ex.getMessage());
                    }
                }
            }
        } catch (EmptyFileException e) {
            System.out.println(e.getMessage());
        }
    }
}