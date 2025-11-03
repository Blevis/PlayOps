public class Main {
    public static void main(String[] args) {
        System.out.println("Play Ops - Video Game rental management system\n");

        // Testing game + customer class
        Game g1 = new Game("Minecraft", 2009, Game.GameGenre.SANDBOX, Game.GamePlatform.PC, "Minecraft is a game made up of blocks, creatures, and community. You can survive the night or build a work of art – the choice is all yours.", 1010.99);
        Game g2 = new Game("Doom Eternal", 2020, Game.GameGenre.SHOOTER, Game.GamePlatform.PC,"Doom Eternal is a first-person shooter where you play as the Slayer to stop demons from destroying Earth.", 4);
        Game g3 = new Game("Resident Evil Village", 2021, Game.GameGenre.HORROR, Game.GamePlatform.PlayStation, "Experience survival horror like never before in the eighth major installment in the storied Resident Evil franchise - Resident Evil Village", 4);

        Customer c1 = new Customer("John", "Doe", "john@doe.com", "Street 123");
        Customer c2 = new Customer("Jane", "Doe", "jane@doe.com", "Street 234");
        Customer c3 = new Customer("Mario", "Rossi", "mario@rossi.com", "Street 345");

        System.out.println(g1);
        System.out.println(g2);
        System.out.println(g3);

        System.out.println("-------------------------------------------------------------------");

        System.out.println(c1);
        System.out.println(c2);
        System.out.println(c3);

        Store s1 = new Store("Bobi Store");
        s1.addGame(g1);
        s1.addGame(g2);
        s1.displayInventory();
        s1.addCustomer(c1);
        s1.addCustomer(c2);
        s1.displayCustomers();
    }
}
