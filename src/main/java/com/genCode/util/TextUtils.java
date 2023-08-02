package com.genCode.util;

import org.apache.commons.lang3.StringUtils;

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

  public static String wordsToCamel(String input) {
    StringBuilder camelCase = new StringBuilder();

    boolean capitalizeNext = false;

    for (char c : input.toCharArray()) {
      if (Character.isWhitespace(c) || c == '_') {
        capitalizeNext = true;
      } else if (capitalizeNext) {
        camelCase.append(Character.toUpperCase(c));
        capitalizeNext = false;
      } else {
        camelCase.append(Character.toLowerCase(c));
      }
    }

    return camelCase.toString();
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

  public static String toPascalCase(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }

    StringBuilder result = new StringBuilder();
    boolean capitalizeNext = true;

    for (char c : input.toCharArray()) {
      if (Character.isWhitespace(c) || c == '-' || c == '_') {
        capitalizeNext = true;
      } else if (capitalizeNext) {
        result.append(Character.toUpperCase(c));
        capitalizeNext = false;
      } else {
        result.append(Character.toLowerCase(c));
      }
    }

    return result.toString();
  }

  public static String removeAccentAndUpper(String input){
    String strRemovedAcc = StringUtils.stripAccents(input);
    return adjOutput(strRemovedAcc.replace(" ", "_").toUpperCase());
  }

  public static String checkAccentSql(String input){
    // Loại bỏ khoảng trắng ở cuối chuỗi (nếu có)
    String trimmed = input.trim();

    // Kiểm tra và xóa dấu phẩy ở cuối chuỗi (nếu có)
    if (trimmed.endsWith(",")) {
      trimmed = trimmed.substring(0, trimmed.length() - 1);
    }

    return trimmed;
  }
}
