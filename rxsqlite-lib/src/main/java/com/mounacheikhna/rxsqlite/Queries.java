package com.mounacheikhna.rxsqlite;

import java.util.List;
import rx.Observable;

import static com.mounacheikhna.rxsqlite.RxUtil.concatButIgnoreFirstSequence;

public class Queries {

  /**
   * Returns query.getParameters() {@link Observable} but only after query
   * dependencies have been fully emitted (and ignored).
   *
   * @return query parameters
   */
  static Observable<Parameter> parametersAfterDependencies(Query query) {
    return concatButIgnoreFirstSequence(query.depends(), query.parameters());
  }


  /**
   * If the number of parameters in a query is >0 then group the parameters in
   * lists of that number in size but only after the dependencies have been
   * completed. If the number of parameteres is zero then return an observable
   * containing one item being an empty list.
   *
   * @param query
   * @return
   */
  static Observable<List<Parameter>> bufferedParameters(Query query) {
    int numParamsPerQuery = numParamsPerQuery(query);
    if (numParamsPerQuery > 0)
      // we don't check that parameters is empty after this because by
      // general design we want nothing to happen if a query is passed no
      // parameters when it expects them
      return parametersAfterDependencies(query).concatMap(FLATTEN_NAMED_MAPS)
          .buffer(numParamsPerQuery);
    else
      return singleIntegerAfterDependencies(query).map(TO_EMPTY_PARAMETER_LIST);
  }

  /**
   * Returns the number of parameters required to run this query once. Roughly
   * corresponds to the number of ? characters in the sql but have to watch
   * out for ? characters within quoted strings.
   *
   * @return number of parameters in query sql
   */
  static int numParamsPerQuery(Query query) {
    return Util.parametersCount(query);
  }


}
