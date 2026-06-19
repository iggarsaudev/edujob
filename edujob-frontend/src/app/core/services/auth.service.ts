import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {}

  login(dni: string, pin: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { dni, pin }).pipe(
      tap((response) => {
        if (response && response.token) {
          localStorage.setItem('edujob_token', response.token);
        }
      }),
    );
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('edujob_token');
  }

  logout(): void {
    localStorage.removeItem('edujob_token');
  }
}
