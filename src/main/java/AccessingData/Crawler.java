package AccessingData;

import java.util.Optional;
import java.util.concurrent.*;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by dlo on 05/07/17.
 */
public class Crawler {
    //get number of available processors
    private ExecutorService executor;
    public Crawler() {
        int numberOfProcessors = Runtime.getRuntime().availableProcessors();
        executor = Executors.newFixedThreadPool(numberOfProcessors);
    }

    public Optional crawl(String url) {
         //A Future represents the result of an asynchronous computation. Methods are provided to check if the
        // computation is complete, to wait for its completion, and to retrieve the result of the computation.
        // The result can only be retrieved using method get when the computation has completed, blocking if
        // necessary until it is ready. Cancellation is performed by the cancel method. Additional methods
        // are provided to determine if the task completed normally or was cancelled. Once a computation has completed,
        // the computation cannot be cancelled. If you would like to use a Future for the sake of cancellability but
        // not provide a usable result, you can declare types of the form Future<?> and return null as
        // a result of the underlying task.
        try {
            Future<String> future = executor.submit(() -> HandleHTML.request(url));
            //future.get():Waits if necessary for the computation to complete, and then retrieves its result
            String result = future.get(30, TimeUnit.SECONDS);
            return Optional.of(result);
        }catch (TimeoutException ex) {
            LOGGER.warning("timeout exception");
            return Optional.empty();
        } catch (InterruptedException e) {
            LOGGER.warning("timeout exception");
        } catch (ExecutionException e) {
            LOGGER.warning("timeout exception");
        }
        return  Optional.empty();
    }

    public void shutdown() {
        executor.shutdown();
    }

}
