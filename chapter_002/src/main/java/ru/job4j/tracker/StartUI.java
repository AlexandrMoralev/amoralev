package ru.job4j.tracker;

import java.util.Date;

/**
 * StartUI
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StartUI {

    // Action descriptions in String format.
    private static final String ADD = "0";
    private static final String SHOW_ALL = "1";
    private static final String EDIT = "2";
    private static final String DELETE = "3";
    private static final String FIND_BY_ID = "4";
    private static final String FIND_BY_NAME = "5";
    private static final String EXIT = "6";

    // Actions menu.
    private final String[] menu = {
            "0. Add new Item",
            "1. Show all items",
            "2. Edit item",
            "3. Delete item",
            "4. Find item by Id",
            "5. Find items by name",
            "6. Exit Program"
    };

    // Input instance
    private final Input input;

    // Tracker instance
    private final Tracker tracker;

    // StringBuilder instance to concatenate Strings inside loops
    private final StringBuilder stringBuilder = new StringBuilder();

    /**
     * StartUI instance constructor
     * @param input @NotNull Input instance
     * @param tracker @NotNull Tracker instance
     */
    public StartUI(Input input, Tracker tracker) {
        this.input = input;
        this.tracker = tracker;
    }

    /**
     *  UI initialisation
     *  shows action menu, asking for user action in while() loop
     *  when user choose some action then called appropriate method
     *  user must enter "6" to exit
     */
    void init(){
        boolean exit = false;

        while(!exit) {
            this.showMenu();
            String answer = this.input.ask("Enter menu item");

            switch (answer) {
                case ADD:
                    this.createItem();
                    break;
                case SHOW_ALL:
                    this.showAllItems();
                    break;
                case EDIT:
                    this.editItem();
                    break;
                case DELETE:
                    this.deleteItem();
                    break;
                case FIND_BY_ID:
                    this.findItemById();
                    break;
                case FIND_BY_NAME:
                    this.findItemsByName();
                    break;
                case EXIT:
                    exit = true;
                    break;
            }
        }
    }

    /**
     *  Method createItem - creating new order(Item) and adding it to Tracker
     *  user enters order's name and order's description
     *  then the order ID is displayed on the console
     */
    private void createItem() {
        System.out.println("-------- New order creation --------");
        String name = this.input.ask("Enter the order name: ");
        String description = this.input.ask("Enter the order description: ");
        Item item = new Item(name, description, System.currentTimeMillis());
        this.tracker.add(item);
        System.out.println("-------- new order id : " + item.getId() + " --------");
    }

    /**
     * Method showAllItems - prints all orders
     * calls printItem() method
     */
    private void showAllItems() {
        System.out.println("-------- All tracker orders --------");
        for (int counter = 0; counter < this.tracker.findAll().length; counter++) {
            System.out.print(counter + 1);
            printItem(this.tracker.findAll()[counter]);
        }
        System.out.println("------------------------------------------------------------");
    }

    /**
     * Method editItem - replaces order with new one by Id
     */
    private void editItem() {
        System.out.println("-------- Replace the order with another by id --------");
        String id = this.input.ask("Enter ID of the order to be replaced: ");

        if (this.tracker.findById(id) != null) {
            String name = this.input.ask("Enter the new order name: ");
            String description = this.input.ask("Enter the new order description: ");
            Item newItem = new Item(name, description, System.currentTimeMillis());
            this.tracker.replace(id, newItem);
            System.out.println("-------- Order replaced! new order id : " + newItem.getId() + " --------");
        } else {
            System.out.println("-------- Order with id = " + id + " doesn't exist. Nothing to replace. --------");
        }
    }

    /**
     * Method deleteItem - finds item by id and delete if it exists
     */
    private void deleteItem() {
        System.out.println("-------- Delete the order by id --------");
        String id = this.input.ask("Enter ID of the order to be deleted: ");
        Item anItem = this.tracker.findById(id);
        if (anItem == null) {
            System.out.println("-------- There is no order with id = " + id + ". Nothing to delete. --------");
        } else {
            this.tracker.delete(anItem.getId());
            System.out.println("-------- Order with id = " + id + " deleted. --------");
        }
    }

    /**
     * Method findItemById - finds item by id and prints it
     * calls printItem() method
     */
    private void findItemById() {
        System.out.println("-------- Find order by id --------");
        String id = this.input.ask("Enter ID for the order search: ");
        Item anItem = this.tracker.findById(id);
        if (anItem == null) {
            System.out.println("-------- There is no order with id = " + id + ". --------");
        } else {
            this.printItem(anItem);
            System.out.println("------------------------------------------------------------");
        }
    }

    /**
     * Method findItemsByName - finds all orders by name and prints them
     * calls printItem() method
     */
    private void findItemsByName() {
        System.out.println("-------- Find orders by name --------");
        String name = this.input.ask("Enter order's name for the search: ");
        Item[] items = this.tracker.findByName(name);
        if (items == null) {
            System.out.println("-------- There is no orders with name = " + name + ". --------");
        } else {
            for (Item item : items) {
                printItem(item);
            }
            System.out.println("------------------------------------------------------------");
        }
    }

    /**
     * Method showMenu - prints action menu
     */
    private void showMenu() {
        System.out.println("========== Menu ===========");
        for (String element : this.menu) {
            System.out.println(element);
        }
        System.out.println("============================");
    }

    /**
     * Method printItem - prints Item data, except comments
     * uses stringBuilder to concat Strings
     * @param item item to print
     */
    private void printItem(Item item) {
        System.out.println(
                stringBuilder
                        .append(" Order's name: ")
                        .append(item.getName())
                        .append(", id = ")
                        .append(item.getId())
                        .append(", description: ")
                        .append(item.getDescription())
                        .append(", date of creation: ")
                        .append(new Date(item.getCreated() * 1000L)) // converting order creation date from milliseconds to date format
        );
    }

    public static void main(String[] args) {
        new StartUI(new ConsoleInput(), new Tracker()).init();
    }
}
