//package javab.sql;
//
//import java.sql.*;
//import java.util.*;
//
//
///**
// * This class wraps around a {@link PreparedStatement} and allows the
// * programmer to set parameters by name instead
// * of by index.  This eliminates any confusion as to which parameter index
// * represents what.  This also means that
// * rearranging the SQL statement or adding a parameter doesn't involve
// * renumbering your indices.
// * Code such as this:
// * <p>
// * Connection con=getConnection();
// * String query="select * from my_table where name=? or address=?";
// * PreparedStatement p=con.prepareStatement(query);
// * p.setString(1, "bob");
// * p.setString(2, "123 terrace ct");
// * ResultSet rs=p.executeQuery();
// * <p>
// * can be replaced with:
// * <p>
// * Connection con=getConnection();
// * String query="select * from my_table where name=:name or
// * address=:address";
// * NamedParameterStatement p=new NamedParameterStatement(con, query);
// * p.setString("name", "bob");
// * p.setString("address", "123 terrace ct");
// * ResultSet rs=p.executeQuery();
// *
// * @author adam_crume
// */
//@SuppressWarnings({"unused", "ForLoopReplaceableByForEach"})
//public final class NamedParameterStatement {
//
//    /**
//     * The statement this object is wrapping.
//     */
//    private final PreparedStatement statement;
//
//    /**
//     * Maps parameter names to arrays of ints which are the parameter indices.
//     */
//    private final Map<String, Object> indexMap;
//
//    /**
//     * Creates a NamedParameterStatement.  Wraps a call to
//     * c.{@link Connection#prepareStatement(java.lang.String)
//     * prepareStatement}.
//     *
//     * @param connection the database connection
//     * @param query      the parameterized query
//     * @throws SQLException if the statement could not be created
//     */
//    public NamedParameterStatement(final Connection connection, final String query) throws
//            SQLException {
//        indexMap = new HashMap<>();
//        final String parsedQuery = parse(query, indexMap);
//        statement = connection.prepareStatement(parsedQuery);
//    }
//
//    /**
//     * Parses a query with named parameters.  The parameter-index mappings are
//     * put into the map, and the
//     * parsed query is returned.  DO NOT CALL FROM CLIENT CODE.  This
//     * method is non-private so JUnit code can
//     * test it.
//     *
//     * @param query    query to parse
//     * @param paramMap map to hold parameter-index mappings
//     * @return the parsed query
//     */
//    private static String parse(final String query, final Map<String, Object> paramMap) {
//        // I was originally using regular expressions, but they didn't work well for ignoring
//        // parameter-like strings inside quotes.
//        final int length = query.length();
//        final StringBuilder parsedQuery = new StringBuilder(length);
//        boolean inSingleQuote = false;
//        boolean inDoubleQuote = false;
//        int index = 1;
//
//        for (int i = 0; i < length; i++) {
//            char c = query.charAt(i);
//            if (inSingleQuote) {
//                if (c == '\'') {
//                    inSingleQuote = false;
//                }
//            } else if (inDoubleQuote) {
//                if (c == '"') {
//                    inDoubleQuote = false;
//                }
//            } else {
//                if (c == '\'') {
//                    inSingleQuote = true;
//                } else if (c == '"') {
//                    inDoubleQuote = true;
//                } else if (c == ':' && i + 1 < length &&
//                        Character.isJavaIdentifierStart(query.charAt(i + 1))) {
//                    int j = i + 2;
//                    while (j < length && Character.isJavaIdentifierPart(query.charAt(j))) {
//                        j++;
//                    }
//                    final String name = query.substring(i + 1, j);
//                    c = '?'; // replace the parameter with a question mark
//                    i += name.length(); // skip past the end if the parameter
//
//                    List<Integer> indexList = (List<Integer>) paramMap.get(name);
//                    if (indexList == null) {
//                        indexList = new LinkedList<>();
//                        paramMap.put(name, indexList);
//                    }
//                    indexList.add(index);
//
//                    index++;
//                }
//            }
//            parsedQuery.append(c);
//        }
//
//        // replace the lists of Integer objects with arrays of ints
//        for (final Iterator itr = paramMap.entrySet().iterator(); itr.hasNext(); ) {
//            final Map.Entry entry = (Map.Entry) itr.next();
//            final List list = (List) entry.getValue();
//            final int[] indexes = new int[list.size()];
//            int i = 0;
//            for (final Iterator itr2 = list.iterator(); itr2.hasNext(); ) {
//                Integer x = (Integer) itr2.next();
//                indexes[i++] = x;
//            }
//            entry.setValue(indexes);
//        }
//
//        return parsedQuery.toString();
//    }
//
//    /**
//     * Returns the indexes for a parameter.
//     *
//     * @param name parameter name
//     * @return parameter indexes
//     * @throws IllegalArgumentException if the parameter does not exist
//     */
//    private int[] getIndexes(final String name) {
//        final int[] indexes = (int[]) indexMap.get(name);
//        if (indexes == null) {
//            throw new IllegalArgumentException("Parameter not found: " + name);
//        }
//        return indexes;
//    }
//
//    /**
//     * Sets a parameter.
//     *
//     * @param name  parameter name
//     * @param value parameter value
//     * @throws SQLException             if an error occurred
//     * @throws IllegalArgumentException if the parameter does not exist
//     * @see PreparedStatement#setObject(int, java.lang.Object)
//     */
//    public void setObject(final String name, final Object value) throws SQLException {
//        final int[] indexes = getIndexes(name);
//        for (int i = 0; i < indexes.length; i++) {
//            statement.setObject(indexes[i], value);
//        }
//    }
//
//    /**
//     * Sets a parameter.
//     *
//     * @param name  parameter name
//     * @param value parameter value
//     * @throws SQLException             if an error occurred
//     * @throws IllegalArgumentException if the parameter does not exist
//     * @see PreparedStatement#setString(int, java.lang.String)
//     */
//    public void setString(final String name, final String value) throws SQLException {
//        final int[] indexes = getIndexes(name);
//        for (int i = 0; i < indexes.length; i++) {
//            statement.setString(indexes[i], value);
//        }
//    }
//
//    /**
//     * Sets a parameter.
//     *
//     * @param name  parameter name
//     * @param value parameter value
//     * @throws SQLException             if an error occurred
//     * @throws IllegalArgumentException if the parameter does not exist
//     * @see PreparedStatement#setInt(int, int)
//     */
//    public void setInt(final String name, final int value) throws SQLException {
//        final int[] indexes = getIndexes(name);
//        for (int i = 0; i < indexes.length; i++) {
//            statement.setInt(indexes[i], value);
//        }
//    }
//
//    /**
//     * Sets a parameter.
//     *
//     * @param name  parameter name
//     * @param value parameter value
//     * @throws SQLException             if an error occurred
//     * @throws IllegalArgumentException if the parameter does not exist
//     * @see PreparedStatement#setInt(int, int)
//     */
//    public void setLong(final String name, final long value) throws SQLException {
//        final int[] indexes = getIndexes(name);
//        for (int i = 0; i < indexes.length; i++) {
//            statement.setLong(indexes[i], value);
//        }
//    }
//
//    /**
//     * Sets a parameter.
//     *
//     * @param name  parameter name
//     * @param value parameter value
//     * @throws SQLException             if an error occurred
//     * @throws IllegalArgumentException if the parameter does not exist
//     * @see PreparedStatement#setTimestamp(int, java.sql.Timestamp)
//     */
//    public void setTimestamp(final String name, final Timestamp value) throws SQLException {
//        final int[] indexes = getIndexes(name);
//        for (int i = 0; i < indexes.length; i++) {
//            statement.setTimestamp(indexes[i], value);
//        }
//    }
//
//    /**
//     * Executes the statement.
//     *
//     * @return true if the first result is a {@link ResultSet}
//     * @throws SQLException if an error occurred
//     * @see PreparedStatement#execute()
//     */
//    public boolean execute() throws SQLException {
//        return statement.execute();
//    }
//
//    /**
//     * Executes the statement, which must be a query.
//     *
//     * @return the query results
//     * @throws SQLException if an error occurred
//     * @see PreparedStatement#executeQuery()
//     */
//    public ResultSet executeQuery() throws SQLException {
//        return statement.executeQuery();
//    }
//
//    /**
//     * Executes the statement, which must be an SQL INSERT, UPDATE or DELETE
//     * statement;
//     * or an SQL statement that returns nothing, such as a DDL statement.
//     *
//     * @return number of rows affected
//     * @throws SQLException if an error occurred
//     * @see PreparedStatement#executeUpdate()
//     */
//    public int executeUpdate() throws SQLException {
//        return statement.executeUpdate();
//    }
//
//    /**
//     * Closes the statement.
//     *
//     * @throws SQLException if an error occurred
//     * @see Statement#close()
//     */
//    public void close() throws SQLException {
//        statement.close();
//    }
//
//    /**
//     * Adds the current set of parameters as a batch entry.
//     *
//     * @throws SQLException if something went wrong
//     */
//    public void addBatch() throws SQLException {
//        statement.addBatch();
//    }
//
//    /**
//     * Executes all of the batched statements.
//     * <p>
//     * See {@link Statement#executeBatch()} for details.
//     *
//     * @return update counts for each statement
//     * @throws SQLException if something went wrong
//     */
//    public int[] executeBatch() throws SQLException {
//        return statement.executeBatch();
//    }
//
//}
