package cos_rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vault_database.DatabaseAccessor;
import vault_database.DatabaseUnavailableException;
import vault_database.InvalidTableTypeException;
import cos_db.CoSDatabaseTable;
import nexus_http.ForbiddenActionException;
import nexus_http.HttpException;
import nexus_http.InternalServerException;
import nexus_http.InvalidParametersException;
import nexus_http.MethodNotSupportedException;
import nexus_http.MethodType;
import nexus_http.NotFoundException;
import nexus_rest.RestEntity;
import nexus_rest.RestEntityLinkList;
import nexus_rest.SimpleRestData;
import nexus_rest.SimpleRestEntityLinkList;
import alliance_rest.DatabaseEntity;

/**
 * This entity represents a service user
 * @author Mikko Hilpinen
 * @since 2.7.2015
 */
public class UserEntity extends DatabaseEntity
{
	// ATTRIBUTES	----------------------
	
	static final String ROOTPATH = "root/user/";
	
	// CONSTRUCTOR	----------------------
	
	/**
	 * Creates a new user by reading its data from the database
	 * @param id The identifier of the user
	 * @throws HttpException If the user couldn't be read
	 */
	public UserEntity(String id) throws HttpException
	{
		super(new SimpleRestData(), ROOTPATH, CoSDatabaseTable.USERS, id);
	}

	/**
	 * Creates a new user and saves it to the database
	 * @param parent The entity that created this entity
	 * @param parameters The parameters provided by the client
	 * @throws HttpException If the entity couldn't be created
	 */
	public UserEntity(RestEntity parent, Map<String, String> parameters) throws HttpException
	{
		super(new SimpleRestData(), parent, CoSDatabaseTable.USERS, 
				modifyParameters(parameters), new HashMap<>());
		
		// Also creates a secure for the new user
		new UserSecureEntity(this, parameters);
	}
	
	
	// IMPLEMENTED METHODS	--------------------

	@Override
	public void Put(Map<String, String> parameters) throws HttpException
	{
		throw new MethodNotSupportedException(MethodType.PUT);
	}

	@Override
	protected RestEntity getMissingEntity(String pathPart,
			Map<String, String> parameters) throws HttpException
	{
		if (pathPart.equalsIgnoreCase("stories"))
			return getStories();
		else if (pathPart.equalsIgnoreCase("secure"))
			return getSecure();
		
		throw new NotFoundException(getPath() + "/" + pathPart);
	}

	@Override
	protected Map<String, RestEntity> getMissingEntities(
			Map<String, String> parameters) throws HttpException
	{
		Map<String, RestEntity> links = new HashMap<>();
		links.put("stories", getStories());
		links.put("secure", getSecure());
		
		return links;
	}
	
	@Override
	protected void prepareDelete(Map<String, String> parameters)
			throws HttpException
	{
		throw new MethodNotSupportedException(MethodType.DELETE);
	}
	
	
	// GETTERS & SETTERS	-------------------
	
	/**
	 * @return The userName of this user
	 */
	public String getUserName()
	{
		return getAttributes().get("userName");
	}
	
	
	// OTHER METHODS	-----------------------
	
	private UserSecureEntity getSecure() throws HttpException
	{
		return new UserSecureEntity(getDatabaseID());
	}
	
	private RestEntityLinkList getStories() throws HttpException
	{
		// Finds out all the storyies created by the user
		List<RestEntity> stories = new ArrayList<>();
		try
		{
			for (String storyID : DatabaseAccessor.findMatchingIDs(CoSDatabaseTable.STORIES, 
					"creatorID", getDatabaseID()))
			{
				stories.add(new StoryEntity(storyID));
			}
		}
		catch (DatabaseUnavailableException | SQLException | InvalidTableTypeException e)
		{
			throw new InternalServerException("Failed to find the linked stories", e);
		}
		
		return new SimpleRestEntityLinkList("stories", this, stories);
	}
	
	private static Map<String, String> modifyParameters(Map<String, String> parameters) 
			throws HttpException
	{
		// Checks that the secure parameters exist
		if (!parameters.containsKey("email") || !parameters.containsKey("password"))
			throw new InvalidParametersException("Parameters 'email' and 'password' required");
		
		// Checks if the userName is already in use
		if (parameters.containsKey("userName"))
		{
			try
			{
				if (!DatabaseAccessor.findMatchingData(CoSDatabaseTable.USERS, "userName", 
						parameters.get("userName"), "userName").isEmpty())
					throw new ForbiddenActionException("userName '" + parameters.get("userName") + 
							"' is already in use");
			}
			catch (DatabaseUnavailableException | SQLException e)
			{
				throw new InternalServerException("failed to iterate through existing userNames", 
						e);
			}
		}
		
		return parameters;
	}
}
