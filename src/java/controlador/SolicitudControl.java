/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import modelo.dao.ClienteDao;
import modelo.entidad.Solicitudes;
import modelo.dao.SolicitudesDao;
import modelo.entidad.Cliente;
import modelo.util.EmailService;
import modelo.util.MessageUtil;

/**
 *
 * @author paola.vargas
 */
@Named(value = "solicitudesControl")
@ViewScoped
public class SolicitudControl implements Serializable {

    /**
     * Creates a new instance of SolicitudControl
     */
    private List<Solicitudes> listaSolicitudess;
    private Solicitudes solicitudes;
    private Cliente cliente;
    private UIInput inputBox;
    private String idCliente;
    
    public SolicitudControl() {
        solicitudes = new Solicitudes();
        cliente = new Cliente();
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
        idCliente = paramMap.get("idcliente");
    }

    public List<Solicitudes> getListaSolicitudess() {
        SolicitudesDao ad = new SolicitudesDao();
        listaSolicitudess = ad.listarSolicitudes();
        return listaSolicitudess;
    }

    public void setListaSolicitudess(List<Solicitudes> listaSolicitudess) {
        this.listaSolicitudess = listaSolicitudess;
    }

    public Solicitudes getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(Solicitudes solicitudes) {
        this.solicitudes = solicitudes;
    }

    public void limpiarSolicitudes() {
        solicitudes = new Solicitudes();
    }

    public void agregarSolicitudes() {
        try {
            ClienteDao ad = new ClienteDao();
            cliente = ad.obtenerCliente(idCliente);
            SolicitudesDao adSol = new SolicitudesDao();
            solicitudes.setTipoSolicitud("TARJETA_CREDITO");
            solicitudes.setIdCliente(idCliente);
            solicitudes.setEstado("CREADA");
            solicitudes.setFechaSolicitud(new Date());
            adSol.agregar(solicitudes);
            EmailService emailService= new EmailService();
            emailService.enviarEmail(cliente.getCorreo(), "Solicitud Aprobada", "Su solicitud fue aprobada con exito");
            MessageUtil.info("Solicitud almacenada con exito! En breve recibirás un correo con la respuesta a tu solicitud");
            FacesContext.getCurrentInstance().getExternalContext().redirect("inicioUsuario.xhtml?idcliente="+idCliente);

        } catch (Exception e) {
            MessageUtil.error("Ocurrió un error: " + e);
        }
    }
    
    public void preRenderInput() {
        if(!FacesContext.getCurrentInstance().isPostback()) {
            inputBox.setValue("");
        }
}

 

    public void modificarSolicitudes() {
        SolicitudesDao ad = new SolicitudesDao();
        ad.modificar(solicitudes);
        limpiarSolicitudes();
    }

    public void eliminarSolicitudes() {
        SolicitudesDao ad = new SolicitudesDao();
        ad.eliminar(solicitudes);
        limpiarSolicitudes();
    }

  

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    
}
