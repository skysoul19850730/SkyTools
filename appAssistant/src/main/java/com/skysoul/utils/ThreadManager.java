package com.skysoul.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 */
public class ThreadManager {
    ThreadPoolExecutor mExecutor;
    //核心线程
    final int THREAD_CORE_SIZE = 5;
    //最大线程
    final int THREAD_MAX_SIZE = 10;
    //保持时间  分钟
    final int THREAD_KEEP_TIME = 2;
   private static ThreadManager manager;
    Handler mHandler;

    private ThreadManager() {
        mExecutor = new ThreadPoolExecutor(THREAD_MAX_SIZE, THREAD_MAX_SIZE, THREAD_KEEP_TIME, TimeUnit.MINUTES, new LinkedBlockingDeque<>());
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static ThreadManager getManager() {
        if (manager == null) {
            manager = new ThreadManager();
        }
        return manager;
    }

    //不需要取消任务的
    public void execute(Runnable task) {
        try {
            mExecutor.execute(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //需要取消任务的情况下需要用这个
    public Future submit(Runnable task) {
        return mExecutor.submit(task);
    }

    //需要取消任务的情况下需要用这个
    public <T> Future<T> submit(Callable<T> runnable) {
        return mExecutor.submit(runnable);
    }

    //移除任务
    public void remove(Runnable task) {
        mExecutor.remove(task);
    }

    public void runOnUIThread(Runnable runnable) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            runnable.run();
        } else {
            mHandler.post(runnable);
        }
    }

    public void postDelayedOnMainThread(Runnable runnable, long delayed) {
        mHandler.postDelayed(runnable, delayed);
    }

    //不需要取消任务的
    private Executor singleThreadExecutor = Executors.newSingleThreadExecutor();

    //需要在同一个线程执行的一些任务，
    public void executeBySingleThread(Runnable task) {
        try {
            singleThreadExecutor.execute(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
