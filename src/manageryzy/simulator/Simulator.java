package manageryzy.simulator;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Simulator {
    static String str; // ���ƺ�
    static Scanner cin;
    static BufferedInputStream bis;
    static Direction[] directions = new Direction[4];
    static int tmp; // ����ʱ��������ı���
    static int dir; // car �ķ���
    static ArrayList[] records = new ArrayList[600]; // �����ڵļ�¼���ݰ���ʱ��˳��Ž�ArrayList��ֻ��¼600s�����ݣ�Ҳ��������5����
    // ArrayList ��֧�ַ�������
    // ���ɳ���
    static Car[] car = new Car[200000];

    static int term; // car ���е��ڼ���5����

    public ArrayList Update(int i) {
        term = i / 300;     // ������������˫����ļ�����
        term = (term + 1) % 2;
        i = i % 300;
        if(i == 1){ // ÿ�����е�һ��5min(300s)�ĵ�һ��ʱ���ͽ���һ��5min(300s)���������г��������ҽ���һ������ݸ��ǵ���д��ֻ������5min��������������ʹ��
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
        // ���巽��
        directions[0] = new Direction(0, 1); // ����ĸ�ֵ�ŵ�main�У���Ҫ�ŵ���̬��
        directions[1] = new Direction(1, 0);
        directions[2] = new Direction(0, -1);
        directions[3] = new Direction(-1, 0); // ���� �������� ��˳������

        // ���ڵ�Arraylist��ʼ��
        for (int i = 0; i < 600; i++) {
            records[i] = new ArrayList<Record>();
        }

        // ��car.txt �ж�ȡ����
        try {
            bis = new BufferedInputStream(new FileInputStream("car.txt")); // �ŵ���srcͬ����λ��
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        cin = new Scanner(bis);

        // ��ÿ�������г�ʼ������һ�������ɣ��Ժ��Ǵ����ݿ��ж�ȡ
        for (int i = 0; i < 200000; i++) {
            str = cin.nextLine();
            if (str.endsWith("0")) {
                str = str.substring(0, 7);
            } else {
                str = str.substring(8);
            }
            car[i] = new Car(i, str, 0, 0); // �����������ʱ��totalTime��������ݿ��в�ѯ�õ��Ľ���

            // ���ɳ�����һ�γ��ֵ���Ϣ
            // tmp = (int) (Math.random() * 78 + 30); //
            // �Ժ��5���ӳ����ֵĵ�һ��ʱ��Ͳ�һ����30-108֮���ˣ�Ҫ������һ�ε�totaltime����
            int random = (int) (Math.random() * 4); // ������ ����
            if (random == 0 || random == 2) {
                tmp = (int) (Math.random() * 60 + 30); // ���� Ϊ 500m ����ʱ��Ϊ 30 -
                // 90s
            } else {
                tmp = (int) (Math.random() * 72 + 36); // ����Ϊ 600m ����ʱ��Ϊ 36
                // -108s
            }
            car[i].curdir = random;
            car[i].pos[0] = new Position(); // ע��һ��Ҫ������һ�£�����ᱨ��
            car[i].setPos(0, (int) (nextGaussian() * 40), (int) (Math.random() * 40), tmp);
            car[i].nxtTime = tmp;
            // ����ĳ��������ʼ��ֻ���漴��������û�������ʲô�ֲ���

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

        long startTime = System.currentTimeMillis(); // ��ȡ��ʼʱ��
        // ʱ����Ϊ��׼���м���û���ӿ��ڵļ�¼����
        for (int itime = 0; itime < 300; itime++) {
            for (int icar = 0; icar < 200000; icar++) {
                if(car[icar].flag == 1) continue;
                // �ж��Ƿ�����һ��ᱻ�ĵ�
                if (car[icar].nxtTime >= 300) {
                    car[icar].flag = 1;
                    continue;
                }
                if (car[icar].nxtTime > itime)
                    continue;
                if (car[icar].nxtTime == itime) {

                    // �ж������߽�
                    int dirx = car[icar].pos[car[icar].idx].x + directions[car[icar].curdir].x_offset;
                    int diry = car[icar].pos[car[icar].idx].y + directions[car[icar].curdir].y_offset;
                    // �ж��Ƿ񳵻�Խ��
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

                // �ж���һ�εķ���
                int random = (int) (Math.random() * 4); // ������ɵķ���
                int oppodir = car[icar].curdir + 2;
                oppodir = oppodir % 4; // �õ�car ���ڷ�����෴����
                if (random != car[icar].curdir) {
                    random = (int) (Math.random() * 4); // ����������ԭ���ķ��򣬾���ȥȡһ�������
                }
                if (random == oppodir) {
                    random = (int) (Math.random() * 4); // �����ԭ������ķ�������ȡһ�������
                }
                // ���ĸ�������Ŀ�����Ϊ ��ǰ���� > �Ա��������� > �෴����

                // ���ݷ�����ȷ����һ�ο����ĵ���ʱ�䣬�ٶ����
                if (random == 0 || random == 2) {
                    tmp = (int) (Math.random() * 60 + 30); // ���� Ϊ 500m ����ʱ��Ϊ 30
                    // - 90s
                } else {
                    tmp = (int) (Math.random() * 72 + 36); // ����Ϊ 600m ����ʱ��Ϊ 36
                    // -108s
                }
                car[icar].nxtTime = car[icar].nxtTime + tmp;
                car[icar].curdir = random;
            }

        }

        long endTime = System.currentTimeMillis(); // ��ȡ����ʱ��
        System.out.println("��������ʱ�䣺 " + (endTime - startTime) + "ms");

    }

    public static void main(String[] args) {
        init();
        run();
    }
}
