package manageryzy.simulator;


class Record { // ���ڿ��ڼ�¼����
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