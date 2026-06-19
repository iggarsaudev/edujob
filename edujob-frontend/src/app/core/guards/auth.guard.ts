import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  // En los Guards funcionales usamos 'inject' para traer servicios
  const authService = inject(AuthService);
  const router = inject(Router);

  // Si el usuario tiene token, le abrimos la puerta
  if (authService.isLoggedIn()) {
    return true;
  }

  // Si no tiene token, lo mandamos directo al login y bloqueamos el paso
  router.navigate(['/login']);
  return false;
};
