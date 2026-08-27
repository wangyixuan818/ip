package noms.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import noms.exception.InvalidDateException;

/**
 * Tests {@link DateUtil}, focusing on {@link DateUtil#parse(String)}.
 *
 * {@code parse} is a good JUnit target because it is a pure static method
 * with deterministic output and a well-defined failure mode: valid ISO dates
 * return a {@link LocalDate}, and anything else throws
 * {@link InvalidDateException}. The tests cover both the accepted format and
 * the main ways input can be rejected.
 */
public class DateUtilTest {

    // --- parse: valid input returns the matching date ---

    @Test
    public void parse_validIsoDate_returnsMatchingDate() throws InvalidDateException {
        assertEquals(LocalDate.of(2019, 10, 15), DateUtil.parse("2019-10-15"));
    }

    @Test
    public void parse_leapDayInLeapYear_returnsMatchingDate() throws InvalidDateException {
        // 2020 is a leap year, so Feb 29 exists and must be accepted.
        assertEquals(LocalDate.of(2020, 2, 29), DateUtil.parse("2020-02-29"));
    }

    // --- parse: invalid input throws InvalidDateException ---

    @Test
    public void parse_leapDayInNonLeapYear_throwsInvalidDateException() {
        // 2019 is not a leap year, so Feb 29 is not a real calendar date.
        assertThrows(InvalidDateException.class, () -> DateUtil.parse("2019-02-29"));
    }

    @Test
    public void parse_nonExistentCalendarDate_throwsInvalidDateException() {
        // April has 30 days, so 2019-04-31 is well-formed but not a real date.
        assertThrows(InvalidDateException.class, () -> DateUtil.parse("2019-04-31"));
    }

    @Test
    public void parse_monthOutOfRange_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class, () -> DateUtil.parse("2019-13-01"));
    }

    @Test
    public void parse_nonIsoFormatWithSlashes_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class, () -> DateUtil.parse("2019/10/15"));
    }

    @Test
    public void parse_dayMonthYearOrder_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class, () -> DateUtil.parse("15-10-2019"));
    }

    @Test
    public void parse_missingZeroPadding_throwsInvalidDateException() {
        // ISO format requires two-digit month and day, so "2019-1-5" is rejected.
        assertThrows(InvalidDateException.class, () -> DateUtil.parse("2019-1-5"));
    }

    @Test
    public void parse_nonDateText_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class, () -> DateUtil.parse("tomorrow"));
    }

    @Test
    public void parse_emptyString_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class, () -> DateUtil.parse(""));
    }
}
