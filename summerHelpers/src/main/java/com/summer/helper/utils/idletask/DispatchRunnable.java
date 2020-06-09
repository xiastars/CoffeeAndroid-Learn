package com.summer.helper.utils.idletask;

import android.support.v4.os.TraceCompat;
import android.os.Process;

/**
 * 任务真正执行的地方
 */

public class DispatchRunnable implements Runnable {
    private Task mTask;
    private TaskDispatcher mTaskDispatcher;

    public DispatchRunnable(Task task) {
        this.mTask = task;
    }
    public DispatchRunnable(Task task,TaskDispatcher dispatcher) {
        this.mTask = task;
        this.mTaskDispatcher = dispatcher;
    }

    @Override
    public void run() {
        TraceCompat.beginSection(mTask.getClass().getSimpleName());
        Process.setThreadPriority(mTask.priority());

        mTask.setWaiting(true);
        mTask.waitToSatisfy();

        // 执行Task
        mTask.setRunning(true);
        mTask.run();

        // 执行Task的尾部任务
        Runnable tailRunnable = mTask.getTailRunnable();
        if (tailRunnable != null) {
            tailRunnable.run();
        }

        if (!mTask.needCall() || !mTask.runOnMainThread()) {
            mTask.setFinished(true);
            if(mTaskDispatcher != null){
                mTaskDispatcher.satisfyChildren(mTask);
                mTaskDispatcher.markTaskDone(mTask);
            }
        }
        TraceCompat.endSection();
    }
}