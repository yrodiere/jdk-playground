package org.hibernate.jdk.playground;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TimestampWriteThenReadTest {

	private enum ReadWriteStrategy {
		OLD {
			@Override
			public Timestamp write(LocalDateTime localDateTime) {
				Instant instant = localDateTime.atZone( ZoneId.systemDefault() ).toInstant();
				return Timestamp.from( instant );
			}

			@Override
			public LocalDateTime read(Timestamp timestamp) {
				return LocalDateTime.ofInstant( timestamp.toInstant(), ZoneId.systemDefault() );
			}
		},
		NEW {
			@Override
			public Timestamp write(LocalDateTime localDateTime) {
				return Timestamp.valueOf( localDateTime );
			}

			@Override
			public LocalDateTime read(Timestamp timestamp) {
				return timestamp.toLocalDateTime();
			}
		};

		public abstract Timestamp write(LocalDateTime localDateTime);

		public abstract LocalDateTime read(Timestamp timestamp);
	}

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

	@Parameterized.Parameters(name = "{0} {1}")
	public static List<Object[]> data() {
		List<Object[]> results = new ArrayList<>();
		for ( ReadWriteStrategy strategy : ReadWriteStrategy.values() ) {
			for ( ZonedDateTime zonedDateTime : ZONED_DATE_TIMES ) {
				results.add( new Object[] { strategy, zonedDateTime } );
				/*
				 * The bug reported in HHH-13266 seems to appear before a certain date/time for each time zone (called "breaking point").
				 * Some of the dates we're looping on represent the first date/time that works fine for a certain time zone.
				 * Let's also try some dates before, so that we trigger the bug.
				 */
				results.add( new Object[] { strategy, zonedDateTime.minus( 10L, ChronoUnit.HOURS ) } );
				results.add( new Object[] { strategy, zonedDateTime.minus( 1L, ChronoUnit.SECONDS ) } );
			}
		}
		return results;
	}

	private final ReadWriteStrategy readWriteStrategy;
	private final ZoneId zoneId;
	private final LocalDateTime localDateTime;
	private final Instant instant;
	private final ZonedDateTime utcLocalDateTime;

	public TimestampWriteThenReadTest(ReadWriteStrategy readWriteStrategy, ZonedDateTime zonedDateTime) {
		this.readWriteStrategy = readWriteStrategy;
		this.zoneId = zonedDateTime.getZone();
		this.localDateTime = zonedDateTime.toLocalDateTime();
		this.instant = zonedDateTime.toInstant();
		this.utcLocalDateTime = instant.atZone( ZoneId.of( "UTC" ) );
	}

	@Test
	public void writeThenRead_direct() {
		withDefaultTimeZone( zoneId, () -> {
			Timestamp timestamp = readWriteStrategy.write( localDateTime );
			LocalDateTime newLocalDateTime = readWriteStrategy.read( timestamp );
			Assert.assertEquals( localDateTime, newLocalDateTime );
		} );
	}

	@Test
	public void writeThenRead_intermediaryLongConversion() {
		withDefaultTimeZone( zoneId, () -> {
			Timestamp timestamp = readWriteStrategy.write( localDateTime );

			// Simulate what a JDBC driver will do if it converts timestamps to numeric values
			// > Write
			long epochMilliseconds = timestamp.getTime();
			int epochNanos = timestamp.getNanos();
			// > Read
			Timestamp newTimestamp = new Timestamp( epochMilliseconds );
			newTimestamp.setNanos( epochNanos );

			LocalDateTime newLocalDateTime = readWriteStrategy.read( newTimestamp );

			Assert.assertEquals( localDateTime, newLocalDateTime );
		} );
	}

	@Test
	public void writeThenRead_intermediaryCalendarConversion() {
		withDefaultTimeZone( zoneId, () -> {
			System.out.println( "Ms since epoch: " + instant.toEpochMilli() );
			Timestamp timestamp = readWriteStrategy.write( localDateTime );
			System.out.println( "Timestamp ms since epoch: " + timestamp.getTime() );

			// Simulate what a JDBC driver will do if it converts timestamps to string values (at least that's what H2 does)
			// > Write
			Calendar calendar = Calendar.getInstance();
			calendar.setTime( timestamp );
			System.out.println( "Calendar ms since epoch: " + calendar.getTimeInMillis() );
			// > Read
			Timestamp newTimestamp = new Timestamp(
					calendar.get( Calendar.YEAR ) - 1900, calendar.get( Calendar.MONTH ), calendar.get( Calendar.DAY_OF_MONTH ),
					calendar.get( Calendar.HOUR_OF_DAY ), calendar.get( Calendar.MINUTE ), calendar.get( Calendar.SECOND ),
					calendar.get( Calendar.MILLISECOND ) * 1000
			);
			System.out.println( "New timestamp ms since epoch: " + newTimestamp.getTime() );

			LocalDateTime newLocalDateTime = readWriteStrategy.read( newTimestamp );
			Assert.assertEquals( localDateTime, newLocalDateTime );
		} );
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
