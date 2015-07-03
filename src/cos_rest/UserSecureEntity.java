package cos_rest;

import java.util.Map;

import cos_db.CoSDatabaseTable;
import cos_db.CoSLoginKeyTable;
import nexus_http.HttpException;
import alliance_authorization.SecureEntity;

/**
 * This entity represents an user's secure data
 * @author Mikko Hilpinen
 * @since 2.7.2015
 */
public class UserSecureEntity extends SecureEntity
{
	// CONSTRUCTOR	--------------------
	
	/**
	 * Creates a new secure entity by reading its data from the database
	 * @param userID The identifier of the user linked to this secure
	 * @throws HttpException If the entity couldn't be created
	 */
	public UserSecureEntity(String userID) throws HttpException
	{
		super(CoSDatabaseTable.SECURE, UserEntity.ROOTPATH + "/" + userID + "/", "secure", 
				userID, "passwordHash", "password");
	}
	
	/**
	 * Creates a new secure entity and saves it to the database
	 * @param user The user connected to this entity
	 * @param parameters The parameters provided by the client (password and email required)
	 * @throws HttpException If the entity couldn't be created
	 */
	public UserSecureEntity(UserEntity user, Map<String, String> parameters) throws HttpException
	{
		super(CoSDatabaseTable.SECURE, user, "secure", user.getDatabaseID(), "passwordHash", 
				"password", parameters);
	}
	
	
	// IMPLEMENTED METHODS	------------------

	@Override
	protected void authorizeModification(Map<String, String> parameters) throws HttpException
	{
		CoSLoginKeyTable.authorize(getAttributes().get("userID"), parameters);
	}
}
