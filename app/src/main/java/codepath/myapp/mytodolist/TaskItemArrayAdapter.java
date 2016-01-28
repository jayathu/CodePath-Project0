package codepath.myapp.mytodolist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import codepath.myapp.mytodolist.data.Task;
import codepath.myapp.mytodolist.TaskItem;

/**
 * Created by jnagaraj on 1/27/16.
 */
public class TaskItemArrayAdapter extends ArrayAdapter<TaskItem> {

    private static final String LOG_TAG = TaskItemArrayAdapter.class.getSimpleName();

    public TaskItemArrayAdapter(Activity context, List<TaskItem> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskItem task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item_detail, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.list_task_title_textview);
        titleTextView.setText(task.title);

        TextView descTextView = (TextView) convertView.findViewById(R.id.list_task_description_textview);
        descTextView.setText(task.description);

        TextView daysLeftTextView = (TextView) convertView.findViewById(R.id.list_date_numeric_textview);
        daysLeftTextView.setText(task.remainingDays);

        TextView daysDescTextView = (TextView) convertView.findViewById(R.id.list_date_desc_textview);
        daysDescTextView.setText("days");
        return convertView;
    }
}
