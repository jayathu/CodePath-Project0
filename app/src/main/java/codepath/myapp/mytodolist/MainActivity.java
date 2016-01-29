package codepath.myapp.mytodolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codepath.myapp.mytodolist.data.Task;
import codepath.myapp.mytodolist.data.ToDoDbHelper;

public class MainActivity extends ActionBarActivity {

    ToDoDbHelper databaseHelper;
    EditText editText;

    public final static String EXTRA_MESSAGE = "codepath.myapp.mytodolist.MESSAGE";
    public final static String EXTRA_MESSAGE_ID = "codepath.myapp.mytodolist.MESSAGE_ID";

    private TaskArrayAdapter taskArrayAdapter;

    ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        tasks = new ArrayList<>();
        taskArrayAdapter = new TaskArrayAdapter(this, tasks);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView)findViewById(R.id.listview_tasks);
        listView.setAdapter(taskArrayAdapter);

        editText = (EditText)findViewById(R.id.edTextTest);

        databaseHelper = ToDoDbHelper.getsInstance(this);
        populateListItems();

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateListItems() {

        readItemsFromDb();
    }

    public void onAddItem(View view) {

        Task task = new Task(editText.getText().toString(), "The task has been modified", "1");
        taskArrayAdapter.add(task);
        editText.setText("");
        writeItemsToDb();
    }

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
