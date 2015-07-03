package cos_rest;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import alliance_util.SimpleDate;
import vault_database.DatabaseSettings;
import vault_database.DatabaseUnavailableException;
import nexus_rest.ContentType;
import nexus_rest.ImmutableRestEntity;
import nexus_rest.RestEntity;
import nexus_rest.StaticRestServer;
import nexus_test.HttpServerAnalyzer;

/**
 * This is a static class that starts the server
 * @author Mikko Hilpinen
 * @since 28.5.2015
 */
public class CoSServer
{
	// CONSTRUCTOR	------------------------
	
	private CoSServer()
	{
		// The interface is static
	}

	
	// MAIN METHOD	-----------------------
	
	/**
	 * Starts the server
	 * @param args The following arguments can be provided in the following order: server ip, 
	 * database password, server port (default = 7778), database user (default = root), 
	 * database address (default = "jdbc:mysql://localhost:3306/")
	 */
	public static void main(String[] args)
	{	
		// Parses the arguments
		if (args.length < 2)
		{
			System.out.println("Please provide the correct parameters: server ip, "
					+ "database password, server port (default = 7778), "
					+ "database user (default = root), "
					+ "database address (default = jdbc:mysql://localhost:3306/)");
			System.exit(0);
		}
		
		int port = 7778;
		if (args.length > 2)
		{
			try
			{
				port = Integer.parseInt(args[2]);
			}
			catch (NumberFormatException e)
			{
				System.err.println("The port number must be an integer");
				System.exit(1);
			}
		}
		String user = "root";
		if (args.length > 3)
			user = args[3];
		String address = "jdbc:mysql://localhost:3306/";
		if (args.length > 4)
			address = args[4];
		
		// Initializes the database
		try
		{
			DatabaseSettings.initialize(address, user, args[1], 1000, "cos_management_db", 
					"tableamounts");
		}
		catch (DatabaseUnavailableException | SQLException e)
		{
			System.err.println("Failed to initialize the database");
			e.printStackTrace();
			System.exit(1);
		}
		
		// Creates the entities
		Map<String, String> rootStats = new HashMap<>();
		rootStats.put("version", "0.0.2");
		rootStats.put("started", new SimpleDate().toString());
		rootStats.put("author", "Mikko Hilpinen");
		RestEntity root = new ImmutableRestEntity("root", null, rootStats);
		
		new StoriesList(root);
		new UsersList(root);
		
		// Starts the server
		StaticRestServer.startServer(args[0], port, true, ContentType.JSON, root, 
				new HttpServerAnalyzer());
	}
}
