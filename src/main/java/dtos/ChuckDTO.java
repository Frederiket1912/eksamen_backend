package dtos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import utils.HttpUtils;

public class ChuckDTO implements DTOInterface {

    private String id;
    private String value;
    private String url;
    private boolean failed = false;

    public ChuckDTO(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public ChuckDTO(String url) {
        this.url = url;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setChuck(ChuckDTO chuck) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void fetch() {
        try {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String chuck = HttpUtils.fetchData(this.url, "", "");
        ChuckDTO chuckDTO = gson.fromJson(chuck, ChuckDTO.class);
        this.id = chuckDTO.getId();
        this.value = chuckDTO.getValue();
        }catch(IOException ex){
            this.failed = true;
        }
    }

    @Override
    public String toString() {
        return "ChuckDTO{" + "id=" + id + ", value=" + value + ", url=" + url + '}';
    }

}
