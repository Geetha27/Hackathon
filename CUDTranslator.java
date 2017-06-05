package gMigrate;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;

public class CUDTranslator {
	public static String NEWLINE;

	static {
		NEWLINE = System.lineSeparator();
	}

	public String translate(String statement) {
		StringBuilder updated = new StringBuilder();
		if (statement.indexOf(":") > -1) {
			StringBuilder updatedDML = new StringBuilder();
			List<String> paramList = new ArrayList<>();
			String[] words = statement.split(" ");
			for (int index = 0; index < words.length; index++) {
				if (words[index].contains(":")) {
					String word = words[index];
					StringBuilder wordUpdated = new StringBuilder();
					if (word.contains("(")) {
						wordUpdated.append("(");
					}
					if (word.contains("=")) {
						wordUpdated.append("=");
					}
					paramList.add(words[index].replaceAll(":", "").replaceAll("\\(", "").replaceAll("\\)", "")
							.replaceAll(",", "").replaceAll(";", ""));
					wordUpdated.append("?");
					if (word.contains(",")) {
						wordUpdated.append(",");
					}
					if (word.contains(")")) {
						wordUpdated.append(")");
					}
					updatedDML.append(wordUpdated).append(" ");
				} else {
					updatedDML.append(words[index]).append(" ");
				}
			}
			updated.append(TranslatorConstants.PREPARED_STATEMENT_1).append(updatedDML).append(TranslatorConstants.END)
					.append(NEWLINE);
			updated.append(TranslatorConstants.BOUND_STATEMENT);
			for (String param : paramList) {
				updated.append(param).append(",");
			}
			updated.deleteCharAt(updated.length() - 1).append(TranslatorConstants.END_BRACKET).append(NEWLINE);
			updated.append(TranslatorConstants.BOUND_SESSION_UPDATE).append(NEWLINE);
		} else {
			updated = new StringBuilder().append(TranslatorConstants.SESSION_EXECUTE).append(statement)
					.append(TranslatorConstants.END).append(NEWLINE).append(NEWLINE);
		}
		return updated.toString();
	}

	public static void main(String[] args) {
		//String statement = "INSERT INTO t (id, json) VALUES (:jvar1, :jvar2)";
		//String statement = "DELETE FROM gtable WHERE h= :dsf);";
		String statement = "UPDATE gtable SET back =:thg WHERE id = :sdf";
		try {
			System.out.println("--------------" + statement + "\n");
			System.out.println(new CUDTranslator().translate(statement));

			statement = "SELECT * INTO jvar1, jvar2 FROM gtable WHERE h= (select id from tablemg where id = :jvar and name = :dsf);";
			System.out.println("--------------" + statement + "\n");
			System.out.println(new SelectTranslator().translate(statement));
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
