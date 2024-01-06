package lk.ijse.laboratory.controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.laboratory.Dto.SavePrescriptionDto;
import lk.ijse.laboratory.Dto.Tm.ordersTm;
import lk.ijse.laboratory.Dto.Tm.prescriptionTm;
import lk.ijse.laboratory.Dto.Tm.stockUsageTm;
import lk.ijse.laboratory.Dto.itemOrderDetailDto;
import lk.ijse.laboratory.Dto.stockItemDto;
import lk.ijse.laboratory.Dto.supplierDto;
import lk.ijse.laboratory.Model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdersFormController {
private final ObservableList<ordersTm> obList = FXCollections.observableArrayList();
    public TableView<ordersTm> tblOrders;
    public TableColumn<?,?> colDescription;
    public TableColumn<?,?> colOrderedQty;
    public TableColumn<?,?> colItemCode;
    public TextField txtOrderId;
    public JFXComboBox cmbItemCode;
    public TextField txtDescription;
    public TextField category;
    public TextField txtStockQty;
    public TextField txtQty;
    public JFXComboBox cmbSupplierId;
    public Label lblStocks;
    public Label lblUsage;
    public Label lblSuppliers;
    public Label lblOrders;
    public Label lblDate;

    public TextField txtPaid;
    public JFXButton clearBtn;
    public JFXButton addBtn;
    public JFXButton sendOrderRequestBtn;

    public void initialize() {
        setCellValueFactory();
        generateNextOrderId();
        setDate();
        loadSupplierIds();
        loadItemCodes();
        clearBtn.setDisable(true);
        addBtn.setDisable(true);
        sendOrderRequestBtn.setDisable(true);

    }

    private void loadItemCodes() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<stockItemDto> itemList = stockItemModel.loadAllItems();

            for (stockItemDto dto : itemList) {
                obList.add(dto.getItemCode());
            }
            cmbItemCode.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSupplierIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<supplierDto> supList = supplierModel.getAllSuppliers();

            for (supplierDto dto : supList) {
                obList.add(dto.getSupId());
            }
            cmbSupplierId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDate() {
        String date = String.valueOf(LocalDate.now());

        lblDate.setText(date);
    }

    private void generateNextOrderId() {
        try {
            String orderId = ordersModel.generateNextOrderId();
            txtOrderId.setText(orderId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void setCellValueFactory() {
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colOrderedQty.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
    }

    @FXML
private AnchorPane root;
    public void onActionBackBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/adminDashboard_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionStockUsageBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/stockUsage_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionStockBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/stocks_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionSuppliersBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/suppliers_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionSearchTxtOrderId(ActionEvent event) {

    }


    public void onActionSearchcmbItemCode(ActionEvent event) {
        String code = (String) cmbItemCode.getValue();

        txtQty.requestFocus();

        try {
            stockItemDto dto = stockItemModel.searchItemId(code);

            txtDescription.setText(dto.getDescription());
            txtStockQty.setText(String.valueOf(dto.getQtyOnHand()));
            category.setText(dto.getCategory());
            addBtn.setDisable(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    }

    public void onActionAddBtn(ActionEvent event) {

        cmbSupplierId.setDisable(true);
        String itemCode = (String) cmbItemCode.getValue();
        String desc = txtDescription.getText();
        int qty = Integer.parseInt(txtQty.getText());

        obList.add(new ordersTm(
                desc,
                qty,
                itemCode
        ));

        tblOrders.setItems(obList);
        tblOrders.refresh();
        clearAddRows();
        sendOrderRequestBtn.setDisable(false);
    }

    private void clearAddRows() {
        txtDescription.setText("");
        txtStockQty.setText("");
        txtQty.setText("");
        category.setText("");
    }

    public void onActionSendOrderRequestBtn(ActionEvent event) throws SQLException {
        cmbSupplierId.setDisable(false);
        String oId =txtOrderId.getText();
        String Sup = (String) cmbSupplierId.getValue();

        ObservableList<ordersTm> obList = FXCollections.observableArrayList();
        List<ordersTm> tmList = new ArrayList<>();

        for (ordersTm  oTm : obList) {
            tmList.add(oTm);
        }

        var savedDto = new itemOrderDetailDto(
                oId,
                Sup,
                tmList
        );

        boolean isSaved = OrderSaveModel.saveOrder(savedDto);
        if(isSaved) {
            tblOrders.getItems().clear();
            new Alert(Alert.AlertType.CONFIRMATION, "Order requested !!!").show();
            clearBtn.setDisable(false);
            generateNextOrderId();
        }else {
            new Alert(Alert.AlertType.ERROR, "Error !!!").show();
        }
    }

    public void onActionSearchcmbSupplierId(ActionEvent event) {
    }

    public void onActionStocksMouseEntered(MouseEvent mouseEvent) {
lblStocks.setVisible(true);
    }

    public void onActionStocksMouseExited(MouseEvent mouseEvent) {
lblStocks.setVisible(false);
    }

    public void onActionOrdersMouseExited(MouseEvent mouseEvent) {
lblOrders.setVisible(false);
    }

    public void onActionOrdersMouseEntered(MouseEvent mouseEvent) {
lblOrders.setVisible(true);
    }

    public void onActionSuppliersMouseEntered(MouseEvent mouseEvent) {
lblSuppliers.setVisible(true);
    }

    public void onActionSuppliersMouseExited(MouseEvent mouseEvent) {
lblSuppliers.setVisible(false);
    }

    public void onActionUsageMouseExited(MouseEvent mouseEvent) {
lblUsage.setVisible(false);
    }

    public void onActionUsageMouseEntered(MouseEvent mouseEvent) {
lblUsage.setVisible(true);
    }

    public void onActionClearBtn(ActionEvent event) {
        clearAddRows();
        cmbSupplierId.setDisable(false);
        generateNextOrderId();;
    }
}
