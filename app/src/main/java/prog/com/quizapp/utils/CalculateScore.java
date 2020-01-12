package prog.com.quizapp.utils;
/*---------------------o----------o----------------------
 * Created by Blasanka on 11,January,2020
 * Contact: blasanka95@gmail.com
 *-------------------------<>----------------------------*/

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import prog.com.quizapp.models.SelectedAnswer;

enum SelectedType {
    SELECTED,
    DESELECTED
}

public class CalculateScore {
    private static final String TAG = "CalculateScore";
    private List<SelectedAnswer> mSelectedAnswers;
    private int scores = 0;

    public CalculateScore() {
        this.mSelectedAnswers = new ArrayList<>();
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public int getSelectedAnswersSize() {
        return mSelectedAnswers.size();
    }

    public List<SelectedAnswer> getSelectedAnswers() {
        return mSelectedAnswers;
    }

    public void setSelectedAnswer(SelectedAnswer selected) {
        Log.d(TAG, "setSelectedAnswer: mSelectedAnswers length: " + getSelectedAnswersSize());
        this.mSelectedAnswers.add(selected);
        calculateScore(selected, SelectedType.SELECTED);
    }

    public void removeSelectedAnswer(SelectedAnswer selected) {
        this.mSelectedAnswers.remove(selected);
        Log.d(TAG, "removeSelectedAnswer: mSelectedAnswers length: " + getSelectedAnswersSize());
        calculateScore(selected, SelectedType.DESELECTED);
    }

    private void calculateScore(SelectedAnswer selected, SelectedType type) {
        if (type == SelectedType.SELECTED) {
            if(isSelectedAnswerCorrect(selected)) scores++;
        } else if (type == SelectedType.DESELECTED) scores--;
    }

    private boolean isSelectedAnswerCorrect(SelectedAnswer selected) {
        //TODO: Remove when development finished
        Log.d(TAG, "isSelectedAnswerCorrect: correct answer is: " + selected.getQuestion()
                .getCorrect_answer());
        Log.d(TAG, "isSelectedAnswerCorrect: selected answer is: " + selected.getSelectedAnswer());
        return selected
                .getQuestion()
                .getCorrect_answer()
                .equals(selected.getSelectedAnswer());
    }

    public boolean isLevelPassed(String levelName) {
        Log.d(TAG, "isLevelPassed: score is: " + scores);
        switch (levelName.toLowerCase()) {
            case "level one":
                return scores >= Constants.LEVEL_ONE_PASS_MARK;
            case "level two":
                return scores >= Constants.LEVEL_TWO_PASS_MARK;
            case "level three":
                return scores == Constants.LEVEL_THREE_PASS_MARK;
            default: return true;
        }
    }
}
