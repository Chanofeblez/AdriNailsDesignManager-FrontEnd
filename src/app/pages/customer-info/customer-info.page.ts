import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Appointment } from 'src/app/models/appointment.interface';
import { AppointmentService } from 'src/app/services/appointment.service';
import { CustomerService } from 'src/app/services/customer.service';

@Component({
  selector: 'app-customer-info',
  templateUrl: './customer-info.page.html',
  styleUrls: ['./customer-info.page.scss'],
})
export class CustomerInfoPage implements OnInit {

  customer: any;
  appointments: Appointment[] = [];

  constructor(
    private route: ActivatedRoute,
    private customerService: CustomerService,
    private appointmentService: AppointmentService
  ) {}

  ngOnInit() {
    const customerId = this.route.snapshot.paramMap.get('id');
    if (customerId) {
      console.log("customerId",customerId);
      this.loadCustomerInfo(customerId);
      this.loadCustomerAppointments(customerId);
    }
  }

  loadCustomerInfo(customerId: string) {
    this.customerService.getCustomerById(customerId).subscribe(
      (customer) => {
        this.customer = customer;
        console.log("this.customer",this.customer);
      },
      (error) => {
        console.error('Error al cargar la información del cliente', error);
      }
    );
  }

  loadCustomerAppointments(customerId: string) {
    this.appointmentService.getAppointmentsByCustomerId(customerId).subscribe(
      (appointments) => {
        console.log("appointments", appointments); // Agrega este log para verificar los datos en el frontend
        // Ordenar appointments de más reciente a más antiguo
        this.appointments = appointments.sort((a, b) => {
          return new Date(b.appointmentDate).getTime() - new Date(a.appointmentDate).getTime();
        });
      },
      (error) => {
        console.error('Error al cargar los appointments del cliente', error);
      }
    );
  }


  formatAppointmentTime(time: string): string {
    const [hours, minutes] = time.split(':').map(Number); // Divide la hora y los minutos
    const suffix = hours >= 12 ? 'PM' : 'AM'; // Determina si es AM o PM
    const hours12 = hours % 12 || 12; // Convierte el formato de 24 horas a 12 horas
    return `${hours12}:${minutes.toString().padStart(2, '0')} ${suffix}`; // Formatea la salida
  }


}