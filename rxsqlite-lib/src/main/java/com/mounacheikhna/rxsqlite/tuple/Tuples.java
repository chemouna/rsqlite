package com.mounacheikhna.rxsqlite.tuple;

import android.database.Cursor;
import com.mounacheikhna.rxsqlite.CursorMapper;
import java.util.ArrayList;
import java.util.List;

import static com.mounacheikhna.rxsqlite.Util.mapObject;

/**
 * Utility methods for tuples.
 */
public final class Tuples {

  /**
   * Private constructor to prevent instantiation.
   */
  private Tuples() {
    // prevent instantiation.
  }

  public static <T> CursorMapper<T> single(final Class<T> cls) {
    return new CursorMapper<T>() {

      @SuppressWarnings("unchecked")
      @Override
      public T call(Cursor rs) {
        return (T) mapObject(rs, cls, 1);
      }

    };
  }

  public static <T1, T2> CursorMapper<Tuple2<T1, T2>> tuple(final Class<T1> cls1,
      final Class<T2> cls2) {
    return new CursorMapper<Tuple2<T1, T2>>() {

      @SuppressWarnings("unchecked")
      @Override
      public Tuple2<T1, T2> call(Cursor rs) {
        return new Tuple2<T1, T2>((T1) mapObject(rs, cls1, 1), (T2) mapObject(rs, cls2, 2));
      }
    };
  }

  public static <T1, T2, T3> CursorMapper<Tuple3<T1, T2, T3>> tuple(final Class<T1> cls1,
      final Class<T2> cls2, final Class<T3> cls3) {
    return new CursorMapper<Tuple3<T1, T2, T3>>() {
      @SuppressWarnings("unchecked")
      @Override
      public Tuple3<T1, T2, T3> call(Cursor rs) {
        return new Tuple3<T1, T2, T3>((T1) mapObject(rs, cls1, 1),
            (T2) mapObject(rs, cls2, 2), (T3) mapObject(rs, cls3, 3));
      }
    };
  }

  public static <T1, T2, T3, T4> CursorMapper<Tuple4<T1, T2, T3, T4>> tuple(
      final Class<T1> cls1, final Class<T2> cls2, final Class<T3> cls3,
      final Class<T4> cls4) {
    return new CursorMapper<Tuple4<T1, T2, T3, T4>>() {
      @SuppressWarnings("unchecked")
      @Override
      public Tuple4<T1, T2, T3, T4> call(Cursor rs) {
        return new Tuple4<T1, T2, T3, T4>((T1) mapObject(rs, cls1, 1),
            (T2) mapObject(rs, cls2, 2), (T3) mapObject(rs, cls3, 3),
            (T4) mapObject(rs, cls4, 4));
      }
    };
  }

  public static <T1, T2, T3, T4, T5> CursorMapper<Tuple5<T1, T2, T3, T4, T5>> tuple(
      final Class<T1> cls1, final Class<T2> cls2, final Class<T3> cls3, final Class<T4> cls4,
      final Class<T5> cls5) {
    return new CursorMapper<Tuple5<T1, T2, T3, T4, T5>>() {
      @SuppressWarnings("unchecked")
      @Override
      public Tuple5<T1, T2, T3, T4, T5> call(Cursor rs) {
        return new Tuple5<T1, T2, T3, T4, T5>((T1) mapObject(rs, cls1, 1),
            (T2) mapObject(rs, cls2, 2), (T3) mapObject(rs, cls3, 3),
            (T4) mapObject(rs, cls4, 4), (T5) mapObject(rs, cls5, 5));
      }
    };
  }

  public static <T1, T2, T3, T4, T5, T6> CursorMapper<Tuple6<T1, T2, T3, T4, T5, T6>> tuple(
      final Class<T1> cls1, final Class<T2> cls2, final Class<T3> cls3, final Class<T4> cls4,
      final Class<T5> cls5, final Class<T6> cls6) {

    return new CursorMapper<Tuple6<T1, T2, T3, T4, T5, T6>>() {
      @SuppressWarnings("unchecked")
      @Override
      public Tuple6<T1, T2, T3, T4, T5, T6> call(Cursor rs) {
        return new Tuple6<T1, T2, T3, T4, T5, T6>((T1) mapObject(rs, cls1, 1),
            (T2) mapObject(rs, cls2, 2), (T3) mapObject(rs, cls3, 3),
            (T4) mapObject(rs, cls4, 4), (T5) mapObject(rs, cls5, 5),
            (T6) mapObject(rs, cls6, 6));
      }
    };
  }

  public static <T1, T2, T3, T4, T5, T6, T7> CursorMapper<Tuple7<T1, T2, T3, T4, T5, T6, T7>> tuple(
      final Class<T1> cls1, final Class<T2> cls2, final Class<T3> cls3, final Class<T4> cls4,
      final Class<T5> cls5, final Class<T6> cls6, final Class<T7> cls7) {

    return new CursorMapper<Tuple7<T1, T2, T3, T4, T5, T6, T7>>() {
      @SuppressWarnings("unchecked")
      @Override
      public Tuple7<T1, T2, T3, T4, T5, T6, T7> call(Cursor rs) {
        return new Tuple7<T1, T2, T3, T4, T5, T6, T7>((T1) mapObject(rs, cls1, 1),
            (T2) mapObject(rs, cls2, 2), (T3) mapObject(rs, cls3, 3),
            (T4) mapObject(rs, cls4, 4), (T5) mapObject(rs, cls5, 5),
            (T6) mapObject(rs, cls6, 6), (T7) mapObject(rs, cls7, 7));
      }
    };
  }

  public static <T> CursorMapper<TupleN<T>> tupleN(final Class<T> cls) {
    return new CursorMapper<TupleN<T>>() {
      @Override
      public TupleN<T> call(Cursor rs) {
        return toTupleN(cls, rs);
      }
    };
  }

  @SuppressWarnings("unchecked")
  private static <T> TupleN<T> toTupleN(final Class<T> cls, Cursor rs) {
    try {
      //int n = rs.getMetaData().getColumnCount();
      //temp
      int n = 0;
      //temp

      List<T> list = new ArrayList<T>();
      for (int i = 1; i <= n; i++) {
        list.add((T) mapObject(rs, cls, i));
      }
      return new TupleN<T>(list);
    }
    catch (Exception e) {}
    /*catch (SQLException e) {
      throw new SQLRuntimeException(e);
    }*/
    return null;//temp
  }

}
