package com.nailsSalon.AdriDesign.services;

import com.nailsSalon.AdriDesign.controller.AuthResponseDTO;
import com.nailsSalon.AdriDesign.customer.Customer;
import com.nailsSalon.AdriDesign.customer.CustomerRepository;
import com.nailsSalon.AdriDesign.dto.AuthLoginRequestDTO;
import com.nailsSalon.AdriDesign.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //Tenemos nuestro UserSec y necesitamos devolverlo en formato UserDetails
        //Traemos nuestro usuario de la DB
        Customer customer = customerRepository.findUserEntityByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("El usuario "+email+" no fue encontrado"));

        //Creamos una lista para los permisos
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        //Taer roles y convertirlos en SimpleGrantedAuthority
      //  customer.getRolesList()
      //          .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));

        //Traer permisos y convertirlos en SimpleGrantedAuthority
      //  customer.getRolesList().stream()
      //          .flatMap(role -> role.getPermissionsList().stream())
      //          .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName())));

        return new User(
                customer.getEmail(),
                customer.getPassword(),
                customer.isEnabled(),
                customer.isAccountNotExpired(),
                customer.isCredentialNotExpired(),
                customer.isAccountNotLocked(),
                authorityList);

    }

    public AuthResponseDTO loginUser(AuthLoginRequestDTO userRequest) {

        Customer customer = new Customer();

        //Recuperamos nombre de usuario y contraseña
        String username = userRequest.email();
        String password = userRequest.password();

        Authentication authentication = this.authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(username,"LoginOK",accessToken,true);
        return authResponseDTO;

    }

    private Authentication authenticate(String email, String password) {

        UserDetails userDetails = this.loadUserByUsername(email);

        if (userDetails==null){
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(email,userDetails.getPassword(),userDetails.getAuthorities());
    }
}