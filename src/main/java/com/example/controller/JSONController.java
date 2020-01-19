package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.User;
import com.example.service.ArticleDetailService;
import com.example.service.RegisterUserService;

/**
 * JSON形式で受け取るコントローラー.
 * 
 * @author iidashuhei
 *
 */
@RestController
public class JSONController {

	@Autowired
	private RegisterUserService service;
	
	@Autowired
	private ArticleDetailService detailService;

	/**
	 * メールを返す.
	 * 
	 * @param email メール
	 * @return メールアドレス
	 */
	@RequestMapping("/judge")
	public User emailReturn(String email) {
		User user = service.findByEmail(email);
		if(user == null) {
			user = new User();
		}
		return user;
	}
	
	/**
	 * グッドを返す.
	 * 
	 * @param good グッド
	 * @return グッドを返す
	 */
	@RequestMapping("/good")
	public Integer update(Integer good) {
		if(good == null) {
			good = 1;
		} else {
			good += 1;
		}
		detailService.update(good);
		return good;
	}
}
