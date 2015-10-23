package com.mounacheikhna.rxsqlite;

import rx.Observable;

/**
 * The types of {@link Observable.Operator} that can be created by a select or update
 * query.
 */
enum OperatorType {
  /**
   * A type of operator that consumes an Observable<Object> to be used as
   * query parameters.
   */
  PARAMETER,

  /**
   * A type of operator that consumes an Observable<Object> representing
   * dependencies to be completed before the select or update query.
   */
  DEPENDENCY,

  PARAMETER_LIST;

}