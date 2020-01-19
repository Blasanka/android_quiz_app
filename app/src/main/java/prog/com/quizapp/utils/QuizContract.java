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
                    QuestionEntry.COL_ANSWER_C + " TEXT," +
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
                    ScoreEntry.COL_LEVEL_ONE_SCORE + " INTEGER," +
                    ScoreEntry.COL_LEVEL_TWO_SCORE + " INTEGER," +
                    ScoreEntry.COL_LEVEL_THREE_SCORE + " INTEGER," +
                    ScoreEntry.COL_TOTAL_SCORE + " INTEGER)";

    static final String SQL_DELETE_SCORE_ENTRIES =
            "DROP TABLE IF EXISTS " + ScoreEntry.TABLE_NAME;

    private static String query = "INSERT INTO quiz (question, answer_a, answer_b, answer_c, answer_d, correct_answer, level_name) VALUES (";
    static final String[] SQL_QUESTION_ENTRY = {
            query + "'If nucleon number of potassium is 39, number of neutrons will be?', '39', '19', '20', '29', '20', 'level_one');",
            query + "'If proton number is 19, electron configuration will be?', '2,8 and 7', '2,8 and 18', '2,8,8 and 1', '2,8,8 and 3', '2,8,8 and 1', 'level_one');",
            query + "'When a single atom provides both electrons which are needed for completion of covalent bond, lead to', 'Ionic bond', 'Covalent bond', 'Co-ordinate bond', 'Dative bond', 'Co-ordinate bond', 'level_one');",
            query + "'In Al2Cl6, number of electron pairs donated by each Chloride ion are?', '1', '2', '3', '4', '2', 'level_one');",
            query + "'In an atom, number of protons and neutrons are added to obtain?', 'Number of electrons', 'Number of nucleons', 'Atomic number of element', 'Number of positrons', 'Number of nucleons', 'level_one');",
            query + "'Dative covalent bond is found in?', 'Ammonia', 'Ammonium ion', 'Urea', 'Nitrogen', 'Ammonium ion', 'level_one');",
            query + "'Number of bonding pairs of electrons in water H2O is?', '1', '2', '3', '4', '2', 'level_one');",
            query + "'First element in periodic table to have 8 valence electrons is?', 'Nitrogen', 'Oxygen', 'Fluorine', 'Neon', 'Neon', 'level_one');",
            query + "'Burning of glucose produces heat and', 'carbon dioxide (gas)', 'water (gas)', 'oxygen (gas)', 'Both A and B', 'Both A and B', 'level_one');",
            query + "'Complete transfer of one or more electrons from one atom to different atom forms?', 'Ionic bonds', 'Covalent bonds', 'Metallic bonding', 'Co-ordinate bonding', 'Ionic bonds', 'level_one');",

            query + "'Conversion of Copper Hydroxide (Cu(OH)2) into Copper Oxide (CuO) and water (H2O) is an example of', 'Combustion', 'Thermal decomposition', 'Neutralization', 'Respiration', 'Thermal decomposition', 'level_two');",
            query + "'Products of reaction CuO + HNO3 → are', 'Cu2NO3 + H2O', 'Cu(NO3)2 + H2O', 'Cu(NO3)2 + H2', 'Cu(NO2)4 + H2', 'Cu(NO3)2 + H2O', 'level_two');",
            query + "'Symbol of Magnesium is', 'Mg', 'Mn', 'Hg', 'M', 'Mg', 'level_two');",
            query + "'Elements and their isotopes have', 'Same number of protons', 'Same number of electrons', 'Different number of neutrons', 'All of these', 'All of these', 'level_two');",
            query + "'In ammonium ion, electrons required between hydrogen ion and nitrogen ion are?', '1', '2', '3', '4', '2', 'level_two');",
            query + "'Non-renewable fossil fuels include', 'Coal', 'Oil', 'Petrol', 'All of these', 'All of these', 'level_two');",
            query + "'Burning of methane (CH4) gives off heat and', 'Carbon dioxide (gas)', 'Water (gas)', 'Hydrogen gas', 'Both A and B', 'Both A and B', 'level_two');",
            query + "'LPG is obtained by', 'Cooling methane', 'Cooling natural gas', 'Cooling carbon dioxide', 'Cooling nitrous oxide', 'Cooling natural gas', 'level_two');",
            query + "'Acidic oxide do not include?', 'CO2', 'SO2', 'P2O5', 'PbO', 'PbO', 'level_two');",
            query + "'All are noble gases except', 'Helium', 'Neon', 'Argon', 'Xeon', 'Helium', 'level_two');",

            query + "'If NaOH(aq) reacts with HCl(aq), reaction can be known as', 'Combustion', 'Thermal decomposition', 'Neutralization', 'Respiration', 'Neutralization', 'level_three');",
            query + "'Methane (CH4) is better fuel than Coal gas because', 'It is endothermic', 'It is exothermic', 'It gives off more heat than coal gas', 'It requires very little oxygen to burn', 'It gives off more heat than coal gas', 'level_three');",
            query + "'On burning, hydrogen is changed into', 'Hydroxides', 'Water vapors', 'Dilute acids', 'Hydrogen gas', 'Water vapors', 'level_three');",
            query + "'Burning of hydrogen', 'Is an endothermic reaction', 'Acts as fuel in rockets etc.', 'Is fatal for plant habitats', 'Results in acid rains', 'Acts as fuel in rockets etc.', 'level_three');",
            query + "'Methane (CH4) is an excellent fuel because', 'It is basically an endothermic reaction', 'It gives off a lot of heat', 'It produces carbon monoxide', 'It contains a lot of carbon', 'It gives off a lot of heat', 'level_three');",
            query + "'Under thermal decomposition, copper nitrate gets converted into', 'Copper oxide', 'Nitrogen oxide', 'Oxygen', 'All of the above', 'All of the above', 'level_three');",
            query + "'Thermal decomposition does not occur in', 'Carbonates', 'Hydroxides', 'Sulphates', 'Nitrates', 'Sulphates', 'level_three');",
            query + "'Chemical reaction with oxygen is called', 'Oxidation', 'Activation', 'Combustion', 'Reduction', 'Combustion', 'level_three');",
            query + "'Isotopes have similar', 'Chemical properties', 'Physical properties', 'Number of neutrons', 'Mass numbers', 'Chemical properties', 'level_three');",
            query + "'First element in periodic table to have 8 valence electrons in third shell is?', 'Silicon', 'Chlorine', 'Argon', 'Potassium', 'Argon', 'level_three');",
    };
}
