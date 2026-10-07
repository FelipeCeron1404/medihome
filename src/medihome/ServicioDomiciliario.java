package medihome;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServicioDomiciliario {
    public static final String SOLICITADO = "Solicitado";
    public static final String PROGRAMADO = "Programado";
    public static final String EN_ATENCION = "En atención";
    public static final String FINALIZADO = "Finalizado";
    public static final String CANCELADO = "Cancelado";

    private String codigo;
    private LocalDateTime fechaProgramada;
    private String direccionAtencion;
    private String motivo;
    private String estado;

    private Paciente paciente;
    private ProfesionalSalud profesional;
    private AtencionMedica atencion;

    public ServicioDomiciliario() {
        this.estado = SOLICITADO;
    }

    public ServicioDomiciliario(String codigo, Paciente paciente,
                                String direccionAtencion, String motivo) {
        this.codigo = codigo;
        this.paciente = paciente;
        this.direccionAtencion = direccionAtencion;
        this.motivo = motivo;
        this.estado = SOLICITADO;
        paciente.agregarServicio(this);
        paciente.notificar("Su servicio " + codigo + " fue registrado con estado " + estado + ".");
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public String getDireccionAtencion() {
        return direccionAtencion;
    }

    public void setDireccionAtencion(String direccionAtencion) {
        this.direccionAtencion = direccionAtencion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public ProfesionalSalud getProfesional() {
        return profesional;
    }

    public AtencionMedica getAtencion() {
        return atencion;
    }

    public void programar(LocalDateTime fecha) {
        validarEstado(SOLICITADO, "programar");
        this.fechaProgramada = fecha;
    }

    public void asignarProfesional(ProfesionalSalud profesional) {
        validarEstado(SOLICITADO, "asignar profesional a");
        if (fechaProgramada == null) {
            throw new IllegalStateException("Primero se debe programar la fecha del servicio " + codigo + ".");
        }
        if (!profesional.estaDisponible(fechaProgramada)) {
            throw new IllegalStateException(profesional.getNombre() + " no está disponible en esa fecha.");
        }
        this.profesional = profesional;
        profesional.agregarServicio(this);
        this.estado = PROGRAMADO;
        paciente.notificar("Su servicio " + codigo + " fue programado para "
                + fechaProgramada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                + " con " + profesional.getNombre() + ".");
        profesional.notificar("Se le asignó el servicio " + codigo + " en " + direccionAtencion + ".");
    }

    public void iniciarAtencion() {
        validarEstado(PROGRAMADO, "iniciar la atención de");
        this.atencion = new AtencionMedica(LocalDateTime.now());
        this.estado = EN_ATENCION;
        paciente.notificar("El profesional inició la atención del servicio " + codigo + ".");
    }

    public void finalizar() {
        validarEstado(EN_ATENCION, "finalizar");
        atencion.setFechaHoraFin(LocalDateTime.now());
        this.estado = FINALIZADO;
        paciente.notificar("El servicio " + codigo + " fue finalizado. Revise sus recomendaciones.");
    }

    public void cancelar() {
        if (estado.equals(EN_ATENCION) || estado.equals(FINALIZADO) || estado.equals(CANCELADO)) {
            throw new IllegalStateException("No se puede cancelar un servicio en estado " + estado + ".");
        }
        this.estado = CANCELADO;
        paciente.notificar("El servicio " + codigo + " fue cancelado.");
        if (profesional != null) {
            profesional.notificar("El servicio " + codigo + " fue cancelado.");
        }
    }

    private void validarEstado(String esperado, String accion) {
        if (!estado.equals(esperado)) {
            throw new IllegalStateException("No se puede " + accion + " el servicio " + codigo
                    + " porque está en estado " + estado + ".");
        }
    }
}
