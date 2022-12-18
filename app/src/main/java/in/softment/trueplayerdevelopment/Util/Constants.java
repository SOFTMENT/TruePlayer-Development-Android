package in.softment.trueplayerdevelopment.Util;

import java.util.Date;

public class Constants {

    public static Date currentDate = new Date();

    public static class Category {
        public static final String basic_skills = "BASICSKILLS";
        public static final String ball_mastery = "BALLMASTERY";
        public static final String elite_drill = "ELITEDRILL";
        public static final String foot_speed = "FOOTSPEED";
        public static final String attacking_moves = "ATTACKINGMOVES";

        public static String getCategoryName(String categoryId){
            switch (categoryId) {
                case basic_skills: return  "Basic Skills";
                case ball_mastery: return  "Ball Mastery";
                case elite_drill: return  "Elite Drill";
                case foot_speed: return  "Foot Speed";
                case attacking_moves: return  "Attacking Moves";
                default:return "";
            }
        }
    }


}

