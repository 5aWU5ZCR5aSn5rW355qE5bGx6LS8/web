package manageryzy.simulator;


public class Lock {
    protected boolean sig;

    public Lock() {
        sig = false;
    }

    public void setSignal(boolean signal) {
        this.sig = signal;
    }

    public boolean getSig() {
        return sig;
    }
}
