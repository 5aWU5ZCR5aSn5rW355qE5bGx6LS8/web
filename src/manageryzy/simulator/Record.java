package manageryzy.simulator;


class Record { // 用于卡口记录的类
    int car_id;
    int x, y;
    public Record(int id, int x, int y) {
        this.car_id = id;
        this.x = x;
        this.y = y;
    }
    public void print() {
        System.out.println(car_id + " " + x + " " + y);
        System.out.println("hello");
    }
}