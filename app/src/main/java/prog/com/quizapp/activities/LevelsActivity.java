package prog.com.quizapp.activities;
/*---------------------o----------o----------------------
 * Created by Blasanka on 08,January,2020
 * Contact: blasanka95@gmail.com
 *-------------------------<>----------------------------*/

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import prog.com.quizapp.R;
import prog.com.quizapp.utils.CalculateScore;
import prog.com.quizapp.utils.DatabaseHandler;

public class LevelsActivity extends AppCompatActivity {

    private static final String TAG = "LevelsActivity";

    LinearLayout mLinearLayout;
    RelativeLayout relLayout;
    View levelTwoBtLinearLayout;
    View levelThreeBtLinearLayout;
    TextView levelOneBt;
    TextView levelTwoTv;
    TextView levelThreeTv;

    // To collect selected answers and calculate scores
    CalculateScore mCalculateScore;
    // SQLite database
    private DatabaseHandler mDbHandler;
    private Context mContext = LevelsActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHandler = new DatabaseHandler(mContext);
        mCalculateScore = new CalculateScore();

        mLinearLayout = initLayout();

        setupLevelTv(mLinearLayout, levelTwoBtLinearLayout, levelTwoTv, R.string.level_two_label, R.string.level_one_label);
        setupLevelTv(mLinearLayout, levelThreeBtLinearLayout, levelThreeTv, R.string.level_three_label, R.string.level_two_label);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called...");
        getSQLiteScores(getString(R.string.level_one_label), R.string.level_one_label, R.string.level_two_label, levelTwoBtLinearLayout, levelTwoTv);
        getSQLiteScores(getString(R.string.level_two_label), R.string.level_two_label, R.string.level_three_label, levelThreeBtLinearLayout, levelThreeTv);
    }

    private LinearLayout initLayout() {
        relLayout = findViewById(R.id.relLayout);

        levelTwoBtLinearLayout = LayoutInflater.from(this).inflate(R.layout.layout_level_button, null);
        levelThreeBtLinearLayout = LayoutInflater.from(this).inflate(R.layout.layout_level_button, null);
        levelTwoTv = levelTwoBtLinearLayout.findViewById(R.id.levelBtLocked);
        levelThreeTv = levelThreeBtLinearLayout.findViewById(R.id.levelBtLocked);

        LinearLayout linearLayout = new LinearLayout(this);
        int linearLayoutId = View.generateViewId();
        linearLayout.setId(linearLayoutId);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(params);
        params.setMargins(0, 0, 0, 16);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        ImageView backBt = new ImageView(this);
        int backBtId = View.generateViewId();
        backBt.setId(backBtId);
        backBt.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        backBt.setX(20);
        backBt.setY(20);
        backBt.setImageResource(R.drawable.ic_back_arrow);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        relLayout.addView(backBt, 0);
        relLayout.addView(linearLayout, 1);

        levelOneBt = findViewById(R.id.quizBt);
        levelOneBt.setText(getString(R.string.level_one_label));
        levelOneBt.setTextSize(22);
        levelOneBt.setGravity(Gravity.START);
        levelOneBt.setPadding(26, 12, 26, 12);
        levelOneBt.setBackgroundResource(R.drawable.button_border_white);
        levelOneBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelsActivity.this, QuizActivity.class);
                intent.putExtra("level", getString(R.string.level_one_label));
                startActivity(intent);
            }
        });
        relLayout.removeView(levelOneBt);
        linearLayout.addView(levelOneBt);
        return linearLayout;
    }

    private void setupLevelTv(LinearLayout linearLayout, View levelBtLinearLayout, TextView levelTv, int currentLevel, final int previousLevel) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(650, LinearLayout.LayoutParams.WRAP_CONTENT);
        levelBtLinearLayout.setLayoutParams(params);
        params.setMargins(0, 0, 0, 24);
        levelBtLinearLayout.setPadding(26, 12, 26, 12);
        linearLayout.addView(levelBtLinearLayout);

        getSQLiteScores(getString(R.string.level_one_label), previousLevel, currentLevel, levelBtLinearLayout, levelTv);
        levelTv.setText(getString(currentLevel));
    }

    private void getSQLiteScores(final String levelName, final int previousLevel, final int currentLevel, final View levelBtLinearLayout, TextView levelTv) {
        // Storing level scores into database to later access and give level access
        try {
            int score = mDbHandler.getScoreFromDb(compressLevelName(levelName));
            mCalculateScore.setScores(score);
            Log.d(TAG, "getScoreFromDb: score is: " + score);
            if (mCalculateScore.isLevelPassed(getString(previousLevel))) {
                Log.d(TAG, "getSQLiteScores: level is passed");
                levelTv.setTextColor(Color.WHITE);
                levelBtLinearLayout.setBackgroundResource(R.drawable.button_border_white);
                ImageView iv = levelBtLinearLayout.findViewById(R.id.lockedIcon);
                iv.setVisibility(View.GONE);
                levelTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LevelsActivity.this, QuizActivity.class);
                        intent.putExtra("level", getString(currentLevel));
                        startActivity(intent);
                    }
                });
            } else {
                int color = ContextCompat.getColor(mContext, R.color.light_grey);
                levelTv.setTextColor(color);
                levelBtLinearLayout.setBackgroundResource(R.drawable.button_border_grey);
            }
        } catch (Exception e) {
            Log.d(TAG, "onDataChange: Exception " + e.getMessage());
        }
    }

    private String compressLevelName(String levelName) {
        return levelName.replaceAll(" ", "_").toLowerCase();
    }
}
