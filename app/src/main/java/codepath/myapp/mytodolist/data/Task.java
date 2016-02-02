package codepath.myapp.mytodolist.data;

import java.util.Date;

/**
 * Created by jnagaraj on 1/25/16.
 */


public class Task {

    public int id;
    public String title;
    public String description;
    public String remainingDays;
    public int colorVal;

    public Task(String t, String desc, String daysLeft, int cVal )
    {
        title = t;
        description = desc;
        remainingDays = daysLeft;
        colorVal = cVal;
    }

}

