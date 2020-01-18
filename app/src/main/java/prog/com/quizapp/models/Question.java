package prog.com.quizapp.models;


import org.json.JSONException;
import org.json.JSONObject;

public class Question {
    private String question;
    private String correct_answer;
    private String answer_a;
    private String answer_b;
    private String answer_c;
    private String answer_d;

    public Question() {
    }

    public Question(String question, String answer_a, String answer_b, String answer_c, String answer_d, String correct_answer) {
        this.question = question;
        this.answer_a = answer_a;
        this.answer_b = answer_b;
        this.answer_c = answer_c;
        this.answer_d = answer_d;
        this.correct_answer = correct_answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public String getAnswer_a() {
        return answer_a;
    }

    public void setAnswer_a(String answer_a) {
        this.answer_a = answer_a;
    }

    public String getAnswer_b() {
        return answer_b;
    }

    public void setAnswer_b(String answer_b) {
        this.answer_b = answer_b;
    }

    public String getAnswer_c() {
        return answer_c;
    }

    public void setAnswer_c(String answer_c) {
        this.answer_c = answer_c;
    }

    public String getAnswer_d() {
        return answer_d;
    }

    public void setAnswer_d(String answer_d) {
        this.answer_d = answer_d;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", correct_answer='" + correct_answer + '\'' +
                ", answer_a='" + answer_a + '\'' +
                ", answer_b='" + answer_b + '\'' +
                ", answer_c='" + answer_c + '\'' +
                ", answer_d='" + answer_d + '\'' +
                '}';
    }

    public static Question fromJson(JSONObject obj) throws JSONException {
        return new Question(
                obj.getString("question"),
                obj.getString("answer_a"),
                obj.getString("answer_b"),
                obj.getString("answer_c"),
                obj.getString("answer_d"),
                obj.getString("correct_answer"));
    }
}
