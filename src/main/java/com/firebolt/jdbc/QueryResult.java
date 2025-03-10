package com.firebolt.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.firebolt.jdbc.type.FireboltDataType;

import lombok.Builder;
import lombok.Value;

/**
 * Class containing a query result that can be used to create a {@link com.firebolt.jdbc.resultset.FireboltResultSet}
 */
@Builder
@Value
public class QueryResult {

	String databaseName;

	String tableName;

	private static final String TAB = "\t";
	private static final String NEXT_LINE = "\n";
	@Builder.Default
	List<Column> columns = new ArrayList<>();
	@Builder.Default
	List<List<?>> rows = new ArrayList<>();

	/**
	 * @return the string representing a query response in the TabSeparatedWithNamesAndTypes format
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		this.appendWithListValues(stringBuilder, columns.stream().map(Column::getName).collect(Collectors.toList()));
		stringBuilder.append(NEXT_LINE);
		this.appendWithListValues(stringBuilder, columns.stream().map(Column::getType)
				.map(FireboltDataType::getInternalName).collect(Collectors.toList()));
		stringBuilder.append(NEXT_LINE);

		for (int i = 0; i < rows.size(); i++) {
			appendWithListValues(stringBuilder, rows.get(i));
			if (i != rows.size() - 1) {
				stringBuilder.append(NEXT_LINE);
			}
		}
		return stringBuilder.toString();
	}

	private StringBuilder appendWithListValues(StringBuilder destination, List<?> values) {
		Iterator<?> iterator = values.iterator();
		while (iterator.hasNext()) {
			Object value = iterator.next();
			if (null == value) {
				value = "\\N"; // null
			}
			destination.append(value);
			if (iterator.hasNext()) {
				destination.append(TAB);
			}
		}
		return destination;
	}

	@Builder
	@Value
	public static class Column {
		String name;
		FireboltDataType type;
	}
}
