package com.mounacheikhna.rxsqlite;

/**
 * Created by cheikhnamouna on 10/18/15.
 */
public class Checks {

  private Checks() {}

  /**
   * If and only if parameter is false throw a {@link RuntimeException}.
   *
   * @param b
   */
  static void checkTrue(boolean b) {
    if (!b)
      throw new RuntimeException("check failed");
  }

  /**
   * If and only if parameter is true throw a {@link RuntimeException}.
   *
   * @param b
   */
  static void checkFalse(boolean b) {
    checkTrue(!b);
  }


}
