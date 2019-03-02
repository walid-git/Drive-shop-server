package observer;

public interface Observable<T> {
    void addObserver(Observer observer);

    void notifyObserver(T object);

    void removeObserver(Observer observer);
}
