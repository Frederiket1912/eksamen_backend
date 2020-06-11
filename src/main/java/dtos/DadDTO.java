package dtos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import utils.HttpUtils;

public class DadDTO implements DTOInterface{
    private String id;
    private String joke;
    private String url;
    private boolean failed = false;

    public boolean isFailed() {
        return failed;
    }

    public DadDTO(String id, String joke) {
        this.id = id;
        this.joke = joke;
    }

    public DadDTO(String url) {
        this.url = url;
    }
    
    public String getId() {
        return id;
    }

    public String getJoke() {
        return joke;
    }

    @Override
    public void fetch(){
        try {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String dad = HttpUtils.fetchData(this.url, "", "");
        DadDTO dadDTO = gson.fromJson(dad, DadDTO.class);
        this.id = dadDTO.getId();
        this.joke = dadDTO.getJoke();
        }catch(IOException ex) {
            this.failed=true;
        }
    }

    @Override
    public String toString() {
        return "DadDTO{" + "id=" + id + ", joke=" + joke + ", url=" + url + '}';
    }
    
    
    
}
