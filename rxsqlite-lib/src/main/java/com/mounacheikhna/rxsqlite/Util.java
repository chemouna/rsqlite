package com.mounacheikhna.rxsqlite;

import com.mounacheikhna.rxsqlite.annotations.IsQuery;

/**
 * Created by cheikhnamouna on 10/19/15.
 */
public final class Util {

  static <T> void setSqlFromQueryAnnotation(Class<T> cls, QueryBuilder builder) {
    if (builder.sql() == null) {
      IsQuery query = cls.getAnnotation(IsQuery.class);
      if (query != null && query.value() != null) {
        String sql = query.value();
        builder.setSql(sql);
      } else
        throw new RuntimeException("Class " + cls
            + " must be annotated with @Query(sql) or sql must be specified to the builder.select() call");
    }
  }

  /**
   * Count the number of sqlite parameters in a sql statement.
   *
   * @param query
   *            .sql()
   * @return
   */
  static int parametersCount(Query query) {
    if (query.names().isEmpty())
      return countQuestionMarkParameters(query.sql());
    else
      return query.names().size();
  }

  // Visible for testing
  static int countQuestionMarkParameters(String sql) {
    // was originally using regular expressions, but they didn't work well
    // for ignoring parameter-like strings inside quotes.
    int count = 0;
    int length = sql.length();
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
        } else if (c == '?') {
          count++;
        }
      }
    }
    return count;
  }


}
