package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class MineSweeperFieldTest {

    private MineSweeperField mf;

    @Test
    public void flag() {
        mf = new MineSweeperField();
        assertEquals(false,mf.isFlagged());
        mf.flag();
        assertEquals(true,mf.isFlagged());
        mf.flag();
        assertEquals(false,mf.isFlagged());
    }

    @Test
    public void setFieldText() {
        mf = new MineSweeperField();
        mf.setMinesNear(6);
        mf.setFieldText();
        assertEquals('6',mf.text);

        mf = new MineSweeperField();
        mf.setMined(true);
        mf.setFieldText();
        assertEquals('M',mf.text);

        mf = new MineSweeperField();
        mf.flag();
        mf.setFieldText();
        assertEquals('F',mf.text);

        mf = new MineSweeperField();
        mf.flag();
        mf.setMined(true);
        mf.setFieldText();
        assertEquals('F',mf.text);
    }
}