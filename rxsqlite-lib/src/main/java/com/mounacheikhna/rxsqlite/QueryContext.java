package com.mounacheikhna.rxsqlite;

import android.database.Cursor;
import rx.Scheduler;
import rx.functions.Func1;

/**
 * The threading and database connection context for mutliple jdbc queries.
 *
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
   *
   * @return
   */
  Scheduler scheduler() {
    return db.currentScheduler();
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