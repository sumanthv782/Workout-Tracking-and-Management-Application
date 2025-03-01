package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

public class DAOModel {
	//Declare DB objects 
	DBConnect conn = null;
	Connection connection = null;
	Statement sqlsmt = null;
	PreparedStatement preparedStatement = null;

		// constructor
	public DAOModel() { // Instantiate the DB object.
		 conn = new DBConnect();
	}
	// Method for createTable.
	public void createTable(String sql) {
	 try {

	 // Connect to DB
	 System.out.println("Connecting to database to create S_Vupp_tab Table");
	 connection = conn.getConnection();
	 System.out.println("DB connected successfully");

	 // Execute create table query
	 System.out.println("Creating table in given DB");
		 
		 preparedStatement = connection.prepareStatement(sql);

         // Execute create table query
         preparedStatement.executeUpdate();
         // Close resources
         preparedStatement.close();
	 System.out.println("Table created in given DB...");
//	 connection.close(); //disconnect from DB 
	}catch (SQLException se) { // Handle the possible exceptions from JDBC.
		if (!se.getMessage().contains("already exists")) {
            se.printStackTrace();
        } else {
            System.out.println("");
        }
	}
	     }


	public void insertExercise(String muscle_group, String exerciseName, String difficulty, double reps) {
	    try {
	        // Connect to DB
	        connection = conn.getConnection();
	        
	        // Prepare the SQL statement
	        String sql = "INSERT INTO sv_workouts (muscle_group, exercise, difficulty, reps) VALUES (?, ?, ?, ?)";
	        preparedStatement = connection.prepareStatement(sql);

	        // Set the parameters for the SQL statement
	        preparedStatement.setString(1, muscle_group);
	        preparedStatement.setString(2, exerciseName);
	        preparedStatement.setString(3, difficulty);
	        preparedStatement.setDouble(4, reps);

	        // Execute the SQL statement
	        preparedStatement.executeUpdate();

	        System.out.println("Exercise inserted successfully.");
	    } catch (SQLException se) {
	        se.printStackTrace();
	    }
	}
	
	 public ObservableList<Exercise> getAllExercises() {
	        ObservableList<Exercise> exercises = FXCollections.observableArrayList();
	        try {
	        	connection = conn.getConnection();
	            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Muscle_Group, Exercise, Difficulty, Reps FROM sv_workouts");
	            ResultSet resultSet = preparedStatement.executeQuery();
	            while (resultSet.next()) {
	            	String muscleGroup = resultSet.getString("Muscle_Group");
	            	String exercise = resultSet.getString("Exercise");
	                String difficulty = resultSet.getString("Difficulty");
	                int reps = resultSet.getInt("Reps");
	                exercises.add(new Exercise(muscleGroup, exercise, difficulty, reps));
	            }
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return exercises;
	    }
	 
	 public Pair<Boolean, Pair<Integer, String>> authenticateUser(String username, String password) {
	        try {
	            connection = conn.getConnection();
	            String sql = "SELECT * FROM sv_users WHERE username = ?";
	            preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setString(1, username);
	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                // Retrieve hashed password from the database
	                String storedHashedPassword = hashPassword(resultSet.getString("password"));

	                // Hash the provided password
	                String hashedPassword = hashPassword(password);

	                // Compare hashed passwords
	                if (hashedPassword.equals(storedHashedPassword)) {
	                    // Authentication successful
	                    int userId = resultSet.getInt("userid");
	                    String role = resultSet.getString("role");
	                    return new Pair<>(true, new Pair<>(userId, role));
	                }
	            }
	            // Authentication failed
	            return new Pair<>(false, null);
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return new Pair<>(false, null);
	        }
	    }
	 
	 public void addToWorkouts(Exercise exercise) {
	        String sql = "INSERT INTO sv_user_progress (muscle_group, exercise_name, difficulty, reps_required) VALUES (?, ?, ?, ?)";
	        try {
	            PreparedStatement statement = connection.prepareStatement(sql);
	            statement.setString(1, exercise.getMuscleGroup());
	            statement.setString(2, exercise.getExercise());
	            statement.setString(3, exercise.getDifficulty());
	            statement.setInt(4, exercise.getReps());
	            statement.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 public ObservableList<Exercise> getUserProgressExercises() {
		    ObservableList<Exercise> exercises = FXCollections.observableArrayList();
		    try {
		        connection = conn.getConnection();
		        PreparedStatement preparedStatement = connection.prepareStatement("SELECT muscle_group, exercise_name, difficulty, reps_required FROM sv_user_progress");
		        ResultSet resultSet = preparedStatement.executeQuery();
		        while (resultSet.next()) {
		            String muscleGroup = resultSet.getString("muscle_group");
		            String exerciseName = resultSet.getString("exercise_name");
		            String difficulty = resultSet.getString("difficulty");
		            int reps = resultSet.getInt("reps_required");
		            exercises.add(new Exercise(muscleGroup, exerciseName, difficulty, reps));
		        }
		        
		     // Print all data in console
	            for (Exercise exercise : exercises) {
	                System.out.println(exercise);
	            }
		        
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return exercises;
		}
	 
	 public ObservableList<UserProgress> getAllUserProgressExercises() {
		 ObservableList<UserProgress> userExercises = FXCollections.observableArrayList();

	        try {
	            connection = conn.getConnection();
	            String sql = "SELECT workout_id, pid, muscle_group, exercise_name, difficulty, reps_required, reps_completed, progress FROM sv_user_progress";
	            PreparedStatement preparedStatement = connection.prepareStatement(sql);
	            ResultSet resultSet = preparedStatement.executeQuery();

	            while (resultSet.next()) {
	                int workoutID = resultSet.getInt("workout_id");
	                int pid = resultSet.getInt("pid");
	                String muscleGroup = resultSet.getString("muscle_group");
	                String exerciseName = resultSet.getString("exercise_name");
	                String difficulty = resultSet.getString("difficulty");
	                int repsRequired = resultSet.getInt("reps_required");
	                int repsCompleted = resultSet.getInt("reps_completed");
	                double progress = resultSet.getDouble("progress");

	                userExercises.add(new UserProgress(workoutID, pid, muscleGroup, exerciseName, difficulty, repsRequired, repsCompleted, progress));
	            }
	            
	         // Print all data in console
	            for (UserProgress exercise : userExercises) {
	                System.out.println(exercise);
	            }
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return userExercises;
	    }

	 public void updateRepsCompleted(Exercise exercise) {
	        try {
	            Connection connection = conn.getConnection();
	            String sql = "UPDATE sv_user_progress SET reps_completed = ?, progress = ? WHERE muscle_group = ? AND exercise_name = ? AND difficulty = ?";
	            PreparedStatement preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setInt(1, exercise.getRepsCompleted());
	            preparedStatement.setDouble(2, exercise.getProgress());
	            preparedStatement.setString(3, exercise.getMuscleGroup());
	            preparedStatement.setString(4, exercise.getExercise());
	            preparedStatement.setString(5, exercise.getDifficulty());
	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 public void updateRepsRequired(Exercise exercise) {
	        try {
	            Connection connection = conn.getConnection();
	            String sql = "UPDATE sv_workouts SET reps = ? WHERE muscle_group = ? AND exercise = ? AND difficulty = ?";
	            PreparedStatement preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setInt(1, exercise.getReps());
	            preparedStatement.setString(2, exercise.getMuscleGroup());
	            preparedStatement.setString(3, exercise.getExercise());
	            preparedStatement.setString(4, exercise.getDifficulty());
	            preparedStatement.executeUpdate();
	            
	            String query = "UPDATE sv_user_progress SET reps_required = ? WHERE muscle_group = ? AND exercise_name = ? AND difficulty = ?";
	            PreparedStatement prepStmt = connection.prepareStatement(query);
	            prepStmt.setInt(1, exercise.getReps());
	            prepStmt.setString(2, exercise.getMuscleGroup());
	            prepStmt.setString(3, exercise.getExercise());
	            prepStmt.setString(4, exercise.getDifficulty());
	            prepStmt.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 public ObservableList<Integer> getAllRepsCompleted() {
		    ObservableList<Integer> repsCompletedList = FXCollections.observableArrayList();
		    try {
		        connection = conn.getConnection();
		        PreparedStatement preparedStatement = connection.prepareStatement("SELECT reps_completed FROM sv_user_progress");
		        ResultSet resultSet = preparedStatement.executeQuery();
		        while (resultSet.next()) {
		            int repsCompleted = resultSet.getInt("reps_completed");
		            repsCompletedList.add(repsCompleted);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return repsCompletedList;
		}
	 
	 public ObservableList<Pair<Integer, Double>> getAllRepsCompletedAndProgress() {
		    ObservableList<Pair<Integer, Double>> repsCompletedAndProgressList = FXCollections.observableArrayList();
		    try {
		        connection = conn.getConnection();
		        PreparedStatement preparedStatement = connection.prepareStatement("SELECT reps_completed, progress FROM sv_user_progress");
		        ResultSet resultSet = preparedStatement.executeQuery();
		        while (resultSet.next()) {
		            int repsCompleted = resultSet.getInt("reps_completed");
		            double progress = resultSet.getDouble("progress");
		            repsCompletedAndProgressList.add(new Pair<>(repsCompleted, progress));
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return repsCompletedAndProgressList;
		}

	 
	 public void addToUserProgress(Exercise exercise, int pid) {
		    try {
		        connection = conn.getConnection();
		        String sql = "INSERT INTO sv_user_progress (pid, muscle_group, exercise_name, difficulty, reps_required, reps_completed, progress) VALUES (?, ?, ?, ?, ?, ?, ?)";
		        preparedStatement = connection.prepareStatement(sql);
		        preparedStatement.setInt(1, pid);
		        preparedStatement.setString(2, exercise.getMuscleGroup());
		        preparedStatement.setString(3, exercise.getExercise());
		        preparedStatement.setString(4, exercise.getDifficulty());
		        preparedStatement.setInt(5, exercise.getReps());
		        preparedStatement.setInt(6, exercise.getRepsCompleted()); // Assuming default reps_completed is 0
		        preparedStatement.setDouble(7, 0);
		        preparedStatement.executeUpdate();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
	 
	 public boolean isExerciseAlreadyExists(int pid, String muscleGroup, String exerciseName, String difficulty) {
		    try {
		        connection = conn.getConnection();
		        String sql = "SELECT * FROM sv_user_progress WHERE pid = ? AND muscle_group = ? AND exercise_name = ? AND difficulty = ?";
		        preparedStatement = connection.prepareStatement(sql);
		        preparedStatement.setInt(1, pid);
		        preparedStatement.setString(2, muscleGroup);
		        preparedStatement.setString(3, exerciseName);
		        preparedStatement.setString(4, difficulty);
		        ResultSet resultSet = preparedStatement.executeQuery();
		        return resultSet.next(); // If any rows are returned, it means the exercise already exists
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false; // Return false in case of any error
		    }
		}
	 
	 public int getPid(String muscleGroup, String exerciseName, String difficulty) {
		    try {
		        connection = conn.getConnection();
		        String sql = "SELECT pid FROM sv_workouts WHERE muscle_group = ? AND exercise = ? AND difficulty = ?";
		        preparedStatement = connection.prepareStatement(sql);
		        preparedStatement.setString(1, muscleGroup);
		        preparedStatement.setString(2, exerciseName);
		        preparedStatement.setString(3, difficulty);
		        ResultSet resultSet = preparedStatement.executeQuery();
		        
		        // If a row is found, retrieve the pid
		        if (resultSet.next()) {
		            return resultSet.getInt("pid");
		        } else {
		            // No matching row found, handle this case appropriately
		            return -1; // Or throw an exception, return null, etc.
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return -1; // Return a default value or handle the exception
		    }
		    
		}

	 public void deleteExerciseByParams(String muscleGroup, String exercise, String difficulty) {
		    try {
		        connection = conn.getConnection();
		        String sql = "DELETE FROM sv_user_progress WHERE muscle_group = ? AND exercise_name = ? AND difficulty = ?";
		        preparedStatement = connection.prepareStatement(sql);
		        preparedStatement.setString(1, muscleGroup);
		        preparedStatement.setString(2, exercise);
		        preparedStatement.setString(3, difficulty);
		        preparedStatement.executeUpdate();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } 
		}
	 
	 
	 public void deleteWorkoutByParams(String muscleGroup, String exercise, String difficulty) {
		    try {
		        connection = conn.getConnection();
		        String sql = "DELETE FROM sv_workouts WHERE muscle_group = ? AND exercise = ? AND difficulty = ?";
		        preparedStatement = connection.prepareStatement(sql);
		        preparedStatement.setString(1, muscleGroup);
		        preparedStatement.setString(2, exercise);
		        preparedStatement.setString(3, difficulty);
		        preparedStatement.executeUpdate();
//		        deleteExerciseByParams(muscleGroup, exercise, difficulty);
		        
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
	 
	 public ObservableList<User> getAllUsers() {
		    ObservableList<User> userList = FXCollections.observableArrayList();
		    try {
		        // Establish database connection
		        Connection connection = conn.getConnection();
		        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM sv_users");
		        ResultSet resultSet = preparedStatement.executeQuery();
		        while (resultSet.next()) {
		            int userID = resultSet.getInt("userID");
		            String username = resultSet.getString("username");
		            String password = hashPassword(resultSet.getString("password"));
		            
		            // Create User object and add it to the list
		            User user = new User(userID, username, password);
		            userList.add(user);
		        }
		        
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return userList;
		}
	 
	 public boolean deleteUser(User user) {
		    try {
		        // Establish a database connection
		        Connection connection = conn.getConnection();
		        
		        // Prepare the SQL statement
		        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM sv_users WHERE userID = ?");
		        preparedStatement.setInt(1, user.getUserID()); 
		        // Execute the delete operation
		        int rowsAffected = preparedStatement.executeUpdate();
		        
		        // Close the resources
		        
		        // Check if any rows were affected
		        return rowsAffected > 0;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false; // Return false in case of any exception or error
		    }
		}
	 public boolean resetPassword(User user) {
		    try {
		        // Establish a database connection
		        Connection connection = conn.getConnection();
		        
		        // Prepare the SQL statement
		        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE sv_users SET password = ? WHERE userID = ?");
		        preparedStatement.setString(1, ""); // Set the password to an empty string
		        preparedStatement.setInt(2, user.getUserID());
		        
		        // Execute the update operation
		        int rowsAffected = preparedStatement.executeUpdate();
		        
		        // Check if any rows were affected
		        return rowsAffected > 0;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false; // Return false in case of any exception or error
		    }
		}
	 
	 private String hashPassword(String password) {
	        try {
	            MessageDigest md = MessageDigest.getInstance("SHA-256");
	            md.update(password.getBytes());

	            byte[] byteData = md.digest();

	            // Convert the byte to hex format
	            StringBuilder hexString = new StringBuilder();
	            for (byte b : byteData) {
	                String hex = Integer.toHexString(0xff & b);
	                if (hex.length() == 1) {
	                    hexString.append('0');
	                }
	                hexString.append(hex);
	            }
	            return hexString.toString();
	        } catch (NoSuchAlgorithmException e) {
	            // Handle exception
	            e.printStackTrace();
	            return null;
	        }
	    }


}
