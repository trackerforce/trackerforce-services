package com.trackerforce.session.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(methods = { RequestMethod.POST })
@RestController
@RequestMapping("session")
public class SessionController {

}
