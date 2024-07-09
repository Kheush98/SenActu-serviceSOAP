package esp.sn.webservicesoap.services;

import esp.sn.webservicesoap.model.User;
import esp.sn.webservicesoap.repository.UserRepository;
import esp.sn.webservicesoap.util.JwtUtil;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@WebService(serviceName = "UserWS")
public class UserService {

    private static final List<User> users;
    static UserRepository userRepository = new UserRepository();

    static {
        users = userRepository.getUsers();
    }

    @WebMethod
    public String addUser(@WebParam(name = "username") String username, @WebParam(name = "password") String password, @WebParam(name = "role") String role, @WebParam(name = "token") String token) {
        if (!isTokenValid(token)) {
            return "Invalid token";
        }
        long id = userRepository.addUser(username, password, role);
        for (User user : users) {
            if (user.getUsername().equals(username))
                return "L'utilisateur existe deja";
        }
        users.add(new User(id, username, password, role));
        return "Utilisateur ajouté";
    }

    @WebMethod
    public String updateUser(@WebParam(name = "username") String username, @WebParam(name = "password") String password, @WebParam(name = "role") String role, @WebParam(name = "token") String token) {
        if (!isTokenValid(token)) {
            return "Invalid token";
        }
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
    public List<User> getUsers(@WebParam(name = "token") String token) {
        if (!isTokenValid(token)) {
            return null;
        }
        return users;
    }

    @WebMethod
    public String deleteUser(@WebParam(name = "username") String username, @WebParam(name = "token") String token) {
        if (!isTokenValid(token)) {
            return "Invalid token";
        }
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
    public String login(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return JwtUtil.generateToken(username);
            }
        }
        return "Login ou mot de passe incorrect";
    }

    @WebMethod
    public String getAdminToken(@WebParam(name = "adminUsername") String adminUsername, @WebParam(name = "adminPassword") String adminPassword) {
        for (User user : users) {
            if (user.getUsername().equals(adminUsername) && user.getPassword().equals(adminPassword)){
                if (!user.getRole().equals("admin"))
                    return "Access refusé";
                return JwtUtil.generateToken(adminUsername);
            }
        }

        return "Nom d'utilisateur ou mot de passe incorrect";
    }

    private boolean isTokenValid(String token) {
        try {
            JwtUtil.validateToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
