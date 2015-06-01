package cos_rest;

import java.util.HashMap;
import java.util.Map;

import cos_db.CoSDatabaseTable;
import cos_util.Location;
import nexus_http.HttpException;
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
		return new HashMap<>();
	}

	@Override
	protected RestEntity getMissingEntity(String pathPart,
			Map<String, String> parameters) throws HttpException
	{
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
	
	
	// OTHER METHODS	-------------------------
	
	private static Map<String, String> modifyParameters(Map<String, String> parameters)
	{	
		// Adds parameter(s)
		parameters.put("created", new SimpleDate().toString());
		
		return parameters;
	}
	
	private static Map<String, String> getDefaultParameters(Map<String, String> userParameters)
	{
		Map<String, String> params = new HashMap<>();
		params.put("fileName", userParameters.get("name") + ".mp3");
		
		return params;
	}
}
