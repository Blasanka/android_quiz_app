package prog.com.quizapp.models;
/*---------------------o----------o----------------------
 * Created by Blasanka on 11,January,2020
 * Contact: blasanka95@gmail.com
 *-------------------------<>----------------------------*/

import android.util.Log;

public class SelectedAnswer {
    private static final String TAG = "SelectedAnswer";
    private Question question;
    private String selectedAnswer;
    private boolean isCorrect;
    private double score;

    public SelectedAnswer() {}

    public SelectedAnswer(Question question, String selectedAnswer, boolean isCorrect, double score) {
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
        this.score = score;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        Log.d(TAG, "setSelectedAnswer: " + selectedAnswer);
        this.selectedAnswer = selectedAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
