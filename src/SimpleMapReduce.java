import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleMapReduce {

    private static String inputFilePath = "hamlet.txt";
    private static CopyOnWriteArrayList<String> c4 = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<String> c3 = new CopyOnWriteArrayList<>();


    private static String[] splitTask(String inputFile) {
        return inputFile.split("\n");
    }

    private List<Runnable> threadsPool = new ArrayList<>();

    public SimpleMapReduce() {

    }

    private static Map<String, Integer> mapping(String taskInput) {
        Map<String, Integer> rst = new HashMap<>();
        String[] words = taskInput.split(" ");
        for (String word : words) {
            if (rst.containsKey(word)) {
                rst.put(word, rst.get(word) + 1);
            } else {
                rst.put(word, 1);
            }
        }
        return rst;
    }

    private static void shuffling(List<Map<String, Integer>> mapped) {
        for (Map<String, Integer> m : mapped) {
            for (Map.Entry<String, Integer> entry : m.entrySet()) {
                int val = entry.getValue();
                if (entry.getKey().length() == 3) {
                    for (int i=0; i<val; i++) {
                        c3.add(entry.getKey());
                    }
                }
                if (entry.getKey().length() == 4) {
                    for (int i=0; i<val; i++) {
                        c4.add(entry.getKey());
                    }
                }
            }
        }
    }

    private static Map<String, Integer> reduceAndoutput() {
        Map<String, Integer> rst = new HashMap<>();
        for (String word : c3) {
            if (rst.containsKey(word)) {
                rst.put(word, rst.get(word) + 1);
            } else {
                rst.put(word, 1);
            }
        }
        for (String word : c4) {
            if (rst.containsKey(word)) {
                rst.put(word, rst.get(word) + 1);
            } else {
                rst.put(word, 1);
            }
        }
        return rst;
    }


    public static void main(String[] args) {

        Runnable t1 = new YourThread(splitTask(inputFile));

        System.out.println("Starting MapReduce");
    }

    static class YourThread implements Runnable {
        String name;
        String[] workFunc;
        public YourThread(String[] func) {
            this.workFunc = func;
        }
        @Override
        public void run() {
            try {
                this.workFunc.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


