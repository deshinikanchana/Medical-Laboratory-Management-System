package lk.ijse.laboratory.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.laboratory.Dto.*;
import lk.ijse.laboratory.Dto.Tm.patientTm;
import lk.ijse.laboratory.Model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static lk.ijse.laboratory.db.DbConnection.newDto;

public class PatientFormController implements Initializable {
    private final patientModel PtMod = new patientModel();
    public TextField txtPtId;
    public TextField txtName;
    public TextField txtDob;
    public TextField txtEmail;
    public TextField txtTelNo;
    public ComboBox cmbCenters;
    public TableView<patientTm> tblPatient;
    public TableColumn<?,?> coltId;
    public TableColumn<?,?> colPtName;
    public TableColumn<?,?> colPtGender;
    public TableColumn<?,?> colPtAge;
    public TableColumn<?,?> colPtTel;
    public TableColumn<?,?> colPtEmail;
    public Label lblPatients;
    public Label lblPrescription;
    @FXML
    private AnchorPane root;
    @FXML
    private ChoiceBox<String> genderChoiceBox;

    private String [] Gender = {"Male","Female"};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderChoiceBox.getItems().addAll(Gender);
        loadAllCenters();
        setCellValueFactory();
        generateNextPtId();
        loadAllPatients();
    }

    private void generateNextPtId() {
        try {
            String ptId = PtMod.generateNextPtId();
            txtPtId.setText(ptId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        coltId.setCellValueFactory(new PropertyValueFactory<>("ptId"));
        colPtName.setCellValueFactory(new PropertyValueFactory<>("ptName"));
        colPtGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPtAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colPtTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colPtEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadAllCenters() {

        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<collectingCenterDto> centerList = collectingCenterModel.loadAllCenters();

            for (collectingCenterDto dto : centerList) {
                obList.add(dto.getCcId());
            }
            cmbCenters.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onActionBackBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/userDashboard_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionNewCenterBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/collectingCentersManage_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }
   /* private boolean validatePatient() {

        String id = txtPtId.getText();
        boolean isCustomerValidate = Pattern.matches("[P][0-9]{3,}", id);
        if (!isCustomerValidate) {
            new Alert(Alert.AlertType.ERROR, "Inavlid Patient Id !").show();
            return false;
        }
        String name = txtName.getText();

        boolean isCustomerNameValidate = Pattern.matches("[A-za-z]{3,}", name);
        if (!isCustomerNameValidate) {
            new Alert(Alert.AlertType.ERROR, "Inavlid Name !").show();
            return false;
        }
        String tel = txtTelNo.getText();

        boolean isCustomerTelValidate = Pattern.matches("\\d{10}", tel);
        if (!isCustomerTelValidate) {
            new Alert(Alert.AlertType.ERROR, "Inavlid tel. No !").show();
            return false;
        }

        return true;
    }
*/
    public void onActionSaveBtn(ActionEvent event) {
   // boolean isValidate = validatePatient();
   // if(isValidate) {
        String ptId = txtPtId.getText();
        String userId = newDto.getUserId();
        String ccId = (String) cmbCenters.getValue();
        String name = txtName.getText();
        String gender = genderChoiceBox.getValue();
        String Dob = txtDob.getText();
        String telNo = txtTelNo.getText();
        String email = txtEmail.getText();


        var dto = new patientDto(ptId, userId, ccId, name, gender, Dob, telNo, email);

        try {
            boolean isSaved = PtMod.savePatient(dto);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "New Patient saved Successfully !!!").show();
                loadAllPatients();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
   // }

    }

    private void clearFields() {
        txtPtId.setText("");
        txtName.setText("");
        cmbCenters.setValue("");
        txtTelNo.setText("");
        txtEmail.setText("");
        txtDob.setText("");
        genderChoiceBox.setValue(" ");
        generateNextPtId();

    }

    private void loadAllPatients() {

        ObservableList<patientTm> obList = FXCollections.observableArrayList();

        try {
            List<patientDto> dtoList = PtMod.getAllPatients();

            for(patientDto dto : dtoList) {
                String age;
                LocalDate dob = LocalDate.parse(dto.getDob());
                LocalDate today = LocalDate.now();
                int ag =(Period.between(dob,today).getYears());
                if(ag >= 1) {
                    age = String.valueOf(ag);
                }else{
                    age = String.valueOf(Period.between(dob,today).getMonths());
                }
                obList.add(
                        new patientTm(
                                dto.getPtId(),
                                dto.getName(),
                                dto.getGender(),
                                  age,
                                dto.getTelNo(),
                                dto.getEmail()
                        )
                );
            }

            tblPatient.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onActionUpdateBtn(ActionEvent event) {
      //  boolean isValidate = validatePatient();
        //if (isValidate) {

            String ptId = txtPtId.getText();
            String userId = newDto.getUserId();
            String ccId = (String) cmbCenters.getValue();
            String name = txtName.getText();
            String gender = genderChoiceBox.getValue();
            String dob = txtDob.getText();
            String telNo = txtTelNo.getText();
            String email = txtEmail.getText();

            var dto = new patientDto(ptId, userId, ccId, name, gender, dob, telNo, email);

            try {
                boolean isUpdated = PtMod.updatePatient(dto);
                System.out.println(isUpdated);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "patient updated!").show();
                    loadAllPatients();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
       // }
    }


    public void onActionDeleteBtn(ActionEvent event) {
        String id = txtPtId.getText();

        try {
            boolean isDeleted = PtMod.deletePatient(id);

            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Patient deleted!").show();
                loadAllPatients();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void onActionClearBtn(ActionEvent event) {
        clearFields();
    }

    public void onActionPatientsMouseEntered(MouseEvent mouseEvent) {
        lblPatients.setVisible(true);
    }

    public void onActionPatientsMouseExited(MouseEvent mouseEvent) {
        lblPatients.setVisible(false);
    }

    public void onActionPrescriptionMouseEntered(MouseEvent mouseEvent) {
        lblPrescription.setVisible(true);
    }

    public void onActionPrescriptionBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/prescription_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionPrescriptionMouseExited(MouseEvent mouseEvent) {
        lblPrescription.setVisible(false);
    }

    public void onActionPtId(ActionEvent event) {
        String id= txtPtId.getText();

        var model = new patientModel();

        try {
            patientDto dto = model.searchPatient(id);

            if(dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Patient not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            clearFields();
        }
    }

    private void fillFields(patientDto dto) {
        txtPtId.setText(dto.getPtId());
        txtName.setText(dto.getName());
        cmbCenters.setValue(dto.getCcId());
        txtTelNo.setText(dto.getTelNo());
        txtEmail.setText(dto.getEmail());
        txtDob.setText(dto.getDob());
        genderChoiceBox.setValue(dto.getGender());

    }
}
