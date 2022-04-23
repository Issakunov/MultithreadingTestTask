package jam.workspace.queue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class Consumer implements Runnable {

    BlockingQueue<String> blockingQueue = null;
    BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));

    public Consumer(BlockingQueue<String> queue) throws IOException {
        this.blockingQueue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String element = this.blockingQueue.take();
                bw.write(element);
                bw.newLine();
            }catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void closeBufferedWriter() {
        try {
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
