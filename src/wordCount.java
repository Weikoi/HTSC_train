import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.CountDownLatch;




public class wordCount {

    public static HashMap<String,LinkedList<Integer>> result;

    public static class map implements Runnable {
        private PublicBox box;

        private String[] s;
        public LinkedList<String> wordmap;

        public map(String[] string,PublicBox box1){
            s = string;
            wordmap = new LinkedList<>();
            box = box1;
        }

        @Override
        public void run() {
            for(int i=0;i<s.length;i++){
                String[] res= s[i].split("[^a-zA-Z0-9]+");
                for(String s : res)
                    wordmap.add(s);
            }
            //System.out.println(wordmap.size());
            try {
                Thread. sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            box.increace(wordmap);
        }
    }

    public static class shuffle implements Runnable{
        public HashMap<String,LinkedList<Integer>> res;

        private PublicBox box;

        private CountDownLatch countDownLatch;

        public shuffle(PublicBox box1,CountDownLatch c,HashMap<String,LinkedList<Integer>> r){
            box = box1;
            res = r;
            countDownLatch = c;
        }


        @Override
        public void run() {
            try {
                Thread. sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LinkedList<String> mapres = box.decreace();
            for(String s : mapres){
                if(!s.equals("")){
                    if(res.containsKey(s))
                        res.get(s).add(1);
                    else{
                        LinkedList<Integer> linkedList = new LinkedList<>();
                        linkedList.add(1);
                        res.put(s,linkedList);
                    }
                }
            }
            countDownLatch.countDown();
        }
    }


    public static class reducer implements Runnable{
        private HashMap<String,LinkedList<Integer>> reduinput;
        public HashMap<String,Integer> redures;

        private CountDownLatch countDownLatch;

        public reducer(HashMap<String,LinkedList<Integer>> shuffleres,CountDownLatch c){
            reduinput = shuffleres;
            redures = new HashMap<>();
            countDownLatch = c;
        }

        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(Map.Entry<String,LinkedList<Integer>> entry : reduinput.entrySet()){
                redures.put(entry.getKey(),entry.getValue().size());
            }
            List<Map.Entry<String,Integer>> infoIds = new ArrayList<Map.Entry<String,Integer>>(redures.entrySet());
            infoIds.sort(new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            });
            System.out.println(infoIds);
        }
    }


    public static class PublicBox {
        private LinkedList<LinkedList<String>> app = new LinkedList<>();

        public synchronized void increace(LinkedList<String> s) {
            while (app.size() == 10) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            app.add(s);
            System.out.println("生成map结果成功！");
            notify();
        }

        public synchronized LinkedList<String> decreace() {
            while (app.size() == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LinkedList<String> res = app.get(app.size()-1);
            app.remove(app.size()-1);
            System.out.println("消费map结果成功！");
            notify();
            return res;
        }
    }

    public static void main(String[] args) {

        LinkedList<String[]> s = new LinkedList<>();
        LinkedList<String> mapres = new LinkedList<>();
        PublicBox box= new PublicBox();

        HashMap<String,LinkedList<Integer>> res = new HashMap<>();

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("hamlet.txt")),
                    "UTF-8"));
            String lineTxt = null;
            int num = 0;
            String[] input = new String[2000];
            while ((lineTxt = br.readLine()) != null) {
                if( num == 2000){
                    s.add(input);
                    input = new String[2000];
                    num = 0;
                }
                input[num] = lineTxt;
                num++;
            }
        }catch (Exception e) {
            System.err.println("read errors :" + e);
        }

        CountDownLatch countDownLatch = new CountDownLatch(s.size());


        for(String[] inn : s){
            Runnable maptask = new map(inn,box);
            Thread thread = new Thread(maptask);
            thread.start();

            Runnable shuffletask = new shuffle(box,countDownLatch,res);
            Thread thread1 = new Thread(shuffletask);
            thread1.start();
            //thread.join();
            //Runnable shuffletask = new shuffle()
        }

        Runnable reducertask = new reducer(res,countDownLatch);
        Thread thread3 = new Thread(reducertask);
        thread3.start();


    }
}
