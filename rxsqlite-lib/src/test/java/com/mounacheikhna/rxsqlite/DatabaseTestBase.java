package com.mounacheikhna.rxsqlite;

import org.junit.Test;

import java.util.List;

import rx.Observable;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * Created by cheikhnamouna on 10/17/15.
 */
public class DatabaseTestBase {

    //How do i have a db creation here ?
   /* Database db() {
        return DatabaseCreator.db().asynchronous();
    }

    @Test
    public void testSimpleExample() {
        Observable<String> names = db().select("select name from person order by name")
                .getAs(String.class);
        // convert the names to a list for unit test
        List<String> list = names.toList().toBlocking().single();
        assertEquals(asList("FRED", "JOSEPH", "MARMADUKE"), list);
    }*/

}
