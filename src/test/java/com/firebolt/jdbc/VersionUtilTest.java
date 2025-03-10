package com.firebolt.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VersionUtilTest {

	@Test
	void shouldGetDriverMajorVersion() {
		assertEquals(2, VersionUtil.getMajorDriverVersion());
	}

	@Test
	void shouldGetDriverMinorVersion() {
		assertEquals(5, VersionUtil.getDriverMinorVersion());
	}

	@Test
	void shouldGetProjectVersion() {
		assertEquals("2.5-TEST-SNAPSHOT", VersionUtil.getDriverVersion());
	}

	@Test
	void shouldGetMinorVersionFromString() {
		assertEquals(54, VersionUtil.extractMinorVersion("123.54.13"));
	}

	@Test
	void shouldGetMajorVersionFromString() {
		assertEquals(123, VersionUtil.extractMajorVersion("123.54.13"));
	}

	@Test
	void shouldGet0WhenMajorVersionCannotBeFound() {
		assertEquals(0, VersionUtil.extractMinorVersion("123a54b13"));
	}

	@Test
	void shouldGet0WhenMinorVersionCannotBeFound() {
		assertEquals(0, VersionUtil.extractMajorVersion("123a54b13"));
	}
}
