import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OfferService } from '../../core/services/offer.service';
import { AuthService } from '../../core/services/auth.service';
import { JobOffer } from '../../core/models/job-offer.model';
import { PageResponse } from '../../core/models/page-response.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  offers: JobOffer[] = [];
  userRole: string | null = null; // Creamos la variable para guardar el rol

  constructor(
    private offerService: OfferService,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.loadOffers();
    this.userRole = this.authService.getUserRole(); // Leemos el rol al entrar
    // console.log('Rol del usuario logueado:', this.userRole); // Un chivato para la consola
  }

  loadOffers(): void {
    this.offerService.getOpenOffers(0, 10).subscribe({
      next: (data: PageResponse<JobOffer>) => {
        this.offers = data.content;
      },
      error: (err) => {
        console.error('Error cargando ofertas:', err);
      },
    });
  }

  translateStatus(status: string): string {
    const statusMap: { [key: string]: string } = {
      OPEN: 'ABIERTA',
      CLOSED: 'CERRADA',
      IN_PROGRESS: 'EN PROCESO',
    };
    return statusMap[status] || status;
  }
}
