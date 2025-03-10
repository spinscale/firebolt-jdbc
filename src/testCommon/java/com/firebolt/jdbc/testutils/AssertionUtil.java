package com.firebolt.jdbc.testutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AssertionUtil {

	private AssertionUtil() {
	}

	public static void assertResultSetEquality(ResultSet expected, ResultSet actual) throws SQLException {
		assertEquals(expected.getMetaData(), actual.getMetaData());
		while (expected.next()) {
			assertTrue(actual.next(), "The actual resultSet has less rows than expected");
			for (int i = 0; i < expected.getMetaData().getColumnCount(); i++) {
				assertEquals(expected.getObject(i + 1), actual.getObject(i + 1));
			}
		}
	}
}
