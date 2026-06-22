import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OfferService } from '../../core/services/offer.service';
import { AuthService } from '../../core/services/auth.service';
import { JobOffer } from '../../core/models/job-offer.model';
import { PageResponse } from '../../core/models/page-response.model';
import { AttendanceService } from '../../core/services/attendance.service';
import { CourseService } from '../../core/services/course.service';
import { Course } from '../../core/models/course.model';

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
  myCourses: Course[] = []; // Array para guardar los cursos del alumno

  constructor(
    private offerService: OfferService,
    private authService: AuthService,
    private attendanceService: AttendanceService,
    private courseService: CourseService,
  ) {}

  ngOnInit(): void {
    this.loadOffers();
    this.userRole = this.authService.getUserRole(); // Leemos el rol al entrar
    // console.log('Rol del usuario logueado:', this.userRole); // Un chivato para la consola
    // Si el usuario es un estudiante, cargamos sus cursos
    if (this.userRole === 'STUDENT' || this.userRole === 'ROLE_STUDENT') {
      this.loadMyCourses();
    }
  }

  loadMyCourses(): void {
    this.courseService.getMyCourses().subscribe({
      next: (courses) => {
        this.myCourses = courses;
      },
      error: (err) => console.error('Error cargando mis cursos', err),
    });
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

  // Variables para nuestro Toast corporativo
  toastMessage: string | null = null;
  toastType: 'success' | 'error' = 'success';

  // Función para mostrar el Toast durante 3 segundos
  showToast(message: string, type: 'success' | 'error'): void {
    this.toastMessage = message;
    this.toastType = type;
    setTimeout(() => (this.toastMessage = null), 3000);
  }

  // Modificamos el método para que reciba el id
  registerAttendance(courseId: string): void {
    this.attendanceService.clockIn(courseId).subscribe({
      next: (response) => {
        // Buscamos el curso en nuestro array y le damos la vuelta a su estado
        const course = this.myCourses.find((c) => c.id === courseId);
        if (course) {
          course.isCheckedIn = !course.isCheckedIn;
        }

        // Mostramos el Toast correcto
        const action = course?.isCheckedIn ? 'Entrada' : 'Salida';
        this.showToast(`¡${action} registrada con éxito!`, 'success');
      },
      error: (err) => {
        const errorMessage =
          err.error?.message || 'Error de conexión al servidor.';
        this.showToast(`No se pudo fichar: ${errorMessage}`, 'error');
      },
    });
  }
}
