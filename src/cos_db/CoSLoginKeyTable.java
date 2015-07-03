package cos_db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import nexus_http.HttpException;
import vault_database.DatabaseTable;
import vault_database.DatabaseUnavailableException;
import alliance_authorization.LoginKeyTable;

/**
 * These are the tables that hold the login keys in cos
 * @author Mikko Hilpinen
 * @since 3.7.2015
 */
public enum CoSLoginKeyTable implements LoginKeyTable
{
	/**
	 * The default (and only) login key table
	 */
	DEFAULT;
	
	
	// ATTRIBUTES	------------------------
	
	private static List<ColumnInfo> columnInfo = null;
	
	
	// IMPLEMENTED METHODS	---------------------

	@Override
	public List<String> getColumnNames()
	{
		return DatabaseTable.getColumnNamesFromColumnInfo(getColumnInfo());
	}

	@Override
	public String getDatabaseName()
	{
		return "cos_users_db";
	}

	@Override
	public String getPrimaryColumnName()
	{
		return DatabaseTable.findPrimaryColumnInfo(getColumnInfo()).getColumnName();
	}

	@Override
	public String getTableName()
	{
		return "loginkeys";
	}

	@Override
	public boolean usesAutoIncrementIndexing()
	{
		return false;
	}

	@Override
	public boolean usesIntegerIndexing()
	{
		return true;
	}

	@Override
	public String getUserIDColumnName()
	{
		return getPrimaryColumnName();
	}

	@Override
	public String getKeyColumnName()
	{
		return "userKey";
	}

	@Override
	public String getCreationTimeColumnName()
	{
		return "created";
	}
	
	
	// OTHER METHODS	----------------
	
	/**
	 * Checks the user key that should be provided among the parameters
	 * @param userID The identifier of the concerned user
	 * @param parameters The parameters provided by the client
	 * @throws HttpException If the authorization failed
	 */
	public static void authorize(String userID, Map<String, String> parameters) throws 
			HttpException
	{
		LoginKeyTable.checkKey(DEFAULT, userID, parameters);
	}
	
	private List<ColumnInfo> getColumnInfo()
	{
		if (columnInfo == null)
		{
			try
			{
				columnInfo = DatabaseTable.readColumnInfoFromDatabase(this);
			}
			catch (DatabaseUnavailableException | SQLException e)
			{
				System.err.println("Can't read loginKeyTable info");
				e.printStackTrace();
			}
		}
		
		return columnInfo;
	}
}
