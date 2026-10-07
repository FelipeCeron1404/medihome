package medihome;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProfesionalSalud extends Usuario implements Notificable {
    private String numeroRegistroProfesional;
    private String especialidad;
    private final List<ServicioDomiciliario> serviciosAsignados = new ArrayList<>();

    public ProfesionalSalud() {
        super();
    }

    public ProfesionalSalud(String identificacion, String nombre, String correo,
                            String numeroRegistroProfesional, String especialidad) {
        super(identificacion, nombre, correo);
        this.numeroRegistroProfesional = numeroRegistroProfesional;
        this.especialidad = especialidad;
    }

    public String getNumeroRegistroProfesional() {
        return numeroRegistroProfesional;
    }

    public void setNumeroRegistroProfesional(String numeroRegistroProfesional) {
        this.numeroRegistroProfesional = numeroRegistroProfesional;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public void notificar(String mensaje) {
        System.out.println("  [Correo a " + getCorreo() + " - " + getNombre() + "] " + mensaje);
    }

    public boolean estaDisponible(LocalDateTime fecha) {
        for (ServicioDomiciliario servicio : serviciosAsignados) {
            boolean activo = !servicio.getEstado().equals(ServicioDomiciliario.FINALIZADO)
                    && !servicio.getEstado().equals(ServicioDomiciliario.CANCELADO);
            if (activo && fecha.equals(servicio.getFechaProgramada())) {
                return false;
            }
        }
        return true;
    }

    public List<ServicioDomiciliario> getServiciosAsignados() {
        return serviciosAsignados;
    }

    void agregarServicio(ServicioDomiciliario servicio) {
        serviciosAsignados.add(servicio);
    }
}
