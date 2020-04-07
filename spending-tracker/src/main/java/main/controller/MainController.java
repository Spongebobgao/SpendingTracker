package main.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import main.model.MonthlyTotal;
import main.model.SpendingDetails;
import main.model.User;
import main.repository.MonthlyTotalRepository;
import main.repository.SpendingDetailsRepository;
import main.repository.UserRepository;
import main.service.SpendingDetailsService;
import main.service.UserService;
import main.service.Mail;
import main.service.MonthlyTotalService;

@Controller
public class MainController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SpendingDetailsRepository spendingDetailsRepository;
	
	@Autowired
	private SpendingDetailsService spendingDetailsService;
	
	@Autowired
	private MonthlyTotalRepository monthlyTotalRepository;
	
	@Autowired
	private MonthlyTotalService monthlyTotalService;
	
	@Autowired
	private Mail mail;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public ModelAndView home(Model model,ModelAndView modelAndView) {
		model.addAttribute("name","Lin");
		modelAndView.setViewName("home");
		return modelAndView;
	}
	
	@GetMapping("/register")
	public String regsiter(Model model) {
		model.addAttribute("user",new User());
		return "register";
	}
	
	@PostMapping("/register")
	public RedirectView check(@ModelAttribute User user,RedirectAttributes redirectAttrs) {
		if(userRepository.findByEmail(user.getEmail())!=null){
			redirectAttrs.addFlashAttribute("resiterError", "The user already exits");
			return new RedirectView("/register");
		}else{
			userService.register(user);
			redirectAttrs.addFlashAttribute("registerSuccessed", "You've registered sccessfully! ");			
			return new RedirectView("/login");
		}
	}
	
	@GetMapping("/login")
	public String login(Model model) {	
		model.addAttribute("user",new User());
		return "login";
	}
	
	@PostMapping("/login")
	public RedirectView login(@ModelAttribute User user,RedirectAttributes redirectAttrs,HttpSession session) {
		if(!userService.login(user)) {
			redirectAttrs.addFlashAttribute("loginError","The email or password is invalid!");			
			return new RedirectView("/login");
		}
		user.setUid(userRepository.findByEmail(user.getEmail()).getUid());
		user.setUserName(userRepository.findByEmail(user.getEmail()).getUserName());
		session.setAttribute("user", user);
		
		return  new RedirectView("/info");
	}
	
	@GetMapping("/info")
	public String getSpendingDetails(Model model, HttpSession session) {		
		User user = (User) session.getAttribute("user");
		if(user!=null) {
			List<MonthlyTotal> monthlyTotal=monthlyTotalService.getAllMonthlyTotal(user);
			model.addAttribute("monthlyTotal", monthlyTotal);
			model.addAttribute("spendingDetails", new SpendingDetails());
			List<SpendingDetails> allDetails = spendingDetailsService.getAllDetails(user);
			String totalAmount = spendingDetailsService.monthlyTotal(allDetails);
			model.addAttribute("allDetails",allDetails);
			model.addAttribute("totalAmount", totalAmount);
			return "spendingDetails";
		}else {
			model.addAttribute("user",new User());
			return "login";
		}
	}
	
	@PostMapping("/info")
	public RedirectView addNewSpending(@ModelAttribute SpendingDetails spendingDetails, HttpSession session) {
		spendingDetails.setUser((User)session.getAttribute("user"));
		spendingDetailsRepository.save(spendingDetails);
		return new RedirectView("/info");
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "logout";
	}
	
	@GetMapping("/forgetPass")
	public String forgetPass(@RequestParam(value="email",required=false)String email, Model model) {
        if(email==null) {
        	model.addAttribute("user", new User());
    		return "forgetPass";
        }
        else {
        	User user = new User();
        	user.setEmail(email);
			model.addAttribute("user", user);
			return "forgetPass";
        }
	}
	
	@PostMapping("/forgetPass")
	public RedirectView forgetPass(@ModelAttribute User user, RedirectAttributes redirectAttrs ) {
		if(user.getCode()==0) {
			redirectAttrs.addAttribute("email", user.getEmail());
			if(userRepository.findByEmail(user.getEmail())==null) {
				redirectAttrs.addFlashAttribute("invaildEmail", "Please enter the correct email address.");
				return new RedirectView("forgetPass");
			}
			redirectAttrs.addFlashAttribute("emailedMessage","We've sent you an email, please check.");
			userService.setCode(user.getEmail(),mail.sendMail(user.getEmail()));
			return new RedirectView("forgetPass");
		}		
		else if(userRepository.findByEmail(user.getEmail()).getCode()==user.getCode()) {
			redirectAttrs.addFlashAttribute("codeCorrect","The code is correct, you can reset the password now.");
			redirectAttrs.addAttribute("email",user.getEmail());		
			return new RedirectView("resetPass");
		}
		else{
			redirectAttrs.addFlashAttribute("codeErrorMessage","The code is invaild, please try again.");
			//set this to let the code box show up
			redirectAttrs.addFlashAttribute("emailedMessage","");
			redirectAttrs.addAttribute("email",user.getEmail());
			return new RedirectView("forgetPass");
		}
	}
	
	@GetMapping("/resetPass")
	public String resetPass(@PathParam("email")String email, Model model ) {
			User user = new User();
			user.setEmail(email);
			model.addAttribute("user",user);
			return "resetPass";	
	}
	
	@PostMapping("/resetPass")
	public RedirectView resetPass(@ModelAttribute User user,RedirectAttributes redirectAttrs) {
		userService.resetPass(user.getEmail(),user.getPassword());
		redirectAttrs.addFlashAttribute("resetPassDone", "You've reset your password successfully");
		return new RedirectView("login");
	}
}
