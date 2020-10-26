package io.renren.modules.employment.controller;

import io.renren.modules.employment.service.EmploymentService;
import io.renren.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/em/employment")
public class EmploymentController extends AbstractController {

    @Autowired
    private EmploymentService employmentService;
}
