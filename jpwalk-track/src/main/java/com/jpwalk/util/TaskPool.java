package com.jpwalk.util;

import java.util.ArrayList;


/**
 * 管理任务队列，
 * 负责分发任务队列
 * 负责解析数据
 * @author zt
 *
 */
public class TaskPool implements Runnable{

    private static final String TAG = TaskPool.class.getSimpleName();

    private static TaskPool sTaskQueue = null;
    public static TaskPool getInstance() {
        if (sTaskQueue == null) {
            sTaskQueue = new TaskPool();
        }

        return sTaskQueue;
    }

    public static void closeThread() {
        if (sTaskQueue != null) {
            sTaskQueue.exitThread();
            sTaskQueue = null;
        }
        ThreadQueue.closeThreadQueue();
    }

    public static void setMaxThreadCount(int count) {
        ThreadQueue.sMaxThreadCount = count;
    }


    private ArrayList<Object> mQueue = null;

    private Thread mThread;

    private TaskPool() {
        mQueue = new ArrayList<Object>();

        mThread = new Thread(this);

        mThread.start();
    }

    /**
     * 往其中添加一个新的任务
     * 同步的
     * @param taskInfo
     */
    public synchronized void addTask(Object taskInfo) {
        mQueue.add(taskInfo);
    }

    /**
     * 手动删除某一项taskInfo
     * @param taskInfo
     */
    public synchronized void removeTask(Object taskInfo) {
        for (int i = mQueue.size() - 1; i > 0; i--) {
            if (mQueue.get(i) == taskInfo) {
                mQueue.remove(i);
                break;
            }
        }
    }


    /**
     * 向对象申请一个新的任务，有可能返回null结果
     * 申请成功的话就将这个任务从任务链表中删除
     * 同步的
     * @return
     */
    public synchronized Object requestNewTask() {
        int index = mQueue.size();
        if (index > 0) {
            /**按顺序依次执行，先进先出*/
            Object task = mQueue.get(0);
            mQueue.remove(0);
            return task;
        } else {
            return null;
        }
    }

    private boolean mExitFlag = false;
    private void exitThread() {
        mExitFlag = true;
    }

    /**
     * 请不要直接调用这个方法。
     */
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(!mExitFlag) {
            dispatchTask();
            try {
                Thread.sleep(1300);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * 分发任务
     * 先申请一个任务，成功后
     * 申请一个thread，申请成功，执行任务；不成功，把任务重新添加进去。
     */
    private void dispatchTask() {

        boolean isProcessAgain = true;

        while(isProcessAgain) {

            Object task = requestNewTask();
            if (task != null) {
                ThreadQueue thread = ThreadQueue.requestNewThread();
                if (thread != null) {
                    thread.executeTask(task);
                    isProcessAgain = true;
                } else {
                    isProcessAgain = false;
                    addTask(task);
                }
            } else {
                isProcessAgain = false;
            }

        }

    }

}
