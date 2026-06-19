import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PageResponse } from '../models/page-response.model';
import { JobOffer } from '../models/job-offer.model';

@Injectable({
  providedIn: 'root',
})
export class OfferService {
  private apiUrl = `${environment.apiUrl}/offers`;

  constructor(private http: HttpClient) {}

  // Pide las ofertas abiertas. Por defecto pedirá la página 0 con 10 resultados.
  getOpenOffers(
    page: number = 0,
    size: number = 10,
  ): Observable<PageResponse<JobOffer>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<JobOffer>>(this.apiUrl, { params });
  }
}
