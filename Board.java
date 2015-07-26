/**
 * @author Internet's own boy
 */

import java.util.Iterator;
import java.util.HashMap;
import java.util.Scanner;

/*
 * This class is responsible for managing stories and tasks on the board
 * here "story" and "user story" are the same entity
 */

public class Board 
{

//Variables
	
	private HashMap<String,Story > stories;
	private HashMap<String,Task> tasks;
	
	int uniqueStoryId = 0;
	int uniqueTaskId = 0;

//Constructors
	
	public Board ()
	{
		stories = new HashMap<>();
		tasks = new HashMap<>();		
	}

//Methods
	
	public String getUniqueStoryId()
	{
	     return ""+(++uniqueStoryId);
	}
	
	public String getUniqueTaskId()
	{
		return ""+(++uniqueTaskId);
	}
	
	public void resetId()
	{
		uniqueTaskId = 0;
	}
	
//Story Related Methods
	
	/**
	 * Creates a user story
	 * 
	 * @param storyId		Id of the new story
	 * @param description	description of the new story
	 * @return true if story is created successfully, otherwise false
	 */
	public boolean createStory(String storyId, String description)
	{
		try
		{
			//check if description is empty or null
			if (description != null && !description.isEmpty())
			{
				Story temp = new Story();
				if (stories.containsKey(storyId))
				{
					System.out.println ("Story with given Id exists!");
					System.out.println ("Creating new one with auto generated Id");
					temp.setId(stories.size()+getUniqueStoryId());
				}
				else
				{
					temp.setId(storyId);
				}
				//everything's good, create a new story, and set its details
				temp.setDescription(description);
				
				//by default created story will be put in "In Process" state
				temp.setStatus(Status.StoryStatus.IN_PROCESS);
				stories.put(temp.getId(), temp);
				System.out.println ("Story created successfully");	
				return true;
			}
		}
		catch (Exception e)
		{
			return false;	
		}			
		
		//if we get here, something bad happened
		return false;		
	}
	
	/**
	 * Delete Story with given id 
	 * 
	 * @param 	storyId		story id we want to delete 
	 * @return 				true if removal is successful, otherwise false 
	 */
	public boolean deleteStory(String storyId)
	{
		
		try 
		{
			//check story existence and its value 
			if (storyId==null || storyId.isEmpty() || stories.size()==0 || !stories.containsKey(storyId))
			{
				return false;
			}
			
			//chek if story exists
			if (!stories.containsKey(storyId))
			{
				System.out.println("Story doesn't exist.");
				return true;
			}
			//we have the story, get user's respond
			Scanner reader = new Scanner(System.in);
			System.out.println ("Deleting story will delete all its tasks, sure?(y/n)");
			String answer = reader.next();
			
			//delete the story?
			if (answer.equalsIgnoreCase("y"))
			{		
				//yes, then delete all its tasks first
				if (deleteAllTasks(storyId))
				{
					//all tasks removed, now delete the story itself
					if (stories.remove(storyId)!= null)
					{
						System.out.println ("Story deleted successfully");
						return true;
					}
				}
				
				//deleting tasks encountered some problems
				else 
				{
					return false;
				}
			}
			
			//user doesn't want to delete the story
			else
			{
				System.out.println ("Story deletion cancelled");
				return true;
			}
			return false;
		}
		catch (Exception e)
		{
			return false;	
		}
		
	}//end of delete story function
	
	/**
	 * Mark a story as complete backward move for a task of a story  
	 * 
	 * @param 	storyId		id of the story to mark it completed  
	 * @return 				true if operation is successful, otherwise false 
	 */
	public boolean completeStory(String storyId)
	{		
		try
		{
	
			//check given story id
			if (storyId==null || storyId.isEmpty() || stories.size()==0 || !stories.containsKey(storyId))
			{
				return false;
			}		
		
			//check if all of this story's tasks are in DONE state  
			if (listTasks(storyId, false))
			{	
				Story tempStory = stories.get(storyId);
				
				//set the story's status
				tempStory.setStatus(Status.StoryStatus.COMPLETED);
				System.out.print("Following Story completed:");
				System.out.println (storyId + " : "+ tempStory.getDescription());
				System.out.println ("To see all the stories: story list ");
				return true;
			}
			else 
			{
				System.out.println ("Some tasks are not completed yet");
				System.out.println ("Run: task list "+storyId+"  - to see the story's tasks list.");
				System.out.println ("Run: help for more infomartion.");
				
				return false;
			}
		}
			
		catch (Exception e)
		{
			return false;	
		}	
	}//end of method
	
	/**
	 * List available stories  
	 * 
	 * @param 	N/A
	 * @return 	N/A
	 */
	public void listStories()
	{
		try 
		{	
			//check if list is empty
			if (stories.size()==0)
			{
				System.out.println ("No Stories Available.");
				System.out.println ("Create one: story create <storyId> <description>");
			}
			
			// there are stories in the list, print the out
			else 
			{
				Story tempStory;
				
				//iterate over the list an print the details
				for (String id : stories.keySet())
				{
					tempStory = stories.get(id);
					System.out.println (tempStory.getId() + "," +
										tempStory.getDescription() + "," +
										tempStory.getStatus());
				}//end of for loop
			}//end of else
		}//end of try block
		
		catch (Exception e) 
		{
			return;
		}
	}//end of method

//Task Related methods
	
	/**
	 * Create task with given description 
	 * 
	 * @param taskId		Id of the task to create 
	 * @param description	description of task to create or update
	 * @return 				true on success, otherwise false
	 */
	public boolean createTask (String storyId, String taskId, String description)
	{
		try
		{
			if (description != null && !description.isEmpty())
			{
				Task tempTask = new Task();
				if (stories.containsKey(storyId))
				{
					String newId = combineId(storyId, taskId);	
			
					//task exists already
					if (tasks.containsKey(newId))
					{
						System.out.println ("Task with given Id exists!overwrite?(y/n)");
						Scanner reader = new Scanner(System.in);
						String answer = reader.next();
						
						//user confirmed the removal
						if (answer.equalsIgnoreCase("y"))
						{			
							updateTask(storyId, taskId, description);
						}
						else
							System.out.println("Overwrite cancelled.");
					}						
				
					//task doesn't exist, create a new one
					else
					{
						tempTask.setId(newId);
					}				
					tempTask.setDescription(description);
					tempTask.setStatus(Status.TasksStatus.TODO);
					tasks.put(tempTask.getId(), tempTask);
					System.out.println ("Task created successfully");	
					return true;
				}

				//story doesn't exist
				else
				{
					System.out.println ("Story doen't exist.");
					System.out.println ("Run: story list - to see list of available stories");
					return false;
				}
			}
		}
		catch(Exception e)
		{
			return false;
		}
		return false;
	}
	
	/**
	 * Update the give task with new description
	 * 
	 * @param taskId		Id of the task to be updated with new description
	 * @param description	new description of the task 
	 * @return				true if update is successful, otherwise false
	 */
	public boolean updateTask (String storyId, String taskId,String description)
	{
		Task tempTask;
		String newId; 
		int exist; 
		try
		{
			newId = combineId(storyId, taskId);
			exist = doesExist(storyId, taskId);
			if (exist==1)
			{
				//get the task
				tempTask = tasks.get(newId);
				tempTask.setDescription(description);
				tempTask.setStatus(tempTask.getStatus());
				tasks.put(newId, tempTask);
				System.out.println("Task Updated Successfully.");
				return true;
			}
		}
		catch(Exception e)
		{
			return false;
		}
		return false;
	}
	
	/**
	 * Delete a task of a given story  
	 * 
	 * @param 	storyId		story id of the task we want to delete
	 * @param	taskId		task id of the task we want to delete
	 * @return 				deleted task on success, otherwise null
	 */
	public Task deleteTask(String storyId, String taskId)
	{
		try 
		{
			//combine the ids and check the task existence
			String newId = combineId(storyId, taskId);
			int exist = doesExist(storyId, taskId);
		
			//requested task of the given story exists
			if (exist==1)
			{
				//get user's input to proceed
				Scanner reader = new Scanner(System.in);
				System.out.println("Delete task\" "+taskId+"\" of story\""+storyId+"\"?(y/n)");
				String answer = reader.next();
				
				//user confirmed the removal
				if (answer.equalsIgnoreCase("y"))
				{			
					System.out.println("Task\" "+taskId+"\" of story \""+storyId+"\" deleted.");
					return tasks.remove(newId);
				}
				else
					System.out.println("Delete cancelled");
			}
			
			//story or task doesn't exist
			else 
			{
				System.out.println("Story, task, or both don't exist. check your input");
			}
		}
		catch (Exception e)
		{
			System.out.println ("Error in task removal. Error Message: " + e.getMessage());
		}
		
		//we should not reach here
		return null;
	}//end of method
	
	/**
	 * Move task to a new state 
	 * 
	 * @param 	storyId		story id of the task we want to move 
	 * @param	taskId		id of the task to be moved to new column 
	 * @param	newColumn	new column that the task needs to be moved to
	 * @return 				true if moving is successful, otherwise false 
	 */
	public boolean moveTask (String storyId, String taskId,Status.TasksStatus newColumn) 
	{
		
		Task tempTask;
		
		try
		{
			//check story and task existence and its value 
			if (storyId==null || storyId.isEmpty() || stories.size()==0 || !stories.containsKey(storyId) ||
				taskId ==null || taskId.isEmpty()  || tasks.size()==0   || newColumn==null)
			{
				return false;
			}
			//combine the ids and check for task existence
			String newId = combineId(storyId, taskId);	
			int exist = doesExist(storyId, taskId);
			
			//story and task exist, do the move
			if (exist==1)
			{
				tempTask = tasks.get(newId);
				
				//check for forward or backward move
				int result = tempTask.getStatus().compareTo(newColumn);
				
				//negative values means forward move
				if (result<0)
				{
					//check if forward move is possible 
					if (checkForwardMove(tempTask.getStatus(), newColumn))
					{				
						tempTask.setStatus(newColumn);
						tasks.put(newId, tempTask);		
						System.out.println("Task Moved Successfully.");
						return true;
					}
				}
				
				//positive values means backward move
				else if (result>0)
				{
					//check if backward move is possible
					if (checkBackwardMove(tempTask.getStatus(), newColumn))
					{
						tempTask.setStatus(newColumn);
						tasks.put(newId, tempTask);	
						System.out.println("Task Moved Successfully.");
						return true;
					}
				}
					return false;
			}//end of outer if
			else
			{
				System.out.println("either story or task does not exist");
				return false;
			}
		}//end of try block
		
		catch (Exception e)
		{
			return false;
		}
	}//end of method
	
	/**
	 * List available tasks of a given story id, it also check the tasks status
	 * 
	 * @param 	storyId		story id of the task we want to delete
	 * @param	which		if true passed in, then list the task otherwise checks the tasks sates
	 * @return 				true if all story's tasks are in DONE state, otherwise false
	 */
	public boolean listTasks (String storyId,boolean which)
	{
		boolean allTasksComplete=true;	
		Task tempTask;
		String[] ids;
		try
		{
			//check if list is empty
			if (tasks.size()==0)
			{
				System.out.println ("This story doesn't have any tasks.");
				System.out.println ("Create one: task create <storyId> <taskId> <description>");
				return true;
			}
			if (!stories.containsKey(storyId))
			{
				System.out.println ("Story doesn't exist.");
				return false;
			}			

			//iterate over the tasks list, and print them out
			for (String id : tasks.keySet())
			{
				//get tasks key values
				tempTask = tasks.get(id);
				
				//Separate them to get story and task ids
				ids=separateId(tempTask.getId());
				
				//check if the first separated id matches our story id 
				if (ids[0].equals(storyId))
				{
					//list the tasks? 
					if (which)
					{
						System.out.println (tempTask.getId() + "," +
											tempTask.getDescription() + "," +
											tempTask.getStatus());
					}
					
					//or just see if they are in DONE state
					else
					{
						if (tempTask.getStatus()!= Status.TasksStatus.DONE)
						{
							allTasksComplete=false;
						}
					}
				}//end of if 			
			}//end of for loop
			
		}//end of try block
		
		catch (Exception e)
		{
			System.out.println ("Error in listing tasks. Error Message: " + e.getMessage());
		}
		return allTasksComplete;
	}//end of method
		
	/**
	 * Combine story Id and Task Id to form a unique id
	 * 
	 * @param storyId	Id of the story
	 * @param taskId	Id of the task
	 * @return 			combined Ids i.e. storyId+"_"+askId
	 */
	private String combineId (String storyId, String taskId)
	{
		String combined;
		
		//concatenate 2 ids with _character in between 
		combined = storyId+"_"+taskId;
		return combined;
	}
	
	/**
	 * Separate combined Id to story Id and task Id
	 * 
	 * @param combinedI consist of storyId and task Id	Id of the story
	 * @return String[]	separated ids as n array
	 */
	private String[] separateId(String combinedId)
	{
		//split the combined id on the special character we combined them before
		String[] ids = combinedId.split("\\_");
		return ids;
	}
	
	/**
	 * check if story or task exist
	 * 
	 * @param storyId	Id of the story to check
	 * @param taskId	Id of the task to check
	 * @return 			0, if story does not exist
	 * 					1, story exists and task exists too
	 * 					2, story exist but task doesn't exist
	 */
	private int doesExist(String storyId, String taskId)
	{
		//combine ids and check its existence
		String idTosearch;
		idTosearch = combineId(storyId, taskId);
		
		//check if story exists
		if (!stories.containsKey(storyId))
		{
			return 0;
		}
		
		//story exist, check for existence of task
		else if (tasks.containsKey(idTosearch))
		{
			//task exists for the given story
			return 1;
		}	
		
		//story exist, but task doesn't
		else 
		{
			return 2;
		}
	}
		
	/**
	 * Delete all the tasks of a given story id 
	 * 
	 * @param 	storyId		story id we want to remove all of it task
	 * @return 				true if all removal is successful, otherwise false 
	 */
	private boolean deleteAllTasks(String storyId)
	{
		String[] ids;
		try
		{
			//return if there are no stories
			if (stories.size()==0)
			{
				return false;
			}
			
			//otherwise iterate over tasks list
			for (Iterator<String> i=tasks.keySet().iterator(); i.hasNext() ;)
			{
				String current = i.next();
				ids = separateId(current);
				
				//if it matches the story id, then remove it
				if (ids[0].equals(storyId))
				{
					i.remove();
				}
			}
			
			return true;
		}//end of try block
		
		catch (Exception e)
		{
			System.out.println ("Error in deleting tasks. Error Message: " + e.getMessage());
		}
		return false;
	}//end of method	
	
	/**
	 * Check forward move for a task of a story  
	 * 
	 * @param 	oldStatus	current status of the task  
	 * @param	newStatus	status to move to to  
	 * @return 				true if move is possible, otherwise false 
	 */
	private boolean checkForwardMove(Status.TasksStatus  oldStatus, Status.TasksStatus newStatus)
	{
		try 
		{
			if (oldStatus==null || newStatus ==null)
			{
				return false;
			}
		
			int result = oldStatus.compareTo(newStatus);
			//move is possible
			if (result==-1)
			{
				return true;
			}
			//old status and new status are the same
			else if (result==0)
			{
				System.out.println ("Task is already in the requested state. Run: task list <story id> or help");
				return false;
			}
			//move is not possible
			else 
			{
				System.out.println ("Error! Moving task to new state is not possible.");
				return false;
			}
		}//end of try block
		catch (Exception e)
		{
			System.out.println ("Error in moving task. Error Message: " + e.getMessage());
		}
		return false;
	}//end of method
	
	/**
	 * Check backward move for a task of a story  
	 * 
	 * @param 	oldStatus	current status of the task  
	 * @param	newStatus	status to move to to  
	 * @return 				true if move is possible, otherwise false 
	 */
	private boolean checkBackwardMove(Status.TasksStatus  oldStatus, Status.TasksStatus newStatus)
	{
		try 	
		{
			if (oldStatus==null || newStatus ==null)
			{
				return false;
			}
			//can't move back from initial or final state (from TODO or DONE to previous states)
			if ((oldStatus.compareTo(Status.TasksStatus.DONE)==0) || 
			    (oldStatus.compareTo(Status.TasksStatus.TODO)==0))
			{
				System.out.println("Move not possible, task is inital/final states can't be moved back.");
				System.out.println("Run \"help\" command for more information");
				return false;
			}
			
			//old state and new state are the same 
			else if (oldStatus.compareTo(newStatus)==0)
			{
				System.out.println ("Task is already in the requested state. Run: task list <story id> or help");
				return false;
			}
			
			//move is possible
			else
			{
				return true;
			}
		}//end of try block
		
		catch (Exception e)
		{
			System.out.println ("Error in moving task. Error Message: " + e.getMessage());
		}
		
		return false;		
	}//end of method
	
	/**
	 * Print out list of available operations  
	 * 
	 * @param 	N/A		
	 * @return 	N/A			
	 */
	public void printCommands()
	{
		System.out.println ("Available operations:");
		System.out.println ("story create <storyId> <description>");
		System.out.println ("story list");
		System.out.println ("story delete <storyId>");
		System.out.println ("story complete <storyId>");
		System.out.println ("task create <storyId> <taskId> <description>");
		System.out.println ("task list <storyId>");
		System.out.println ("task delete <storyId> <taskId>");
		System.out.println ("task move <storyId> <taskId> <new column>");
		System.out.println ("task update <storyId> <taskId> <new description>");
		System.out.println ("quite - to exit the program");
		System.out.println ("help - to see this list ");
	}
	
	/**
	 * convert the status integer value to text  
	 * 
	 * @param 	status	status of the task (1,2,3,4)		
	 * @return 	N/A			
	 */
	public Status.TasksStatus convertTasksStatus(String status)
	{
		//check if given status 
		if (status ==null || status.isEmpty())
		{
			return null;
		}
		
		//check the input and return the proper status
		if (status.equals("1"))
			return Status.TasksStatus.TODO;
		else if (status.equals("2"))
			return Status.TasksStatus.IN_PROCESS;
		else if (status.equals("3"))
			return Status.TasksStatus.TO_VERIFY;
		else if (status.equals("4"))
			return Status.TasksStatus.DONE;
		else 
			return null;
	}

	/**
	 * free resources  
	 * 
	 * @param 	N/A		
	 * @return 	N/A			
	 */
	public void freeAll()
	{
		stories = null;
		tasks = null;
	}

}//end of class
