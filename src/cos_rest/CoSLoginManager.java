package cos_rest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import vault_database.DatabaseAccessor;
import vault_database.DatabaseUnavailableException;
import vault_database.InvalidTableTypeException;
import cos_db.CoSDatabaseTable;
import cos_db.CoSLoginKeyTable;
import nexus_http.HttpException;
import nexus_http.InternalServerException;
import nexus_rest.RestEntity;
import alliance_authorization.LoginManagerEntity;
import alliance_authorization.PasswordChecker;

/**
 * This login manager also supports the use of userNames
 * @author Mikko Hilpinen
 * @since 3.7.2015
 */
public class CoSLoginManager extends LoginManagerEntity
{
	// CONSTRUCTOR	--------------------
	
	/**
	 * Creates a new login manager entity
	 * @param parent
	 */
	public CoSLoginManager(RestEntity parent)
	{
		super("login", parent, CoSLoginKeyTable.DEFAULT, new PasswordChecker(
				CoSDatabaseTable.SECURE, "passwordHash", "userID"), false);
	}

	
	// IMPLEMENTED METHODS	------------
	
	@Override
	protected RestEntity getMissingEntity(String pathPart,
			Map<String, String> parameters) throws HttpException
	{
		// First tries to find the user based on the user name (if not integer)
		if (!Character.isDigit(pathPart.charAt(0)))
		{
			try
			{
				List<String> ids = DatabaseAccessor.findMatchingIDs(CoSDatabaseTable.USERS, 
						"userName", pathPart, 1);
				if (!ids.isEmpty())
					return super.getMissingEntity(ids.get(0), parameters);
			}
			catch (DatabaseUnavailableException | SQLException
					| InvalidTableTypeException e)
			{
				throw new InternalServerException("Failed to find a user based on a userName", 
						e);
			}
		}
		
		return super.getMissingEntity(pathPart, parameters);
	}
}
