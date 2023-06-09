package com.mc.mvc.module.member;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mc.mvc.infra.code.ErrorCode;
import com.mc.mvc.infra.exception.HandlableException;
import com.mc.mvc.module.member.dto.Principal;
import com.mc.mvc.module.member.dto.request.LoginRequest;
import com.mc.mvc.module.member.dto.request.SignUpRequest;
import com.mc.mvc.module.member.validator.SignUpValidator;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {
	
	private final SignUpValidator signUpValidator;
	private final MemberService memberService;
	
	@InitBinder("signUpRequest")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(signUpValidator);
	}
	
	@GetMapping("signup")
	public void signUp(Model model) {
		model.addAttribute("signUpRequest", new SignUpRequest());
	}
	
	@GetMapping("checkId")
	@ResponseBody
	public Map<String,Boolean> checkId(String userId) {
		return Map.of("exist", memberService.existUser(userId));
	}
	
	@PostMapping("signup")
	public String authentocateEmail(@Valid SignUpRequest form
								, Errors error
								, Model model
								, HttpSession session) {
		
		if(error.hasErrors()) {
			return "/member/signup";
		}
		
		session.setAttribute("signupForm", form);
		
		String authToken = UUID.randomUUID().toString();
		session.setAttribute("authToken", authToken);
		
		memberService.authenticateEmail(form, authToken);
		return "redirect:/";
	}
	
	@GetMapping("signupimpl/{authToken}")
	public String signUpImpl(
				HttpSession session,
				@PathVariable String authToken,
				@SessionAttribute(name = "authToken", required = false) String sessionToken,
				@SessionAttribute(name="signupForm", required = false) SignUpRequest form,
				Model model
			) {
			
		if(!authToken.equals(sessionToken)) throw new HandlableException(ErrorCode.EXPRIATION_SIGNUP_TOKEN);
		
		memberService.registNewMember(form);
		
		session.removeAttribute("authToken");
		session.removeAttribute("signupForm");
		
		return "redirect:/member/login";
	}
	
	@GetMapping("login")
	public void login(Model model) {
		model.addAttribute("loginRequest", new LoginRequest());
	};
	
	@PostMapping("login")
	public String loginImpl(@Valid LoginRequest loginRequest
							, Errors error
							, HttpSession session
							, RedirectAttributes redirectAttributes ) {
		
		if(error.hasErrors()) {
			return "/member/login";
		}
		
		Principal principal = memberService.authenticateUser(loginRequest);
		
		if(principal == null) {
			redirectAttributes.addFlashAttribute("msg", "아이디나 비밀번호가 일치하지 않습니다.");
			return "redirect:/member/login";
		}
		
		session.setAttribute("auth", principal);
		return "redirect:/";
	}
	
	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.removeAttribute("auth");
		return "redirect:/";
	}
	
	@GetMapping("mypage")
	public String myPage() {
		return "/member/mypage";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
