package prog.com.quizapp.activities;
/*---------------------o----------o----------------------
 * Created by Blasanka on 08,January,2020
 * Contact: blasanka95@gmail.com
 *-------------------------<>----------------------------*/

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import prog.com.quizapp.R;
import prog.com.quizapp.utils.CalculateScore;

public class LevelsActivity extends AppCompatActivity {

    private static final String TAG = "LevelsActivity";

    LinearLayout mLinearLayout;
    RelativeLayout relLayout;
    TextView levelOneBt;
    TextView levelTwoTv;
    TextView levelThreeTv;

            // To collect selected answers and calculate scores
    CalculateScore mCalculateScore;
    DatabaseReference mRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalculateScore = new CalculateScore();
        mRef = FirebaseDatabase.getInstance().getReference("quiz");

        mLinearLayout = initLayout();

        setupLevelTv(mLinearLayout, levelTwoTv, R.string.level_two_label, R.string.level_one_label);
        setupLevelTv(mLinearLayout, levelThreeTv, R.string.level_three_label, R.string.level_two_label);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called...");
        getFirebaseScores(getString(R.string.level_one_label), R.string.level_one_label, R.string.level_two_label, levelTwoTv);
        getFirebaseScores(getString(R.string.level_two_label), R.string.level_two_label, R.string.level_three_label, levelThreeTv);
    }

    private LinearLayout initLayout() {
        relLayout = findViewById(R.id.relLayout);

        levelTwoTv = new TextView(LevelsActivity.this);
        levelThreeTv = new TextView(LevelsActivity.this);

        LinearLayout linearLayout = new LinearLayout(this);
        int linearLayoutId = View.generateViewId();
        linearLayout.setId(linearLayoutId);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
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

    private void setupLevelTv(LinearLayout linearLayout, TextView levelTv, int currentLevel, final int previousLevel) {
        levelTv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

//        disableLevelBasedOnScore(previousLevel, currentLevel, levelTv);
        getFirebaseScores(getString(R.string.level_one_label), previousLevel, currentLevel, levelTv);
        levelTv.setTextColor(Color.LTGRAY);
        levelTv.setTextSize(24);
        levelTv.setText(getString(currentLevel));
        linearLayout.addView(levelTv);
    }

//    private void disableLevelBasedOnScore(int previousLevel, final int currentLevel, TextView levelTv) {
//        if (!shouldDisable(previousLevel)) {
//            levelTv.setTextColor(Color.WHITE);
//            levelTv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(LevelsActivity.this, QuizActivity.class);
//                    intent.putExtra("level", getString(currentLevel));
//                    startActivity(intent);
//                }
//            });
//        } else levelTv.setTextColor(Color.LTGRAY);
//    }

//    private boolean shouldDisable(int previousLevel) {
//        int dbScore = getFirebaseScores(getString(previousLevel));
//        mCalculateScore.setScores(dbScore);
//
//        return !mCalculateScore.isLevelPassed(getString(previousLevel));
//    }

    private void getFirebaseScores(final String levelName, final int previousLevel, final int currentLevel, final TextView levelTv) {

        // Storing level scores into database to later access and give level access
        mRef.child("scores")
                .child(compressLevelName(levelName))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: score is: " + dataSnapshot.getValue());
                        try {
                            Object obj = dataSnapshot.getValue();
                            assert obj != null;
                            String value = obj.toString();
                            int score = Integer.parseInt(value);
                            mCalculateScore.setScores(score);
                            if (mCalculateScore.isLevelPassed(getString(previousLevel))) {
                                levelTv.setTextColor(Color.WHITE);
                                levelTv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(LevelsActivity.this, QuizActivity.class);
                                        intent.putExtra("level", getString(currentLevel));
                                        startActivity(intent);
                                    }
                                });
                            } else levelTv.setTextColor(Color.LTGRAY);
                        } catch (Exception e) {
                            Log.d(TAG, "onDataChange: Exception " + e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: error: " + databaseError.getMessage());
                    }
                });
    }

    private String compressLevelName(String levelName) {
        return levelName.replaceAll(" ", "_").toLowerCase();
    }

}
