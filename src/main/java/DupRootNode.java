import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class DupRootNode {
    private static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void main(String[] args) throws Exception {

        String query = readFile("src/main/resources/update-doc.xqy", StandardCharsets.UTF_8);
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail job = newJob(CountRootNodes.class)
                .withIdentity("job", "group")
                .usingJobData("uri", "/test.xml")
                .build();

        JobDetail job1 = newJob(UpdateTestXML.class)
                .withIdentity("job1", "group1")
                .usingJobData("query", query)
                .build();

        JobDetail job2 = newJob(Summary.class)
                .withIdentity("job2", "group2")
                .usingJobData("counter", CounterProvider.getInstance().getTotalCountSoFar())
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("trigger", "group")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInMilliseconds(50)
                        .repeatForever())
                .build();

        Trigger trigger1 = newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInMilliseconds(20)
                        .repeatForever())
                .build();

        Trigger trigger2 = newTrigger()
                .withIdentity("trigger2", "group2")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInHours(2)
                        .repeatForever())
                .build();

        scheduler.scheduleJob(job, trigger);
        scheduler.scheduleJob(job1, trigger1);
        scheduler.scheduleJob(job2, trigger2);
        scheduler.start();
    }
}
