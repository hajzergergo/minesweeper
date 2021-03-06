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
     * Ha a paraméterül kapott {@code username} üres, akkor a
     * {@code System.getProperty("user.name")} értékkel jön létre a {@link Player}.
     *
     * @param username A játékos felhasználóneve.
     * @param points A játékos pontszáma.
     */
    public Player(String username, int points) {
        if (username.equals(""))
            this.username = System.getProperty("user.name");
        else
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
