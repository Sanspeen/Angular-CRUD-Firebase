import { Injectable } from '@angular/core';
import { AngularFirestore } from '@angular/fire/compat/firestore';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EmpleadoService {
  constructor(private firestore: AngularFirestore) {}

  agregarEmpleado(empleado: any): Promise<any> {
    return this.firestore.collection('empleado').add(empleado);
  }

  getEmpleados(): Observable<any> {
    return this.firestore
      .collection('empleado', (res) => res.orderBy('fechaCreacion', 'asc'))
      .snapshotChanges();
  }

  eliminarEmpleado(id: string): Promise<any> {
    return this.firestore.collection('empleado').doc(id).delete();
  }

  getEmpleado(id: string): Observable<any> {
    return this.firestore.collection('empleado').doc(id).snapshotChanges();
  }

  actualizarEmpleado(id: string, data: any): Promise<any> {
    return this.firestore.collection('empleado').doc(id).update(data);
  }
}
