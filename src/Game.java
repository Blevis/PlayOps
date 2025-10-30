public class Game {
    private int id;
    private String title;
    private int year;
    private gameGenre genre;
    private gamePlatform platform;
    private String description;

    public enum gameGenre{
        ACTION, HORROR, SHOOTER;
    }
    public enum gamePlatform{
        PC, Xbox, PlayStation, NintendoSwitch;
    }

    public Game(int id, String title, int year, Game.gameGenre genre, Game.gamePlatform platform, String description){
        this.id = id;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.platform = platform;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public gameGenre getGenre() {
        return genre;
    }

    public void setGenre(gameGenre genre) {
        this.genre = genre;
    }

    public gamePlatform getPlatform() {
        return platform;
    }

    public void setPlatform(gamePlatform platform) {
        this.platform = platform;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
