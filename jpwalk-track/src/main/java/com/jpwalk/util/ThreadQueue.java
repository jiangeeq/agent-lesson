package com.jpwalk.util;

import java.util.ArrayList;


class ThreadQueue implements Runnable {

    private static final String TAG = ThreadQueue.class.getSimpleName();

    //控制最大线程数量的变量
    public static int sMaxThreadCount = 5;
    private static ArrayList<ThreadQueue> sThreadQueueList = new ArrayList<ThreadQueue>();
    /**
     * 申请一个新的线程用来处理任务的。
     * 超过上线的时候就返回为null。
     * 同步的
     * @return
     */
    public synchronized static ThreadQueue requestNewThread() {
        int index = sThreadQueueList.size();
        if (index >= sMaxThreadCount) {
            return null;
        } else {
            ThreadQueue threadQueue = new ThreadQueue();
            sThreadQueueList.add(threadQueue);
            return threadQueue;
        }
    }

    public static void closeThreadQueue() {
        if (sThreadQueueList.size() > 0) {
            for(int i = sThreadQueueList.size(); i > 0; i--) {
                sThreadQueueList.remove(sThreadQueueList.size()-1);
            }
        }
    }

    /**
     * 当一个线程执行完毕后用来删除其在线程链表里的引用
     * 同步的
     * @param flag
     */
    private synchronized static void removeThread(int flag) {
        for (int i = 0; i < sThreadQueueList.size(); i++) {
            if (sThreadQueueList.get(i).getFlag() == flag && flag != 0) {
                sThreadQueueList.remove(i);
                break;
            }
        }
    }

    /**
     * 线程用来获取一个新的任务的方法。
     * 向TaskQueue申请一个新的任务，有可能为null。
     * 同步的
     * @return
     */
    private synchronized static Object getNewTask() {
        return TaskPool.getInstance().requestNewTask();
    }


    private Thread mThread;
    private final int mFlag;
    private ThreadQueue() {
        // TODO Auto-generated constructor stub
        mFlag = (int) (Math.random() * 10000) + 1; // for 0
    }

    public int getFlag() {
        return mFlag;
    }

    private Object mTaskInfo = null;
    /**
     * 申请到一个线程之后，需要执行这一步。
     * 使其执行这个任务
     * @param taskInfo
     */
    public void executeTask(Object taskInfo) {
        mTaskInfo = taskInfo;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        // mTaskInfo.executeBackground();

        while((mTaskInfo = ThreadQueue.getNewTask()) != null) {
           // mTaskInfo.executeBackground();
        }
        //process taskInfo

        ThreadQueue.removeThread(getFlag());

    }

}
