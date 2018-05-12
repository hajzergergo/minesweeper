package model;

/**
 * Az aknakereső játék egy mezőjét reprezentáló osztály.
 */
public class MineSweeperField {


    /**
     * A mező tartalma.
     */
    public char text;

    /**
     * Környező aknák darabszáma.
     */
    private int minesNear;
    /**
     * A mező jelölt vagy nem.
     */
    private boolean flagged;
    /**
     * A mező tartalmaz-e aknát.
     */
    private boolean mined;
    /**
     * A mező fel van e fedve.
     */
    private boolean revealed;

    public MineSweeperField() {
        text = '-';
        this.flagged = false;
        this.mined = false;
        this.revealed = false;
    }

    public char getText(){
        return text;
    }

    public int getMinesNear() {
        return minesNear;
    }

    public void setMinesNear(int minesNear) {
        this.minesNear = minesNear;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public boolean isMined() {
        return mined;
    }

    public void setMined(boolean mined) {
        this.mined = mined;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    /**
     * Megjelöli a mezőt.
     */
    public void flag(){
        flagged = !flagged;
    }

    /**
     * Beállítja a mező tartalmát.
     */
    public void setFieldText()
    {
        if (flagged)
        {
            text = 'F';
            return;
        }
        if (mined)
        {
            text = 'M';
            return;
        }
        text = Character.forDigit(minesNear,10);
        return;
    }
}
