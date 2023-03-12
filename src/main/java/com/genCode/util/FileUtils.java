package com.genCode.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {
  /**
   * Size of the buffer to read/write data
   */
  private static final int BUFFER_SIZE = 4096;

  public static void modifyFile(String filePath, List<String> oldStringList, List<String> newStringList) {
    File fileToBeModified = new File(filePath);
  
    String oldContent = "";
  
    BufferedReader reader = null;
  
    FileWriter writer = null;
  
    try {
      reader = new BufferedReader(new FileReader(fileToBeModified));
  
      // Reading all the lines of input text file into oldContent
      String line = reader.readLine();
      while (line != null) {
        oldContent = oldContent + line + System.lineSeparator();
  
        line = reader.readLine();
      }
  
      // Replacing oldString with newString in the oldContent
      String newContent = oldContent;
      for (int i = 0; i < oldStringList.size() && i < newStringList.size(); i++) {
        newContent = newContent.replaceAll(oldStringList.get(i), newStringList.get(i));
      }
  
      // Rewriting the input text file with newContent
      writer = new FileWriter(fileToBeModified);
      writer.write(newContent);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        reader.close();
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Extracts a zip file specified by the zipFilePath to a directory specified by
   * destDirectory (will be created if does not exists)
   * 
   * @param zipFilePath
   * @param destDirectory
   * @throws IOException
   */
  public static void unzip(String zipFilePath, String destDirectory) throws IOException {
    File destDir = new File(destDirectory);
    if (!destDir.exists()) {
      destDir.mkdir();
    }
    ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
    ZipEntry entry = zipIn.getNextEntry();
    // iterates over entries in the zip file
    while (entry != null) {
      String filePath = destDirectory + File.separator + entry.getName();
      if (!entry.isDirectory()) {
        // if the entry is a file, extracts it
        extractFile(zipIn, filePath);
      } else {
        // if the entry is a directory, make the directory
        File dir = new File(filePath);
        dir.mkdirs();
      }
      zipIn.closeEntry();
      entry = zipIn.getNextEntry();
    }
    zipIn.close();
  }

  /**
   * Extracts a zip entry (file entry)
   * 
   * @param zipIn
   * @param filePath
   * @throws IOException
   */
  private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
    byte[] bytesIn = new byte[BUFFER_SIZE];
    int read = 0;
    while ((read = zipIn.read(bytesIn)) != -1) {
      bos.write(bytesIn, 0, read);
    }
    bos.close();
  }
}
