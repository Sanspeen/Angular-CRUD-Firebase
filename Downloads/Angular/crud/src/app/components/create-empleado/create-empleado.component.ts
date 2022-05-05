import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { EmpleadoService } from 'src/app/services/empleado.service';

@Component({
  selector: 'app-create-empleado',
  templateUrl: './create-empleado.component.html',
  styleUrls: ['./create-empleado.component.css'],
})
export class CreateEmpleadoComponent implements OnInit {
  createEmpleado: FormGroup;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private empleadoService: EmpleadoService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.createEmpleado = this.formBuilder.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      documento: ['', Validators.required],
      salario: ['', Validators.required],
    });
  }

  ngOnInit(): void {}

  agregarEmpleado() {
    if (this.createEmpleado.invalid) {
      return;
    }

    const empleado: any = {
      nombre: this.createEmpleado.value.nombre,
      apellido: this.createEmpleado.value.apellido,
      documento: this.createEmpleado.value.documento,
      salario: this.createEmpleado.value.salario,
      fechaCreacion: new Date(),
      fechaActualizacion: new Date(),
    };

    this.submitted = true;

    this.empleadoService
      .agregarEmpleado(empleado)
      .then(() => {
        this.toastr.success('¡El empleado ha sido registrado con exito!', 'Empleado registrado.', {
          positionClass:'toast-bottom-right'
        })
        this.router.navigate(['/list-empleados']);
      })
      .catch((error) => {
        console.log(error);
      });
  }
}
