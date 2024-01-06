package lk.ijse.laboratory.controller;

import com.jfoenix.controls.JFXComboBox;
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
import lk.ijse.laboratory.Dto.*;
import lk.ijse.laboratory.Dto.Tm.ordersTm;
import lk.ijse.laboratory.Dto.Tm.prescriptionTm;
import lk.ijse.laboratory.Model.*;
import lk.ijse.laboratory.db.DbConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PrescriptionFormController {
    private final prescriptionModel  PresModel = new prescriptionModel();
    private final savePrescriptionModel  savePreModel = new savePrescriptionModel();
    private final testModel  TestMod = new testModel();
    private final patientModel  PtMod = new patientModel();
    private final ObservableList<prescriptionTm> obList = FXCollections.observableArrayList();

    public AnchorPane root;
    public TableColumn<?,?> colTestCode;
    public TableColumn<?,?> colTestName;
    public TableColumn<?,?> colTestPrice;
    public TableColumn<?,?> colTime;
    public JFXComboBox cmbPtId;
    public JFXComboBox cmbTestId;
    public Label lblPatient;
    public Label lblPrescription;
    public TableView<prescriptionTm> tblPrescription;
    public TextField txtPresId;
    public TextField txtTotalAmount;
    public TextField txtPaidAmount;
    public TextField txtDuePayment;
    public TextField txtPtName;
    public TextField txtPtAge;
    public TextField txtPrice;
    public TextField txtEstTime;
    public TextField txtRef;
    public TextField txtDate;
    public TextField txtTestName;
    public TextField txtCenterId;
    public TextField txtCenterName;

    public void initialize() {

        setCellValueFactory();
        generateNextPrescriptionId();
        loadAllTests();
        loadAllPatientIds();
        setDate();

    }

    private void setDate() {
        String date = String.valueOf(LocalDate.now());
        txtDate.setText(date);
    }

    private void loadAllPatientIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<patientDto> ptList = patientModel.getAllPatients();

            for (patientDto dto : ptList) {
                obList.add(dto.getPtId());
            }
            cmbPtId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void loadAllTests() {
        ObservableList<String> obListId = FXCollections.observableArrayList();
        try {
            List<testDto> testList = TestMod.loadAllTests();

            for (testDto dto : testList) {
                obListId.add(dto.getTestId());
            }
            cmbTestId.setItems(obListId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearFields() {

        txtPrice.setText("");
        txtEstTime.setText("");
        txtDuePayment.setText("");
        txtRef.setText("");
        txtPaidAmount.setText("");
        txtPtAge.setText("");
        txtPtName.setText("");
        txtTotalAmount.setText("");
        txtCenterId.setText("");
        txtTestName.setText("");
        txtCenterName.setText("");
        tblPrescription.getItems().clear();
        generateNextPrescriptionId();
    }

    private void clearadd(){
        txtPrice.setText(" ");
        txtEstTime.setText(" ");
        txtTestName.setText(" ");
    }
    private void generateNextPrescriptionId() {
        try {
            String id = PresModel.generateNextPrescriptionId();
               txtPresId.setText(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colTestCode.setCellValueFactory(new PropertyValueFactory<>("testCode"));
        colTestName.setCellValueFactory(new PropertyValueFactory<>("testName"));
        colTestPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("estTime"));
    }


    public void onActionBackBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/userDashboard_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionPatientsBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/patient_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }


    public void onActionCmbPtIdSearch(ActionEvent event) {
        String id = (String) cmbPtId.getValue();

        try {
            patientDto ptDto = patientModel.searchPatient(id);

            if(ptDto != null) {
                fillPtFields(ptDto);
                collectingCenterDto cDto = collectingCenterModel.searchCenterById(ptDto.getCcId());
                if(cDto != null) {
                    txtCenterName.setText(cDto.getCenterName());
                }
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Patient not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            clearFields();
        }

    }

    private void fillPtFields(patientDto ptDto) {
        String age = null;
        LocalDate dob = LocalDate.parse(ptDto.getDob());
        LocalDate today = LocalDate.now();
        int ag = Period.between(dob, today).getYears();
        if (ag >= 1) {
            age = String.valueOf(ag);
        } else {
            age = String.valueOf(Period.between(dob, today).getMonths());
        }

        txtPtName.setText(ptDto.getName());
        txtPtAge.setText(age);
        txtCenterId.setText(ptDto.getCcId());
    }

    public void onActionTestIdSearch(ActionEvent event) {
        String id = (String) cmbTestId.getValue();
        try {
            testDto dto = testModel.searchTestById(id);

            if(dto != null) {
              fillTestFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Test not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            clearFields();
        }
    }

    private void fillTestFields(testDto dto) {
        cmbTestId.setValue(dto.getTestId());
        txtTestName.setText(dto.getTest());
        txtEstTime.setText(dto.getEstimatedTime());
        txtPrice.setText(String.valueOf(dto.getPrice()));
    }


    public void onActionNewPtBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/patient_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionPatientsMouseEntered(MouseEvent mouseEvent) {
        lblPatient.setVisible(true);
    }

    public void onActionPatientsMouseExited(MouseEvent mouseEvent) {
        lblPatient.setVisible(false);
    }

    public void onActionPrescriptionBtnMouseEntered(MouseEvent mouseEvent) {
        lblPrescription.setVisible(true);
    }

    public void onActionPrescriptionBtnMouseExited(MouseEvent mouseEvent) {
        lblPrescription.setVisible(false);
    }

    public void onActionPresId(ActionEvent event) {
        String id = txtPresId.getText();
        try {
            prescriptionDto dto = PresModel.SearchPrescriptionById(id);

            if(dto != null) {
                patientDto ptDto = PtMod.searchPatient(dto.getPtId());

                if(ptDto != null) {
                    collectingCenterDto Cdto = collectingCenterModel.searchCenterById(ptDto.getCcId());

                    if(Cdto != null) {
                        txtCenterName.setText(Cdto.getCenterName());
                    } else {
                        new Alert(Alert.AlertType.INFORMATION, "Collecting Center not found!").show();
                    }
                    fillPtFields(ptDto);
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Patient not found!").show();
                }
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Prescription not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            clearFields();
        }
    }

    public void onActionBtnPaidAmount(ActionEvent event) {
        float price = Float.parseFloat(txtPaidAmount.getText());
        float total = Float.parseFloat(txtTotalAmount.getText());

        float due = (total - price);
        txtDuePayment.setText(String.valueOf(due));
    }

    public void onActionSaveBtn(ActionEvent event) throws SQLException, JRException {
        String presID = txtPresId.getText();
        String ptId = (String) cmbPtId.getValue();
        String refBy = txtRef.getText();
        Float TotalAmount = Float.valueOf(txtTotalAmount.getText());
        Float duePayment = Float.valueOf(txtDuePayment.getText());
        Date date = Date.valueOf(txtDate.getText());


        List<prescriptionTm> tmList = new ArrayList<>();


        for (prescriptionTm PTm : obList) {
            tmList.add(PTm);
        }


        var savedDto = new SavePrescriptionDto(
                presID,
                ptId,
                refBy,
                duePayment,
                TotalAmount,
                date,
                tmList
        );
        System.out.println("meka ok.me prescription controller eka");
        boolean y = savePreModel.savePrescription(savedDto);

        if(y) {
            generateReport(savedDto);
            clearFields();
        }
    }

    private void generateReport(SavePrescriptionDto savedDto) throws JRException, SQLException {
        patientDto pt = patientModel.searchPatient(savedDto.getPtId());
        collectingCenterDto cl = collectingCenterModel.searchCenterById(pt.getCcId());

        String[] topic = new String[12];
        ArrayList<String> test = new ArrayList<String>();
        ArrayList<Float> price = new ArrayList<Float>();

        List<prescriptionTm> tmList = new ArrayList<>(obList);


        for (prescriptionTm PTm : tmList) {
         test.add(PTm.getTestName());
         price.add(PTm.getPrice());

        }

            String age = null;
            LocalDate today = LocalDate.now();
            LocalDate dob = LocalDate.parse(pt.getDob());
            int ag = Period.between(dob, today).getYears();
            if (ag >= 1) {
                age = String.valueOf(ag);
            } else {
                age = String.valueOf(Period.between(dob, today).getMonths());
            }
            float pay = savedDto.getTotal() - savedDto.getDuePayment();
            String presId = savedDto.getPresId();
            String ptId = savedDto.getPtId();
            String ptName = pt.getName();
            String ptAge = age;
            String ptEmail = " ";
            if (pt.getEmail() != null) {
                ptEmail = pt.getEmail();
            }
            String reffBy = "Self Investigation";
            if(savedDto.getRefBy()!= null){
                reffBy =savedDto.getRefBy();
            }
            String colCenter = cl.getCenterName();
            String gender = pt.getGender();
            String total = String.valueOf(savedDto.getTotal());
            String due = String.valueOf(savedDto.getDuePayment());
            String paid = String.valueOf(pay);


            HashMap hashMap = new HashMap();

            hashMap.put("presId", presId);
            hashMap.put("ptId", ptId);
            hashMap.put("ptName", ptName);
            hashMap.put("ptAge", ptAge);
            hashMap.put("ptEmail", ptEmail);
            hashMap.put("reffBy", reffBy);
            hashMap.put("colCenter", colCenter);
            hashMap.put("gender", gender);
            hashMap.put("total", total);
            hashMap.put("paid", paid);
            hashMap.put("due", due);


            for (int j = 0, k = 1; j < topic.length; ) {
                topic[j] = "test" + k;
                j++;
                topic[j] = "t" + k + "Price";
                j++;
                k++;
            }

            for (int i = 0, l = 0; l < tmList.size()*2; ) {
                hashMap.put(topic[l], test.get(i));
                l++;
                hashMap.put(topic[l], String.valueOf(price.get(i)));
                l++;
                i++;
            }

            int ind = tmList.size()*2;
            for (int i = ind; i < 12; i++) {
                hashMap.put(topic[i], "    ");
            }


            InputStream resourceAsStream = getClass().getResourceAsStream("/reports/Receipt.jrxml");
            JasperDesign load = JRXmlLoader.load(resourceAsStream);

            JasperReport jasperReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    hashMap,
                    new JREmptyDataSource()
            );

            JasperViewer.viewReport(jasperPrint, false);
        }


        public void onActionAddBtn (ActionEvent event){
            String TestCode = (String) cmbTestId.getValue();
            String testName = txtTestName.getText();
            Float testPrice = Float.valueOf(txtPrice.getText());
            String time = txtEstTime.getText();

            obList.add(new prescriptionTm(
                    TestCode,
                    testName,
                    testPrice,
                    time
            ));

            tblPrescription.setItems(obList);
            calculateNetTotal();
            clearadd();
        }
        private void calculateNetTotal () {
            Float total = (float) 0;
            for (int i = 0; i < tblPrescription.getItems().size(); i++) {
                total += (float) colTestPrice.getCellData(i);
            }

            txtTotalAmount.setText(String.valueOf(total));
        }
    }
