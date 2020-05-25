package snake;
//ВВЕРХ- стрелка вверх
//ВНИЗ- стрелка вниз
//ВЛЕВО- стрелка влево
//ВПРАВО- стрелка вправо
//НОВАЯ ИГРА-пробел


import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

//Логика игры
public class SnakeGame extends Game {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private Snake snake;
    //Продолжительность хода
    private int turnDelay;
    private Apple apple;
    private boolean isGameStopped;
    //Длина змейки при которой игра заканчивается победой
    private static final int GOAL = 28;
    //Набранные очки
    private int score;

    @Override
    public void setScore(int score) {
        super.setScore(score);
        this.score=score;
    }

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    //Создание игры
    private void createGame() {
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        score=0;
        setScore(0);
        createNewApple();
        isGameStopped = false;
        drawScene();
        //Игра будет начинаться со скоростью 300мс/ход
        turnDelay = 300;
        setTurnTimer(turnDelay);
    }

    //Отрисовка экрана
    private void drawScene() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                setCellValueEx(x, y, Color.DARKSEAGREEN, "");
            }
        }
        snake.draw(this);
        apple.draw(this);
    }

    // Всё, что должно происходить в игре на протяжении одного хода, описывается здесь
    @Override
    public void onTurn(int step) {
        snake.move(apple);
        if (!apple.isAlive) {
            score = score + 5;
            setScore(score);
            turnDelay = turnDelay - 10;
            setTurnTimer(turnDelay);
            createNewApple();
        }
        if (!snake.isAlive) {
            gameOver();
        }
        if (snake.getLength() > GOAL) {
            win();
        }
        drawScene();
    }

    //Кнопки для управления змейкой
    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case LEFT:
                snake.setDirection(Direction.LEFT);
                break;
            case RIGHT:
                snake.setDirection(Direction.RIGHT);
                break;
            case UP:
                snake.setDirection(Direction.UP);
                break;
            case DOWN:
                snake.setDirection(Direction.DOWN);
                break;
            case SPACE:
                if (isGameStopped) {
                    createGame();
                }
                break;
        }
    }

    // Генерация новых яблок
    private void createNewApple() {
        do {
            int a = getRandomNumber(WIDTH);
            int b = getRandomNumber(HEIGHT);
            apple = new Apple(a, b);
        } while (snake.checkCollision(apple));
    }

    //Действия при победе
    private void win() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.ANTIQUEWHITE, "YOU WIN", Color.BLACK, 55);
    }

    //Команды, которые выполняются при остановке игры (проигрыше)
    private void gameOver() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.GAINSBORO, "GAME OVER", Color.BLACK, 55);
    }
}