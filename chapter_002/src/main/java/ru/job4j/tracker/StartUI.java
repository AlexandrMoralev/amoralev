package ru.job4j.tracker;

/**
 * StartUI
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StartUI {

    private Input input;
    private Tracker tracker;

    private int[] range;

    /**
     * StartUI instance constructor
     * @param input @NotNull Input instance
     */
    StartUI(Input input, Tracker tracker) {
        this.input = input;
        this.tracker = tracker;
    }

    /**+
     *  UI initialisation
     *  shows action menu, asking for user action in while() loop
     *  when user choose some action then called appropriate method
     *  user must enter "6" to exit, then press "y" to confirm
     */
    void init() {
        MenuTracker menu = new MenuTracker(this.input, this.tracker);
        // getting actions range[] from menu.
        this.range = new int[menu.getMenuSize()];
        for (int i = 0; i < range.length; i++) {
            this.range[i] = i;
        }
        // init menu actions.
        menu.fillActions();
        // running application
        do {
            menu.show();
            menu.select(input.ask("Select: ", this.range));
        } while (!menu.timeToExit()  & !"y".equals(this.input.ask("Exit? (press \"y\") ")));
    }

    public static void main(String[] args) {

        new StartUI(
                new ValidateInput(
                        new ConsoleInput()
                ),
                new Tracker()
        ).init();
    }
}
