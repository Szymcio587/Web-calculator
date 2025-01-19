import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserService } from 'src/app/shared/services/user/user.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent {
    credentials = { username: '', password: '' };
    errorMessage = '';
    successMessage = '';
    buttonClicked: string = '';

    constructor(private http: HttpClient, private router: Router, private userService: UserService) {}

    onLogin() {
      if(this.buttonClicked == 'login') {
        this.userService.loginUser(this.credentials.username, this.credentials.password).subscribe({
          next: (response) => {
            this.successMessage = response.message;
            this.router.navigate(['/home']);
          },
          error: (err) => {
              this.errorMessage = err.error?.message || 'Registration failed';
          },
      });
      }
      else if(this.buttonClicked == 'enter') {
        this.userService.logoutUser();
        this.router.navigate(['/home']);
      }
    }
}
