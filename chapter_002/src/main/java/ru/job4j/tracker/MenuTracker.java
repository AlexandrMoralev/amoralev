package ru.job4j.tracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
     *
     * @param key  NonNull int menu action's key
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
    public void execute(Input input, ITracker tracker) {
        input.print(String.format("%s %s %s", input.ACTION_LEFT_SEPARATOR, " Replace the order with another by id ", input.ACTION_RIGHT_SEPARATOR));
        String id = input.ask("Enter ID of the order to be replaced: ");
        Optional<Item> currentItem = tracker.findById(id);
        if (currentItem.isPresent()) {
            String name = input.ask("Enter the new order name: ");
            String description = input.ask("Enter the new order description: ");
            Item newItem = Item.newBuilder().of(currentItem.get()).setName(name).setDescription(description).build();
            tracker.update(newItem);
            input.print(String.format("%s %s %s %s", input.ACTION_LEFT_SEPARATOR, " Order replaced! new order id : ", newItem.getId(), input.ACTION_RIGHT_SEPARATOR));
        } else {
            input.print(String.format("%s %s %s %s %s", input.EXCEPT_MSG_SEPARATOR, " Order with id = ", id, " doesn't exist. Nothing to update. ", input.EXCEPT_MSG_SEPARATOR));
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
     *
     * @return boolean
     */
    public boolean isTimeToExit() {
        return this.isTimeToExit;
    }

    /**
     * ExitMenu instance constructor, calls the superclass constructor
     *
     * @param key  NonNull int menu action's key
     * @param name NonNull String menu action's name
     */
    public ExitMenu(int key, String name) {
        super(key, name);
        this.isTimeToExit = false;
    }

    /**
     * Method execute - sets program exit condition to "true"
     */
    @Override
    public void execute(Input input, ITracker tracker) {
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
    private final ITracker tracker;

    // actions on Tracker
    private final List<UserAction> actions = new ArrayList<>();

    // StringBuilder instance to concatenate Strings inside loops
    private final StringBuilder stringBuilder = new StringBuilder(128);

    // ExitMenu reference for checking condition of menu exit
    private ExitMenu exitMenu;

    /**
     * MenuTracker instance constructor
     *
     * @param input   NonNull Input instance
     * @param tracker NonNull Tracker instance
     */
    MenuTracker(Input input, ITracker tracker) {
        this.input = input;
        this.tracker = tracker;
    }

    /**
     * Method fillActions - menu actions aggregation
     * !hardcode as is
     */
    public void fillActions() {
        this.actions.add(new AddItem(0, "Add the new item. "));
        this.actions.add(new MenuTracker.ShowAllItems(1, "Show all Items. "));
        this.actions.add(new EditItem(2, "Edit item. "));
        this.actions.add(new DeleteItem(3, "Delete item. "));
        this.actions.add(new FindItemById(4, "Find item by Id. "));
        this.actions.add(new FindItemByName(5, "Find item by name. "));
        this.actions.add(new ExitMenu(6, "Exit the program. "));
    }

    public void addAction(UserAction userAction) {
        this.actions.add(userAction);
    }

    /**
     * Method select - calls the specific action by the action's key from menu
     *
     * @param key int value, entered by user
     */
    public void select(int key) {
        this.actions.stream()
                .filter(action -> action.key() == key)
                .findFirst()
                .ifPresent(action -> action.execute(this.input, this.tracker));
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
     *
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
                        .append(new Date(item.getCreated()))
        );
    }

    /**
     * Method getMenuSize
     *
     * @return int number of menu actions to fill range of validation
     */
    public int getMenuSize() {
        return this.actions.size();
    }

    /**
     * Method timeToExit - "getter" for the flag transmission
     *
     * @return boolean
     */
    public boolean timeToExit() {
        exitMenu = (ExitMenu) this.actions.get(6);
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
         *
         * @param key  NonNull int menu action's key
         * @param name NonNull String menu action's name
         */
        public AddItem(int key, String name) {
            super(key, name);
        }

        /**
         * Method execute - creating new order(Item) and adding it to Tracker
         * user enters order's name and order's description
         * then the order ID is displayed on the console
         */
        @Override
        public void execute(Input input, ITracker tracker) {
            input.print(String.format("%s %s %s", input.ACTION_LEFT_SEPARATOR, "New order creation", input.ACTION_RIGHT_SEPARATOR));
            String name = input.ask("Enter the order name: ");
            String description = input.ask("Enter the order description: ");
            String itemId = tracker.add(new Item(name, description));
            input.print(String.format("%s %s %s %s", input.ACTION_LEFT_SEPARATOR, " new order id : ", itemId, input.ACTION_RIGHT_SEPARATOR));
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
         *
         * @param key  NonNull int menu action's key
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
        public void execute(Input input, ITracker tracker) {

            MenuTracker mt = new MenuTracker(input, tracker);
            List<Item> items = new ArrayList<>(tracker.findAll());
            if (items.isEmpty()) {
                input.print(String.format("%s %s %s", input.EXCEPT_MSG_SEPARATOR, "Tracker is empty!", input.EXCEPT_MSG_SEPARATOR));
            } else {
                input.print(String.format("%s %s %s", input.ACTION_LEFT_SEPARATOR, " All tracker orders ", input.ACTION_RIGHT_SEPARATOR));
                for (int counter = 0; counter < items.size(); counter++) {
                    System.out.print(counter + 1);
                    mt.printItem(items.get(counter));
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
         *
         * @param key  NonNull int menu action's key
         * @param name NonNull String menu action's name
         */
        public DeleteItem(int key, String name) {
            super(key, name);
        }

        /**
         * Method execute - finds item by id and delete if it exists
         */
        @Override
        public void execute(Input input, ITracker tracker) {
            input.print(String.format("%s %s %s", input.ACTION_LEFT_SEPARATOR, " Delete the order by id ", input.ACTION_RIGHT_SEPARATOR));
            String id = input.ask("Enter ID of the order to be deleted: ");
            Optional<Item> item = tracker.findById(id);
            if (item.isEmpty()) {
                input.print(String.format("%s %s %s %s %s", input.EXCEPT_MSG_SEPARATOR, " There is no order with id = ", id, ". Nothing to delete. ", input.EXCEPT_MSG_SEPARATOR));
            } else {
                tracker.delete(item.get().getId());
                input.print(String.format("%s %s %s %s %s %s %s", input.ACTION_LEFT_SEPARATOR, " Order \"", item.get().getName(), "\" with id = ", id, " deleted successfully. ", input.ACTION_RIGHT_SEPARATOR));
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
         *
         * @param key  NonNull int menu action's key
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
        public void execute(Input input, ITracker tracker) {
            input.print(String.format("%s %s %s", input.ACTION_LEFT_SEPARATOR, " Find order by id ", input.ACTION_RIGHT_SEPARATOR));
            String id = input.ask("Enter ID for the order search: ");
            Optional<Item> item = tracker.findById(id);
            if (item.isEmpty()) {
                input.print(String.format("%s %s %s %s", input.EXCEPT_MSG_SEPARATOR, " There is no order with id = ", id, input.EXCEPT_MSG_SEPARATOR));
            } else {
                input.print(input.DIVIDING_LINE);
                printItem(item.get());
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
         *
         * @param key  NonNull int menu action's key
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
        public void execute(Input input, ITracker tracker) {
            input.print(String.format("%s %s %s", input.ACTION_LEFT_SEPARATOR, " Find orders by name ", input.ACTION_RIGHT_SEPARATOR));
            String name = input.ask("Enter order's name for the search: ");
            List<Item> items = new ArrayList<>(tracker.findByName(name));
            if (items.isEmpty()) {
                input.print(String.format("%s %s %s %s", input.EXCEPT_MSG_SEPARATOR, " There is no orders with name = ", name, input.EXCEPT_MSG_SEPARATOR));
            } else {
                items.forEach(MenuTracker.this::printItem);
                input.print(input.DIVIDING_LINE);
            }
        }
    }
}
