/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ark.optimal.wallet.ui.main;

import ark.optimal.wallet.services.storageservices.StorageService;
import ark.optimal.wallet.ui.AlertController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mastadon
 */
public class FXMLExportWalletViewController implements Initializable {

    @FXML
    private JFXButton exportOkBtn;
    @FXML
    private JFXButton exportCancel;
    @FXML
    private JFXTextField walletName;
    @FXML
    private JFXTextField walletFolder;
    @FXML
    private JFXButton openDirectoryBtn;

    private Path walletPath;
    private Path walletDirectory;
    private Stage parentStage;
    private FXMLHomeViewController homeViewController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.walletPath = Paths.get(StorageService.getInstance().getWalletFilePath());
        this.walletDirectory = this.walletPath.getParent();
        this.walletName.setText(this.walletPath.getFileName().toString());
        this.walletFolder.setText(this.walletDirectory.toString());

    }

    @FXML
    private void onExportOk(ActionEvent event) {
        StorageService.getInstance().saveWallet(this.walletDirectory.toString() + "/" + this.walletName.getText());
        closeWindow();
        new AlertController().successMessage(this.walletDirectory.toString() + "/" + this.walletName.getText());

    }

    @FXML
    private void onExportCancel(ActionEvent event) {
        closeWindow();
    }

    @FXML
    private void onOpenWalletDirectory(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(this.walletPath.getParent().toFile());
        File dir = chooser.showDialog(parentStage.getScene().getWindow());
        if (dir == null) {
            return;
        }
        this.walletFolder.setText(Paths.get(dir.getAbsolutePath()).toString());
        this.walletDirectory = Paths.get(dir.getAbsolutePath());

    }

    public void setHomeViewController(FXMLHomeViewController homeViewController) {
        this.homeViewController = homeViewController;
    }

    void setStage(Stage stage) {
        this.parentStage = stage;
    }

    private void closeWindow() {
        Stage stage = (Stage) walletName.getScene().getWindow();
        stage.close();
    }

}
