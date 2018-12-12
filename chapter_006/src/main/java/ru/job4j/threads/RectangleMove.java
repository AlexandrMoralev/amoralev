package ru.job4j.threads;

import javafx.scene.shape.Rectangle;

/**
 * RectangleMove
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class RectangleMove implements Runnable {
    private final Rectangle rect;
    private final Rectangle framing;

    public RectangleMove(Rectangle rect, Rectangle framing) {
        this.rect = rect;
        this.framing = framing;
    }

    @Override
    public void run() {
        Integer dX = 5;
        Integer dY = 0;
        int delta;
        while (true) {
            this.rect.setX(this.rect.getX() + dX);
            this.rect.setY(this.rect.getY() + dY);
            if (this.rect.getX() >= framing.getWidth() || this.rect.getX() <= 0) {
                dX = -dX;
                delta = (int) (Math.random() * 7);
                dY = dY > 0 ? dY + delta : -dY - delta;
                dY = dY == 0 && delta >= 3 ? dY + delta : dY - delta;
            }

            if (this.rect.getY() >= framing.getHeight() || this.rect.getY() <= 0) {
                dY = -dY;
                delta = (int) (Math.random() * 9);
                dX = dX > 0 ? dX + delta : dX + delta;
                dX = dX == 0 && delta >= 5 ? dX + delta : dX - delta;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}