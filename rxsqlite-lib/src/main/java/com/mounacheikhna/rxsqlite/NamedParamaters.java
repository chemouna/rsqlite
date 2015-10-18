package com.mounacheikhna.rxsqlite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheikhnamouna on 10/18/15.
 */
public final class NamedParamaters {

  public static SqliteQuery parse(String sql) {
    // was originally using regular expressions, but they didn't work well
    // for ignoring parameter-like strings inside quotes.
    List<String> names = new ArrayList<String>();
    int length = sql.length();
    StringBuilder parsedQuery = new StringBuilder(length);
    boolean inSingleQuote = false;
    boolean inDoubleQuote = false;
    for (int i = 0; i < length; i++) {
      char c = sql.charAt(i);
      if (inSingleQuote) {
        if (c == '\'') {
          inSingleQuote = false;
        }
      } else if (inDoubleQuote) {
        if (c == '"') {
          inDoubleQuote = false;
        }
      } else {
        if (c == '\'') {
          inSingleQuote = true;
        } else if (c == '"') {
          inDoubleQuote = true;
        } else if (c == ':' && i + 1 < length && !isFollowedOrPrefixedByColon(sql, i)
            && Character.isJavaIdentifierStart(sql.charAt(i + 1))) {
          int j = i + 2;
          while (j < length && Character.isJavaIdentifierPart(sql.charAt(j))) {
            j++;
          }
          String name = sql.substring(i + 1, j);
          c = '?'; // replace the parameter with a question mark
          i += name.length(); // skip past the end if the parameter
          names.add(name);
        }
      }
      parsedQuery.append(c);
    }
    return new SqliteQuery(parsedQuery.toString(), names);
  }

  static boolean isFollowedOrPrefixedByColon(String sql, int i) {
    return ':' == sql.charAt(i + 1) || (i > 0 && ':' == sql.charAt(i - 1));
  }

}
