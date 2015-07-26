/**
 * @author Internet's own boy
 */

/*
 * class to represent status of tasks and stories on the board
 * task can be in 4 states To do, In Process, To Verify, or Done
 * story are either In Process or Completed state
 */
public class Status
{
	/* Task status */
	public enum TasksStatus
	{
		TODO(0),IN_PROCESS(1),TO_VERIFY(2),DONE(3);
		
		private int value;
		private TasksStatus (int value)
		{
			this.value=value;
		}
	}
	
	/* Story status */
	public enum StoryStatus
	{
		IN_PROCESS(0),COMPLETED(1);
		
		private int value;
		private StoryStatus (int value)
		{
			this.value=value;
		}
	}
}


