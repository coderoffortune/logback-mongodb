package logback.mongodb;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * @author Christian Trutz
 */
public class MongoDBConnectionSource {

	private volatile MongoCollection<Document> dbCollection = null;

	private String uri = null;

	private String db = null;

	private String collection = null;

	protected MongoCollection<Document> getDBCollection() {
    MongoCollection<Document> dbCollectionHelper = dbCollection;

		if (dbCollectionHelper == null) {
			synchronized (this) {

			  dbCollectionHelper = dbCollection;

				if (dbCollectionHelper == null) {
					try {
						final MongoClient mongo = new MongoClient(new MongoClientURI(uri));

						dbCollection = mongo.getDatabase(db).getCollection(collection);

						Runtime.getRuntime().addShutdownHook(
								new Thread(new Runnable() {

									@Override
									public void run() {
										mongo.close();
									}

								},
                "mongo shutdown"));
					} catch (MongoException mongoException) {
						mongoException.printStackTrace();
					}
				}

			}
		}

		return dbCollection;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

}
