/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.util.List;

/**
 *
 * @author Frederik
 */
public class items {

    private String PlaceId;
    private String PlaceName;
    private String CountryId;
    private String RegionId;
    private String CityId;
    private String CountryName;

    public items() {
    }

    public items(String PlaceId, String PlaceName, String CountryId, String RegionId, String CityId, String CountryName) {
        this.PlaceId = PlaceId;
        this.PlaceName = PlaceName;
        this.CountryId = CountryId;
        this.RegionId = RegionId;
        this.CityId = CityId;
        this.CountryName = CountryName;
    }

    public String getPlaceId() {
        return PlaceId;
    }

    public void setPlaceId(String PlaceId) {
        this.PlaceId = PlaceId;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String PlaceName) {
        this.PlaceName = PlaceName;
    }

    public String getCountryId() {
        return CountryId;
    }

    public void setCountryId(String CountryId) {
        this.CountryId = CountryId;
    }

    public String getRegionId() {
        return RegionId;
    }

    public void setRegionId(String RegionId) {
        this.RegionId = RegionId;
    }

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String CityId) {
        this.CityId = CityId;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String CountryName) {
        this.CountryName = CountryName;
    }

    @Override
    public String toString() {
        return "items{" + "PlaceId=" + PlaceId + ", PlaceName=" + PlaceName + ", CountryId=" + CountryId + ", RegionId=" + RegionId + ", CityId=" + CityId + ", CountryName=" + CountryName + '}';
    }
    
    
    
    
}

