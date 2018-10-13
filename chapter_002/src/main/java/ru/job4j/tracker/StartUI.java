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

    private Input input;
    private Tracker tracker;

    private int[] range;

   Consumer<MenuTracker> menuPrinter = o -> o.show();

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
        // adding new action to menu, using new anonymous class
        UserAction voidAction = new UserAction() {
            @Override
            public int key() {
                return 7;
            }

            @Override
            public void execute(Input input, Tracker tracker) {
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
