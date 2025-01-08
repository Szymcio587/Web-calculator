import { Component} from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../services/user/user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  isLoggedIn: Observable<boolean>;

  constructor(private userService: UserService, private router: Router, private http: HttpClient) {
    this.isLoggedIn = this.userService.isLoggedIn;
    this.isLoggedIn.subscribe((loggedIn) => {
      console.log('Is user logged in?', loggedIn);
    });
  }

  onLogout(): void {
    this.userService.logoutUser();
    this.router.navigate(['/']);
  }

  isLinkVisible(): boolean {
    return this.router.url !== '/';
  }

  getResults() {
    this.http.post<number>('http://localhost:8081/history/', UserService.getUsername()).subscribe(
      (result: number) => {
        this.router.navigate(['/results-history']);
      },
      (error: any) => {
        console.error("Error:", error);
      }
    );
  }

}
