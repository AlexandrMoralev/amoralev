package ru.job4j.tracker;

import java.util.function.Consumer;

/**
 * StartUI
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StartUI {

    private final Input input;
    private final ITracker tracker;
    private final Consumer<MenuTracker> menuPrinter = MenuTracker::show;

    /**
     * StartUI instance constructor
     *
     * @param input @NotNull Input instance
     */
    StartUI(Input input, ITracker tracker) {
        this.input = input;
        this.tracker = tracker;
    }

    /**
     * +
     * UI initialisation
     * shows action menu, asking for user action in while() loop
     * when user choose some action then called appropriate method
     * user must enter "6" to exit, then press "y" to confirm
     */
    void init() {
        MenuTracker menu = new MenuTracker(this.input, this.tracker);
        // init menu actions.
        menu.fillActions();
        // getting actions range[] from menu.
        int[] range = new int[menu.getMenuSize()];
        for (int i = 0; i < range.length; i++) {
            range[i] = i;
        }
        // adding new action to menu, using new anonymous class
        UserAction voidAction = new UserAction() {
            @Override
            public int key() {
                return 7;
            }

            @Override
            public void execute(Input input, ITracker tracker) {
            }

            @Override
            public String info() {
                return "Do nothing. ";
            }
        };
        menu.addAction(voidAction);

        // running application
        do {
            menuPrinter.accept(menu);
            int ask = input.ask("Select: ", range);
            System.out.println("asking: " + ask);
            menu.select(ask);
        } while (!menu.timeToExit()
                && !"y".equalsIgnoreCase(this.input.ask("Exit? (press \"y\") "))
        );
    }

    public static void main(String[] args) {
        ITracker tracker = new Tracker();
        Input input = new ConsoleInput();
        Input validateInput = new ValidateInput(input);
        StartUI startUI = new StartUI(validateInput, tracker);
        startUI.init();
    }
}
