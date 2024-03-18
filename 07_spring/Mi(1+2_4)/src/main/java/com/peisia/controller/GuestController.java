package com.peisia.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peisia.dto.GuestDto;
import com.peisia.service.GuestService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequestMapping("/guest/*")	//프로젝트 루트 경로 이하 /guest 상위폴더로 진입 시 여기로 진입하게 됨.  
@AllArgsConstructor	//필드 값을 매개변수로 하는 생성자를 스프링이 알아서 만들어 줌. 그리고 그런 형태의 생성자를 추가하면 스프링이 알아서 객체관리 해줌(@Auto.. 처럼)
@Controller
public class GuestController {

//	위에 @AllArgsConstructor 이걸 쓰면
//	롬복라이브러리가 아래 코드를 자동으로 삽입해줌
	//
//	public GuestController(GuestService service){
//		this.service = service;
//	}
	
	private GuestService service;
	
	   // currentPage와 함께 페이지 링크 수를 직접 전달
    @GetMapping("/getList")
    public void getList(@RequestParam(value = "currentPage", defaultValue = "1") int currentPage, Model model) {
    	model = service.getList(currentPage, model);
    	service.noRe();
    	service.noRe2();
//        model.addAttribute("list", service.getList(currentPage)); //[1] 글페이지 내 글 가져오기
//        model.addAttribute("pageCount",service.getTotalCount()); //최대 글 수
    	
    }

	@GetMapping({"/read", "/modify"})
	public void read(@RequestParam("bno") Long bno, Model model) {
		log.info("컨트롤러 ==== 글번호 ==============="+bno);
		model.addAttribute("read",service.read(bno));
	}	
	
	
	// 이런식으로 url 호출될 것을 가정하고..
	// >>> 홈페이지/spring/guest/del?bno=2	
	@GetMapping("/del")
	public String del(@RequestParam("bno") Long bno) {
		log.info("컨트롤러 ==== 글번호 ==============="+bno);
		service.del(bno);
    	service.noRe();
    	service.noRe2();
		return "redirect:/guest/getList";	//sendRedirect 로 이동하게 됨. redirect...스프링 내장객체로 응용됨
	}		
	
	@PostMapping("/modify")
	public String modify(GuestDto dto) {
		service.modify(dto);
		return "redirect:/guest/getList"; // redirect...스프링 내장객체로 응용됨
	}
	

	// >>> 홈페이지/spring/guest/write (Post 방식으로 오면 여기로 옴)	
	@PostMapping("/write")
	//폼 태그의 텍스트에어리어 태그에 btext 변수로 데이터가 넘어왔는데
	//매개변수에 (GuestVO gvo) 이런 클래스를 선언해놓게 되면
	//해당 객체의 멤버변수에 스프링이 알아서 채워줌.
	//                        ★GuestDto dto★ 이게 가장 중요함
	public String write(GuestDto dto) {
		service.write(dto);
		return "redirect:/guest/getList";	//sendRedirect 로 이동하게 됨. // 책 p.245 참고  redirect...스프링 내장객체로 응용됨
	}	
	// >>> 홈페이지/spring/guest/write (get 방식으로 오면 여기로 옴. 일반링크이동=get방식임)	
	@GetMapping("/write")	// 책 p.239 /write 중복이지만 이건 글쓰기 화면을 위한 url 매핑
	public void write() {
		
	}	
	//내용을 다 끝내고 /write로 이동하는 문법
	

	@RequestMapping("/test")
	public void test(HttpServletRequest request,Model m) {
		setCp(m,request.getContextPath());
		m.addAttribute("a","개");
		m.addAttribute("b","삵");
	}
	
	public void setCp(Model m, String cp) {
		m.addAttribute("cp",cp);
		log.info("==== 컨텍스트 패스임:"+cp);
	}
	
	@RequestMapping("/test2")
	public void test2() {
		
	}
	
	@RequestMapping("/testapi")
	public void testapi() {
		
	}
	
}