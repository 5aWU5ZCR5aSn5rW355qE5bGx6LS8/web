package manageryzy.simulator;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Simulator {
    static String str; // 车牌号
    static Scanner cin;
    static BufferedInputStream bis;
    static Direction[] directions = new Direction[4];
    static int tmp; // 生成时间随机数的变量
    static int dir; // car 的方向
    static ArrayList[] records = new ArrayList[600]; // 将卡口的记录数据按照时间顺序放进ArrayList，只记录600s内数据，也就是两个5分钟
    // ArrayList 不支持泛型数组
    // 生成车辆
    static Car[] car = new Car[200000];

    static int term; // car 运行到第几个5分钟

    public ArrayList Update(int i) {
        term = i / 300;     // 这里了类似于双缓冲的技术，
        term = (term + 1) % 2;
        i = i % 300;
        if(i == 1){ // 每次运行到一个5min(300s)的第一秒时，就将下一个5min(300s)的数据运行出来，并且将另一块的数据覆盖掉重写，只有两块5min的数据区，交换使用
            run();
        }
        return records[i + 300 * ((term + 1) % 2)];
    }

    private static double nextNextGaussian;
    private static boolean haveNextNextGaussian = false;
    public static double nextGaussian() {
        if (haveNextNextGaussian) {
            haveNextNextGaussian = false;
            return nextNextGaussian;
        } else {
            double v1, v2, s;
            do {
                v1 = Math.random(); // between -1.0 and 1.0
                v2 = Math.random(); // between -1.0 and 1.0
                s = v1 * v1 + v2 * v2;
            } while (s >= 1 || s == 0);
            double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
            nextNextGaussian = v2 * multiplier;
            haveNextNextGaussian = true;
            return v1 * multiplier;
        }
    }

    public static void init() {
        term = 0;
        // 定义方向
        directions[0] = new Direction(0, 1); // 这里的赋值放到main中，不要放到静态区
        directions[1] = new Direction(1, 0);
        directions[2] = new Direction(0, -1);
        directions[3] = new Direction(-1, 0); // 按照 上左下右 的顺序排列

        // 卡口的Arraylist初始化
        for (int i = 0; i < 600; i++) {
            records[i] = new ArrayList<Record>();
        }

        // 从car.txt 中读取数据
        try {
            bis = new BufferedInputStream(new FileInputStream("car.txt")); // 放到和src同级的位置
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        cin = new Scanner(bis);

        // 对每辆车进行初始化，第一次是生成，以后是从数据库中读取
        for (int i = 0; i < 200000; i++) {
            str = cin.nextLine();
            if (str.endsWith("0")) {
                str = str.substring(0, 7);
            } else {
                str = str.substring(8);
            }
            car[i] = new Car(i, str, 0, 0); // 后面申请对象时，totalTime会根据数据库中查询得到的进行

            // 生成车辆第一次出现的信息
            // tmp = (int) (Math.random() * 78 + 30); //
            // 以后的5分钟车出现的第一次时间就不一定是30-108之间了，要根据上一次的totaltime来算
            int random = (int) (Math.random() * 4); // 先生成 方向
            if (random == 0 || random == 2) {
                tmp = (int) (Math.random() * 60 + 30); // 纵向 为 500m 所用时间为 30 -
                // 90s
            } else {
                tmp = (int) (Math.random() * 72 + 36); // 横向为 600m 所用时间为 36
                // -108s
            }
            car[i].curdir = random;
            car[i].pos[0] = new Position(); // 注意一定要先申请一下，否则会报错
            car[i].setPos(0, (int) (nextGaussian() * 40), (int) (Math.random() * 40), tmp);
            car[i].nxtTime = tmp;
            // 这里的车辆坐标初始化只是随即函数，还没有想好用什么分布呢

        }

    }
    public static void run() {
        for(int icar = 0; icar < 200000; icar++) {
            if(car[icar].flag == 1){
                car[icar].nxtTime -= 300;
                car[icar].flag = 0;
            }
        }
        for(int itime = 0; itime < 300; itime++){
            System.out.println(itime + term * 300);
            if(!records[itime].isEmpty())
                records[itime + term * 300].clear();
        }

        long startTime = System.currentTimeMillis(); // 获取开始时间
        // 时间轴为基准进行计算没秒钟卡口的记录数据
        for (int itime = 0; itime < 300; itime++) {
            for (int icar = 0; icar < 200000; icar++) {
                if(car[icar].flag == 1) continue;
                // 判断是否在这一秒会被拍到
                if (car[icar].nxtTime >= 300) {
                    car[icar].flag = 1;
                    continue;
                }
                if (car[icar].nxtTime > itime)
                    continue;
                if (car[icar].nxtTime == itime) {

                    // 判断碰到边界
                    int dirx = car[icar].pos[car[icar].idx].x + directions[car[icar].curdir].x_offset;
                    int diry = car[icar].pos[car[icar].idx].y + directions[car[icar].curdir].y_offset;
                    // 判断是否车会越界
                    if (dirx < 0 || dirx >= 40 || diry < 0 || diry >= 40) {
                        dirx = car[icar].pos[car[icar].idx].x - directions[car[icar].curdir].x_offset;
                        diry = car[icar].pos[car[icar].idx].y - directions[car[icar].curdir].y_offset;
                        car[icar].curdir += 2;
                        car[icar].curdir %= 4;
                    }
                    car[icar].idx++;
                    if(car[icar].idx == 30) car[icar].idx = 0;
                    car[icar].setPos(car[icar].idx, dirx, diry, itime);


                    records[itime + term * 300].add(new Record(icar, dirx, diry));
                    continue;
                }

                // 判断下一段的方向
                int random = (int) (Math.random() * 4); // 随机生成的方向
                int oppodir = car[icar].curdir + 2;
                oppodir = oppodir % 4; // 得到car 现在方向的相反方向
                if (random != car[icar].curdir) {
                    random = (int) (Math.random() * 4); // 若果不是沿原来的方向，就在去取一次随机数
                }
                if (random == oppodir) {
                    random = (int) (Math.random() * 4); // 如果是原来方向的反方向，再取一次随机数
                }
                // 最后的各个方向的可能性为 当前方向 > 旁边两个方向 > 相反方向

                // 根据方向来确定下一次卡口拍到的时间，速度随机
                if (random == 0 || random == 2) {
                    tmp = (int) (Math.random() * 60 + 30); // 纵向 为 500m 所用时间为 30
                    // - 90s
                } else {
                    tmp = (int) (Math.random() * 72 + 36); // 横向为 600m 所用时间为 36
                    // -108s
                }
                car[icar].nxtTime = car[icar].nxtTime + tmp;
                car[icar].curdir = random;
            }

        }

        long endTime = System.currentTimeMillis(); // 获取结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");

    }

    public static void main(String[] args) {
        init();
        run();
    }
}
