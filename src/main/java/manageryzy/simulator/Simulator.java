package manageryzy.simulator;

import com.csvreader.CsvReader;
import manageryzy.simulator.car.Car;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Simulator implements ISimulator{
    ArrayList<Car> cars;
    ArrayList<Record> results;
    int t = 0;

    Timer timer = null;
    final Lock lock = new Lock();
    final Lock lock2 = new Lock();

    static Simulator mSimulator;

    Simulator(String path) throws IOException {
        cars = new ArrayList<>();
        results = new ArrayList<>();
        this.intiCars(path);
    }

    protected int intiCars(String path) throws IOException {
        CsvReader carCSV = new CsvReader(path);

        while (carCSV.readRecord()){
            String[] line = carCSV.getValues();

            String carID;
            if(line[1].equals("0")){
                carID = line[0];
            }else {
                carID = line[1];
            }

            Car c = new Car(carID) {
                @Override
                public void onUpdate() throws Exception {
                    if(t==getNextT()) {
                        int nextX,nextY,dir,nextT;
                        results.add(new Record(getCarID(), getX(), getY(), getNextT()));

                        if(getCarID().equals("A000001"))
                        {
                            getCarID();
                        }

                        dir = getDirection();
                        double speed = (Math.random()*40 + 20)/3.6;

                        if(Math.random()>0.5){
                            dir = (int)(Math.random()*4);
                        }

                        switch (dir) {
                            case 0:
                                nextX = getX();
                                nextY = getY() + 600;
                                break;
                            case 1:
                                nextX = getX();
                                nextY = getY() - 600;
                                break;
                            case 2:
                                nextX = getX() - 500;
                                nextY = getY();
                                break;
                            case 3:
                                nextX = getX() + 500;
                                nextY = getY();
                                break;
                            default:
                                throw new Exception("unknown direction");
                        }

                        if(nextX < 0){
                            if(Math.random()>0.5){
                                dir = 0;
                            }else {
                                dir = 1;
                            }
                        }else if(nextX > 25000){
                            if(Math.random()>0.5){
                                dir = 0;
                            }else {
                                dir = 1;
                            }
                        }else  if(nextY < 0){
                            if(Math.random()>0.5){
                                dir = 2;
                            }else {
                                dir = 3;
                            }
                        }else  if(nextY > 25000){
                            if(Math.random()>0.5){
                                dir = 2;
                            }else {
                                dir = 3;
                            }
                        }

                        switch (dir) {
                            case 0:
                                nextX = getX();
                                nextY = getY() + 600;
                                nextT = t+(int)(600/speed);
                                break;
                            case 1:
                                nextX = getX();
                                nextY = getY() - 600;
                                nextT = t+(int)(600/speed);
                                break;
                            case 2:
                                nextX = getX() - 500;
                                nextY = getY();
                                nextT = t+(int)(500/speed);
                                break;
                            case 3:
                                nextX = getX() + 500;
                                nextY = getY();
                                nextT = t+(int)(500/speed);
                                break;
                            default:
                                throw new Exception("unknown direction");
                        }

                        setY(nextY);
                        setX(nextX);
                        setDirection(dir);
                        setNextT(nextT);
                    }
                }

                @Override
                public void init() {
                    int x,y,nextT,direction;

                    x = (int)(Math.random()*50 + 1) * 500;
                    y = (int)(Math.random()*40 + 1) * 600;

                    direction = (int)(Math.random()*4);

                    nextT = (int)(Math.random()*30);

                    setX(x);
                    setY(y);
                    setNextT(nextT);
                    setDirection(direction);
                }
            };

            cars.add(c);

        }

        return 0;
    }

    public void update() throws Exception {
        results.clear();

        for(Car car:cars)
        {
            car.onUpdate();
        }

        t++;

        this.onUpdate(results);
    }

    public static Simulator getmSimulator() {
        return mSimulator;
    }

    public static void setmSimulator(Simulator mSimulator) {
        Simulator.mSimulator = mSimulator;
    }

    public void run() {
        synchronized (lock2) {
            if (isRunning()) {
                return;
            }
            synchronized (lock) {
                lock.setSignal(true);
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            update();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (!lock.getSig()) {
                            synchronized (lock) {
                                lock.notifyAll();
                                timer.cancel();
                            }
                        }
                    }
                }, 1000, 1000);
            }
        }
    }

    public void pause() throws InterruptedException {
        synchronized (lock2) {
            synchronized (lock) {
                if (this.isRunning()) {
                    lock.setSignal(false);
                    lock.wait();
                }
            }
            timer = null;
        }
    }

    public void step() throws Exception {
        synchronized (lock2) {
            if (isRunning()) {
                pause();
            }

            update();
        }
    }

    public boolean isRunning() {
        synchronized (lock2) {
            return timer != null;
        }
    }


    public int getT() {
        return t;
    }
}
