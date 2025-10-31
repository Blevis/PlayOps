public class Game {
    // PROPERTIES
    private static int nextId = 1;
    private final int id;
    private String title;
    private int year;
    private GameGenre genre;
    private GamePlatform platform;
    private String description;
    private double pricePerDay;

    // ENUMERATIONS
    public enum GameGenre{
        ACTION,
        ADVENTURE,
        ROLE_PLAYING,
        SIMULATION,
        STRATEGY,
        SPORTS,
        RACING,
        FIGHTING,
        HORROR,
        PLATFORMER,
        PUZZLE,
        SANDBOX,
        SHOOTER,
        SURVIVAL,
        STEALTH
    }
    public enum GamePlatform { PC, Xbox, PlayStation, NintendoSwitch }

    // CONSTRUCTOR
    public Game(String title, int year, Game.GameGenre genre, GamePlatform platform, String description, double pricePerDay){
        this.id = nextId++;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.platform = platform;
        this.description = description;
        this.pricePerDay = pricePerDay;
    }

    // GET/SET METHODS
    public int getId() {return id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public int getYear() {return year;}

    public void setYear(int year) {this.year = year;}

    public GameGenre getGenre() {return genre;}

    public void setGenre(GameGenre genre) {this.genre = genre;}

    public GamePlatform getPlatform() {return platform;}

    public void setPlatform(GamePlatform platform) {this.platform = platform;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public double getPricePerDay() {return pricePerDay;}


    // OTHER METHODS
    public double calculateRentalCost(int days) {
        return pricePerDay * days;
    }

    // TO STRING METHOD
    @Override
    public String toString() {
        return "[" + id + "] " + title + " | " + year + " | " + genre + " | " + platform + " | $" + pricePerDay;
    }

}
