package manageryzy.simulator;


class Record { // 用于卡口记录的类
    String car_id;
    int x, y,t;

    public Record(String id, int x, int y,int t) {
        this.car_id = id;
        this.x = x;
        this.y = y;
        this.t = t;
    }
}