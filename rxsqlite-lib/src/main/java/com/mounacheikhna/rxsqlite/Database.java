package com.mounacheikhna.rxsqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.github.davidmoten.rx.Functions;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 */
public final class Database { //#FBN

  private SQLiteOpenHelper sqLiteOpenHelper;
  private SQLiteDatabase sQLiteDatabase;

  /**
   * Provides access for queries to a limited subset of {@link Database}
   * methods.
   */
  private final QueryContext queryContext;

  /**
   * ThreadLocal storage of the current {@link Scheduler} factory to use with
   * queries.
   */
  private final ThreadLocal<Func0<Scheduler>> currentSchedulerFactory = new ThreadLocal<>();

  //TODO: this will probably cause some problems since sqliteOpenHelper isnt like ConnectionProvider
  //and may not deal well with being threadLocal

  /**
   * ThreadLocal storage of the current {@link SQLiteDatabase} to use with
   * queries.
   */
  private final ThreadLocal<SQLiteDatabase> currentSqliteDatabase = new ThreadLocal<>();

  /**
   * Schedules non transactional queries.
   */
  private final Func0<Scheduler> nonTransactionalSchedulerFactory;

  static final ThreadLocal<CursorCache> cCache = new ThreadLocal<>();

  /**
   * Cursors are transformed with this transform once on creation in select
   * queries
   */
  private final Func1<Cursor, ? extends Cursor> cursorTransform;

  /**
   * Schedules using {@link Schedulers}.trampoline().
   */
  private static final Func0<Scheduler> CURRENT_THREAD_SCHEDULER_FACTORY = new Func0<Scheduler>() {

    @Override
    public Scheduler call() {
      return Schedulers.trampoline();
    }
  };
  private static Func1<Cursor, ? extends Cursor> IDENTITY_TRANSFORM = Functions.identity();

  public Database(final SQLiteOpenHelper sqLiteOpenHelper,
      Func0<Scheduler> nonTransactionalSchedulerFactory) {
    this(sqLiteOpenHelper, nonTransactionalSchedulerFactory, IDENTITY_TRANSFORM);
  }

  public Database(SQLiteOpenHelper sqLiteOpenHelper, Func0<Scheduler> nonTransactionalSchedulerFactory,
      Func1<Cursor, ? extends Cursor> cursorTransform) {
    this.sqLiteOpenHelper = sqLiteOpenHelper;
    if (nonTransactionalSchedulerFactory != null)
      this.nonTransactionalSchedulerFactory = nonTransactionalSchedulerFactory;
    else
      this.nonTransactionalSchedulerFactory = CURRENT_THREAD_SCHEDULER_FACTORY;
    this.queryContext = new QueryContext(this);
    this.cursorTransform = cursorTransform;
  }

  /**
   * Returns the currently defined {@link Cursor} transform.
   *
   * @return the current Cursor transform applied at the start of select
   *         queries
   */
  public Func1<Cursor, ? extends Cursor> getCursorTransform() {
    return cursorTransform;
  }

  /**
   * Returns the thread local current query queryContext (will not return null).
   * Will return overriden queryContext (for example using Database returned from
   * {@link Database#beginTransaction()} if set.
   *
   * @return
   */
  public QueryContext queryContext() {
    return queryContext;
  }

  public Observable<Boolean> beginTransaction(){
    return beginTransaction(Observable.empty());
  }

  private Observable<Boolean> beginTransaction(Observable<?> dependency) {
    update("begin")
  }

  //public QueryUpdate.Buil

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


  /**
   * Sets the current thread local {@link Scheduler} to be
   * {@link Schedulers#trampoline()}.
   */
  void beginTransactionSubscribe() {
    currentSchedulerFactory.set(CURRENT_THREAD_SCHEDULER_FACTORY);
  }

  /**
   * Resets the current thread local {@link Scheduler} to default.
   */
  void endTransactionSubscribe() {
    currentSchedulerFactory.set(null);
    cCache.set(null);
  }

  /**
   * Returns the current thread local {@link Scheduler}.
   *
   * @return
   */
  Scheduler currentScheduler() {
    if (currentSchedulerFactory.get() == null)
      return nonTransactionalSchedulerFactory.call();
    else
      return currentSchedulerFactory.get().call();
  }

  /**
   * Returns the current {@link SQLiteOpenHelper}.
   *
   * @return
   */
  SQLiteDatabase currentSqliteDatabase() {
    if (currentSqliteDatabase.get() == null)
      return sQLiteDatabase;
    else
      return currentSqliteDatabase.get();
  }



}

