package prog.com.quizapp.utils;
/*---------------------o----------o----------------------
 * Created by Blasanka on 11,January,2020
 * Contact: blasanka95@gmail.com
 *-------------------------<>----------------------------*/

import java.util.ArrayList;
import java.util.List;

import prog.com.quizapp.models.SelectedAnswer;

public class CalculateScore {
    private List<SelectedAnswer> mSelectedAnswers;

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
        this.mSelectedAnswers.add(selectedAnswer);
    }

    public double calculateScore() {
        return getSelectedAnswersSize() * Constants.MARK_FOR_ONE_QUESTION;
    }
}
