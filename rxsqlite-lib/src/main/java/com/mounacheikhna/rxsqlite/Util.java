package com.mounacheikhna.rxsqlite;

import android.database.Cursor;
import com.mounacheikhna.rxsqlite.annotations.IsQuery;
import com.mounacheikhna.rxsqlite.exceptions.SQLRuntimeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import rx.functions.Func1;

/**
 * Created by cheikhnamouna on 10/19/15.
 */
public final class Util {

  /**
   * Converts from java.sql Types to common java types like java.util.Date and
   * numeric types. See {@link Builder#autoMap(Class)}.
   *
   * @param o
   * @param cls
   * @return
   */
  public static Object autoMap(Object o, Class<?> cls) {
    if (o == null)
      return o;
    else if (cls.isAssignableFrom(o.getClass())) {
      return o;
    } else {
      if (o instanceof java.sql.Date) {
        java.sql.Date d = (java.sql.Date) o;
        if (cls.isAssignableFrom(Long.class))
          return d.getTime();
        else if (cls.isAssignableFrom(BigInteger.class))
          return BigInteger.valueOf(d.getTime());
        else
          return o;
      } else if (o instanceof java.sql.Timestamp) {
        Timestamp t = (java.sql.Timestamp) o;
        if (cls.isAssignableFrom(Long.class))
          return t.getTime();
        else if (cls.isAssignableFrom(BigInteger.class))
          return BigInteger.valueOf(t.getTime());
        else
          return o;
      } else if (o instanceof java.sql.Time) {
        Time t = (java.sql.Time) o;
        if (cls.isAssignableFrom(Long.class))
          return t.getTime();
        else if (cls.isAssignableFrom(BigInteger.class))
          return BigInteger.valueOf(t.getTime());
        else
          return o;
      } else if (o instanceof Blob && cls.isAssignableFrom(byte[].class)) {
        return toBytes((Blob) o);
      } else if (o instanceof Clob && cls.isAssignableFrom(String.class)) {
        return toString((Clob) o);
      } else if (o instanceof BigInteger && cls.isAssignableFrom(Long.class)) {
        return ((BigInteger) o).longValue();
      } else if (o instanceof BigInteger && cls.isAssignableFrom(Integer.class)) {
        return ((BigInteger) o).intValue();
      } else if (o instanceof BigInteger && cls.isAssignableFrom(Double.class)) {
        return ((BigInteger) o).doubleValue();
      } else if (o instanceof BigInteger && cls.isAssignableFrom(Float.class)) {
        return ((BigInteger) o).floatValue();
      } else if (o instanceof BigInteger && cls.isAssignableFrom(Short.class)) {
        return ((BigInteger) o).shortValue();
      } else if (o instanceof BigInteger && cls.isAssignableFrom(BigDecimal.class)) {
        return new BigDecimal((BigInteger) o);
      } else if (o instanceof BigDecimal && cls.isAssignableFrom(Double.class)) {
        return ((BigDecimal) o).doubleValue();
      } else if (o instanceof BigDecimal && cls.isAssignableFrom(Integer.class)) {
        return ((BigDecimal) o).toBigInteger().intValue();
      } else if (o instanceof BigDecimal && cls.isAssignableFrom(Float.class)) {
        return ((BigDecimal) o).floatValue();
      } else if (o instanceof BigDecimal && cls.isAssignableFrom(Short.class)) {
        return ((BigDecimal) o).toBigInteger().shortValue();
      } else if (o instanceof BigDecimal && cls.isAssignableFrom(Long.class)) {
        return ((BigDecimal) o).toBigInteger().longValue();
      } else if (o instanceof BigDecimal && cls.isAssignableFrom(BigInteger.class)) {
        return ((BigDecimal) o).toBigInteger();
      } else if ((o instanceof Short || o instanceof Integer || o instanceof Long)
          && cls.isAssignableFrom(BigInteger.class)) {
        return new BigInteger(o.toString());
      } else if (o instanceof Number && cls.isAssignableFrom(BigDecimal.class)) {
        return new BigDecimal(o.toString());
      } else if (o instanceof Number && cls.isAssignableFrom(Short.class))
        return ((Number) o).shortValue();
      else if (o instanceof Number && cls.isAssignableFrom(Integer.class))
        return ((Number) o).intValue();
      else if (o instanceof Number && cls.isAssignableFrom(Integer.class))
        return ((Number) o).intValue();
      else if (o instanceof Number && cls.isAssignableFrom(Long.class))
        return ((Number) o).longValue();
      else if (o instanceof Number && cls.isAssignableFrom(Float.class))
        return ((Number) o).floatValue();
      else if (o instanceof Number && cls.isAssignableFrom(Double.class))
        return ((Number) o).doubleValue();
      else
        return o;
    }
  }

  public static <T> Object mapObject(final Cursor rs, Class<T> cls, int i) {
    return autoMap(getObject(rs, cls, i), cls);
  }

  private static <T> Object getObject(final Cursor rs, Class<T> cls, int i) {
    /*try {
      if (rs.getObject(i) == null) {
        return null;
      }

      final int type = rs.getMetaData().getColumnType(i);
      // TODO java.util.Calendar support
      // TODO XMLGregorian Calendar support
      if (type == Types.DATE)
        return rs.getDate(i, Calendar.getInstance());
      else if (type == Types.TIME)
        return rs.getTime(i, Calendar.getInstance());
      else if (type == Types.TIMESTAMP)
        return rs.getTimestamp(i, Calendar.getInstance());
      else if (type == Types.CLOB && cls.equals(String.class)) {
        return toString(rs.getClob(i));
      } else if (type == Types.CLOB && Reader.class.isAssignableFrom(cls)) {
        Clob c = rs.getClob(i);
        Reader r = c.getCharacterStream();
        return createFreeOnCloseReader(c, r);
      } else if (type == Types.BLOB && cls.equals(byte[].class)) {
        return toBytes(rs.getBlob(i));
      } else if (type == Types.BLOB && InputStream.class.isAssignableFrom(cls)) {
        final Blob b = rs.getBlob(i);
        final InputStream is = rs.getBlob(i).getBinaryStream();
        return createFreeOnCloseInputStream(b, is);
      } else
        return rs.getObject(i);
    } catch (SQLException e) {
      throw new SQLRuntimeException(e);
    }*/
    //temp
    return null;
  }

  /**
   * Returns the empty list whenever called.
   */
  static Func1<Integer, List<Parameter>> TO_EMPTY_PARAMETER_LIST = new Func1<Integer, List<Parameter>>() {
    @Override
    public List<Parameter> call(Integer n) {
      return Collections.emptyList();
    };
  };

  /**
   * Returns the String of a {@link Clob} and frees the clob resource.
   *
   * @param c
   * @return
   */
  private static String toString(Clob c) {
    try {
      Reader reader = c.getCharacterStream();
      String result = IOUtils.toString(reader);
      reader.close();
      c.free();
      return result;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new SQLRuntimeException(e);
    }
  }

  /**
   * Automatically frees the blob (<code>blob.free()</code>) once the blob
   * {@link InputStream} is closed.
   *
   * @param blob
   * @param is
   * @return
   */
  private static InputStream createFreeOnCloseInputStream(final Blob blob, final InputStream is) {
    return new InputStream() {

      @Override
      public int read() throws IOException {
        return is.read();
      }

      @Override
      public void close() throws IOException {
        try {
          is.close();
        } finally {
          try {
            blob.free();
          } catch (SQLException e) {
            //log.debug(e.getMessage());
          }
        }
      }
    };
  }

  /**
   * Returns the bytes of a {@link Blob} and frees the blob resource.
   *
   * @param b
   *            blob
   * @return
   */
  private static byte[] toBytes(Blob b) {
    try {
      InputStream is = b.getBinaryStream();
      byte[] result = IOUtils.toByteArray(is);
      is.close();
      b.free();
      return result;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new SQLRuntimeException(e);
    }

  }

  /**
   * Automatically frees the clob (<code>Clob.free()</code>) once the clob
   * Reader is closed.
   *
   * @param clob
   * @param reader
   * @return
   */
  private static Reader createFreeOnCloseReader(final Clob clob, final Reader reader) {
    return new Reader() {

      @Override
      public void close() throws IOException {
        try {
          reader.close();
        } finally {
          try {
            clob.free();
          } catch (SQLException e) {
            //log.debug(e.getMessage());
          }
        }
      }

      @Override
      public int read(char[] cbuf, int off, int len) throws IOException {
        return reader.read(cbuf, off, len);
      }
    };
  }

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

  /**
   * Count the number of sqlite parameters in a sql statement.
   *
   * @param query
   *            .sql()
   * @return
   */
  static int parametersCount(Query query) {
    if (query.names().isEmpty())
      return countQuestionMarkParameters(query.sql());
    else
      return query.names().size();
  }

  // Visible for testing
  static int countQuestionMarkParameters(String sql) {
    // was originally using regular expressions, but they didn't work well
    // for ignoring parameter-like strings inside quotes.
    int count = 0;
    int length = sql.length();
    boolean inSingleQuote = false;
    boolean inDoubleQuote = false;
    for (int i = 0; i < length; i++) {
      char c = sql.charAt(i);
      if (inSingleQuote) {
        if (c == '\'') {
          inSingleQuote = false;
        }
      } else if (inDoubleQuote) {
        if (c == '"') {
          inDoubleQuote = false;
        }
      } else {
        if (c == '\'') {
          inSingleQuote = true;
        } else if (c == '"') {
          inDoubleQuote = true;
        } else if (c == '?') {
          count++;
        }
      }
    }
    return count;
  }


}
