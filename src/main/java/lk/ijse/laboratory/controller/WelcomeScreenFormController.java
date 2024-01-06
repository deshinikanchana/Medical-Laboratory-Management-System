package lk.ijse.laboratory.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Effect;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;

import static lk.ijse.laboratory.Util.EmailController.SendEmail;
import static lk.ijse.laboratory.db.DbConnection.verify;


public class WelcomeScreenFormController {
    @FXML
    private AnchorPane root;
    public void onActionBtnToLoginForm(ActionEvent event) throws IOException, MessagingException {
        verify = 2;
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }

    public void onActionNewAccount(ActionEvent event) throws IOException {
        verify =1;
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/newAccount_form.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = new Stage();
        stage.setTitle("Create A New Account");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

}