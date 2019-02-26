package GUI;

public class MyListObject {

    private String name;
    private int number;

    public MyListObject(String name, int number) {
        this.setName(name);
        this.setNumber(number);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
