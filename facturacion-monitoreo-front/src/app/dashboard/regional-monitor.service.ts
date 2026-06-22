import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ResumenRegional } from './resumen-regional.model';

@Injectable({
  providedIn: 'root'
})
export class RegionalMonitorService {
  private apiUrl = 'http://localhost:8081/api/v1/estado-regional/resumen';

  constructor(private http: HttpClient) {}

  obtenerResumen(fechaInicio?: string, fechaFin?: string): Observable<ResumenRegional[]> {
    let params = new HttpParams();
    if (fechaInicio) params = params.set('fechaInicio', fechaInicio);
    if (fechaFin) params = params.set('fechaFin', fechaFin);

    return this.http.get<ResumenRegional[]>(this.apiUrl, { params });
  }
}
