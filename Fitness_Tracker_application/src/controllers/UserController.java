// ClientController.java
package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.DAOModel;
import models.Exercise;

public class UserController {
    
    @FXML
    private TableView<Exercise> exerciseTable1;
    
    @FXML
    private TableView<Exercise> exerciseTable2;
    
    @FXML
    private TableColumn<Exercise, String> colMuscleGroup;
    
    @FXML
    private TableColumn<Exercise, String> colExercise;
    
    @FXML
    private TableColumn<Exercise, String> colDifficulty;
    
    @FXML
    private TableColumn<Exercise, Integer> colReps;
    
    @FXML
    private TableColumn<Exercise, Integer> colRepsCompleted;
    
    @FXML
    private TableColumn<Exercise, Double> colProgress;
        
    private DAOModel daoModel;
    
    private List<Exercise> addedExercises = new ArrayList<>();

	private int userId;

     
    public UserController() {
        daoModel = new DAOModel();
    }
    
    public void setUserData(int userId) {
        this.userId = userId;
    }
    
    private ObservableList<Exercise> exerciseTable2Data = FXCollections.observableArrayList();
    
    @FXML
    private void initialize() {
        // Initialize table columns
    	colMuscleGroup.setCellValueFactory(new PropertyValueFactory<>("muscleGroup"));
    	colExercise.setCellValueFactory(new PropertyValueFactory<>("exercise"));
        colDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        colReps.setCellValueFactory(new PropertyValueFactory<>("reps"));
        colRepsCompleted.setCellValueFactory(new PropertyValueFactory<>("repsCompleted"));
        colProgress.setCellValueFactory(new PropertyValueFactory<>("progress"));
               
        
//     // Make the Reps Completed column editable
//        colRepsCompleted.setCellValueFactory(new PropertyValueFactory<>("repsCompleted"));
//        colRepsCompleted.setCellFactory(new Callback<TableColumn<Exercise, Integer>, TableCell<Exercise, Integer>>() {
//            @Override
//            public TableCell<Exercise, Integer> call(TableColumn<Exercise, Integer> param) {
//                return new EditableIntegerCell();
//            }
//        });
        
     // Fetch exercises from the sv_user_progress table
        ObservableList<Exercise> userProgressExercises = daoModel.getUserProgressExercises();
        
        // Populate the table with exercises from sv_user_progress
        exerciseTable1.setItems(userProgressExercises);
        
//        ObservableList<Exercise> exerciseTable2Data = FXCollections.observableArrayList();
//        exerciseTable2.setItems(exerciseTable2Data);
        ObservableList<Integer> repsCompletedList = daoModel.getAllRepsCompleted();
        for (Integer repsCompleted : repsCompletedList) {
            // Create Exercise objects with only repsCompleted and progress
            Exercise exercise = new Exercise("", "", "", 0);
            exercise.setRepsCompleted(repsCompleted);
            // Calculate progress if needed and set it in the Exercise object
            // double progress = ...;
            // exercise.setProgress(progress);
            // Add the Exercise object to exerciseTable2Data
            exerciseTable2Data.add(exercise);
        }
        // Populate exerciseTable2 with exerciseTable2Data
        exerciseTable2.setItems(exerciseTable2Data);
        exerciseTable2.refresh();
            // You can process the repsCompleted here, such as displaying it or performing calculations
    }
    
    @FXML
    private void handleAddExerciseButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddExercise.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            AddExerciseController addExerciseController = loader.getController();
            addExerciseController.setClientController(this);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add method to add exercises to the addedExercises list
    public void addExercise(Exercise exercise) {
    	Exercise reqColumns = new Exercise(exercise.getMuscleGroup(), exercise.getExercise(), exercise.getDifficulty(), exercise.getReps());
        addedExercises.add(reqColumns);
    }
    
    public void addExercise(Exercise exercise, int pid) {
        // Check if the exercise already exists
        boolean exerciseExists = daoModel.isExerciseAlreadyExists(pid, exercise.getMuscleGroup(), exercise.getExercise(), exercise.getDifficulty());
        if (exerciseExists) {
            showAlert("This exercise already exists.");
        } else {
            daoModel.addToUserProgress(exercise, pid);
            // Add the exercise to the local list
            addedExercises.add(exercise);
        }
    }
    
    // Modify  method to populate the table with added exercises
    public void refreshexerciseTable1() {
        // Clear the table
        exerciseTable1.getItems().clear();
        
        // Fetch exercises from the sv_user_progress table
        ObservableList<Exercise> userProgressExercises = daoModel.getUserProgressExercises();
        
        // Populate the table with exercises from sv_user_progress
        exerciseTable1.setItems(userProgressExercises);
        int numRowsBeforeRefresh = exerciseTable2.getItems().size();
        
        // Populate table2 with default entries for new rows
        for (int i = numRowsBeforeRefresh; i < userProgressExercises.size(); i++) {
            Exercise exercise = userProgressExercises.get(i);
            Exercise defaultEntry = new Exercise(exercise.getMuscleGroup(), exercise.getExercise(),
                    exercise.getDifficulty(), 0); // Assuming default reps_completed is 0
            exerciseTable2.getItems().add(defaultEntry);
        }
        exerciseTable2.refresh();
    }

    

   
//    private class EditableIntegerCell extends TableCell<Exercise, Integer> {
//        private javafx.scene.control.TextField textField;
//
//        public EditableIntegerCell() {
//            textField = new javafx.scene.control.TextField();
//            textField.setOnAction(e -> commitEdit(Integer.parseInt(textField.getText())));
//            textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
//                if (!isNowFocused) {
//                    commitEdit(Integer.parseInt(textField.getText()));
//                }
//            });
//        }

//        @Override
//        protected void updateItem(Integer item, boolean empty) {
//            super.updateItem(item, empty);
//            if (empty) {
//                setGraphic(null);
//            } else {
//                textField.setText(item.toString());
//                setGraphic(textField);
//            }
//        }
//    }

    
//    private void addAddToWorkoutsButtonToTable() {
//        TableColumn<Exercise, Void> colAddToWorkouts = new TableColumn<>("Add to Workouts");
//        colAddToWorkouts.setCellFactory(param -> new TableCell<Exercise, Void>() {
//            final Button addButton = new Button("Add");
//
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//
//                if (empty) {
//                    setGraphic(null);
//                    setText(null);
//                } else {
//                    setGraphic(addButton);
//                    addButton.setOnAction(event -> {
//                        Exercise exercise = getTableView().getItems().get(getIndex());
//                        // Add the selected exercise to the table and store it in the database
//                        addToWorkouts(exercise);
//                    });
//                }
//            }
//        });
//
//        exerciseTable1.getColumns().add(colAddToWorkouts);
//    }

//    private void addToWorkouts(Exercise selectedExercise) {
//        // Add the selected exercise to the table
//        exerciseTable1.getItems().add(selectedExercise); 
//        Exercise defaultEntry = new Exercise(selectedExercise.getMuscleGroup(), selectedExercise.getExercise(),
//                selectedExercise.getDifficulty(), 0); // Assuming default reps_completed is 0
//        daoModel.addToUserProgress(defaultEntry);
//
//        // Refresh table2 to reflect the changes
//        exerciseTable2.refresh();
//        
//        // Store the selected exercise in the database
//        daoModel.addToWorkouts(selectedExercise);
//        exerciseTable2.refresh();
//    }

    @FXML
    private void handleRefreshButtonClicked() {
        refreshexerciseTable1();
        exerciseTable2.refresh();
    }
    
    @FXML
    private void handleDeleteButtonClicked() {
        Exercise exerciseToDelete1 = exerciseTable1.getSelectionModel().getSelectedItem();
        Exercise exerciseToDelete2 = exerciseTable2.getSelectionModel().getSelectedItem();
        
        if (exerciseToDelete1 != null) {
            daoModel.deleteExerciseByParams(exerciseToDelete1.getMuscleGroup(), exerciseToDelete1.getExercise(), exerciseToDelete1.getDifficulty());
            exerciseTable1.getItems().remove(exerciseToDelete1);
        }

        if (exerciseToDelete2 != null) {
            daoModel.deleteExerciseByParams(exerciseToDelete2.getMuscleGroup(), exerciseToDelete2.getExercise(), exerciseToDelete2.getDifficulty());
            exerciseTable2.getItems().remove(exerciseToDelete2);
        }
    }

    @FXML
    private void handleLogoutButtonClicked() {
        // Close the current stage
        Stage stage = (Stage) exerciseTable1.getScene().getWindow();
        stage.close();
        
        // Open the login view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleUpdateButtonClicked() {
        // Iterate over the items in the table
    	Exercise exerciseToUpdate1 = exerciseTable1.getSelectionModel().getSelectedItem();
    	Exercise exerciseToUpdate2 = exerciseTable2.getSelectionModel().getSelectedItem();
        if (exerciseToUpdate1 != null && exerciseToUpdate2 != null) {
        	 
        	
        	TextInputDialog dialog = new TextInputDialog("Enter reps completed");
            dialog.setTitle("Update Reps Completed");
            dialog.setHeaderText("Update Reps Completed");
            dialog.setContentText("Please enter the new value for reps completed:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
            	try {
                    // Parse the input string to get the new reps completed value
                    int newRepsCompleted = Integer.parseInt(result.get());
                    
                    int repsRequired = exerciseToUpdate1.getReps();
                    System.out.println(repsRequired);
                    // Calculate progress
                    double progress = (double) newRepsCompleted / repsRequired * 100;
                    
                    for (Exercise exercise : new Exercise[] {exerciseToUpdate1, exerciseToUpdate2}) {
                    	// Update reps completed for the exercise
                    	
                    	exercise.setRepsCompleted(newRepsCompleted);
                    	 exercise.setProgress(progress);
                    	// Update the reps completed value in the database
                    	daoModel.updateRepsCompleted(exercise);
                    }
                    exerciseTable2.refresh();
                    // Optionally, calculate progress and update it in the Exercise object
                    // double progress = (double) newRepsCompleted / exercise.getReps() * 100;
                    // exercise.setProgress(progress);
                } catch (NumberFormatException e) {
                    // Handle invalid input
                    System.out.println("Invalid input for reps completed.");
                }
            }
        	
        	
        	
            exerciseTable2.refresh();
        } else {
            // Handle case when no Exercise is selected
        	showAlert("Please select an exercise to update reps completed.");
        }
        // Refresh the table to reflect the changes
        exerciseTable2.refresh();
    }

    public void updateRepsCompletedDialog(Exercise exercise) {
        TextInputDialog dialog = new TextInputDialog("Enter reps completed");
        dialog.setTitle("Update Reps Completed");
        dialog.setHeaderText("Update Reps Completed");
        dialog.setContentText("Please enter the new value for reps completed:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
        	try {
                // Parse the input string to get the new reps completed value
                int newRepsCompleted = Integer.parseInt(result.get());
                
                // Update reps completed for the exercise
                exercise.setRepsCompleted(newRepsCompleted);

                // Update the reps completed value in the database
                daoModel.updateRepsCompleted(exercise);
                
                exerciseTable2.refresh();
                // Optionally, calculate progress and update it in the Exercise object
                // double progress = (double) newRepsCompleted / exercise.getReps() * 100;
                // exercise.setProgress(progress);
            } catch (NumberFormatException e) {
                // Handle invalid input
                System.out.println("Invalid input for reps completed.");
            }
        }
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

