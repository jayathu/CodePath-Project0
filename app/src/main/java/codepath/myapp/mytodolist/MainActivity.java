package codepath.myapp.mytodolist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import codepath.myapp.mytodolist.data.Task;
import codepath.myapp.mytodolist.data.ToDoDbHelper;

public class MainActivity extends ActionBarActivity {

    ToDoDbHelper databaseHelper;

    public final static String EXTRA_MESSAGE = "codepath.myapp.mytodolist.MESSAGE";
    public final static String EXTRA_MESSAGE_ID = "codepath.myapp.mytodolist.MESSAGE_ID";

    private TaskArrayAdapter taskArrayAdapter;

    ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks = new ArrayList<>();
        taskArrayAdapter = new TaskArrayAdapter(this, tasks);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) findViewById(R.id.listview_tasks);
        listView.setAdapter(taskArrayAdapter);

        databaseHelper = ToDoDbHelper.getsInstance(this);

        Intent intent = getIntent();

        if(intent != null) {
            String title = intent.getStringExtra(AddTaskActivity.EDITED_TITLE);
            String desc = intent.getStringExtra(AddTaskActivity.EDITED_DESC);

            int taskColor = intent.getIntExtra(AddTaskActivity.EDITED_TASK_COLOR, Color.parseColor("#FFC107"));
            Task task = new Task(title, desc, "1", taskColor);
            databaseHelper.addTask(task);
            populateListItems();

        }else {

            populateListItems();
        }
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    tasks.remove(position);
                    taskArrayAdapter.notifyDataSetChanged();
                    writeItemsToDb();
                    return true;
                }
            });


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                    String message = tasks.get(position).title;
                    intent.putExtra(EXTRA_MESSAGE, message);
                    intent.putExtra(EXTRA_MESSAGE_ID, position);
                    startActivityForResult(intent, 2);  //TODO:2 = resultCode ?? Why? See Documentation

                }
            });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        if(requestCode==2)
        {
            String message = data.getStringExtra(EditItemActivity.EDITED_RESULT);
            int index = data.getIntExtra(EditItemActivity.EDITED_RESULT_ID, 0);
            Task task = tasks.get(index);
            task.title = message;

            tasks.add(index, task);
            tasks.remove(index + 1);
            taskArrayAdapter.notifyDataSetChanged();
            writeItemsToDb();
        }
        */

        if(requestCode==2)
        {
            String title = data.getStringExtra(AddTaskActivity.EDITED_TITLE);
            String desc = data.getStringExtra(AddTaskActivity.EDITED_DESC);
            int taskColor = data.getIntExtra(AddTaskActivity.EDITED_TASK_COLOR, Color.parseColor("#FFC107"));

            Task task = new Task(title, desc, "3", taskColor);

            tasks.add(task);
            taskArrayAdapter.notifyDataSetChanged();
            writeItemsToDb();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_add_task:
                launchAddTaskActivity();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void populateListItems() {

        readItemsFromDb();
    }

    private void launchAddTaskActivity() {
        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);

        startActivity(intent);
    }

    /*
    public void onAddItem(View view) {

        Task task = new Task(editText.getText().toString(), "The task has been modified", "1");
        taskArrayAdapter.add(task);
        editText.setText("");
        writeItemsToDb();
    }*/

    private void readItemsFromDb() {

        List<Task> tempTasks = databaseHelper.getAllTasks();
        try {
            for(Task task : tempTasks) {
                tasks.add(task);
            }
        }catch (Exception e) {
            tasks = new ArrayList<>();
        }
    }

    private void writeItemsToDb() {

        databaseHelper.deleteAllTasks();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            databaseHelper.addTask(task);
        }
    }


}
