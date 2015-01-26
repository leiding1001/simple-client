package com.example.cassandra;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class SimpleClient {
	private Cluster cluster ;
	private Session session;
    public Session getSession() {
      return this.session;
    }
	public void connect(String node) {
		   cluster = Cluster.builder()
		         .addContactPoint(node)
		         .build();
		   Metadata metadata = cluster.getMetadata();
		   System.out.printf("Connected to cluster: %s\n", 
		         metadata.getClusterName());
		   for ( Host host : metadata.getAllHosts() ) {
		      System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
		         host.getDatacenter(), host.getAddress(), host.getRack());
		   }
		   
		   session = cluster.connect();
	}
	
	public void createSchema() {
	      session.execute("CREATE KEYSPACE IF NOT EXISTS simplex WITH replication " + 
	            "= {'class':'SimpleStrategy', 'replication_factor':3};");
	      session.execute(
	            "CREATE TABLE IF NOT EXISTS simplex.songs (" +
	                  "id uuid PRIMARY KEY," + 
	                  "title text," + 
	                  "album text," + 
	                  "artist text," + 
	                  "tags set<text>," + 
	                  "data blob" + 
	                  ");");
	      session.execute(
	            "CREATE TABLE IF NOT EXISTS simplex.playlists (" +
	                  "id uuid," +
	                  "title text," +
	                  "album text, " + 
	                  "artist text," +
	                  "song_id uuid," +
	                  "PRIMARY KEY (id, title, album, artist)" +
	                  ");");
	   }
	 public void loadData() {
	      /*
	       //the first way
	       session.execute(
	            "INSERT INTO simplex.songs (id, title, album, artist, tags) " +
	            "VALUES (" +
	                "756716f7-2e54-4715-9f00-91dcbea6cf50," +
	                "'La Petite Tonkinoise'," +
	                "'Bye Bye Blackbird'," +
	                "'Joséphine Baker'," +
	                "{'jazz', '2013'})" +
	                ";");
	      session.execute(
	            "INSERT INTO simplex.playlists (id, song_id, title, album, artist) " +
	            "VALUES (" +
	                "2cc9ccb7-6221-4ccb-8387-f22b6a1b354d," +
	                "756716f7-2e54-4715-9f00-91dcbea6cf50," +
	                "'La Petite Tonkinoise'," +
	                "'Bye Bye Blackbird'," +
	                "'Joséphine Baker'" +
	                ");");*/
		 //the second way
		/* PreparedStatement statement = getSession().prepare(
			      "INSERT INTO simplex.songs " +
			      "(id, title, album, artist, tags) " +
			      "VALUES (?, ?, ?, ?, ?);");
		 BoundStatement boundStatement = new BoundStatement(statement);
		 Set<String> tags = new HashSet<String>();
		 tags.add("dinglei");
		 tags.add("2015");
		 getSession().execute(boundStatement.bind(
		       UUID.fromString("756716f7-2e54-4715-9f00-91dcbea6cf500"),
		       "dingleiLa Petite Tonkinoise'",
		       "dingleiBye Bye Blackbird'",
		       "dingleiJoséphine Baker",
		       tags ) );
		 
		 statement = getSession().prepare(
			      "INSERT INTO simplex.playlists " +
			      "(id, song_id, title, album, artist) " +
			      "VALUES (?, ?, ?, ?, ?);");
			boundStatement = new BoundStatement(statement);
			getSession().execute(boundStatement.bind(
			      UUID.fromString("2cc9ccb7-6221-4ccb-8387-f22b6a1b354d"),
			      UUID.fromString("756716f7-2e54-4715-9f00-91dcbea6cf50"),
			      "La Petite Tonkinoisedinglei",
			      "Bye Bye Blackbirddinglei",
			      "Joséphine Bakerdinglei") );*/
	   }
	 public void batchData(){
		 /*
		  * Cluster cluster = Cluster.builder()
		    .addContactPoint("10.1.1.3", "10.1.1.4", "10.1.1.5")
		    .withLoadBalancingPolicy(new DCAwareRoundRobinPolicy("US_EAST"))
		    .build();
			cluster.getConfiguration()
		    .getProtocolOptions()
		    .setCompression(ProtocolOptions.Compression.LZ4);
		  * 
		 from cassandra.query import BatchStatement
		 
		//Prepare the statements involved in a profile update
		profile_stmt = session.prepare(
		    "UPDATE user_profiles SET email=? WHERE key=?")
		user_track_stmt = session.prepare(
		    "INSERT INTO user_track (key, text, date) VALUES (?, ?, ?)")
		 
		# add the prepared statements to a batch
		batch = BatchStatement()
		batch.add(profile_stmt, [emailAddress, "hendrix"])
		batch.add(user_track_stmt,
		  ["hendrix", "email changed", datetime.utcnow()])
		 
		# execute the batch
		session.execute(batch)*/
	 }
	 public void querySchema() {
	      ResultSet results = session.execute("SELECT * FROM simplex.playlists " +
	             "WHERE id = 2cc9ccb7-6221-4ccb-8387-f22b6a1b354d;");
	      System.out.println(String.format("%-30s\t%-20s\t%-20s\n%s", "title", "album", "artist",
	             "-------------------------------+-----------------------+--------------------"));
	      for (Row row : results) {
	         System.out.println(String.format("%-30s\t%-20s\t%-20s", row.getString("title"),
	         row.getString("album"),  row.getString("artist")));
	      }
	      System.out.println();
	   }

	   public void close() {
	      session.close();
	      cluster.close();
	   }

	public static void main(String[] args) {
		 SimpleClient client = new SimpleClient();
	      client.connect("127.0.0.1");
	      client.createSchema();
	      client.loadData();
	      client.querySchema();
	      client.close();
	}
}
