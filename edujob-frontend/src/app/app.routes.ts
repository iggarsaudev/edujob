import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },

  // Protegemos el dashboard con nuestro guard
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard],
  },

  // Cambiamos la redirección por defecto para que vaya al dashboard
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
];
