package codepath.myapp.mytodolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    public final static String EDITED_RESULT = "codepath.myapp.mytodolist.EDITED_RESULT";
    public final static String EDITED_RESULT_ID = "codepath.myapp.mytodolist.EDITED_RESULT_ID";

    public int listIndex;
    EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        listIndex = intent.getIntExtra(MainActivity.EXTRA_MESSAGE_ID, 0);

        etText = (EditText)findViewById(R.id.edEditText);
        etText.setText(message);
        Selection.setSelection(etText.getText(), etText.getText().length());

    }

    public void editItemName(View view)
    {
        Intent intent = new Intent();

        intent.putExtra(EDITED_RESULT, etText.getText().toString());

        intent.putExtra(EDITED_RESULT_ID, listIndex);
        setResult(2, intent);
        finish();
    }
}
