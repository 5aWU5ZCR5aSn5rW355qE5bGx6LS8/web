package manageryzy.simulator;


class Car {
    int num;
    String str;
    int totalTime; // 每辆车的5分钟内计时时间,跟Position中的time好像重复了
    int idx; // 记录汽车5分钟内的当前的第几次，查询的时候用
    Position[] pos;
    int curdir; // car 的当前方向， 0,1,2,3
    int nxtTime; // car 下一次出现的时间
    int flag; // 判断 car 是否已经超出了当前的 5 min
    public Car(int num, String str, int totalTime, int idx) {
        this.num = num;
        this.str = str;
        this.totalTime = totalTime;
        this.idx = idx;
        this.pos = new Position[30]; // 注意一定要再申请一下，否则会报错
        this.flag = 0;
    }
    public void setPos(int idx, int x, int y, int time) {
        pos[idx] = new Position(); // 注意一定要再申请一下，否则会报错
        pos[idx].x = x;
        pos[idx].y = y;
        totalTime += time;
        pos[idx].time = totalTime;
    }
}