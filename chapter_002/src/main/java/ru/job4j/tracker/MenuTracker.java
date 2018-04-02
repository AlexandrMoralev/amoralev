package ru.job4j.tracker;

import java.util.Date;

/**
 * EditItem - external class, in accordance with the technical specifications
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
class EditItem extends BaseAction {

    /**
     * EditItem instance constructor, calls the superclass constructor
     * @param key NonNull int menu action's key
     * @param name NonNull String menu action's name
     */
    public EditItem(int key, String name) {
        super(key, name);
    }

    /**
     * Method execute - replaces order with new one by Id
     * user enters new Item's name and description
     * then new Item reference replaces existing Item reference
     */
    @Override
    public void execute(Input input, Tracker tracker) {
        input.print(String.format("%s %s %s", input.ACTION_LEFT_SEPARATOR, " Replace the order with another by id ", input.ACTION_RIGHT_SEPARATOR));
        String id = input.ask("Enter ID of the order to be replaced: ");
        if (tracker.findById(id) != null) {
            String name = input.ask("Enter the new order name: ");
            String description = input.ask("Enter the new order description: ");
            Item newItem = new Item(name, description);
            tracker.replace(id, newItem);
            input.print(String.format("%s %s %s %s", input.ACTION_LEFT_SEPARATOR, " Order replaced! new order id : ", newItem.getId(), input.ACTION_RIGHT_SEPARATOR));
        } else {
            input.print(String.format("%s %s %s %s %s", input.EXCEPT_MSG_SEPARATOR, " Order with id = ", id, " doesn't exist. Nothing to replace. ", input.EXCEPT_MSG_SEPARATOR));
        }
    }
}

/**
 * ExitMenu - external class, in accordance with the technical specifications
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
class ExitMenu extends BaseAction {

    // menu exit flag
    private boolean isTimeToExit;

    /**
     * Method isTimeToExit - "getter" for the flag transmission
     * @return boolean
     */
    public boolean isTimeToExit() {
        return this.isTimeToExit;
    }

    /**
     * ExitMenu instance constructor, calls the superclass constructor
     * @param key NonNull int menu action's key
     * @param name NonNull String menu action's name
     */
    public ExitMenu(int key, String name) {
        super(key, name);
        this.isTimeToExit = false;
    }

    /**
     * Method execute - sets program exit condition to "true"
     *
     */
    @Override
    public void execute(Input input, Tracker tracker) {
        this.isTimeToExit = true;
    }

}

/**
 * MenuTracker
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class MenuTracker {

    private final Input input;
    private final Tracker tracker;

    // actions on Tracker
    private UserAction[] actions = new UserAction[7];

    // StringBuilder instance to concatenate Strings inside loops
    private final StringBuilder stringBuilder = new StringBuilder(128);

    // ExitMenu reference for checking condition of menu exit
    private ExitMenu exitMenu;

    /**
     * MenuTracker instance constructor
     * @param input NonNull Input instance
     * @param tracker NonNull Tracker instance
     */
    MenuTracker(Input input, Tracker tracker) {
        this.input = input;
        this.tracker = tracker;
    }

    /**
     * Method fillActions - menu actions aggregation
     * !hardcode as is
     */
    public void fillActions() {
        this.actions[0] = this.new AddItem(0, "Add the new item. ");
        this.actions[1] = new MenuTracker.ShowAllItems(1, "Show all Items. ");
        this.actions[2] = new EditItem(2, "Edit item. ");
        this.actions[3] = new DeleteItem(3, "Delete item. ");
        this.actions[4] = new FindItemById(4, "Find item by Id. ");
        this.actions[5] = new FindItemByName(5, "Find item by name. ");
        this.actions[6] = new ExitMenu(6, "Exit the program. ");
    }

    /**
     * Method select - calls the specific action by the action's key from menu
     * @param key int value, entered by user
     */
    public void select(int key) {
        this.actions[key].execute(this.input, this.tracker);
    }

    /**
     * Method show - prints all available menu actions to console
     */
    public void show() {
        this.input.print(input.MENU_START_SEPARATOR);
        for (UserAction action : this.actions) {
            if (action != null) {
                System.out.println(action.info());
            }
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

    /**
     * Method getMenuSize
     * @return int number of menu actions to fill range of validation
     */
    public int getMenuSize() {
        return this.actions.length;
    }

    /**
     * Method timeToExit - "getter" for the flag transmission
     * @return boolean
     */
    public boolean timeToExit() {
        exitMenu = (ExitMenu) this.actions[6];
        return exitMenu.isTimeToExit();
    }

    /**
     * AddItem.
     *
     * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
     * @version $Id$
     * @since 0.1
     */
    private class AddItem extends BaseAction {

        /**
         * AddItem instance constructor, calls the superclass constructor
         * @param key NonNull int menu action's key
         * @param name NonNull String menu action's name
         */
        public AddItem(int key, String name) {
            super(key, name);
        }

        /**
         *  Method execute - creating new order(Item) and adding it to Tracker
         *  user enters order's name and order's description
         *  then the order ID is displayed on the console
         */
        @Override
        public void execute(Input input, Tracker tracker) {
            input.print(String.format("%s %s %s", input.ACTION_LEFT_SEPARATOR, "New order creation", input.ACTION_RIGHT_SEPARATOR));
            String name = input.ask("Enter the order name: ");
            String description = input.ask("Enter the order description: ");
            Item item = new Item(name, description);
            tracker.add(item);
            input.print(String.format("%s %s %s %s", input.ACTION_LEFT_SEPARATOR, " new order id : ", item.getId(), input.ACTION_RIGHT_SEPARATOR));
        }
    }

    /**
     * ShowAllItems - static class in accordance with technical specifications
     *
     * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
     * @version $Id$
     * @since 0.1
     */
    private static class ShowAllItems extends BaseAction {

        /**
         * ShowAllItems instance constructor, calls the superclass constructor
         * @param key NonNull int menu action's key
         * @param name NonNull String menu action's name
         */
        public ShowAllItems(int key, String name) {
            super(key, name);
        }

        /**
         * Method execute - prints all orders to console
         * calls printItem() method
         */
        @Override
        public void execute(Input input, Tracker tracker) {

            MenuTracker mt = new MenuTracker(input, tracker);
            Item[] items = tracker.findAll();
            if (items == null) {
                input.print(String.format("%s %s %s", input.EXCEPT_MSG_SEPARATOR, "Tracker is empty!", input.EXCEPT_MSG_SEPARATOR));
            } else {
                input.print(String.format("%s %s %s", input.ACTION_LEFT_SEPARATOR, " All tracker orders ", input.ACTION_RIGHT_SEPARATOR));
                for (int counter = 0; counter < items.length; counter++) {
                    System.out.print(String.valueOf(counter + 1));
                    mt.printItem(items[counter]);
                }
                input.print(input.DIVIDING_LINE);
            }
        }
    }


    /**
     * DeleteItem.
     *
     * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
     * @version $Id$
     * @since 0.1
     */
    private class DeleteItem extends BaseAction {

        /**
         * DeleteItem instance constructor, calls the superclass constructor
         * @param key NonNull int menu action's key
         * @param name NonNull String menu action's name
         */
        public DeleteItem(int key, String name) {
            super(key, name);
        }

        /**
         * Method execute - finds item by id and delete if it exists
         */
        @Override
        public void execute(Input input, Tracker tracker) {
            input.print(String.format("%s %s %s", input.ACTION_LEFT_SEPARATOR, " Delete the order by id ", input.ACTION_RIGHT_SEPARATOR));
            String id = input.ask("Enter ID of the order to be deleted: ");
            Item anItem = tracker.findById(id);
            if (anItem == null) {
                input.print(String.format("%s %s %s %s %s", input.EXCEPT_MSG_SEPARATOR, " There is no order with id = ", id, ". Nothing to delete. ", input.EXCEPT_MSG_SEPARATOR));
            } else {
                tracker.delete(anItem.getId());
                input.print(String.format("%s %s %s %s %s %s %s", input.ACTION_LEFT_SEPARATOR, " Order \"", anItem.getName(), "\" with id = ", id, " deleted successfully. ", input.ACTION_RIGHT_SEPARATOR));
            }
        }
    }

    /**
     * FindItemById.
     *
     * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
     * @version $Id$
     * @since 0.1
     */
    private class FindItemById extends BaseAction {

        /**
         * FindItemById instance constructor, calls the superclass constructor
         * @param key NonNull int menu action's key
         * @param name NonNull String menu action's name
         */
        public FindItemById(int key, String name) {
            super(key, name);
        }

        /**
         * Method execute - finds item by id and prints it to console
         * calls printItem() method
         */
        @Override
        public void execute(Input input, Tracker tracker) {
            input.print(String.format("%s %s %s", input.ACTION_LEFT_SEPARATOR, " Find order by id ", input.ACTION_RIGHT_SEPARATOR));
            String id = input.ask("Enter ID for the order search: ");
            Item anItem = tracker.findById(id);
            if (anItem == null) {
                input.print(String.format("%s %s %s %s", input.EXCEPT_MSG_SEPARATOR, " There is no order with id = ", id, input.EXCEPT_MSG_SEPARATOR));
            } else {
                input.print(input.DIVIDING_LINE);
                printItem(anItem);
                input.print(input.DIVIDING_LINE);
            }
        }
    }

    /**
     * FindItemByName.
     *
     * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
     * @version $Id$
     * @since 0.1
     */
    private class FindItemByName extends BaseAction {

        /**
         * FindItemByName instance constructor, calls the superclass constructor
         * @param key NonNull int menu action's key
         * @param name NonNull String menu action's name
         */
        public FindItemByName(int key, String name) {
            super(key, name);
        }

        /**
         * Method execute - finds all orders by name and prints them to console
         * calls printItem() method
         */
        @Override
        public void execute(Input input, Tracker tracker) {
            input.print(String.format("%s %s %s", input.ACTION_LEFT_SEPARATOR, " Find orders by name ", input.ACTION_RIGHT_SEPARATOR));
            String name = input.ask("Enter order's name for the search: ");
            Item[] items = tracker.findByName(name);
            if (items == null) {
                input.print(String.format("%s %s %s %s", input.EXCEPT_MSG_SEPARATOR, " There is no orders with name = ", name, input.EXCEPT_MSG_SEPARATOR));
            } else {
                for (Item item : items) {
                    printItem(item);
                }
                input.print(input.DIVIDING_LINE);
            }
        }
    }
}
