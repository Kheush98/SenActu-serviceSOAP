package esp.sn.webservicesoap;


import esp.sn.webservicesoap.services.UserService;
import jakarta.xml.ws.Endpoint;

public class WebServiceSoapApplication {

    private static final String url = "http://0.0.0.0:8080/ws/user";
    public static void main(String[] args) {
        Endpoint.publish(url, new UserService());
        System.out.println("Serveur déployé sur " + url);
    }

}
