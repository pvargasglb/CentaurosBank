/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import modelo.entidad.Cliente;
import modelo.dao.ClienteDao;
import modelo.util.MessageUtil;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author paola.vargas
 */
@Named(value = "clienteControl")
@ViewScoped
public class ClienteControl implements Serializable {

    /**
     * Creates a new instance of ClienteControl
     */
    private List<Cliente> listaClientes;
    private Cliente cliente;
    private String departamento;
    private String ciudad;
    private Map<String, String> departamentos;
    private Map<String, String> ciudades;
    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    private UIInput inputBox;
    private String idCliente;

    @PostConstruct
    public void init() {
        departamentos = new HashMap<String, String>();
        departamentos.put("Antioquia", "Antioquia");
        departamentos.put("Cundinamarca", "Cundinamarca");

        Map<String, String> map = new HashMap<String, String>();
        map.put("Medellin", "Medellin");
        map.put("Itagui", "Itagui");
        data.put("Antioquia", map);

        map = new HashMap<String, String>();
        map.put("Tocaima", "Tocaima");
        data.put("Cundinamarca", map);
        
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
        idCliente = paramMap.get("idcliente");
    }

    public void preRenderInput() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            inputBox.setValue("");
        }
    }

    private void refresh() {
        departamento = "";
        ciudad = "";
        cliente = new Cliente();
    }

    public ClienteControl() {
        cliente = new Cliente();
    }

    public List<Cliente> getListaClientes() {
        ClienteDao ad = new ClienteDao();
        listaClientes = ad.listarClientes();
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Map<String, String> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(Map<String, String> departamentos) {
        this.departamentos = departamentos;
    }

    public Map<String, String> getCiudades() {
        return ciudades;
    }

    public void setCiudades(Map<String, String> ciudades) {
        this.ciudades = ciudades;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    public UIInput getInputBox() {
        return inputBox;
    }

    public void setInputBox(UIInput inputBox) {
        this.inputBox = inputBox;
    }

    public void limpiarCliente() {
        cliente = new Cliente();
    }
    
     public void obtenerCliente() {
        ClienteDao ad = new ClienteDao();
        cliente = ad.obtenerCliente(idCliente);
    }

    public void agregarCliente() {
        String contrasena = DigestUtils.md5Hex(cliente.getContrasena());
        cliente.setContrasena(contrasena);
        cliente.setDepartamento(departamento);
        cliente.setCiudad(ciudad);
        cliente.setEstado("ACTIVO");
        cliente.setUsuario(cliente.getIdCliente());
        try {
            ClienteDao ad = new ClienteDao();
            ad.agregar(cliente);
            refresh();
            MessageUtil.info("Cliente almacenado con exito!");
        } catch (Exception e) {
            MessageUtil.error("Ocurrió un error: " + e);
        }

    }
    
    public void validarLogin() throws IOException {
        ClienteDao ad = new ClienteDao();
        String contrasena = DigestUtils.md5Hex(cliente.getContrasena());
        Cliente clienteLog = ad.validarLogin(cliente.getUsuario(), contrasena);
        if (clienteLog!=null){
            MessageUtil.info("Bienvenido!");     
            FacesContext.getCurrentInstance().getExternalContext().redirect("inicioUsuario.xhtml?idcliente="+clienteLog.getIdCliente());
        } else {
             MessageUtil.error("No estás registrado, por favor crea tu usuario primero!");
        }
    }

    public void modificarCliente() {
        ClienteDao ad = new ClienteDao();
        ad.modificar(cliente);
        limpiarCliente();
    }

    public void eliminarCliente() {
        ClienteDao ad = new ClienteDao();
        ad.eliminar(cliente);
        limpiarCliente();
    }

    public void cambiarDepartamento() {

        if (departamento != null && !departamento.equals("")) {
            ciudades = data.get(departamento);
        } else {
            ciudades = new HashMap<String, String>();
        }
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }
    

}
