public class Apple {
    
    public int x;
    public int y;
    private int WIDTH = 500;
    private int HEIGHT = 500;
    private int UNIT_SIZE = 20;

    public Apple (int[] snakeX, int[] snakeY, int bodyParts) {
        x = (int) (Math.random() * (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        y = (int) (Math.random() * (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

        for (int i = bodyParts; i >= 0; i--) {
            while (snakeX[i] == x && snakeY[i] == y) {
                x = (int) (Math.random() * (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
                y = (int) (Math.random() * (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;                
            }
        }

    }
}
