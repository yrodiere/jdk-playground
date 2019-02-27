package org.hibernate.jdk.bugs;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class JdkChronoFieldsToEpochMillisConversionTest {

	private static final List<ZonedDateTime> ZONED_DATE_TIMES = Arrays.asList(
			LocalDateTime.parse( "2017-11-06T19:19:00" ).atZone( ZoneId.of( "UTC-8" ) ),
			LocalDateTime.parse( "2017-11-06T19:19:00" ).atZone( ZoneId.of( "Europe/Paris" ) ),
			LocalDateTime.parse( "1970-01-01T00:00:00" ).atZone( ZoneId.of( "GMT" ) ),
			LocalDateTime.parse( "1900-01-01T00:00:00" ).atZone( ZoneId.of( "GMT" ) ),
			LocalDateTime.parse( "1892-01-01T00:00:00" ).atZone( ZoneId.of( "Europe/Oslo" ) ), // Before the breaking point
			LocalDateTime.parse( "1900-01-01T00:09:21" ).atZone( ZoneId.of( "Europe/Paris" ) ), // Breaking point
			LocalDateTime.parse( "1900-01-01T00:19:32" ).atZone( ZoneId.of( "Europe/Amsterdam" ) ), // Breaking point
			LocalDateTime.parse( "1900-01-01T01:00:00" ).atZone( ZoneId.of( "Europe/Amsterdam" ) ), // Before the breaking point
			LocalDateTime.parse( "1899-12-31T23:39:03" ).atZone( ZoneId.of( "Europe/Amsterdam" ) ) // Before the breaking point
	);

	@Parameterized.Parameters(name = "{0}")
	public static List<Object[]> data() {
		List<Object[]> results = new ArrayList<>();
		for ( ZonedDateTime zonedDateTime : ZONED_DATE_TIMES ) {
			results.add( new Object[] { zonedDateTime } );
			/*
			 * The bug seems to appear before a certain date/time for each time zone (called "breaking point").
			 * Some of the dates we're looping on represent the first date/time that works fine for a certain time zone.
			 * Let's also try some dates before, so that we trigger the bug.
			 */
			results.add( new Object[] { zonedDateTime.minus( 10L, ChronoUnit.HOURS ) } );
			results.add( new Object[] { zonedDateTime.minus( 1L, ChronoUnit.SECONDS ) } );
		}
		return results;
	}

	private final ZoneId zoneId;
	private final LocalDateTime localDateTime;
	private final Instant instant;

	public JdkChronoFieldsToEpochMillisConversionTest(ZonedDateTime zonedDateTime) {
		this.zoneId = zonedDateTime.getZone();
		this.localDateTime = zonedDateTime.toLocalDateTime();
		this.instant = zonedDateTime.toInstant();
	}

	@Before
	public void logParameters() {
		System.out.println( "Zone: " + zoneId );
		System.out.println( "LocalDateTime: " + localDateTime );
		System.out.println( "Millis since epoch: " + instant.toEpochMilli() );
	}

	@Test
	public void timestamp_fromChronoFields_getTime() {
		withDefaultTimeZone( zoneId, () -> {
			Timestamp timestamp = new Timestamp(
					localDateTime.getYear() - 1900, localDateTime.getMonthValue() - 1,
					localDateTime.getDayOfMonth(),
					localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond(),
					localDateTime.getNano()
			);
			// getTime should return a number of millisecond since the epoch on the UTC timezone
			logAndAssert( instant.toEpochMilli(), timestamp.getTime() );
		} );
	}

	@Test
	public void timestamp_fromChronoFields_getChronoFields() {
		withDefaultTimeZone( zoneId, () -> {
			Timestamp timestamp = new Timestamp(
					localDateTime.getYear() - 1900, localDateTime.getMonthValue() - 1,
					localDateTime.getDayOfMonth(),
					localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond(),
					localDateTime.getNano()
			);
			// getYear, etc. should return values consistent with the *local* timezone
			logAndAssert(
					new int[] {
							localDateTime.getYear() - 1900, localDateTime.getMonth().getValue() - 1,
							localDateTime.getDayOfMonth(),
							localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
					},
					new int[] {
							timestamp.getYear(), timestamp.getMonth(),
							timestamp.getDate(),
							timestamp.getHours(), timestamp.getMinutes(), timestamp.getSeconds()
					}
			);
		} );
	}

	@Test
	public void timestamp_fromEpoch_getTime() {
		withDefaultTimeZone( zoneId, () -> {
			Timestamp timestamp = new Timestamp( instant.toEpochMilli() );
			// getTime should return a number of millisecond since the epoch on the UTC timezone
			logAndAssert( instant.toEpochMilli(), timestamp.getTime() );
		} );
	}

	@Test
	public void timestamp_fromEpoch_getChronoFields() {
		withDefaultTimeZone( zoneId, () -> {
			Timestamp timestamp = new Timestamp( instant.toEpochMilli() );
			// getYear, etc. should return values consistent with the *local* timezone
			logAndAssert(
					new int[] {
							localDateTime.getYear() - 1900, localDateTime.getMonth().getValue() - 1,
							localDateTime.getDayOfMonth(),
							localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
					},
					new int[] {
							timestamp.getYear(), timestamp.getMonth(),
							timestamp.getDate(),
							timestamp.getHours(), timestamp.getMinutes(), timestamp.getSeconds()
					}
			);
		} );
	}

	@Test
	public void calendar_fromChronoFields_getTimeInMillis() {
		withDefaultTimeZone( zoneId, () -> {
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(
					localDateTime.getYear(), localDateTime.getMonthValue() - 1,
					localDateTime.getDayOfMonth(),
					localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
			);
			calendar.set( Calendar.MILLISECOND, localDateTime.getNano() / 1000 );
			// getTimeInMillis should return a number of millisecond since the epoch on the UTC timezone
			logAndAssert( instant.toEpochMilli(), calendar.getTimeInMillis() );
		} );
	}

	@Test
	public void calendar_fromChronoFields_getChronoFields() {
		withDefaultTimeZone( zoneId, () -> {
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(
					localDateTime.getYear(), localDateTime.getMonthValue() - 1,
					localDateTime.getDayOfMonth(),
					localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
			);
			calendar.set( Calendar.MILLISECOND, localDateTime.getNano() / 1000 );
			// get(...) should return values consistent with the *local* timezone
			logAndAssert(
					new int[] {
							localDateTime.getYear(), localDateTime.getMonth().getValue() - 1, localDateTime.getDayOfMonth(),
							localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
					},
					new int[] {
							calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DAY_OF_MONTH ),
							calendar.get( Calendar.HOUR_OF_DAY ), calendar.get( Calendar.MINUTE ), calendar.get( Calendar.SECOND )
					}
			);
		} );
	}

	@Test
	public void calendar_fromEpoch_getTimeInMillis() {
		withDefaultTimeZone( zoneId, () -> {
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.setTimeInMillis( instant.toEpochMilli() );
			// getTimeInMillis should return a number of millisecond since the epoch on the UTC timezone
			logAndAssert( instant.toEpochMilli(), calendar.getTimeInMillis() );
		} );
	}

	@Test
	public void calendar_fromEpoch_getChronoFields() {
		withDefaultTimeZone( zoneId, () -> {
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.setTimeInMillis( instant.toEpochMilli() );
			// get(...) should return values consistent with the *local* timezone
			logAndAssert(
					new int[] {
							localDateTime.getYear(), localDateTime.getMonth().getValue() - 1, localDateTime.getDayOfMonth(),
							localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
					},
					new int[] {
							calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DAY_OF_MONTH ),
							calendar.get( Calendar.HOUR_OF_DAY ), calendar.get( Calendar.MINUTE ), calendar.get( Calendar.SECOND )
					}
			);
		} );
	}

	private static void logAndAssert(long expected, long actual) {
		System.out.println( "Expected: " + expected + ", actual: " + actual );
		Assert.assertEquals( expected, actual );
	}

	private static void logAndAssert(int[] expecteds, int[] actuals) {
		System.out.println( "Expected: " + Arrays.toString( expecteds ) + ", actual: " + Arrays.toString( actuals ) );
		Assert.assertArrayEquals( expecteds, actuals );
	}

	private static void withDefaultTimeZone(ZoneId zoneId, Runnable runnable) {
		TimeZone timeZoneBefore = TimeZone.getDefault();
		TimeZone.setDefault( TimeZone.getTimeZone( zoneId ) );
		try {
			runnable.run();
		}
		finally {
			TimeZone.setDefault( timeZoneBefore );
		}
	}

}
