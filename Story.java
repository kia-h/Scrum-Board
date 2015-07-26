/**
 * @author Internet's own boy
 */

/*
 * This class represents story on the board.
 * Each story can have multiple tasks
 * story are either in in process state or in completed state
 */

public class Story 
{

//Variables
	private String id;
	private String description;
	private Status.StoryStatus status;

//Constructors
	public Story()
	{
		id="";
		description="Not Set";
		status = Status.StoryStatus.IN_PROCESS;
	}
	
//Accessor methods
		
	public String getId() 
	{
		return id;
	}

	public void setId(String id) 
	{
		this.id = id;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public Status.StoryStatus getStatus() 
	{
		return status;
	}

	public void setStatus(Status.StoryStatus status) 
	{
		this.status = status;
	}

}//end of class