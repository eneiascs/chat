import java.util.HashMap;
import java.util.Map;

import br.unb.spl.server.authentication.User;
import br.unb.spl.server.connection.AuthenticationLoader;
import br.unb.spl.server.main.HtmlLoader;

public aspect Authentication {
	String around(): execution(String AuthenticationLoader.getDivAuthentication()){
		
		return HtmlLoader.getHtml("authentication.html");
		
	}
String around(): execution(String AuthenticationLoader.registerUser(User)){
	
	User user =(User) thisJoinPoint.getArgs()[0];
	UserService userService = UserServiceImpl.getInstance();
	return userService.registerUser(user);
	
		
	}
String around(): execution(* AuthenticationLoader.authenticateUser(*)){
	User user =(User) thisJoinPoint.getArgs()[0];
	UserService userService = UserServiceImpl.getInstance();
	return userService.authenticateUser(user);

		
	}
	
	public interface UserService {
		public String registerUser(User user);

		public String authenticateUser(User user);
	}
	
	public static class UserServiceImpl implements UserService {
		private Map<String, User> users = new HashMap<String, User>();
		private static final UserServiceImpl instance=new UserServiceImpl();
		public String registerUser(User user) {
			if (users.containsKey(user.getUsername())) {
				return "This username is already registered";

			} else {
				users.put(user.getUsername(), user);
				return "OK";
			}
		}

		public static UserService getInstance() {
			
			return instance;
		}

		public String authenticateUser(User user) {
			User us = users.get(user.getUsername());
			if (us != null && us.getPassword().equals(user.getPassword())) {
				return "OK";
			} else {
				return "Username or password incorrect";
			}
		}

	}
	
}