//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package vip.ylove.sdk.util;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StStopWatch {

    private static  final Logger log = LoggerFactory.getLogger(StStopWatch.class);

    private final String id;
    private List<StStopWatch.TaskInfo> taskList;
    private String currentTaskName;
    private long startTimeNanos;
    private StStopWatch.TaskInfo lastTaskInfo;
    private int taskCount;
    private long totalTimeNanos;

    public static StStopWatch create(String id) {
        return new StStopWatch(id);
    }

    public StStopWatch() {
        this("");
    }

    public StStopWatch(String id) {
        this(id, true);
    }

    public StStopWatch(String id, boolean keepTaskList) {
        this.id = id;
        if (keepTaskList) {
            this.taskList = new ArrayList();
        }

    }

    public String getId() {
        return this.id;
    }

    public void setKeepTaskList(boolean keepTaskList) {
        if (keepTaskList) {
            if (null == this.taskList) {
                this.taskList = new ArrayList();
            }
        } else {
            this.taskList = null;
        }

    }

    public void start()  {
        this.start("");
    }

    public void start(String taskName) {
        if (null != this.currentTaskName) {
            log.error("Can't start StopWatch: it's already running");
        } else {
            this.currentTaskName = taskName;
            this.startTimeNanos = System.nanoTime();
        }
    }

    public void stop()  {
        if (null == this.currentTaskName) {
            log.error("Can't stop StopWatch: it's not running");
        } else {
            long lastTime = System.nanoTime() - this.startTimeNanos;
            this.totalTimeNanos += lastTime;
            this.lastTaskInfo = new StStopWatch.TaskInfo(this.currentTaskName, lastTime);
            if (null != this.taskList) {
                this.taskList.add(this.lastTaskInfo);
            }

            ++this.taskCount;
            this.currentTaskName = null;
        }
    }

    public boolean isRunning() {
        return this.currentTaskName != null;
    }

    public String currentTaskName() {
        return this.currentTaskName;
    }

    public long getLastTaskTimeNanos()  {
        if (this.lastTaskInfo == null) {
            log.error("No tasks run: can't get last task interval");
            return 0;
        } else {
            return this.lastTaskInfo.getTimeNanos();
        }
    }

    public long getLastTaskTimeMillis()  {
        if (this.lastTaskInfo == null) {
            log.error("No tasks run: can't get last task interval");
            return 0;
        } else {
            return this.lastTaskInfo.getTimeMillis();
        }
    }

    public String getLastTaskName()  {
        if (this.lastTaskInfo == null) {
            log.error("No tasks run: can't get last task name");
            return null;
        } else {
            return this.lastTaskInfo.getTaskName();
        }
    }

    public StStopWatch.TaskInfo getLastTaskInfo()  {
        if (this.lastTaskInfo == null) {
            log.error("No tasks run: can't get last task info");
            return null;
        } else {
            return this.lastTaskInfo;
        }
    }

    public long getTotal(TimeUnit unit) {
        return unit.convert(this.totalTimeNanos, TimeUnit.NANOSECONDS);
    }

    public long getTotalTimeNanos() {
        return this.totalTimeNanos;
    }

    public long getTotalTimeMillis() {
        return this.getTotal(TimeUnit.MILLISECONDS);
    }

    public double getTotalTimeSeconds() {
        return DateUtil.nanosToSeconds(this.totalTimeNanos ) ;
    }

    public int getTaskCount() {
        return this.taskCount;
    }

    public StStopWatch.TaskInfo[] getTaskInfo() {
        if (null == this.taskList) {
            log.error("Task info is not being kept!");
            return null;
        } else {
            return (StStopWatch.TaskInfo[])this.taskList.toArray(new StStopWatch.TaskInfo[0]);
        }
    }

    public String shortSummary() {
        return this.shortSummary((TimeUnit)null);
    }

    public String shortSummary(TimeUnit unit) {
        if (null == unit) {
            unit = TimeUnit.NANOSECONDS;
        }

        return StrUtil.format("StopWatch '{}': running time = {} {}", new Object[]{this.id, this.getTotal(unit), DateUtil.getShotName(unit)});
    }

    public String prettyPrint() {
        return this.prettyPrint((TimeUnit)null);
    }

    public String prettyPrint(TimeUnit unit) {
        if (null == unit) {
            unit = TimeUnit.NANOSECONDS;
        }

        StringBuilder sb = new StringBuilder(this.shortSummary(unit));
        sb.append(FileUtil.getLineSeparator());
        if (null == this.taskList) {
            sb.append("No task info kept");
        } else {
            sb.append("---------------------------------------------").append(FileUtil.getLineSeparator());
            sb.append(DateUtil.getShotName(unit)).append("         %     Task name").append(FileUtil.getLineSeparator());
            sb.append("---------------------------------------------").append(FileUtil.getLineSeparator());
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumIntegerDigits(9);
            nf.setGroupingUsed(false);
            NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(2);
            pf.setGroupingUsed(false);
            StStopWatch.TaskInfo[] var5 = this.getTaskInfo();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                StStopWatch.TaskInfo task = var5[var7];
                sb.append(nf.format(task.getTime(unit))).append("  ");
                sb.append(pf.format((double)task.getTimeNanos() / (double)this.getTotalTimeNanos())).append("   ");
                sb.append(task.getTaskName()).append(FileUtil.getLineSeparator());
            }
        }

        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.shortSummary());
        if (null != this.taskList) {
            Iterator var2 = this.taskList.iterator();

            while(var2.hasNext()) {
                StStopWatch.TaskInfo task = (StStopWatch.TaskInfo)var2.next();
                sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeNanos()).append(" ns");
                long percent = Math.round(100.0D * (double)task.getTimeNanos() / (double)this.getTotalTimeNanos());
                sb.append(" = ").append(percent).append("%");
            }
        } else {
            sb.append("; no task info kept");
        }

        return sb.toString();
    }

    public static final class TaskInfo {
        private final String taskName;
        private final long timeNanos;

        TaskInfo(String taskName, long timeNanos) {
            this.taskName = taskName;
            this.timeNanos = timeNanos;
        }

        public String getTaskName() {
            return this.taskName;
        }

        public long getTime(TimeUnit unit) {
            return unit.convert(this.timeNanos, TimeUnit.NANOSECONDS);
        }

        public long getTimeNanos() {
            return this.timeNanos;
        }

        public long getTimeMillis() {
            return this.getTime(TimeUnit.MILLISECONDS);
        }

        public double getTimeSeconds() {
            return DateUtil.nanosToSeconds(this.timeNanos);
        }
    }
    
}
