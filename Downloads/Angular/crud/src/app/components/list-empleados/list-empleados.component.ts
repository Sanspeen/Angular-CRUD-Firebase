import { Component, OnInit } from '@angular/core';
import { EmpleadoService } from 'src/app/services/empleado.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-list-empleados',
  templateUrl: './list-empleados.component.html',
  styleUrls: ['./list-empleados.component.css']
})
export class ListEmpleadosComponent implements OnInit {
  
  empleados:any[] = [];
  
    constructor(private _empleadoService:EmpleadoService, private toastr: ToastrService) {
   }

  ngOnInit(): void {
    this.getEmpleados();
  }

  getEmpleados(){
    this._empleadoService.getEmpleados().subscribe(data => {
      this.empleados = [];
      data.forEach((element:any) => {
        this.empleados.push({
          id: element.payload.doc.id,
          ...element.payload.doc.data()
        })
      });
      console.log(this.empleados);
    })
  }

  eliminar(id:string){
    this._empleadoService.eliminarEmpleado(id).then(() => {
      console.log('Eliminado correctamente');
      this.toastr.error('El usuario ha sido eliminado con exito!', 'Usuario eliminado', {
        positionClass:'toast-bottom-left'
      })
    }).catch(error => {
      console.log(error);
    });
  }

}
