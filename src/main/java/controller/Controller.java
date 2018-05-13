package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import toplist.DomParser;
import toplist.Player;
import view.MineSweeperPane;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *  Controller osztály minden scene-hez.
 */
public class Controller {

    /**
     * {@link Controller} osztályhoz tartozó logger.
     */
    private static Logger logger = LoggerFactory.getLogger(Controller.class);

    /**
     * A {@link MineSweeperPane} mezőket tartalmazó játéktábla.
     */
    private MineSweeperPane[][] mineSweeperPanes;

    /**
     * A játékost reprezentálő {@link Player}.
     */
    private Player player;


    /**
     * A  címet tartalmazó {@link Label}.
     */
    @FXML
    private Label label;

    /**
     * A játékos nevét tartalmazó {@link TextField}.
     */
    @FXML
    private TextField usernameTF;


    /**
     * Elindítja a játékot a Minesweeper gomb lenyomására.
     * Létrehozza az új stage-et, ahol a játékos játszhatja a játékot.
     *
     * @param e Gomb lenyomása.
     */
    @FXML
    private void handleStartMineSweeperBtn(ActionEvent e){
        logger.info("Minesweeper button pressed");
        Stage stage = (Stage) label.getScene().getWindow();
        Parent root = null;

        MineSweeper g = new MineSweeper(10,10);
        root = createMineSweeperContent(g);

        Scene scene = new Scene(root);

        stage.setTitle("Mine sweeper");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Létrehozza a játéktáblát, és megadja a mezők viselkedését.
     * Létrehoz továbbá egy felugró ablakot ({@link Alert}),
     * melynek segítségével, tudatjuk a játékossal az elért eredményét.
     * Eltárolja a játékos által megadott nevet, ha nem ad meg nevet, akkor a
     * {@code System.getProperty("user.name")} értékét kapja meg.
     * Létrehoz egy stoppert, ami a pontszám kiszámításában játszik szerepet.
     * Ezek után minden egyes mezőhöz hozzárendeli a megfelelo viselkedéseket.
     *
     * @param g MineSweeper
     * @return pane
     */
    public Parent createMineSweeperContent(MineSweeper g) {
        Pane root = new Pane();
        root.setPrefSize(400, 400);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Game over");

        String playerName = usernameTF.getText();
        mineSweeperPanes = new MineSweeperPane[g.getHeight()][g.getWidth()];
        StopWatch stopWatch = new StopWatch();

        for (int x = 0; x < g.getHeight(); x++) {
            for (int y = 0; y < g.getWidth(); y++) {
                mineSweeperPanes[x][y] = new MineSweeperPane(g.table[x][y].getText(),x,y);
                root.getChildren().add(mineSweeperPanes[x][y]);
                int finalX = x;
                int finalY = y;
                mineSweeperPanes[x][y].setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.SECONDARY)){
                        g.flag(finalX,finalY);
                        rightClickOnField(g);
                    }
                    else{
                        g.shoot(finalX, finalY);
                    }
                    refreshMineSweeperContent(g);
                    if (g.isWin()){
                        stopWatch.stop();
                        DomParser.writeToTopMineSweeper(new Player(playerName,10000-(int)stopWatch.getTotalTimeSeconds()));
                        alert.setContentText("YOU WIN  " + playerName + ": " + (10000-(int)stopWatch.getTotalTimeSeconds()));
                        alert.showAndWait();
                        goToMainScreen(true);
                    }
                    if (g.isOver()){
                        stopWatch.stop();
                        alert.setContentText("YOU LOSE  " + playerName);
                        alert.showAndWait();
                        goToMainScreen(true);
                    }
                });
            }
        }

        stopWatch.start();
        logger.info("Minesweeper content created");
        return root;
    }

    /**
     * A játéktábla mezőinek frissítéséért felelős.
     * Minden mezőn végig megy és ha az fel van fedve a játékos számára,
     * akkor beállítja annak megjelenítendő tartalmát, majd láthatóvá teszi azt.
     *
     * @param g MineSweeper
     */
    public void refreshMineSweeperContent(MineSweeper g){
        logger.info("Refresh Minesweeper content");
        for (int x = 0; x < g.getHeight(); x++) {
            for (int y = 0; y < g.getWidth(); y++) {

                if(((MineSweeperField)g.table[x][y]).isRevealed()){
                    mineSweeperPanes[x][y].setTx(g.table[x][y].getText()+"");
                    mineSweeperPanes[x][y].tx.setVisible(true);
                }
            }
        }
    }

    /**
     * A másodlagos kattintásokat kezeli az egyes mezőkön.
     *
     * Jobb klikkre elhelyez egy kérdőjelet az adott mezőn, újabb jobb klikkre törli azt.
     *
     * @param g : MineSweeper
     */
    public void rightClickOnField(MineSweeper g){
        for (int x = 0; x < g.getHeight(); x++) {
            for (int y = 0; y < g.getWidth(); y++) {
                if (((MineSweeperField)g.table[x][y]).isFlagged()){
                    mineSweeperPanes[x][y].tx.setText("?");
                    mineSweeperPanes[x][y].tx.setVisible(true);
                }
                else{
                    mineSweeperPanes[x][y].setTx(g.table[x][y].getText()+"");
                    mineSweeperPanes[x][y].tx.setVisible(false);
                }

            }
        }
    }


    /**
     * Létrehoz egy új stage-et ahol megjeleníti a legjobb játékosokat.
     *
     * @param e Gomb lenyomás.
     */
    public void handleTopTen(ActionEvent e) {
        logger.info("Top 10 button pressed");
        Stage stage = (Stage) label.getScene().getWindow();

        FXMLLoader fl = new FXMLLoader(getClass().getResource("/fxml/topten.fxml"));
        try {
            Parent root = fl.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("mainscreen.css");

            stage.setTitle("Top 10");
            stage.setScene(scene);
            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     *  {@link ListView}, amely tartalmazza a legjobb 10 játékos nevét és pontszámát.
     */
    @FXML
    private ListView minesweeperListV;

    /**
     *  A get gomb lenyomására megmutatja a játkos toplistát.
     *
     *  @param e Gomb lenyomása.
     */
    public void handleMinesweeperGetBtn(ActionEvent e) {
        logger.info("Get button pressed");
        minesweeperListV.setOpacity(1);
        minesweeperListV.setItems(FXCollections.observableArrayList(DomParser.readFromTopMindeSweeper()));
    }

    /**
     * A Go back gomb lenyomására visszalép a főképernyőre.
     *
     * @param e Gomb lenyomása.
     */
    public void handleGoBackBtn(ActionEvent e) {
        logger.info("Go back button pressed");
        goToMainScreen(false);
    }

    /**
     * Létrehozza a főmenüt.
     *
     * @param b Az aktuális scene azonosítására szolgál.
     */
    public void goToMainScreen(boolean b){
        try {
            Stage stage;
            if (b == true)
                stage = (Stage) mineSweeperPanes[0][0].getScene().getWindow();
            else
                stage = (Stage) minesweeperListV.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));

            Scene scene = new Scene(root,600,400);
            scene.getStylesheets().add("mainscreen.css");

            stage.setTitle("Board Games");
            stage.setScene(scene);
            stage.show();
            logger.info("Opened main screen");
        } catch (Exception e) {
            logger.error("Error while opening main screen");
            e.printStackTrace();
        }
    }
}
