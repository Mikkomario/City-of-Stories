package cos_db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vault_database.DatabaseTable;
import vault_database.DatabaseUnavailableException;

/**
 * These are the database tables used for storing the basic entities introduced in this project
 * @author Mikko Hilpinen
 * @since 28.5.2015
 */
public enum CoSDatabaseTable implements DatabaseTable
{
	/**
	 * This table holds basic story data
	 */
	STORIES,
	/**
	 * This table holds basic user data
	 */
	USERS,
	/**
	 * This table holds secured user data
	 */
	SECURE;
	
	
	// ATTRIBUTES	------------------------------------
	
	private static Map<CoSDatabaseTable, List<ColumnInfo>> columnInfo = new HashMap<>();
	
	
	// IMPLEMENTED METHODS	----------------------------

	@Override
	public List<String> getColumnNames()
	{
		return DatabaseTable.getColumnNamesFromColumnInfo(getColumnInfo());
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
		return true;
	}

	@Override
	public boolean usesIntegerIndexing()
	{
		return true;
	}

	@Override
	public String getPrimaryColumnName()
	{
		return DatabaseTable.findPrimaryColumnInfo(getColumnInfo()).getColumnName();
	}
	
	
	// OTHER METHODS	-----------------------
	
	private List<ColumnInfo> getColumnInfo()
	{
		if (!columnInfo.containsKey(this))
		{
			try
			{
				columnInfo.put(this, DatabaseTable.readColumnInfoFromDatabase(this));
			}
			catch (DatabaseUnavailableException | SQLException e)
			{
				System.err.println("Can't read column info");
				e.printStackTrace();
			}
		}
		
		return columnInfo.get(this);
	}
}
