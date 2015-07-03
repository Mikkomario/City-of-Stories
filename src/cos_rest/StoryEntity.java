package cos_rest;

import java.util.HashMap;
import java.util.Map;

import cos_db.CoSDatabaseTable;
import cos_util.Duration;
import cos_util.Location;
import flow_recording.ObjectFormatException;
import nexus_http.HttpException;
import nexus_http.InvalidParametersException;
import nexus_http.MethodNotSupportedException;
import nexus_http.MethodType;
import nexus_http.NotFoundException;
import nexus_rest.RestEntity;
import nexus_rest.SimpleRestData;
import alliance_rest.DatabaseEntity;
import alliance_util.SimpleDate;

/**
 * This entity represents a user created story
 * @author Mikko Hilpinen
 * @since 28.5.2015
 */
public class StoryEntity extends DatabaseEntity
{
	// ATTRIBUTES	---------------------------
	
	static final String ROOTPATH = "root/stories/";
	
	
	// CONSTRUCTOR	---------------------------
	
	/**
	 * Creates a new story by reading its data from the database
	 * @param id The identifier of the story
	 * @throws HttpException If the story couldn't be read
	 */
	public StoryEntity(String id) throws HttpException
	{
		super(new SimpleRestData(), ROOTPATH, CoSDatabaseTable.STORIES, id);
	}

	/**
	 * Creates a new story based on the provided parameters
	 * @param parent The creator entity
	 * @param parameters The parameters provided by the client (name & location)
	 * @throws HttpException If the entity couldn't be created
	 */
	public StoryEntity(RestEntity parent, Map<String, String> parameters) throws HttpException
	{
		super(new SimpleRestData(), parent, CoSDatabaseTable.STORIES, 
				modifyParameters(parameters), getDefaultParameters(parameters));
	}
	
	
	// IMPLEMENTED METHODS	----------------------
	
	@Override
	public void Put(Map<String, String> parameters) throws HttpException
	{
		throw new MethodNotSupportedException(MethodType.PUT);
	}

	@Override
	protected Map<String, RestEntity> getMissingEntities(
			Map<String, String> parameters) throws HttpException
	{
		Map<String, RestEntity> links = new HashMap<>();
		links.put("creator", getCreator());
		
		return links;
	}

	@Override
	protected RestEntity getMissingEntity(String pathPart,
			Map<String, String> parameters) throws HttpException
	{
		if (pathPart.equalsIgnoreCase("creator"))
			return getCreator();
		
		throw new NotFoundException(getPath() + "/" + pathPart);
	}
	
	
	// GETTERS & SETTERS	---------------------

	/**
	 * @return The story's location
	 */
	public Location getLocation()
	{
		return new Location(getAttributes().get("location"));
	}
	
	/**
	 * @return The duration of the story
	 */
	public Duration getDuration()
	{
		return new Duration(getAttributes().get("duration"));
	}
	
	/**
	 * @return The user that created this story
	 * @throws HttpException If the user couldn't be found
	 */
	public UserEntity getCreator() throws HttpException
	{
		return new UserEntity(getAttributes().get("creatorID"));
	}
	
	
	// OTHER METHODS	-------------------------
	
	private static Map<String, String> modifyParameters(Map<String, String> parameters) throws 
			HttpException
	{	
		// Adds parameter(s)
		parameters.put("created", new SimpleDate().toString());
		
		// Parses the duration to test it
		if (parameters.containsKey("duration"))
		{
			try
			{
				parameters.put("duration", 
						new Duration(parameters.get("duration")).toString());
			}
			catch (ObjectFormatException e)
			{
				throw new InvalidParametersException(e.getMessage());
			}
		}
		
		// Tests the user validity
		if (parameters.containsKey("creatorID"))
			new UserEntity(parameters.get("creatorID"));
		
		// TODO: Also test the authorization
		
		return parameters;
	}
	
	private static Map<String, String> getDefaultParameters(Map<String, String> userParameters)
	{
		Map<String, String> params = new HashMap<>();
		params.put("fileName", userParameters.get("name") + ".mp3");
		
		return params;
	}
}
