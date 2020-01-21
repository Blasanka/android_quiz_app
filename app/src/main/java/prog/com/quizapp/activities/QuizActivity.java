package prog.com.quizapp.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import prog.com.quizapp.R;
import prog.com.quizapp.models.Question;
import prog.com.quizapp.models.SelectedAnswer;
import prog.com.quizapp.utils.CalculateScore;
import prog.com.quizapp.utils.DatabaseHandler;
import prog.com.quizapp.utils.QuizContract;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private Context mContext = QuizActivity.this;
    String levelName = "";

    // widgets
    RelativeLayout mQuizLayout;
    RelativeLayout mScoreLayout;
    ImageView arrowBackBt;
    TextView questionTv, answerATv, answerBTv, answerCTv, answerDTv, questionNumberTv,
            timerTv, greetingTv, noticeMessageTv, scoreTv;
    TextView nextBt, previousBt, seeLevelsBt, takeNextLevelBt;
    ProgressBar mProgressBar;

    MediaPlayer mMediaPlayer;

    // objects
    CountDownTimer cTimer;
    // To collect selected answers and calculate scores
    CalculateScore mCalculateScore;

    // SQLite database
    private DatabaseHandler mDbHandler;

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
        readQuizFromTheDatabase();
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

        mMediaPlayer = MediaPlayer.create(this, R.raw.sound);

        mQuestions = new ArrayList<>();
        mCalculateScore = new CalculateScore();

        mDbHandler = new DatabaseHandler(mContext);
//        assert levelName != null;
//        String levelDbKey = compressLevelName();// Gets the data repository in write mode
//
//        Log.d(TAG, "init: SQLiteDatabase initialized..." + levelDbKey);

        saveScores(0);

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

                highlightSelectedAnswer(nextBt); // nextBt is here to avoid null
            }
        });

        previousBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make mSelectedAnswer null to prevent adding not selected answers
                mCalculateScore.removeSelectedAnswer(mSelectedAnswer);
                mSelectedAnswer = null;
                highlightSelectedAnswer(previousBt); // previousBt sending as a holder, nothing gonna change

                Log.d(TAG, "previousBt onClick: displaying previous question");
                if (questionNumber < 10 && questionNumber > 0)
                    changeToNextQuestion(--questionNumber);
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

    private void highlightSelectedAnswer(TextView tv) {
        answerATv.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.white));
        answerBTv.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.white));
        answerCTv.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.white));
        answerDTv.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.white));

        if (tv == answerATv)
            answerATv.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.darkYellow));
        else if (tv == answerBTv)
            answerBTv.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.darkYellow));
        else if (tv == answerCTv)
            answerCTv.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.darkYellow));
        else if (tv == answerDTv)
            answerDTv.setTextColor(ContextCompat.getColor(QuizActivity.this, R.color.darkYellow));
    }

//    private String compressLevelName() {
//        return levelName.replaceAll(" ", "_").toLowerCase();
//    }

    private void finishQuizAndShowScores(String levelName) {
        mQuizLayout.setVisibility(View.GONE);
        mScoreLayout.setVisibility(View.VISIBLE);

        String formattedScore = String.format(Locale.getDefault(),
                "Score is %d out of %d", mCalculateScore.getScores(), mQuestions.size());
        scoreTv.setText(formattedScore);

        saveScores(mCalculateScore.getScores());
        Log.d(TAG, "finishQuizAndShowScores: level name: " + levelName);

        if (levelName.equals(getString(R.string.level_three_label))) {

            int levelOne = getLevelScoreFromDb(getString(R.string.level_one_label));
            int levelTwo = getLevelScoreFromDb(getString(R.string.level_two_label));
            int levelThree = getLevelScoreFromDb(getString(R.string.level_three_label));
            mCalculateScore.setLevelOneScore(levelOne);
            mCalculateScore.setLevelTwoScore(levelTwo);
            mCalculateScore.setLevelThreeScore(levelThree);
            mCalculateScore.setTotalScore(levelOne + levelTwo + levelThree);

            if (mCalculateScore.isQuizPassed()) {
                // Have to hide usual level end layout to display quiz end layout
                changeLayoutForQuizEnd(R.layout.layout_won_score_board);
            } else {
                // Have to hide usual level end layout to display quiz end layout
                changeLayoutForQuizEnd(R.layout.layout_failed_score_board);
            }
        } else {
            if (mCalculateScore.isLevelPassed(levelName)) {
                noticeMessageTv.setText(getString(R.string.continue_notice_message));
                takeNextLevelBt.setText(getNextLevelKey(levelName));
            } else {
                greetingTv.setText(getString(R.string.failed_level_message));
                noticeMessageTv.setText(getString(R.string.cannot_continue_notice_message));
                takeNextLevelBt.setText(String.format("Retake %s", levelName));
            }
        }

        // Release MediaPlayer when playing finished.
        whenSoundCompleteReleaseResources(mMediaPlayer);
    }

    // Hide level end layout to display quiz end layout
    private void changeLayoutForQuizEnd(int layoutResourceId) {
        // Hide level end layout
        greetingTv.setVisibility(View.GONE);
        scoreTv.setVisibility(View.GONE);
        noticeMessageTv.setVisibility(View.GONE);
        takeNextLevelBt.setVisibility(View.GONE);
        seeLevelsBt.setVisibility(View.GONE);

        // Display score table, for win or failed
        addLevelsScoresToTableView(layoutResourceId);
    }

    private void addLevelsScoresToTableView(int p) {
        View layout = LayoutInflater.from(this).inflate(p, null);

        final TextView levelOneScore = layout.findViewById(R.id.levelOneScore);
        final TextView levelTwoScore = layout.findViewById(R.id.levelTwoScore);
        final TextView levelThreeScore = layout.findViewById(R.id.levelThreeScore);
        final TextView totalScore = layout.findViewById(R.id.totalScore);

        int levelOne = mCalculateScore.getLevelOneScore();
        int levelTwo = mCalculateScore.getLevelTwoScore();
        int levelThree = mCalculateScore.getLevelThreeScore();
        mCalculateScore.setTotalScore(levelOne + levelTwo + levelThree);

        levelOneScore.setText(String.format(Locale.getDefault(), "%d", levelOne));
        levelTwoScore.setText(String.format(Locale.getDefault(), "%d", levelTwo));
        levelThreeScore.setText(String.format(Locale.getDefault(), "%d", levelThree));
        totalScore.setText(String.format(Locale.getDefault(), "%d", mCalculateScore.getTotalScore()));

        if (R.layout.layout_failed_score_board == p) {
            TextView seeLevelsBt = layout.findViewById(R.id.seeLevelsBt);
            seeLevelsBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(QuizActivity.this, LevelsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        } else {
            // show particles if won
            new ParticleSystem(this, 500, R.drawable.star_pink, 10000)
                    .setSpeedRange(0.1f, 0.2f)
                    .oneShot(layout.findViewById(R.id.particleView), 400);
        }

        TextView homeBt = layout.findViewById(R.id.homeBt);
        homeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mScoreLayout.addView(layout);
    }

    private int getLevelScoreFromDb(String s) {
        int score = mDbHandler.getScoreFromDb(compressLevelName(s));
        Log.d(TAG, "assignScoreToTableTv: level score: " + score);
        return score;
    }

    private void saveScores(int scores) {
        if (levelName.equals(getString(R.string.level_one_label))) mCalculateScore.setLevelOneScore(scores);
        if (levelName.equals(getString(R.string.level_two_label))) mCalculateScore.setLevelTwoScore(scores);
        if (levelName.equals(getString(R.string.level_three_label))) mCalculateScore.setLevelThreeScore(scores);

        long affectedCount = mDbHandler.updateScoreOnDb(scores, levelName);
        Log.d(TAG, "saveScores: updated row count: " + affectedCount);
    }

    private String getNextLevelKey(String levelName) {
        String nextLevel = "";
        if (levelName.contains(getString(R.string.level_one_label)))
            nextLevel = getString(R.string.level_two_label);
        else if (levelName.contains(getString(R.string.level_two_label)))
            nextLevel = getString(R.string.level_three_label);
        return nextLevel;
    }

    private void readQuizFromTheDatabase() {
        Log.d(TAG, "readQuizFromTheDatabase: level Name "+ levelName);
        Cursor cursor = mDbHandler.getValuesFromDb(compressLevelName(levelName));
        while(cursor.moveToNext()) {
            String question = cursor.getString(
                    cursor.getColumnIndexOrThrow(QuizContract.QuestionEntry.COL_QUESTION));
            String answerA = cursor.getString(
                    cursor.getColumnIndexOrThrow(QuizContract.QuestionEntry.COL_ANSWER_A));
            String answerB = cursor.getString(
                    cursor.getColumnIndexOrThrow(QuizContract.QuestionEntry.COL_ANSWER_B));
            String answerC = cursor.getString(
                    cursor.getColumnIndexOrThrow(QuizContract.QuestionEntry.COL_ANSWER_C));
            String answerD = cursor.getString(
                    cursor.getColumnIndexOrThrow(QuizContract.QuestionEntry.COL_ANSWER_D));
            String correctAns = cursor.getString(
                    cursor.getColumnIndexOrThrow(QuizContract.QuestionEntry.COL_CORRECT_ANS));

            mQuestions.add(new Question(question, answerA, answerB, answerC, answerD, correctAns));
        }
        cursor.close();
        mDbHandler.closeDatabase();

        try {
            // to make questions display randomly
            Collections.shuffle(mQuestions);

            Log.d(TAG, "onDataChange: mQuestions list length is: " + mQuestions.size());

            // show the first question by argument: questionNumber = 0
            changeToNextQuestion(questionNumber);

            // hide progressBar to display questions
            mProgressBar.setVisibility(View.GONE);

            /* count down timer to finish the quiz
             *  timer starts for 10 minutes = 600999 (milliseconds)
             *  @param duration milliseconds to complete in future
             */
            long quizDuration = 300999;
            displayTimer(quizDuration);

            // display next and previous buttons after questions displayed
            nextBt.setVisibility(View.VISIBLE);
            previousBt.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        // answer d alignment fixing according to system font scale
//        float scale = getResources().getConfiguration().fontScale;
//        if (scale == 1.15) answerD = "D.\t\t\t" + mQuestions.get(questionNumber).getAnswer_d();

        String questionInfo = String.format(Locale.getDefault(),"%d / %d", questionNumber+1, mQuestions.size());
        questionNumberTv.setText(questionInfo);

        questionTv.setText(formattedQuestion);

        // set selected answer
        answerATv.setText(answerA);
        answerATv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: answerATv selected");
                highlightSelectedAnswer(answerATv);

                stopPlayingAndStartOver();

                mSelectedAnswer = new SelectedAnswer(mQuestions.get(questionNumber), mQuestions.get(questionNumber).getAnswer_a());
            }
        });
        answerBTv.setText(answerB);
        answerBTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: answerBTv selected");
                highlightSelectedAnswer(answerBTv);

                stopPlayingAndStartOver();

                mSelectedAnswer = new SelectedAnswer(mQuestions.get(questionNumber), mQuestions.get(questionNumber).getAnswer_b());
            }
        });

        answerCTv.setText(answerC);
        answerCTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: answerCTv selected");

                stopPlayingAndStartOver();

                highlightSelectedAnswer(answerCTv);
                mSelectedAnswer = new SelectedAnswer(mQuestions.get(questionNumber), mQuestions.get(questionNumber).getAnswer_c());
            }
        });

        answerDTv.setText(answerD);
        answerDTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: answerDTv selected");

                stopPlayingAndStartOver();

                highlightSelectedAnswer(answerDTv);
                mSelectedAnswer = new SelectedAnswer(mQuestions.get(questionNumber), mQuestions.get(questionNumber).getAnswer_d());
            }
        });

        // change buttons color and label based on question number
        changeButtonsStyle(questionNumber);
    }

    private void stopPlayingAndStartOver() {
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void whenSoundCompleteReleaseResources(final MediaPlayer mp) {
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.release();
            }
        });
    }

    private String compressLevelName(String levelName) {
        return levelName.replaceAll(" ", "_").toLowerCase();
    }
}
