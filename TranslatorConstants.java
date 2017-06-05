package gMigrate;

public class TranslatorConstants {
public static final String SPACE = " ";

public static final String SELECT_INTO = "INTO";
public static final String SELECT = "SELECT";
public static final String SELECT_FROM = "FROM";

public static final String ROW_INITIALISE_SELECT_1 = "Row row = session.execute(\"";
public static final String ROW_INITIALISE_SELECT_2 = "\").one();";
public static final String SESSION_EXECUTE = "session.execute(\"";
public static final String ROW_STRING_1 = "row.get";
public static final String ROW_STRING_1_a = "(\"";
public static final String END = "\");";
public static final String PREPARED_STATEMENT_1 = "PreparedStatement prepared = session.prepare(\"";
public static final String BOUND_STATEMENT = "BoundStatement bound = prepared.bind(";
public static final String BOUND_SESSION_SELECT = "Row row = session.execute(bound).one();";
public static final String BOUND_SESSION_UPDATE = "session.execute(bound);";
public static final String END_BRACKET = ");";
public static final String EQUAL = "=";

public static final String COMMA = ",";

public static final String SELECT_WHERE = "WHERE";
}
