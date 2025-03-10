package com.firebolt.jdbc.type.array;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Array;
import java.sql.SQLException;

import com.firebolt.jdbc.type.array.FireboltArray;
import com.firebolt.jdbc.type.array.SqlArrayUtil;
import org.junit.jupiter.api.Test;

import com.firebolt.jdbc.resultset.FireboltColumn;
import com.firebolt.jdbc.type.FireboltDataType;

class SqlArrayUtilTest {

	@Test
	void shouldTransformToEmptyArray() throws SQLException {
		String value = "[]";
		FireboltArray emptyArray = FireboltArray.builder().array(new Integer[] {}).type(FireboltDataType.INT_32)
				.build();

		Array result = SqlArrayUtil.transformToSqlArray(value, FireboltColumn.of("Array(INT32)"));

		assertEquals(emptyArray.getBaseType(), result.getBaseType());
		assertArrayEquals((Integer[]) emptyArray.getArray(), (Integer[]) result.getArray());
	}

	@Test
	void shouldTransformIntArray() throws SQLException {
		String value = "[1,2,3,\\N,5]";
		FireboltArray expectedArray = FireboltArray.builder().array(new Integer[] { 1, 2, 3, null, 5 })
				.type(FireboltDataType.INT_32).build();

		Array result = SqlArrayUtil.transformToSqlArray(value, FireboltColumn.of("Array(INT32)"));

		assertEquals(expectedArray.getBaseType(), result.getBaseType());
		assertArrayEquals((Integer[]) expectedArray.getArray(), (Integer[]) result.getArray());
	}

	@Test
	void shouldTransformStringArray() throws SQLException {
		String value = "['1','2','3','',\\N,'5']";
		FireboltArray expectedArray = FireboltArray.builder().array(new String[] { "1", "2", "3", "", null, "5" })
				.type(FireboltDataType.STRING).build();

		Array result = SqlArrayUtil.transformToSqlArray(value, FireboltColumn.of("Array(TEXT)"));

		assertEquals(expectedArray.getBaseType(), result.getBaseType());
		assertArrayEquals((String[]) expectedArray.getArray(), (String[]) result.getArray());
	}

	@Test
	void shouldTransformStringArrayWithComma() throws SQLException {
		String value = "['1','2,','3','',\\N,'5']";
		FireboltArray expectedArray = FireboltArray.builder().array(new String[] { "1", "2,", "3", "", null, "5" })
				.type(FireboltDataType.STRING).build();

		Array result = SqlArrayUtil.transformToSqlArray(value, FireboltColumn.of("Array(TEXT)"));

		assertEquals(expectedArray.getBaseType(), result.getBaseType());
		assertArrayEquals((String[]) expectedArray.getArray(), (String[]) result.getArray());
	}

	@Test
	void shouldTransformArrayOfTuples() throws SQLException {
		String value = "[(1,'a'),(2,'b'),(3,'c')]";
		Object[][] expectedArray = new Object[][] { { 1, "a" }, { 2, "b" }, { 3, "c" } };
		FireboltArray expectedFireboltArray = FireboltArray.builder().array(expectedArray).type(FireboltDataType.TUPLE)
				.build();

		Array result = SqlArrayUtil.transformToSqlArray(value, FireboltColumn.of("Array(TUPLE(int,string))"));

		assertEquals(expectedFireboltArray.getBaseType(), result.getBaseType());
		assertArrayEquals(expectedArray, (Object[]) result.getArray());
	}

	@Test
	void shouldTransformArrayOfArrayTuples() throws SQLException {
		String value = "[[(1,'(a))'),(2,'[b]'),(3,'[]c[')],[(4,'d')]]";
		Object[][][] expectedArray = new Object[][][] { { { 1, "(a))" }, { 2, "[b]" }, { 3, "[]c[" } },
				{ { 4, "d" } } };
		FireboltArray expectedFireboltArray = FireboltArray.builder().array(expectedArray).type(FireboltDataType.TUPLE)
				.build();

		Array result = SqlArrayUtil.transformToSqlArray(value, FireboltColumn.of("Array(Array(TUPLE(int,string)))"));

		assertEquals(expectedFireboltArray.getBaseType(), result.getBaseType());
		assertArrayEquals(expectedArray, (Object[][]) result.getArray());
	}

	@Test
	void shouldTransformArrayOfTuplesWithSpecialCharacters() throws SQLException {
		String value = "[(1,'a','1a'),(2,'b','2b'),(3,'[c]','3c')]";
		Object[][] expectedArray = new Object[][] { { 1, "a", "1a" }, { 2, "b", "2b" }, { 3, "[c]", "3c" } };
		FireboltArray expectedFireboltArray = FireboltArray.builder().array(expectedArray).type(FireboltDataType.TUPLE)
				.build();

		Array result = SqlArrayUtil.transformToSqlArray(value, FireboltColumn.of("Array(TUPLE(int,string,string))"));

		assertEquals(expectedFireboltArray.getBaseType(), result.getBaseType());
		assertArrayEquals(expectedArray, (Object[]) result.getArray());
	}

}
