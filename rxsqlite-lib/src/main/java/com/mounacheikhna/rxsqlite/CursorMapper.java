package com.mounacheikhna.rxsqlite;

import android.database.Cursor;

/**
 * Created by cheikhnamouna on 10/19/15.
 */
public interface CursorMapper<T> {

  T call(Cursor cursor);
}
