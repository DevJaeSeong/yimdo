package egovframework.web.admin.elementList.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/elementList")
public class ElementListController {
	
	@GetMapping("/elementListPage.do")
	public String elementListPage() {
		
		return "admin/elementList";
	}
}
