package manageryzy.simulator;

//20km/h - 60km/h   108s之内(20km/h跑600m)肯定会拍到一次，30s(60km/h跑500m)之内不会拍到第二次，5min/300s内最多有10次出镜机会
// 600m的距离至少需要36s, 500米距离最多需要90s
// 20万辆车有 200万次记录
class Direction {
    int x_offset;
    int y_offset;
    public Direction(int x, int y) {
        x_offset = x;
        y_offset = y;
    }
}