package lk.ijse.laboratory.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.laboratory.Dto.Tm.stockUsageTm;
import lk.ijse.laboratory.Dto.Tm.resultTm;
import lk.ijse.laboratory.Dto.resultDto;
import lk.ijse.laboratory.Model.refferenceRangeModel;

import lk.ijse.laboratory.Dto.refferenceRangesDto;
import lk.ijse.laboratory.Dto.subTestDto;
import lk.ijse.laboratory.Model.resultModel;
import lk.ijse.laboratory.Model.subTestModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static lk.ijse.laboratory.controller.ReportsFormController.*;

public class ResultFormController {

    private final ObservableList<resultTm> obList = FXCollections.observableArrayList();
    public TableColumn colSubTestCode;
    public TableColumn colSubTestName;
    public TableColumn colResult;
    public TextField txtRefRange;
    public TableView tblSubTestResults;
    public AnchorPane pane;
    public TextField txtTestCode;
    public TextField txtSubTestName;
    public TextField txtUnit;
    public JFXComboBox cmbSubTest;
    public TextField txtResult;

    public void initialize() {
        setTestCode();
        loadAllSubTestIds(TD);
        setCellValueFactory();
    }


    private void setCellValueFactory() {
        colSubTestCode.setCellValueFactory(new PropertyValueFactory<>("subTestCode"));
        colSubTestName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colResult.setCellValueFactory(new PropertyValueFactory<>("result"));
    }

    private void loadAllSubTestIds(String id) {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<subTestDto> testList = subTestModel.loadSubTestIds(id);

            for (subTestDto dto : testList) {
                obList.add(dto.getSubTestId());
            }
            cmbSubTest.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void setTestCode() {
    txtTestCode.setText(TD);
    }

    public void onActionTestCode(ActionEvent event) {
        String code = txtTestCode.getText();

        loadAllSubTestIds(code);
    }

    public void onActionCmbSubTest(ActionEvent event) throws SQLException {
        String sId = (String) cmbSubTest.getValue();
        subTestDto dto = subTestModel.getsubTest(sId);
        txtSubTestName.setText(dto.getName());

        refferenceRangesDto rDto = refferenceRangeModel.getRanges(sId);
        txtRefRange.setText(rDto.getRanges());
        txtUnit.setText(rDto.getUnit());
        resultDto res = resultModel.getResults(sId,Prescription);
        if(res != null){
            txtResult.setText(res.getResult());
            txtResult.setEditable(false);
        }
        txtResult.requestFocus();
    }

    public void onActionTxtResult(ActionEvent event) {
        String sub = (String) cmbSubTest.getValue();
        String name = txtSubTestName.getText();
        String result =txtResult.getText();


        obList.add(new resultTm(
               sub,
                name,
                result
        ));

        tblSubTestResults.setItems(obList);
        tblSubTestResults.refresh();
        clearFields();
    }

    private void clearFields() {
        txtResult.setText(" ");
        txtUnit.setText("");
        txtRefRange.setText("");
    }

    public void onActionAddToReport(ActionEvent event) throws SQLException {

        List<resultTm> tmList = new ArrayList<>();

        for (resultTm Rtm : obList) {
            tmList.add(Rtm);
        }

        boolean isSaved = resultModel.saveResult(tmList,PD);
        if(isSaved) {
            Stadded = true;
            tblSubTestResults.getItems().clear();
            new Alert(Alert.AlertType.CONFIRMATION, "details Saved !!!").show();

        }else {
            new Alert(Alert.AlertType.ERROR, "Results Not Saved !!!").show();
        }
    }
}
