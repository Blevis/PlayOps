package com.playops.app;

public class Main {
    public static void main(String[] args) {
        System.out.println("\nPlay Ops - Video com.playops.model.Game rental management system\n");

        // Creation of store object
        Store metroRetro = new Store("MetroRetro");

        // Creation of game objects
        Game g1 = new Game("Super Mario 64", 1996, Game.GameGenre.PLATFORMER, Game.GamePlatform.Nintendo64, "Super Mario 64 is a 1996 platform game developed and published by Nintendo for the Nintendo 64", 2.99);
        Game g2 = new Game("Doom", 1993, Game.GameGenre.SHOOTER, Game.GamePlatform.MS_DOS,"The player assumes the role of a space marine, later unofficially referred to as Doomguy, fighting through hordes of undead humans and invading demons", 4);
        Game g3 = new Game("Pokémon Red", 1996, Game.GameGenre.ROLE_PLAYING, Game.GamePlatform.GameBoy, "Pokémon Red is a 1996 role-playing game where you play as a young boy from Pallet Town who travels the Kanto region to capture and train Pokémon to become the Pokémon League Champion.", 6);

        // Creation of customer objects
        Customer c1 = new Customer("John", "Doe", "john@doe.com", "Street 123");
        Customer c2 = new Customer("Jane", "Doe", "jane@doe.com", "Street 234");
        Customer c3 = new Customer("Mario", "Rossi", "mario@rossi.com", "Street 345");

        // Adding games to store
        metroRetro.addGame(g1);
        metroRetro.addGame(g2);
        metroRetro.addGame(g3);

        // Adding customers to store
        metroRetro.addCustomer(c1);
        metroRetro.addCustomer(c2);
        metroRetro.addCustomer(c3);

        // Displaying store data
        System.out.println("\n============================");
        System.out.println("\tWelcome to "+metroRetro.getName());
        System.out.println("============================");

        System.out.println("\n\tAVAILABLE GAMES");
        metroRetro.displayInventory();

        System.out.println("\n\tREGISTERED CUSTOMERS");
        metroRetro.displayCustomers();

        // Searching instances
        System.out.println("\n\tSearching for 'Doom'");
        Game foundGame = metroRetro.findGame("doom");
        System.out.println(foundGame != null ? foundGame:"com.playops.model.Game not found");

        // Removing instances from demo
        System.out.println("\n\tRemoving 'Super Mario 64'");
        metroRetro.removeGame("Super Mario 64");

        // Updated inventory
        System.out.println("\n\tUPDATED INVENTORY");
        metroRetro.displayInventory();

        System.out.println("\n\nProgram finished successfully, thank you for trying out!");
    }
}
