package prog.com.quizapp.activities;
/*---------------------o----------o----------------------
 * Created by Blasanka on 09,January,2020
 * Contact: blasanka95@gmail.com
 *-------------------------<>----------------------------*/

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import prog.com.quizapp.R;
import prog.com.quizapp.models.Question;
import prog.com.quizapp.models.SelectedAnswer;
import prog.com.quizapp.utils.CalculateScore;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    String levelName = "";

    // widgets
    RelativeLayout mQuizLayout;
    RelativeLayout mScoreLayout;
    ImageView arrowBackBt;
    TextView questionTv, answerATv, answerBTv, answerCTv, answerDTv, questionNumberTv,
            timerTv, greetingTv, noticeMessageTv, scoreTv;
    TextView nextBt, previousBt, seeLevelsBt, takeNextLevelBt;
    ProgressBar mProgressBar;

    // objects
    CountDownTimer cTimer;
    // To collect selected answers and calculate scores
    CalculateScore mCalculateScore;

    // firebase database
    FirebaseDatabase mDatabase;
    DatabaseReference mDbReference;

    // questions related
    List<Question> mQuestions;
    int questionNumber = 0;

    SelectedAnswer mSelectedAnswer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: initializing layout");
        setContentView(R.layout.activity_quiz);

        // initialize component and other objects
        init();

        // Read questions from the database
        readFromTheDatabase();
    }

    @Override
    protected void onDestroy() {
        cancelTimer();
        super.onDestroy();
    }

    private void init() {
        Log.d(TAG, "init: components and objects");

        Intent intent = getIntent();
        levelName = intent.getStringExtra("level");

        mQuizLayout = findViewById(R.id.quizLayout);
        mScoreLayout = findViewById(R.id.scoreLayout);

        arrowBackBt = findViewById(R.id.backBt);
        questionTv = findViewById(R.id.question);
        answerATv = findViewById(R.id.answer_a);
        answerBTv = findViewById(R.id.answer_b);
        answerCTv = findViewById(R.id.answer_c);
        answerDTv = findViewById(R.id.answer_d);
        questionNumberTv = findViewById(R.id.questionNumber);
        timerTv = findViewById(R.id.timer);

        greetingTv = findViewById(R.id.greetingTv);
        noticeMessageTv = findViewById(R.id.noticeMessageTv);
        scoreTv = findViewById(R.id.scoreTv);

        mProgressBar = findViewById(R.id.progressBar);

        nextBt = findViewById(R.id.nextBt);
        previousBt = findViewById(R.id.previousBt);
        seeLevelsBt = findViewById(R.id.levelsBt);
        takeNextLevelBt = findViewById(R.id.nextLevelBt);

        mQuestions = new ArrayList<>();
        mCalculateScore = new CalculateScore();

        mDatabase = FirebaseDatabase.getInstance();
        assert levelName != null;
        String levelDbKey = levelName.replaceAll(" ", "_").toLowerCase();
        mDbReference = mDatabase.getReference("quiz/"+levelDbKey);
        Log.d(TAG, "init: mDbReference initialized with database and levelDbKey: " + levelDbKey);

        nextBt.setVisibility(View.GONE);
        previousBt.setVisibility(View.GONE);

        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "nextBt onClick: displaying next question");

                //TODO: this way if timeout without clicking next button lastly
                // selected answer will not calculate

                // add selected answer to calculate scores
                if (mSelectedAnswer != null) mCalculateScore.setSelectedAnswer(mSelectedAnswer);
                // Make mSelectedAnswer null to prevent adding not selected answers
                mSelectedAnswer = null;

                if (questionNumber < 9 && questionNumber >= 0) {
                    changeToNextQuestion(++questionNumber);
                } else if (questionNumber == 9) {
                    finishQuizAndShowScores(levelName);
                }
            }
        });

        previousBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "nextBt onClick: displaying next question");
                if (questionNumber < 10 && questionNumber > 0)
                    changeToNextQuestion(--questionNumber);
                // Make mSelectedAnswer null to prevent adding not selected answers
                mSelectedAnswer = null;
            }
        });

        takeNextLevelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(QuizActivity.this, QuizActivity.class);
                if (mCalculateScore.isLevelPassed(levelName))
                    intent.putExtra("level", getNextLevelKey(levelName));
                else intent.putExtra("level", levelName);
                startActivity(intent);
            }
        });

        seeLevelsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        arrowBackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "arrowBackBt onClick: finishing activity");
                finish();
            }
        });
    }

    private void finishQuizAndShowScores(String levelName) {
        mQuizLayout.setVisibility(View.GONE);
        mScoreLayout.setVisibility(View.VISIBLE);
        String formattedScore = String.format(Locale.getDefault(),
                "Score is %d out of %d", mCalculateScore.getScores(), mQuestions.size());
        scoreTv.setText(formattedScore);

        Log.d(TAG, "finishQuizAndShowScores: level name: " + levelName);
        if (mCalculateScore.isLevelPassed(levelName)) {
            if (levelName.equals(getString(R.string.level_three_label))) {
                greetingTv.setText(getString(R.string.congratulations));

                noticeMessageTv.setText(getString(R.string.win_message));
                noticeMessageTv.setTextSize(18);
                noticeMessageTv.setTextColor(ContextCompat.getColor(this, R.color.green));

                takeNextLevelBt.setText("");
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.setMarginEnd(0);
                takeNextLevelBt.setLayoutParams(params);
                takeNextLevelBt.setVisibility(View.INVISIBLE);
            } else {
                noticeMessageTv.setText(getString(R.string.continue_notice_message));
                takeNextLevelBt.setText(getNextLevelKey(levelName));
            }
        } else {
            greetingTv.setText(getString(R.string.failed_level_message));
            noticeMessageTv.setText(getString(R.string.cannot_continue_notice_message));
            takeNextLevelBt.setText(String.format("Retake %s", levelName));
        }
    }

    private String getNextLevelKey(String levelName) {
        String nextLevel = "";
        if (levelName.contains(getString(R.string.level_one_label)))
            nextLevel = getString(R.string.level_two_label);
        else if (levelName.contains(getString(R.string.level_two_label)))
            nextLevel = getString(R.string.level_three_label);
        return nextLevel;
    }

    private void readFromTheDatabase() {
        mDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // iterate through firebase json object to loop through level questions
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Question questionModel = postSnapshot.getValue(Question.class);
                    mQuestions.add(questionModel);
                }

                Log.d(TAG, "onDataChange: mQuestions list length is: " + mQuestions.size());

                // to make questions display randomly
                Collections.shuffle(mQuestions);

                // hide progressBar to display questions
                mProgressBar.setVisibility(View.GONE);

                // show the first question by argument: questionNumber = 0
                changeToNextQuestion(questionNumber);

                /* count down timer to finish the quiz
                 *  timer starts for 10 minutes = 600999 (milliseconds)
                 *  @param duration milliseconds to complete in future
                */
                long quizDuration = 300999;
                displayTimer(quizDuration);

                // display next and previous buttons after questions displayed
                nextBt.setVisibility(View.VISIBLE);
                previousBt.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read question
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void changeToNextQuestion(final int questionNumber) {
        assert mQuestions.get(questionNumber) != null;
        Log.d(TAG, "Value of QuestionModel is: " + mQuestions.get(questionNumber).toString());

        String question = mQuestions.get(questionNumber).getQuestion();
        String formattedQuestion = String.format(Locale.getDefault(), "%d)\t %s", questionNumber + 1, question);

        String answerA = "A.\t\t" + mQuestions.get(questionNumber).getAnswer_a();
        String answerB = "B.\t\t" + mQuestions.get(questionNumber).getAnswer_b();
        String answerC = "C.\t\t" + mQuestions.get(questionNumber).getAnswer_c();
        String answerD = "D.\t\t" + mQuestions.get(questionNumber).getAnswer_d();

        String questionInfo = String.format(Locale.getDefault(),"%d / %d", questionNumber+1, mQuestions.size());
        questionNumberTv.setText(questionInfo);

        questionTv.setText(formattedQuestion);

        // set selected answer
        answerATv.setText(answerA);
        answerATv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: answerATv selected");
                mSelectedAnswer = new SelectedAnswer(mQuestions.get(questionNumber), mQuestions.get(questionNumber).getAnswer_a());
            }
        });
        answerBTv.setText(answerB);
        answerBTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: answerBTv selected");
                mSelectedAnswer = new SelectedAnswer(mQuestions.get(questionNumber), mQuestions.get(questionNumber).getAnswer_b());
            }
        });

        answerCTv.setText(answerC);
        answerCTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: answerCTv selected");
                mSelectedAnswer = new SelectedAnswer(mQuestions.get(questionNumber), mQuestions.get(questionNumber).getAnswer_c());
            }
        });

        answerDTv.setText(answerD);
        answerDTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: answerDTv selected");
                mSelectedAnswer = new SelectedAnswer(mQuestions.get(questionNumber), mQuestions.get(questionNumber).getAnswer_d());
            }
        });

        // change buttons color and label based on question number
        changeButtonsStyle(questionNumber);
    }

    private void changeButtonsStyle(int questionNumber) {
        // change previous button color if not question 1
        if (questionNumber == 1)
            previousBt.setTextColor(getResources().getColor(R.color.white));
        else if (questionNumber == 0)
            previousBt.setTextColor(getResources().getColor(R.color.grey));

        // change next button to finish if last question
        if (questionNumber == 9) nextBt.setText(getString(R.string.finish_quiz_label));
        else if (questionNumber == 8) nextBt.setText(getString(R.string.next_button));
    }

    private void displayTimer(long duration) {
        cTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                String timerFormat = String.format(Locale.getDefault(), "%d:%d",
                        minutes, seconds);
                timerTv.setText(timerFormat);
            }

            public void onFinish() {
                // display 00:00 when time finished
                timerTv.setText(getString(R.string.finished_time_format));
                Log.d(TAG, "onFinish: Timer finished");

                // Time out and display scores
                finishQuizAndShowScores(levelName);
            }
        }.start();
    }

    //cancel timer
    void cancelTimer() {
        if(cTimer!=null) cTimer.cancel();
    }
}
