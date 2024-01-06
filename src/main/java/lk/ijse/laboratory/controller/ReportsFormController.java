package lk.ijse.laboratory.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.laboratory.Dto.*;
import lk.ijse.laboratory.Dto.Tm.prescriptionTm;
import lk.ijse.laboratory.Dto.Tm.ptTestDetailsTm;
import lk.ijse.laboratory.Dto.Tm.resultTm;
import lk.ijse.laboratory.Model.*;
import lk.ijse.laboratory.db.DbConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.eclipse.jdt.internal.compiler.parser.AbstractCommentParser;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static lk.ijse.laboratory.Util.EmailController.SendEmail;

public class ReportsFormController {
    private final reportModel ReportModel = new reportModel();
    public TableView<ptTestDetailsTm> tblReports;
    public TableColumn<?,?> colDate;
    public TableColumn<?,?> colPatientId;
    public TableColumn<?,?> colPatientName;
    public TableColumn<?,?> colTestName;
    public TableColumn<?,?> colComment1;
    public ComboBox cmbPresId;
    public TextField txtPtName;
    public TextField txtComment;
    public ComboBox cmbTestCode;
    public Label lblDate;
    public TextField txtTestName;
    public TextField txtPtId;
    public JFXButton BtnGetReport;
    @FXML
    private AnchorPane root;

    public static String TD;
    public static String  PD;
    public static String  Prescription;
    public static boolean Stadded;

    public void initialize(){
        loadPrescriptionIds();
        setCellValueFactory();
        loadAllReports();
        setDate();
       // BtnGetReport.setDisable(true);
    }

    private void loadAllReports() {
        ObservableList<ptTestDetailsTm> obList = FXCollections.observableArrayList();

        try {
            List<ptTestDetailsDto> dtoList = ReportModel.getAllReports();

            for(ptTestDetailsDto dto : dtoList) {
                prescriptionDto Prdto = prescriptionModel.SearchPrescriptionById(dto.getPresId());

                if (Prdto != null) {
                    patientDto pDto = patientModel.searchPatient(Prdto.getPtId());
                    testDto TDto = testModel.searchTestById(dto.getTestId());


                    obList.add(
                            new ptTestDetailsTm(
                                    (Date) dto.getDate(),
                                    pDto.getPtId(),
                                    pDto.getName(),
                                    TDto.getTest(),
                                    dto.getStatus()

                            )
                    );
                }
            }

            tblReports.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colTestName.setCellValueFactory(new PropertyValueFactory<>("testName"));
        colComment1.setCellValueFactory(new PropertyValueFactory<>("status"));

    }

    private void loadTestIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
            String presId = (String) cmbPresId.getValue();
        try {
            List<ptTestDetailsDto> testList = ReportModel.loadTestIds(presId);

            for (ptTestDetailsDto dto : testList) {
                obList.add(dto.getTestId());
            }
            cmbTestCode.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void loadPrescriptionIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<prescriptionDto> presList = prescriptionModel.loadAllPrescriptions();

            for (prescriptionDto dto : presList) {
                obList.add(dto.getPresId());
            }
            cmbPresId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDate() {
        String date = String.valueOf(LocalDate.now());
        lblDate.setText(date);
    }

    public void onActionBackBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/userDashboard_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    private void clearFields(){
        cmbPresId.setValue("");
        setDate();
        txtPtId.setText("");
        txtPtName.setText("");
        cmbTestCode.setValue("");
        txtTestName.setText("");
        txtComment.setText("");
    }

    public void onActionCmbPresidSearch(ActionEvent event) throws SQLException {
        String ptId = prescriptionModel.SearchPrescriptionByPresId((String) cmbPresId.getValue());
        txtPtId.setText(ptId);
        if (ptId != null) {
            try {
                patientDto dto = patientModel.searchPatient(ptId);

                if (dto != null) {
                    txtPtName.setText(dto.getName());
                }
                loadTestIds();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
            return;
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Prescription not found!").show();
        }
      //  BtnGetReport.setDisable(false);
    }

    public void onActionTestCodeSearch(ActionEvent event) {
        String id= (String) cmbTestCode.getValue();

        try {
            testDto dto = testModel.searchTestById(id);

            if(dto != null) {
               txtTestName.setText(dto.getTest());
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Test not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void onActionBtnGetReport(ActionEvent event) throws JRException, SQLException, MessagingException, IOException {
        String pres = (String) cmbPresId.getValue();
        patientDto PDto = patientModel.searchPatient(txtPtId.getText());
        prescriptionDto PRDto = prescriptionModel.SearchPrescriptionPresId(pres);
        testDto TDto = testModel.searchTestById(String.valueOf(cmbTestCode.getValue()));
      sectionDto SDto = sectionModel.searchSectionById(TDto.getSecId());
      machineDto MDto = machineModel.searchMachinesById(TDto.getMachineId());
      collectingCenterDto CDto = collectingCenterModel.searchCenterById(PDto.getCcId());
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> res = new ArrayList<String>();
        ArrayList<String> ranges = new ArrayList<String>();
        ArrayList<String> unit = new ArrayList<String>();


        String date = reportModel.getDate(pres);
      List<subTestDto> SubList = subTestModel.getAllsubTests(String.valueOf(cmbTestCode.getValue()));
        for (subTestDto Sdto : SubList) {
                name.add(Sdto.getName());
            String subId = String.valueOf(Sdto.getSubTestId());

            List<refferenceRangesDto> refList = refferenceRangeModel.getRangeList(subId);
            for(refferenceRangesDto refDto : refList){
                ranges.add(refDto.getRanges());
                unit.add(refDto.getUnit());
            }

          resultDto re = resultModel.getResults(subId, pres);
            res.add(re.getResult());
        }

        String age = null;
        LocalDate today = LocalDate.now();
        LocalDate dob = LocalDate.parse(PDto.getDob());
        int ag = Period.between(dob, today).getYears();
        if (ag >= 1) {
            age = String.valueOf(ag);
        } else {
            age = String.valueOf(Period.between(dob, today).getMonths());
        }

        String reffBy = "Self Investigation";
        if(PRDto.getRefferedBy().length() > 3){
            reffBy =PRDto.getRefferedBy();
        }
        String[] topic = new String[68];
        String tId = (String) cmbTestCode.getValue();
        String Tname =txtTestName.getText();
        String Tcomment = txtComment.getText();
        HashMap hashMap = new HashMap();

        hashMap.put("ptName", PDto.getName());
        hashMap.put("age", age);
        hashMap.put("gender", PDto.getGender());
        hashMap.put("refferedBy", reffBy);
        hashMap.put("section", SDto.getSecName());
        hashMap.put("date", date);
        hashMap.put("specimenType", TDto.getSampleType());
        hashMap.put("specimenId", pres);
        hashMap.put("ptId", PDto.getPtId());
        hashMap.put("machineName", MDto.getMachineName());
        hashMap.put("collectingCenter", CDto.getCenterName());
        hashMap.put("testName",Tname);
        hashMap.put("comment", Tcomment);

        String val = "  ";


        for (int j = 0, k = 1; j < topic.length; ) {
            topic[j] = "subTestName" + k;
            j++;
            topic[j] = "result" + k;
            j++;
            topic[j] = "referenceRange" + k;
            j++;
            topic[j] = "unit" + k;
            j++;
            k++;
        }


        for(int i =0,l=0;i< name.size();i++) {
            hashMap.put(topic[l] , name.get(i));
            l++;
            hashMap.put(topic[l],res.get(i));
             l++;
             if(ranges.size() <= name.size()) {
                 hashMap.put(topic[l], ranges.get(i));
             }
             l++;
             if(unit.size()<= name.size()) {
                 hashMap.put(topic[l], unit.get(i));
             }
             l++;
        }

        int ind = name.size() * 4;
        for(int i =ind;i< topic.length;i++){
            hashMap.put(topic[i],val);
        }
        InputStream resourceAsStream = getClass().getResourceAsStream("/reports/reps.jrxml");
        JasperDesign load = JRXmlLoader.load(resourceAsStream);

        JasperReport jasperReport = JasperCompileManager.compileReport(load);
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                hashMap,
                new JREmptyDataSource()
        );
        JasperViewer.viewReport(jasperPrint, false);
        JasperExportManager.exportReportToPdfFile(jasperPrint,"/home/kmgdk/intelij project/Medical-Laboratoey-Management-System/src/main/resources/testReports/Report.pdf");

        boolean isOk = reportModel.updateReport("Report Ready",txtComment.getText(), (String) cmbPresId.getValue(), (String) cmbTestCode.getValue());
       if(PDto.getEmail()!= null) {
           if (PRDto.getDuePayment() == 0) {
               //System.out.println("Mail ywan Nae");
            SendEmail(PDto.getName(), PDto.getEmail(), "Test Report","/home/kmgdk/intelij project/Medical-Laboratoey-Management-System/src/main/resources/testReports/Report.pdf" , 3);
           } else {
               //send mail report ready and pay and get tit
           }
       }
        clearFields();
    }

    public void onActionSubTestResults(ActionEvent event) throws IOException, SQLException {
        TD = (String) cmbTestCode.getValue();
        PD = (String) cmbPresId.getValue();
        Prescription = String.valueOf(cmbPresId.getValue());

        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/result_form.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = new Stage();
        stage.setTitle("Suwasahana Medical Laboratory");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }
}
