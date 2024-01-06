package lk.ijse.laboratory.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.laboratory.Dto.Tm.testdetailsTm;
import lk.ijse.laboratory.Dto.sectionDto;
import lk.ijse.laboratory.Dto.testDto;
import lk.ijse.laboratory.Model.sectionModel;
import lk.ijse.laboratory.Model.testModel;
import lk.ijse.laboratory.Model.instructionModel;
import lk.ijse.laboratory.Util.Translator;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TestDetailsFormController {

    public TableView<testdetailsTm> tblTests;
    public TableColumn<?,?> colTestId;
    public TableColumn<?,?> colTestsSection;
    public TableColumn<?,?> colTestName;
    public TableColumn<?,?> colSample;
    public TableColumn<?,?> colEstTime;
    public TableColumn<?,?> colDoctor;
    public TableColumn<?,?> colPrice;
    public TextField txtTestCode;
    public CheckBox chBoxLang;
    @FXML
    private AnchorPane root;

    public void initialize(){
        loadAllTestDetails();
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colTestId.setCellValueFactory(new PropertyValueFactory<>("testCode"));
        colTestName.setCellValueFactory(new PropertyValueFactory<>("testName"));
        colTestsSection.setCellValueFactory(new PropertyValueFactory<>("testSection"));
        colSample.setCellValueFactory(new PropertyValueFactory<>("sampleType"));
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("consultant"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colEstTime.setCellValueFactory(new PropertyValueFactory<>("estTime"));
    }

    private void loadAllTestDetails() {

        ObservableList<testdetailsTm> obList = FXCollections.observableArrayList();

        try {
            List<testDto> dtoList = testModel.loadAllTests();

            for(testDto dto : dtoList) {
                sectionDto sdto = sectionModel.searchSectionById(dto.getSecId());
                obList.add(
                        new testdetailsTm(
                                dto.getTestId(),
                                sdto.getSecName(),
                                dto.getTest(),
                                dto.getSampleType(),
                                dto.getEstimatedTime(),
                                sdto.getConsultant(),
                                dto.getPrice()
                        )
                );
            }

            tblTests.setItems(obList);
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


    public void onActionClearBtn(ActionEvent event) {
        txtTestCode.setText("");
        loadAllTestDetails();
    }


    public void onActionTxtTestCodeSearch(ActionEvent event) {
        String id = txtTestCode.getText();
        loadSearchTest(id);
    }

    private void loadSearchTest(String id) {
        ObservableList<testdetailsTm> obList = FXCollections.observableArrayList();

        try {
            List<testDto> dtoList = testModel.getTestLoad(id);

            for(testDto dto : dtoList) {
                sectionDto sdto = sectionModel.searchSectionById(dto.getSecId());
                obList.add(
                        new testdetailsTm(
                                dto.getTestId(),
                                sdto.getSecName(),
                                dto.getTest(),
                                dto.getSampleType(),
                                dto.getEstimatedTime(),
                                sdto.getConsultant(),
                                dto.getPrice()
                        )
                );
            }

            tblTests.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onActionGetInstructions(ActionEvent event) throws Exception {

        ArrayList<String> line = instructionModel.getInstructions("T005");
        HashMap hashMap = new HashMap();

        if(chBoxLang.isSelected()) {
            String Test = "T005";
            String Instructions = "INSTRUCTIONS";
            String id = testModel.getTestName(Test);

            hashMap.put("instructions", Instructions);
            hashMap.put("Test", id);

            String lines = line.get(0) + "\n\n" + line.get(1) + "\n\n" + line.get(2) + "\n\n" + line.get(3);

            System.out.println(lines);
            hashMap.put("lines", lines);
        }else{
            String Test = "T005";
            String Instructions = "INSTRUCTIONS";
            String id = testModel.getTestName(Test);

            /*String name = conSinhala(id);
            String instruct = conSinhala(Instructions);*/
            String line1 = "# රුධිර සාම්පල ලබාගැනීමට අවම වශයෙන් පැය 8කට පෙර සිට ජලය හැර වෙනත් කිසිවක් කන්න හෝ බොන්න එපා.";
            String line2 = "# ඔබ පානය කල යුත්තේ ජලය පමණි.";
            String line3 = "# ඔබේ රුධිරය පරීක්ෂාවට පෙර ජලය පානය කිරීමෙන් ඔබේ රුධිරය ලබා ගැනීම පහසු වේ.";
            String line4 = "# ඔබට දියවැඩියාව තිබේ නම්, ඔබ ඉන්සියුලීන් හෝ දියවැඩියා ඖෂධය උදෑසන මාත්\u200Dරාව ලබාගැනීම පෙර වෛද්\u200Dයවරයා නිර්දේශ කරයි නම් ඔබේ රුධිර සාම්පලය ලබා දෙන්න.";

            String lines = line1 + "\n\n" + line2 + "\n\n" + line3 + "\n\n" + line4;

            hashMap.put("instructions", "උපදෙස්");
            hashMap.put("Test", "FBS(නිරාහාර රුධිර සීනි)");

            hashMap.put("lines", lines);
        }
        InputStream resourceAsStream = getClass().getResourceAsStream("/reports/Instructions.jrxml");
        JasperDesign load = JRXmlLoader.load(resourceAsStream);

        JasperReport jasperReport = JasperCompileManager.compileReport(load);
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                hashMap,
                new JREmptyDataSource()
        );
        JasperViewer.viewReport(jasperPrint, false);

    }

    private String conSinhala(String id) throws Exception {
        String fromLang = "en";
        String toLang = "si";
        String text = id;
       String res = Translator.translate(fromLang,toLang,text);
       return res;

    }
}
