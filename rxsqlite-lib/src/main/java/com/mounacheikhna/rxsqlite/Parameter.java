package com.mounacheikhna.rxsqlite;

import android.support.annotation.NonNull;
import rx.functions.Func1;

/**
 * Created by cheikhnamouna on 10/18/15.
 *
 * Encapsulates a query parameter.
 *
 */
final class Parameter {
  private final String name;
  /**
   * Actual query parameter value to be encapsulated.
   */
  private final Object value;

  /**
   * Constructor.
   *
   */
  Parameter(Object value) {
    this(null, value);
  }

  Parameter(String name, Object value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the parameter value.
   *
   * @return
   */
  Object value() {
    return value;
  }

  boolean hasName() {
    return name != null;
  }

  String name() {
    return name;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Parameter [value=");
    builder.append(value);
    builder.append("]");
    return builder.toString();
  }

  /**
   * A conversion function for use in Observable.map().
   */
  static final Func1<Object, Parameter> TO_PARAMETER = new Func1<Object, Parameter>() {

    @NonNull
    @Override
    public Parameter call(Object parameter) {
      Checks.checkFalse(parameter instanceof Parameter);
      return new Parameter(parameter);
    }
  };

}
