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
    private final StringBuilder stringBuilder = new StringBuilder(128);

    /**
     * StartUI instance constructor
     * @param input @NotNull Input instance
     * @param tracker @NotNull Tracker instance
     */
    StartUI(Input input, Tracker tracker) {
        this.input = input;
        this.tracker = tracker;
    }

    /**
     *  UI initialisation
     *  shows action menu, asking for user action in while() loop
     *  when user choose some action then called appropriate method
     *  user must enter "6" to exit
     */
    void init() {
        boolean exit = false;

        while (!exit) {
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
                default:
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
        this.input.print(input.ACTION_LEFT_SEPARATOR + "New order creation" + input.ACTION_RIGHT_SEPARATOR);
        String name = this.input.ask("Enter the order name: ");
        String description = this.input.ask("Enter the order description: ");
        Item item = new Item(name, description);
        this.tracker.add(item);
        this.input.print(input.ACTION_LEFT_SEPARATOR + " new order id : " + item.getId() + input.ACTION_RIGHT_SEPARATOR);
    }

    /**
     * Method showAllItems - prints all orders
     * calls printItem() method
     */
    private void showAllItems() {
        Item[] items = this.tracker.findAll();
        if (items == null) {
            this.input.print(input.EXCEPT_MSG_SEPARATOR + "Tracker is empty!" + input.EXCEPT_MSG_SEPARATOR);
        } else {
            this.input.print(input.ACTION_LEFT_SEPARATOR + " All tracker orders " + input.ACTION_RIGHT_SEPARATOR);
            for (int counter = 0; counter < items.length; counter++) {
                System.out.print(String.valueOf(counter + 1));
                this.printItem(items[counter]);
            }
            this.input.print(input.DIVIDING_LINE);
        }
    }

    /**
     * Method editItem - replaces order with new one by Id
     */
    private void editItem() {
        this.input.print(input.ACTION_LEFT_SEPARATOR + " Replace the order with another by id " + input.ACTION_RIGHT_SEPARATOR);
        String id = this.input.ask("Enter ID of the order to be replaced: ");
        if (this.tracker.findById(id) != null) {
            String name = this.input.ask("Enter the new order name: ");
            String description = this.input.ask("Enter the new order description: ");
            Item newItem = new Item(name, description);
            this.tracker.replace(id, newItem);
            this.input.print(input.ACTION_LEFT_SEPARATOR + " Order replaced! new order id : " + newItem.getId() + input.ACTION_RIGHT_SEPARATOR);
        } else {
            this.input.print(input.EXCEPT_MSG_SEPARATOR + " Order with id = " + id + " doesn't exist. Nothing to replace. " + input.EXCEPT_MSG_SEPARATOR);
        }
    }

    /**
     * Method deleteItem - finds item by id and delete if it exists
     */
    private void deleteItem() {
        this.input.print(input.ACTION_LEFT_SEPARATOR + " Delete the order by id " + input.ACTION_RIGHT_SEPARATOR);
        String id = this.input.ask("Enter ID of the order to be deleted: ");
        Item anItem = this.tracker.findById(id);
        if (anItem == null) {
            this.input.print(input.EXCEPT_MSG_SEPARATOR + " There is no order with id = " + id + ". Nothing to delete. " + input.EXCEPT_MSG_SEPARATOR);
        } else {
            this.tracker.delete(anItem.getId());
            this.input.print(input.ACTION_LEFT_SEPARATOR + " Order \"" + anItem.getName() + "\" with id = " + id + " deleted successfully. " + input.ACTION_RIGHT_SEPARATOR);
        }
    }

    /**
     * Method findItemById - finds item by id and prints it
     * calls printItem() method
     */
    private void findItemById() {
        this.input.print(input.ACTION_LEFT_SEPARATOR + " Find order by id " + input.ACTION_RIGHT_SEPARATOR);
        String id = this.input.ask("Enter ID for the order search: ");
        Item anItem = this.tracker.findById(id);
        if (anItem == null) {
            this.input.print(input.EXCEPT_MSG_SEPARATOR + " There is no order with id = " + id + input.EXCEPT_MSG_SEPARATOR);
        } else {
            this.input.print(input.DIVIDING_LINE);
            this.printItem(anItem);
            this.input.print(input.DIVIDING_LINE);
        }
    }

    /**
     * Method findItemsByName - finds all orders by name and prints them
     * calls printItem() method
     */
    private void findItemsByName() {
        this.input.print(input.ACTION_LEFT_SEPARATOR  + " Find orders by name " + input.ACTION_RIGHT_SEPARATOR);
        String name = this.input.ask("Enter order's name for the search: ");
        Item[] items = this.tracker.findByName(name);
        if (items == null) {
            this.input.print(input.EXCEPT_MSG_SEPARATOR + " There is no orders with name = " + name + input.EXCEPT_MSG_SEPARATOR);
        } else {
            for (Item item : items) {
                this.printItem(item);
            }
            this.input.print(input.DIVIDING_LINE);
        }
    }

    /**
     * Method showMenu - prints action menu
     */
    private void showMenu() {
        this.input.print(input.MENU_START_SEPARATOR);
        for (String element : this.menu) {
            this.input.print(element);
        }
        this.input.print(input.MENU_END_SEPARATOR);
    }

    /**
     * Method printItem - prints Item data, except comments
     * uses stringBuilder to concat Strings
     * @param item item to print
     */
    private void printItem(Item item) {
        stringBuilder.setLength(0); // making stringBuilder empty
        System.out.println(
                stringBuilder
                        .append(" Order's name: ")
                        .append(item.getName())
                        .append(", id = ")
                        .append(item.getId())
                        .append(", description: ")
                        .append(item.getDescription())
                        .append(", date of creation: ")
                        .append(new Date(item.getCreated())) // converting order creation date from milliseconds to date format
        );
    }

    public static void main(String[] args) {
        Input input = new ConsoleInput();
        new StartUI(input, new Tracker()).init();
    }
}
