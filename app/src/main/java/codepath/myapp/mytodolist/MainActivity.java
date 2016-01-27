package codepath.myapp.mytodolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import java.util.List;

import codepath.myapp.mytodolist.data.Task;
import codepath.myapp.mytodolist.data.ToDoDbHelper;

public class MainActivity extends AppCompatActivity {

    ToDoDbHelper databaseHelper;
    ArrayList<String> listItems;
    ArrayAdapter<String> listItemsAdapter;

    ListView listView;
    EditText editText;

    public final static String EXTRA_MESSAGE = "codepath.myapp.mytodolist.MESSAGE";
    public final static String EXTRA_MESSAGE_ID = "codepath.myapp.mytodolist.MESSAGE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.lvItems);
        editText = (EditText)findViewById(R.id.edText);

        databaseHelper = ToDoDbHelper.getsInstance(this);
        populateListItems();

        listView.setAdapter(listItemsAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listItems.remove(position);
                listItemsAdapter.notifyDataSetChanged();
                //writeItems();
                writeItemsToDb();
                return true;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                String message = listItems.get(position);
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
            listItems.add(index, message);
            listItems.remove(index + 1);
            listItemsAdapter.notifyDataSetChanged();
            //writeItems();
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

        listItems = new ArrayList<String>();

        //readItems();
        readItemsFromDb();

        listItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems );
    }

    public void onAddItem(View view) {

        listItemsAdapter.add(editText.getText().toString());
        editText.setText("");
        //writeItems();
        writeItemsToDb();
    }

    private void readItemsFromDb() {

        List<Task> tasks = databaseHelper.getAllTasks();
        try {
            for(Task task : tasks) {
                listItems.add(task.description);
            }
        }catch (Exception e) {
            listItems = new ArrayList<>();
        }
    }

    private void writeItemsToDb() {

        databaseHelper.deleteAllTasks();
        for(int i = 0; i < listItems.size(); i++)
        {
            Task task = new Task();
            task.index = i;
            task.description = listItems.get(i);
            databaseHelper.addTask(task);
        }
    }

    /*
    private void readItems()
    {
        File fileDir = getFilesDir();
        File file = new File(fileDir, "todo.txt");
        try{
            listItems = new ArrayList<String>(FileUtils.readLines(file));
        }catch (IOException e) {
            listItems = new ArrayList<String>();
        }
    }


    private void writeItems()
    {
        File fileDir = getFilesDir();
        File file = new File(fileDir, "todo.txt");
        try{
            FileUtils.writeLines(file, listItems);
        }catch (IOException e) {
                e.printStackTrace();
        }
    }*/


}
