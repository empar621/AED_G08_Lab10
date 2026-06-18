package listlinked;

public class ListLinked<T> {
    private Node<T> first;
    private int size;

    public ListLinked() {
        this.first = null;
        this.size = 0;
    }

    public int size() {
        return this.size;
    }

    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (first == null) {
            first = newNode;
        } else {
            Node<T> current = first;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }
        Node<T> current = first;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }
}