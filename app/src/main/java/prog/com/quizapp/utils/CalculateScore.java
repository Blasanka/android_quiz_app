package prog.com.quizapp.utils;
/*---------------------o----------o----------------------
 * Created by Blasanka on 11,January,2020
 * Contact: blasanka95@gmail.com
 *-------------------------<>----------------------------*/

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import prog.com.quizapp.models.SelectedAnswer;

public class CalculateScore {
    private static final String TAG = "CalculateScore";
    private List<SelectedAnswer> mSelectedAnswers;
    private int scores = 0;

    public CalculateScore() {
        this.mSelectedAnswers = new ArrayList<>();
    }

    public int getSelectedAnswersSize() {
        return mSelectedAnswers.size();
    }

    public List<SelectedAnswer> getSelectedAnswers() {
        return mSelectedAnswers;
    }

    public void setSelectedAnswer(SelectedAnswer selectedAnswer) {
        Log.d(TAG, "setSelectedAnswer: mSelectedAnswers length: " + getSelectedAnswersSize());
        this.mSelectedAnswers.add(selectedAnswer);
        calculateScore();
    }

    public void removeSelectedAnswer(SelectedAnswer selectedAnswer) {
        this.mSelectedAnswers.remove(selectedAnswer);
        Log.d(TAG, "removeSelectedAnswer: mSelectedAnswers length: " + getSelectedAnswersSize());
        calculateScore();
    }

    private void calculateScore() {
        scores = getSelectedAnswersSize();// * Constants.MARK_FOR_ONE_QUESTION
    }

    public String getScores() {
        return scores+"";
    }

    public void setScores(int scores) {
        this.scores = scores;
    }
}
