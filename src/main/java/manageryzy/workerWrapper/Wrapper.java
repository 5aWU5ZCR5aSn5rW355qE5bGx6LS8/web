package manageryzy.workerWrapper;

import org.json.JSONException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Wrapper {
    static Wrapper wrapper;

    public String select(String car) throws IOException, JSONException {
        ArrayList<Record> result = new ArrayList<>();

        if (car == null) {
            return "[]";
        }

        if (car.isEmpty() || car.contains("'`!@#$%^&*()_+-={}|[]\\;':\",./<>?") || car.length() > 9) {
            return "[]";
        }

        Socket socket = new Socket("127.0.0.1", 8900);

        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();

        String command = "select " + car + "\n";
        os.write(command.getBytes());

        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = bufferedReader.readLine();
//        if (line != null && !line.equals("null")) {
//            JSONArray jsonArray = new JSONArray(line);
//
//            int len = jsonArray.length();
//            for (int i = 0; i < len; i++) {
//                JSONObject object = jsonArray.getJSONObject(i);
//
//                int t = object.getInt("t");
//                int x = object.getInt("x");
//                int y = object.getInt("y");
//
//                result.add(new Record(car, x, y, t));
//            }
//        }

        bufferedReader.close();
        is.close();
        os.close();
        socket.close();

        return line;
    }

    public String check() throws IOException, JSONException {
        ArrayList<ResultCheck> result = new ArrayList<>();

        Socket socket = new Socket("127.0.0.1", 8900);

        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();

        String command = "check\n";
        os.write(command.getBytes());

        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = bufferedReader.readLine();

        bufferedReader.close();
        is.close();
        os.close();
        socket.close();

        return line;
    }

    public static Wrapper getWrapper() {
        return wrapper;
    }

    public static void setWrapper(Wrapper wrapper) {
        Wrapper.wrapper = wrapper;
    }
}
