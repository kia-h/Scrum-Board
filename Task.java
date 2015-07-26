/**
 * @author Internet's own boy
 */

/* this class represents task on the board
 * each task belongs to one story only */
public class Task 
{
	
//Variables
	private String id;
	private String description;
	private Status.TasksStatus status;

//Constructors
	public Task()
	{
		id="";
		description = "Not Set";
		status=Status.TasksStatus.TODO;
	}
	
	public Task(String id, String description) 
	{
		this();
		this.id = id;
		this.description = description;
	}
	
//Accessors methods
	
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
	
	public Status.TasksStatus getStatus() 
	{
		return status;
	}

	public void setStatus(Status.TasksStatus status)
	{
		this.status = status;
	}

}//end of class