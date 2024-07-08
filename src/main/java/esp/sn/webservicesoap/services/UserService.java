package esp.sn.webservicesoap.services;

import esp.sn.webservicesoap.model.User;
import esp.sn.webservicesoap.repository.UserRepository;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.ArrayList;
import java.util.List;

@WebService(serviceName = "UserWS")
public class UserService {

    private static final List<User> users;
    static UserRepository userRepository = new UserRepository();

    static {
        users = userRepository.getUsers();
    }

    @WebMethod
    public String addUser(@WebParam(name = "username") String username, @WebParam(name = "password") String password, @WebParam(name = "role") String role) {
        long id = userRepository.addUser(username, password, role);
        for (User user : users) {
            if (user.getUsername().equals(username))
                return "L'utilisateur existe deja";
        }
        users.add(new User(id, username, password, role));
        return "Utilisateur ajouté";
    }

    @WebMethod
    public String updateUser(@WebParam(name = "username") String username, @WebParam(name = "password") String password, @WebParam(name = "role") String role) {
        userRepository.updateUser(username, password, role);
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.setPassword(password);
                user.setRole(role);
                return "Utilisateur modifié";
            }
        }

        return "L'utilisateur n'existe pas";
    }

    @WebMethod
    public List<User> getUsers() {
        return users;
    }

    @WebMethod
    public String deleteUser(@WebParam(name = "username") String username) {
        userRepository.deleteUser(username);
        for (User user : users) {
            if (user.getUsername().equals(username)){
                users.remove(user);
                return "Utilisateur supprimé";
            }
        }

        return "L'utilisateur n'existe pas";
    }

    @WebMethod
    public void login(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {}
}
