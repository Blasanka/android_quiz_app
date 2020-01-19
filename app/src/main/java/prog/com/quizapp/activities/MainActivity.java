package prog.com.quizapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import prog.com.quizapp.R;
import prog.com.quizapp.utils.DatabaseHandler;

public class MainActivity extends AppCompatActivity {

    TextView quizBt;
    private Context mContext = MainActivity.this;
    private static final String TAG = "MainActivity";

    DatabaseHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quizBt = findViewById(R.id.quizBt);
        quizBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, LevelsActivity.class);
            startActivity(intent);
            }
        });

        mHandler = new DatabaseHandler(mContext);
        createAndResetDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        createAndResetDatabase();
    }

    private void createAndResetDatabase() {
        Log.d(TAG, "onCreate: delete store db rows: " + mHandler.deleteScoreOnDb());
        mHandler.saveScoreToDb(0, getString(R.string.level_one_label));
        mHandler.saveScoreToDb(0, getString(R.string.level_two_label));
        long newRowId = mHandler.saveScoreToDb(0, getString(R.string.level_three_label));
        mHandler.saveScoreToDb(0, getString(R.string.total_label).toLowerCase());
        Log.d(TAG, "onCreate: new row id " + newRowId);
        mHandler.closeDatabase();
    }
}
