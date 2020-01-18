package prog.com.quizapp.utils;

import android.provider.BaseColumns;

public class QuizContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private QuizContract() {}

    /* Inner class that defines the table contents */
    public static class QuestionEntry implements BaseColumns {
        static final String TABLE_NAME = "quiz";

        // TABLE_LEVEL columns
        public static final String COL_QUESTION = "question";
        public static final String COL_ANSWER_A = "answer_a";
        public static final String COL_ANSWER_B = "answer_b";
        public static final String COL_ANSWER_C = "answer_c";
        public static final String COL_ANSWER_D = "answer_d";
        public static final String COL_CORRECT_ANS = "correct_answer";
        static final String COL_LEVEL_NAME = "level_name";
    }

    // create table
    static final String SQL_CREATE_QUIZ_ENTRIES =
            "CREATE TABLE " + QuestionEntry.TABLE_NAME + " (" +
                    QuestionEntry._ID + " INTEGER PRIMARY KEY," +
                    QuestionEntry.COL_QUESTION + " TEXT," +
                    QuestionEntry.COL_ANSWER_A + " TEXT," +
                    QuestionEntry.COL_ANSWER_B + " TEXT," +
                    QuestionEntry.COL_ANSWER_C + " TEXT" +
                    QuestionEntry.COL_ANSWER_D + " TEXT," +
                    QuestionEntry.COL_CORRECT_ANS + " TEXT," +
                    QuestionEntry.COL_LEVEL_NAME + " TEXT)";

    // drop table
    static final String SQL_DELETE_QUIZ_ENTRIES =
            "DROP TABLE IF EXISTS " + QuestionEntry.TABLE_NAME;

    static class ScoreEntry implements BaseColumns {
        static final String TABLE_NAME = "scores";

        // TABLE SCORE columns
        static final String COL_LEVEL_ONE_SCORE = "level_one";
        static final String COL_LEVEL_TWO_SCORE = "level_two";
        static final String COL_LEVEL_THREE_SCORE = "level_three";
        static final String COL_TOTAL_SCORE = "total";
    }

    static final String SQL_CREATE_SCORE_ENTRIES =
            "CREATE TABLE " + ScoreEntry.TABLE_NAME + " (" +
                    ScoreEntry._ID + " INTEGER PRIMARY KEY," +
                    ScoreEntry.COL_LEVEL_ONE_SCORE + " TEXT," +
                    ScoreEntry.COL_LEVEL_TWO_SCORE + " TEXT," +
                    ScoreEntry.COL_LEVEL_THREE_SCORE + " TEXT," +
                    ScoreEntry.COL_TOTAL_SCORE + " TEXT)";

    static final String SQL_DELETE_SCORE_ENTRIES =
            "DROP TABLE IF EXISTS " + ScoreEntry.TABLE_NAME;

    static final String SQL_QUESTION_ENTRY =
            "INSERT INTO quiz (question, answer_a, answer_b, answer_c, answer_d, correct_answer, level_name) values(, , , , );";
}
