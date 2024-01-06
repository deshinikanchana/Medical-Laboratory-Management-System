package lk.ijse.laboratory.controller;


import com.jfoenix.controls.JFXButton;
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
import lk.ijse.laboratory.Model.*;
import lk.ijse.laboratory.Dto.Tm.SalaryTm;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRDesignViewerToolbar;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;


public class SalaryManageFormController {

    public TableView<SalaryTm> tblSalary;
    public TableColumn<?,?> colDate;
    public TableColumn<?,?> colEmpId;
    public TableColumn<?,?> colWrkingHurs;
    public TableColumn<?,?> colOt;
    public TableColumn<?,?>  colSalary;
    public TextField txtWorkedHoursCount;
    public TextField txtOtHours;
    public TextField txtEmployeeName;
    public JFXButton viewAttendanceBtn;
    public JFXComboBox cmbEmployeeId;
    public TextField txtSalaryAmount;
    public TextField txtSalaryCode;

    public AnchorPane root;
    public Label lblSalary;
    public Label lblViewAttendance;
    public Label lblDesignation;
    public Label lblEmployees;
    public Label lblDate;


    public void initialize(){
        loadEmployeeIds();
        setCellValueFactory();
        loadAllSalaries();
        generateNextSalaryId();
        setDate();
    }

    private void generateNextSalaryId() {
        try {
            String salId = salaryModel.generateNextSalaryId();
            txtSalaryCode.setText(salId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllSalaries() {
        var model = new salaryModel();

        ObservableList<SalaryTm> obList = FXCollections.observableArrayList();

        try {
            List<salaryDto> dtoList = model.getAllsalaries();

            Date date = Date.valueOf(LocalDate.now());
            int mon = date.getMonth() + 1;

            for(salaryDto dto : dtoList) {
                float TotalHours = attendanceModel.calculateTotalHours(dto.getEmpId(),mon);
                 int WrkingHrs  = employeeModel.getJobId(dto.getEmpId());
                Float otHours= getOtHours(WrkingHrs,TotalHours);
                obList.add(
                        new SalaryTm(
                                dto.getPaidDate(),
                                dto.getEmpId(),
                              TotalHours,
                                otHours,
                               dto.getAmount()
                        )
                );
            }

            tblSalary.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colOt.setCellValueFactory(new PropertyValueFactory<>("OTHours"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("Salary"));
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("EmpId"));
        colWrkingHurs.setCellValueFactory(new PropertyValueFactory<>("TotalWorkedHours"));
    }

    private void loadEmployeeIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<employeeDto> empList = employeeModel.loadAllemployees();

            for (employeeDto dto : empList) {
                obList.add(dto.getEmpId());
            }
            cmbEmployeeId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDate() {
        String date = String.valueOf(LocalDate.now());

        lblDate.setText(date);
    }


    public void onActionBackBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/adminDashboard_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void cmbEmployeeIdSearchOnAction(ActionEvent event) throws SQLException {
        String id = (String) cmbEmployeeId.getValue();
        try {
            employeeDto dto = employeeModel.searchEmployeeById(id);
            if(dto != null) {
                txtEmployeeName.setText(dto.getName());

                Date date = Date.valueOf(lblDate.getText());
                int mon = (date.getMonth() + 1);

                float TotalHours = attendanceModel.calculateTotalHours(dto.getEmpId(),mon);
                int WrkingHrs  = employeeModel.getJobId(dto.getEmpId());
                Float otHours=getOtHours(WrkingHrs,TotalHours);
               Double salary = calculateSalary(TotalHours,otHours,dto.getJobId());
                fillFields(date,dto.getName(),TotalHours,otHours,salary);
                    return;
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            clearFields();
        }

    }

    private Float getOtHours(int must, float worked) {
        float ot = 0;

        if(LocalDate.now().getDayOfMonth() > 25 && worked > must ) {

            ot = worked - must;
        }

        return ot;
    }

    private void fillFields(Date date, String name, Float totalHours, Float otHours, Double salary) {
        lblDate.setText(String.valueOf(date));
        txtEmployeeName.setText(name);
        txtWorkedHoursCount.setText(String.valueOf(totalHours));
        txtOtHours.setText(String.valueOf(otHours));
        txtSalaryAmount.setText(String.valueOf(salary));
    }

    private Double calculateSalary(Float totalHours, Float otHours, String jobId) {

        var model = new designationModel();
        Double salary = 0.0;
        Double otSal =0.0;
        try {
            designationDto dto = model.searchDesignation(jobId);
            if (dto != null) {
                int wrkingHrs = dto.getWorkingHoursPerMonth();

                    Double nop = 0.0;
                    if (LocalDate.now().getDayOfMonth() > 25 && totalHours < wrkingHrs) {
                        nop = Double.valueOf((wrkingHrs) - (totalHours) * dto.getOtPaymentsPerHour());
                        Double sal = (dto.getBasicSalary() - nop) + otSal;
                        salary = sal - (sal * (8/100));
                        return salary;
                    }else if (totalHours > wrkingHrs) {
                     otSal = Double.valueOf(otHours * (dto.getOtPaymentsPerHour()));
                        Double sal = (dto.getBasicSalary() - nop) + otSal;
                        salary = sal - (sal * (8/100));
                        return salary;
                  }
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Salary not calculated!").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return salary;
    }
    private salDto paySlip(Float totalHours, Float otHours, designationDto dto) {

        salDto sol = null;

        Double salary = 0.0;
        Double otSal =0.0;
        double epf =0.0;
        if (dto != null) {
            int wrkingHrs = dto.getWorkingHoursPerMonth();

            double nop = 0.0;
            if (LocalDate.now().getDayOfMonth() > 25 && totalHours < wrkingHrs) {
                sol.setNopayHrs(wrkingHrs-totalHours);
                nop = Double.valueOf((wrkingHrs) - (totalHours) * dto.getOtPaymentsPerHour());
                float no = (float) nop;
                sol.setNopay(no);
                Double sal = (dto.getBasicSalary() - nop) + otSal;
                salary = sal - (sal * (8/100));
                epf = (dto.getBasicSalary()-nop) *(8/100);
                sol.setNetsal(salary);
                sol.setOtPay(0.0);
                sol.setEpfCut(epf);
                sol.setOtHrs(0.0);

            }else if (totalHours > wrkingHrs) {
                otSal = Double.valueOf(otHours * (dto.getOtPaymentsPerHour()));
                sol.setOtHrs(Double.valueOf(otHours));
                sol.setOtPay(otSal);
                Double sal = (dto.getBasicSalary() - nop) + otSal;
                salary = sal - (sal * (8/100));
                epf = dto.getBasicSalary() * (8/100);
                sol.setNetsal(salary);
                sol.setNopay(0.0F);
                sol.setNopayHrs(0);
                sol.setEpfCut(epf);



            }
        }
        return sol;
    }

    private void clearFields() {
        txtEmployeeName.setText("");
        txtSalaryAmount.setText("");
        txtWorkedHoursCount.setText("");
        txtOtHours.setText("");
        cmbEmployeeId.setValue("");
        setDate();
        generateNextSalaryId();
    }

    public void onActionEmployeeManageBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/employee_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionManageDesigntionsBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/designationManage_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionViewAttendanceBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/viewAttendance_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionBtnPaid(ActionEvent event) {
        String SalId = txtSalaryCode.getText();
        String empId = (String) cmbEmployeeId.getValue();
        Double amount = Double.valueOf(txtSalaryAmount.getText());
        Date paidDate = Date.valueOf(lblDate.getText());


        var dto = new salaryDto(SalId,empId,amount,paidDate);

        var model = new salaryModel();
        try {
            boolean isSaved = model.savePaidDetails(dto);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment Saved Successfully !!!").show();
                loadAllSalaries();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void onActionSalaryMouseEntered(MouseEvent mouseEvent) {
     lblSalary.setVisible(true);
    }

    public void onActionSalaryMouseExited(MouseEvent mouseEvent) {
lblSalary.setVisible(false);
    }

    public void onActionEmployeeMouseEntered(MouseEvent mouseEvent) {
lblEmployees.setVisible(true);
    }

    public void onActionEmployeeMouseExited(MouseEvent mouseEvent) {
lblEmployees.setVisible(false);
    }

    public void onActionDesignationMouseEntered(MouseEvent mouseEvent) {
lblDesignation.setVisible(true);
    }

    public void onActionDesignationMouseExited(MouseEvent mouseEvent) {lblDesignation.setVisible(false);}

    public void onActionViewAttendanceMouseExited(MouseEvent mouseEvent) {
lblViewAttendance.setVisible(false);
    }

    public void onActionViewAttendanceMouseEntered(MouseEvent mouseEvent) {
lblViewAttendance.setVisible(true);
    }

    public void onActionPaySlipBtn(ActionEvent event) throws JRException, SQLException {

        String empId = (String) cmbEmployeeId.getValue();
        employeeDto Emdto = employeeModel.searchEmployeeById(empId);
        designationDto DeDto = designationModel.searchDesignation(Emdto.getJobId());
        salaryDto SalDto = salaryModel.getSalary(empId);
        Date date = Date.valueOf(LocalDate.now());
        int mon = date.getMonth() + 1;
        String Today = String.valueOf(date);
        float TotalHours = attendanceModel.calculateTotalHours(empId,mon);

        Float otHours=getOtHours(DeDto.getWorkingHoursPerMonth(),TotalHours);
       // salDto sal = paySlip(TotalHours,otHours,DeDto);
      double epSal = (DeDto.getBasicSalary());
        double gr = epSal;
        double epf = epSal * 12/100;
        double etf = epSal * 3/100;
        double epfCut = DeDto.getBasicSalary()*8/100;
    double ans = epSal - epfCut;
        String basic = String.valueOf(DeDto.getBasicSalary());
        String noH = "0.0";
        String nop = "0.0";
        String totEpf = String.valueOf(epSal);
        String oth = "0.0";
        String otpay = "0.0";
        String gross = String.valueOf(gr);
        String ep = String.valueOf(epfCut);
        String ded = "0.0";
        String net = String.valueOf(ans);
        String epfS = String.valueOf(epf);
        String etfS = String.valueOf(etf);

        int count = attendanceModel.getCount(empId,mon);
        String days = String.valueOf(count);
        HashMap hashMap = new HashMap();

        hashMap.put("date", Today);
        hashMap.put("name", Emdto.getName());
        hashMap.put("designation", DeDto.getJobTitle());
        hashMap.put("empId",Emdto.getEmpId());
        hashMap.put("workedDays", days);
        hashMap.put("basicSalary", basic);
        hashMap.put("noPayHrs", noH);
        hashMap.put("nopayDeduction", nop);
        hashMap.put("totalEPF", totEpf);
        hashMap.put("otHrs", oth);
        hashMap.put("otAmount", otpay);
        hashMap.put("grossSal", gross);
        hashMap.put("epf8%", ep);
        hashMap.put("TotalDeduction", ded);
        hashMap.put("netSal", net);
        hashMap.put("EPF", epfS);
        hashMap.put("ETF", etfS);

        InputStream resourceAsStream = getClass().getResourceAsStream("/reports/Pay_Slip.jrxml");
        JasperDesign load = JRXmlLoader.load(resourceAsStream);

        JasperReport jasperReport = JasperCompileManager.compileReport(load);
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                hashMap,
                new JREmptyDataSource()
        );

        JasperViewer.viewReport(jasperPrint, false);
        clearFields();
    }
}