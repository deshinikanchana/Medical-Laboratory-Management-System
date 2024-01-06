package lk.ijse.laboratory.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.laboratory.Dto.Tm.stockItemTm;
import lk.ijse.laboratory.Dto.stockItemDto;
import lk.ijse.laboratory.Model.stockItemModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static lk.ijse.laboratory.db.DbConnection.newDto;

public class StocksFormController {

    @FXML
    public AnchorPane root;
    public TableView<stockItemTm> tblItemStock;
    public TableColumn<?,?> colItemCode;
    public TableColumn<?,?> colDescription;
    public TableColumn<?,?> colQtyOnHand;
    public TableColumn<?,?> colCategory;
    public TableColumn<?,?> colAction;
    public TextField txtItemCode;
    public TextField txtQtyOnHand;
    public TextField txtDescription;
    public TextField txtItemCategory;
    public Label lblStock;
    public Label lblUsage;
    public Label lblSuppliers;
    public Label lblOrders;
    public TextField txtWarning;
    private ActionEvent Event;

    public void initialize(){
        setCellValueFactory();
        loadAllItems();
        generateNextItemCode();
    }

    private void generateNextItemCode() {
        try {
            String itemCode = stockItemModel.generateNextItemCode();
            txtItemCode.setText(itemCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));

    }

    public void onActionBackBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/adminDashboard_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionSaveBtn(ActionEvent event) {
        String itemCode = txtItemCode.getText();
        String userId = newDto.getUserId();
        String description = txtDescription.getText();
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        String category = txtItemCategory.getText();
        int warn = Integer.parseInt(txtWarning.getText());

        var dto = new stockItemDto(itemCode,userId,description,qtyOnHand,category,warn);

        var model = new stockItemModel();
        try {
            boolean isSaved = model.saveItem(dto);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item saved Successfully !!!").show();
                loadAllItems();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtItemCode.setText("");
        txtDescription.setText("");
        txtItemCategory.setText("");
        txtQtyOnHand.setText("");
        txtWarning.setText(" ");
        generateNextItemCode();
    }

    private void loadAllItems() {
        try {
            List<stockItemDto> dtoList = stockItemModel.loadAllItems();

            ObservableList<stockItemTm> obList = FXCollections.observableArrayList();

            for(stockItemDto dto : dtoList) {
                Button btn = new Button("Order");
                btn.setCursor(Cursor.HAND);

                btn.setOnAction((e) -> {
                    ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
                    ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

                    Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to make an Order ?", yes, no).showAndWait();

                    if(type.orElse(no) == yes) {
                        try {
                            onActionOrdersBtn(Event);
                        } catch (IOException ex) {
                            new Alert(Alert.AlertType.ERROR, "Please Use Order Icon !!!").show();
                        }
                    }
                });

                var tm = new stockItemTm(
                        dto.getItemCode(),
                        dto.getDescription(),
                        dto.getQtyOnHand(),
                        dto.getCategory(),
                        btn
                );
                obList.add(tm);
            }
            tblItemStock.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onActionUpdateBtn(ActionEvent event) {
        String itemCode = txtItemCode.getText();
        String userId = newDto.getUserId();
        String description = txtDescription.getText();
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        String category = txtItemCategory.getText();
        int warning = Integer.parseInt(txtWarning.getText());

        var dto = new stockItemDto(itemCode,userId,description,qtyOnHand,category,warning);

        var model = new stockItemModel();
        try {
            boolean isUpdated = model.updateItem(dto);

            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item Details updated !!!").show();
                loadAllItems();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            clearFields();
        }

    }

    public void onActionDeleteBtn(ActionEvent event) {
        String id = txtItemCode.getText();

        var model = new stockItemModel();
        try {
            boolean isDeleted = model.deleteItem(id);

            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item deleted !!!").show();
                clearFields();
                loadAllItems();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void onActionClearBtn(ActionEvent event) {
clearFields();
    }

    public void onActionSearchTxtItemCode(ActionEvent event) {
        String id = txtItemCode.getText();

        var model = new stockItemModel();

        try {
            stockItemDto dto = model.searchItemId(id);

            if(dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Item not found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            clearFields();
        }
        txtItemCategory.requestFocus();
    }

    private void fillFields(stockItemDto dto) {
        txtItemCode.setText(dto.getItemCode());
        txtDescription.setText(dto.getDescription());
        txtQtyOnHand.setText(String.valueOf(dto.getQtyOnHand()));
        txtItemCategory.setText(dto.getCategory());
        txtWarning.setText(String.valueOf(dto.getWarningLevel()));
    }

    public void onActionOrdersBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/orders_form.fxml"));
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

    public void onActionStockUsageBtn(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/stockUsage_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionOrdersMouseEntererd(MouseEvent mouseEvent) {
lblOrders.setVisible(true);
    }

    public void onActionOrdersMouseExited(MouseEvent mouseEvent) {
lblOrders.setVisible(false);
    }

    public void onActionUsageMouseEntered(MouseEvent mouseEvent) {
lblUsage.setVisible(true);
    }

    public void onActionUsageMouseExited(MouseEvent mouseEvent) {
lblUsage.setVisible(false);
    }

    public void onActionSuppliersMouseExited(MouseEvent mouseEvent) {
lblSuppliers.setVisible(false);
    }

    public void onActionSuppliersMouseEntered(MouseEvent mouseEvent) {
lblSuppliers.setVisible(true);
    }

    public void onActionStocksMouseExited(MouseEvent mouseEvent) {
lblStock.setVisible(false);
    }

    public void onActionStocksMouseEntered(MouseEvent mouseEvent) {
lblStock.setVisible(true);
    }

    public void onActiontxtQty(ActionEvent event) {
        txtWarning.requestFocus();
    }

    public void onActiontxtDescription(ActionEvent event) {
        txtQtyOnHand.requestFocus();
    }

    public void onActiontxtCategory(ActionEvent event) {
        txtDescription.requestFocus();
    }
}
