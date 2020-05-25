package snake;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

//Змейка
public class Snake {
    //Список всех сегментов змейки
    private List<GameObject> snakeParts = new ArrayList<>();
    //Символ для отрисовки головы змейки
    private static final String HEAD_SIGN = "\uD83C\uDD82";
    // Символ для отрисовки тела змейки
    private static final String BODY_SIGN = "\u25A0";
    public boolean isAlive = true;
    //Текущее направление движения
    private Direction direction = Direction.LEFT;

    public Snake(int x, int y) {
        GameObject gameObject1 = new GameObject(x, y);
        GameObject gameObject2 = new GameObject(x + 1, y);
        GameObject gameObject3 = new GameObject(x + 2, y);
        snakeParts.add(gameObject1);
        snakeParts.add(gameObject2);
        snakeParts.add(gameObject3);
    }

    public int getLength() {
        return snakeParts.size();
    }

    public Direction getDirection() {
        return direction;
    }

    //Изменение направления движения змейки
    public void setDirection(Direction direction) {
        if (!checkConterDirection(this.getDirection(), direction)) {
            if (direction == Direction.LEFT && snakeParts.get(0).y == snakeParts.get(1).y) {
                return;
            }
            if (direction == Direction.RIGHT && snakeParts.get(0).y == snakeParts.get(1).y) {
                return;
            }
            if (direction == Direction.UP && snakeParts.get(0).x == snakeParts.get(1).x) {
                return;
            }
            if (direction == Direction.DOWN && snakeParts.get(0).x == snakeParts.get(1).x) {
                return;
            }
            this.direction = direction;
        }
    }

    //Отрисовка змейки на игровом поле
    public void draw(Game game) {
        for (int i = 0; i < snakeParts.size(); i++) {
            Color c = Color.BLACK;
            if (!isAlive) {
                c = Color.RED;
            }
            if (i == 0) {
                game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, HEAD_SIGN, c, 75);
            } else {
                game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, c, 75);
            }
        }
    }

    // Движение змейки: в соседней с головой ячейке создается новая голова, а последний элемент (хвост) змейки удаляется.
    public void move(Apple apple) {
        GameObject oldHead = snakeParts.get(0);
        GameObject head = createNewHead();
        if (isOutOfField(head)) {
            isAlive = false;
        }
//Если змейка съела яблоко, то состояние яблока должно устанавливаться в неживое.А длина змейки увеличиваться на 1
        if (apple.x == head.x && apple.y == head.y) {
            apple.setAlive(false);
            snakeParts.add(0, head);
        } else {
            if (checkCollision(head)) {
                isAlive = false;
            } else {
                snakeParts.add(0, head);
                removeTail();
            }
        }
    }

    //Создать новый элемент с той стороны, куда движется змейка
    public GameObject createNewHead() {
        int x = snakeParts.get(0).x;
        int y = snakeParts.get(0).y;
        if (direction == Direction.LEFT) {
            x = x - 1;
        } else if (direction == Direction.DOWN) {
            y = y + 1;
        } else if (direction == Direction.RIGHT) {
            x = x + 1;
        } else if (direction == Direction.UP) {
            y = y - 1;
        }
        GameObject gameObject = new GameObject(x, y);
        return gameObject;
    }

    //Удалить последний элемент змейки
    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    //Проверить выходит ли элемент за границы игрового поля
    public boolean isOutOfField(GameObject gameObject) {
        if (gameObject.x < 0 || gameObject.y < 0 || gameObject.x >= SnakeGame.WIDTH || gameObject.y >= SnakeGame.HEIGHT) {
            return true;
        }
        return false;
    }

    //Проверить является ли изменяемое направление на противоположное. Т.к. змея не может разворачиваться на 180 градусов
    private boolean checkConterDirection(Direction current, Direction newDirection) {
        if (current == Direction.UP && newDirection == Direction.DOWN) {
            return true;
        }
        if (newDirection == Direction.UP && current == Direction.DOWN) {
            return true;
        }
        if (current == Direction.LEFT && newDirection == Direction.RIGHT) {
            return true;
        }
        if (newDirection == Direction.LEFT && current == Direction.RIGHT) {
            return true;
        }
        return false;
    }

    //Проверка новосозданной головы змейки на совпадение со всеми остальными элементами её тела
    public boolean checkCollision(GameObject gameObject) {
        for (GameObject object : snakeParts) {
            if (object.x == gameObject.x && object.y == gameObject.y) {
                return true;
            }
        }
        return false;
    }
}
