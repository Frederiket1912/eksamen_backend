package dtos;


public class CombinedDTO {
    private String dadJoke;
    private String dadJokeURL;
    private String chuckJoke;
    private String chuckJokeURL;
    private String dogDTOMessage;
    private String weatherTimezone;
    private String weatherURL;
    private SkyscannerDTO scanner;
    
    public CombinedDTO(DadDTO dadDTO, ChuckDTO chuckDTO, DogImgDTO diDTO, WeatherDTO weatherDTO, SkyscannerDTO scannerDTO){
        //hvis failed er true skriver vi "fetch failed" i stedet for joken som ville være null
        this.dadJoke = dadDTO.isFailed() ? "fetch failed" : dadDTO.getJoke();    
        this.dadJokeURL = "https://icanhazdadjoke.com";
        this.chuckJoke = chuckDTO.isFailed() ? "fetch failed" : chuckDTO.getValue();
        this.chuckJokeURL = "https://api.chucknorris.io/jokes/random";
        this.dogDTOMessage = diDTO.isFailed() ? "fetch failed" : diDTO.getMessage();
        this.weatherTimezone = weatherDTO.isFailed() ? "fetch failed" : weatherDTO.getData().get(0).getTimezone();
        this.weatherURL = "https://api.weatherbit.io/v2.0/current?city=Copenhagen,DK&key=de4ff00ad5a24948967c5a21d3892aea";
        this.scanner = scannerDTO;
    }

    @Override
    public String toString() {
        return "CombinedDTO{" + "dadJoke=" + dadJoke + ", dadJokeURL=" + dadJokeURL + ", chuckJoke=" + chuckJoke + ", chuckJokeURL=" + chuckJokeURL + ", dogDTOMessage=" + dogDTOMessage + ", weatherTimezone=" + weatherTimezone + ", weatherURL=" + weatherURL + ", scanner=" + scanner + '}';
    }

    public String getDadJoke() {
        return dadJoke;
    }

    public String getDadJokeURL() {
        return dadJokeURL;
    }

    public String getChuckJoke() {
        return chuckJoke;
    }

    public String getChuckJokeURL() {
        return chuckJokeURL;
    }

    public String getDogDTOMessage() {
        return dogDTOMessage;
    }

    public String getWeatherTimezone() {
        return weatherTimezone;
    }

    public String getWeatherURL() {
        return weatherURL;
    }

    public SkyscannerDTO getScanner() {
        return scanner;
    }
    
    
}
