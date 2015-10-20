package com.mounacheikhna.rxsqlite.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by cheikhnamouna on 10/19/15.
 */
@Target({ TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsQuery {
  String value();
}
