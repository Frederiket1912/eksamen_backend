/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import dtos.ChuckDTO;
import dtos.DadDTO;
import dtos.SkyscannerDTO;

/**
 *
 * @author Frederik
 */
public class ApiConstructor {
    private String url;
    private String host;
    private String apikey;
    private DadDTO dadDTO;
    private ChuckDTO chuckDTO;
    private SkyscannerDTO skyscannerDTO;
    
    private static int iterate;
    

    public ApiConstructor() {
    }

    public int getIterate() {
        return iterate;
    }

    public void setIterate(int iterate) {
        this.iterate = iterate;
    }
    
    

    public ApiConstructor(String url, String host, String apikey) {
        this.url = url;
        this.host = host;
        this.apikey = apikey;
    }

    public String getUrl() {
        return url;
    }

    public void setDadDTO(DadDTO dadDTO) {
        this.dadDTO = dadDTO;
    }

    public void setChuckDTO(ChuckDTO chuckDTO) {
        this.chuckDTO = chuckDTO;
    }

    public void setSkyscannerDTO(SkyscannerDTO skyscannerDTO) {
        this.skyscannerDTO = skyscannerDTO;
    }

    public DadDTO getDadDTO() {
        return dadDTO;
    }

    public ChuckDTO getChuckDTO() {
        return chuckDTO;
    }

    public SkyscannerDTO getSkyscannerDTO() {
        return skyscannerDTO;
    }
    
    

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    @Override
    public String toString() {
        return "ApiConstructor{" + "url=" + url + ", host=" + host + ", apikey=" + apikey + '}';
    }
    
    
    
    
}
