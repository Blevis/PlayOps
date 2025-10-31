public class Main {
    public static void main(String[] args) {
        System.out.println("Play Ops - Video Game rental management system\n");


        // Testing game class
        Game g1 = new Game(1, "Minecraft", 2009, Game.GameGenre.SANDBOX, Game.GamePlatform.PC, "Minecraft is a game made up of blocks, creatures, and community. You can survive the night or build a work of art – the choice is all yours.", 2.99);
        Game g2 = new Game(2, "Doom Eternal", 2020, Game.GameGenre.SHOOTER, Game.GamePlatform.PC,"Doom Eternal is a first-person shooter where you play as the Slayer to stop demons from destroying Earth.", 4);
        Game g3 = new Game(3, "Resident Evil Village", 2021, Game.GameGenre.HORROR, Game.GamePlatform.PlayStation, "Experience survival horror like never before in the eighth major installment in the storied Resident Evil franchise - Resident Evil Village", 4);

        System.out.println(g1);
        System.out.println(g2);
        System.out.println(g3);
    }
}
