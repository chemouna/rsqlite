package com.mounacheikhna.rxsqlite;

import java.util.List;
import rx.Observable;

/**
 * Created by cheikhnamouna on 10/18/15.
 */
//#FBN
final public class QueryUpdate implements Query {

  @Override public String sql() {
    return null;
  }

  @Override public List<String> names() {
    return null;
  }

  @Override public Observable<Parameter> parameters() {
    return null;
  }

  @Override public Observable<?> depends() {
    return null;
  }

  @Override public QueryContext context() {
    return null;
  }


}
