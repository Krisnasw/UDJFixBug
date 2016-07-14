package intivestudio.web.id.udjseamolec;

/**
 * Created by Krisnasw on 5/16/2016.
 */
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    public static void TRACE(String TAG, String message)
    {
        if (BuildConfig.DEBUG)
        {
            Log.d(TAG, message);
        }
    }

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}
