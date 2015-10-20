package com.mounacheikhna.rxsqlite;

import com.mounacheikhna.rxsqlite.annotations.IsQuery;

/**
 * Created by cheikhnamouna on 10/19/15.
 */
public final class Util {

  static <T> void setSqlFromQueryAnnotation(Class<T> cls, QueryBuilder builder) {
    if (builder.sql() == null) {
      IsQuery query = cls.getAnnotation(IsQuery.class);
      if (query != null && query.value() != null) {
        String sql = query.value();
        builder.setSql(sql);
      } else
        throw new RuntimeException("Class " + cls
            + " must be annotated with @Query(sql) or sql must be specified to the builder.select() call");
    }
  }


}
