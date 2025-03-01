package models;

public class UserProgress{
    private int userWorkoutID;
    private int userPID;
    private String userMuscleGroup;
    private String userExerciseName;
    private String userDifficulty;
    private int userRepsRequired;
    private int userRepsCompleted;
    private double userProgress;
    
    @Override
    public String toString() {
        return "UserProgress{" +
                "workoutID=" + userWorkoutID +
                ", pid=" + userPID +
                ", muscleGroup='" + userMuscleGroup + '\'' +
                ", exerciseName='" + userExerciseName + '\'' +
                ", difficulty='" + userDifficulty + '\'' +
                ", repsRequired=" + userRepsRequired +
                ", repsCompleted=" + userRepsCompleted +
                ", progress=" + userProgress +
                '}';
    }
    public UserProgress(int userWorkoutID, int userPID, String userMuscleGroup, String userExerciseName, String userDifficulty, int userRepsRequired, int userRepsCompleted, double userProgress) {
        this.userWorkoutID = userWorkoutID;
        this.userPID = userPID;
        this.userMuscleGroup = userMuscleGroup;
        this.userExerciseName = userExerciseName;
        this.userDifficulty = userDifficulty;
        this.userRepsRequired = userRepsRequired;
        this.userRepsCompleted = userRepsCompleted;
        this.userProgress = userProgress;
    }

    // Getters and setters
    public int getUserWorkoutID() {
        return userWorkoutID;
    }

    public void setUserWorkoutID(int userWorkoutID) {
        this.userWorkoutID = userWorkoutID;
    }

    public int getUserPID() {
        return userPID;
    }

    public void setUserPID(int userPID) {
        this.userPID = userPID;
    }

    public String getUserMuscleGroup() {
        return userMuscleGroup;
    }

    public void setUserMuscleGroup(String userMuscleGroup) {
        this.userMuscleGroup = userMuscleGroup;
    }

    public String getUserExerciseName() {
        return userExerciseName;
    }

    public void setUserExerciseName(String userExerciseName) {
        this.userExerciseName = userExerciseName;
    }

    public String getUserDifficulty() {
        return userDifficulty;
    }

    public void setUserDifficulty(String userDifficulty) {
        this.userDifficulty = userDifficulty;
    }

    public int getUserRepsRequired() {
        return userRepsRequired;
    }

    public void setUserRepsRequired(int userRepsRequired) {
        this.userRepsRequired = userRepsRequired;
    }

    public int getUserRepsCompleted() {
        return userRepsCompleted;
    }

    public void setUserRepsCompleted(int userRepsCompleted) {
        this.userRepsCompleted = userRepsCompleted;
    }

    public double getUserProgress() {
        return userProgress;
    }

    public void setUserProgress(double userProgress) {
        this.userProgress = userProgress;
    }
}
