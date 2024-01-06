package lk.ijse.laboratory.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.laboratory.Dto.Tm.EmployeeTm;
import lk.ijse.laboratory.Dto.Tm.collectingCenterTm;
import lk.ijse.laboratory.Dto.Tm.sectionTm;
import lk.ijse.laboratory.Dto.collectingCenterDto;
import lk.ijse.laboratory.Dto.employeeDto;
import lk.ijse.laboratory.Dto.sectionDto;
import lk.ijse.laboratory.Model.collectingCenterModel;



import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static lk.ijse.laboratory.db.DbConnection.newDto;

public class CollectingCentersManageFormController {

    public AnchorPane root;
    public TableView<collectingCenterTm> tblCollectingCenters;
    public TableColumn<?,?> colCenterId;
    public TableColumn<?,?> colCenterName;
    public TableColumn<?,?> colAddress;
    public TableColumn<?,?> colTel;
    public TableColumn<?,?> colEmail;
    public TableColumn<?,?> colSampleCount;
    public TextField txtCenterId;
    public TextField txtAddress;
    public TextField txtCenterName;
    public TextField txtTelNo;
    public TextField txtEmail;
    public Label lblTests;
    public Label lblSections;
    public Label lblMachines;
    public Label lblCollectionCenter;

    public void initialize(){
        setCellValueFactory();
        loadAllCenters();
        generateNextCenterId();
    }

    private void generateNextCenterId() {
        try {
            String centerId = collectingCenterModel.generateNextCenterId();
            txtCenterId.setText(centerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllCenters() {
        var model = new collectingCenterModel();

        ObservableList<collectingCenterTm> obList = FXCollections.observableArrayList();

        try {
            List<collectingCenterTm> dtoList = model.getAllSections();

            for(collectingCenterTm dto : dtoList) {
                obList.add(
                        new collectingCenterTm(
                                dto.getCcId(),
                                dto.getCenterName(),
                                dto.getAddress(),
                                dto.getTelNo(),
                                dto.getEmail(),
                                dto.getSampleCount()
                        )
                );
            }

            tblCollectingCenters.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colCenterId.setCellValueFactory(new PropertyValueFactory<>("ccId"));
        colCenterName.setCellValueFactory(new PropertyValueFactory<>("centerName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("telNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSampleCount.setCellValueFactory(new PropertyValueFactory<>("sampleCount"));
    }


    public void onActionBackBtn(ActionEvent event) throws IOException {
        if(newDto.getUserType().equals("Admin")) {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/adminDashboard_form.fxml"));
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new Scene(anchorPane));
            stage.centerOnScreen();
        }else{
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/userDashboard_form.fxml"));
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(new Scene(anchorPane));
            stage.centerOnScreen();
        }
    }

    public void onActionManageTestsBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/test_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionManageSectionsBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/sectionsManage_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionSaveBtn(ActionEvent event) {
        String ccId = txtCenterId.getText();
        String centerName = txtCenterName.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String tel = txtTelNo.getText();

        var dto = new collectingCenterDto(ccId,centerName,address,tel,email);

        var model = new collectingCenterModel();
        try {
            boolean isSaved = model.saveCenter(dto);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "new Collecting Center saved Successfully !!!").show();
                loadAllCenters();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtCenterId.setText("");
        txtCenterName.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
        txtTelNo.setText("");

    }

    public void onActionUpdateBtn(ActionEvent event) {
        String ccId = txtCenterId.getText();
        String centerName = txtCenterName.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String tel = txtTelNo.getText();


        var dto = new collectingCenterDto(ccId,centerName,address,tel,email);

        var model = new collectingCenterModel();
        try {
            boolean isUpdated = model.updateCenter(dto);

            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Collecting Center updated Successfully!!!").show();
                loadAllCenters();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            clearFields();
        }
    }

    public void onActionDeleteBtn(ActionEvent event) {
        String ccId = txtCenterId.getText();

        var centerModel = new collectingCenterModel();
        try {
            boolean isDeleted = centerModel.deleteCenter(ccId);

            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "collecting Center deleted !!!").show();
                clearFields();
                loadAllCenters();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void onActionClearBtn(ActionEvent event) {
        clearFields();
    }

    public void onActionManageTestMachinesBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/machinesManage_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionSearchTxtCenterId(ActionEvent event) {
        String id= txtCenterId.getText();

        var model = new collectingCenterModel();

        try {
            collectingCenterDto dto = model.searchCenterById(id);

            if(dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Section Id not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            clearFields();
        }
    }

    private void fillFields(collectingCenterDto dto) {
        txtCenterId.setText(dto.getCcId());
        txtCenterName.setText(dto.getCenterName());
        txtAddress.setText(dto.getAddress());
        txtEmail.setText(dto.getEmail());
        txtTelNo.setText(dto.getTelNo());
    }

    public void onActiontestBtnMouseEntered(MouseEvent mouseEvent) {
        lblTests.setVisible(true);
    }

    public void onActionSectionBtnMouseEntered(MouseEvent mouseEvent) {
        lblSections.setVisible(true);
    }

    public void onActionMachinesBtnMouseEntered(MouseEvent mouseEvent) {
        lblMachines.setVisible(true);
    }

    public void onActiontestBtnMouseExited(MouseEvent mouseEvent) {
        lblTests.setVisible(false);
    }

    public void onActionSectionBtnMouseExited(MouseEvent mouseEvent) {
        lblSections.setVisible(false);
    }

    public void onActionMachinesBtnMouseExited(MouseEvent mouseEvent) {
        lblMachines.setVisible(false);
    }

    public void onActionCentersBtnMouseEntered(MouseEvent mouseEvent) {
        lblCollectionCenter.setVisible(true);
    }

    public void onActionCentersBtnMouseExited(MouseEvent mouseEvent) {
        lblCollectionCenter.setVisible(false);
    }
}
