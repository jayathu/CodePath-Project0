package codepath.myapp.mytodolist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jnagaraj on 1/25/16.
 */
public class ToDoDbHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = ToDoDbHelper.class.toString();
    private static ToDoDbHelper sInstance;

    //Use the application context, which will ensure that you don't actually
    // leak any Activity's context.

    public static synchronized ToDoDbHelper getsInstance(Context context) {
        if(sInstance == null) {
            sInstance = new ToDoDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */

    private ToDoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Database Info
    private static final String DATABASE_NAME = "todoDatabase";
    private static final int DATABASE_VERSION = 2;

    //Table Names
    private static final String TABLE_TASKS = "tasks";

    //Task Table Columns
    private static final String KEY_TASK_ID = "_id";
    private static final String TASK_TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String REMAINING_DAYS = "remainingDays";
    private static final String TASK_COLOR = "taskColor";

    //Called when the database connection is being configured.
    //Configure database settings for things like foreign key suppert, write-ahead logging etc

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    //Called when the database is created for the FIRST time.
    //If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS +
                "(" +
                KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TASK_TITLE + " TEXT," +
                DESCRIPTION + " TEXT," +
                REMAINING_DAYS + " TEXT," +
                TASK_COLOR + " INTEGER" +
                ")";
        db.execSQL(CREATE_TASKS_TABLE);

        Log.v(LOG_TAG, "Database TASK was created");
    }

    //Called when the database needs to be upgraded.
    //This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    //but the DATABASE_VERSION is different than the version of the database that exists on disk.

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }

    public void addTask(Task task)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(TASK_TITLE, task.title);
            values.put(DESCRIPTION, task.description);
            values.put(REMAINING_DAYS, task.remainingDays);
            values.put(TASK_COLOR, task.colorVal);

            db.insertOrThrow(TABLE_TASKS, null, values);
            db.setTransactionSuccessful();
        }catch(Exception e){
            Log.d(LOG_TAG, "Error while trying to add task to database " + e.toString());
        }finally {
            db.endTransaction();
        }
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        String TASKS_SELECT_QUERY =
                String.format("SELECT * FROM TASKS", TABLE_TASKS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TASKS_SELECT_QUERY, null);
        try {
            if(cursor.moveToFirst()) {
                do {
                    //Task newTask = new Task();
                    String title = cursor.getString(cursor.getColumnIndex(TASK_TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
                    String remainingDays = cursor.getString(cursor.getColumnIndex(REMAINING_DAYS));
                    int taskColor = cursor.getInt(cursor.getColumnIndex(TASK_COLOR));

                    tasks.add(new Task(title, description, remainingDays, taskColor));
                } while (cursor.moveToNext());
            }
            }catch (Exception e) {
                Log.v(LOG_TAG, "Error while trying to get posts from database");
            }finally {
                if(cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return tasks;
        }

    public void deleteAllTasks() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_TASKS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(LOG_TAG, "Error while trying to delete all tasks");
        } finally {
            db.endTransaction();
        }
    }

    /*
    public void removeTask(Task task) {

        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_TASKS, "WHERE KEY_TASK_ID = ?", new String[] { String.valueOf(task.id) });
    }

    public void updateTask(int index, String title) {

        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(TASK_TITLE, title);
        //values.put(DESCRIPTION, task.description);
        //values.put(TASK_INDEX, task.index);

        db.update(TABLE_TASKS, values, KEY_TASK_ID + " = ?", new String[]{String.valueOf(task.id)});
    }*/

}

