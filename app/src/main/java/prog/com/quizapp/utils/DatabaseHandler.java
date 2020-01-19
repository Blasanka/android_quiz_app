package prog.com.quizapp.utils;
/*---------------------o----------o----------------------
 * Created by Blasanka on 18,January,2020
 * Contact: blasanka95@gmail.com
 *-------------------------<>----------------------------*/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import prog.com.quizapp.R;

public class DatabaseHandler {
    private static final String TAG = "DatabaseHandler";
    private final Context mContext;
    private SQLiteDbHelper mDbHelper;
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        this.mContext = context;
        mDbHelper = new SQLiteDbHelper(context);
    }

    public Cursor getValuesFromDb(String levelName) {
        db = mDbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                QuizContract.QuestionEntry.COL_QUESTION,
                QuizContract.QuestionEntry.COL_ANSWER_A,
                QuizContract.QuestionEntry.COL_ANSWER_B,
                QuizContract.QuestionEntry.COL_ANSWER_C,
                QuizContract.QuestionEntry.COL_ANSWER_D,
                QuizContract.QuestionEntry.COL_CORRECT_ANS
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = QuizContract.QuestionEntry.COL_LEVEL_NAME + " = ?";
        String[] selectionArgs = {levelName};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = BaseColumns._ID + " ASC";

        return db.query(
                QuizContract.QuestionEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder             // The sort order
        );
    }

    public int getScoreFromDb(String selectName) {
        db = mDbHelper.getReadableDatabase();
        int selection = -1;
        Cursor c = db.query(
                QuizContract.ScoreEntry.TABLE_NAME, new String[]{selectName},
                null, null, null, null, null);
        if (c.moveToFirst()) selection = c.getInt(c.getColumnIndex(selectName));
        c.close();
        db.close();
        Log.d("getScoreFromDb", selectName + "=" + selection);
        return selection;
    }

    public long saveScoreToDb(double scores, String levelName) {
        db = mDbHelper.getWritableDatabase();

        // Storing level scores into database to later access and give level access
        // Create a new map of values, where column names are the keys

        ContentValues values = new ContentValues();
        if (levelName.equals(mContext.getString(R.string.level_one_label)))
            values.put(QuizContract.ScoreEntry.COL_LEVEL_ONE_SCORE, scores);
        else if (levelName.equals(mContext.getString(R.string.level_two_label)))
            values.put(QuizContract.ScoreEntry.COL_LEVEL_TWO_SCORE, scores);
        else values.put(QuizContract.ScoreEntry.COL_LEVEL_THREE_SCORE, scores);

        values.put(QuizContract.ScoreEntry.COL_TOTAL_SCORE, scores);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(QuizContract.ScoreEntry.TABLE_NAME, null, values);
        Log.d(TAG, "saveScores: saved to SQLite database: newRowId is: " + newRowId);
        db.close();
        return newRowId;
    }

    public long updateScoreOnDb(double scores, String levelName) {
        db = mDbHelper.getWritableDatabase();

        // Storing level scores into database to later access and give level access
        // Create a new map of values, where column names are the keys

        ContentValues values = new ContentValues();
        if (levelName.equals(mContext.getString(R.string.level_one_label)))
            values.put(QuizContract.ScoreEntry.COL_LEVEL_ONE_SCORE, scores);
        else if (levelName.equals(mContext.getString(R.string.level_two_label)))
            values.put(QuizContract.ScoreEntry.COL_LEVEL_TWO_SCORE, scores);
        else if (levelName.equals(mContext.getString(R.string.level_three_label)))
            values.put(QuizContract.ScoreEntry.COL_LEVEL_THREE_SCORE, scores);

        values.put(QuizContract.ScoreEntry.COL_TOTAL_SCORE, scores);

        // Update the score, returning the primary key value of the new row
        int count = db.update(
                QuizContract.ScoreEntry.TABLE_NAME,
                values,
                null,
                null);

        Log.d(TAG, "updateScoreOnDb: updated to SQLite database: count is: " + count);
        db.close();
        return count;
    }

    public int deleteScoreOnDb() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Issue SQL statement and return affected count.
        return db.delete(QuizContract.ScoreEntry.TABLE_NAME, "1", null);
    }
    
    public void closeDatabase() {
        db.close();
        Log.d(TAG, "closeDatabase: closed!");
    }
}
