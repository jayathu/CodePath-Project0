package codepath.myapp.mytodolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import codepath.myapp.mytodolist.data.Task;
import codepath.myapp.mytodolist.data.ToDoDbHelper;

public class AddTaskActivity extends AppCompatActivity{

    private DatePickerDialog datePicker;
    DatePickerDialog datePickerDialog;
    int taskColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_task);

        fillOutSpinner();

        datePickerDialog = new DatePickerDialog(
                this, R.style.MyDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                TextView textView = (TextView)findViewById(R.id.textViewDate);
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();
                int year = datePicker.getYear();

                textView.setText(month + "/" + day + "/" + year);

            }
        }, 2016 , 1, 0);


    }

    public void onSetDate(View view) {
        datePickerDialog.show();

    }

    public void onSetTaskColor_Pink(View view) {

        taskColor = Color.parseColor("#E91E63");
    }

    public void onSetTaskColor_Teal(View view) {


        taskColor =  Color.parseColor("#009688");
    }

    public void onSetTaskColor_Amber(View view) {


        taskColor =  Color.parseColor("#FFC107");
    }

    public final static String EDITED_TITLE = "codepath.myapp.mytodolist.EDITED_TITLE";
    public final static String EDITED_DESC = "codepath.myapp.mytodolist.EDITED_DESC";
    public final static String EDITED_TASK_TYPE = "codepath.myapp.mytodolist.EDITED_TASK_TYPE";
    public final static String EDITED_TASK_COLOR = "codepath.myapp.mytodolist.EDITED_TASK_COLOR";
    public final static String EDITED_DAYS_LEFT = "codepath.myapp.mytodolist.EDITED_DAYS_LEFT";

    public void onSave(View view)
    {
        EditText editTextTitle = (EditText)findViewById(R.id.editTextName);
        String title = editTextTitle.getText().toString();

        EditText editTextDesc = (EditText)findViewById(R.id.editTextDesc);
        String description = editTextDesc.getText().toString();

        Spinner spinner = (Spinner) findViewById(R.id.spinnerCategory);
        String taskType = spinner.getSelectedItem().toString();

        TextView textView = (TextView)findViewById(R.id.textViewDate);
        String dueDate = textView.getText().toString();


        Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
        intent.putExtra(EDITED_TITLE, title);
        intent.putExtra(EDITED_DESC, description);
        intent.putExtra(EDITED_TASK_TYPE, taskType);
        intent.putExtra(EDITED_TASK_COLOR, taskColor);
        startActivity(intent);
    }

    private void fillOutSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.task_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }
}
