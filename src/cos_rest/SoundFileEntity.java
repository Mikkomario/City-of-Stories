package cos_rest;

import java.util.HashMap;
import java.util.Map;

import cos_db.CoSDatabaseTable;
import nexus_http.HttpException;
import nexus_http.MethodNotSupportedException;
import nexus_http.MethodType;
import nexus_http.NotFoundException;
import nexus_rest.RestEntity;
import nexus_rest.SimpleRestData;
import alliance_rest.DatabaseEntity;

/**
 * This entity contains the sound file data associated with a single story entity
 * @author Mikko Hilpinen
 * @since 28.5.2015
 */
public class SoundFileEntity extends DatabaseEntity
{
	// CONSTRUCTOR	------------------------------
	
	/**
	 * Creates a new entity by reading it's data from the database
	 * @param storyID the unique identifier of the story associated with this file
	 * @throws HttpException If the entity couldn't be created
	 */
	public SoundFileEntity(String storyID) throws HttpException
	{
		super(new SimpleRestData(), StoryEntity.ROOTPATH + storyID + "/soundFile", 
				CoSDatabaseTable.SOUNDFILES, storyID);
	}

	/**
	 * Creates a new entity from the user-given parameters
	 * @param story The story that creates this entity
	 * @param parameters The parameters provided by the user (content)
	 * @throws HttpException If the entity couldn't be created
	 */
	SoundFileEntity(StoryEntity story, 
			Map<String, String> parameters) throws HttpException
	{
		super(new SimpleRestData(), story, CoSDatabaseTable.SOUNDFILES, story.getDatabaseID(), 
				parameters, new HashMap<>());
	}
	
	
	// IMPLEMENTED METHODS	----------------------------
	
	@Override
	public String getName()
	{
		return "soundFile";
	}
	
	@Override
	protected void prepareDelete(Map<String, String> parameters)
			throws HttpException
	{
		// SoundFiles can't be deleted by the user
		throw new MethodNotSupportedException(MethodType.DELETE);
	}

	@Override
	public void Put(Map<String, String> parameters) throws HttpException
	{
		throw new MethodNotSupportedException(MethodType.PUT);
	}

	@Override
	protected Map<String, RestEntity> getMissingEntities(
			Map<String, String> parameters) throws HttpException
	{
		// No missing entities
		return null;
	}

	@Override
	protected RestEntity getMissingEntity(String pathPart,
			Map<String, String> parameters) throws HttpException
	{
		throw new NotFoundException(getPath() + "/" + pathPart);
	}
	
	
	// OTHER METHODS	---------------------------------
	
	void delete() throws HttpException
	{
		super.prepareDelete(new HashMap<>());
	}
}
