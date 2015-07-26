/**
 * @author Internet's own boy
 */

import java.util.*;

public class Demo 
{

	public static void main(String[] args) 
	{		
		//to hold the board
		Board currentBoard = new Board();
		
		//print out the available operations
		currentBoard.printCommands();
		
		//to get user inputs
		Scanner inputScanner = new Scanner(System.in);
		
		String[] userInput=null;
		String command=null;
		
		//keep getting user input
		while (true)
		{
			command = inputScanner.nextLine();
			
			//if user wants to quit
			if (command.startsWith("quit"))
			{
				System.out.println("Exisiting Board Manager.");
				inputScanner.close();
				currentBoard.freeAll();
				currentBoard = null;
				return;
			}
			
			//split the user inputs on space
			userInput = command.split(" +");
			
			//commands either start with "story" or "task" except "help" and "quit" command
			
			//check for story related commands
			if (command.startsWith("story"))
			{
				// create story command
				if (userInput[1].equalsIgnoreCase("create"))
				{
					String description="";
					
					if (userInput.length>3)
					{
						//to get the story description
						for (int i=3; i<userInput.length;i++)
						{
							description = description + " "+userInput[i];						
						}
						
						//create the story 
						if (!currentBoard.createStory(userInput[2],description))
						{
							System.out.println ("Usage: story <storyId> <description>");						
						}						
					}
					else
					{
						System.out.println ("Invalid arguments! Usage: story <storyId> <description>");
					}
				}
				
				//delete story command
				else if (userInput[1].equalsIgnoreCase("delete"))
				{				
					if (!currentBoard.deleteStory(userInput[2]))
					{
						System.out.println ("Usage: story delete <storyId>");
					}
				}
				
				//list story command
				else if (userInput[1].equalsIgnoreCase("list"))
				{
					currentBoard.listStories();
				}
				
				//complete story command
				else if (userInput[1].equalsIgnoreCase("complete"))
				{
					if (!currentBoard.completeStory(userInput[2]))
					{
						System.out.println ("Usage: story list");
					}
				}
				
				//invalid command
				else
				{
					System.out.println ("Invalid story command. run \"help\"");
				}
			}//end of story related commands
			
			//Task related commands
			else if (command.startsWith("task"))
			{
				//create task command
				if (userInput[1].equalsIgnoreCase("create"))
				{
					String description="";
					
					//to get task description 
					for (int i=4; i<userInput.length;i++)
					{
						description = description + " "+userInput[i];						
					}
					
					if (!currentBoard.createTask(userInput[2],userInput[3], description))
					{
						System.out.println ("Usage: task <storyId> <taskId> <descrption>");
					}
				}
				
				// delete task command
				else if (userInput[1].equalsIgnoreCase("delete"))
				{
					if (currentBoard.deleteTask(userInput[2], userInput[3])!=null)
					{
						System.out.println ("Usage: task delete <storyId> <taskId>");
					}
				}
				
				//list tasks of a story command 
				else if (userInput[1].equalsIgnoreCase("list"))
				{	
					if (userInput.length != 3)
					{
						System.out.println ("Error! Usage: task list <storyId>");
						System.out.println ("Run \"help\" command to see the available operations");
					}
					else
					{
						if (!currentBoard.listTasks(userInput[2], true))
						{
							System.out.println ("Usage: task list <storyId>");
						}
					}
				}
				
				//task move command
				else if (userInput[1].equalsIgnoreCase("move"))
				{
					//check the requested status is valid
					Status.TasksStatus newStatus =  currentBoard.convertTasksStatus(userInput[4]);
					if (newStatus!= null)
					{
						if (!currentBoard.moveTask(userInput[2], userInput[3], newStatus))
						{
							System.out.println ("Usage: task move <storyId> <taskId> <new column>");
						}	
					}
				}
				
				//task update command
				else if (userInput[1].equalsIgnoreCase("update"))
				{					
					String description="";
					
					//to get task description 
					for (int i=4; i<userInput.length;i++)
					{
						description = description + " "+userInput[i];						
					}
					
					if (!currentBoard.updateTask(userInput[2],userInput[3], description))
					{
						System.out.println ("Error! Updating following task faild.");
						System.out.println ("Task Id: " +userInput[3] + " Story Id:"+userInput[2]);
						System.out.println ("Usage: task update <storyId> <taskId> <new cdescription>");
					}
				}
				
				//invalid command
				else
				{
					System.out.println ("Invalid task command. run \"help\"");
				}
			}//end of task related commands
			
			//to print out help
			else if (command.startsWith("help"))
			{
				currentBoard.printCommands();
			}
			
			//for other unsupported inputs
			else 
			{
				System.out.println ("Error! Not supported command or bad input");
				System.out.println ("Run \"help\" command to see the available operations");
			}
		}//end of while loop
	}//end of main method

}
