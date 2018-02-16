package logback.mongodb;

import java.util.Date;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

import org.bson.Document;

/**
 * @author Christian Trutz, Alessandro Garzaro
 */
public class MongoDBAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

	private MongoDBConnectionSource connectionSource = null;

	@Override
	protected void append(ILoggingEvent eventObject) {
    Document logEntry = new Document( );

		logEntry.append("message",   eventObject.getFormattedMessage());
		logEntry.append("logger",    eventObject.getLoggerName());
		logEntry.append("thread",    eventObject.getThreadName());
		logEntry.append("timestamp", new Date(eventObject.getTimeStamp()));
		logEntry.append("level",     eventObject.getLevel().toString());

		connectionSource.getDBCollection().insertOne(logEntry);
	}

	public void setConnectionSource(MongoDBConnectionSource connectionSource) {
		this.connectionSource = connectionSource;
	}

}
