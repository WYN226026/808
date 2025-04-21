package tools.entity;
//初始化数据实体类。
public class WorkIData {
    private int p1;
    private int p2;
    private int rf1;
    private int rf2;
    private int f1;
    private int f2;
    private int f3;

    private int id;

    public WorkIData(int p1, int p2, int rf1, int rf2, int f1, int f2, int f3, int id) {
        this.p1 = p1;
        this.p2 = p2;
        this.rf1 = rf1;
        this.rf2 = rf2;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.id = id;
    }

    @Override
    public String toString() {
        return "WorkIData{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                ", rf1=" + rf1 +
                ", rf2=" + rf2 +
                ", f1=" + f1 +
                ", f2=" + f2 +
                ", f3=" + f3 +
                ", id=" + id +
                '}';
    }

    public WorkIData() {

    }

    public int getP1() {
        return p1;
    }

    public void setP1(int p1) {
        this.p1 = p1;
    }

    public int getP2() {
        return p2;
    }

    public void setP2(int p2) {
        this.p2 = p2;
    }

    public int getRf1() {
        return rf1;
    }

    public void setRf1(int rf1) {
        this.rf1 = rf1;
    }

    public int getRf2() {
        return rf2;
    }

    public void setRf2(int rf2) {
        this.rf2 = rf2;
    }

    public int getF1() {
        return f1;
    }

    public void setF1(int f1) {
        this.f1 = f1;
    }

    public int getF2() {
        return f2;
    }

    public void setF2(int f2) {
        this.f2 = f2;
    }

    public int getF3() {
        return f3;
    }

    public void setF3(int f3) {
        this.f3 = f3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
