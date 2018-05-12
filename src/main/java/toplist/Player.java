package toplist;

/**
 * Játékost reprezentáló osztály.
 */
public class Player {
    /**
     * Játékos felhasználó neve.
     */
    private String username;

    /**
     * Játékos pontszáma.
     */
    private int points;

    /**
     * Létrehozza a játékos objektumot.
     *
     * @param username A játékos felhasználóneve.
     * @param points A játékos pontszáma.
     */
    public Player(String username, int points) {
        this.username = username;
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return points + "\t\t\t " + username;
    }
}
