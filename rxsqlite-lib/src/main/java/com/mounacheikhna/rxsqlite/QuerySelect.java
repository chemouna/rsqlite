package com.mounacheikhna.rxsqlite;

import android.database.Cursor;
import android.support.annotation.NonNull;
import com.github.davidmoten.rx.Functions;
import com.mounacheikhna.rxsqlite.tuple.Tuple2;
import com.mounacheikhna.rxsqlite.tuple.Tuple3;
import com.mounacheikhna.rxsqlite.tuple.Tuple4;
import com.mounacheikhna.rxsqlite.tuple.Tuple5;
import com.mounacheikhna.rxsqlite.tuple.Tuple6;
import com.mounacheikhna.rxsqlite.tuple.Tuple7;
import com.mounacheikhna.rxsqlite.tuple.TupleN;
import com.mounacheikhna.rxsqlite.tuple.Tuples;
import java.sql.ResultSet;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

import static com.mounacheikhna.rxsqlite.Queries.bufferedParameters;

/**
 * Created by cheikhnamouna on 10/18/15.
 */
final public class QuerySelect implements Query { //#FBN

  private final Observable<Parameter> parameters;
  private final QueryContext context;
  private final SqliteQuery sqliteQuery;
  private final Func1<Cursor, ? extends Cursor> cursorTransform;
  private Observable<?> depends = Observable.empty();

  public QuerySelect(@NonNull String sql, @NonNull Observable<Parameter> parameters,
      @NonNull Observable<?> depends, @NonNull QueryContext context,
      Func1<Cursor, ? extends Cursor> cursorTransform) {
    this.sqliteQuery = NamedParamaters.parse(sql);
    this.parameters = parameters;
    this.depends = depends;
    this.context = context;
    this.cursorTransform = cursorTransform;
  }

  @Override
  public String sql() {
    return sqliteQuery.sql();
  }

  @Override
  public QueryContext context() {
    return context;
  }

  @Override
  public Observable<Parameter> parameters() {
    return parameters;
  }

  @Override
  public List<String> names() {
    return sqliteQuery.names();
  }

  @Override
  public String toString() {
    return "QuerySelect [sql=" + sql() + "]";
  }

  @Override
  public Observable<?> depends() {
    return depends;
  }

  //TODO: maybe this should somehow a map ?
  Func1<Cursor, ? extends Cursor> cursorTransform() {
    return cursorTransform;
  }

  /**
   * Returns the results of running a select query with all sets of
   * parameters.
   *
   * @return
   */
  public <T> Observable<T> execute(CursorMapper<? extends T> function) {
    return bufferedParameters(this)
        // execute once per set of parameters
        .concatMap(executeOnce(function));
  }

  /**
   * Returns a {@link Func1} that itself returns the results of pushing one
   * set of parameters through a select query.
   *
   * @param query
   * @return
   */
  private <T> Func1<List<Parameter>, Observable<T>> executeOnce(
      final CursorMapper<? extends T> function) {
    return new Func1<List<Parameter>, Observable<T>>() {
      @Override
      public Observable<T> call(List<Parameter> params) {
        return executeOnce(params, function);
      }
    };
  }

  /**
   * Returns an Observable of the results of pushing one set of parameters
   * through a select query.
   *
   * @param params
   *            one set of parameters to be run with the query
   * @return
   */
  private <T> Observable<T> executeOnce(final List<Parameter> params,
      CursorMapper<? extends T> function) {
    return QuerySelectOnSubscribe.execute(this, params, function)
        .subscribeOn(context.scheduler());
  }


  /**
   * Builds a {@link QuerySelect}.
   */
  public static final class Builder {

    /**
     * Builds the standard stuff.
     */
    private final QueryBuilder builder;

    /**
     * The {@link ResultSet} is transformed before use.
     */
    private Func1<Cursor, ? extends Cursor> cursorTransform = Functions.identity();

    /**
     * Constructor.
     *
     * @param sql
     * @param db
     */
    public Builder(String sql, Database db) {
      builder = new QueryBuilder(sql, db);
    }

    /**
     * Appends the given parameters to the parameter list for the query. If
     * there are more parameters than required for one execution of the
     * query then more than one execution of the query will occur.
     *
     * @param parameters
     * @return this
     */
    public <T> Builder parameters(Observable<T> parameters) {
      builder.parameters(parameters);
      return this;
    }

    /**
     * Appends the given parameter values to the parameter list for the
     * query. If there are more parameters than required for one execution
     * of the query then more than one execution of the query will occur.
     *
     * @param objects
     * @return this
     */
    public Builder parameters(Object... objects) {
      builder.parameters(objects);
      return this;
    }

    /**
     * Appends a parameter to the parameter list for the query. If there are
     * more parameters than required for one execution of the query then
     * more than one execution of the query will occur.
     *
     * @param value
     * @return this
     */
    public Builder parameter(Object value) {
      builder.parameter(value);
      return this;
    }

    /**
     * Sets a named parameter. If name is null throws a
     * {@link NullPointerException}. If value is instance of Observable then
     * throws an {@link IllegalArgumentException}.
     *
     * @param name
     *            the parameter name. Cannot be null.
     * @param value
     *            the parameter value
     */
    public Builder parameter(String name, Object value) {
      builder.parameter(name, value);
      return this;
    }

    /**
     * Appends a dependency to the dependencies that have to complete their
     * emitting before the query is executed.
     *
     * @param dependency
     * @return this
     */
    public Builder dependsOn(Observable<?> dependency) {
      builder.dependsOn(dependency);
      return this;
    }

    /**
     * Appends a dependency on the result of the last transaction (
     * <code>true</code> for commit or <code>false</code> for rollback) to
     * the dependencies that have to complete their emitting before the
     * query is executed.
     *
     * @return this
     */
    public Builder dependsOnLastTransaction() {
      builder.dependsOnLastTransaction();
      return this;
    }

    /**
     * The Cursor is transformed by the given transform before the
     * it is traversed.
     *
     * @param transform
     *            transforms the cursor
     * @return this
     */
    public Builder cursorTransform(Func1<Cursor, ? extends Cursor> transform) {
      this.cursorTransform = transform;
      return this;
    }

    /**
     * Transforms the results using the given function.
     *
     * @param function
     * @return the cursor of the query as an Observable
     */
    public <T> Observable<T> get(CursorMapper<? extends T> function) {
      return get(function, builder, cursorTransform);
    }

    static <T> Observable<T> get(CursorMapper<? extends T> function, QueryBuilder builder,
        Func1<Cursor, ? extends Cursor> cursorTransform) {
      return new QuerySelect(builder.sql(), builder.parameters(), builder.depends(),
          builder.context(), cursorTransform).execute(function);
    }

    /**
     * <p>
     * Transforms each row of the {@link ResultSet} into an instance of
     * <code>T</code> using <i>automapping</i> of the ResultSet columns into
     * corresponding constructor parameters that are assignable. Beyond
     * normal assignable criteria (for example Integer 123 is assignable to
     * a Double) other conversions exist to facilitate the automapping:
     * </p>
     * <p>
     * They are:
     * <ul>
     * <li>java.sql.Blob &#10143; byte[]</li>
     * <li>java.sql.Blob &#10143; java.io.InputStream</li>
     * <li>java.sql.Clob &#10143; String</li>s
     * <li>java.sql.Clob &#10143; java.io.Reader</li>
     * <li>java.sql.Date &#10143; java.util.Date</li>
     * <li>java.sql.Date &#10143; Long</li>
     * <li>java.sql.Timestamp &#10143; java.util.Date</li>
     * <li>java.sql.Timestamp &#10143; Long</li>
     * <li>java.sql.Time &#10143; java.util.Date</li>
     * <li>java.sql.Time &#10143; Long</li>
     * <li>java.math.BigInteger &#10143;
     * Short,Integer,Long,Float,Double,BigDecimal</li>
     * <li>java.math.BigDecimal &#10143;
     * Short,Integer,Long,Float,Double,BigInteger</li>
     * </p>
     *
     * @param cls
     * @return
     */
    /*public <T> Observable<T> autoMap(Class<T> cls) {
      return autoMap(cls, builder, cursorTransform);
    }

    static <T> Observable<T> autoMap(Class<T> cls, QueryBuilder builder,
        Func1<Cursor, ? extends Cursor> cursorTransform) {
      Util.setSqlFromQueryAnnotation(cls, builder);
      return get(Util.autoMap(cls), builder, resultSetTransform);
    }*/

    /**
     * Automaps the first column of the ResultSet into the target class
     * <code>cls</code>.
     *
     * @param cls
     * @return
     */
    public <S> Observable<S> getAs(Class<S> cls) {
      return get(Tuples.single(cls));
    }

    /**
     * Automaps all the columns of the {@link ResultSet} into the target
     * class <code>cls</code>. See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls
     * @return
     */
    public <S> Observable<TupleN<S>> getTupleN(Class<S> cls) {
      return get(Tuples.tupleN(cls));
    }

    /**
     * Automaps all the columns of the {@link ResultSet} into {@link Object}
     * . See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls
     * @return
     */
    public <S> Observable<TupleN<Object>> getTupleN() {
      return get(Tuples.tupleN(Object.class));
    }

    /**
     * Automaps the columns of the {@link ResultSet} into the specified
     * classes. See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls1
     * @param cls2
     * @return
     */
    public <T1, T2> Observable<Tuple2<T1, T2>> getAs(Class<T1> cls1, Class<T2> cls2) {
      return get(Tuples.tuple(cls1, cls2));
    }

    /**
     * Automaps the columns of the {@link ResultSet} into the specified
     * classes. See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls1
     * @param cls2
     * @param cls3
     * @return
     */
    public <T1, T2, T3> Observable<Tuple3<T1, T2, T3>> getAs(Class<T1> cls1, Class<T2> cls2,
        Class<T3> cls3) {
      return get(Tuples.tuple(cls1, cls2, cls3));
    }

    /**
     * Automaps the columns of the {@link ResultSet} into the specified
     * classes. See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls1
     * @param cls2
     * @param cls3
     * @param cls4
     * @return
     */
    public <T1, T2, T3, T4> Observable<Tuple4<T1, T2, T3, T4>> getAs(Class<T1> cls1,
        Class<T2> cls2, Class<T3> cls3, Class<T4> cls4) {
      return get(Tuples.tuple(cls1, cls2, cls3, cls4));
    }

    /**
     * Automaps the columns of the {@link ResultSet} into the specified
     * classes. See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls1
     * @param cls2
     * @param cls3
     * @param cls4
     * @param cls5
     * @return
     */
    public <T1, T2, T3, T4, T5> Observable<Tuple5<T1, T2, T3, T4, T5>> getAs(Class<T1> cls1,
        Class<T2> cls2, Class<T3> cls3, Class<T4> cls4, Class<T5> cls5) {
      return get(Tuples.tuple(cls1, cls2, cls3, cls4, cls5));
    }

    /**
     * Automaps the columns of the {@link ResultSet} into the specified
     * classes. See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls1
     * @param cls2
     * @param cls3
     * @param cls4
     * @param cls5
     * @param cls6
     * @return
     */
    public <T1, T2, T3, T4, T5, T6> Observable<Tuple6<T1, T2, T3, T4, T5, T6>> getAs(
        Class<T1> cls1, Class<T2> cls2, Class<T3> cls3, Class<T4> cls4, Class<T5> cls5,
        Class<T6> cls6) {
      return get(Tuples.tuple(cls1, cls2, cls3, cls4, cls5, cls6));
    }

    /**
     * Automaps the columns of the {@link ResultSet} into the specified
     * classes. See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls1
     * @param cls2
     * @param cls3
     * @param cls4
     * @param cls5
     * @param cls6
     * @param cls7
     * @return
     */
    public <T1, T2, T3, T4, T5, T6, T7> Observable<Tuple7<T1, T2, T3, T4, T5, T6, T7>> getAs(
        Class<T1> cls1, Class<T2> cls2, Class<T3> cls3, Class<T4> cls4, Class<T5> cls5,
        Class<T6> cls6, Class<T7> cls7) {
      return get(Tuples.tuple(cls1, cls2, cls3, cls4, cls5, cls6, cls7));
    }

    public Observable<Integer> count() {
      return get(Util.toOne()).count();
    }

    /**
     * Returns an {@link Observable.Operator} to allow the query to be pushed
     * parameters via the {@link Observable#lift(Observable.Operator)} method.
     *
     * @return operator that acts on parameters
     */
    public OperatorBuilder<Object> parameterOperator() {
      return new OperatorBuilder<Object>(this, OperatorType.PARAMETER);
    }

    /**
     * Returns an {@link Observable.Operator} to allow the query to be pushed
     * dependencies via the {@link Observable#lift(Observable.Operator)} method.
     *
     * @return operator that acts on dependencies
     */
    public OperatorBuilder<Object> dependsOnOperator() {
      return new OperatorBuilder<Object>(this, OperatorType.DEPENDENCY);
    }

    /**
     * Returns an {@link Observable.Operator} that runs a select query for each list of
     * parameter objects in the source observable.
     *
     * @return
     */
    public OperatorBuilder<Observable<Object>> parameterListOperator() {
      return new OperatorBuilder<Observable<Object>>(this, OperatorType.PARAMETER_LIST);
    }

  }

  /**
   * Builder pattern for select query {@link Observable.Operator}.
   */
  public static class OperatorBuilder<R> {

    private final Builder builder;
    private final OperatorType operatorType;

    /**
     * Constructor.
     *
     * @param builder
     * @param operatorType
     */
    public OperatorBuilder(Builder builder, OperatorType operatorType) {
      this.builder = builder;
      this.operatorType = operatorType;
    }

    /**
     * Transforms the results using the given function.
     *
     * @param function
     * @return
     */
    public <T> Observable.Operator<T, R> get(ResultSetMapper<? extends T> function) {
      return new QuerySelectOperator<T, R>(builder, function, operatorType);
    }

    /**
     * See {@link Builder#autoMap(Class)}.
     *
     * @param cls
     * @return
     */
    public <S> Observable.Operator<S, R> autoMap(Class<S> cls) {
      return get(Util.autoMap(cls));
    }

    /**
     * Automaps the first column of the ResultSet into the target class
     * <code>cls</code>.
     *
     * @param cls
     * @return
     */
    public <S> Observable.Operator<S, R> getAs(Class<S> cls) {
      return get(Tuples.single(cls));
    }

    /**
     * Automaps all the columns of the {@link ResultSet} into the target
     * class <code>cls</code>. See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls
     * @return
     */
    public <S> Observable.Operator<TupleN<S>, R> getTupleN(Class<S> cls) {
      return get(Tuples.tupleN(cls));
    }

    /**
     * Automaps all the columns of the {@link ResultSet} into {@link Object}
     * . See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls
     * @return
     */
    public <S> Observable.Operator<TupleN<Object>, R> getTupleN() {
      return get(Tuples.tupleN(Object.class));
    }

    /**
     * Automaps the columns of the {@link ResultSet} into the specified
     * classes. See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls1
     * @param cls2
     * @return
     */
    public <T1, T2> Observable.Operator<Tuple2<T1, T2>, R> getAs(Class<T1> cls1, Class<T2> cls2) {
      return get(Tuples.tuple(cls1, cls2));
    }

    /**
     * Automaps the columns of the {@link ResultSet} into the specified
     * classes. See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls1
     * @param cls2
     * @param cls3
     * @return
     */
    public <T1, T2, T3> Observable.Operator<Tuple3<T1, T2, T3>, R> getAs(Class<T1> cls1, Class<T2> cls2,
        Class<T3> cls3) {
      return get(Tuples.tuple(cls1, cls2, cls3));
    }

    /**
     * Automaps the columns of the {@link ResultSet} into the specified
     * classes. See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls1
     * @param cls2
     * @param cls3
     * @param cls4
     * @return
     */
    public <T1, T2, T3, T4> Observable.Operator<Tuple4<T1, T2, T3, T4>, R> getAs(Class<T1> cls1,
        Class<T2> cls2, Class<T3> cls3, Class<T4> cls4) {
      return get(Tuples.tuple(cls1, cls2, cls3, cls4));
    }

    /**
     * Automaps the columns of the {@link ResultSet} into the specified
     * classes. See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls1
     * @param cls2
     * @param cls3
     * @param cls4
     * @param cls5
     * @return
     */
    public <T1, T2, T3, T4, T5> Observable.Operator<Tuple5<T1, T2, T3, T4, T5>, R> getAs(Class<T1> cls1,
        Class<T2> cls2, Class<T3> cls3, Class<T4> cls4, Class<T5> cls5) {
      return get(Tuples.tuple(cls1, cls2, cls3, cls4, cls5));
    }

    /**
     * Automaps the columns of the {@link ResultSet} into the specified
     * classes. See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls1
     * @param cls2
     * @param cls3
     * @param cls4
     * @param cls5
     * @param cls6
     * @return
     */
    public <T1, T2, T3, T4, T5, T6> Observable.Operator<Tuple6<T1, T2, T3, T4, T5, T6>, R> getAs(
        Class<T1> cls1, Class<T2> cls2, Class<T3> cls3, Class<T4> cls4, Class<T5> cls5,
        Class<T6> cls6) {
      return get(Tuples.tuple(cls1, cls2, cls3, cls4, cls5, cls6));
    }

    /**
     * Automaps the columns of the {@link ResultSet} into the specified
     * classes. See {@link #autoMap(Class) autoMap()}.
     *
     * @param cls1
     * @param cls2
     * @param cls3
     * @param cls4
     * @param cls5
     * @param cls6
     * @param cls7
     * @return
     */
    public <T1, T2, T3, T4, T5, T6, T7> Observable.Operator<Tuple7<T1, T2, T3, T4, T5, T6, T7>, R> getAs(
        Class<T1> cls1, Class<T2> cls2, Class<T3> cls3, Class<T4> cls4, Class<T5> cls5,
        Class<T6> cls6, Class<T7> cls7) {
      return get(Tuples.tuple(cls1, cls2, cls3, cls4, cls5, cls6, cls7));
    }

  }

}
