package io.soliditycraft.solidityloader.utils;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * A generic Queue implementation using LinkedList.
 *
 * <p>This class provides a basic FIFO (First-In-First-Out) data structure. Elements are added to
 * the end of the queue and removed from the front.</p>
 *
 * @param <T> The type of elements in the queue.
 */
public class Queue<T> {
    private final LinkedList<T> list;

    /**
     * Creates an empty queue.
     * <p>This constructor initializes the queue as an empty list.</p>
     */
    public Queue() {
        this.list = new LinkedList<>();
    }

    /**
     * Adds an element to the end of the queue.
     *
     * @param element The element to be added to the queue.
     * @throws IllegalArgumentException if the element is null (if null elements are not allowed).
     * @example <pre>
     * Queue<String> queue = new Queue<>();
     * queue.enqueue("First");
     * queue.enqueue("Second");
     * </pre>
     */
    public void enqueue(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null.");
        }
        list.addLast(element);
    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * @return The element at the front of the queue.
     * @throws NoSuchElementException if the queue is empty.
     * @example <pre>
     * Queue<Integer> queue = new Queue<>();
     * queue.enqueue(1);
     * int first = queue.dequeue(); // Returns 1
     * </pre>
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        return list.removeFirst();
    }

    /**
     * Returns the element at the front of the queue without removing it.
     *
     * @return The element at the front of the queue.
     * @throws NoSuchElementException if the queue is empty.
     * @example <pre>
     * Queue<String> queue = new Queue<>();
     * queue.enqueue("Hello");
     * String front = queue.peek(); // Returns "Hello" without removing it
     * </pre>
     */
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        return list.getFirst();
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue is empty, false otherwise.
     * @example <pre>
     * Queue<Double> queue = new Queue<>();
     * boolean empty = queue.isEmpty(); // Returns true
     * </pre>
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Processes each element in the queue using the specified action.
     *
     * @param action The action to be performed on each element.
     * @example <pre>
     * Queue<String> queue = new Queue<>();
     * queue.enqueue("Player1");
     * queue.enqueue("Player2");
     * queue.process(player -> System.out.println(player + " joined the game."));
     * // Outputs:
     * // Player1 joined the game.
     * // Player2 joined the game.
     * </pre>
     */
    public void process(Consumer<? super T> action) {
        for (T element : list) {
            action.accept(element);
        }
    }

    /**
     * Returns the number of elements in the queue.
     *
     * @return The size of the queue.
     * @example <pre>
     * Queue<Character> queue = new Queue<>();
     * queue.enqueue('A');
     * int size = queue.size(); // Returns 1
     * </pre>
     */
    public int size() {
        return list.size();
    }

    /**
     * Clears the queue, removing all elements.
     * <p>After calling this method, the queue will be empty.</p>
     *
     * @example <pre>
     * Queue<Integer> queue = new Queue<>();
     * queue.enqueue(1);
     * queue.clear(); // The queue is now empty
     * </pre>
     */
    public void clear() {
        list.clear();
    }

    /**
     * Returns true if the queue contains the specified element.
     *
     * @param element The element to search for in the queue.
     * @return true if the queue contains the specified element, false otherwise.
     * @example <pre>
     * Queue<String> queue = new Queue<>();
     * queue.enqueue("Apple");
     * boolean contains = queue.contains("Apple"); // Returns true
     * </pre>
     */
    public boolean contains(T element) {
        return list.contains(element);
    }

    /**
     * Returns an array containing all elements in the queue.
     *
     * @return An array of elements in the queue.
     * @example <pre>
     * Queue<Integer> queue = new Queue<>();
     * queue.enqueue(1);
     * queue.enqueue(2);
     * Integer[] array = queue.toArray(); // Returns [1, 2]
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        return (T[]) list.toArray();
    }

    /**
     * Returns a string representation of the queue.
     *
     * @return A string containing all elements in the queue, in order.
     * @example <pre>
     * Queue<String> queue = new Queue<>();
     * queue.enqueue("A");
     * queue.enqueue("B");
     * String str = queue.toString(); // Returns "[A, B]"
     * </pre>
     */
    @Override
    public String toString() {
        return list.toString();
    }

    /**
     * Returns an iterable to allow for easy iteration through the queue's elements.
     *
     * @return An iterable of the queue elements.
     */
    public Iterable<T> iterable() {
        return list;
    }
}
