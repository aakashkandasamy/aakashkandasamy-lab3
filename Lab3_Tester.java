import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class Lab3_Tester {

  @Test
  public void test1() {
    System.out.println("process long text with found stopword");
    String expected = "7";
    StringBuffer input = new StringBuffer("This sentence is long enough to yellow pass this test. But, it could be -- even -- longer...");
    String result = "error";
    try{
      result = "" + WordCounter.processText(input, "yellow");
    } catch (Exception e) {}
    
    assertEquals(expected, result);
  }

  @Test
  public void test2() {
    System.out.println("process long text with no stopword");
    String expected = "16";
    StringBuffer input = new StringBuffer("This sentence is long enough to yellow pass this test. But, it could be -- even -- longer...");
    String result = "error";
    try{
      result = "" + WordCounter.processText(input, null);
    } catch (Exception e) {}
    assertEquals(expected, result);
  }

  @Test
  public void test3() {
    System.out.println("process long text with missing stopword");
    StringBuffer input = new StringBuffer("This sentence is long enough to yellow pass this test. But, it could be -- even -- longer...");
    String expected = "InvalidStopwordException: Couldn't find stopword: red";
    String result = "InvalidStopwordException not raised";
    try {
      WordCounter.processText(input, "red");
    } catch (InvalidStopwordException e){
      result = e.toString();
    } catch (Exception e) {}
    assertEquals(expected, result);
  }

  @Test
  public void test4() {
    System.out.println("process short text with found stopword");
    StringBuffer input = new StringBuffer("This red is");
    String expected = "TooSmallText: Only found 3 words.";
    String result = "TooSmallText not raised";
    try {
      WordCounter.processText(input, "red");
    } catch (TooSmallText e){
      result = e.toString();
    } catch (Exception e) {}
    assertEquals(expected, result);
  }

  @Test
  public void test5() {
    System.out.println("process short text with missing stopword");
    StringBuffer input = new StringBuffer("This yellow is");
    String expected = "TooSmallText: Only found 3 words.";
    String result = "TooSmallText not raised";
    try {
      WordCounter.processText(input, "red");
    } catch (TooSmallText e){
      result = e.toString();
    } catch (Exception e) {}
    assertEquals(expected, result);
  }

  @Test
  public void test6() {
    System.out.println("process short text with found stopword");
    StringBuffer input = new StringBuffer("This yellow is");
    String expected = "TooSmallText: Only found 3 words.";
    String result = "TooSmallText not raised";
    try {
      WordCounter.processText(input, "yellow");
    } catch (TooSmallText e){
      result = e.toString();
    } catch (Exception e) {}
    assertEquals(expected, result);
  }

  @Test
  public void test7() {
    System.out.println("process easy long text with end stopword");
    String expected = "10";
    StringBuffer input = new StringBuffer("This sentence is long enough to pass this test. yellow.");
    String result = "error";
    try{
      result = "" + WordCounter.processText(input, "yellow");
    } catch (Exception e) {}
    assertEquals(expected, result);
  }

  @Test
  public void test8() {
    System.out.println("process found file");
    String expected = "This file has enough words to not trigger and exception and the stopword we're going to use " +
        "is yellow so we shouldn't have scanned into this point -- it just isn't necessary...unless " +
        "the stopword we wanted was green in which case we stopped above. Or, perhaps no stopword was provided, " + 
        "so then we will read in the whole file. ";
    try {
      String strCurrentLine = null;
      BufferedWriter file = new BufferedWriter(new FileWriter("valid.txt"));
      file.write(expected);
      file.close();
    } catch (IOException err1) { }
    StringBuffer result = new StringBuffer("error");
    try{
      result = WordCounter.processFile("valid.txt");
    } catch (Exception e) {}
    assertEquals(expected, result.toString());
  }

  @Test
  public void test9() {
    System.out.println("process missing file");
    String expected = "This file has enough words to not trigger and exception and the stopword we're going to use " +
        "is yellow so we shouldn't have scanned into this point -- it just isn't necessary...unless " +
        "the stopword we wanted was green in which case we stopped above. Or, perhaps no stopword was provided, " + 
        "so then we will read in the whole file. ";

    InputStream sysInBackup = System.in; // backup System.in to restore it later
    ByteArrayInputStream in = new ByteArrayInputStream("valid.txt".getBytes());
    System.setIn(in);
    StringBuffer result = new StringBuffer("error");
    try{
      result = WordCounter.processFile("invalid.txt");
    } catch (Exception e) {}
    assertEquals(expected, result.toString());
    System.setIn(sysInBackup); // optionally, reset System.in to its original
  }

  @Test
  public void test10() {
    System.out.println("process empty file");
    try {
      String strCurrentLine = null;
      BufferedWriter file = new BufferedWriter(new FileWriter("empty.txt"));
      String expected = "";
      file.write(expected);
      file.close();
    } catch (IOException err1) { }

    String result = "failed to catch EmptyFileException";
    try {
      WordCounter.processFile("empty.txt");
    } catch (EmptyFileException e){
      result = e.toString();
    } catch (Exception e){
      result = "error";
    }
    assertEquals("EmptyFileException: empty.txt was empty", result);
  }

}
