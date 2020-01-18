package prog.com.quizapp.utils;

import java.util.List;

import prog.com.quizapp.models.Question;

/*---------------------o----------o----------------------
 * Created by Blasanka on 18,January,2020
 * Contact: blasanka95@gmail.com
 *-------------------------<>----------------------------*/
public interface InterfaceListener {
    void obtainQuestions(List<Question> mQuestions);
}