package prog.com.quizapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import static prog.com.quizapp.utils.QuizContract.SQL_CREATE_QUIZ_ENTRIES;
import static prog.com.quizapp.utils.QuizContract.SQL_CREATE_SCORE_ENTRIES;
import static prog.com.quizapp.utils.QuizContract.SQL_DELETE_QUIZ_ENTRIES;
import static prog.com.quizapp.utils.QuizContract.SQL_DELETE_SCORE_ENTRIES;
import static prog.com.quizapp.utils.QuizContract.SQL_QUESTION_ENTRY;

public class SQLiteDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteDbHelper";

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Quiz.db";

    SQLiteDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_QUIZ_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_SCORE_ENTRIES);

        Log.d(TAG, "onCreate: tables created!");

        for (String s : SQL_QUESTION_ENTRY) {
            sqLiteDatabase.execSQL(s);
        }

        Log.d(TAG, "onCreate: inserted!");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_QUIZ_ENTRIES);
        db.execSQL(SQL_DELETE_SCORE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
