package unibo.actor22comm.interfaces;


public interface IObservable {
    void addObserver(IObserver obs); //implemented by Java's Observable

    void deleteObserver(IObserver obs); //implemented by Java's Observable
}
