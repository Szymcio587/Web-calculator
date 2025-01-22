import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { BaseData } from 'src/app/shared/data/data.interface';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';
import { UserService } from 'src/app/shared/services/user/user.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent {

  isLoggedIn: Observable<boolean>;

  constructor(private router: Router, private http: HttpClient, private userService: UserService) {
    this.isLoggedIn = this.userService.isLoggedIn;
    this.isLoggedIn.subscribe((loggedIn) => {
      console.log('Is user logged in?', loggedIn);
    });
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
