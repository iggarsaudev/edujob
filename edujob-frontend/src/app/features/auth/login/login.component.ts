import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule], // Importamos los módulos necesarios
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
  ) {
    // Inicializamos el formulario con validaciones básicas en el cliente
    this.loginForm = this.fb.group({
      dni: ['', [Validators.required]],
      pin: ['', [Validators.required, Validators.minLength(4)]],
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const { dni, pin } = this.loginForm.value;

      this.authService.login(dni, pin).subscribe({
        next: (response) => {
          console.log('¡Login correcto!', response);
          this.errorMessage = '';
          this.router.navigate(['/']);
        },
        error: (err) => {
          console.error(err);

          // Capturamos el mensaje del backend
          let backendMessage =
            err.error?.message ||
            'Error al iniciar sesión. Comprueba tus credenciales.';

          // Interceptamos el mensaje técnico de Spring Security y lo traducimos
          if (backendMessage === 'Bad credentials') {
            this.errorMessage =
              'DNI o PIN incorrecto. Por favor, inténtalo de nuevo.';
          } else {
            this.errorMessage = backendMessage;
          }
        },
      });
    }
  }
}
