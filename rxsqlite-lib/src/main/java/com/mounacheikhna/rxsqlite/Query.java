package com.mounacheikhna.rxsqlite;

import java.util.List;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by cheikhnamouna on 10/18/15.
 */
public interface Query {

  /**
   * Returns the sql statement for this query following sqlite format (? for
   * parameters for instance).
   *
   * @return sqlite sql
   */
  String sql();

  /**
   * Returns the list of names corresponding positionally to the ? characters
   * in the sql. If names were not used then returns an empty list.
   *
   * @return ist of names corresponding positionally to the ? characters in
   *         the sql
   */
  List<String> names();

  /**
   * Returns the parameters for the query in order of appearance as ? markers
   * in the sql. May emit more than the number of parameters in one run of the
   * query in which case the query would be run multiple times.
   *
   * @return
   */
  Observable<Parameter> parameters();

  /**
   * Returns the Observables that have to complete before this query is
   * started.
   *
   * @return
   */
  Observable<?> depends();

  /**
   * Returns the query context including {@link ConnectionProvider} and
   * {@link Scheduler}.
   *
   * @return
   */
  QueryContext context();


}
