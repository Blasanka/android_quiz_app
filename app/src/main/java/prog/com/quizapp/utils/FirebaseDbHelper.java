package prog.com.quizapp.utils;
/*---------------------o----------o----------------------
 * Created by Blasanka on 18,January,2020
 * Contact: blasanka95@gmail.com
 *-------------------------<>----------------------------*/

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

import prog.com.quizapp.R;
import prog.com.quizapp.models.Question;

public class FirebaseDbHelper {
    private static final String TAG = "FirebaseDbHelper";
    private final Context mContext;

    // questions related
    List<Question> mQuestions;

    String levelDbKey;

    // firebase database
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbReference;

    CalculateScore mCalculateScore;

    public FirebaseDbHelper(Context context, String levelName) {
        this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance();
        assert levelName != null;
        this.levelDbKey = compressLevelName(levelName);
        mDbReference = mDatabase.getReference("quiz/"+levelDbKey);
        Log.d(TAG, "init: mDbReference initialized with database and levelDbKey: " + levelDbKey);
    }

    public void getLevelScoreFromDb(final TextView tv, final TextView totalTv, String s) {
        mDatabase.getReference(s).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long value = dataSnapshot.getValue(Long.class);
                tv.setText(String.format(Locale.getDefault(), "%d", value));
                String previousTotal = totalTv.getText().toString();
                if (!previousTotal.equals(""))
                    value += Double.parseDouble(totalTv.getText().toString());
                totalTv.setText(String.format(Locale.getDefault(), "%d", value));
                Log.d(TAG, "onDataChange: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: DatabaseError " + databaseError.getMessage());
            }
        });
    }

    public void saveScores(int scores) {
        // Storing level scores into database to later access and give level access
        mDatabase.getReference("quiz").child("scores")
                .child(levelDbKey)
                .setValue(scores);
        if (levelDbKey.equals(mContext.getString(R.string.level_one_label))) mCalculateScore.setLevelOneScore(scores);
        if (levelDbKey.equals(mContext.getString(R.string.level_two_label))) mCalculateScore.setLevelTwoScore(scores);
        if (levelDbKey.equals(mContext.getString(R.string.level_three_label))) mCalculateScore.setLevelThreeScore(scores);
    }

    public void readFromTheDatabase() {
        mDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // iterate through firebase json object to loop through level questions
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Question questionModel = postSnapshot.getValue(Question.class);
                    mQuestions.add(questionModel);
                }

                Log.d(TAG, "onDataChange: mQuestions list length is: " + mQuestions.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read question
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private String compressLevelName(String levelName) {
        return levelName.replaceAll(" ", "_").toLowerCase();
    }
}
