package manageryzy.simulator;

//20km/h - 60km/h   108s֮��(20km/h��600m)�϶����ĵ�һ�Σ�30s(60km/h��500m)֮�ڲ����ĵ��ڶ��Σ�5min/300s�������10�γ�������
// 600m�ľ���������Ҫ36s, 500�׾��������Ҫ90s
// 20�������� 200��μ�¼
class Direction {
    int x_offset;
    int y_offset;
    public Direction(int x, int y) {
        x_offset = x;
        y_offset = y;
    }
}