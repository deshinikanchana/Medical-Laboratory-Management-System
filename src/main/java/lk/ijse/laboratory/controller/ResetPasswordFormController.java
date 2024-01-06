package lk.ijse.laboratory.controller;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.laboratory.Model.userModel;


import java.io.IOException;
import java.sql.SQLException;

import static javafx.fxml.FXMLLoader.load;
import static lk.ijse.laboratory.controller.ForgetPasswordFormController.ud;

public class ResetPasswordFormController {
    public AnchorPane pane;
    public TextField txtPassword;
    public TextField txtConfirmPw;
    public PasswordField pwfPw;
    public PasswordField pwfConfirmPw;
    public CheckBox choiceBoxShowPw;

    public void onActionSubmitBtn(ActionEvent event) throws SQLException, IOException {
        String pw = txtPassword.getText();
        String cpw = txtConfirmPw.getText();
        pw = pwfPw.getText();
        cpw = pwfConfirmPw.getText();

        if(pw != null && cpw != null) {
            if(pw.equals(cpw)) {
                ud.setPassword(pw);
                boolean ans = userModel.resetPassword(ud);

                if(ans){
                    AnchorPane anchorPane = load(getClass().getResource("/view/successAlert_form.fxml"));
                    Stage stage = (Stage) pane.getScene().getWindow();

                    stage.setScene(new Scene(anchorPane));
                    stage.centerOnScreen();
                    return;
                }
                new Alert(Alert.AlertType.ERROR, "Password Reset Failed !!!").show();
            }
            new Alert(Alert.AlertType.ERROR, "Passwords Not Matched !!!").show();
            return;
        }
        new Alert(Alert.AlertType.ERROR, "All Fields Are Required !!!").show();
    }

    public void onActionChoiceBoxShowPw(ActionEvent event) {

        if(choiceBoxShowPw.isSelected()){
            txtPassword.setText(pwfPw.getText());
            pwfPw.setVisible(false);
            txtPassword.setVisible(true);

            txtConfirmPw.setText(pwfConfirmPw.getText());
            pwfConfirmPw.setVisible(false);
            txtConfirmPw.setVisible(true);

            return;
        }
        pwfPw.setText(txtPassword.getText());
        txtPassword.setVisible(false);
        pwfPw.setVisible(true);

        pwfConfirmPw.setText(txtConfirmPw.getText());
        txtConfirmPw.setVisible(false);
        pwfConfirmPw.setVisible(true);

    }
}
