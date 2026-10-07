package medihome;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        System.out.println("==== MediHome - Atención médica domiciliaria ====\n");

        Paciente paciente = new Paciente("1085123456", "María Fernanda López",
                "maria.lopez@correo.com", "3104567890", "Calle 18 # 25-40, Pasto");

        ProfesionalSalud profesional = new ProfesionalSalud("27456789", "Dr. Andrés Martínez",
                "andres.martinez@medihome.com", "RM-52-0145", "Medicina general");
        EquipoAtencion equipo = new EquipoAtencion("EQ-01", "Equipo Norte", "Comuna 9 - Pasto");
        equipo.agregarProfesional(profesional);

        System.out.println("-> Solicitud del servicio:");
        ServicioDomiciliario servicio = new ServicioDomiciliario("SD-001", paciente,
                paciente.getDireccion(), "Fiebre persistente y malestar general");

        System.out.println("\n-> Programación del servicio:");
        servicio.programar(LocalDateTime.now().withSecond(0).withNano(0));
        servicio.asignarProfesional(profesional);

        System.out.println("\n-> Inicio de la atención:");
        servicio.iniciarAtencion();
        AtencionMedica atencion = servicio.getAtencion();

        atencion.registrarMedicion(38.4, 96, 125, 82, 95.0);
        atencion.setObservaciones("Paciente febril, sin signos de dificultad respiratoria. "
                + "Faringe eritematosa.");
        atencion.setRecomendaciones("Acetaminofén 500 mg cada 8 horas por 3 días, abundante "
                + "hidratación y reposo. Volver a consultar si la fiebre continúa después de 72 horas.");

        System.out.println("\n-> Cierre del servicio:");
        servicio.finalizar();

        imprimirReporte(servicio);
    }

    private static void imprimirReporte(ServicioDomiciliario servicio) {
        Paciente paciente = servicio.getPaciente();
        ProfesionalSalud profesional = servicio.getProfesional();
        AtencionMedica atencion = servicio.getAtencion();
        String linea = "=".repeat(64);

        System.out.println("\n" + linea);
        System.out.println("            REPORTE DE ATENCIÓN MÉDICA DOMICILIARIA");
        System.out.println(linea);

        System.out.println("SERVICIO");
        System.out.println("  Código:            " + servicio.getCodigo());
        System.out.println("  Estado:            " + servicio.getEstado());
        System.out.println("  Fecha programada:  " + servicio.getFechaProgramada().format(FORMATO));
        System.out.println("  Dirección:         " + servicio.getDireccionAtencion());
        System.out.println("  Motivo:            " + servicio.getMotivo());

        System.out.println("\nPACIENTE");
        System.out.println("  Identificación:    " + paciente.getIdentificacion());
        System.out.println("  Nombre:            " + paciente.getNombre());
        System.out.println("  Correo:            " + paciente.getCorreo());
        System.out.println("  Teléfono:          " + paciente.getTelefono());

        System.out.println("\nPROFESIONAL DE LA SALUD");
        System.out.println("  Nombre:            " + profesional.getNombre());
        System.out.println("  Especialidad:      " + profesional.getEspecialidad());
        System.out.println("  Registro prof.:    " + profesional.getNumeroRegistroProfesional());

        System.out.println("\nATENCIÓN MÉDICA");
        System.out.println("  Inicio:            " + atencion.getFechaHoraInicio().format(FORMATO));
        System.out.println("  Finalización:      " + atencion.getFechaHoraFin().format(FORMATO));
        System.out.println("  Observaciones:     " + atencion.getObservaciones());
        System.out.println("  Recomendaciones:   " + atencion.getRecomendaciones());

        System.out.println("\nSIGNOS VITALES (" + atencion.getMediciones().size() + " medición/es)");
        for (MedicionSignosVitales m : atencion.getMediciones()) {
            System.out.println("  Fecha y hora:      " + m.getFechaHora().format(FORMATO));
            System.out.println("  Temperatura:       " + m.getTemperatura() + " °C");
            System.out.println("  Frec. cardiaca:    " + m.getFrecuenciaCardiaca() + " lpm");
            System.out.println("  Presión arterial:  " + m.getPresionSistolica() + "/"
                    + m.getPresionDiastolica() + " mmHg");
            System.out.println("  Saturación O2:     " + m.getSaturacionOxigeno() + " %");
        }
        System.out.println(linea);
    }
}
