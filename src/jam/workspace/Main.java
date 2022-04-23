package jam.workspace;

import jam.workspace.queue.Consumer;
import jam.workspace.service.FunctionService;
import jam.workspace.service.FunctionServiceImpl;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {

    /*
    * 1) The main thread reads the input.txt file
    * 2) Enter a number of threads you want in thread pool.
    *    Threads in thread pool find the factorial of read number from input.txt
    *    Factorial which is found by any thread will be put into ArrayBlockingQueue( which capacity is 10)
    * 3) Thread "Writing Thread" writes the elements from ArrayBlockingQueue into output.txt
    * */

    private static FunctionService functionService = new FunctionServiceImpl();

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);

        // Assign the amount of thread in Thread Pool
        ExecutorService executorService = Executors.newFixedThreadPool(sc.nextInt());

        // Enter your input.txt file location
        File file1 = new File("/home/atabek/Downloads/Test Task Folder/input.txt");
        BufferedReader br = new BufferedReader(new FileReader(file1));



        /*
         * With the help of Consumer we write the factorial of numbers into output.txt file
         * */
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(10);
        Consumer consumer = new Consumer(blockingQueue);
        Thread thread = new Thread(consumer, "Writing Thread");
        thread.start();

        // Main thread reads numbers from input.txt
        String str = "";
        while ((str = br.readLine()) != null) {
            if (!str.isBlank()) {
                // Thread pool solves factorial of read numbers and put into blockingQueue the returned result
                Future ft = executorService.submit(newCallable(Long.parseLong(str)));
                try {
                    String solvedFactorial = (String) ft.get();
                    System.out.println(solvedFactorial);
                    blockingQueue.put(solvedFactorial);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
//        Closing the resources
        executorService.shutdown();
        br.close();
        consumer.closeBufferedWriter();
    }

    public static Callable newCallable(long number) {
        return new Callable() {
            @Override
            public Object call() throws Exception {
                String factorial = functionService.findFactorial(number);
                /* if you want to see which thread is working uncomment the code below on 64 line */
//                System.out.println(Thread.currentThread().getName() + " - works with " + factorial + "\n");

                return factorial;
            }
        };
    }
}
