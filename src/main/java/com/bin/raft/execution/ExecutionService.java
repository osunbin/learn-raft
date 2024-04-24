package com.bin.raft.execution;

import com.bin.raft.common.utils.ConcurrencyUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.bin.raft.common.utils.Preconditions.checkNotNull;
import static java.lang.Thread.currentThread;

public class ExecutionService {

    private static volatile int currentAvailableProcessors = Runtime.getRuntime().availableProcessors();


    private static final long KEEP_ALIVE_TIME = 60L;

    private static final int CORE_POOL_SIZE = 3;

    private static final long AWAIT_TIME = 3;

    String SYSTEM_EXECUTOR = "system";

    private static final String  SCHEDULED = "scheduled";

    private static final String  EXECUTOR = "executor";

    private final ConcurrentMap<String, ExecutorService> executors = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, ScheduledThreadPoolExecutor> schedulers = new ConcurrentHashMap<>();


    private final ExecutorService globalExecutor;


    private final ScheduledThreadPoolExecutor globalTaskScheduler;

    public ExecutionService() {

        PoolExecutorThreadFactory poolSchedulerThreadFactory =
                new PoolExecutorThreadFactory(createThreadName(SYSTEM_EXECUTOR,SCHEDULED));
        globalTaskScheduler = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE,poolSchedulerThreadFactory);

        int coreSize = currentAvailableProcessors;
        PoolExecutorThreadFactory poolExecutorThreadFactory =
                new PoolExecutorThreadFactory(createThreadName(SYSTEM_EXECUTOR,EXECUTOR));
        globalExecutor = new ThreadPoolExecutor(coreSize,coreSize,KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),poolExecutorThreadFactory);
    }



    public ExecutorService registerExecutor(String name, int defaultPoolSize) {
        ThreadFactory threadFactory = new PoolExecutorThreadFactory(name);
        ExecutorService executor = new ThreadPoolExecutor(defaultPoolSize, defaultPoolSize, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), threadFactory);
        executors.putIfAbsent(name, executor);
        return executor;
    }

    public ScheduledThreadPoolExecutor registerScheduled(String name, int defaultPoolSize) {
        ThreadFactory threadFactory = new PoolExecutorThreadFactory(name);
        ScheduledThreadPoolExecutor scheduler =
                new ScheduledThreadPoolExecutor(defaultPoolSize,threadFactory);
        schedulers.putIfAbsent(name,scheduler);
        return scheduler;
    }


    public void execute(String name, Runnable command) {
        getExecutor(name).execute(command);
    }


    public Future<?> submit(String name, Runnable task) {
        return getExecutor(name).submit(task);
    }

    public <T> Future<T> submit(String name, Callable<T> task) {
        return getExecutor(name).submit(task);
    }



    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return globalTaskScheduler.schedule(command, delay, unit);
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> task, long delay, TimeUnit unit) {
        return globalTaskScheduler.schedule(task, delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                                  long initialDelay,
                                                  long period,
                                                  TimeUnit unit) {
        return globalTaskScheduler.scheduleAtFixedRate(command,initialDelay,period,unit);
    }


    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                     long initialDelay,
                                                     long delay,
                                                     TimeUnit unit) {
        return globalTaskScheduler.scheduleWithFixedDelay(command,initialDelay,delay,unit);
    }


    public ScheduledFuture<?> schedule(String name, Runnable command, long delay, TimeUnit unit) {
        return getScheduled(name).schedule(command, delay, unit);
    }


    public <V> ScheduledFuture<V> schedule(String name, Callable<V> task, long delay, TimeUnit unit) {
        return getScheduled(name).schedule(task, delay, unit);
    }


    public ScheduledFuture<?> scheduleAtFixedRate(String name,Runnable command,
                                                  long initialDelay,
                                                  long period,
                                                  TimeUnit unit) {
        return getScheduled(name).scheduleAtFixedRate(command,initialDelay,period,unit);
    }


    public ScheduledFuture<?> scheduleWithFixedDelay(String name,Runnable command,
                                                     long initialDelay,
                                                     long delay,
                                                     TimeUnit unit) {
        return getScheduled(name).scheduleWithFixedDelay(command,initialDelay,delay,unit);
    }



    public ExecutorService getExecutor(String name) {
        ExecutorService executorService = executors.get(name);
        if (executorService == null)
            throw new NullPointerException(name + "executor not registered");
        return executorService;
    }


    public ScheduledThreadPoolExecutor getScheduled(String name) {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = schedulers.get(name);
        if (scheduledThreadPoolExecutor == null)
            throw new NullPointerException(name + "Scheduler not registered");
        return scheduledThreadPoolExecutor;
    }



    public void shutdown() {

        for (ExecutorService executorService : executors.values()) {
            executorService.shutdown();
        }

        for (ExecutorService executorService : schedulers.values()) {
            executorService.shutdown();
        }


        globalExecutor.shutdownNow();
        globalTaskScheduler.shutdownNow();

        try {
            globalExecutor.awaitTermination(AWAIT_TIME, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
        try {
            globalTaskScheduler.awaitTermination(AWAIT_TIME, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }

        executors.clear();
        schedulers.clear();
    }


    public void shutdownExecutor(String name) {
        ExecutorService executorService = executors.remove(name);
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    public void shutdownScheduler(String name) {
        ExecutorService executorService = schedulers.remove(name);
        if (executorService != null) {
            executorService.shutdown();
        }
    }








    public int getCurrentAvailableProcessors() {
        return currentAvailableProcessors;
    }



    private static String createThreadName(String taskName, String poolName) {
        checkNotNull(taskName, "name can't be null");
        return taskName + "." + poolName + ".thread-";
    }



}
