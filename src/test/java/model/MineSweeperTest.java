package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class MineSweeperTest {

    MineSweeper mineSweeper;

    @Test
    public void flag() {
        mineSweeper = new MineSweeper(10,10);
        mineSweeper.flag(1,1);
        assertEquals(true,((MineSweeperField)mineSweeper.table[1][1]).isFlagged());
        mineSweeper.flag(1,1);
        assertEquals(false,((MineSweeperField)mineSweeper.table[1][1]).isFlagged());
    }

    @Test
    public void shoot() {
        mineSweeper = new MineSweeper(10,10);
        mineSweeper.shoot(5,5);
        if (mineSweeper.table[5][5].text != 'M'){
            assertEquals(true,((MineSweeperField)mineSweeper.table[5][5]).isRevealed());
            mineSweeper.shoot(5,5);
            assertEquals(true,((MineSweeperField)mineSweeper.table[5][5]).isRevealed());
        }else{
            assertEquals(true,mineSweeper.isOver());
        }
    }

    @Test
    public void isOver() {
        mineSweeper = new MineSweeper(10,10);
        for (int i = 0; i < mineSweeper.getHeight(); i++) {
            for (int j = 0; j < mineSweeper.getWidth(); j++) {
                mineSweeper.shoot(i,j);
                if (mineSweeper.table[i][j].text == 'M'){
                    assertEquals(true,mineSweeper.isOver());
                    return;
                }else{
                    assertEquals(false,mineSweeper.isOver());
                }
            }

        }
    }

    @Test
    public void testToString() {
        mineSweeper = new MineSweeper(10,10);
        mineSweeper.table[0][0].flag();
        mineSweeper.table[0][0].setFieldText();
        assertEquals('F',mineSweeper.toString().charAt(0));

    }
}