// Exercise.java
package models;

import javafx.beans.property.*;
import javafx.util.Pair;

public class Exercise {
    
	private String muscleGroup;
    private String exercise;
    private String difficulty;
    private int reps;
    private IntegerProperty repsCompleted = new SimpleIntegerProperty();
    private double progress;
    private int pid;
    
    @Override
    public String toString() {
        return "Exercise{" +
                "muscleGroup='" + muscleGroup + '\'' +
                ", exerciseName='" + exercise + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", reps=" + reps +
                ", repsCompleted=" + repsCompleted +
                ", progress=" + progress +
                '}';
    }
    
    public Exercise(String muscleGroup, String exercise, String difficulty, int reps) {
        this.muscleGroup = muscleGroup;
    	this.exercise = exercise;
        this.difficulty = difficulty;
        this.reps = reps;
        
    }
    
        // Getters and setters
    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    
    
    public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getRepsCompleted() {
        return repsCompleted.get();
    }

    public void setRepsCompleted(int repsCompleted2) {
        this.repsCompleted.set(repsCompleted2);
    }

    public IntegerProperty repsCompletedProperty() {
        return repsCompleted;
    }
    
    public double getProgress() {
		return progress;
	}

	public void setProgress(double progress2) {
		this.progress = progress2;
	}

	public void setRepsCompleted(Pair<Integer, Double> repsCompleted2) {
		    // Now you can use the extracted values as needed
		    // For example, you can set the repsCompleted and progress attributes of your class:
		    this.repsCompleted = repsCompleted;
		    this.progress = progress;
	}

	
    
    
}
