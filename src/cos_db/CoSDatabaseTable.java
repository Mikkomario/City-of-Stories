package cos_db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vault_database.DatabaseTable;
import vault_database.DatabaseUnavailableException;
import alliance_rest.DatabaseEntityTable;

/**
 * These are the database tables used for storing the basic entities introduced in this project
 * @author Mikko Hilpinen
 * @since 28.5.2015
 */
public enum CoSDatabaseTable implements DatabaseEntityTable
{
	/**
	 * This table holds basic story data
	 */
	STORIES,
	/**
	 * This table holds the sound files associated with each story
	 */
	SOUNDFILES;
	
	
	// ATTRIBUTES	------------------------------------
	
	private static Map<CoSDatabaseTable, List<String>> columnNames = new HashMap<>();
	
	
	// IMPLEMENTED METHODS	----------------------------

	@Override
	public List<String> getColumnNames()
	{
		if (!columnNames.containsKey(this))
		{
			try
			{
				columnNames.put(this, DatabaseTable.readColumnNamesFromDatabase(this));
			}
			catch (DatabaseUnavailableException | SQLException e)
			{
				System.err.println("Failed to read the column names");
				e.printStackTrace();
			}
		}
		
		return columnNames.get(this);
	}

	@Override
	public String getDatabaseName()
	{
		return "cos_stories_db";
	}

	@Override
	public String getTableName()
	{
		return toString().toLowerCase();
	}

	@Override
	public boolean usesAutoIncrementIndexing()
	{
		if (this == STORIES)
			return true;
		
		return false;
	}

	@Override
	public boolean usesIndexing()
	{
		return true;
	}

	@Override
	public String getIDColumnName()
	{
		if (this == STORIES)
			return "id";
		
		return "storyID";
	}
}
