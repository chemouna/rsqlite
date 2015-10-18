package com.mounacheikhna.rxsqlite;

import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */
public final class Database { //#FBN

  private SQLiteOpenHelper sqLiteOpenHelper;

  /**
   * Provides access for queries to a limited subset of {@link Database}
   * methods.
   */
  private final QueryContext context;

  public Database(SQLiteOpenHelper sqLiteOpenHelper) {
    this.sqLiteOpenHelper = sqLiteOpenHelper;
  }

  /**
   * Returns the thread local current query context (will not return null).
   * Will return overriden context (for example using Database returned from
   * {@link Database#beginTransaction()} if set.
   *
   * @return
   */
  public QueryContext queryContext() {
    return context;
  }


  /**
   * Returns a {@link QuerySelect.Builder} builder based on the given select
   * statement sql.
   *
   * @param sql
   *            a select statement.
   * @return select query builder
   */
  public QuerySelect.Builder select(String sql) {
    return new QuerySelect.Builder(sql, this);
  }

}

