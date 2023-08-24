public class Snake {

    public int [] x;
    public int [] y;
    public int bodyParts;
    public char direction;
    private int WIDTH = 500;
    private int HEIGHT = 500;
    private int UNIT_SIZE = 20;

    public Snake (int gameUnits) {
        x = new int [gameUnits];
        y = new int [gameUnits];

        x[0] = WIDTH / 2 - UNIT_SIZE / 2;
        y[0] = HEIGHT / 2 - UNIT_SIZE / 2;

        bodyParts = 1;
        direction = 'R';       
    }

    public void move () {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public boolean checkCollisions () {
        //if collided, return true else return false
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                return false;
            }
        }

        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            return false;
        }

        return true;
    }

    public void setBodyParts (int num) {
        bodyParts = num;
    }

    public void setDirection (char d) {
        direction = d;
    }
}
