package manageryzy.simulator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;


public class SimulatorServer extends Simulator {
    Socket client;

    public SimulatorServer(String path) throws IOException {
        super(path);
        client = new Socket("127.0.0.1",8889);
    }

    @Override
    public void onUpdate(ArrayList<Record> results) throws Exception {
        if(client.isClosed()){
            client = new Socket("127.0.0.1",8889);
        }

        OutputStream out = client.getOutputStream();

        JSONArray json = new JSONArray();
        int size = results.size();
        for(int i=0;i<size;i++) {
            Record record = results.get(i);

            JSONObject object = new JSONObject();
            object.put("c", record.car_id);
            object.put("x", record.x);
            object.put("y", record.y);
            object.put("t", record.t);

            json.put(object);

            if (i % 100 == 0 || i == size - 1) {
                String jsonStr = json.toString();
                json = new JSONArray();
                if(jsonStr.isEmpty())
                {
                    jsonStr = "[]";
                }
                out.write(Base64.getEncoder().encode(jsonStr.getBytes("UTF-8")));
                out.write('\n');
            }
        }


    }
}
