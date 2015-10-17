package com.mounacheikhna.rxsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Observable;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * Created by cheikhnamouna on 10/17/15.
 */
//@RunWith(AndroidJUnit4.class)
public final class DatabaseTest {

   /*Database db() {
        return new Database(new TestSqliteHelper(InstrumentationRegistry.getContext()));
    }

    @Test
    public void testSimpleExample() {
        Observable<String> names = db().select("select name from person order by name")
                .getAs(String.class);
        List<String> list = names.toList().toBlocking().single();
        assertEquals(asList("JOHN", "KALIB", "REZA"), list);
    }

    public class TestSqliteHelper extends SQLiteOpenHelper {
        private static final String TABLE_PLAYER = "player";
        private static final String CREATE_PLAYER =
              "create table player (name varchar(50) primary key, score int not null,dob date, registered timestamp);";

        public TestSqliteHelper(Context context) {
            super(context, null, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
          db.execSQL("PRAGMA foreign_keys=ON");
          db.execSQL(CREATE_PLAYER);

          long johnId = db.insert(TABLE_PLAYER, null, player("JOHN", 19));
          long kalibId = db.insert(TABLE_PLAYER, null, player("KALIB", 32));
          long rezaId = db.insert(TABLE_PLAYER, null, player("REZA", 27));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

      ContentValues player(String user, int score) {
        ContentValues values = new ContentValues();
        values.put("name", user);
        values.put("score", score);
        return values;
      }

    }
    */

}
