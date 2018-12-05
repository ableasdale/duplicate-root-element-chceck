import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

public class UpdateTestXML implements Job {

    //private static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getMergedJobDataMap();
        String query = dataMap.getString("query");
        Session s = MarkLogicContentSourceProvider.getInstance().getContentSource().newSession();
        try {
            s.submitRequest(s.newAdhocQuery(query));
        } catch (RequestException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
    }

}
