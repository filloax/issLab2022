package unibo.actor22comm.interfaces;

import java.util.Observer;

public interface IObserver extends Observer {
    void update(String value);
    //From Observer: public void update(Observable o, Object news)
}
