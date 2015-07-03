package cos_rest;

import java.util.HashMap;
import java.util.Map;

import cos_db.CoSDatabaseTable;
import nexus_http.HttpException;
import nexus_http.MethodNotSupportedException;
import nexus_http.MethodType;
import nexus_rest.ImmutableRestData;
import nexus_rest.RestEntity;
import alliance_rest.DatabaseTableEntity;

/**
 * This list contains all the users of the service
 * @author Mikko Hilpinen
 * @since 3.7.2015
 */
public class UsersList extends DatabaseTableEntity
{
	// CONSTRUCTOR	------------------------
	
	/**
	 * Creates a new users list entity
	 * @param parent The entity above this entity
	 */
	public UsersList(RestEntity parent)
	{
		super("users", new ImmutableRestData(new HashMap<>()), parent, CoSDatabaseTable.USERS);
	}
	
	
	// IMPLEMENTED METHODS	----------------

	@Override
	protected RestEntity loadEntityWithID(String id) throws HttpException
	{
		return new UserEntity(id);
	}

	@Override
	public RestEntity Post(Map<String, String> parameters) throws HttpException
	{
		return new UserEntity(this, parameters);
	}

	@Override
	public void Put(Map<String, String> parameters) throws HttpException
	{
		throw new MethodNotSupportedException(MethodType.PUT);
	}

	@Override
	protected void prepareDelete(Map<String, String> parameters)
			throws HttpException
	{
		throw new MethodNotSupportedException(MethodType.DELETE);
	}
}
