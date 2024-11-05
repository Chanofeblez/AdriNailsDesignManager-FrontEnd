/*
  Authors : initappz (Rahul Jograna)
  Website : https://initappz.com/
  App Name : Salon - 2 This App Template Source code is licensed as per the
  terms found in the Website https://initappz.com/license
  Copyright and Good Faith Purchasers © 2023-present initappz.
*/
import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
})
export class LoginPage implements OnInit {
  email: string = '';
  password: string = '';
  passwordView: boolean = false;
  errorMessage: string = '';

  private authService = inject(AuthService);
  private router = inject(Router);

  constructor() {}

  ngOnInit() {}

  togglePassword() {
    this.passwordView = !this.passwordView;
  }

  onLogin() {
    if (!this.authService.login(this.email, this.password)) {
        console.log('Credenciales incorrectas');
        this.errorMessage = 'Credenciales incorrectas';
        // Muestra un mensaje de error aquí, si es necesario
    }
}

}
