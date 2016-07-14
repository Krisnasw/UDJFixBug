package intivestudio.web.id.udjseamolec;

/**
 * Created by outattacker on 28/04/16.
 */
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

public class Loading extends AsyncTask<String, Integer, Integer> {
    public interface LoadingTaskFinishedListener {
        void onTaskFinished();
    }

    private final ProgressBar progressBar;
    private final LoadingTaskFinishedListener finishedListener;

    public Loading(ProgressBar progressBar,
                   LoadingTaskFinishedListener finishedListener) {
        this.progressBar = progressBar;
        this.finishedListener = finishedListener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        if (resourcesDontAlreadyExist()) {
            downloadResources();
        }
        return 1234;

    }

    private boolean resourcesDontAlreadyExist() {

        return true;
    }

    private void downloadResources() {

        int count = 10;
        for (int i = 0; i < count; i++) {

            int progress = (int) ((i / (float) count) * 100);
            publishProgress(progress);

            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        finishedListener.onTaskFinished();
    }

}
