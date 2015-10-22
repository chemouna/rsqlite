package com.mounacheikhna.rxsqlite;

import rx.Observable;

/**
 * Utility methods for RxJava.
 */
public final class RxUtil {

  private RxUtil() {
    // prevent instantiation
  }

  /**
   * Returns the concatenation of two {@link Observable}s but the first
   * sequence will be emitted in its entirety and ignored before o2 starts
   * emitting.
   *
   * @param <T>
   *            the generic type of the second observable
   * @param o1
   *            the sequence to ignore
   * @param o2
   *            the sequence to emit after o1 ignored
   * @return observable result of concatenating two observables, ignoring the
   *         first
   */
  @SuppressWarnings("unchecked")
  public static <T> Observable<T> concatButIgnoreFirstSequence(Observable<?> o1,
      Observable<T> o2) {
    return Observable.concat((Observable<T>) o1.ignoreElements(), o2);
  }
}
