package dtos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import utils.HttpUtils;

public class SkyscannerDTO implements DTOInterface{
    private List<items> Places;
    private boolean failed;

    public boolean isFailed() {
        return failed;
    }

    public List<items> getPlaces() {
        return Places;
    }
    
    public SkyscannerDTO() {
    }

    public SkyscannerDTO(List<items> Places) {
        this.Places = Places;
    }

    @Override
    public String toString() {
        return "SkyscannerDTO{" + "Places=" + Places + '}';
    }
    
        @Override
    public void fetch(){
        try{
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String skyscanner = HttpUtils.fetchData("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/UK/GBP/en-GB/?query=Stockholm", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com", "e0d467d76bmsh9ff99e16d60d6c1p11ec83jsn67c7df490a96");
        SkyscannerDTO skyscannerDTO = gson.fromJson(skyscanner, SkyscannerDTO.class);
        Places = skyscannerDTO.Places;
        }catch(IOException ex){
            this.failed=true;
        }
    }
        
}