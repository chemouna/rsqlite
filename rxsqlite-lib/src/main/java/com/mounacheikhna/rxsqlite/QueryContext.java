package com.mounacheikhna.rxsqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import rx.Scheduler;
import rx.functions.Func1;

/**
 * The threading and database connection context for mutliple jdbc queries.
 */
final class QueryContext {

  private final Database db;

  /**
   * Constructor.
   *
   * @param db {@link Database} instance
   */
  QueryContext(Database db) {
    this.db = db;
  }

  /**
   * Returns the scheduler service to use to run queries with this context.
   */
  Scheduler scheduler() {
    return db.currentScheduler();
  }

  //TODO: should we distinguish with this writable from readable db or just return SqliteOpenHelper ?
  /**
   * Returns the sqlite database for queries with this context.
   */
  SQLiteDatabase sQLiteDatabase() {
    return db.currentSqliteDatabase();
  }

  /*void beginTransactionObserve() {
    db.beginTransactionObserve();
  }*/

  void beginTransactionSubscribe() {
    db.beginTransactionSubscribe();
  }

  void endTransactionSubscribe() {
    db.endTransactionSubscribe();
  }

  /*void endTransactionObserve() {
    db.endTransactionObserve();
  }*/

  Func1<Cursor, ? extends Cursor> cursorTransform() {
    return db.getCursorTransform();
  }
}