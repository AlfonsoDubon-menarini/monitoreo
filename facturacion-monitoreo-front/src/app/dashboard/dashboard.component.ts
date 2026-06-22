import { Component, OnInit, OnDestroy, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegionalMonitorService } from './regional-monitor.service';
import { ResumenRegional } from './resumen-regional.model';
import { Subscription, interval } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {
  nodos = signal<ResumenRegional[]>([]);
  filtroPais = signal<string>('TODOS');
  isLive = signal<boolean>(true);

  fechaInicio = signal<string>('');
  fechaFin = signal<string>('');

  nodosFiltrados = computed(() => {
    const paisActual = this.filtroPais();
    if (paisActual === 'TODOS') return this.nodos();
    return this.nodos().filter(nodo => nodo.pais === paisActual);
  });

  totalGlobal = computed(() =>
    this.nodosFiltrados().reduce((acc, nodo) => acc + nodo.totalProcesado, 0)
  );

  private pollingSub!: Subscription;

  constructor(private monitorService: RegionalMonitorService) {}

  ngOnInit(): void {
    this.activarTelemetria();
  }

  activarTelemetria(): void {
    this.pollingSub = interval(2000)
      .pipe(
        startWith(0),
        switchMap(() => {
          if (this.fechaInicio() || this.fechaFin()) {
            return [];
          }
          return this.isLive() ? this.monitorService.obtenerResumen() : [];
        })
      )
      .subscribe({
        next: (data) => {
          if (data && data.length > 0) this.nodos.set(data);
        },
        error: (err) => console.error('Error en telemetria en tiempo real:', err)
      });
  }

  consultarHistorico(): void {
    if (!this.fechaInicio() || !this.fechaFin()) return;

    this.isLive.set(false);

    const inicioISO = `${this.fechaInicio()}:00`;
    const finISO = `${this.fechaFin()}:00`;

    this.monitorService.obtenerResumen(inicioISO, finISO).subscribe({
      next: (data) => this.nodos.set(data),
      error: (err) => console.error('Error al consultar metricas historicas:', err)
    });
  }

  limpiarFechas(): void {
    this.fechaInicio.set('');
    this.fechaFin.set('');
    this.isLive.set(true);

    this.monitorService.obtenerResumen().subscribe({
      next: (data) => this.nodos.set(data),
      error: (err) => console.error('Error al restaurar flujo de tiempo real:', err)
    });
  }

  toggleLiveStream(): void {
    this.isLive.set(!this.isLive());
  }

  cambiarFiltro(pais: string): void {
    this.filtroPais.set(pais);
  }

  obtenerClaseEstado(latencia: number): string {
    if (latencia === 0) return 'state-down';
    return latencia > 15 ? 'state-warning' : 'state-ok';
  }

  ngOnDestroy(): void {
    if (this.pollingSub) this.pollingSub.unsubscribe();
  }
}
