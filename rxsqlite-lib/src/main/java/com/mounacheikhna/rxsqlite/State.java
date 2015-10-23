package com.mounacheikhna.rxsqlite;

import android.database.Cursor;
import java.util.concurrent.atomic.AtomicBoolean;

class State {
  /*volatile Connection con;
  volatile PreparedStatement ps;*/
  volatile Cursor rs;
  final AtomicBoolean closed = new AtomicBoolean(false);
}