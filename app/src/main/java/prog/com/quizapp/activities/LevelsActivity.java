package prog.com.quizapp.activities;
/*---------------------o----------o----------------------
 * Created by Blasanka on 08,January,2020
 * Contact: blasanka95@gmail.com
 *-------------------------<>----------------------------*/

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import prog.com.quizapp.R;

public class LevelsActivity extends AppCompatActivity {

    RelativeLayout relLayout;
    TextView levelOneBt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relLayout = findViewById(R.id.relLayout);

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

        TextView levelTwoBt = new TextView(LevelsActivity.this);
        levelTwoBt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        levelTwoBt.setTextColor(Color.WHITE);
        levelTwoBt.setTextSize(24);
        levelTwoBt.setText(getString(R.string.level_two_label));
        levelTwoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelsActivity.this, QuizActivity.class);
                intent.putExtra("level", getString(R.string.level_two_label));
                startActivity(intent);
            }
        });
        linearLayout.addView(levelTwoBt);

        TextView levelThreeBt = new TextView(LevelsActivity.this);
        levelThreeBt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        levelThreeBt.setTextColor(Color.WHITE);
        levelThreeBt.setTextSize(24);
        levelThreeBt.setText(getString(R.string.level_three_label));
        levelThreeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelsActivity.this, QuizActivity.class);
                intent.putExtra("level", getString(R.string.level_three_label));
                startActivity(intent);
            }
        });
        linearLayout.addView(levelThreeBt);
    }
}
