package com.summer.helper.utils.idletask;

import android.os.Looper;
import android.os.MessageQueue;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author: lybj
 * @date: 2020/5/26
 * @Description: 空闲队列
 */
public class IdleTaskDispatcher {

    private Queue<Task> mIldeQueue = new LinkedList<>();

    private MessageQueue.IdleHandler idleHandler  = () -> {

        if(mIldeQueue.size() > 0){

            // 如果CPU空闲了，
            Task IldeTask = mIldeQueue.poll();
            new DispatchRunnable(IldeTask).run();
        }
        // 如果返回false，则移除该 IldeHandler
        return !mIldeQueue.isEmpty();
    };

    public IdleTaskDispatcher addTask(Task task){

        mIldeQueue.add(task);
        return this;
    }
    
    
    /**
     * 执行空闲方法，因为用了DispatchRunnable，所以会优先处理需要依赖的task，再处理本次需要处理的task，顺序执行
     * */
    public void start(){
        Looper.myQueue().addIdleHandler(idleHandler);
    }
}
