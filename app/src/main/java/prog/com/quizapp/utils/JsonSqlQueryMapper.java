package prog.com.quizapp.utils;
/*---------------------o----------o----------------------
 * Created by Blasanka on 18,January,2020
 * Contact: blasanka95@gmail.com
 *-------------------------<>----------------------------*/

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import prog.com.quizapp.models.Question;

public class JsonSqlQueryMapper {
    private Context mContext;

    public JsonSqlQueryMapper(Context context) {
        this.mContext = context;
    }

    private static final String TAG = "JsonSqlQueryMapper";

    public JSONObject loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("quiz_app.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        try {
            JSONObject quizObject = new JSONObject(json).getJSONObject("quiz");
            return quizObject;
        } catch (Exception e) {
            Log.d(TAG, "loadJSONFromAsset: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<Question> generateInsertQueryForJsonObjects() {
        ArrayList<Question> questions = new ArrayList<>();
        JSONObject jsonObject = loadJSONFromAsset();
        try {
            Iterator<String> iter = jsonObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    JSONObject value = jsonObject.getJSONObject(key);
                    Question question = Question.fromJson(value.getJSONObject("question_two"));
                    questions.add(question);
                    Log.d(TAG, "generateInsertQueryForJsonObjects: " + question.getAnswer_a());
                } catch (JSONException e) {
                    // Something went wrong!
                    Log.e(TAG, "generateInsertQueryForJsonObjects: error occured", e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }
}
