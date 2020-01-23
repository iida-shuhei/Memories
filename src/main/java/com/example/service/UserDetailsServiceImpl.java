package com.example.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.domain.LoginUser;
import com.example.domain.User;
import com.example.repository.UserRepository;

/**
 * ログイン後のユーザーに権限情報を付与するためのサービスクラス.
 * @author taro
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	/**
	 * DBから情報を得るためのリポジトリ
	 */
	@Autowired
	private UserRepository userRepository;
	
	/**
	 *ログインを行うと同時に注文テーブルのユーザーIDを更新する。
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
		System.out.println(0);
		System.out.println(email);
		User user = userRepository.findByEmail(email);
		System.out.println(1);
		if(user == null) {
			System.out.println(2);
			throw new UsernameNotFoundException("そのメールアドレスは登録されていません。");
		}
		Collection<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
		LoginUser loginUser = new LoginUser(user,authorityList);
		return loginUser;
	}
	
}