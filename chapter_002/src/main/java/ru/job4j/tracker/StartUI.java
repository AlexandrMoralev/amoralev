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

    /**
     * StartUI instance constructor
     * @param input @NotNull Input instance
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
        MenuTracker menu = new MenuTracker(input, tracker);
        menu.fillActions();
        ExitMenu exitMenu = new ExitMenu();

        do {
            menu.show();
            int key = Integer.valueOf(input.ask("Select: "));
            menu.select(key);
            if (exitMenu.isTimeToExit()) {
                break;
            }
        } while (!"y".equals(this.input.ask("Exit? (press \"y\") ")));
    }

    public static void main(String[] args) {
        Input input = new ConsoleInput();
        new StartUI(input, new Tracker()).init();
    }
}
