package com.mounacheikhna.rxsqlite;

import android.database.Cursor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cheikhnamouna on 10/19/15.
 */
public class CursorCache {

  final Cursor cursor;
  final Map<String, Integer> colIndexes;

  public CursorCache(Cursor cursor) {
    this.cursor = cursor;
    this.colIndexes = collectColIndexes(cursor);
  }

  private static Map<String, Integer> collectColIndexes(Cursor cursor) {
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    try {
      String[] columns = cursor.getColumnNames();
      for (int i = 1; i <= columns.length; i++) {
        map.put(cursor.getColumnName(i).toUpperCase(), i);
      }
      return map;
    }
    finally {
      cursor.close(); //not sure
    }
  }

}
