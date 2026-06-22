import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AttendanceResponse } from '../models/attendance.model';

@Injectable({
  providedIn: 'root',
})
export class AttendanceService {
  private apiUrl = `${environment.apiUrl}/attendance`;

  constructor(private http: HttpClient) {}

  // 1. Añadimos courseId como parámetro
  clockIn(courseId: string): Observable<AttendanceResponse> {
    // 2. Lo inyectamos en la URL exacta que espera Spring Boot
    return this.http.post<AttendanceResponse>(
      `${this.apiUrl}/${courseId}/clock-in`,
      {},
    );
  }
}
