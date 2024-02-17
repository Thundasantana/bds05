package com.devsuperior.movieflix.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.UserRepository;

//Classe que adiciona dados ao token, como id e nome do usuário por exemplo.
@Component
public class JwtTokenEnhancer implements TokenEnhancer {

	@Autowired
	private UserRepository userRepository;

	@Override
	public OAuth2AccesToken enhance(OAuth2AccesToken accesToken, OAuth2Authentication authentication) {
		User user = userRepository.findByEmail(authentication.getName());
		
		Map<String, Object> map = new HashMap<>();
		map.put("UserId", user.getId());
		
		DefaultOAth2AccesToken token = (DefaultOAth2AccesToken) accesToken;
		token.setAdditionalInformation(map);
		
		return accesToken;
	}
}

