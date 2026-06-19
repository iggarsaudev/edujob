import { HttpInterceptorFn } from '@angular/common/http';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  // 1. Buscamos el token en el cajón del navegador
  const token = localStorage.getItem('edujob_token');

  // 2. Si hay token, clonamos la petición original y le pegamos el sello de Autorización
  if (token) {
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
    // 3. Dejamos que la petición continúe su viaje hacia el Backend
    return next(clonedRequest);
  }

  // Si no hay token (ej: el usuario no se ha logueado), la petición sale tal cual
  return next(req);
};
