import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = 'http://localhost:8081/api/users';
  private isLoggedInSubject = new BehaviorSubject<boolean>(false);
  private static username = '';

  constructor(private http: HttpClient) {}

  get isLoggedIn(): Observable<boolean> {
    return this.isLoggedInSubject.asObservable();
  }

  public static getUsername(): string {
    return UserService.username;
  }

  registerUser(user: any): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.apiUrl}/register`, user);
  }

  loginUser(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { username, password }).pipe(
      tap((response) => {
        if (response.status === 'loggedIn') {
          UserService.username = username;
          this.isLoggedInSubject.next(true);
        }
        else {
          console.log('Login failed');
        }
      })
    );
  }

  logoutUser(): void {
    UserService.username = '';
    this.isLoggedInSubject.next(false);
  }
}
