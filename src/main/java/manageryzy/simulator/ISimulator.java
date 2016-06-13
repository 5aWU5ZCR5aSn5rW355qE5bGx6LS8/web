package manageryzy.simulator;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public interface ISimulator {
    void onUpdate(ArrayList<Record> results) throws IOException, JSONException, Exception;
}
