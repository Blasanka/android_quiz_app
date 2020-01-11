package prog.com.quizapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import prog.com.quizapp.R;

public class MainActivity extends AppCompatActivity {

    TextView quizBt;

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
    }
}
