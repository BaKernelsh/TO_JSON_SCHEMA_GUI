package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.Generator.Generator;
import org.example.Validator.JSONValidator;
import org.example.Validator.OnUnknownKeyword;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class JsonToolController {

    // Generate Schema Tab
    @FXML private TextArea jsonInputAreaGen;
    @FXML private TextArea outputSchemaArea;
    @FXML private Button loadJsonButtonGen;
    @FXML private Button generateSchemaButton;

    // Validate JSON Tab
    @FXML private TextArea jsonInputAreaVal;
    @FXML private TextArea schemaInputAreaVal;
    @FXML private TextArea outputValidationArea;
    @FXML private Button validateButton;
    @FXML private ComboBox<String> onUnknownKeywordComboBox;

    private OnUnknownKeyword onUnknownKeyword = OnUnknownKeyword.THROW;
    private final Map<String, OnUnknownKeyword> keywordOptions = Map.of(
            "Validation unsuccessful", OnUnknownKeyword.THROW,
            "Continue validation", OnUnknownKeyword.KEYWORD_VALIDATION_UNSUCCESFUL_CONTINUE_VALIDATION
    );

    @FXML
    private void initialize() {
        loadJsonButtonGen.setOnAction(e -> loadJsonFromFile(jsonInputAreaGen));

        onUnknownKeywordComboBox.getItems().addAll(keywordOptions.keySet());
        onUnknownKeywordComboBox.setValue("Validation unsuccessful");

        generateSchemaButton.setOnAction(e -> {
            try {
                if(jsonInputAreaGen.getText()==null || jsonInputAreaGen.getText().strip().equals(""))
                    throw new Exception("Please input a json.");

                Generator generator = new Generator();
                String schema = generator.generateSchema(jsonInputAreaGen.getText());

                outputSchemaArea.setText(schema);
            } catch (Exception ex) {
                outputSchemaArea.setText("Error: " + ex.getMessage());
            }
        });

        validateButton.setOnAction(e -> {
            try {
                if(jsonInputAreaVal.getText()==null || jsonInputAreaVal.getText().strip().equals(""))
                    throw new Exception("Please input a json.");
                if(schemaInputAreaVal.getText()==null || schemaInputAreaVal.getText().strip().equals(""))
                    throw new Exception("Please input a schema.");


                JSONValidator validator = new JSONValidator();
                validator.setUnknownKeywordBehavior(onUnknownKeyword);
                boolean result = validator.validateAgainstSchema(jsonInputAreaVal.getText(), schemaInputAreaVal.getText());

                if(result)
                    outputValidationArea.setText("Document is valid");
                else
                    outputValidationArea.setText("Document is valid for known validation keywords.\nUnknown validation keyword: "+validator.getFirstUnknownKeyword());
                //else
            } catch (Exception ex) {
                outputValidationArea.setText("Validation error: " + ex.getMessage());
            }
        });

        onUnknownKeywordComboBox.setOnAction(e -> {
            String selected = onUnknownKeywordComboBox.getValue();
            onUnknownKeyword = keywordOptions.get(selected);
        });
    }

    private void loadJsonFromFile(TextArea targetArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open JSON File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                targetArea.setText(content);
            } catch (Exception e) {
                targetArea.setText("Failed to load file: " + e.getMessage());
            }
        }
    }


    private String validateJsonAgainstSchema(String json, String schema) {
        return "Validation passed!";
    }
}
