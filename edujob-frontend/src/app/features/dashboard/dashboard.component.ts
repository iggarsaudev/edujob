import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OfferService } from '../../core/services/offer.service';
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
  // Aquí guardaremos las ofertas que lleguen del Backend
  offers: JobOffer[] = [];

  constructor(private offerService: OfferService) {}

  // Este método se ejecuta automáticamente cuando la pantalla carga
  ngOnInit(): void {
    this.loadOffers();
  }

  loadOffers(): void {
    this.offerService.getOpenOffers(0, 10).subscribe({
      next: (data: PageResponse<JobOffer>) => {
        // Extraemos el "content" del sobre paginado y lo guardamos en nuestra variable
        this.offers = data.content;
      },
      error: (err) => {
        console.error('Error cargando ofertas:', err);
      },
    });
  }

  // Diccionario de traducción de estados
  translateStatus(status: string): string {
    const statusMap: { [key: string]: string } = {
      OPEN: 'ABIERTA',
      CLOSED: 'CERRADA',
      IN_PROGRESS: 'EN PROCESO',
    };
    // Si encuentra la traducción la devuelve, si no, devuelve el original
    return statusMap[status] || status;
  }
}
