package sample.equi.com.equinox.Common;

/**
 * Created by DravitLochan on 15-04-2018.
 */

public class HttpResponse {

    private final String results;
    private final String info;

    public HttpResponse(String results, String info) {
        this.results = results;
        this.info = info;
    }

    public String getResults() {
        return results;
    }

    public String getInfo() {
        return info;
    }
}
