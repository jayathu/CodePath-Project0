package codepath.myapp.mytodolist;

/**
 * Created by jnagaraj on 1/27/16.
 */
public class TaskItem {


    public long id;
    public int index;
    public String title;
    public String description;
    public String remainingDays;

    public TaskItem(String t, String desc, String daysLeft )
    {
        title = t;
        description = desc;
        remainingDays = daysLeft;
    }
}