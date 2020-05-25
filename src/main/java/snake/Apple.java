package snake;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

//Объект яблоко, которое ест змея
public class Apple extends GameObject {
    //Символ для отрисовки яблока
    private static final String APPLE_SIGN = "\uD83C\uDF4E";
    public boolean isAlive = true;

    public Apple(int x, int y) {
        super(x, y);
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    //Метод для отрисовывания яблока на игровом поле
    public void draw(Game game) {
        game.setCellValueEx(x, y, Color.NONE, APPLE_SIGN, Color.RED, 75);
    }
}
