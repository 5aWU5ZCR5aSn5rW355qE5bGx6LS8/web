package manageryzy.simulator.car;

public abstract class Car implements ICar{
    int x,y;
    int nextT,direction;
    String carID;

    public Car(String carID) {
        this.carID = carID;
        this.init();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getCarID() {
        return carID;
    }

    public int getNextT() {
        return nextT;
    }


    public void setNextT(int nextT) {
        this.nextT = nextT;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}