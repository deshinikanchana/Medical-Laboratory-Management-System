package lk.ijse.laboratory.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.laboratory.Dto.Tm.refferenceRangesTm;
import lk.ijse.laboratory.Dto.refferenceRangesDto;
import lk.ijse.laboratory.Dto.subTestDto;
import lk.ijse.laboratory.Model.subTestModel;
import lk.ijse.laboratory.Model.refferenceRangeModel;
import lk.ijse.laboratory.Model.testModel;

import java.sql.SQLException;
import java.util.List;

public class RefferenceRangesFormController {
    private final refferenceRangeModel RefMod = new refferenceRangeModel();

    public AnchorPane pane;

    public TextField txtRefRange;
    public TableView<refferenceRangesTm> tblRefRanges;
    public TableColumn<?,?> colCode;
    public TableColumn<?,?> colRefRange;
    public TextField txtTestCode;
    public ComboBox cmbSubTestId;
    public TextField txtSubTestName;
    public TableColumn<?,?> colSubTestCode;
    public TableColumn<?,?> colSubTestName;
    public TableColumn<?,?> colUnit;
    public TextField txtUnit;
    public TextField txtSubTestCode;

    public void initialize() throws SQLException {
        setCellValueFactory();
        loadAllRefferenceRanges();
        setTestCode();
        setSubTestCode();
        generateNextStId();
    }

    private void generateNextStId() {
        try {
            String testId = subTestModel.generateNextTestId();
            txtSubTestCode.setText(testId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setSubTestCode() throws SQLException {
        String id = subTestModel.getLastsubTestId();

        if (id != null) {
            String[] split = id.split("T");
            int code = Integer.parseInt(split[1]);
            if (code < 10) {
                code++;
                txtSubTestCode.setText("ST00" + code);
            } else {
                code++;
                txtSubTestCode.setText("ST0" + code);
            }
        }
        txtSubTestCode.setText("ST001");
    }


    private void setTestCode() {

        try {
            String id = RefMod.getLastTestCode();
            txtTestCode.setText(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllRefferenceRanges() {

        ObservableList<refferenceRangesTm> obList = FXCollections.observableArrayList();

        try {
            List<refferenceRangesDto> dtoList = RefMod.getAllRanges();

            for(refferenceRangesDto dto : dtoList) {
               subTestDto sdto = subTestModel.getsubTest(dto.getSubTestId());
                obList.add(
                        new refferenceRangesTm(
                                 sdto.getTestId(),
                                sdto.getSubTestId(),
                                sdto.getName(),
                                dto.getRanges(),
                                dto.getUnit()
                        )
                );
            }

            tblRefRanges.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("testCode"));
        colSubTestCode.setCellValueFactory(new PropertyValueFactory<>("subTestCode"));
        colSubTestName.setCellValueFactory(new PropertyValueFactory<>("subTest"));
        colRefRange.setCellValueFactory(new PropertyValueFactory<>("refferenceRange"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
    }

    public void onActionSaveBtn(ActionEvent event) {
        String testId = String.valueOf(cmbSubTestId.getValue());
        String ranges = txtRefRange.getText();
        String unit = txtUnit.getText();


        var dto = new refferenceRangesDto(testId,ranges,unit);

        try {
            boolean isSaved = RefMod.saveRanges(dto);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Refference Ranges saved Successfully !!!").show();
                loadAllRefferenceRanges();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    private void clearFields() {
        txtTestCode.setText("");
        txtUnit.setText("");
        txtRefRange.setText("");
        txtSubTestName.setText("");
        cmbSubTestId.getItems().clear();;
        txtSubTestCode.setText(" ");
    }

    public void onActionUpdateBtn(ActionEvent event) {
        String testId = (String) cmbSubTestId.getValue();
        String ranges = txtRefRange.getText();
        String unit = txtUnit.getText();


        var dto = new refferenceRangesDto(testId,ranges,unit);
        try {
            boolean isUpdated = RefMod.updateRanges(dto);

            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Refference Range updated !!!").show();
                loadAllRefferenceRanges();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    public void onActionDeleteBtn(ActionEvent event) {
        String id = (String) cmbSubTestId.getValue();

        try {
            boolean isDeleted = RefMod.deleteRanges(id);

            if(isDeleted) {
                tblRefRanges.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "Refference Range deleted!").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void onActionReferenceRange(ActionEvent event) {
        txtUnit.requestFocus();
    }

    public void onActionSubTestName(ActionEvent event) {
        txtRefRange.requestFocus();
    }
}
