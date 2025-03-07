import { Component} from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../services/user/user.service';
import { ResultDataService } from '../services/result/result-data.service';
import { BaseData } from '../data/data.interface';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  isLoggedIn: Observable<boolean>;

  constructor(private userService: UserService, private router: Router, private http: HttpClient,
    private resultDataService: ResultDataService) {
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
    this.http.post<BaseData[]>('http://localhost:8081/history', UserService.getUsername()).subscribe(
      (result: BaseData[]) => {
        ResultDataService.SetBaseData(result);
        this.router.navigate(['/results-history']);
      },
      (error: any) => {
        console.error("Error:", error);
      }
    );
  }

}
