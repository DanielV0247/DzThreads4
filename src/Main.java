import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static final String LETTERS = "abc";
    public static final int AMOUNT = 10_000;
    public static final int LENGTH = 100_000;
    public static ArrayBlockingQueue<String> qA = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> qB = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> qC = new ArrayBlockingQueue<>(100);
    public static AtomicInteger sumA = new AtomicInteger(0);
    public static AtomicInteger sumB = new AtomicInteger(0);
    public static AtomicInteger sumC = new AtomicInteger(0);

    public static void main(String[] args) {
        new Thread(()->{
            for (int i = 0; i <AMOUNT ; i++) {
                String text = generateText(LETTERS,LENGTH);
                try {
                    qA.put(text);
                    qB.put(text);
                    qC.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < AMOUNT; i++) {
                try {
                    String rout = qA.take();
                    CountChars(rout, 'a', sumA);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимальное количество символов 'a': " + sumA);
        }).start();

        new Thread(() -> {
            for (int i = 0; i < AMOUNT; i++) {
                try {
                    String rout = qB.take();
                    CountChars(rout, 'b', sumB);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимальное количество символов 'b': " + sumB);
        }).start();

        new Thread(() -> {
            for (int i = 0; i < AMOUNT; i++) {
                try {
                    String rout = qC.take();
                    CountChars(rout, 'c', sumC);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимальное количество символов 'c': " + sumC);
        }).start();

    }

    private static void CountChars(String text, char f, AtomicInteger sum) {
        int frequency = (int) text.chars().filter(ch -> ch == f).count();
        if (sum.get() < frequency) {
            sum.set(frequency);
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}