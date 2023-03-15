package com.genCode.util;

import java.util.HashSet;
import java.util.Set;

public class TextUtils {
  private static Set<String> reservedKeywords = new HashSet<String>();

  public static void initReservedKeyword(String keywords) {
    reservedKeywords.clear();
    if (keywords == null || keywords.isEmpty()) {
      return;
    }
    String[] kArray = keywords.split(",");
    for (String k : kArray) {
      if (k.isEmpty()) {
        continue;
      }
      reservedKeywords.add(k);
    }
  }

  public static String wordsToNoSpace(String words) {
    return adjOutput(words.replace(" ", ""));
  }

  public static String wordsToCamel(String words) {
    String t = words.replace(" ", "");
    t = t.substring(0, 1).toLowerCase() + t.substring(1);
    return adjOutput(t);
  }

  public static String wordsToCamelFirstUpper(String words) {
    String t = words.replace(" ", "");
    t = t.substring(0, 1).toUpperCase() + t.substring(1);
    return adjOutput(t);
  }

  public static String wordsToLowerCase(String words) {
    String t = words.replace(" ", "");
    t = t.toLowerCase();
    return adjOutput(t);
  }

  public static String wordsToSnakeLower(String words) {
    return adjOutput(words.replace(" ", "_").toLowerCase());
  }

  public static String wordsToSnakeUpper(String words) {
    return adjOutput(words.replace(" ", "_").toUpperCase());
  }

  public static String wordsToKebabLower(String words) {
    return words.replace(" ", "-").toLowerCase();
  }

  public static String wordsToUpperCase(String words) {
    return adjOutput(words.replace(" ", "").toUpperCase());
  }
  
  public static String getPlural(String words) {
    String ret = "";
    String[] strs = words.split(" ");
    for (int i = 0; i < strs.length - 1; i++) {
      ret += strs[i] + " ";
    }
    ret += Inflector.getInstance().pluralize(strs[strs.length - 1]);
    return ret;
  }

  private static String adjOutput(String str) {
    if (reservedKeywords.contains(str)) {
      return "_" + str;
    }
    return str;
  }
}
