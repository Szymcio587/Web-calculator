import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/shared/services/user/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent {
  credentials = { username: '', password: '' };
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private fb: FormBuilder, private userService: UserService, private router: Router) {
  }

  onSubmit(): void {
    console.log(this.credentials.username);
    console.log(this.credentials.password);
      this.userService
        .resetUserPassword(this.credentials.username, this.credentials.password)
        .subscribe({
          next: (response) => {
            this.successMessage = response.message;
            this.router.navigate(['/']);
          },
          error: (err) => (this.errorMessage = err.error?.message || 'Rejestracja nie powiodła się'),
        });
  }
}
