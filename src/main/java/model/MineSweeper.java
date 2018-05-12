package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Minesweeper játékot reprezentáló osztály.
 */
public class MineSweeper {

    private static Logger logger = LoggerFactory.getLogger(MineSweeper.class);

    /**
     * Játék nehézsége.
     *
     * Nagyobb szám esetén a játék könnyebb.
     */
    private static int minesCounterHelper = 20; // 20 = every 20th field is mine, 1 = every single field is mine


    /**
     * Játék végét jelöli.
     */
    private boolean isOver = false;


    /**
     * Győzelmet jelöli.
     */
    private boolean win = false;

    public boolean isWin() {
        return win;
    }

    /**
     * A játékmezőket összefoglaló tábla.
     */
    public MineSweeperField[][] table;

    /**
     * Létrehozza a Minesweeper játékot, adott oldalhosszúságokkal.
     *
     * @param width A játékmező szélessége.
     * @param height A játékmező magassága.
     */
    public MineSweeper(int width, int height) {
        this.table = new MineSweeperField[width][height];
        Random rn = new Random();
        int x, y;
        for (int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                table[i][j] = new MineSweeperField();
            }
        }

        int mines = width * height / minesCounterHelper;
        while (mines > 0)
        {
            x = rn.nextInt(height);
            y = rn.nextInt(width);
            if (!((MineSweeperField)table[x][y]).isMined())
            {
                ((MineSweeperField)table[x][y]).setMined(true);
                mines--;
            }
        }

        for (int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                ((MineSweeperField)table[i][j]).setMinesNear(countMinesNear(i,j));
                ((MineSweeperField)table[i][j]).setFieldText();
            }
        }
    }

    public int getWidth(){
        return table[0].length;
    }

    public int getHeight(){
        return table.length;
    }

    public boolean isOver() {
        return isOver;
    }

    /**
     * Az adott koordinátákon elhelyezkedő mezőt megjelöli.
     *
     * @param x A mező x koordinátája.
     * @param y A mező y koordinátája.
     */
    public void flag(int x, int y){
        ((MineSweeperField)table[x][y]).flag();
        logger.info("Player put a flag on (" + x + ":" + y + ") field");
    }

    /**
     * Az x,y koordinátájú mezőre lead egy "lövést".
     *
     * Ha aknát talált a játéknak vége.
     *
     * Ha minden olyan mező ami nem tartalmaz aknát felfedésre került, a játékos nyert.
     *
     * Ha az x,y koordinátájú mező környezetében nincsenek aknák, akkor felfedi azokat.
     *
     * @param x A mező x koordinátája.
     * @param y A mező y koordinátája.
     */
    public void shoot(int x, int y) {
        logger.info("Player shot on (" + x + ":" + y + ") field");
        int ypos = y;
        int xpos = x;
        if (!((MineSweeperField)table[xpos][ypos]).isFlagged() && !((MineSweeperField)table[xpos][ypos]).isRevealed())
        {
            Recursive(xpos, ypos);
            if (((MineSweeperField)table[xpos][ypos]).isMined()){
                isOver = true;
                logger.info("Player LOST");
            }

        }
        if (CountRemaining() <= getWidth()*getHeight()/minesCounterHelper)
        {
            win = true;
            logger.info("Player WON");
        }
    }

    /**
     * Kiszámolja az adott koordinátájú mező környezetében lévő aknák darabszámát.
     *
     * @param xpos A mező x koordinátája.
     * @param ypos A mező y koordinátája.
     * @return Szomszédos aknák száma.
     */
    private int countMinesNear(int xpos, int ypos)
    {
        int mines = 0;
        int ymin = Math.max(0, ypos - 1);
        int ymax = Math.min(getHeight() - 1, ypos + 1);
        int xmin = Math.max(0, xpos - 1);
        int xmax = Math.min(getWidth() - 1, xpos + 1);
        for (int x = xmin; x <= xmax; x++)
        {
            for (int y = ymin; y <= ymax; y++)
            {
                if (((MineSweeperField)table[x][y]).isMined()){
                    mines++;
                }
            }
        }
        if (((MineSweeperField)table[xpos][ypos]).isMined()){
            mines--;
        }
        logger.info("Counting near mines on (" + xpos + ":" + ypos + ") field");
        return mines;
    }

    /**
     * Az x,y koordinátájú mezőtől kezdődően addig fedi fel a mezőket,
     * amíg olyan mezőbe nem ütközik, amelyiknek már van akna a környezetében.
     *
     * @param x A mező x koordinátája.
     * @param y A mező y koordinátája.
     */
    private void Recursive(int x, int y)
    {
        if (y >= 0 && y < getWidth() && x >= 0 && x < getHeight() && // valid coords AND
                !((MineSweeperField)table[x][y]).isMined() && !((MineSweeperField)table[x][y]).isFlagged() && // not mine AND not flagged AND
                !((MineSweeperField)table[x][y]).isRevealed()) // unknown AND
        {
            ((MineSweeperField)table[x][y]).setRevealed(true);
            if (((MineSweeperField)table[x][y]).getMinesNear() == 0) // 0 mines nearby
            {
                Recursive(x, y - 1);
                Recursive(x - 1, y);
                Recursive(x + 1, y);
                Recursive(x, y + 1);
            }
        }
    }

    /**
     * @return A még nem felfedett mezők darabszámá.
     */
    private int CountRemaining()
    {
        int remaining = 0;
        for (int x = 0; x < getHeight(); x++)
        {
            for (int y = 0; y < getWidth(); y++)
            {
                if (!((MineSweeperField)table[x][y]).isRevealed()){
                    remaining++;
                }
            }
        }
        logger.info("Counting remaining mines");
        return remaining;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < getHeight(); i++)
        {
            for (int j = 0; j < getWidth(); j++)
            {
                s += (table[i][j].text + " ");
            }
            s += "\n";
        }
        return s;
    }
}
