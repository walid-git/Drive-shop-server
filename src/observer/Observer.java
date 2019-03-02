package observer;

public interface Observer<T> {
    void onChange(T obj);
}
