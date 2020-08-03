/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import modelo.entidad.Cliente;
import modelo.util.HibernateUtil;
import org.hibernate.Query;

/**
 *
 * @author paola.vargas
 */

public class ClienteDao {
 public List<Cliente> listarClientes() {
        List<Cliente> lista = null;
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction t = sesion.beginTransaction();
        String hql = "FROM Cliente";
        try {
            lista = sesion.createQuery(hql).list();
            t.commit();
            sesion.close();
        } catch (Exception e) {
            t.rollback();
        }
        return lista;
    }
 
 

    public void agregar(Cliente cliente) {
        Session sesion = null;
        try {
            sesion = HibernateUtil.getSessionFactory().openSession();
            sesion.beginTransaction();
            sesion.save(cliente);
            sesion.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            sesion.getTransaction().rollback();
            throw e;
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }
    }
    
    public Cliente validarLogin(String usuario, String contrasena) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction t = sesion.beginTransaction();
        Query hql = sesion.createQuery("FROM Cliente WHERE usuario=:usuario AND contrasena=:contrasena");
        hql.setParameter("usuario", usuario);
        hql.setParameter("contrasena", contrasena);

        try {
            Cliente cliente = (Cliente) hql.uniqueResult();
            t.commit();
            sesion.close();
            if (cliente != null) {
                return cliente;
            }
        } catch (Exception e) {
            t.rollback();
             return null;
        }
        return null;
    }
    
    
        public Cliente obtenerCliente(String id_cliente ) {
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction t = sesion.beginTransaction();
        Query hql = sesion.createQuery("FROM Cliente WHERE id_cliente=:idcliente");
        hql.setParameter("idcliente", id_cliente );
        try {
            Cliente cliente = (Cliente) hql.uniqueResult();
            t.commit();
            sesion.close();
            if (cliente != null) {
                return cliente;
            }
        } catch (Exception e) {
            t.rollback();
             return null;
        }
        return null;
    }

    public void modificar(Cliente cliente) {
        Session sesion = null;
        try {
            sesion = HibernateUtil.getSessionFactory().openSession();
            sesion.beginTransaction();
            sesion.update(cliente);
            sesion.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sesion.getTransaction().rollback();
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }
    }

    public void eliminar(Cliente cliente) {
        Session sesion = null;
        try {
            sesion = HibernateUtil.getSessionFactory().openSession();
            sesion.beginTransaction();
            sesion.delete(cliente);
            sesion.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sesion.getTransaction().rollback();
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }
    }
}