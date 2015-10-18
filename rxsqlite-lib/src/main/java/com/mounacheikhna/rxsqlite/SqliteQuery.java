package com.mounacheikhna.rxsqlite;

import java.util.List;

/**
 * Created by cheikhnamouna on 10/18/15.
 */
public class SqliteQuery {

  private final String sql;
  private final List<String> names;

  public SqliteQuery(String sql, List<String> names) {
    this.sql = sql;
    this.names = names;
  }

  public String sql() {
    return sql;
  }

  public List<String> names() {
    return names;
  }

}
