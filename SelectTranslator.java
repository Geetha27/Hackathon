package gMigrate;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;

public class SelectTranslator {
	public static String NEWLINE;

	static {
		NEWLINE = System.lineSeparator();
	}

	public String translate(String statement) throws JSQLParserException {
		StringBuilder updatedStatement = new StringBuilder();

		if (statement.indexOf(TranslatorConstants.SELECT_INTO) > -1) {
			String tableName = getTableName(statement);
			String extractedVariables = statement.substring(statement.indexOf(TranslatorConstants.SELECT_INTO) + 5,
					statement.indexOf(TranslatorConstants.SELECT_FROM) - 1);
			String[] variableList = extractedVariables.split(TranslatorConstants.COMMA);
			StringBuilder remove = new StringBuilder(TranslatorConstants.SPACE).append(TranslatorConstants.SELECT_INTO)
					.append(TranslatorConstants.SPACE).append(extractedVariables);
			String extractedDML = statement.replace(remove.toString(), "");
			String[] columnList = fetchColumnList(statement, tableName, variableList.length);
			updatedStatement.append(prepareSelectStatement(extractedDML));
			for (int index = 0; index < variableList.length; index++) {
				Util util = new Util();
				/*
				 * To be uncommented and used instead of the following statement
				 * for actual run String type = util.getColumnType(tableName,
				 * columnList[index]);
				 */
				String type = util.getColumnTypeLocal(tableName.trim(), columnList[index].trim());
				updatedStatement.append(variableList[index].trim()).append(TranslatorConstants.SPACE)
						.append(TranslatorConstants.EQUAL).append(TranslatorConstants.SPACE)
						.append(TranslatorConstants.ROW_STRING_1).append(type)
						.append(TranslatorConstants.ROW_STRING_1_a).append(variableList[index].trim())
						.append(TranslatorConstants.END).append(NEWLINE);
			}
		} else {
			updatedStatement.append(prepareSelectStatement(statement));
		}

		return updatedStatement.toString();
	}

	private String prepareSelectStatement(String statement) {
		StringBuilder updated = new StringBuilder();
		if (statement.indexOf(":") > -1) {
			StringBuilder updatedDML = new StringBuilder();
			List<String> paramList = new ArrayList<>();
			String[] words = statement.split(" ");
			for (int index = 0; index < words.length; index++) {
				if (words[index].contains(":")) {
					paramList.add(words[index].replaceAll(":", "").replaceAll("\\)", "").replaceAll(";", ""));
					words[index] = "?";
				}
				updatedDML.append(words[index]).append(" ");
			}
			updated.append(TranslatorConstants.PREPARED_STATEMENT_1).append(updatedDML).append(TranslatorConstants.END)
					.append(NEWLINE);
			updated.append(TranslatorConstants.BOUND_STATEMENT);
			for (String param : paramList) {
				updated.append(param).append(",");
			}
			updated.deleteCharAt(updated.length() - 1).append(TranslatorConstants.END_BRACKET).append(NEWLINE);
			updated.append(TranslatorConstants.BOUND_SESSION_SELECT).append(NEWLINE);
		} else {
			updated = new StringBuilder().append(TranslatorConstants.ROW_INITIALISE_SELECT_1).append(statement)
					.append(TranslatorConstants.ROW_INITIALISE_SELECT_2).append(NEWLINE).append(NEWLINE);
		}
		return updated.toString();
	}

	private String[] fetchColumnList(String statement, String table, int length) {
		if (statement.indexOf("*") == -1) {
			String extractedColumns = statement.substring(statement.indexOf(TranslatorConstants.SELECT) + 7,
					statement.indexOf(TranslatorConstants.SELECT_FROM) - 1);
			return extractedColumns.split(TranslatorConstants.COMMA);
		} else {
			Util util = new Util();
			/*
			 * To be uncommented and used instead of the following statement for
			 * actual run return util.getColumnList(table);
			 */
			return util.getColumnListLocal(table, length);
		}
	}

	private String getTableName(String statement) {
		return statement.substring(statement.indexOf(TranslatorConstants.SELECT_FROM) + 5, statement.length())
				.split(" ")[0];
	}

	public static void main(String[] args) {
		String statement = "SELECT * INTO jvar1, jvar2 FROM gtable WHERE h= (select id from tablemg where id = :jvar and name = :dsf);";

		try {
			System.out.println("--------------" + statement + "\n");
			System.out.println(new SelectTranslator().translate(statement));

			statement = "SELECT variable1,variable2, variable3 FROM gtable WHERE h=9";
			System.out.println("--------------" + statement + "\n");
			System.out.println(new SelectTranslator().translate(statement));
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
