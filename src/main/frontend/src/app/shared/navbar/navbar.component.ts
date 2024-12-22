import { Component} from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  isLoggedIn: Observable<boolean>;

  constructor(private userService: UserService, private router: Router) {
    this.isLoggedIn = this.userService.isLoggedIn;
    this.isLoggedIn.subscribe((loggedIn) => {
      console.log('Is user logged in?', loggedIn);
    });
  }

  onLogout(): void {
    this.userService.logoutUser();
    this.router.navigate(['/']);
  }

}
