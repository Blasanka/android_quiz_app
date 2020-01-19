package prog.com.quizapp.utils;

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
    private int totalScore = 0;
    private int levelOneScore;
    private int levelTwoScore;
    private int levelThreeScore;

    public CalculateScore() {
        this.mSelectedAnswers = new ArrayList<>();
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        Log.d(TAG, "setScores: score is: " + scores);
        this.scores = scores;
    }

    public int getLevelOneScore() {
        return levelOneScore;
    }

    public void setLevelOneScore(int levelOneScore) {
        this.levelOneScore = levelOneScore;
    }

    public int getLevelTwoScore() {
        return levelTwoScore;
    }

    public void setLevelTwoScore(int levelTwoScore) {
        this.levelTwoScore = levelTwoScore;
    }

    public int getLevelThreeScore() {
        return levelThreeScore;
    }

    public void setLevelThreeScore(int levelThreeScore) {
        this.levelThreeScore = levelThreeScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getSelectedAnswersSize() {
        return mSelectedAnswers.size();
    }

    public List<SelectedAnswer> getSelectedAnswers() {
        return mSelectedAnswers;
    }

    public void setSelectedAnswer(SelectedAnswer selected) {
        Log.d(TAG, "setSelectedAnswer: mSelectedAnswers length: " + getSelectedAnswersSize());
        if (!this.mSelectedAnswers.contains(selected)) {
            this.mSelectedAnswers.add(selected);
            calculateScore(selected, SelectedType.SELECTED);
        }
    }

    public void removeSelectedAnswer(SelectedAnswer selected) {
        this.mSelectedAnswers.remove(selected);
        Log.d(TAG, "removeSelectedAnswer: mSelectedAnswers length: " + getSelectedAnswersSize());
        calculateScore(selected, SelectedType.DESELECTED);
    }

    private void calculateScore(SelectedAnswer selected, SelectedType type) {
        if (type == SelectedType.SELECTED) {
            if (isSelectedAnswerCorrect(selected)) scores++;
        } else if (type == SelectedType.DESELECTED && scores > 0) scores--;
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
            default:
                return true;
        }
    }

    public boolean isQuizPassed() {
        Log.d(TAG, "isQuizPassed: levels total score is: " + totalScore);
        return totalScore >= 20;
    }
}
