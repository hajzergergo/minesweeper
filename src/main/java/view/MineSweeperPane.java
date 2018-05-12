package view;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Az aknakereső egy mezőjének megjelenítéséért felelős osztály.
 */
public class MineSweeperPane extends StackPane {

    /**
     * Az egyes mezők mérete(40x40).
     */
    private static final int FIELD_SIZE = 40;

    /**
     * Mező x koordinátája.
     */
    private int x;

    /**
     * Mező y koordinátája.
     */
    private int y;

    /**
     * A mező tartalma.
     */
    public Text tx;

    /**
     * Fel van e fedve az adott mező.
     */
    private boolean revealed;

    /**
     * A mező kereteit szemléltető négyzet.
     */
    private Rectangle border = new Rectangle(38, 38);

    /**
     * Létrehozza az objektumot.
     *
     * @param c A mező tartalma.
     * @param x A mező x koordinátája.
     * @param y A mező y koordinátája.
     */
    public MineSweeperPane(char c, int x, int y) {
        this.x = x;
        this.y = y;
        this.tx = new Text(c + "");
        border.setStroke(Color.LIGHTGRAY);
        border.setFill(Paint.valueOf("#ffffff"));

        getChildren().addAll(border, tx);

        tx.setFont(Font.font(18));
        tx.setVisible(false);

        setTranslateX(x * FIELD_SIZE);
        setTranslateY(y * FIELD_SIZE);
    }

    /**
     * A mező tartalmának beállításáért felelős metódus.
     *
     * @param tx a mező kívánt tartalma.
     */
    public void setTx(String tx) {
        this.tx.setText(tx);
    }
}
