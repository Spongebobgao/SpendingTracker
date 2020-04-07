package main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import lombok.Data;
import main.model.User;
import main.repository.UserRepository;

@Service
@Data
public class UserService {
	@Autowired
	UserRepository userRepository;
	private int code;
	
	
	public void register(User user) {
		String pw_hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(pw_hash);
		userRepository.save(user);
	}
	
	public boolean login(User user) {
		if(user==null||userRepository.findByEmail(user.getEmail())==null
					 ||!BCrypt.checkpw(user.getPassword(), userRepository.findByEmail(user.getEmail()).getPassword())) {
			return false;
		}
		return true;
	}

	public int setCode(String email, int code) {
		User user = userRepository.findByEmail(email);
		user.setCode(code);
		userRepository.save(user);
		return code;
	}
	
	public void resetPass(String email, String pass) {
		User user = userRepository.findByEmail(email);
		String pw_hash = BCrypt.hashpw(pass, BCrypt.gensalt());
		user.setPassword(pw_hash);
		userRepository.save(user);
	}
		
}
