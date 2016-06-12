package manageryzy.simulator;


class Car {
    int num;
    String str;
    int totalTime; // ÿ������5�����ڼ�ʱʱ��,��Position�е�time�����ظ���
    int idx; // ��¼����5�����ڵĵ�ǰ�ĵڼ��Σ���ѯ��ʱ����
    Position[] pos;
    int curdir; // car �ĵ�ǰ���� 0,1,2,3
    int nxtTime; // car ��һ�γ��ֵ�ʱ��
    int flag; // �ж� car �Ƿ��Ѿ������˵�ǰ�� 5 min
    public Car(int num, String str, int totalTime, int idx) {
        this.num = num;
        this.str = str;
        this.totalTime = totalTime;
        this.idx = idx;
        this.pos = new Position[30]; // ע��һ��Ҫ������һ�£�����ᱨ��
        this.flag = 0;
    }
    public void setPos(int idx, int x, int y, int time) {
        pos[idx] = new Position(); // ע��һ��Ҫ������һ�£�����ᱨ��
        pos[idx].x = x;
        pos[idx].y = y;
        totalTime += time;
        pos[idx].time = totalTime;
    }
}