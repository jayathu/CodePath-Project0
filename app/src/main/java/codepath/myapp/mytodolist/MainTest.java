package codepath.myapp.mytodolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.sql.Date;
import java.util.Arrays;

import codepath.myapp.mytodolist.TaskItem;
import codepath.myapp.mytodolist.data.Task;

public class MainTest extends AppCompatActivity {

    private TaskItemArrayAdapter taskArrayAdapter;

    TaskItem[] tasks = {
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1"),
            new TaskItem("Task One", "Description for task one goes here", "1")


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        taskArrayAdapter = new TaskItemArrayAdapter(this, Arrays.asList(tasks));

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView)findViewById(R.id.listview_tasks);
        listView.setAdapter(taskArrayAdapter);
    }
}
