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
import cos_util.Duration;
import cos_util.Location;
import flow_recording.ObjectFormatException;
import nexus_http.HttpException;
import nexus_http.InternalServerException;
import nexus_http.InvalidParametersException;
import nexus_http.MethodNotSupportedException;
import nexus_http.MethodType;
import nexus_rest.ImmutableRestData;
import nexus_rest.RestEntity;
import nexus_rest.RestEntityLinkList;
import alliance_rest.DatabaseTableEntity;

/**
 * This entityList provides access to all the stories on the server
 * @author Mikko Hilpinen
 * @since 28.5.2015
 */
public class StoriesList extends DatabaseTableEntity
{
	// CONSTRUCTOR	------------------------
	
	/**
	 * Creates a new list
	 * @param parent The element above this list
	 */
	public StoriesList(RestEntity parent)
	{
		super("stories", new ImmutableRestData(new HashMap<>()), parent, 
				CoSDatabaseTable.STORIES);
	}
	
	
	// IMPLEMENTED METHODS	----------------

	@Override
	protected Map<String, RestEntity> getMissingEntities(
			Map<String, String> parameters) throws HttpException
	{
		// Also contains a filtered stories list
		Map<String, RestEntity> entities = super.getMissingEntities(parameters);
		FilteredStoriesList filtered = new FilteredStoriesList(this, parameters);
		entities.put(filtered.getName(), filtered);
		
		return entities;
	}

	@Override
	protected RestEntity getMissingEntity(String pathPart,
			Map<String, String> parameters) throws HttpException
	{
		if (pathPart.equalsIgnoreCase("filtered"))
			return new FilteredStoriesList(this, parameters);
		
		return super.getMissingEntity(pathPart, parameters);
	}
	
	@Override
	protected RestEntity loadEntityWithID(String id) throws HttpException
	{
		return new StoryEntity(id);
	}

	@Override
	public RestEntity Post(Map<String, String> parameters) throws HttpException
	{
		return new StoryEntity(this, parameters);
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
	
	
	// SUBCLASSES	------------------------
	
	private static class FilteredStoriesList extends RestEntityLinkList
	{
		// ATTRIBUTES	--------------------
		
		private List<RestEntity> entities;
		private Map<String, String> parameters;
		
		
		// CONSTRUCTOR	--------------------
		
		public FilteredStoriesList(RestEntity parent, Map<String, String> parameters)
		{
			super("filtered", parent);
			
			this.parameters = parameters;
			this.entities = null;
		}
		
		
		// IMPLEMENTED METHODS	------------

		@Override
		protected List<RestEntity> getEntities() throws HttpException
		{
			if (this.entities == null)
			{
				this.entities = new ArrayList<>();
				this.entities.addAll(getFilteredEntities());
			}
			
			return this.entities;
		}

		@Override
		public void trim(Map<String, String> parameters)
		{
			// TODO Add trimming if necessary
		}
		
		
		// OTHER METHODS	---------------
		
		private List<StoryEntity> getFilteredEntities() throws HttpException
		{
			try
			{
				List<String> storyIDs = DatabaseAccessor.findMatchingIDs(
						CoSDatabaseTable.STORIES, new String[0], new String[0]);
				
				// Parses the filters from the parameters
				if (!this.parameters.containsKey("location"))
					throw new InvalidParametersException("Parameter 'location' required");
				Location location = new Location(this.parameters.get("location"));
				int radius = 1000;
				if (this.parameters.containsKey("radius"))
					radius = Integer.parseInt(this.parameters.get("radius"));
				Duration maxDuration = null;
				if (this.parameters.containsKey("maxDuration"))
					maxDuration = new Duration(this.parameters.get("maxDuration"));
				String creatorName = null;
				if (this.parameters.containsKey("creatorName"))
					creatorName = this.parameters.get("creatorName");
				
				// Goes through the stories and filters them
				List<StoryEntity> filteredStories = new ArrayList<>();
				for (String storyID : storyIDs)
				{
					StoryEntity story = new StoryEntity(storyID);
					if (story.getLocation().getDistanceFrom(location) < radius && 
							(maxDuration == null || 
							story.getDuration().compareTo(maxDuration) < 0) && 
							(creatorName == null || 
							story.getCreator().getUserName().equals(creatorName)))
						filteredStories.add(story);
				}
				
				return filteredStories;
			}
			catch (NumberFormatException e)
			{
				throw new InvalidParametersException("Couldn't parse parameter: 'radius'");
			}
			catch (ObjectFormatException e)
			{
				throw new InvalidParametersException("Couldn't parse parameter: 'location'");
			}
			catch (DatabaseUnavailableException | SQLException | InvalidTableTypeException e)
			{
				throw new InternalServerException("Couldn't filter the stories", e);
			}
		}
	}
}
