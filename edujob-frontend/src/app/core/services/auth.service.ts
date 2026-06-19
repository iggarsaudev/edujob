import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { jwtDecode } from 'jwt-decode';

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

  getUserRole(): string | null {
    const token = localStorage.getItem('edujob_token');
    if (!token) return null;

    try {
      const decoded: any = jwtDecode(token);

      // console.log('Token descifrado completo:', decoded);

      // el backend de spring security suele guardar el rol en un array llamado 'roles' o 'authorities'
      return decoded.roles ? decoded.roles[0] : null;
    } catch (error) {
      console.error('error descifrando el token', error);
      return null;
    }
  }
}
